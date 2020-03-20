package com.codingwithmitch.coroutineexamples

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "people")
data class Person(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "identity_number")
    var identity_number: Int,

    @ColumnInfo(name = "first_name")
    var first_name: String? = "Anonymous",

    @ColumnInfo(name = "last_name")
    var last_name: String? = "Anonymous"
){


}















