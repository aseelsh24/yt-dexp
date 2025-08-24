package com.aseelsh.ytdexp.data.media

import android.content.Context
import com.arthenica.mobileffmpeg.FFmpeg
import com.arthenica.mobileffmpeg.Config
import com.arthenica.mobileffmpeg.FFmpegExecution
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MediaProcessor @Inject constructor(
    @ApplicationContext private val context: Context
) {
    suspend fun extractAudio(
        inputPath: String,
        outputFileName: String,
        onProgress: (Int) -> Unit
    ): Result<File> = withContext(Dispatchers.IO) {
        try {
            val outputDir = context.getExternalFilesDir(null)
            val outputPath = File(outputDir, outputFileName).absolutePath

            // Configure FFmpeg
            Config.enableStatisticsCallback { statistics ->
                val timeInMilliseconds = statistics.time
                val duration = statistics.duration
                if (duration > 0) {
                    val progress = (timeInMilliseconds * 100 / duration).toInt()
                    onProgress(progress)
                }
            }

            // Extract audio command
            val command = arrayOf(
                "-i", inputPath,
                "-vn",                    // Disable video
                "-acodec", "libmp3lame", // Use MP3 codec
                "-ab", "192k",           // Audio bitrate
                "-ar", "44100",          // Audio sample rate
                outputPath
            )

            val result = FFmpeg.execute(command)

            if (result == Config.RETURN_CODE_SUCCESS) {
                Result.success(File(outputPath))
            } else {
                Result.failure(Exception("FFmpeg process failed with code $result"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun convertVideo(
        inputPath: String,
        outputFileName: String,
        format: String,
        onProgress: (Int) -> Unit
    ): Result<File> = withContext(Dispatchers.IO) {
        try {
            val outputDir = context.getExternalFilesDir(null)
            val outputPath = File(outputDir, outputFileName).absolutePath

            Config.enableStatisticsCallback { statistics ->
                val timeInMilliseconds = statistics.time
                val duration = statistics.duration
                if (duration > 0) {
                    val progress = (timeInMilliseconds * 100 / duration).toInt()
                    onProgress(progress)
                }
            }

            val command = when (format.lowercase()) {
                "mp4" -> arrayOf(
                    "-i", inputPath,
                    "-c:v", "libx264",   // Video codec
                    "-c:a", "aac",       // Audio codec
                    "-strict", "experimental",
                    outputPath
                )
                "webm" -> arrayOf(
                    "-i", inputPath,
                    "-c:v", "libvpx-vp9", // Video codec
                    "-c:a", "libopus",    // Audio codec
                    outputPath
                )
                else -> throw IllegalArgumentException("Unsupported format: $format")
            }

            val result = FFmpeg.execute(command)

            if (result == Config.RETURN_CODE_SUCCESS) {
                Result.success(File(outputPath))
            } else {
                Result.failure(Exception("FFmpeg process failed with code $result"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
