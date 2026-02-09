package com.dinzio.zendo.core.data.local

import android.content.Context
import android.net.Uri
import android.util.Log
import com.dinzio.zendo.core.domain.model.BackupData
import com.dinzio.zendo.features.category.data.local.dao.CategoryDao
import com.dinzio.zendo.features.task.data.local.dao.TaskDao
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import javax.inject.Inject

class BackupManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val taskDao: TaskDao,
    private val categoryDao: CategoryDao,
) {
    private val gson = Gson()

    suspend fun exportData(uri: Uri): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val tasks = taskDao.getAllTasksSync()
            val categories = categoryDao.getAllCategoriesSync()

            val backupData = BackupData(
                tasks = tasks,
                categories = categories
            )

            val jsonString = gson.toJson(backupData)

            context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                outputStream.write(jsonString.toByteArray())
            }
            Result.success(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    suspend fun importData(uri: Uri): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val stringBuilder = StringBuilder()
            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                BufferedReader(InputStreamReader(inputStream)).use { reader ->
                    var line: String? = reader.readLine()
                    while (line != null) {
                        stringBuilder.append(line)
                        line = reader.readLine()
                    }
                }
            }

            val jsonString = stringBuilder.toString()
            if (jsonString.isEmpty()) {
                return@withContext Result.failure(Exception("File kosong"))
            }

            val backupData = gson.fromJson(jsonString, BackupData::class.java)
                ?: return@withContext Result.failure(Exception("File format invalid"))

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