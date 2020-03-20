package com.codingwithmitch.coroutineexamples

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import java.lang.Exception
import kotlin.coroutines.CoroutineContext
import kotlin.random.Random


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

    fun main()  {

        for(index in 1..10){
            CoroutineScope(IO).launch {
                try{
                    println("RESULT: ${attemptConcurrentSum()}")
                }catch (e: Exception){
                    println("${e.message}")
                }

            }
        }

    }

    suspend fun attemptConcurrentSum(): String = coroutineScope {
        val randomNum1 = async {
            delay(500)
            getRandomNumber(false)
        }

        val randomNum2 = async {
            delay(2000)
            getRandomNumber(false)
        }
        val num1 = randomNum1.await()
        val num2 = randomNum2.await()
        println("NumOne=${num1}, NumTwo=${num2}")
        "${(num1+ num2)}"
    }

    fun getRandomNumber(shouldFail: Boolean): Int{
        return if(shouldFail){
            throw ArithmeticException("Something went wrong! ")
        }
        else{
            Random.nextInt(1,11)
        }
    }


    private fun println(message: String){
        Log.d(TAG, message)
    }


}






















