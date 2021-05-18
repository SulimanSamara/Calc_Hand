package com.example.hand_calculator.utily

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.hand_calculator.models.history_model

class historySql(context: Context):
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION){


    companion object{

        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "HISTORY"

        private const val TABLE_HISTORY = "history"
        private const val COLUMN_H_IDD = "idd"
        private const val COLUMN_HISTORY_ID = "_id"
        private const val COLUMN_HISTORY_NAME = "name"
        private const val COLUMN_LASTPLAY = "lastplay"
    }

    override fun onCreate(db: SQLiteDatabase?) {


                  Log.i("dataa", "ssssssssss")

            val CREATE_DATE_TABLE = ("CREATE TABLE "
                    + TABLE_HISTORY + "("
                    + COLUMN_HISTORY_ID + " INTEGER PRIMARY KEY," +
                    COLUMN_HISTORY_NAME + " TEXT," +
                    COLUMN_H_IDD + " TEXT," +
                    COLUMN_LASTPLAY + " TEXT"+ ");")

            db?.execSQL(CREATE_DATE_TABLE)


        }

    override fun onUpgrade(dp: SQLiteDatabase?, p1: Int, p2: Int){


            dp!!.execSQL("DROP TABLE IF EXISTS " + TABLE_HISTORY)

        onCreate(dp)
    }

    fun add(h_id: Int, h_name: String, h_lastplay: String):Long {

        val dp = this.writableDatabase

        val valuse = ContentValues()


            valuse.put(COLUMN_H_IDD, h_id)
            valuse.put(COLUMN_HISTORY_NAME, h_name)
            valuse.put(COLUMN_LASTPLAY, h_lastplay)

            val sucsses = dp.insert(TABLE_HISTORY, null, valuse)

            dp.close()

            return sucsses

    }


    fun getplayershistory ():ArrayList<history_model>{

        val playerslist :ArrayList<history_model> = ArrayList()

        val selectQuery = "SELECT * FROM $TABLE_HISTORY"

        val db = readableDatabase

        var cursor : Cursor? = null

        try {

            cursor =db.rawQuery(selectQuery, null)

        }catch (e: SQLiteException){

            db.execSQL(selectQuery)
            return ArrayList()
        }

        var id :Int
        var idd:Int
        var name :String
        var last:String

        if (cursor!!.moveToFirst()){

            do {

                id = cursor.getInt(cursor.getColumnIndex(COLUMN_HISTORY_ID))
                idd =cursor.getInt(cursor.getColumnIndex(COLUMN_H_IDD))
                name = cursor.getString(cursor.getColumnIndex(COLUMN_HISTORY_NAME))
                last = cursor.getString(cursor.getColumnIndex(COLUMN_LASTPLAY))

                val player = history_model(id, idd, name, last)
                playerslist.add(player)

            }while (cursor.moveToNext())

        }

        return playerslist
    }


    fun deleteallRESULT() {

        val db = this.writableDatabase
        //db.execSQL("DROP TABLE $TABLE_PLAYERS")
        db.execSQL("delete  from $TABLE_HISTORY")
        db.close()

    }

}