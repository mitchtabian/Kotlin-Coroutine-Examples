package com.codingwithmitch.coroutineexamples

import androidx.room.*

@Dao
interface PeopleDao{


    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(person: Person): Long

    @Delete
    suspend fun delete(person: Person)

    @Query("""
        UPDATE people SET first_name = :first_name, last_name = :last_name 
        WHERE identity_number = :identity_number
        """)
    fun updatePerson(identity_number: Int, first_name: String, last_name: String)

    @Query("SELECT * FROM people")
    fun getAllPeople()

    @Query("""
        SELECT * FROM people WHERE identity_number = :identity_number
        OR first_name LIKE :first_name OR last_name LIKE :last_name
    """)
    fun findPerson(identity_number: Int?, first_name: String?, last_name: String?)
}

















