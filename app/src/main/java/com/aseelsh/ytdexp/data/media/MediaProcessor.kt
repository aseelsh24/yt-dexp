package com.aseelsh.ytdexp.data.media

import android.content.Context
import android.util.Log
import com.infullmobile.android.videokit.VideoKit
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MediaProcessor
@Inject
constructor(
    @ApplicationContext private val context: Context,
) {
    suspend fun extractAudio(
        inputPath: String,
        outputFileName: String,
        onProgress: (Int) -> Unit, // Note: Progress reporting is not supported by VideoKit
    ): Result<File> =
        withContext(Dispatchers.IO) {
            try {
                val outputDir = context.getExternalFilesDir(null)
                val outputPath = File(outputDir, outputFileName).absolutePath

                // VideoKit does not have a direct equivalent for progress callbacks.
                // We will call onProgress with 0 and 100 to indicate start and end.
                onProgress(0)

                val command = VideoKit().createCommand()
                    .inputPath(inputPath)
                    .outputPath(outputPath)
                    .customCommand("-vn -acodec libmp3lame -ab 192k -ar 44100")
                    .build()

                command.execute()

                onProgress(MAX_PROGRESS)
                Result.success(File(outputPath))
            } catch (e: Exception) {
                Log.e(TAG, "Audio extraction failed", e)
                Result.failure(IOException("FFmpeg process failed", e))
            }
        }

    suspend fun convertVideo(
        inputPath: String,
        outputFileName: String,
        format: String,
        onProgress: (Int) -> Unit, // Note: Progress reporting is not supported by VideoKit
    ): Result<File> =
        withContext(Dispatchers.IO) {
            try {
                val outputDir = context.getExternalFilesDir(null)
                val outputPath = File(outputDir, outputFileName).absolutePath

                onProgress(0)

                val customCommand = when (format.lowercase()) {
                    "mp4" -> "-c:v libx264 -c:a aac -strict experimental"
                    "webm" -> "-c:v libvpx-vp9 -c:a libopus"
                    else -> throw IllegalArgumentException("Unsupported format: $format")
                }

                val command = VideoKit().createCommand()
                    .inputPath(inputPath)
                    .outputPath(outputPath)
                    .customCommand(customCommand)
                    .build()

                command.execute()

                onProgress(MAX_PROGRESS)
                Result.success(File(outputPath))
            } catch (e: Exception) {
                Log.e(TAG, "Video conversion failed", e)
                Result.failure(IOException("FFmpeg process failed", e))
            }
        }

    companion object {
        private const val TAG = "MediaProcessor"
        private const val MAX_PROGRESS = 100
    }
}
