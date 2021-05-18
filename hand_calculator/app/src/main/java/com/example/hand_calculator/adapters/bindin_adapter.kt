package com.example.hand_calculator.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.hand_calculator.ui.HistoryActivity
import com.example.hand_calculator.ui.MainActivity
import com.example.hand_calculator.R
import com.example.hand_calculator.models.Calculator_model
import com.example.hand_calculator.databinding.CalculatorItemBinding
import com.example.hand_calculator.utily.Sqliteopenhelper

class bindin_adapter(val context: Context, val activity: MainActivity, private val list:ArrayList<Calculator_model>,):RecyclerView.Adapter<bindin_adapter.playerviewholder>() {


inner class playerviewholder (private val binding:CalculatorItemBinding):RecyclerView.ViewHolder(binding.root){
    @SuppressLint("ResourceAsColor")
    fun bind(player:Calculator_model, position: Int){
        binding.player = player

        val sql =  Sqliteopenhelper(context)
        binding.btnDelete.setOnClickListener {


                val result = sql.deletplayer(player.id)
                list.removeAt(position)
                activity.deletsumitem(position)
                notifyItemRemoved(position)

                if (result > -1) {
                    Toast.makeText(context, "player deleted", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(context, "fail to delete", Toast.LENGTH_LONG).show()

                }
        }

        val value :ArrayList<Int>
            value = ArrayList()
       for (i in list.indices){

           if (list[i].sum.isNotEmpty())
        value.add(list[i].sum.toInt())
       }

       val max = value.maxByOrNull { it }
       val low = value.minByOrNull { it }


        if (list.size > 1) {
            if (player.sum.isNotEmpty()) {
                if (player.sum.toInt() == max) {

                    binding.lyout.setBackgroundColor(ContextCompat.getColor(
                            context, R.color.whitered))
                } else if (player.sum.toInt() == low) {

                    binding.lyout.setBackgroundColor(ContextCompat.getColor(
                            context, R.color.whitgreen))
                } else {


                    binding.lyout.setBackgroundColor(ContextCompat.getColor(
                            context, R.color.white))
                }
            }
        }
    }

}
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): playerviewholder {
        return playerviewholder(CalculatorItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: playerviewholder, position: Int) {
             holder.bind(list[position],position)


    }

    override fun getItemCount(): Int {
        return list.size
    }


}