package com.codingwithmitch.coroutineexamples

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.channels.Channel
import kotlin.system.measureTimeMillis


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button.setOnClickListener {
            setNewText("Click!")
            fakeApiRequest()
        }
    }

    private fun setNewText(input: String){
        val newText = text.text.toString() + "\n$input"
        text.text = newText
    }

    private fun fakeApiRequest() = runBlocking{

        val time = measureTimeMillis {

                val channel = Channel<Int>()
                launch(IO) {
                    logThread()
                    // this might be heavy CPU-consuming computation or async logic, we'll just send five squares
                    for (x in 1..5) channel.send(x * x)
                }
                // here we print five received integers:
                repeat(5) { println("debug: ${channel.receive()}") }
                println("debug: Done!")
        }
        println("debug: elapsed time: ${time} ms")
    }

    private fun logThread(){
        println("debug: thread: ${Thread.currentThread().name}")
    }

}



























