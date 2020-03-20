package com.codingwithmitch.coroutineexamples

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.Exception


class MainActivity : AppCompatActivity() {

    private val TAG: String = "AppDebug"

    val dao: PeopleDao by lazy {
        PeopleDatabase.buildDatabase(application).peopleDao
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

    fun addPeople(){
        CoroutineScope(IO).launch {

            launch {
                delay(100)
                dao.insert(DataSource.mitch)
            }

            launch {
                try{
                    delay(300)
                    throw Exception("Something went wrong inserting Blake into db.")
                    dao.insert(DataSource.blake)
                }catch (e: Exception){
                    println(e.message)
                }
            }

            launch {
                delay(100)
                dao.insert(DataSource.jess)
            }

            launch {
                delay(500)
                dao.insert(DataSource.vinny)
            }

            launch {
                delay(500)
                dao.insert(DataSource.ayzia)
            }


        }
    }

    private fun println(message: String?){
        Log.d(TAG, message)
    }


}






















