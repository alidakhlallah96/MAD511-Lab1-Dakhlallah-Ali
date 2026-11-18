java.lang.NoSuchMethodException: com.example.mad511_lab1_dakhlallah_ali.work.BackupWorker.<init> [class android.content.Context, class androidx.work.WorkerParameters]

WorkManager crashed because removing the startup provider bypassed Hilt's custom factory, so it tried to use a default constructor that doesn't exist for our injected worker.
