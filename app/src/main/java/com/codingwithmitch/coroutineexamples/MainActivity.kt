package com.codingwithmitch.coroutineexamples

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.channels.Channel


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button.setOnClickListener {
            setNewText("Click!")

            CoroutineScope(IO).launch {
                println("debug: CoroutineScope")
                fakeApiRequest()
            }
        }

    }

    private fun setNewText(input: String){
        val newText = text.text.toString() + "\n$input"
        text.text = newText
    }


    private suspend fun fakeApiRequest() {
        coroutineScope {

            val channel = Channel<Int>()

            val job = launch {
                for (x in 1..5){
                    println("debug: send: ${x}")
                    channel.send(x)
                }

                channel.close()
            }

            // receive values from channel with .receive()
//            repeat(5){
//                println("debug: receive: ${channel.receive()}")
//            }

            // receive values from channel using a loop
            for(receivedValue in channel){
                println("debug: receive: ${receivedValue}")
            }

            job.join()
        }
    }
}