package com.usefulness.slidr.example

import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SlidableContainer(
    enabled: Boolean,
    background: @Composable () -> Unit,
    foreground: @Composable () -> Unit,
    orientation: Orientation = Orientation.Horizontal,
    onDismiss: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    lateinit var swipeToEndCallback: suspend () -> Unit
    lateinit var swipeToStartCallback: suspend () -> Unit
    val swipeableState = rememberSwipeableState(
        initialValue = SwipeToGoBackState.Default,
        confirmStateChange = { state ->
            when (state) {
                SwipeToGoBackState.Default -> true
                SwipeToGoBackState.SwipedEnd -> {
                    scope.launch {
                        swipeToEndCallback()
                        onDismiss()
                    }
                    true
                }
                SwipeToGoBackState.SwipedStart -> {
                    scope.launch {
                        swipeToStartCallback()
                        onDismiss()
                    }
                    true
                }
            }
        },
    )
    swipeToEndCallback = { swipeableState.animateTo(targetValue = SwipeToGoBackState.SwipedEnd, tween()) }
    swipeToStartCallback = { swipeableState.animateTo(targetValue = SwipeToGoBackState.SwipedStart, tween()) }
    val configuration = LocalConfiguration.current
    val screenSize = when (orientation) {
        Orientation.Vertical -> configuration.screenHeightDp
        Orientation.Horizontal -> configuration.screenWidthDp
    }.dp
    val density = LocalDensity.current.density
    val screenWidthPx = screenSize.times(density).value

    Box(
        modifier = Modifier.swipeable(
            enabled = enabled,
            state = swipeableState,
            anchors = mapOf(
                -screenWidthPx to SwipeToGoBackState.SwipedStart,
                0f to SwipeToGoBackState.Default,
                screenWidthPx to SwipeToGoBackState.SwipedEnd,
            ),
            thresholds = { _, _ -> FractionalThreshold(0.5f) },
            orientation = orientation,
        ),
    ) {
        if (enabled && swipeableState.offset.value.absoluteValue > 0) {
            background()
        }
        Box(
            modifier = Modifier.offset {
                when (orientation) {
                    Orientation.Vertical -> IntOffset(0, swipeableState.offset.value.roundToInt())
                    Orientation.Horizontal -> IntOffset(swipeableState.offset.value.roundToInt(), 0)
                }
            },
        ) {
            foreground()
        }
    }
}

enum class SwipeToGoBackState {
    SwipedStart,
    Default,
    SwipedEnd,
}
