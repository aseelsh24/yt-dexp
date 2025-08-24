package com.aseelsh.ytdexp.ui.screens

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberWebViewState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BrowserScreen(
    url: String,
    onUrlChange: (String) -> Unit,
    onDownloadClick: (String, String) -> Unit,
    isUrlValid: Boolean,
    modifier: Modifier = Modifier
) {
    var showFormatDialog by remember { mutableStateOf(false) }
    var currentVideoUrl by remember { mutableStateOf("") }
    
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // URL Input Field
        OutlinedTextField(
            value = url,
            onValueChange = onUrlChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            label = { Text("Enter URL") },
            trailingIcon = {
                if (isUrlValid) {
                    IconButton(onClick = { 
                        currentVideoUrl = url
                        showFormatDialog = true 
                    }) {
                        Icon(Icons.Default.Download, "Download")
                    }
                }
            }
        )

        // WebView
        if (url.isNotEmpty()) {
            val webViewState = rememberWebViewState(url = url)
            
            WebView(
                state = webViewState,
                modifier = Modifier.weight(1f),
                onCreated = { webView ->
                    webView.settings.apply {
                        javaScriptEnabled = true
                        domStorageEnabled = true
                        databaseEnabled = true
                    }
                }
            )
        }
    }

    if (showFormatDialog) {
        AlertDialog(
            onDismissRequest = { showFormatDialog = false },
            title = { Text("Choose Format") },
            text = {
                Column {
                    Text("Select download format:")
                    Spacer(modifier = Modifier.height(8.dp))
                    FormatButton("MP4 - Video", "mp4", currentVideoUrl, onDownloadClick)
                    FormatButton("MP3 - Audio", "mp3", currentVideoUrl, onDownloadClick)
                    FormatButton("WEBM - Video", "webm", currentVideoUrl, onDownloadClick)
                }
            },
            confirmButton = {
                TextButton(onClick = { showFormatDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun FormatButton(
    text: String,
    format: String,
    url: String,
    onDownloadClick: (String, String) -> Unit
) {
    Button(
        onClick = {
            onDownloadClick(url, format)
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(text)
    }
}
