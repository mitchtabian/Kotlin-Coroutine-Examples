package com.codingwithmitch.coroutineexamples

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main


class MainActivity : AppCompatActivity() {

    private val JOB_TIMEOUT = 1900L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button.setOnClickListener {
            setNewText("Click!")

            CoroutineScope(IO).launch {
                fakeApiRequest()
            }
        }

    }

    private fun setNewText(input: String){
        val newText = text.text.toString() + "\n$input"
        text.text = newText
    }
    private suspend fun setTextOnMainThread(input: String) {
        withContext (Main) {
            setNewText(input)
        }
    }

    private suspend fun fakeApiRequest() {
        coroutineScope {

            val job = withTimeoutOrNull(JOB_TIMEOUT) {

                    val result1 = getResult1FromApi() // wait until job is done

                    if ( result1.equals("Result #1")) {

                        setTextOnMainThread("Got $result1")

                        val result2 = getResult2FromApi() // wait until job is done

                        if (result2.equals("Result #2")) {
                            setTextOnMainThread("Got $result2")
                        } else {
                            setTextOnMainThread("Couldn't get Result #2")
                        }
                    } else {
                        setTextOnMainThread("Couldn't get Result #1")
                    }
            }

            if(job == null){
                println("debug: Cancelling job...Job took longer than $JOB_TIMEOUT")
            }

        }
    }
    
    private suspend fun getResult1FromApi(): String {
        delay(1000) // Does not block thread. Just suspends the coroutine inside the thread
        return "Result #1"
    }

    private suspend fun getResult2FromApi(): String {
        delay(1000)
        return "Result #2"
    }

}