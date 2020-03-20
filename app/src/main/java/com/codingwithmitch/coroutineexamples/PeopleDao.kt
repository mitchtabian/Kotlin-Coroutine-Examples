package com.codingwithmitch.coroutineexamples

import androidx.room.*
import kotlinx.coroutines.cancel
import kotlinx.coroutines.coroutineScope
import java.lang.Exception

@Dao
interface PeopleDao{

    @Transaction
    suspend fun insertPeople(people: List<Person>) = coroutineScope {
        for((index, person) in people.withIndex()){
            try{
                if(index == 3){
                    throw Exception()
                }
                insert(person)
            }catch (e: Exception){
                cancel("Something went wrong")
            }
        }
    }

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(person: Person): Long

    @Delete
    suspend fun delete(person: Person): Int

    @Query("""
        UPDATE people SET first_name = :first_name, last_name = :last_name 
        WHERE identity_number = :identity_number
        """)
    suspend fun updatePerson(identity_number: Int, first_name: String, last_name: String): Int

    @Query("SELECT * FROM people")
    suspend fun getAllPeople(): List<Person>

    @Query("""
        SELECT * FROM people WHERE identity_number = :identity_number
        OR first_name LIKE :first_name OR last_name LIKE :last_name
    """)
    suspend fun findPeople(identity_number: Int?, first_name: String?, last_name: String?): List<Person>
}

















