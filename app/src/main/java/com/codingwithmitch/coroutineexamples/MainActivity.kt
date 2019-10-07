package com.codingwithmitch.coroutineexamples

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlin.system.measureTimeMillis


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button.setOnClickListener {
            setNewText("Click!")

            CoroutineScope(IO).launch {
                fakeApiRequest()
            }
        }

    }

    private fun setNewText(input: String){
        val newText = text.text.toString() + "\n$input"
        text.text = newText
    }
    private suspend fun setTextOnMainThread(input: String) {
        withContext (Main) {
            setNewText(input)
        }
    }


    /**
     * async() is a blocking call (similar to the job pattern with job.join())
    *  NOTES:
    *  1) IF you don't call await(), it does not wait for the result
    *  2) Calling await() on both these Deffered values will EXECUTE THEM IN PARALLEL. But the RESULTS won't
    *     be published until the last result is available (in this case that's result2)
     *
     */
    private suspend fun fakeApiRequest() {

        withContext(IO) {

            val executionTime = measureTimeMillis {

//                val result1 = async {
//                    println("debug: launching job1: ${Thread.currentThread().name}")
//                    getResult1FromApi()
//                }.await()


                var result1 = ""
                val job1 = launch{
                    println("debug: launching job1: ${Thread.currentThread().name}")
                    result1 = getResult1FromApi()
                }
                job1.join()


                val result2 = async {
                    println("debug: launching job2: ${Thread.currentThread().name}")
                    getResult2FromApi(result1)
                }.await()
                println("Got result2: $result2")

            }
            println("debug: job1 and job2 are complete. It took ${executionTime} ms")
        }
    }

    private suspend fun getResult1FromApi(): String {
        delay(1000)
        return "Result #1"
    }

    private suspend fun getResult2FromApi(result1: String): String {
        delay(1700)
        return "Result #2"
    }

}