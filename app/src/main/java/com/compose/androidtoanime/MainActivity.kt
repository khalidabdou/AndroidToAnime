package com.compose.androidtoanime

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.compose.androidtoanime.screens.TopBar
import com.compose.androidtoanime.ui.theme.AndroidToAnimeTheme
import com.compose.androidtoanime.viewmodels.ViewModel
import com.wishes.jetpackcompose.runtime.NavigationHost
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidToAnimeTheme {
                // A surface container using the 'background' color from the theme
                val viewModel: ViewModel = hiltViewModel()
                val navController = rememberNavController()
                Scaffold(modifier = Modifier.fillMaxSize(),topBar = { TopBar() }) {
                    NavigationHost(navController,viewModel)
                }


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