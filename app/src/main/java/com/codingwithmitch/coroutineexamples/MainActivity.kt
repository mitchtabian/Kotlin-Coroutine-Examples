package com.codingwithmitch.coroutineexamples

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlin.system.measureTimeMillis


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button.setOnClickListener {
            setNewText("Click!")

            CoroutineScope(IO).launch {
                println("debug: CoroutineScope")
                val result = fakeApiRequest()
                println("debug: result: ${result}") // waits until all jobs in coroutine scope are complete to return result
            }

        }

    }

    private fun setNewText(input: String){
        val newText = text.text.toString() + "\n$input"
        text.text = newText
    }

    private suspend fun fakeApiRequest(): String{


        val time = measureTimeMillis {
            coroutineScope{

                val job1 = launch{
                    println("debug: starting job 1")
                    delay(1000)
                    println("debug: done job 1")
                }

                val job2 = launch{
                    println("debug: staring job 2")
                    delay(1500)
                    println("debug: done job 2")
                }

                val job3 = launch{
                    println("debug: starting job 3")
                    delay(1000)
                    println("debug: done job 3")
                }

            }
        }
        println("debug: elapsed time: ${time}")


        return "All jobs are done."
    }


}



























