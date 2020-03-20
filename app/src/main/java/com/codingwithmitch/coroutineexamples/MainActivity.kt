package com.codingwithmitch.coroutineexamples

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO


class MainActivity : AppCompatActivity() {

    private val TAG: String = "AppDebug"

    var count = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        main()

        button.setOnClickListener {
            text.setText((count++).toString())
        }
    }

    fun main(){
        val startTime = System.currentTimeMillis()
        CoroutineScope(IO).launch {
            val job1 = async(start = CoroutineStart.LAZY) { getResult1() }
            val job2 = async(start = CoroutineStart.LAZY) { getResult2() }
            job1.start()
            job2.start()
            val result1 = job1.await()
            println("Got result1.")
            val result2 = job2.await()
            println("Got result2.")
            println("Done in ${System.currentTimeMillis() - startTime} " +
                    "ms. Result1: ${result1}, Result2: ${result2}")
        }
    }

    suspend fun getResult1(): Int {
        println("Starting Job1!")
        delay(2000)
        println("Result1 is READY...")
        return 1
    }

    suspend fun getResult2(): Int{
        println("Starting Job2!")
        delay(500)
        println("Result2 is READY...")
        return 2
    }


    private fun println(message: String){
        Log.d(TAG, message)
    }


}






















