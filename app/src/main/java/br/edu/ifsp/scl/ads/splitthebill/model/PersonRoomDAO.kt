package br.edu.ifsp.scl.ads.splitthebill.model

abstract class PersonRoomDAO {
    companion object Constants {
        const val PERSON_DATABASE_FILE = "persons_room"
    }

    abstract fun getPersonDao() : PersonDAO
}