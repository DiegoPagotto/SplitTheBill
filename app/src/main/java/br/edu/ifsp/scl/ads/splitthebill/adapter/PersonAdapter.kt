package br.edu.ifsp.scl.ads.splitthebill.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import br.edu.ifsp.scl.ads.splitthebill.R
import br.edu.ifsp.scl.ads.splitthebill.databinding.TilePersonBinding
import br.edu.ifsp.scl.ads.splitthebill.model.Person

class PersonAdapter (
    context: Context,
    private val personList: MutableList<Person>
) : ArrayAdapter<Person>(context, R.layout.tile_person, personList){
    private lateinit var tpb: TilePersonBinding

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val person = personList[position]
        var tilePersonView = convertView
        if(tilePersonView == null){
            tpb = TilePersonBinding.inflate(context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater, parent, false)
            tilePersonView = tpb.root
            val tpvh = TilePersonViewHolder(tpb.nameTv, tpb.totalPaidTv, tpb.itemsBoughtTv)
            tilePersonView.tag = tpvh
        }

        with(tilePersonView.tag as TilePersonViewHolder){
            nameTv.text = person.name
            totalPaidTv.text = "$" + person.totalPaid.toString()
            itemsBoughtTv.text = person.itemBought
        }

        return tilePersonView
    }

    private data class TilePersonViewHolder(
        val nameTv: TextView,
        val totalPaidTv: TextView,
        val itemsBoughtTv: TextView
    )
}
