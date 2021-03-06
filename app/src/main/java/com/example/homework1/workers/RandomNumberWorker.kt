package com.example.homework1.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlin.random.Random


class RandomNumberWorker(context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params) {
    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {

        delay(3000)

        val randNumber = Random.nextInt(1, 1000)

        val output: Data = workDataOf(WORKER_RESULT_RANDOM_NUMBER to randNumber)

        return@withContext Result.success(output)
    }

    companion object {
        const val WORKER_RESULT_RANDOM_NUMBER = "WORKER_RESULT_RANDOM_NUMBER"
    }
}