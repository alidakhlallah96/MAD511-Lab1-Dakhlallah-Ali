package com.example.mad511_lab1_dakhlallah_ali.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import java.io.File

// Returns the oldest backup file names to delete
fun filesToDelete(fileNames: List<String>, keep: Int): List<String> {
    if (keep < 0) return emptyList()

    // Keep only backup files and grab their timestamp
    val validBackups = fileNames.mapNotNull { name ->
        if (!name.startsWith("setlist_backup_") || !name.endsWith(".json")) {
            return@mapNotNull null
        }
        val timestamp = name
            .removePrefix("setlist_backup_")
            .removeSuffix(".json")
            .toLongOrNull() ?: return@mapNotNull null

        name to timestamp
    }

    // Nothing to delete if we are under the limit
    if (validBackups.size <= keep) return emptyList()

    // Sort oldest to newest and keep the newest ones
    return validBackups
        .sortedBy { it.second }
        .dropLast(keep)
        .map { it.first }
}

// Deletes old backup files
class CleanupWorker(
    context: Context,
    params: WorkerParameters,
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        // Read how many backups to keep (default 5)
        val keep = inputData.getInt(KEY_KEEP, 5)
        val backupsDir = File(applicationContext.filesDir, "backups")

        if (!backupsDir.exists()) {
            return Result.success(workDataOf(KEY_DELETED_COUNT to 0))
        }

        val fileNames = backupsDir.listFiles()?.map { it.name }.orEmpty()
        val toDelete = filesToDelete(fileNames, keep)

        var deletedCount = 0
        for (name in toDelete) {
            if (File(backupsDir, name).delete()) {
                deletedCount++
            }
        }

        return Result.success(workDataOf(KEY_DELETED_COUNT to deletedCount))
    }

    companion object {
        const val KEY_KEEP = "keep"
        const val KEY_DELETED_COUNT = "deleted_count"
    }
}