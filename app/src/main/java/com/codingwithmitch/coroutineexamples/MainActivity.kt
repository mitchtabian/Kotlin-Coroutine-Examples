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
    *     be published until the last result is available (in this case that's result2
     */
    private suspend fun fakeApiRequest() {
        coroutineScope {
            
            val parentJob = launch {

                val executionTime = measureTimeMillis {

                    val result1: Deferred<String> = async {
                        println("debug: launching job1: ${Thread.currentThread().name}")
                        getResult1FromApi()
                    }


                    val result2: Deferred<String> = async {
                        println("debug: launching job2: ${Thread.currentThread().name}")
                        getResult2FromApi()
                    }

                    setTextOnMainThread("Got ${result1.await()}")
                    setTextOnMainThread("Got ${result2.await()}")
                }
                println("debug: job1 and job2 are complete. It took ${executionTime} ms")

            }

            // Separate job within the same coroutine context that runs independently of parentJob, Job1 and Job2
            launch {
                for(delay in arrayOf(1, 2, 3, 4, 5, 6)){
                    delay(500)
                    println("debug: is parent job active?: ${parentJob.isActive}")
                }
            }


        }
    }

    private suspend fun getResult1FromApi(): String {
        delay(1000) // Does not block thread. Just suspends the coroutine inside the thread
        return "Result #1"
    }

    private suspend fun getResult2FromApi(): String {
        delay(1000)
        return "Result #2"
    }

}