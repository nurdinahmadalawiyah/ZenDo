package com.dinzio.zendo.features.settings.data.repository

import android.content.Context
import android.content.Intent
import com.dinzio.zendo.features.settings.domain.repository.DriveSyncRepository
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException
import com.google.api.client.http.ByteArrayContent
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.DriveScopes
import com.google.api.services.drive.model.File
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import javax.inject.Inject

class DriveSyncRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : DriveSyncRepository {

    private val BACKUP_FILE_NAME = "zendo_backup.json"

    private fun getDriveService(userEmail: String): Drive {
        try {
            if (userEmail.isBlank() || userEmail == "null") {
                throw IllegalArgumentException("Email is empty or invalid: '$userEmail'")
            }

            val credential = GoogleAccountCredential.usingOAuth2(
                context,
                listOf(DriveScopes.DRIVE_APPDATA)
            )
            // By explicitly creating the Account object, we bypass any bugs in the 
            // GoogleAccountCredential library where selectedAccountName might resolve to null
            credential.selectedAccount = android.accounts.Account(userEmail, "com.google")

            return Drive.Builder(
                NetHttpTransport(),
                GsonFactory.getDefaultInstance(),
                credential
            )
                .setApplicationName("ZenDo")
                .build()
        } catch (e: Exception) {
            throw Exception("getDriveService failed: ${e.message}", e)
        }
    }

    override suspend fun backupToDrive(jsonData: String, userEmail: String): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val driveService = getDriveService(userEmail)

                val fileList = driveService.files().list()
                    .setSpaces("appDataFolder")
                    .setQ("name = '$BACKUP_FILE_NAME'")
                    .execute()

                val fileContent = ByteArrayContent.fromString("application/json", jsonData)
                val existingFile = fileList.files.firstOrNull()

                if (existingFile != null) {
                    driveService.files().update(existingFile.id, File(), fileContent).execute()
                } else {
                    val fileMetadata = File().apply {
                        name = BACKUP_FILE_NAME
                        parents = listOf("appDataFolder")
                    }
                    driveService.files().create(fileMetadata, fileContent).execute()
                }

                Result.success(Unit)
            } catch (e: UserRecoverableAuthIOException) {
                Result.failure(e)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun restoreFromDrive(userEmail: String): Result<String> {
        return withContext(Dispatchers.IO) {
            try {
                val driveService = getDriveService(userEmail)

                val fileList = driveService.files().list()
                    .setSpaces("appDataFolder")
                    .setQ("name = '$BACKUP_FILE_NAME'")
                    .execute()

                val existingFile = fileList.files.firstOrNull()
                    ?: throw Exception("Backup file not found")

                val outputStream = ByteArrayOutputStream()
                driveService.files().get(existingFile.id).executeMediaAndDownloadTo(outputStream)

                val jsonString = outputStream.toString("UTF-8")
                Result.success(jsonString)

            } catch (e: UserRecoverableAuthIOException) {
                Result.failure(e)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun getAuthIntent(userEmail: String): Intent? {
        return try {
            val driveService = getDriveService(userEmail)
            driveService.files().list().setSpaces("appDataFolder").execute()
            null
        } catch (e: UserRecoverableAuthIOException) {
            e.intent
        } catch (e: Exception) {
            null
        }
    }
}