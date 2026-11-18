package com.example.mad511_lab1_dakhlallah_ali.work

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.mad511_lab1_dakhlallah_ali.data.ArtistRepository
import com.example.mad511_lab1_dakhlallah_ali.data.SetListBackup
import com.example.mad511_lab1_dakhlallah_ali.data.toBackupItem
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first
import kotlinx.serialization.json.Json
import java.io.File
import java.io.IOException

@HiltWorker
class BackupWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val repository: ArtistRepository,
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            // read the current artist list once from the existing Flow
            val artists = repository.artists.first()

            if (artists.isEmpty()) {
                return Result.success()
            }

            val now = System.currentTimeMillis()
            val backup = SetListBackup(
                createdAt = now,
                artists = artists.map { it.toBackupItem() },
            )

            // create backups folder inside the app's files directory if missing
            val backupsDir = File(applicationContext.filesDir, "backups").apply {
                mkdirs()
            }
            val fileName = "setlist_backup_$now.json"
            val backupFile = File(backupsDir, fileName)

            backupFile.writeText(Json.encodeToString(backup))

            Result.success(
                workDataOf(
                    KEY_ARTIST_COUNT to artists.size,
                    KEY_FILE_NAME to fileName,
                )
            )
        } catch (e: IOException) {
            // only catch IOException so CancellationException
            if (runAttemptCount < 3) Result.retry() else Result.failure()
        }
    }

    companion object {
        const val KEY_ARTIST_COUNT = "artist_count"
        const val KEY_FILE_NAME = "file_name"
    }
}