package com.ignotusvia.speechbuddy.view.screen

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.ignotusvia.speechbuddy.viewmodel.LearningPathViewModel

@Composable
fun LearningPathScreen() {
    val viewModel: LearningPathViewModel = hiltViewModel()

    var expandedModule by remember { mutableStateOf<String?>(null) }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        Text(
            text = "Your Learning Path",
            style = MaterialTheme.typography.h5,
            modifier = Modifier.align(Alignment.Start)
        )
        Spacer(modifier = Modifier.height(20.dp))

        LazyColumn {
            items(learningModules) { module ->
                LearningModuleItem(moduleName = module.first, isExpanded = expandedModule == module.first) {
                    expandedModule = if (expandedModule == module.first) null else module.first
                }
                if (expandedModule == module.first) {
                    WebViewComponent(videoId = module.second)
                }
            }
        }
    }
}

@Composable
fun LearningModuleItem(moduleName: String, isExpanded: Boolean, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() },
        elevation = 4.dp
    ) {
        Column {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = moduleName, style = MaterialTheme.typography.subtitle1)
                IconButton(onClick = { onClick() }) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                        contentDescription = "Expand or Collapse"
                    )
                }
            }
            if (isExpanded) {
                Text("Video player will be here", Modifier.padding(16.dp))
            }
        }
    }
}

@Composable
fun WebViewComponent(videoId: String) {
    val htmlContent = createHtmlContent(videoId)
    AndroidView(
        modifier = Modifier
            .height(200.dp)
            .fillMaxWidth(),
        factory = { context ->
            WebView(context).apply {
                webViewClient = WebViewClient()
                settings.javaScriptEnabled = true
                loadData(htmlContent, "text/html", "UTF-8")
            }
        }
    )
}

fun createHtmlContent(videoId: String): String {
    return """
        <!DOCTYPE html>
        <html>
          <body>
            <div id="player"></div>
            <script>
              var tag = document.createElement('script');
              tag.src = "https://www.youtube.com/iframe_api";
              var firstScriptTag = document.getElementsByTagName('script')[0];
              firstScriptTag.parentNode.insertBefore(tag, firstScriptTag);

              var player;
              function onYouTubeIframeAPIReady() {
                player = new YT.Player('player', {
                  height: '100%',
                  width: '100%',
                  videoId: '$videoId',
                  playerVars: { 'autoplay': 1, 'controls': 1 },
                  events: {
                    'onReady': onPlayerReady,
                    'onStateChange': onPlayerStateChange
                  }
                });
              }

              function onPlayerReady(event) {
                event.target.playVideo();
              }

              function onPlayerStateChange(event) {
                if (event.data == YT.PlayerState.ENDED) {
                  // Video has ended
                }
              }
              function stopVideo() {
                player.stopVideo();
              }
            </script>
          </body>
        </html>
    """.trimIndent()
}

val learningModules = listOf(
    "Module 1" to "VeH03PzD25E",
    "Module 2" to "ujDtm0hZyII",
    "Module 3" to "xg60VxyK-9I"
)

