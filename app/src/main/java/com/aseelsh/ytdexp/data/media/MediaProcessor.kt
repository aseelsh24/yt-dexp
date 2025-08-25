package com.aseelsh.ytdexp.data.media

import android.content.Context
import android.util.Log
import com.arthenica.mobileffmpeg.Config
import com.arthenica.mobileffmpeg.FFmpeg
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
            onProgress: (Int) -> Unit,
        ): Result<File> =
            withContext(Dispatchers.IO) {
                try {
                    val outputDir = context.getExternalFilesDir(null)
                    val outputPath = File(outputDir, outputFileName).absolutePath

                    Config.enableStatisticsCallback { statistics ->
                        val timeInMilliseconds = statistics.time
                        val duration = statistics.duration
                        if (duration > 0) {
                            val progress = (timeInMilliseconds * MAX_PROGRESS / duration).toInt()
                            onProgress(progress)
                        }
                    }

                    val command =
                        arrayOf(
                            "-i",
                            inputPath,
                            "-vn",
                            "-acodec",
                            "libmp3lame",
                            "-ab",
                            "192k",
                            "-ar",
                            "44100",
                            outputPath,
                        )

                    val result = FFmpeg.execute(command)

                    if (result == Config.RETURN_CODE_SUCCESS) {
                        Result.success(File(outputPath))
                    } else {
                        Result.failure(IOException("FFmpeg process failed with code $result"))
                    }
                } catch (e: IOException) {
                    Log.e(TAG, "Audio extraction failed", e)
                    Result.failure(e)
                }
            }

        suspend fun convertVideo(
            inputPath: String,
            outputFileName: String,
            format: String,
            onProgress: (Int) -> Unit,
        ): Result<File> =
            withContext(Dispatchers.IO) {
                try {
                    val outputDir = context.getExternalFilesDir(null)
                    val outputPath = File(outputDir, outputFileName).absolutePath

                    Config.enableStatisticsCallback { statistics ->
                        val timeInMilliseconds = statistics.time
                        val duration = statistics.duration
                        if (duration > 0) {
                            val progress = (timeInMilliseconds * MAX_PROGRESS / duration).toInt()
                            onProgress(progress)
                        }
                    }

                    val command =
                        when (format.lowercase()) {
                            "mp4" ->
                                arrayOf(
                                    "-i",
                                    inputPath,
                                    "-c:v",
                                    "libx264",
                                    "-c:a",
                                    "aac",
                                    "-strict",
                                    "experimental",
                                    outputPath,
                                )
                            "webm" ->
                                arrayOf(
                                    "-i",
                                    inputPath,
                                    "-c:v",
                                    "libvpx-vp9",
                                    "-c:a",
                                    "libopus",
                                    outputPath,
                                )
                            else -> throw IllegalArgumentException("Unsupported format: $format")
                        }

                    val result = FFmpeg.execute(command)

                    if (result == Config.RETURN_CODE_SUCCESS) {
                        Result.success(File(outputPath))
                    } else {
                        Result.failure(IOException("FFmpeg process failed with code $result"))
                    }
                } catch (e: IOException) {
                    Log.e(TAG, "Video conversion failed", e)
                    Result.failure(e)
                }
            }

        companion object {
            private const val TAG = "MediaProcessor"
            private const val MAX_PROGRESS = 100
        }
    }
