package com.wishes.jetpackcompose.runtime


sealed class NavRoutes(val route: String) {
    object Upload : NavRoutes("upload")
    object Share : NavRoutes("share")
    object Splash : NavRoutes("splash")
}


