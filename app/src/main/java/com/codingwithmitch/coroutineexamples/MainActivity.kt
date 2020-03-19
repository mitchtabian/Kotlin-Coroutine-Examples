package com.codingwithmitch.coroutineexamples

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main


class MainActivity : AppCompatActivity() {

    private val TAG: String = "AppDebug"

    var count = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        CoroutineScope(IO).launch {
        CoroutineScope(Main).launch {
            doNetworkRequest()
        }

        button.setOnClickListener {
            text.setText((count++).toString())
        }
    }


    suspend fun doNetworkRequest(){
        println("Starting network request...")
        Thread.sleep(5000)
//        delay(5000) // delay will NOT freeze the UI by default
        println("Finished network request!")
    }

    private fun println(message: String){
        Log.d(TAG, message)
    }


}






















