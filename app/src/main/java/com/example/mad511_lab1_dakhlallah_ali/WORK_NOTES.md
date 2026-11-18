## Step 9: Intentional Crash & Explanation
**The Crash & Cause:**
  `java.lang.NoSuchMethodException: com.example.mad511_lab1_dakhlallah_ali.work.BackupWorker.<init> [class android.content.Context, class androidx.work.WorkerParameters]`
  WorkManager crashed because removing the startup provider bypassed Hilt's custom factory, so it tried to use a default constructor that doesn't exist for our injected worker.

## Step 3: Reading Artists in BackupWorker
**Method Used:** We read the artists list once from the repository Flow using first.
**Why:** A worker is a one-shot background task rather than an active UI collector. Taking a single snapshot of the database state allows us safely to serialize the current list into JSON without leaving an open-ended flow collection running in the background.

## Step 5: Background Worker Constraints & Rationale
**Constraints Chosen:** We applied resource constraints such as requiring that the device not be in a low battery state.
**Why & What Goes Wrong Without Them:** These limits prevent heavy file serialization and disk writes from firing when the device is struggling for battery or CPU. Without them, background routines could drain the user's phone or fail mid-execution due to resource starvation.


## Step 7: Concurrent Backup Execution Safety
**What Happens:** If the backup worker runs twice in a row, it generates two separate JSON backup files on disk.
**Why It Is Safe:** It is completely safe because every filename gets a unique timestamp, avoiding any file overwrite conflicts. Additionally, the cleanup worker automatically trims older backups so only the newest five remain.


## Step 8: Factors Delaying Periodic Job Execution
**Reason:** Android power-saving features can defer periodic background work to batch device wakeups and save power. Also, if specific constraints aren't met right when the interval hits, WorkManager waits until those conditions are satisfied.