package com.example.hand_calculator.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hand_calculator.R
import com.example.hand_calculator.models.Calculator_model
import com.example.hand_calculator.models.history_model
import com.example.hand_calculator.utily.historySql
import kotlinx.android.synthetic.main.activity_history.view.*
import kotlinx.android.synthetic.main.history_item.view.*

class history_adapter(val context: Context, val playerhistory:ArrayList<history_model>,val calclist:ArrayList<Calculator_model>):RecyclerView.Adapter<history_adapter.viewholder>() {



    inner class viewholder (view:View):RecyclerView.ViewHolder(view){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewholder {

        return viewholder(LayoutInflater.from(context).inflate(R.layout.history_item,parent,false))
    }

    override fun onBindViewHolder(holder: viewholder, position: Int) {

        val model = calclist[position]

        holder.itemView.history_name_txt.text = model.name

        val player: ArrayList<Int>
        player = ArrayList()
        for (i in playerhistory.indices) {
            if (model.name == playerhistory[i].name) {
                player.add(playerhistory[i].last.toInt())
            }

            val adapter = nestedRec_adapter(context, player)
            holder.itemView.nested_rc.layoutManager = LinearLayoutManager(context)
            holder.itemView.nested_rc.adapter = adapter


        }
    }
    override fun getItemCount(): Int {
       return calclist.size

    }
}