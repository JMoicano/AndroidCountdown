package com.example.androiddevchallenge.ui

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import java.math.BigInteger
import java.util.concurrent.TimeUnit

@Composable
fun ClockTimer(timestamp: Long) {
    val hour = TimeUnit.MILLISECONDS.toHours(timestamp)
    val min = TimeUnit.MILLISECONDS.toMinutes(timestamp)%Const.DIVIDER
    val sec = TimeUnit.MILLISECONDS.toSeconds(timestamp)%Const.DIVIDER
    val milli = timestamp%Const.MILLISECOND_REMAINDER_DIVIDER
    Text(text = "%02d:%02d:%02d.%03d".format(hour, min, sec, milli))
}

object Const{
    const val DIVIDER = 60L
    const val MILLISECOND_REMAINDER_DIVIDER = 1000L
}