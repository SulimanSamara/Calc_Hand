package com.example.hand_calculator.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hand_calculator.R
import com.example.hand_calculator.adapters.history_adapter
import com.example.hand_calculator.adapters.nestedRec_adapter
import com.example.hand_calculator.models.Calculator_model
import com.example.hand_calculator.models.history_model
import com.example.hand_calculator.utily.Sqliteopenhelper
import com.example.hand_calculator.utily.historySql
import kotlinx.android.synthetic.main.activity_history.*

class HistoryActivity : AppCompatActivity() {

    private lateinit var  lastlist :ArrayList<history_model>
    private lateinit var playerhistory :ArrayList<Int>
    private lateinit var playerlist:ArrayList<Calculator_model>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)


     /*   if (intent.hasExtra("data")){

            playerlist = intent.getParcelableArrayListExtra("data")!!
        }
*/
        playerhistory = ArrayList()
        playerlist = ArrayList()
        lastlist = ArrayList()
        retrievedata()
    }

    private fun retrievedata (){


        val playersql = Sqliteopenhelper(this)

          playerlist = playersql.getplayers()


        Log.i("list",playerlist.size.toString())
        val sql = historySql(this)
        lastlist = sql.getplayershistory()
      /*  for (i in lastlist.indices){
            if (lastlist.size < playerlist.size)
            if ( lastlist[i].name == playerlist[i].name){

                    playerhistory.add(lastlist[i].last.toInt())

            }
        }
*/
        setadapter()
    }

   private fun setadapter (){

        val adapter = history_adapter(this,lastlist,playerlist)
            history_recycle.layoutManager = LinearLayoutManager(this)
            history_recycle.adapter = adapter
    }
}