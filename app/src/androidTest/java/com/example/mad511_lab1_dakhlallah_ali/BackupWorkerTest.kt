package com.example.mad511_lab1_dakhlallah_ali

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import androidx.work.testing.TestListenableWorkerBuilder
import androidx.work.workDataOf
import com.example.mad511_lab1_dakhlallah_ali.data.ArtistRepository
import com.example.mad511_lab1_dakhlallah_ali.work.BackupWorker
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

class FakeArtistRepository(
    private val artistsToReturn: List<Artist> = emptyList(),
    private val shouldThrowIOException: Boolean = false
) : ArtistRepository {

    override val artists: Flow<List<Artist>>
        get() {
            if (shouldThrowIOException) {
                throw IOException("Simulated disk error")
            }
            return flowOf(artistsToReturn)
        }

    override fun getArtist(id: Int): Flow<Artist?> = flowOf(artistsToReturn.find { it.id == id })

    override fun searchArtists(term: String): Flow<List<Artist>> =
        flowOf(artistsToReturn.filter { it.name.contains(term, ignoreCase = true) })

    override suspend fun addArtist(name: String, genre: String, yearFormed: Int) {}

    override suspend fun toggleFavorite(id: Int) {}

    override suspend fun deleteArtist(id: Int) {}
}

@RunWith(AndroidJUnit4::class)
class BackupWorkerTest {

    private val context: Context = ApplicationProvider.getApplicationContext()

    @Test
    fun testBackupWorkerSuccessWithArtists() = runBlocking {
        val artists = listOf(
            Artist(1, "The Beatles", "Rock", 1960, isFavorite = true),
            Artist(2, "Daft Punk", "Electronic", 1993, isFavorite = false)
        )
        val repository = FakeArtistRepository(artistsToReturn = artists)

        val worker = TestListenableWorkerBuilder<BackupWorker>(context)
            .setWorkerFactory(object : androidx.work.WorkerFactory() {
                override fun createWorker(
                    appContext: Context,
                    workerClassName: String,
                    workerParameters: WorkerParameters
                ): ListenableWorker {
                    return BackupWorker(appContext, workerParameters, repository)
                }
            })
            .build()

        val result = worker.doWork()

        // Assert that it succeeded and returned the correct count in output data
        assertEquals(ListenableWorker.Result.Success::class, result::class)
        assertEquals(2, result.outputData.getInt(BackupWorker.KEY_ARTIST_COUNT, -1))
    }

    @Test
    fun testBackupWorkerSuccessEmptyRepository() = runBlocking {
        val repository = FakeArtistRepository(artistsToReturn = emptyList())

        val worker = TestListenableWorkerBuilder<BackupWorker>(context)
            .setWorkerFactory(object : androidx.work.WorkerFactory() {
                override fun createWorker(
                    appContext: Context,
                    workerClassName: String,
                    workerParameters: WorkerParameters
                ): ListenableWorker {
                    return BackupWorker(appContext, workerParameters, repository)
                }
            })
            .build()

        val result = worker.doWork()

        assertEquals(ListenableWorker.Result.Success::class, result::class)
        // Fallback to 0 if no output data was attached (matching empty worker return)
        assertEquals(0, result.outputData.getInt(BackupWorker.KEY_ARTIST_COUNT, 0))
    }
    @Test
    fun testBackupWorkerRetryOnFirstIOException() = runBlocking {
        val repository = FakeArtistRepository(shouldThrowIOException = true)

        val worker = TestListenableWorkerBuilder<BackupWorker>(context)
            .setWorkerFactory(object : androidx.work.WorkerFactory() {
                override fun createWorker(
                    appContext: Context,
                    workerClassName: String,
                    workerParameters: WorkerParameters
                ): ListenableWorker {
                    return BackupWorker(appContext, workerParameters, repository)
                }
            })
            .build()

        val result = worker.doWork()

        assertEquals(ListenableWorker.Result.retry(), result)
    }

    @Test
    fun testBackupWorkerFailureAfterMaxAttempts() = runBlocking {
        val repository = FakeArtistRepository(shouldThrowIOException = true)

        val worker = TestListenableWorkerBuilder<BackupWorker>(context)
            .setWorkerFactory(object : androidx.work.WorkerFactory() {
                override fun createWorker(
                    appContext: Context,
                    workerClassName: String,
                    workerParameters: WorkerParameters
                ): ListenableWorker {
                    return BackupWorker(appContext, workerParameters, repository)
                }
            })
            .setRunAttemptCount(3) // Simulate reaching max attempts
            .build()

        val result = worker.doWork()

        assertEquals(ListenableWorker.Result.failure(), result)
    }
}