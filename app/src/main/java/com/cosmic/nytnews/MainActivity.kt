package com.cosmic.nytnews

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cosmic.nytnews.model.SampleModels
import com.cosmic.nytnews.ui.theme.NYTNewsTheme
import com.cosmic.nytnews.viewmodel.NewsViewmodel
import androidx.lifecycle.viewmodel.compose.viewModel


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Spacer(modifier = Modifier
                .width(50.dp)
                .height(50.dp)
            )
            NYTNewsTheme {
                Greeting()
            }
        }
    }
}

@Composable
fun Greeting(newsViewModel: NewsViewmodel = viewModel()) {

    val posts by newsViewModel.getNewsPosts.observeAsState()
    val error by newsViewModel.error.observeAsState()

    if (error != null) {

        Text(text = "Error: $error")

    } else {

        if (posts != null) {

            LazyColumn (
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(16.dp)
            ) {
                items(posts!!.results) { item ->
                    NewsPostListItem( item.title, item.abstract)
                }
            }

        } else {

            Text(text = "Currently no news posts at the moment")

        }

    }

}

@Composable
fun NewsPostListItem(title:String, abstract:String) {
    Column {
        Text(
            text = title,
            fontSize = 24.sp
        )
        Text(
            text = abstract,
            fontSize = 14.sp,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    NYTNewsTheme {
        Greeting()
    }
}