package com.dinzio.zendo.features.backup.data.repository

import android.content.Intent
import android.net.Uri
import com.dinzio.zendo.features.backup.data.local.LocalBackupDataSource
import com.dinzio.zendo.features.backup.data.remote.DriveBackupDataSource
import com.dinzio.zendo.features.backup.domain.model.BackupData
import com.dinzio.zendo.features.backup.domain.repository.BackupRepository
import com.dinzio.zendo.features.category.data.local.dao.CategoryDao
import com.dinzio.zendo.features.task.data.local.dao.TaskDao
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class BackupRepositoryImpl @Inject constructor(
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

            val backupData = gson.fromJson(jsonString, BackupData::class.java)
                ?: return Result.failure(Exception("Format JSON tidak valid"))

            taskDao.deleteAll()
            categoryDao.deleteAll()

            categoryDao.insertAll(backupData.categories)
            taskDao.insertAll(backupData.tasks)

            Result.success(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }
}
