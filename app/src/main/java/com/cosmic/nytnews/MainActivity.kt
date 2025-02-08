package com.cosmic.nytnews

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cosmic.nytnews.ui.theme.NYTNewsTheme
import com.cosmic.nytnews.viewmodel.NewsViewmodel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cosmic.nytnews.utils.AppDateUtils
import com.cosmic.nytnews.utils.ResultState


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
                    Greeting()
                }
            }
        }
    }
}

@Composable
fun Greeting(newsViewModel: NewsViewmodel = viewModel()) {

    val posts by newsViewModel.getNewsPosts.observeAsState()

    posts.let {
        when(it) {
            is ResultState.Success -> {

                LazyColumn (
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(it.data.results) { item ->
                        Column {
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
            Greeting()
        }
    }
}