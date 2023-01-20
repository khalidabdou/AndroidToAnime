package com.compose.androidtoanime.Utils

import androidx.compose.animation.*
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp

@Composable
fun Any?.animateVisibility() {
    var editable = false
    LaunchedEffect(key1 = Unit) {
        editable = true
    }
    val offsetText by animateDpAsState(
        targetValue = if (editable) 0.dp else 400.dp,
        tween(1000)
    )
    val density = LocalDensity.current
    AnimatedVisibility(
        visible = editable,
        enter = slideInVertically {
            // Slide in from 40 dp from the top.
            with(density) { -40.dp.roundToPx() }
        } + expandVertically(
            // Expand from the top.
            expandFrom = Alignment.Top
        ) + fadeIn(
            // Fade in with the initial alpha of 0.3f.
            initialAlpha = 0.3f
        ),
        exit = slideOutVertically() + shrinkVertically() + fadeOut()
    ) {
        Text(text = "cjsjs")

        this@animateVisibility


    }


}