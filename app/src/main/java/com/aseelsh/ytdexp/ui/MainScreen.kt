package com.aseelsh.ytdexp.ui

import androidx.compose.foundation.layout.Column
<<<<<<< HEAD
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
=======
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
>>>>>>> origin/main
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
<<<<<<< HEAD
=======
import androidx.compose.runtime.collectAsState
>>>>>>> origin/main
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
<<<<<<< HEAD
=======
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
>>>>>>> origin/main
import androidx.hilt.navigation.compose.hiltViewModel
import com.aseelsh.ytdexp.ui.screens.BrowserScreen
import com.aseelsh.ytdexp.ui.screens.DownloadsScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
<<<<<<< HEAD
fun MainScreen(viewModel: MainViewModel = hiltViewModel()) {
    var tabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("Browser", "Downloads")
=======
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel()
) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Browser", "Downloads", "Settings")

    val downloadState by viewModel.downloadState.collectAsState()
    val currentUrl by viewModel.currentUrl.collectAsState()
>>>>>>> origin/main

    Scaffold(
        topBar = {
            MainTopAppBar()
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            UrlInputField(
                url = currentUrl,
                onUrlChange = { viewModel.updateUrl(it) },
                onDownloadClick = { viewModel.startDownload() }
            )

            MainTabRow(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it },
                tabs = tabs
            )

            when (selectedTab) {
                0 -> BrowserScreen()
                1 -> DownloadsScreen()
                2 -> {/* Settings Screen placeholder */}
            }
        }
    }
}

<<<<<<< HEAD
// Screens moved to their respective files in ui/screens/
=======
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainTopAppBar() {
    CenterAlignedTopAppBar(
        title = { Text("YT-Dexp") },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors()
    )
}

@Composable
private fun UrlInputField(
    url: String,
    onUrlChange: (String) -> Unit,
    onDownloadClick: () -> Unit
) {
    OutlinedTextField(
        value = url,
        onValueChange = onUrlChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        label = { Text("Enter URL") },
        trailingIcon = {
            IconButton(onClick = onDownloadClick) {
                Icon(Icons.Default.Download, "Download")
            }
        }
    )
}

@Composable
private fun MainTabRow(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    tabs: List<String>
) {
    TabRow(selectedTabIndex = selectedTab) {
        tabs.forEachIndexed { index, title ->
            Tab(
                selected = selectedTab == index,
                onClick = { onTabSelected(index) },
                text = {
                    Text(
                        text = title,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                icon = {
                    when (index) {
                        0 -> Icon(Icons.Default.Search, contentDescription = null)
                        1 -> Icon(Icons.Default.Download, contentDescription = null)
                        2 -> Icon(Icons.Default.Settings, contentDescription = null)
                    }
                }
            )
        }
    }
}

@Composable
fun SettingsScreen() {
    // Empty placeholder
}
>>>>>>> origin/main
