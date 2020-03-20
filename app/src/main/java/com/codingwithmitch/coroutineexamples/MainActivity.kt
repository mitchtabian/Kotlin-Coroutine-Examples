package com.codingwithmitch.coroutineexamples

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private val TAG: String = "AppDebug"

    var count = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button.setOnClickListener {
            text.setText((count++).toString())
        }
    }


    private fun println(message: String){
        Log.d(TAG, message)
    }


}






















