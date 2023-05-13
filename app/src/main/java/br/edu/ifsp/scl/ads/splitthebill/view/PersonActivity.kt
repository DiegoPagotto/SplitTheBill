package br.edu.ifsp.scl.ads.splitthebill.view

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import br.edu.ifsp.scl.ads.splitthebill.R
import br.edu.ifsp.scl.ads.splitthebill.databinding.ActivityPersonBinding
import br.edu.ifsp.scl.ads.splitthebill.model.Person

class PersonActivity : BaseActivity() {
    private val apb : ActivityPersonBinding by lazy {
        ActivityPersonBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(apb.root)
        supportActionBar?.subtitle = getString(R.string.person_info)

        val receivedPerson = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            intent.getParcelableExtra<Person>(EXTRA_PERSON, Person::class.java)
        else
            intent.getParcelableExtra<Person>(EXTRA_PERSON)

        receivedPerson?.let { _receivedPerson ->
            with(apb){
                with(_receivedPerson){
                    nameEt.setText(name)
                    totalPaidEt.setText(totalPaid.toString())
                    itemsBoughtEt.setText(itemBought)
                }
            }
        }

        val viewPerson = intent.getBooleanExtra(EXTRA_VIEW_PERSON, false)
        with(apb){
            nameEt.isEnabled = !viewPerson
            totalPaidEt.isEnabled = !viewPerson
            itemsBoughtEt.isEnabled = !viewPerson
            saveBt.visibility = if(viewPerson) View.GONE else View.VISIBLE
        }

        apb.saveBt.setOnClickListener{
            try {
                val person = Person(
                    receivedPerson?.id,
                    apb.nameEt.text.toString(),
                    apb.totalPaidEt.text.toString().toDouble(),
                    apb.itemsBoughtEt.text.toString()
                )
                val resultIntent = Intent()
                resultIntent.putExtra(EXTRA_PERSON, person)
                setResult(RESULT_OK, resultIntent)
                finish()
            } catch (exception: NumberFormatException) {
                apb.totalPaidEt.error = getString(R.string.invalid_total_paid)
                apb.totalPaidEt.requestFocus()
            }

        }
    }
}