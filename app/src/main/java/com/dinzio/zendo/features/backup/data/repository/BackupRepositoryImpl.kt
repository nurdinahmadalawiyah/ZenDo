package com.dinzio.zendo.features.backup.data.repository

import android.content.Intent
import android.net.Uri
import androidx.room.withTransaction
import com.dinzio.zendo.core.data.local.ZenDoDatabase
import com.dinzio.zendo.features.backup.data.local.LocalBackupDataSource
import com.dinzio.zendo.features.backup.data.local.BackupMetadataDataSource
import com.dinzio.zendo.features.backup.data.remote.DriveBackupDataSource
import com.dinzio.zendo.features.backup.domain.model.BackupData
import com.dinzio.zendo.features.backup.domain.model.BackupMetadata
import com.dinzio.zendo.features.backup.domain.repository.BackupRepository
import com.dinzio.zendo.features.category.data.local.dao.CategoryDao
import com.dinzio.zendo.features.task.data.local.dao.TaskDao
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import com.google.gson.JsonParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class BackupRepositoryImpl @Inject constructor(
    private val database: ZenDoDatabase,
    private val taskDao: TaskDao,
    private val categoryDao: CategoryDao,
    private val localBackupDataSource: LocalBackupDataSource,
    private val backupMetadataDataSource: BackupMetadataDataSource,
    private val driveBackupDataSource: DriveBackupDataSource
) : BackupRepository {

    private val gson = Gson()

    override suspend fun exportToFile(uri: Uri): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val jsonString = serializeBackupData()
            localBackupDataSource.writeToFile(uri, jsonString).onSuccess {
                saveSuccessfulBackupMetadata(
                    userEmail = null,
                    operation = OPERATION_LOCAL_BACKUP,
                    sizeBytes = jsonString.toByteArray(Charsets.UTF_8).size.toLong(),
                    statusMessage = "Local backup saved successfully"
                )
            }.onFailure { error ->
                saveFailedOperationStatus(userEmail = null, operation = OPERATION_LOCAL_BACKUP, error = error)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            saveFailedOperationStatus(userEmail = null, operation = OPERATION_LOCAL_BACKUP, error = e)
            Result.failure(e)
        }
    }

    override suspend fun importFromFile(uri: Uri): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val readResult = localBackupDataSource.readFromFile(uri)
            readResult.fold(
                onSuccess = { jsonString ->
                    deserializeAndImport(jsonString).onSuccess {
                        backupMetadataDataSource.saveOperationStatus(
                            accountEmail = null,
                            operation = OPERATION_LOCAL_RESTORE,
                            isSuccess = true,
                            statusMessage = "Local backup restored successfully"
                        )
                    }.onFailure { error ->
                        saveFailedOperationStatus(userEmail = null, operation = OPERATION_LOCAL_RESTORE, error = error)
                    }
                },
                onFailure = {
                    saveFailedOperationStatus(userEmail = null, operation = OPERATION_LOCAL_RESTORE, error = it)
                    Result.failure(it)
                }
            )
        } catch (e: Exception) {
            e.printStackTrace()
            saveFailedOperationStatus(userEmail = null, operation = OPERATION_LOCAL_RESTORE, error = e)
            Result.failure(e)
        }
    }

    override suspend fun backupToDrive(userEmail: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val jsonString = serializeBackupData()
            driveBackupDataSource.uploadToDrive(jsonString, userEmail).onSuccess {
                saveSuccessfulBackupMetadata(
                    userEmail = userEmail,
                    operation = OPERATION_CLOUD_BACKUP,
                    sizeBytes = jsonString.toByteArray(Charsets.UTF_8).size.toLong(),
                    statusMessage = "Cloud backup completed successfully"
                )
            }.onFailure { error ->
                saveFailedOperationStatus(userEmail = userEmail, operation = OPERATION_CLOUD_BACKUP, error = error)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            saveFailedOperationStatus(userEmail = userEmail, operation = OPERATION_CLOUD_BACKUP, error = e)
            Result.failure(e)
        }
    }

    override suspend fun restoreFromDrive(userEmail: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val downloadResult = driveBackupDataSource.downloadFromDrive(userEmail)
            downloadResult.fold(
                onSuccess = { jsonString ->
                    deserializeAndImport(jsonString).onSuccess {
                        backupMetadataDataSource.saveOperationStatus(
                            accountEmail = userEmail,
                            operation = OPERATION_CLOUD_RESTORE,
                            isSuccess = true,
                            statusMessage = "Cloud backup restored successfully"
                        )
                    }.onFailure { error ->
                        saveFailedOperationStatus(userEmail = userEmail, operation = OPERATION_CLOUD_RESTORE, error = error)
                    }
                },
                onFailure = {
                    saveFailedOperationStatus(userEmail = userEmail, operation = OPERATION_CLOUD_RESTORE, error = it)
                    Result.failure(it)
                }
            )
        } catch (e: Exception) {
            e.printStackTrace()
            saveFailedOperationStatus(userEmail = userEmail, operation = OPERATION_CLOUD_RESTORE, error = e)
            Result.failure(e)
        }
    }

    override suspend fun getAuthIntent(userEmail: String): Intent? {
        return driveBackupDataSource.getAuthIntent(userEmail)
    }

    override fun observeBackupMetadata(userEmail: String?): Flow<BackupMetadata> {
        return backupMetadataDataSource.observeMetadata(userEmail)
    }

    private suspend fun serializeBackupData(): String {
        val tasks = taskDao.getAllTasksSync()
        val categories = categoryDao.getAllCategoriesSync()

        val backupData = BackupData(
            tasks = tasks,
            categories = categories
        )

        return gson.toJson(backupData)
    }

    private suspend fun deserializeAndImport(jsonString: String): Result<Unit> {
        return try {
            if (jsonString.isEmpty()) {
                return Result.failure(Exception("Data kosong"))
            }

            val backupData = validateAndParseBackupData(jsonString)

            database.withTransaction {
                taskDao.deleteAll()
                categoryDao.deleteAll()

                categoryDao.insertAll(backupData.categories)
                taskDao.insertAll(backupData.tasks)
            }

            Result.success(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    private fun validateAndParseBackupData(jsonString: String): BackupData {
        val root = try {
            JsonParser.parseString(jsonString).asJsonObject
        } catch (e: Exception) {
            throw JsonParseException("Format JSON tidak valid", e)
        }

        validateRootPayload(root)

        val backupData = gson.fromJson(root, BackupData::class.java)
            ?: throw JsonParseException("Format JSON tidak valid")

        val categoryIds = backupData.categories.map { it.id }.toSet()
        val invalidTask = backupData.tasks.firstOrNull { task ->
            task.categoryId != null && task.categoryId !in categoryIds
        }

        if (invalidTask != null) {
            throw IllegalArgumentException(
                "Backup tidak valid: task ${invalidTask.id} memiliki categoryId ${invalidTask.categoryId} yang tidak ditemukan"
            )
        }

        return backupData
    }

    private fun validateRootPayload(root: JsonObject) {
        if (!root.has("tasks") || !root.get("tasks").isJsonArray) {
            throw JsonParseException("Backup tidak valid: field 'tasks' tidak ditemukan")
        }

        if (!root.has("categories") || !root.get("categories").isJsonArray) {
            throw JsonParseException("Backup tidak valid: field 'categories' tidak ditemukan")
        }
    }

    private suspend fun saveSuccessfulBackupMetadata(
        userEmail: String?,
        operation: String,
        sizeBytes: Long,
        statusMessage: String
    ) {
        backupMetadataDataSource.saveSuccessfulBackupMetadata(
            accountEmail = userEmail,
            timestamp = System.currentTimeMillis(),
            sizeBytes = sizeBytes,
            operation = operation,
            statusMessage = statusMessage
        )
    }

    private suspend fun saveFailedOperationStatus(
        userEmail: String?,
        operation: String,
        error: Throwable
    ) {
        backupMetadataDataSource.saveOperationStatus(
            accountEmail = userEmail,
            operation = operation,
            isSuccess = false,
            statusMessage = error.message ?: "Unknown error"
        )
    }

    companion object {
        private const val OPERATION_LOCAL_BACKUP = "LOCAL_BACKUP"
        private const val OPERATION_LOCAL_RESTORE = "LOCAL_RESTORE"
        private const val OPERATION_CLOUD_BACKUP = "CLOUD_BACKUP"
        private const val OPERATION_CLOUD_RESTORE = "CLOUD_RESTORE"
    }
}
