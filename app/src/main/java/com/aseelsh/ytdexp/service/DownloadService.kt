package com.aseelsh.ytdexp.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.aseelsh.ytdexp.R
import com.aseelsh.ytdexp.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import javax.inject.Inject

@AndroidEntryPoint
class DownloadService : Service() {
    private val serviceScope = CoroutineScope(Dispatchers.IO + Job())

    @Inject
    lateinit var notificationManager: NotificationManager

    @Inject
    lateinit var okHttpClient: OkHttpClient

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(
        intent: Intent?,
        flags: Int,
        startId: Int,
    ): Int {
        val url = intent?.getStringExtra(EXTRA_URL) ?: return START_NOT_STICKY
        val fileName = intent.getStringExtra(EXTRA_FILE_NAME) ?: "download"

        val notification = createNotification(fileName, 0)
        startForeground(NOTIFICATION_ID, notification)

        serviceScope.launch {
            downloadFile(url, fileName)
        }

        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(
                    NOTIFICATION_CHANNEL_ID,
                    "Downloads",
                    NotificationManager.IMPORTANCE_LOW,
                ).apply {
                    description = "Used for showing active downloads"
                }
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun createNotification(
        fileName: String,
        progress: Int,
    ): Notification {
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent =
            PendingIntent.getActivity(
                this,
                0,
                intent,
                PendingIntent.FLAG_IMMUTABLE,
            )

        return NotificationCompat
            .Builder(this, NOTIFICATION_CHANNEL_ID)
            .setContentTitle("Downloading $fileName")
            .setSmallIcon(R.drawable.ic_download)
            .setProgress(MAX_PROGRESS, progress, false)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .build()
    }

    private suspend fun downloadFile(
        url: String,
        fileName: String,
    ) {
        try {
            val request =
                Request
                    .Builder()
                    .url(url)
                    .header("Range", "bytes=0-")
                    .build()

            okHttpClient.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw IOException("Unexpected code $response")

                val body = response.body ?: throw IOException("No response body")
                val contentLength = body.contentLength()
                val file = File(getExternalFilesDir(null), fileName)
                body.byteStream().use { input ->
                    FileOutputStream(file).use { output ->
                        copyStream(input, output, contentLength, fileName)
                    }
                }
            }
        } catch (e: IOException) {
            Log.e(TAG, "Download failed", e)
        } finally {
            stopForeground(true)
            stopSelf()
        }
    }

    private fun copyStream(
        input: InputStream,
        output: FileOutputStream,
        contentLength: Long,
        fileName: String,
    ) {
        var downloadedBytes = 0L
        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
        var bytes = input.read(buffer)

        while (bytes >= 0) {
            output.write(buffer, 0, bytes)
            downloadedBytes += bytes

            val progress = ((downloadedBytes * MAX_PROGRESS) / contentLength).toInt()
            updateNotification(fileName, progress)

            bytes = input.read(buffer)
        }
    }

    private fun updateNotification(
        fileName: String,
        progress: Int,
    ) {
        notificationManager.notify(NOTIFICATION_ID, createNotification(fileName, progress))
    }

    companion object {
        private const val TAG = "DownloadService"
        private const val NOTIFICATION_CHANNEL_ID = "download_channel"
        private const val NOTIFICATION_ID = 1
        private const val DEFAULT_BUFFER_SIZE = 8192
        private const val MAX_PROGRESS = 100

        const val EXTRA_URL = "url"
        const val EXTRA_FILE_NAME = "fileName"
    }
}
