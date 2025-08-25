package com.aseelsh.ytdexp.data.downloader

import android.content.Context
import android.os.Environment
import android.util.Log
import com.aseelsh.ytdexp.domain.model.DownloadItem
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FileDownloader @Inject constructor(
    @ApplicationContext private val context: Context,
    private val okHttpClient: OkHttpClient,
) {
    suspend fun downloadFile(
        downloadItem: DownloadItem,
        onProgress: (Int) -> Unit,
    ): Result<File> = withContext(Dispatchers.IO) {
        try {
            val request = Request.Builder().url(downloadItem.url)
                .header("Range", "bytes=0-") // Support for resume
                .build()

            val response = okHttpClient.newCall(request).execute()
            if (!response.isSuccessful) {
                return@withContext Result.failure(IOException("Download failed with code ${response.code}"))
            }

            val body = response.body
            val contentLength = body?.contentLength() ?: -1L
            val inputStream = body?.byteStream()

            val downloadDir = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
            val fileName = "${downloadItem.title}.${downloadItem.format}"
            val outputFile = File(downloadDir, fileName)

            inputStream?.use { input ->
                outputFile.outputStream().use { output ->
                    val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
                    var bytesRead: Int
                    var totalBytesRead = 0L

                    while (input.read(buffer).also { bytesRead = it } != -1) {
                        output.write(buffer, 0, bytesRead)
                        totalBytesRead += bytesRead
                        val progress = ((totalBytesRead * MAX_PROGRESS) / contentLength).toInt()
                        onProgress(progress)
                    }
                }
            }

            Result.success(outputFile)
        } catch (e: IOException) {
            Log.e(TAG, "Download failed", e)
            Result.failure(e)
        }
    }

    companion object {
        private const val TAG = "FileDownloader"
        private const val DEFAULT_BUFFER_SIZE = 8192
        private const val MAX_PROGRESS = 100
    }
}
