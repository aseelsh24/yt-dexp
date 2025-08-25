# detekt

## Metrics

* 92 number of properties

* 59 number of functions

* 21 number of classes

* 13 number of packages

* 20 number of kt files

## Complexity Report

* 1,202 lines of code (loc)

* 1,030 source lines of code (sloc)

* 698 logical lines of code (lloc)

* 20 comment lines of code (cloc)

* 116 cyclomatic complexity (mcc)

* 68 cognitive complexity

* 58 number of total code smells

* 1% comment source ratio

* 166 mcc per 1,000 lloc

* 83 code smells per 1,000 lloc

## Findings (58)

### complexity, LongMethod (2)

One method should have one responsibility. Long methods tend to handle many things at once. Prefer smaller methods to make them easier to understand.

[Documentation](https://detekt.dev/docs/rules/complexity#longmethod)

* /app/app/src/main/java/com/aseelsh/ytdexp/ui/MainScreen.kt:18:5
```
The function MainScreen is too long (61). The maximum length is 60.
```
```kotlin
15
16 @OptIn(ExperimentalMaterial3Api::class)
17 @Composable
18 fun MainScreen(
!!     ^ error
19     viewModel: MainViewModel = hiltViewModel()
20 ) {
21     var selectedTab by remember { mutableStateOf(0) }

```

* /app/app/src/main/java/com/aseelsh/ytdexp/ui/screens/BrowserScreen.kt:32:5
```
The function BrowserScreen is too long (61). The maximum length is 60.
```
```kotlin
29
30 @OptIn(ExperimentalMaterial3Api::class)
31 @Composable
32 fun BrowserScreen(
!!     ^ error
33     url: String,
34     onUrlChange: (String) -> Unit,
35     onDownloadClick: (String, String) -> Unit,

```

### complexity, NestedBlockDepth (1)

Excessive nesting leads to hidden complexity. Prefer extracting code to make it easier to understand.

[Documentation](https://detekt.dev/docs/rules/complexity#nestedblockdepth)

* /app/app/src/main/java/com/aseelsh/ytdexp/service/DownloadService.kt:78:25
```
Function downloadFile is nested too deeply.
```
```kotlin
75             .build()
76     }
77
78     private suspend fun downloadFile(url: String, fileName: String) {
!!                         ^ error
79         try {
80             val request = Request.Builder().url(url)
81                 .header("Range", "bytes=0-")

```

### exceptions, PrintStackTrace (1)

Do not print a stack trace. These debug statements should be removed or replaced with a logger.

[Documentation](https://detekt.dev/docs/rules/exceptions#printstacktrace)

* /app/app/src/main/java/com/aseelsh/ytdexp/service/DownloadService.kt:111:13
```
Do not print a stack trace. These debug statements should be removed or replaced with a logger.
```
```kotlin
108                 stopSelf()
109             }
110         } catch (e: Exception) {
111             e.printStackTrace()
!!!             ^ error
112             stopForeground(true)
113             stopSelf()
114         }

```

### exceptions, TooGenericExceptionCaught (5)

The caught exception is too generic. Prefer catching specific exceptions to the case that is currently handled.

[Documentation](https://detekt.dev/docs/rules/exceptions#toogenericexceptioncaught)

* /app/app/src/main/java/com/aseelsh/ytdexp/data/downloader/FileDownloader.kt:61:18
```
The caught exception is too generic. Prefer catching specific exceptions to the case that is currently handled.
```
```kotlin
58             }
59
60             Result.success(outputFile)
61         } catch (e: Exception) {
!!                  ^ error
62             Result.failure(e)
63         }
64     }

```

* /app/app/src/main/java/com/aseelsh/ytdexp/data/media/MediaProcessor.kt:57:18
```
The caught exception is too generic. Prefer catching specific exceptions to the case that is currently handled.
```
```kotlin
54             } else {
55                 Result.failure(Exception("FFmpeg process failed with code $result"))
56             }
57         } catch (e: Exception) {
!!                  ^ error
58             Result.failure(e)
59         }
60     }

```

* /app/app/src/main/java/com/aseelsh/ytdexp/data/media/MediaProcessor.kt:112:18
```
The caught exception is too generic. Prefer catching specific exceptions to the case that is currently handled.
```
```kotlin
109             } else {
110                 Result.failure(Exception("FFmpeg process failed with code $result"))
111             }
112         } catch (e: Exception) {
!!!                  ^ error
113             Result.failure(e)
114         }
115     }

```

* /app/app/src/main/java/com/aseelsh/ytdexp/data/repository/DownloadRepositoryImpl.kt:42:18
```
The caught exception is too generic. Prefer catching specific exceptions to the case that is currently handled.
```
```kotlin
39             downloadDao.insertDownload(entity)
40
41             Result.success(entity.toDownloadItem())
42         } catch (e: Exception) {
!!                  ^ error
43             Result.failure(e)
44         }
45     }

```

* /app/app/src/main/java/com/aseelsh/ytdexp/service/DownloadService.kt:110:18
```
The caught exception is too generic. Prefer catching specific exceptions to the case that is currently handled.
```
```kotlin
107                 stopForeground(true)
108                 stopSelf()
109             }
110         } catch (e: Exception) {
!!!                  ^ error
111             e.printStackTrace()
112             stopForeground(true)
113             stopSelf()

```

### exceptions, TooGenericExceptionThrown (2)

The thrown exception is too generic. Prefer throwing project specific exceptions to handle error cases.

[Documentation](https://detekt.dev/docs/rules/exceptions#toogenericexceptionthrown)

* /app/app/src/main/java/com/aseelsh/ytdexp/data/extractor/VideoExtractor.kt:45:47
```
Exception is a too generic Exception. Prefer throwing specific exceptions that indicate a specific error case.
```
```kotlin
42             .build()
43
44         val response = okHttpClient.newCall(request).execute()
45         val html = response.body?.string() ?: throw Exception("Empty response")
!!                                               ^ error
46
47         val playerResponsePattern = Pattern.compile("ytInitialPlayerResponse\\s*=\\s*(\\{.+?\\})\\s*;")
48         val matcher = playerResponsePattern.matcher(html)

```

* /app/app/src/main/java/com/aseelsh/ytdexp/data/extractor/VideoExtractor.kt:53:9
```
Exception is a too generic Exception. Prefer throwing specific exceptions that indicate a specific error case.
```
```kotlin
50         if (matcher.find()) {
51             return JSONObject(matcher.group(1))
52         }
53         throw Exception("Could not find player response")
!!         ^ error
54     }
55
56     private fun parseYouTubePlayerResponse(playerResponse: JSONObject): VideoInfo {

```

### naming, FunctionNaming (9)

Function names should follow the naming convention set in the configuration.

[Documentation](https://detekt.dev/docs/rules/naming#functionnaming)

* /app/app/src/main/java/com/aseelsh/ytdexp/ui/MainScreen.kt:18:5
```
Function names should match the pattern: [a-z][a-zA-Z0-9]*
```
```kotlin
15
16 @OptIn(ExperimentalMaterial3Api::class)
17 @Composable
18 fun MainScreen(
!!     ^ error
19     viewModel: MainViewModel = hiltViewModel()
20 ) {
21     var selectedTab by remember { mutableStateOf(0) }

```

* /app/app/src/main/java/com/aseelsh/ytdexp/ui/MainScreen.kt:88:5
```
Function names should match the pattern: [a-z][a-zA-Z0-9]*
```
```kotlin
85 }
86
87 @Composable
88 fun BrowserScreen() {
!!     ^ error
89     // TODO: Implement browser screen with WebView
90 }
91

```

* /app/app/src/main/java/com/aseelsh/ytdexp/ui/MainScreen.kt:93:5
```
Function names should match the pattern: [a-z][a-zA-Z0-9]*
```
```kotlin
90 }
91
92 @Composable
93 fun DownloadsScreen() {
!!     ^ error
94     // TODO: Implement downloads screen
95 }
96

```

* /app/app/src/main/java/com/aseelsh/ytdexp/ui/MainScreen.kt:98:5
```
Function names should match the pattern: [a-z][a-zA-Z0-9]*
```
```kotlin
95  }
96
97  @Composable
98  fun SettingsScreen() {
!!      ^ error
99      // TODO: Implement settings screen
100 }
101

```

* /app/app/src/main/java/com/aseelsh/ytdexp/ui/screens/BrowserScreen.kt:32:5
```
Function names should match the pattern: [a-z][a-zA-Z0-9]*
```
```kotlin
29
30 @OptIn(ExperimentalMaterial3Api::class)
31 @Composable
32 fun BrowserScreen(
!!     ^ error
33     url: String,
34     onUrlChange: (String) -> Unit,
35     onDownloadClick: (String, String) -> Unit,

```

* /app/app/src/main/java/com/aseelsh/ytdexp/ui/screens/BrowserScreen.kt:107:13
```
Function names should match the pattern: [a-z][a-zA-Z0-9]*
```
```kotlin
104 }
105
106 @Composable
107 private fun FormatButton(
!!!             ^ error
108     text: String,
109     format: String,
110     url: String,

```

* /app/app/src/main/java/com/aseelsh/ytdexp/ui/screens/DownloadsScreen.kt:17:5
```
Function names should match the pattern: [a-z][a-zA-Z0-9]*
```
```kotlin
14 import com.aseelsh.ytdexp.domain.model.DownloadStatus
15
16 @Composable
17 fun DownloadsScreen(
!!     ^ error
18     downloads: List<DownloadItem> = emptyList(),
19     onPauseResume: (String) -> Unit = {},
20     onCancel: (String) -> Unit = {}

```

* /app/app/src/main/java/com/aseelsh/ytdexp/ui/screens/DownloadsScreen.kt:38:5
```
Function names should match the pattern: [a-z][a-zA-Z0-9]*
```
```kotlin
35
36 @OptIn(ExperimentalMaterial3Api::class)
37 @Composable
38 fun DownloadItemCard(
!!     ^ error
39     downloadItem: DownloadItem,
40     onPauseResume: (String) -> Unit,
41     onCancel: (String) -> Unit

```

* /app/app/src/main/java/com/aseelsh/ytdexp/ui/theme/Theme.kt:27:5
```
Function names should match the pattern: [a-z][a-zA-Z0-9]*
```
```kotlin
24 )
25
26 @Composable
27 fun YTDexpTheme(
!!     ^ error
28     darkTheme: Boolean = isSystemInDarkTheme(),
29     dynamicColor: Boolean = true,
30     content: @Composable () -> Unit

```

### naming, VariableNaming (3)

Variable names should follow the naming convention set in the projects configuration.

[Documentation](https://detekt.dev/docs/rules/naming#variablenaming)

* /app/app/src/main/java/com/aseelsh/ytdexp/service/DownloadService.kt:18:17
```
Private variable names should match the pattern: (_)?[a-z][A-Za-z0-9]*
```
```kotlin
15 @AndroidEntryPoint
16 class DownloadService : Service() {
17     private val serviceScope = CoroutineScope(Dispatchers.IO + Job())
18     private val NOTIFICATION_CHANNEL_ID = "download_channel"
!!                 ^ error
19     private val NOTIFICATION_ID = 1
20     private val BUFFER_SIZE = 8192
21

```

* /app/app/src/main/java/com/aseelsh/ytdexp/service/DownloadService.kt:19:17
```
Private variable names should match the pattern: (_)?[a-z][A-Za-z0-9]*
```
```kotlin
16 class DownloadService : Service() {
17     private val serviceScope = CoroutineScope(Dispatchers.IO + Job())
18     private val NOTIFICATION_CHANNEL_ID = "download_channel"
19     private val NOTIFICATION_ID = 1
!!                 ^ error
20     private val BUFFER_SIZE = 8192
21
22     @Inject

```

* /app/app/src/main/java/com/aseelsh/ytdexp/service/DownloadService.kt:20:17
```
Private variable names should match the pattern: (_)?[a-z][A-Za-z0-9]*
```
```kotlin
17     private val serviceScope = CoroutineScope(Dispatchers.IO + Job())
18     private val NOTIFICATION_CHANNEL_ID = "download_channel"
19     private val NOTIFICATION_ID = 1
20     private val BUFFER_SIZE = 8192
!!                 ^ error
21
22     @Inject
23     lateinit var notificationManager: NotificationManager

```

### style, ForbiddenComment (3)

Flags a forbidden comment.

[Documentation](https://detekt.dev/docs/rules/style#forbiddencomment)

* /app/app/src/main/java/com/aseelsh/ytdexp/ui/MainScreen.kt:89:5
```
Forbidden TODO todo marker in comment, please do the changes.
```
```kotlin
86
87 @Composable
88 fun BrowserScreen() {
89     // TODO: Implement browser screen with WebView
!!     ^ error
90 }
91
92 @Composable

```

* /app/app/src/main/java/com/aseelsh/ytdexp/ui/MainScreen.kt:94:5
```
Forbidden TODO todo marker in comment, please do the changes.
```
```kotlin
91
92 @Composable
93 fun DownloadsScreen() {
94     // TODO: Implement downloads screen
!!     ^ error
95 }
96
97 @Composable

```

* /app/app/src/main/java/com/aseelsh/ytdexp/ui/MainScreen.kt:99:5
```
Forbidden TODO todo marker in comment, please do the changes.
```
```kotlin
96
97  @Composable
98  fun SettingsScreen() {
99      // TODO: Implement settings screen
!!      ^ error
100 }
101

```

### style, MagicNumber (15)

Report magic numbers. Magic number is a numeric literal that is not defined as a constant and hence it's unclear what the purpose of this number is. It's better to declare such numbers as constants and give them a proper name. By default, -1, 0, 1, and 2 are not considered to be magic numbers.

[Documentation](https://detekt.dev/docs/rules/style#magicnumber)

* /app/app/src/main/java/com/aseelsh/ytdexp/data/downloader/FileDownloader.kt:54:59
```
This expression contains a magic number. Consider defining it to a well named constant.
```
```kotlin
51                     while (input.read(buffer).also { bytesRead = it } != -1) {
52                         output.write(buffer, 0, bytesRead)
53                         totalBytesRead += bytesRead
54                         val progress = ((totalBytesRead * 100) / contentLength).toInt()
!!                                                           ^ error
55                         onProgress(progress)
56                     }
57                 }

```

* /app/app/src/main/java/com/aseelsh/ytdexp/data/media/MediaProcessor.kt:31:58
```
This expression contains a magic number. Consider defining it to a well named constant.
```
```kotlin
28                 val timeInMilliseconds = statistics.time
29                 val duration = statistics.duration
30                 if (duration > 0) {
31                     val progress = (timeInMilliseconds * 100 / duration).toInt()
!!                                                          ^ error
32                     onProgress(progress)
33                 }
34             }

```

* /app/app/src/main/java/com/aseelsh/ytdexp/data/media/MediaProcessor.kt:76:58
```
This expression contains a magic number. Consider defining it to a well named constant.
```
```kotlin
73                 val timeInMilliseconds = statistics.time
74                 val duration = statistics.duration
75                 if (duration > 0) {
76                     val progress = (timeInMilliseconds * 100 / duration).toInt()
!!                                                          ^ error
77                     onProgress(progress)
78                 }
79             }

```

* /app/app/src/main/java/com/aseelsh/ytdexp/di/AppModule.kt:38:29
```
This expression contains a magic number. Consider defining it to a well named constant.
```
```kotlin
35             .addInterceptor(HttpLoggingInterceptor().apply {
36                 level = HttpLoggingInterceptor.Level.BASIC
37             })
38             .connectTimeout(30, TimeUnit.SECONDS)
!!                             ^ error
39             .readTimeout(30, TimeUnit.SECONDS)
40             .writeTimeout(30, TimeUnit.SECONDS)
41             .build()

```

* /app/app/src/main/java/com/aseelsh/ytdexp/di/AppModule.kt:39:26
```
This expression contains a magic number. Consider defining it to a well named constant.
```
```kotlin
36                 level = HttpLoggingInterceptor.Level.BASIC
37             })
38             .connectTimeout(30, TimeUnit.SECONDS)
39             .readTimeout(30, TimeUnit.SECONDS)
!!                          ^ error
40             .writeTimeout(30, TimeUnit.SECONDS)
41             .build()
42     }

```

* /app/app/src/main/java/com/aseelsh/ytdexp/di/AppModule.kt:40:27
```
This expression contains a magic number. Consider defining it to a well named constant.
```
```kotlin
37             })
38             .connectTimeout(30, TimeUnit.SECONDS)
39             .readTimeout(30, TimeUnit.SECONDS)
40             .writeTimeout(30, TimeUnit.SECONDS)
!!                           ^ error
41             .build()
42     }
43

```

* /app/app/src/main/java/com/aseelsh/ytdexp/service/DownloadService.kt:20:31
```
This expression contains a magic number. Consider defining it to a well named constant.
```
```kotlin
17     private val serviceScope = CoroutineScope(Dispatchers.IO + Job())
18     private val NOTIFICATION_CHANNEL_ID = "download_channel"
19     private val NOTIFICATION_ID = 1
20     private val BUFFER_SIZE = 8192
!!                               ^ error
21
22     @Inject
23     lateinit var notificationManager: NotificationManager

```

* /app/app/src/main/java/com/aseelsh/ytdexp/service/DownloadService.kt:72:26
```
This expression contains a magic number. Consider defining it to a well named constant.
```
```kotlin
69         return NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
70             .setContentTitle("Downloading $fileName")
71             .setSmallIcon(R.drawable.ic_download)
72             .setProgress(100, progress, false)
!!                          ^ error
73             .setContentIntent(pendingIntent)
74             .setOngoing(true)
75             .build()

```

* /app/app/src/main/java/com/aseelsh/ytdexp/service/DownloadService.kt:99:64
```
This expression contains a magic number. Consider defining it to a well named constant.
```
```kotlin
96                              output.write(buffer, 0, bytes)
97                              downloadedBytes += bytes
98
99                              val progress = ((downloadedBytes * 100) / contentLength).toInt()
!!                                                                 ^ error
100                             updateNotification(fileName, progress)
101
102                             bytes = input.read(buffer)

```

* /app/app/src/main/java/com/aseelsh/ytdexp/ui/theme/Color.kt:5:22
```
This expression contains a magic number. Consider defining it to a well named constant.
```
```kotlin
2
3 import androidx.compose.ui.graphics.Color
4
5 val Purple80 = Color(0xFFD0BCFF)
!                      ^ error
6 val PurpleGrey80 = Color(0xFFCCC2DC)
7 val Pink80 = Color(0xFFEFB8C8)
8

```

* /app/app/src/main/java/com/aseelsh/ytdexp/ui/theme/Color.kt:6:26
```
This expression contains a magic number. Consider defining it to a well named constant.
```
```kotlin
3  import androidx.compose.ui.graphics.Color
4
5  val Purple80 = Color(0xFFD0BCFF)
6  val PurpleGrey80 = Color(0xFFCCC2DC)
!                           ^ error
7  val Pink80 = Color(0xFFEFB8C8)
8
9  val Purple40 = Color(0xFF6650a4)

```

* /app/app/src/main/java/com/aseelsh/ytdexp/ui/theme/Color.kt:7:20
```
This expression contains a magic number. Consider defining it to a well named constant.
```
```kotlin
4
5  val Purple80 = Color(0xFFD0BCFF)
6  val PurpleGrey80 = Color(0xFFCCC2DC)
7  val Pink80 = Color(0xFFEFB8C8)
!                     ^ error
8
9  val Purple40 = Color(0xFF6650a4)
10 val PurpleGrey40 = Color(0xFF625b71)

```

* /app/app/src/main/java/com/aseelsh/ytdexp/ui/theme/Color.kt:9:22
```
This expression contains a magic number. Consider defining it to a well named constant.
```
```kotlin
6  val PurpleGrey80 = Color(0xFFCCC2DC)
7  val Pink80 = Color(0xFFEFB8C8)
8
9  val Purple40 = Color(0xFF6650a4)
!                       ^ error
10 val PurpleGrey40 = Color(0xFF625b71)
11 val Pink40 = Color(0xFF7D5260)
12

```

* /app/app/src/main/java/com/aseelsh/ytdexp/ui/theme/Color.kt:10:26
```
This expression contains a magic number. Consider defining it to a well named constant.
```
```kotlin
7  val Pink80 = Color(0xFFEFB8C8)
8
9  val Purple40 = Color(0xFF6650a4)
10 val PurpleGrey40 = Color(0xFF625b71)
!!                          ^ error
11 val Pink40 = Color(0xFF7D5260)
12

```

* /app/app/src/main/java/com/aseelsh/ytdexp/ui/theme/Color.kt:11:20
```
This expression contains a magic number. Consider defining it to a well named constant.
```
```kotlin
8
9  val Purple40 = Color(0xFF6650a4)
10 val PurpleGrey40 = Color(0xFF625b71)
11 val Pink40 = Color(0xFF7D5260)
!!                    ^ error
12

```

### style, MaxLineLength (1)

Line detected, which is longer than the defined maximum line length in the code style.

[Documentation](https://detekt.dev/docs/rules/style#maxlinelength)

* /app/app/src/main/java/com/aseelsh/ytdexp/data/extractor/VideoExtractor.kt:30:1
```
Line detected, which is longer than the defined maximum line length in the code style.
```
```kotlin
27
28     private fun extractYouTubeVideoId(url: String): String {
29         val pattern = Pattern.compile(
30             "(?<=watch\\?v=|/videos/|embed/|youtu.be/|/v/|/e/|watch\\?v%3D|watch\\?feature=player_embedded&v=)[^#&?\\n]*",
!! ^ error
31         )
32         val matcher = pattern.matcher(url)
33         if (matcher.find()) {

```

### style, UnusedPrivateProperty (2)

Property is unused and should be removed.

[Documentation](https://detekt.dev/docs/rules/style#unusedprivateproperty)

* /app/app/src/main/java/com/aseelsh/ytdexp/data/downloader/FileDownloader.kt:21:17
```
Private property `activeDownloads` is unused.
```
```kotlin
18     @ApplicationContext private val context: Context,
19     private val okHttpClient: OkHttpClient,
20 ) {
21     private val activeDownloads = mutableMapOf<String, MutableStateFlow<Int>>()
!!                 ^ error
22
23     suspend fun downloadFile(
24         downloadItem: DownloadItem,

```

* /app/app/src/main/java/com/aseelsh/ytdexp/data/repository/DownloadRepositoryImpl.kt:20:17
```
Private property `mediaProcessor` is unused.
```
```kotlin
17 class DownloadRepositoryImpl @Inject constructor(
18     private val downloadDao: DownloadDao,
19     private val videoExtractor: VideoExtractor,
20     private val mediaProcessor: MediaProcessor,
!!                 ^ error
21 ) : DownloadRepository {
22
23     override suspend fun addDownload(url: String, format: String): Result<DownloadItem> {

```

### style, WildcardImport (14)

Wildcard imports should be replaced with imports using fully qualified class names. Wildcard imports can lead to naming conflicts. A library update can introduce naming clashes with your classes which results in compilation errors.

[Documentation](https://detekt.dev/docs/rules/style#wildcardimport)

* /app/app/src/main/java/com/aseelsh/ytdexp/service/DownloadService.kt:3:1
```
android.app.* is a wildcard import. Replace it with fully qualified imports.
```
```kotlin
1 package com.aseelsh.ytdexp.service
2
3 import android.app.*
! ^ error
4 import android.content.Intent
5 import android.os.*
6 import androidx.core.app.NotificationCompat

```

* /app/app/src/main/java/com/aseelsh/ytdexp/service/DownloadService.kt:5:1
```
android.os.* is a wildcard import. Replace it with fully qualified imports.
```
```kotlin
2
3 import android.app.*
4 import android.content.Intent
5 import android.os.*
! ^ error
6 import androidx.core.app.NotificationCompat
7 import com.aseelsh.ytdexp.R
8 import com.aseelsh.ytdexp.ui.MainActivity

```

* /app/app/src/main/java/com/aseelsh/ytdexp/service/DownloadService.kt:10:1
```
kotlinx.coroutines.* is a wildcard import. Replace it with fully qualified imports.
```
```kotlin
7  import com.aseelsh.ytdexp.R
8  import com.aseelsh.ytdexp.ui.MainActivity
9  import dagger.hilt.android.AndroidEntryPoint
10 import kotlinx.coroutines.*
!! ^ error
11 import okhttp3.*
12 import java.io.*
13 import javax.inject.Inject

```

* /app/app/src/main/java/com/aseelsh/ytdexp/service/DownloadService.kt:11:1
```
okhttp3.* is a wildcard import. Replace it with fully qualified imports.
```
```kotlin
8  import com.aseelsh.ytdexp.ui.MainActivity
9  import dagger.hilt.android.AndroidEntryPoint
10 import kotlinx.coroutines.*
11 import okhttp3.*
!! ^ error
12 import java.io.*
13 import javax.inject.Inject
14

```

* /app/app/src/main/java/com/aseelsh/ytdexp/service/DownloadService.kt:12:1
```
java.io.* is a wildcard import. Replace it with fully qualified imports.
```
```kotlin
9  import dagger.hilt.android.AndroidEntryPoint
10 import kotlinx.coroutines.*
11 import okhttp3.*
12 import java.io.*
!! ^ error
13 import javax.inject.Inject
14
15 @AndroidEntryPoint

```

* /app/app/src/main/java/com/aseelsh/ytdexp/ui/MainScreen.kt:3:1
```
androidx.compose.foundation.layout.* is a wildcard import. Replace it with fully qualified imports.
```
```kotlin
1 package com.aseelsh.ytdexp.ui
2
3 import androidx.compose.foundation.layout.*
! ^ error
4 import androidx.compose.material3.*
5 import androidx.compose.runtime.*
6 import androidx.compose.ui.Modifier

```

* /app/app/src/main/java/com/aseelsh/ytdexp/ui/MainScreen.kt:4:1
```
androidx.compose.material3.* is a wildcard import. Replace it with fully qualified imports.
```
```kotlin
1 package com.aseelsh.ytdexp.ui
2
3 import androidx.compose.foundation.layout.*
4 import androidx.compose.material3.*
! ^ error
5 import androidx.compose.runtime.*
6 import androidx.compose.ui.Modifier
7 import androidx.compose.material.icons.Icons

```

* /app/app/src/main/java/com/aseelsh/ytdexp/ui/MainScreen.kt:5:1
```
androidx.compose.runtime.* is a wildcard import. Replace it with fully qualified imports.
```
```kotlin
2
3 import androidx.compose.foundation.layout.*
4 import androidx.compose.material3.*
5 import androidx.compose.runtime.*
! ^ error
6 import androidx.compose.ui.Modifier
7 import androidx.compose.material.icons.Icons
8 import androidx.compose.material.icons.filled.*

```

* /app/app/src/main/java/com/aseelsh/ytdexp/ui/MainScreen.kt:8:1
```
androidx.compose.material.icons.filled.* is a wildcard import. Replace it with fully qualified imports.
```
```kotlin
5  import androidx.compose.runtime.*
6  import androidx.compose.ui.Modifier
7  import androidx.compose.material.icons.Icons
8  import androidx.compose.material.icons.filled.*
!  ^ error
9  import androidx.compose.material3.Tab
10 import androidx.compose.material3.TabRow
11 import androidx.compose.material3.Icon

```

* /app/app/src/main/java/com/aseelsh/ytdexp/ui/screens/DownloadsScreen.kt:3:1
```
androidx.compose.foundation.layout.* is a wildcard import. Replace it with fully qualified imports.
```
```kotlin
1 package com.aseelsh.ytdexp.ui.screens
2
3 import androidx.compose.foundation.layout.*
! ^ error
4 import androidx.compose.foundation.lazy.LazyColumn
5 import androidx.compose.foundation.lazy.items
6 import androidx.compose.material.icons.Icons

```

* /app/app/src/main/java/com/aseelsh/ytdexp/ui/screens/DownloadsScreen.kt:7:1
```
androidx.compose.material.icons.filled.* is a wildcard import. Replace it with fully qualified imports.
```
```kotlin
4  import androidx.compose.foundation.lazy.LazyColumn
5  import androidx.compose.foundation.lazy.items
6  import androidx.compose.material.icons.Icons
7  import androidx.compose.material.icons.filled.*
!  ^ error
8  import androidx.compose.material3.*
9  import androidx.compose.runtime.*
10 import androidx.compose.ui.Alignment

```

* /app/app/src/main/java/com/aseelsh/ytdexp/ui/screens/DownloadsScreen.kt:8:1
```
androidx.compose.material3.* is a wildcard import. Replace it with fully qualified imports.
```
```kotlin
5  import androidx.compose.foundation.lazy.items
6  import androidx.compose.material.icons.Icons
7  import androidx.compose.material.icons.filled.*
8  import androidx.compose.material3.*
!  ^ error
9  import androidx.compose.runtime.*
10 import androidx.compose.ui.Alignment
11 import androidx.compose.ui.Modifier

```

* /app/app/src/main/java/com/aseelsh/ytdexp/ui/screens/DownloadsScreen.kt:9:1
```
androidx.compose.runtime.* is a wildcard import. Replace it with fully qualified imports.
```
```kotlin
6  import androidx.compose.material.icons.Icons
7  import androidx.compose.material.icons.filled.*
8  import androidx.compose.material3.*
9  import androidx.compose.runtime.*
!  ^ error
10 import androidx.compose.ui.Alignment
11 import androidx.compose.ui.Modifier
12 import androidx.compose.ui.unit.dp

```

* /app/app/src/main/java/com/aseelsh/ytdexp/ui/theme/Theme.kt:6:1
```
androidx.compose.material3.* is a wildcard import. Replace it with fully qualified imports.
```
```kotlin
3  import android.app.Activity
4  import android.os.Build
5  import androidx.compose.foundation.isSystemInDarkTheme
6  import androidx.compose.material3.*
!  ^ error
7  import androidx.compose.runtime.Composable
8  import androidx.compose.runtime.SideEffect
9  import androidx.compose.ui.graphics.toArgb

```

generated with [detekt version 1.23.8](https://detekt.dev/) on 2025-08-25 01:09:24 UTC
