package com.dinzio.zendo.features.backup.data.repository

import android.content.Intent
import android.net.Uri
import androidx.room.withTransaction
import com.dinzio.zendo.core.data.local.ZenDoDatabase
import com.dinzio.zendo.features.backup.data.local.LocalBackupDataSource
import com.dinzio.zendo.features.backup.data.remote.DriveBackupDataSource
import com.dinzio.zendo.features.backup.domain.model.BackupData
import com.dinzio.zendo.features.backup.domain.repository.BackupRepository
import com.dinzio.zendo.features.category.data.local.dao.CategoryDao
import com.dinzio.zendo.features.task.data.local.dao.TaskDao
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import com.google.gson.JsonParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class BackupRepositoryImpl @Inject constructor(
    private val database: ZenDoDatabase,
    private val taskDao: TaskDao,
    private val categoryDao: CategoryDao,
    private val localBackupDataSource: LocalBackupDataSource,
    private val driveBackupDataSource: DriveBackupDataSource
) : BackupRepository {

    private val gson = Gson()

    override suspend fun exportToFile(uri: Uri): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val jsonString = serializeBackupData()
            localBackupDataSource.writeToFile(uri, jsonString)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    override suspend fun importFromFile(uri: Uri): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val readResult = localBackupDataSource.readFromFile(uri)
            readResult.fold(
                onSuccess = { jsonString -> deserializeAndImport(jsonString) },
                onFailure = { Result.failure(it) }
            )
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    override suspend fun backupToDrive(userEmail: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val jsonString = serializeBackupData()
            driveBackupDataSource.uploadToDrive(jsonString, userEmail)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    override suspend fun restoreFromDrive(userEmail: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val downloadResult = driveBackupDataSource.downloadFromDrive(userEmail)
            downloadResult.fold(
                onSuccess = { jsonString -> deserializeAndImport(jsonString) },
                onFailure = { Result.failure(it) }
            )
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    override suspend fun getAuthIntent(userEmail: String): Intent? {
        return driveBackupDataSource.getAuthIntent(userEmail)
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
}
