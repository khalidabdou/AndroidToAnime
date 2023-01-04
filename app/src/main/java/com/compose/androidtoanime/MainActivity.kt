package com.compose.androidtoanime

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.compose.androidtoanime.ui.theme.AndroidToAnimeTheme
import com.compose.androidtoanime.viewmodels.ViewModel
import com.wishes.jetpackcompose.runtime.NavigationHost
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidToAnimeTheme {
                // A surface container using the 'background' color from the theme
                val viewModel: ViewModel = hiltViewModel()
                val navController = rememberNavController()
                NavigationHost(navController,viewModel)
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    AndroidToAnimeTheme {
        Greeting("Android")
    }
}