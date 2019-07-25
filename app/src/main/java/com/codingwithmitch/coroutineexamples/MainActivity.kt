package com.codingwithmitch.coroutineexamples

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button.setOnClickListener {
            // Ideally you shouldn't use launch here at all
            GlobalScope.launch { setTextOnMainThread("Click!") }
            fakeApiRequest()
        }
    }

    private suspend fun setTextOnMainThread(input: String) {
        withContext(Main) {
            val newText = text.text.toString() + "\n$input"
            text.text = newText
        }
    }

    private fun fakeApiRequest() {
        GlobalScope.launch(IO) {
            if (getResult1FromApi() == "Result #1") {
                setTextOnMainThread("Got Result #1")

                if (getResult2FromApi() == "Result #2") {
                    setTextOnMainThread("Got Result #2")
                } else {
                    setTextOnMainThread("Couldn't get Result #2")
                }
            } else {
                setTextOnMainThread("Couldn't get Result #1")
            }
        }
    }

    private suspend fun getResult1FromApi(): String {
        delay(2000)
        return "Result #1"
    }

    private suspend fun getResult2FromApi(): String {
        delay(1000)
        return "Result #2"
    }
}
