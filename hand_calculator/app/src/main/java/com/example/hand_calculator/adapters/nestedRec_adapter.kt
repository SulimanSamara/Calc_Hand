package com.example.hand_calculator.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.hand_calculator.R
import kotlinx.android.synthetic.main.activity_history.view.*
import kotlinx.android.synthetic.main.history_item.view.*
import kotlinx.android.synthetic.main.nested_recycle_item.view.*

class nestedRec_adapter(val context: Context, val list:ArrayList<Int>, ):RecyclerView.Adapter<nestedRec_adapter.myviewholder>() {

    inner class myviewholder (view: View):RecyclerView.ViewHolder(view){

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myviewholder {

        return myviewholder(LayoutInflater.from(
                context).inflate(R.layout.nested_recycle_item,
                parent,false))
    }

    override fun onBindViewHolder(holder: myviewholder, position: Int) {

        val model = list[position]

        holder.itemView.number_txt.text = model.toString()

        if (position %2 == 0){
            holder.itemView.nested_layout.setBackgroundColor(ContextCompat.getColor(
                context, R.color.whitgree))
        }else{
            holder.itemView.nested_layout.setBackgroundColor(ContextCompat.getColor(
                    context, R.color.white))
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}