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

    private val PROGRESS_MAX = 2500000
    private val PROGRESS_START = 0
    private val PROGRESS_STEP = PROGRESS_MAX / 100
    private val JOB_TIME = 4000 // ms
    private lateinit var job: CompletableJob


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        job_button.setOnClickListener {
            if(!::job.isInitialized){
                initjob()
            }
            job_progress_bar.startJobOrCancel(job)
        }
    }

    fun resetjob(){
        if(job.isActive || job.isCompleted){
            job.cancel(CancellationException("Resetting job"))
        }
        initjob()
    }

    fun initjob(){
        job_button.text = "Start Job #1"
        updateJobCompleteTextView("")
        job = Job()
        job.invokeOnCompletion {
            Log.d("From_me", "job = Job() invokeOnCompletion")
            it?.message.let{
                var msg = it
                if(msg.isNullOrBlank()){
                    msg = "Unknown cancellation error."
                }
                Log.e(TAG, "${job} was cancelled. Reason: ${msg}")
                showToast(msg)
            }
        }
        job_progress_bar.max = PROGRESS_MAX
        job_progress_bar.progress = PROGRESS_START
    }
    

    fun ProgressBar.startJobOrCancel(job: Job){
        if(this.progress > 0){
            Log.d(TAG, "${job} is already active. Cancelling...")
            resetjob()
        }
        else{
            job_button.text = "Cancel Job #1"
            CoroutineScope(IO + job).launch{
                Log.d(TAG, "coroutine ${this} is activated with job ${job}.")
                for(i in PROGRESS_START..PROGRESS_MAX){
                    if (i % PROGRESS_STEP == 0) {
                        Log.d("From_me", "for(i in PROGRESS_START..PROGRESS_MAX): $i")
                    }
                    this@startJobOrCancel.progress = i / PROGRESS_STEP
                }
                updateJobCompleteTextView("Job is complete!")
                job.cancel(CancellationException("Ending !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!4"))
            }.invokeOnCompletion {
                Log.d("From_me", "CoroutineScope(IO + job) invokeOnCompletion(): Ending !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!4")
            }
        }
    }

    private fun updateJobCompleteTextView(text: String){
        GlobalScope.launch (Main){
            job_complete_text.setText(text)
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
































