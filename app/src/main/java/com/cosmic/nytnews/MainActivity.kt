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
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cosmic.nytnews.model.SampleModels
import com.cosmic.nytnews.ui.theme.NYTNewsTheme

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
                Greeting(
                    greetings = listOf(
                        SampleModels.Greetings(greetings = "Hello", phoneNumber = "123-456-7890"),
                        SampleModels.Greetings(greetings = "We are sending this to you", phoneNumber = "123-456-7890"),
                        SampleModels.Greetings(greetings = "Hello", phoneNumber = "123-456-7890"),
                        SampleModels.Greetings(greetings = "Hello", phoneNumber = "123-456-7890"),
                        SampleModels.Greetings(greetings = "We are sending this to you", phoneNumber = "123-456-7890"),
                    )
                )
            }
        }
    }
}

@Composable
fun Greeting(greetings: List<SampleModels.Greetings>? = null) {

    if (greetings != null) {

        LazyColumn (
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(greetings) { item ->
                GreetingsView( SampleModels.Greetings(
                    greetings = item.greetings,
                    phoneNumber = item.phoneNumber,
                ))
            }
        }

    } else {

        GreetingsView(SampleModels.Greetings(
            greetings = "We are sending this to you",
            phoneNumber = "123-456-7890",
        ))

    }

}

@Composable
fun GreetingsView(samples: SampleModels.Greetings? = null) {
    Column {
        Text(
            text = samples!!.greetings!!,
            fontSize = 24.sp
        )
        Text(
            text = samples.phoneNumber!!,
            fontSize = 14.sp,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    NYTNewsTheme {
        Greeting(greetings = listOf(
            SampleModels.Greetings(greetings = "Hello", phoneNumber = "123-456-7890"),
            SampleModels.Greetings(greetings = "We are sending this to you", phoneNumber = "123-456-7890"),
            SampleModels.Greetings(greetings = "Hello", phoneNumber = "123-456-7890"),
            SampleModels.Greetings(greetings = "Hello", phoneNumber = "123-456-7890"),
        ))
    }
}