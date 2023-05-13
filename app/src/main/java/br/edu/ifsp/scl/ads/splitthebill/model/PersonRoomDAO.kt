package br.edu.ifsp.scl.ads.splitthebill.model

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Person::class], version = 1)
abstract class PersonRoomDAO : RoomDatabase(){
    companion object Constants {
        const val PERSON_DATABASE_FILE = "split_the_bill_room"
    }

    abstract fun getPersonDao() : PersonDAO
}