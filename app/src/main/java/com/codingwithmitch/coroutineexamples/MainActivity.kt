package com.codingwithmitch.coroutineexamples

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main


class MainActivity : AppCompatActivity() {

    private val JOB_TIMEOUT = 1900L

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

    private suspend fun fakeApiRequest() {
        coroutineScope {

            val job = launch {
                try{
                    val result1 = getResult1FromApi() // wait until job is done

                    if ( result1.equals("Result #1")) {

                        setTextOnMainThread("Got $result1")

                        val result2 = getResult2FromApi() // wait until job is done

                        if (result2.equals("Result #2")) {
                            setTextOnMainThread("Got $result2")
                        } else {
                            setTextOnMainThread("Couldn't get Result #2")
                        }
                    } else {
                        setTextOnMainThread("Couldn't get Result #1")
                    }
                }catch (e: CancellationException){
                    println("debug: CancellationException: ${e.message}")
                }
                finally {
                    println("debug: Finishing job. Cleaning up resources...")
                }

            }

            isJobRunning(job.isActive)

            delay(JOB_TIMEOUT) // wait to see if job completes in this time

            // Cancel Option 1
            job.cancel(CancellationException("Job took longer than $JOB_TIMEOUT")) // cancel if delay time elapses and job has not completed
            job.join() // wait for the cancellation to happen

//             Cancel Option 2
//            job.cancelAndJoin()

            delay(1000)

            isJobRunning(job.isActive)


        }
    }

    private fun isJobRunning(isActive: Boolean){
        if(isActive){
            println("debug: Job is active")
        }
        else{
            println("debug: Job is not active")
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