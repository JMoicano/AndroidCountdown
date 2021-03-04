/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge

import android.os.CountDownTimer
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.referentialEqualityPolicy
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.androiddevchallenge.ui.CountDownObject

class CountDownViewModel : ViewModel() {

    private lateinit var timer: CountDownTimer

    var countDownObject by mutableStateOf(CountDownObject(0L), referentialEqualityPolicy())
        private set

    fun start(time: Long = countDownObject.remainingTime) {
        countDownObject = countDownObject.copy(
            remainingTime = time,
            isRunning = true
        )
        timer = object : CountDownTimer(time, 1) {
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
