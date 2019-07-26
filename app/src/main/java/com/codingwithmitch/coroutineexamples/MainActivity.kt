package com.codingwithmitch.coroutineexamples

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

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

            launch {
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
    }


    private suspend fun getResult1FromApi(): String {
        delay(1000) // Does not block thread. Just suspends the coroutine inside the thread
        return "Result #1"
    }

    private suspend fun getResult2FromApi(): String {
        delay(1000)
        return "Result #2"
    }

    fun CoroutineScope.downloader(
        references: ReceiveChannel<Int>,
        results: SendChannel<String>
    ) = launch{
        val requested = mutableSetOf<String>()
        for(ref in references){
            val aString: String = ref.toString()
            if(requested.add(aString)){
                results.send(aString)
            }

        }
    }

    fun CoroutineScope.worker(
        results: ReceiveChannel<String>
    ) {
        launch {
            for(result in results){

            }
        }
    }
}