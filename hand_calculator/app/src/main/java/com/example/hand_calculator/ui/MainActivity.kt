package com.example.hand_calculator.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hand_calculator.R
import com.example.hand_calculator.adapters.bindin_adapter
import com.example.hand_calculator.databinding.ActivityMainBinding
import com.example.hand_calculator.models.Calculator_model
import com.example.hand_calculator.models.history_model
import com.example.hand_calculator.utily.Sqliteopenhelper
import com.example.hand_calculator.utily.historySql
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(),View.OnClickListener {

    private lateinit var playerAdapter: bindin_adapter
    private lateinit var binding: ActivityMainBinding
    private var calculatorlist: ArrayList<Calculator_model> = ArrayList()
    private lateinit var sum :ArrayList<Int>
    private lateinit var lastlist :ArrayList<history_model>
    private var databaseempty = false
    private lateinit var  sharepreference:SharedPreferences
    private var numberofplay = 1
    private var number_ed = 2
    private var number_deffrent = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)


        sharepreference()

        lastlist = ArrayList()
        sum = ArrayList()
        
        if (databaseempty) {

            getplayers()
        }

        setAdapter()
        setListener()

    }


    fun sharepreference (){

        sharepreference = getSharedPreferences("sharepref", MODE_PRIVATE)
        databaseempty = sharepreference.getBoolean("database", false)

        numberofplay = sharepreference.getInt("numberofplay",1)
        //number_count_ed.setText( numberofplay.toString())
        number_ed = sharepreference.getInt("number_ed",2)

        number_deffrent = number_ed - numberofplay

        if (number_deffrent !=1){

            number_count_ed.isEnabled = false
        }

        Log.i("numb",number_deffrent.toString())

        if (number_deffrent <0){
            Log.i("numbb",number_deffrent.toString())
            btn.isEnabled = false
            btn_sum.isEnabled = false
            Toast.makeText(this, "اللعبه انتهت اضغط حذف!", Toast.LENGTH_SHORT).show()


        }


    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (currentFocus != null) {
            val imm: InputMethodManager =
                    getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
        return super.dispatchTouchEvent(ev)
    }
    fun deletsumitem(position: Int){

        if (sum.size > position) {
            sum.removeAt(position)
        }
    }
    fun getplayers (){

        sum.clear()
      val sqldata = Sqliteopenhelper(this)
        calculatorlist = sqldata.getplayers()


        for (i in calculatorlist.indices){

            sum.add(calculatorlist[i].sum.toInt())
        }

        setAdapter()
    }

    private fun setAdapter() {
        playerAdapter = bindin_adapter(this, this, calculatorlist)
        binding.recycle.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = playerAdapter
        }
    }

    private fun setListener() {
        binding.btn.setOnClickListener(this)
        binding.btnSum.setOnClickListener(this)
        binding.btnReset.setOnClickListener(this)
        binding.btnHistory.setOnClickListener(this)
    }

    override fun onClick(v: View?) {

        when (v?.id) {
            binding.btn.id -> {


                val addplayer = player_name_ed.text.toString()

                if (player_name_ed.text.isNotEmpty()
                       ) {

                            val player = Calculator_model(addplayer)

                    calculatorlist.add(player)

                    playerAdapter.notifyItemInserted(calculatorlist.size - 1)

                    player_name_ed.setText("")
                    number_count_ed.isEnabled = false
                } else {

                    showerrorsnackbar("ادخل اسم اللاعب وعدد مرات اللعب", true)
                }
            }
            binding.btnSum.id -> {

                sharepreference()
                numberofplay ++

                val edit2 = sharepreference.edit()
                edit2.putInt("numberofplay", numberofplay)
                if (number_count_ed.text.isNotEmpty()) {
                    edit2.putInt("number_ed", number_count_ed.text.toString().toInt())
                }
                edit2.apply()


                if (number_deffrent > -1){
                    calculatorlist.forEachIndexed { index, student ->

                        val input = student.number

                        if (sum.size > index) {
                            if (input.isNotEmpty()) {
                                sum[index] += input.toInt()
                            }
                        } else {
                            if (input.isNotEmpty()) {
                                sum.add(input.toInt())
                            }

                            val edit = sharepreference.edit()
                            edit.putBoolean("database", true)
                            edit.apply()

                        }

                        if (input.isNotEmpty()) {
                            student.last_play = input

                            val sqlitdatabase = historySql(this)
                            val result =
                                sqlitdatabase.add(index + 1, student.name, student.last_play)
                         /*   if (result > -1) {

                                Toast.makeText(this, "h_sucsses", Toast.LENGTH_SHORT).show()
                            } else {

                                Toast.makeText(this, "h_faild", Toast.LENGTH_SHORT).show()
                            }*/

                        }
                        playerAdapter.notifyDataSetChanged()

                        if (student.sum.isEmpty()) {

                            val sqlitdatabase = Sqliteopenhelper(this)

                            if (sum.size > 0 && input.isNotEmpty()) {
                                student.sum = sum[index].toString()
                            }

                            if (input.isNotEmpty()) {
                                val mm = sqlitdatabase.add(
                                    numberofplay,
                                    student.name,
                                    student.sum.toInt(),
                                    input
                                )

                                if (mm > -1) {

                                  //  Toast.makeText(this, "sucsses", Toast.LENGTH_SHORT).show()
                                } else {

                                 //   Toast.makeText(this, "faild", Toast.LENGTH_SHORT).show()
                                }
                            }
                        } else {

                            if (sum.size > 0) {
                                student.sum = sum[index].toString()
                            }

                            val sqlitdatabase = Sqliteopenhelper(this)
                            if (input.isNotEmpty())
                                sqlitdatabase.updateemployee(
                                    calculatorlist[index].id,
                                    student.sum.toInt(),
                                    input.toInt()
                                )
                            Toast.makeText(
                                this,
                                calculatorlist[index].id.toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        student.number = ""

                    }
                    getplayers()
                    playerAdapter.notifyDataSetChanged()
                }else{

                 btn_sum.isEnabled = false
                 btn.isEnabled = false
                    Toast.makeText(this, "اللعبه انتهت اضغط حذف!", Toast.LENGTH_SHORT).show()

                }
            }

            binding.btnHistory.id -> {

                val intent = Intent(this, HistoryActivity::class.java)
                intent.putParcelableArrayListExtra("data",calculatorlist)

                startActivity(intent)
            }
            binding.btnReset.id -> {

                val sqllite = Sqliteopenhelper(this)

                sqllite.deleteallplayer()

                val historysql = historySql(this)
                historysql.deleteallRESULT()
                calculatorlist.clear()
                sum.clear()
                setAdapter()

                btn.isEnabled = true
                number_count_ed.isEnabled = true
                btn_sum.isEnabled = true
                numberofplay = 1
                number_deffrent = -1
                number_count_ed.setText("")
                val editor = sharepreference.edit()
                editor.clear()
                editor.apply()

            }
        }
    }
    // function to show Snack bar message..
    fun showerrorsnackbar(message: String, errormesage: Boolean){

        val snackbar = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)

        if (errormesage) {
            val snackbarview = snackbar.view
            snackbarview.setBackgroundColor(ContextCompat.getColor(this,
                R.color.whitered
            )
            )
        }else{

            val snackbarview = snackbar.view
            snackbarview.setBackgroundColor(
                    ContextCompat.getColor(
                            this,
                        R.color.whitgreen
                    )
            )

        }
        snackbar.show()

    }

}