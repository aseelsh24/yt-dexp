package com.aseelsh.ytdexp.data.extractor

import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.io.IOException
import java.util.regex.Pattern
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VideoExtractor
    @Inject
    constructor(
        private val okHttpClient: OkHttpClient,
    ) {
        suspend fun extractVideoInfo(url: String): VideoInfo =
            when {
                isYouTubeUrl(url) -> extractYouTubeVideo(url)
                else -> throw UnsupportedOperationException("Unsupported platform")
            }

        private suspend fun extractYouTubeVideo(url: String): VideoInfo {
            val videoId = extractYouTubeVideoId(url)
            val playerResponse = fetchYouTubePlayerResponse(videoId)
            return parseYouTubePlayerResponse(playerResponse)
        }

        private fun extractYouTubeVideoId(url: String): String {
            val pattern =
                Pattern.compile(
                    "(?<=watch\\?v=|/videos/|embed/|youtu.be/|/v/|/e/|watch\\?v%3D|" +
                        "watch\\?feature=player_embedded&v=)[^#&?\\n]*",
                )
            val matcher = pattern.matcher(url)
            if (matcher.find()) {
                return matcher.group()
            }
            throw IllegalArgumentException("Could not extract video ID from URL")
        }

        private suspend fun fetchYouTubePlayerResponse(videoId: String): JSONObject {
            val request =
                Request
                    .Builder()
                    .url("https://www.youtube.com/watch?v=$videoId")
                    .build()

            val response = okHttpClient.newCall(request).execute()
            val html = response.body?.string() ?: throw IOException("Empty response")

            val playerResponsePattern = Pattern.compile("ytInitialPlayerResponse\\s*=\\s*(\\{.+?\\})\\s*;")
            val matcher = playerResponsePattern.matcher(html)

            if (matcher.find()) {
                return JSONObject(matcher.group(1))
            }
            throw IOException("Could not find player response")
        }

        private fun parseYouTubePlayerResponse(playerResponse: JSONObject): VideoInfo {
            val videoDetails = playerResponse.getJSONObject("videoDetails")
            val streamingData = playerResponse.getJSONObject("streamingData")

            val title = videoDetails.getString("title")
            val thumbnail =
                videoDetails
                    .getJSONObject("thumbnail")
                    .getJSONArray("thumbnails")
                    .getJSONObject(0)
                    .getString("url")

            val formats = mutableListOf<VideoFormat>()

            val adaptiveFormats = streamingData.getJSONArray("adaptiveFormats")
            for (i in 0 until adaptiveFormats.length()) {
                val format = adaptiveFormats.getJSONObject(i)
                formats.add(
                    VideoFormat(
                        url = format.getString("url"),
                        mimeType = format.getString("mimeType"),
                        quality = format.optString("quality", ""),
                        bitrate = format.optLong("bitrate", 0),
                    ),
                )
            }

            return VideoInfo(
                title = title,
                thumbnail = thumbnail,
                formats = formats,
            )
        }

        private fun isYouTubeUrl(url: String): Boolean = url.contains("youtube.com") || url.contains("youtu.be")
    }

data class VideoInfo(
    val title: String,
    val thumbnail: String,
    val formats: List<VideoFormat>,
)

data class VideoFormat(
    val url: String,
    val mimeType: String,
    val quality: String,
    val bitrate: Long,
)
