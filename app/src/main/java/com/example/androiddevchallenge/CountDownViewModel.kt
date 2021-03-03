package com.example.androiddevchallenge

import android.os.CountDownTimer
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class CountDownViewModel: ViewModel() {

    var countDownObject by mutableStateOf(0L)
        private set

    init {
        start(7200000L)
    }

    fun start(time: Long) {
        countDownObject = time
        object :CountDownTimer(time, 1){
            override fun onTick(millisUntilFinished: Long) {
                countDownObject = millisUntilFinished
            }

            override fun onFinish() {
                countDownObject = 0L
            }

        }.start()
    }

    fun pause() {
    }
}