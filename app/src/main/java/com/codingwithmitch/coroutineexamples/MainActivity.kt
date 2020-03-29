package com.codingwithmitch.coroutineexamples

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Main


class MainActivity : AppCompatActivity() {

    private val TAG: String = "AppDebug"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        main()

    }

    val handler = CoroutineExceptionHandler { _, exception ->
        println("Exception thrown in one of the children: $exception")
    }

    fun main(){
        val parentJob = CoroutineScope(Main).launch(handler) {

            // --------- JOB A ---------
            val jobA = launch {
                val resultA = getResult(1)
                println("resultA: ${resultA}")
            }
            jobA.invokeOnCompletion { throwable ->
                if(throwable != null){
                    println("Error getting resultA: ${throwable}")
                }
            }

            // --------- JOB B ---------
            val jobB = launch {
                val resultB = getResult(2)
                println("resultB: ${resultB}")
            }
            jobB.invokeOnCompletion { throwable ->
                if(throwable != null){
                    println("Error getting resultB: ${throwable}")
                }
            }

            // --------- JOB C ---------
            val jobC = launch {
                val resultC = getResult(3)
                println("resultC: ${resultC}")
            }
            jobC.invokeOnCompletion { throwable ->
                if(throwable != null){
                    println("Error getting resultC: ${throwable}")
                }
            }
        }
        parentJob.invokeOnCompletion { throwable ->
            if(throwable != null){
                println("Parent job failed: ${throwable}")
            }
            else{
                println("Parent job SUCCESS")
            }
        }
    }

    suspend fun getResult(number: Int): Int{
        return withContext(Main){
            delay(number*500L)
            if(number == 2){
//                cancel(CancellationException("Error getting result for number: ${number}"))
//                throw CancellationException("Error getting result for number: ${number}") // treated like "cancel()"
                throw Exception("Error getting result for number: ${number}")
            }
            number*2
        }
    }


    private fun println(message: String){
        Log.d(TAG, message)
    }


}






















