package com.aseelsh.ytdexp.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.aseelsh.ytdexp.R
import com.aseelsh.ytdexp.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject

@AndroidEntryPoint
class DownloadService : Service() {
    private val serviceScope = CoroutineScope(Dispatchers.IO + Job())
    private val NOTIFICATION_CHANNEL_ID = "download_channel"
    private val NOTIFICATION_ID = 1
    private val BUFFER_SIZE = 8192

    @Inject
    lateinit var notificationManager: NotificationManager

    @Inject
    lateinit var okHttpClient: OkHttpClient

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val url = intent?.getStringExtra("url") ?: return START_NOT_STICKY
        val fileName = intent.getStringExtra("fileName") ?: "download"

        val notification = createNotification(fileName, 0)
        startForeground(NOTIFICATION_ID, notification)

        serviceScope.launch {
            downloadFile(url, fileName)
        }

        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                "Downloads",
                NotificationManager.IMPORTANCE_LOW,
            ).apply {
                description = "Used for showing active downloads"
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun createNotification(fileName: String, progress: Int): Notification {
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE,
        )

        return NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setContentTitle("Downloading $fileName")
            .setSmallIcon(R.drawable.ic_download)
            .setProgress(100, progress, false)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .build()
    }

    private suspend fun downloadFile(url: String, fileName: String) {
        try {
            val request = Request.Builder()
                .url(url)
                .header("Range", "bytes=0-")
                .build()

            okHttpClient.newCall(request).execute().use { response ->
                val body = response.body ?: throw IOException("No response body")
                val contentLength = body.contentLength()
                var downloadedBytes = 0L

                val file = File(getExternalFilesDir(null), fileName)
                FileOutputStream(file).use { output ->
                    body.byteStream().use { input ->
                        val buffer = ByteArray(BUFFER_SIZE)
                        var bytes = input.read(buffer)

                        while (bytes >= 0) {
                            output.write(buffer, 0, bytes)
                            downloadedBytes += bytes

                            val progress = ((downloadedBytes * 100) / contentLength).toInt()
                            updateNotification(fileName, progress)

                            bytes = input.read(buffer)
                        }
                    }
                }

                stopForeground(true)
                stopSelf()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            stopForeground(true)
            stopSelf()
        }
    }

    private fun updateNotification(fileName: String, progress: Int) {
        notificationManager.notify(NOTIFICATION_ID, createNotification(fileName, progress))
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }
}
