package br.edu.ifsp.scl.ads.splitthebill.controller

import androidx.room.Room
import br.edu.ifsp.scl.ads.splitthebill.model.Person
import br.edu.ifsp.scl.ads.splitthebill.model.PersonDAO
import br.edu.ifsp.scl.ads.splitthebill.model.PersonRoomDAO
import br.edu.ifsp.scl.ads.splitthebill.view.MainActivity

class PersonController(private val mainActivity: MainActivity) {
    private val personDAO : PersonDAO = Room.databaseBuilder(
        mainActivity, PersonRoomDAO::class.java, PersonRoomDAO.PERSON_DATABASE_FILE
    ).build().getPersonDao()

    fun insertPerson(person : Person) = Thread{ personDAO.createPerson(person) }.start()
    fun getPersonById(id : Int) = Thread{ personDAO.retrievePerson(id) }.start()
    fun getAllPersons() = Thread{
        val persons = personDAO.retrieveAllPersons()
        mainActivity.runOnUiThread { mainActivity.updatePersonList(persons) }
    }.start()
    fun editPerson(person : Person) = Thread{ personDAO.updatePerson(person) }.start()
    fun removePerson(person: Person) = Thread{ personDAO.deletePerson(person) }.start()
}
