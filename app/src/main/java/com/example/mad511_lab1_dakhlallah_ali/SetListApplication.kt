package com.example.mad511_lab1_dakhlallah_ali

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.example.mad511_lab1_dakhlallah_ali.work.BackupScheduler
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class SetListApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    // Inject the scheduler so we can schedule jobs on app startup
    @Inject
    lateinit var backupScheduler: BackupScheduler

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    override fun onCreate() {
        super.onCreate()

        // Schedule periodic backups once when the app launches
        backupScheduler.scheduleBackups()
    }
}