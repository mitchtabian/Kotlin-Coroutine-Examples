package com.codingwithmitch.coroutineexamples

object DataSource {

    fun providePeople(): List<Person>{
        val people: ArrayList<Person> = ArrayList()
        people.add(mitch)
        people.add(blake)
        people.add(jess)
        people.add(vinny)
        people.add(ayzia)
        return people
    }

    val mitch = Person(1, "Mitch", "Tabian")
    val blake = Person(2, "Blake", "Tabian")
    val jess = Person(3, "Jess", "Dowhal")
    val vinny = Person(4, "Vinny", "Perry")
    val ayzia = Person(5, "Ayzia", "Carlson")

}