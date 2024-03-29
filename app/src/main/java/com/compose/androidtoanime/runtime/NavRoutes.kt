package com.wishes.jetpackcompose.runtime


sealed class NavRoutes(val route: String) {

    object Home : NavRoutes("home")
    object Upload : NavRoutes("upload")
    object Share : NavRoutes("share")
    object MyPhotos : NavRoutes("my_photos")
    object Premium : NavRoutes("premium")
    object Splash : NavRoutes("splash")
    object Chat : NavRoutes("chat")
}


