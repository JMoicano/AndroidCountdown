package com.example.androiddevchallenge.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.util.concurrent.TimeUnit

@Composable
fun Hourglass(
    isActive: Boolean
) {
    val infiniteTransition = rememberInfiniteTransition()
    val alphaAnimation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 1000
                0.7f at 500
            },
            repeatMode = RepeatMode.Reverse
        )
    )
    val angleAnimation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 180f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 1000
                0.7f at 500
            },
            repeatMode = RepeatMode.Restart
        )
    )
    val alpha =
        if (isActive)
            alphaAnimation
        else
            1F
    val angle =
        if (isActive) {
            angleAnimation
        } else {
            90F
        }
    Icon(
        imageVector = Icons.Filled.HourglassFull,
        contentDescription = "Hourglass",
        modifier = Modifier
            .fillMaxSize(.3f)
            .alpha(alpha = alpha)
            .rotate(degrees = angle)
    )
}

@Composable
fun ClockTimer(
    remainingTime: Long
) {
    val hour = TimeUnit.MILLISECONDS.toHours(remainingTime)
    val min = TimeUnit.MILLISECONDS.toMinutes(remainingTime) % Const.CHRONO_DIVIDER
    val sec = TimeUnit.MILLISECONDS.toSeconds(remainingTime) % Const.CHRONO_DIVIDER
    val milli = remainingTime % Const.MILLISECOND_REMAINDER_DIVIDER
    Text(
        text = "%02d:%02d:%02d.%03d".format(hour, min, sec, milli),
        style = MaterialTheme.typography.h2,
        maxLines = 1

    )
}

@Composable
fun ClockEditor(
    countDownObject: CountDownObject
) {
    val hour = remember { mutableStateOf(0L) }
    val min = remember { mutableStateOf(0L) }
    val sec = remember { mutableStateOf(0L) }
    countDownObject.remainingTime =
        TimeUnit.HOURS.toMillis(hour.value) +
        TimeUnit.MINUTES.toMillis(min.value) +
        TimeUnit.SECONDS.toMillis(sec.value)
    Row(verticalAlignment = Alignment.CenterVertically) {
        TimeField(time = hour, max = 99L)
        Text(
            text = ":",
            style = MaterialTheme.typography.h2
        )
        TimeField(min)
        Text(
            text = ":",
            style = MaterialTheme.typography.h2
        )
        TimeField(sec)
    }
}

@Composable
private fun TimeField(
    time: MutableState<Long>,
    max: Long = 60L
) {
    Column {
        IconButton(onClick = {  if (time.value < max) time.value++ }) {
            Icon(imageVector = Icons.Default.KeyboardArrowUp, contentDescription = "Plus Hour")
        }
        Text(
            text = "%02d".format(time.value),
            style = MaterialTheme.typography.h2
        )
        IconButton(onClick = { if (time.value > 0L) time.value-- }) {
            Icon(imageVector = Icons.Default.KeyboardArrowDown, contentDescription = "Plus Hour")
        }
    }
}

@ExperimentalAnimationApi
@Composable
private fun ClockControls(
    countDownObject: CountDownObject,
    pause: () -> Unit,
    start: (Long) -> Unit,
    stop: () -> Unit,
    editing: MutableState<Boolean>
) {
    Row {
        IconButton(onClick = {
            if (countDownObject.isRunning) {
                pause()
            } else {
                editing.value = false
                start(countDownObject.remainingTime)
            }
        }) {
            val imageVector =
                if (countDownObject.isRunning) {
                    Icons.Default.Pause
                } else {
                    Icons.Default.PlayArrow
                }
            Icon(imageVector = imageVector, contentDescription = "Play Pause")
        }
        val alpha by animateFloatAsState(
            targetValue =
            if (countDownObject.isRunning)
                1f
            else
                .5f
        )
        IconButton(
            onClick = { stop() },
            modifier = Modifier.alpha(alpha = alpha),
            enabled = countDownObject.isRunning
        ) {
            Icon(imageVector = Icons.Default.Stop, contentDescription = "Stop")
        }
        IconButton(
            onClick = { editing.value = !editing.value },
            modifier = Modifier.alpha(alpha = 1 / (2 * alpha)),
            enabled = !countDownObject.isRunning
        ) {
            Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit")
        }
    }
}

@ExperimentalAnimationApi
@Composable
fun ClockScreen(
    countDownObject: CountDownObject,
    start: (Long) -> Unit,
    pause: () -> Unit,
    stop: () -> Unit
) {
    val editing = remember { mutableStateOf(false) }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar (
                title = { Text(text = "Simplest Countdown") }
            )
        }
    )
    {
        Column(
            modifier = Modifier
                .padding(bottom = 15.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Hourglass(isActive = countDownObject.isRunning)
            Row {
                if (editing.value) {
                    ClockEditor(countDownObject = countDownObject)
                }
                if (!editing.value) {
                    ClockTimer(remainingTime = countDownObject.remainingTime)
                }
            }
            ClockControls(
                countDownObject = countDownObject,
                pause = pause,
                start = start,
                stop = stop,
                editing = editing
            )
        }
    }
}

@ExperimentalAnimationApi
@Preview
@Composable
fun Preview() {
    ClockScreen(
        countDownObject = CountDownObject(3600L),
        start = {  },
        pause = {  },
        stop = {  })
}

object Const {
    const val CHRONO_DIVIDER = 60L
    const val MILLISECOND_REMAINDER_DIVIDER = 1000L
}