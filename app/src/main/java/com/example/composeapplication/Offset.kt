package com.example.composeapplication

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.OverscrollEffect
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.overscroll
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.Velocity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.roundToInt
import kotlin.math.sign

@OptIn(ExperimentalFoundationApi::class)
// our custom offset overscroll that offset the element it is applied to when we hit the bound
// on the scrollable container.
class OffsetOverscrollEffect(val scope: CoroutineScope) : OverscrollEffect {
    private val overscrollOffset = Animatable(0f)
    override fun consumePreScroll(
        scrollDelta: Offset,
        pointerPosition: Offset?,
        source: NestedScrollSource
    ): Offset {
        // in pre scroll we relax the overscroll if needed
        // relaxation: when we are in progress of the overscroll and user scrolls in the
        // different direction = substract the overscroll first
        val sameDirection = sign(scrollDelta.y) == sign(overscrollOffset.value)
        return if (abs(overscrollOffset.value) > 0.5 && !sameDirection && isEnabled) {
            val prevOverscrollValue = overscrollOffset.value
            val newOverscrollValue = overscrollOffset.value + scrollDelta.y
            if (sign(prevOverscrollValue) != sign(newOverscrollValue)) {
                // sign changed, coerce to start scrolling and exit
                scope.launch { overscrollOffset.snapTo(0f) }
                Offset(x = 0f, y = scrollDelta.y + prevOverscrollValue)
            } else {
                scope.launch {
                    overscrollOffset.snapTo(overscrollOffset.value + scrollDelta.y)
                }
                scrollDelta.copy(x = 0f)
            }
        } else {
            Offset.Zero
        }
    }

    override fun consumePostScroll(
        initialDragDelta: Offset,
        overscrollDelta: Offset,
        pointerPosition: Offset?,
        source: NestedScrollSource
    ) {
        // if it is a drag, not a fling, add the delta left to our over scroll value
        if (abs(overscrollDelta.y) > 0.5 && isEnabled && source == NestedScrollSource.Drag) {
            scope.launch {
                // multiply by 0.1 for the sake of parallax effect
                overscrollOffset.snapTo(overscrollOffset.value + overscrollDelta.y * 0.1f)
            }
        }
    }

    override suspend fun consumePreFling(velocity: Velocity): Velocity = Velocity.Zero

    override suspend fun consumePostFling(velocity: Velocity) {
        // when the fling happens - we just gradually animate our overscroll to 0
        if (isEnabled) {
            overscrollOffset.animateTo(
                targetValue = 0f,
                initialVelocity = velocity.y,
                animationSpec = spring()
            )
        }
    }

    override var isEnabled: Boolean by mutableStateOf(true)

    override val isInProgress: Boolean
        get() = overscrollOffset.isRunning

    // as we're building an offset modifiers, let's offset of our value we calculated
    override val effectModifier: Modifier = Modifier.offset {
        IntOffset(x = 0, y = overscrollOffset.value.roundToInt())
    }
}

