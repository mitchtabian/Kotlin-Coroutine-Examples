package com.codingwithmitch.coroutineexamples

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ProgressBar
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main


class MainActivity : AppCompatActivity() {

    private val TAG: String = "From_me:"

    private val PROGRESS_MAX = 250000
    private val PROGRESS_START = 0
    private lateinit var job: CompletableJob
    private var running = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        job_button.setOnClickListener {
            if(!::job.isInitialized){
                initjob()
            }
            job_progress_bar.startJobOrCancel()
        }
    }

    private fun initjob() {
        running = false
        job_button.text = "Start Job #1"
        updateJobCompleteTextView("")
        job = Job()
        job.invokeOnCompletion {
            it?.message.let{
                var msg = it
                if(msg.isNullOrBlank()){
                    msg = "Unknown cancellation error."
                }
                Log.d(TAG, "CompletableJob ${job} was cancelled. Reason: ${msg}")
                showToast(msg)
            }
        }
        job_progress_bar.max = PROGRESS_MAX
        job_progress_bar.progress = PROGRESS_START
    }

    private fun ProgressBar.startJobOrCancel(){
        if(running){
            Log.d(TAG, "${job} is already active. Cancelling...")
            running = false
        } else {
            running = true
            job_button.text = "Cancel Job #1"
            CoroutineScope(IO + job).launch{
                Log.d(TAG, "coroutine ${this} is activated with jobLocal ${job}.")
                for(i in PROGRESS_START..PROGRESS_MAX){
                    if (i % 25000 == 0) Log.d("From_me", "for(i in PROGRESS_START..PROGRESS_MAX: $i")
                    if(!running) haltProgress()
                    this@startJobOrCancel.progress = i
                }
                updateJobCompleteTextView("Job is complete!")
            }.let {coroutineScopeJob->
                coroutineScopeJob.invokeOnCompletion{
                    Log.d("From_me", "CoroutineScope(IO + jobLocal).launch.invokeOnCompletion: ")
                    job.complete()
                }
            }
        }
    }

    private suspend fun haltProgress() = suspendCancellableCoroutine<String> { launch->
        launch.cancel()
        job.complete()
        CoroutineScope(Main).launch {
            initjob()
        }
    }

    private fun updateJobCompleteTextView(text: String){
        GlobalScope.launch (Main){
            job_complete_text.text = text
        }
    }

    private fun showToast(text: String){
        GlobalScope.launch (Main){
            Toast.makeText(this@MainActivity, text, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

}
































