package com.example.mad511_lab1_dakhlallah_ali.work

import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.workDataOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

// Represents the current state of backup work for the UI
sealed interface BackupStatus {
    data object Idle : BackupStatus
    data object Waiting : BackupStatus
    data object BackingUp : BackupStatus
}

// Schedules and tracks periodic and manual backup work
@Singleton
class BackupScheduler @Inject constructor(
    private val workManager: WorkManager,
) {

    // Enqueues periodic backup and cleanup jobs with proper constraints and backoff
    fun scheduleBackups() {
        val backupConstraints = Constraints.Builder()
            .setRequiresStorageNotLow(true)
            .setRequiresBatteryNotLow(true)
            .build()

        val backupRequest = PeriodicWorkRequestBuilder<BackupWorker>(
            repeatInterval = 24,
            repeatIntervalTimeUnit = TimeUnit.HOURS,
        )
            .setConstraints(backupConstraints)
            .setBackoffCriteria(
                BackoffPolicy.EXPONENTIAL,
                30,
                TimeUnit.SECONDS,
            )
            .build()

        workManager.enqueueUniquePeriodicWork(
            PERIODIC_BACKUP_WORK,
            ExistingPeriodicWorkPolicy.KEEP,
            backupRequest,
        )

        val cleanupConstraints = Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .build()

        val cleanupRequest = PeriodicWorkRequestBuilder<CleanupWorker>(
            repeatInterval = 7,
            repeatIntervalTimeUnit = TimeUnit.DAYS,
        )
            .setConstraints(cleanupConstraints)
            .setInputData(workDataOf(CleanupWorker.KEY_KEEP to DEFAULT_KEEP))
            .build()

        workManager.enqueueUniquePeriodicWork(
            PERIODIC_CLEANUP_WORK,
            ExistingPeriodicWorkPolicy.KEEP,
            cleanupRequest,
        )
    }

    // Runs a one-time backup followed by cleanup if one isn't already running
    fun backupNow() {
        val backupConstraints = Constraints.Builder()
            .setRequiresStorageNotLow(true)
            .setRequiresBatteryNotLow(true)
            .build()

        val backupRequest = OneTimeWorkRequestBuilder<BackupWorker>()
            .setConstraints(backupConstraints)
            .setBackoffCriteria(
                BackoffPolicy.EXPONENTIAL,
                30,
                TimeUnit.SECONDS,
            )
            .build()

        val cleanupConstraints = Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .build()

        val cleanupRequest = OneTimeWorkRequestBuilder<CleanupWorker>()
            .setConstraints(cleanupConstraints)
            .setInputData(workDataOf(CleanupWorker.KEY_KEEP to DEFAULT_KEEP))
            .build()

        workManager.beginUniqueWork(
            BACKUP_NOW_WORK,
            ExistingWorkPolicy.KEEP,
            backupRequest,
        )
            .then(cleanupRequest)
            .enqueue()
    }

    // Watches backup jobs and maps their state for the UI
    fun status(): Flow<BackupStatus> = combine(
        workManager.getWorkInfosForUniqueWorkFlow(PERIODIC_BACKUP_WORK),
        workManager.getWorkInfosForUniqueWorkFlow(BACKUP_NOW_WORK),
    ) { periodicInfos, nowInfos ->
        val states = (periodicInfos + nowInfos).map { it.state }
        when {
            WorkInfo.State.RUNNING in states -> BackupStatus.BackingUp
            WorkInfo.State.ENQUEUED in states -> BackupStatus.Waiting
            else -> BackupStatus.Idle
        }
    }

    // Cancels all unique work chains
    fun cancelAll() {
        workManager.cancelUniqueWork(PERIODIC_BACKUP_WORK)
        workManager.cancelUniqueWork(PERIODIC_CLEANUP_WORK)
        workManager.cancelUniqueWork(BACKUP_NOW_WORK)
    }

    private companion object {
        const val PERIODIC_BACKUP_WORK = "periodic_backup"
        const val PERIODIC_CLEANUP_WORK = "periodic_cleanup"
        const val BACKUP_NOW_WORK = "backup_now"
        const val DEFAULT_KEEP = 5
    }
}