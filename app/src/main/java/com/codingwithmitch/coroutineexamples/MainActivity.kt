package com.codingwithmitch.coroutineexamples

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO


class MainActivity : AppCompatActivity() {

    private val TAG: String = "AppDebug"

    val db: PeopleDatabase by lazy {
        PeopleDatabase.buildDatabase(application)
    }

    val dao: PeopleDao by lazy {
        db.peopleDao
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button.setOnClickListener {
            getAllPeople()
        }

        addPeople()
    }

    fun getAllPeople(){
        CoroutineScope(IO).launch {

            val people = async {
                dao.getAllPeople()
            }
            for(person in people.await()){
                println("${person}")
            }
        }
    }

    fun addPeople() {

        CoroutineScope(IO).launch {
            dao.insertPeople(DataSource.providePeople())
        }
    }

    private fun println(message: String?){
        Log.d(TAG, message)
    }


}






















