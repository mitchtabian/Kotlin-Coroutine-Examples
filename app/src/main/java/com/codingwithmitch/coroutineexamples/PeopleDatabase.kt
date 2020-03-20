package com.codingwithmitch.coroutineexamples

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [Person::class], version = 1)
abstract class PeopleDatabase : RoomDatabase() {

    abstract val peopleDao: PeopleDao

    companion object{

        fun buildDatabase(application: Application): PeopleDatabase{
            return Room.inMemoryDatabaseBuilder(
                application,
                PeopleDatabase::class.java
            ).build()
        }
    }
}