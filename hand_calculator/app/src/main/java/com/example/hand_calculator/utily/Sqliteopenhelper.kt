package com.example.hand_calculator.utily

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.hand_calculator.models.Calculator_model
import com.example.hand_calculator.models.history_model

class Sqliteopenhelper (context:Context):
    SQLiteOpenHelper (context,DATABASE_NAME,null, DATABASE_VERSION){


    companion object{

        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "HANDCALCULATOR"
        private const val TABLE_PLAYERS = "player"
        private const val COLUMN_ID = "_id"
        private const val COLUMN_IDD = "idd"
        private const val COLUMN_NAME = "name"
        private const val COLUMAN_SUM = "sum"
        private const val COLUMAN_LASTINPUT = "last"


    }

    override fun onCreate(db: SQLiteDatabase?) {

            Log.i("dataa","ssssssssss")

            val CREATE_DATE_TABLE = ("CREATE TABLE " + TABLE_PLAYERS + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY," +
                    COLUMN_NAME + " TEXT," +
                    COLUMN_IDD + " TEXT," +
                    COLUMAN_SUM + " TEXT," +
                    COLUMAN_LASTINPUT + " TEXT );")

            db?.execSQL(CREATE_DATE_TABLE)

        }

    override fun onUpgrade(dp: SQLiteDatabase?, p1: Int, p2: Int){

         dp!!.execSQL("DROP TABLE IF EXISTS " + TABLE_PLAYERS)

        onCreate(dp)
    }

    fun add (idd:Int ,name:String ,sum:Int ,lastinput:String):Long {

        val dp = this.writableDatabase

        val valuse = ContentValues()

            valuse.put(COLUMN_IDD, idd)
            valuse.put(COLUMN_NAME, name)
            valuse.put(COLUMAN_SUM, sum)
            valuse.put(COLUMAN_LASTINPUT, lastinput)

            val sucsses = dp.insert(TABLE_PLAYERS, null, valuse)

            dp.close()

            return sucsses


    }


    fun getplayers ():ArrayList<Calculator_model>{

        val playerslist :ArrayList<Calculator_model> = ArrayList<Calculator_model>()

        val selectQuery = "SELECT * FROM $TABLE_PLAYERS"

        val db = readableDatabase

        var cursor : Cursor? = null

        try {

            cursor =db.rawQuery(selectQuery,null)

        }catch (e: SQLiteException){

            db.execSQL(selectQuery)
            return ArrayList()
        }

        var id :Int
        var idd:Int
        var name :String
        var sum :Int
        var last:String

        if (cursor!!.moveToFirst()){

            do {

                id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID))
                idd =cursor.getInt(cursor.getColumnIndex(COLUMN_IDD))
                name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME))
                sum = cursor.getInt(cursor.getColumnIndex(COLUMAN_SUM))
                last = cursor.getString(cursor.getColumnIndex(COLUMAN_LASTINPUT))

                val player = Calculator_model(name,sum.toString(),"",last,id,idd)
                playerslist.add(player)

            }while (cursor.moveToNext())

        }

        return playerslist
    }

    fun  updateemployee (idd: Int,sum: Int,last:Int):Int{

        val db = this.writableDatabase

        val contentvalue = ContentValues()

        contentvalue.put(COLUMAN_SUM, sum)
        contentvalue.put(COLUMAN_LASTINPUT,last)

        val sucsses = db.update(
                TABLE_PLAYERS,contentvalue,
                COLUMN_ID + "=" + idd,null)

        db.close()
        return sucsses

    }

    fun deletplayer (id:Int):Int {

        val db = writableDatabase

        val sucsses = db.delete(TABLE_PLAYERS, COLUMN_ID + "=" + id,null)

        db.close()

        return sucsses
    }

    fun deleteallplayer() {

        val db = this.writableDatabase
        //db.execSQL("DROP TABLE $TABLE_PLAYERS")
        db.execSQL("delete  from $TABLE_PLAYERS")
        db.close()

    }
    /* fun getallcompletdatelist():ArrayList<String>{
         val list = ArrayList<String>()
         val dp = readableDatabase
         val cursor = dp.rawQuery("SELECT * FROM  $TABLE_HISTORY",null )

         while (cursor.moveToNext()){

             val datevalue = cursor.getString(cursor.getColumnIndex(COLUMN_COMPLET_DATE))
             list.add(datevalue)

             }
           cursor.close()

         return list

         }*/

}