package com.usefulness.slidr.example

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SlidableContainer(
    enabled: Boolean,
    background: @Composable () -> Unit,
    foreground: @Composable () -> Unit,
    onDismissed: () -> Boolean,
) {
    val swipeableState = rememberSwipeableState(
        initialValue = SwipeToGoBackState.Default,
        confirmStateChange = {
            when (it) {
                SwipeToGoBackState.Default -> true
                SwipeToGoBackState.SwipedOut -> onDismissed()
            }
        }
    )

    val configuration = LocalConfiguration.current
    val screenSize = configuration.screenWidthDp.dp
    val density = LocalDensity.current.density

    Box(
        modifier = Modifier.swipeable(
            enabled = enabled,
            state = swipeableState,
            anchors = mapOf(
                0f to SwipeToGoBackState.Default,
                screenSize.times(density).value to SwipeToGoBackState.SwipedOut,
            ),
            thresholds = { _, _ -> FractionalThreshold(0.7f) },
            orientation = Orientation.Horizontal,
        ),
    ) {
        if (enabled && swipeableState.offset.value > 0) {
            background()
        }
        Box(
            modifier = Modifier.offset { IntOffset(swipeableState.offset.value.roundToInt(), 0) },
        ) {
            foreground()
        }
    }
}

enum class SwipeToGoBackState {
    Default,
    SwipedOut
}
