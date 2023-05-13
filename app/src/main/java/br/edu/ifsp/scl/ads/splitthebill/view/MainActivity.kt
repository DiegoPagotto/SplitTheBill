package br.edu.ifsp.scl.ads.splitthebill.view

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView.AdapterContextMenuInfo
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import br.edu.ifsp.scl.ads.splitthebill.R
import br.edu.ifsp.scl.ads.splitthebill.adapter.PersonAdapter
import br.edu.ifsp.scl.ads.splitthebill.controller.PersonController
import br.edu.ifsp.scl.ads.splitthebill.databinding.ActivityMainBinding
import br.edu.ifsp.scl.ads.splitthebill.model.Person


class MainActivity : BaseActivity(){
    private val amb: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val personList: MutableList<Person> = mutableListOf()
    private val personAdapter: PersonAdapter by lazy{
        PersonAdapter(this, personList)
    }

    private lateinit var carl: ActivityResultLauncher<Intent>

    private val personController : PersonController by lazy {
        PersonController(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(amb.root)
        supportActionBar?.subtitle = getString(R.string.person_list)
        personController.getAllPersons()
        amb.personLv.adapter = personAdapter

        carl = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
            if (result.resultCode == RESULT_OK){
                val person = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                    result.data?.getParcelableExtra<Person>(EXTRA_PERSON, Person::class.java)
                else
                    result.data?.getParcelableExtra<Person>(EXTRA_PERSON)

                person?.let { _person ->
                    val position = personList.indexOfFirst { it.id == _person.id }

                    if (position != -1){
                        personList[position] = _person
                        personController.editPerson(_person)
                        Toast.makeText(this, R.string.person_edited, Toast.LENGTH_SHORT).show()
                    }
                    else{
                        personController.insertPerson(_person)
                        personController.getAllPersons()
                    }
                    personAdapter.notifyDataSetChanged()
                }
            }
        }

        registerForContextMenu(amb.personLv)
        amb.personLv.setOnItemClickListener { parent, view, position, id ->
            val person = personList[position]
            val editPersonIntent = Intent(this, PersonActivity::class.java)
            editPersonIntent.putExtra(EXTRA_PERSON, person)
            editPersonIntent.putExtra(EXTRA_VIEW_PERSON, true)
            carl.launch(editPersonIntent)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.addPersonMi-> {
                carl.launch(Intent(this, PersonActivity::class.java))
                true
            }
            R.id.splitTheBillMi-> {
                if(personList.isEmpty()){
                    Toast.makeText(this, R.string.no_persons, Toast.LENGTH_SHORT).show()
                    return true
                }
                showAlertDialog(splitTheBill())
                true
            }
            else -> false
        }
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        menuInflater.inflate(R.menu.context_menu_main, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val position = (item.menuInfo as AdapterContextMenuInfo).position
        val person = personList[position]

        return when(item.itemId){
            R.id.editPersonMi -> {
                val editPersonIntent = Intent(this, PersonActivity::class.java)
                editPersonIntent.putExtra(EXTRA_PERSON, person)
                carl.launch(editPersonIntent)
                true
            }
            R.id.removePersonMi -> {
                person.let { personController.removePerson(it) }
                personList.removeAt(position)
                personAdapter.notifyDataSetChanged()
                Toast.makeText(this, R.string.person_removed, Toast.LENGTH_SHORT).show()
                true
            }
            else -> false
        }
    }

    fun updatePersonList(_personList : MutableList<Person>){
        personList.clear()
        personList.addAll(_personList)
        personAdapter.notifyDataSetChanged()
    }

    private fun splitTheBill() : String {
        var billTotal = 0.0
        personList.forEach { person ->
            billTotal += person.totalPaid
        }

        val billPerPerson = billTotal.div(personList.size)
        val output = StringBuilder()
        personList.forEach { person ->
            if(person.totalPaid > billPerPerson)
                output.append("${person.name} should receive $${person.totalPaid - billPerPerson}\n")
            else if(person.totalPaid < billPerPerson)
                output.append("${person.name} should pay $${billPerPerson - person.totalPaid}\n")
            else
                output.append("${person.name} is ok\n")
        }
        return output.toString()
    }

    private fun showAlertDialog(messsage: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Split the Bill")
        builder.setMessage(messsage)
        builder.setPositiveButton("OK") { dialog, which -> dialog.dismiss() }
        val alertDialog = builder.create()
        alertDialog.show()
    }
}