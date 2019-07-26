package com.codingwithmitch.coroutineexamples

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button.setOnClickListener {
            setTextOnMainThread("Click!")
            fakeApiRequest()
        }
    }

    fun setTextOnMainThread(input: String){
        CoroutineScope(Dispatchers.Main)
            .launch { withContext(Dispatchers.Main){
                val newText = text.text.toString() + "\n${input}"
                text.setText(newText)
            } }
    }

    fun fakeApiRequest(){

        CoroutineScope(Dispatchers.IO)
            .launch{ withContext(Dispatchers.IO){

                val result1 = getResult1FromApi()

                if(result1.equals("Result #1")) {

                    setTextOnMainThread("Got Result #1")

                    val result2 = getResult2FromApi()

                    if(result2.equals("Result #2")){
                        setTextOnMainThread("Got Result #2")
                    }
                    else{
                        setTextOnMainThread("Couldn't get Result #2")
                    }
                }
                else{
                    setTextOnMainThread("Couldn't get Result #1")
                }
            } }

    }

    suspend fun getResult1FromApi(): String{
        Thread.sleep(2000)
        return "Result #1"
    }

    suspend fun getResult2FromApi(): String{
        Thread.sleep(1000)
        return "Result #2"
    }
}















