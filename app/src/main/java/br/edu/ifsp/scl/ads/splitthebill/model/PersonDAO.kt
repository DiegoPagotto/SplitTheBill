package br.edu.ifsp.scl.ads.splitthebill.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface PersonDAO {
    @Insert fun createPerson(person: Person)
    @Query("SELECT * FROM Person WHERE id = :id") fun retrievePerson(id: Int): Person?
    @Query("SELECT * FROM Person") fun retrieveAllPersons(): MutableList<Person>
    @Update fun updatePerson(person: Person): Int
    @Delete fun deletePerson(person: Person): Int
}