package com.example.androiddevchallenge

import android.os.CountDownTimer
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.example.androiddevchallenge.ui.CountDownObject

class CountDownViewModel: ViewModel() {

    private lateinit var timer: CountDownTimer

    var countDownObject by mutableStateOf(CountDownObject(0L), referentialEqualityPolicy())
        private set

    fun start(time: Long = countDownObject.remainingTime) {
        countDownObject = countDownObject.copy(
            remainingTime = time,
            isRunning = true
        )
        timer = object : CountDownTimer(time, 1){
            override fun onTick(millisUntilFinished: Long) {
                countDownObject = countDownObject.copy(
                    remainingTime = millisUntilFinished
                )
            }

            override fun onFinish() {
                countDownObject = countDownObject.copy(
                    remainingTime = 0L,
                    isRunning = false
                )
            }

        }
        timer.start()
    }

    fun pause() {
        timer.cancel()
        countDownObject = countDownObject.copy(
            isRunning = false
        )
    }

    fun stop() {
        timer.cancel()
        countDownObject = countDownObject.copy(
            remainingTime = 0L,
            isRunning = false
        )
    }
}