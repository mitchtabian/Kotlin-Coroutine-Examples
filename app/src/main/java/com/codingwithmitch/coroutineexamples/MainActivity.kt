package com.codingwithmitch.coroutineexamples

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.channels.produce
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

            val job = launch(IO){
                logThread()
                val numbers = produceNumbers() // produces integers from 1 and on
                val squares = square(numbers) // squares integers
                for (i in 1..5) println("debug: ${squares.receive()}") // print first five
                println("debug: Done!") // we are done
                coroutineContext.cancelChildren() // cancel children coroutines
            }

            job.join()
        }
        println("debug: elapsed time: ${time} ms")
    }

    fun CoroutineScope.produceNumbers() = produce<Int> {
        var x = 1
        while (true) send(x++) // infinite stream of integers starting from 1
    }

    fun CoroutineScope.square(numbers: ReceiveChannel<Int>): ReceiveChannel<Int> = produce {
        for (x in numbers) send(x * x)
    }

    private fun logThread(){
        println("debug: thread: ${Thread.currentThread().name}")
    }

}



























