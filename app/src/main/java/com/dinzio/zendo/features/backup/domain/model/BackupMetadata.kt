package com.dinzio.zendo.features.backup.domain.model

data class BackupMetadata(
    val lastBackupTimestamp: Long? = null,
    val lastBackupSizeBytes: Long? = null,
    val lastStatusMessage: String? = null,
    val lastStatusSuccess: Boolean? = null,
    val lastOperation: String? = null
)
