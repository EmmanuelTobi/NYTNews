package com.cosmic.nytnews

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.magnifier
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.request.CachePolicy
import coil.request.ImageRequest
import coil.transform.RoundedCornersTransformation
import com.cosmic.nytnews.model.NYTimesNewsModel
import com.cosmic.nytnews.ui.theme.NYTNewsTheme
import com.cosmic.nytnews.utils.AppDateUtils
import com.cosmic.nytnews.utils.ResultState
import com.cosmic.nytnews.viewmodel.NewsViewmodel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NYTNewsTheme {
                var newsDays by remember { mutableStateOf("Yesterday") }

                Column {
                    Row (
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "NYTimes News",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 30.sp,
                                modifier = Modifier.padding(top = 26.dp, start = 16.dp),
                            )
                            Text(
                                newsDays,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(start = 16.dp),
                            )
                        }
                        MinimalDropdownMenu(onDaysSelected = {
                            Log.d("MainActivity", "onCreate: $it")
                            newsDays = it
                            Unit
                        })
                    }
                    NewsBody()
                }
            }
        }
    }
}

@Composable
fun NewsBody(newsViewModel: NewsViewmodel = viewModel()) {

    val posts by newsViewModel.getNewsPosts.observeAsState()
    var selectedNewsItem by remember { mutableStateOf<NYTimesNewsModel.Result?>(null) }
    var showBottomSheet by remember { mutableStateOf(false) }

    posts.let {
        when(it) {
            is ResultState.Success -> {

                LazyColumn (
                    modifier = Modifier.testTag("NewsItems"),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(it.data.results) { item ->
                        Column (
                            modifier = Modifier.clickable {
                                selectedNewsItem = item
                                showBottomSheet = true
                            }
                        ) {
                            NewsPostListItem(
                                item.title,
                                item.abstract,
                                item.publishedDate
                            )
                            HorizontalDivider(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 10.dp, bottom = 10.dp),
                                color = Color.LightGray
                            )
                        }
                    }
                }

            }
            is ResultState.Error -> {
                Text(text = "Error: ${it.message}")
                Log.d("MainActivity", "Greeting: ${it.message}")
            }
            is ResultState.Loading -> {
                LoadingView()
            }
            null -> TODO()
        }
    }

    if (showBottomSheet && selectedNewsItem != null) {
        NewsItemBottomSheet(selectedNewsItem = selectedNewsItem!!, onDismiss = {showBottomSheet = false})
    }

}

@Composable
fun NewsPostListItem(title:String, abstract:String, date: String) {
    Column {

        Text(
            text = AppDateUtils.formatDateToReadableWords(date),
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Gray
        )
        Text(
            text = title,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = abstract,
            fontSize = 14.sp,
            color = Color.DarkGray
        )
    }
}

@Composable
fun MinimalDropdownMenu(onDaysSelected: ((days:String) -> Unit?)?, newsViewModel: NewsViewmodel = viewModel()) {

    var expanded by remember { mutableStateOf(false) }
    var newsDays by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .padding(16.dp)
            .padding(top = 10.dp)
    ) {
        IconButton(onClick = { expanded = !expanded }) {
            Icon(Icons.Default.MoreVert, contentDescription = "More options")
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }
        ) {
            DropdownMenuItem(
                text = { Text("Yesterday") },
                onClick = {

                    if(newsDays != "Yesterday") {
                        newsViewModel.fetchNewsPostsLast1Day()
                    }

                    newsDays = "Yesterday"
                    onDaysSelected?.let { it(newsDays) }
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("Last 7 days") },
                onClick = {
                    newsViewModel.fetchNewsPostsLast7Days()
                    newsDays = "Last 7 days"
                    onDaysSelected?.let { it(newsDays) }
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("Last 30 days") },
                onClick = {
                    newsViewModel.fetchNewsPostsLast30Days()
                    newsDays = "Last 30 days"
                    onDaysSelected?.let { it(newsDays) }
                    expanded = false
                }
            )
        }
    }
}

@Composable
fun LoadingView() {
    Box(
        modifier = Modifier
            .testTag("LoadingIndicator")
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            LinearProgressIndicator(
                modifier = Modifier
                    .width(100.dp)
                    .height(5.dp)
                    .clip(RoundedCornerShape(10.dp)),
                color = MaterialTheme.colorScheme.primary,
                trackColor = Color.DarkGray
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Loading...", fontWeight = FontWeight.Bold)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsItemBottomSheet(selectedNewsItem: NYTimesNewsModel.Result, onDismiss: () -> Unit) {
    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = {
            onDismiss()
        },
        sheetState = sheetState,
        dragHandle = null,
        windowInsets = WindowInsets(0,0,0,0),
        modifier = Modifier.background(Color.Transparent)
    ) {
        // Sheet content
        Column(modifier = Modifier.padding(16.dp)) {
            Spacer(modifier = Modifier.height(4.dp))
            if(selectedNewsItem.media.isNotEmpty()) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(selectedNewsItem.media[0].mediaMetadata[selectedNewsItem.media[0].mediaMetadata.size - 1].url)
                        .memoryCachePolicy(CachePolicy.ENABLED)
                        .diskCachePolicy(CachePolicy.ENABLED)
                        .transformations(RoundedCornersTransformation(16.dp.value))
                        .build(),
                    contentDescription = "News Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    onState = { state ->
                        when (state) {
                            is AsyncImagePainter.State.Error -> {
                                // Handle the error, e.g., display an error image
                                Log.e("AsyncImage", "Error loading image: ${state.result.throwable}")
                            }

                            is AsyncImagePainter.State.Loading -> {
                                Log.e("AsyncImage", "Loading image")
                            }

                            is AsyncImagePainter.State.Success -> {
                                // Image loaded successfully
                            }

                            is AsyncImagePainter.State.Empty -> {
                                // Handle the empty state
                                Log.e("AsyncImage", "Empty image state")
                            }
                        }
                    },
                )
            }
            Spacer(modifier = Modifier.height(34.dp))
            Text(
                text = selectedNewsItem.title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = selectedNewsItem.abstract,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row (
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = AppDateUtils.formatDateToReadableWords(selectedNewsItem.publishedDate),
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                OutlinedButton(
                    onClick = {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(selectedNewsItem.url))
                        context.startActivity(intent)
                    }
                ) {
                    Text(text = "Read more")
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    NYTNewsTheme {
        Column {
            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "NYTimes News",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 30.sp,
                        modifier = Modifier.padding(top = 16.dp, start = 16.dp),
                    )
                    Text(
                        "Last 30 days",
                        fontSize = 10.sp,
                        modifier = Modifier.padding(start = 16.dp),
                    )
                }
                MinimalDropdownMenu(onDaysSelected = {

                })
            }
            NewsBody()
        }
    }
}