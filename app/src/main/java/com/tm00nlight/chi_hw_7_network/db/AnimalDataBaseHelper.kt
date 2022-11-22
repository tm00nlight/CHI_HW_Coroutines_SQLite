package com.tm00nlight.chi_hw_7_network.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.tm00nlight.chi_hw_7_network.data.Animal

class AnimalDataBaseHelper(context: Context) :
    SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    override fun onCreate(db: SQLiteDatabase?) {
        val createAnimalSql = "create table ${Animal.TABLE} (" +
                Animal.ID + " integer primary key autoincrement, " +
                Animal.NAME + " text not null, " +
                Animal.LATIN_NAME + " text not null, " +
                Animal.ANIMAL_TYPE + " text not null, " +
                Animal.IMAGE_LINK + " text not null);"
        db?.execSQL(createAnimalSql)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db?.execSQL("drop table if exists " + Animal.TABLE)
        onCreate(db)
    }

    companion object {
        private const val DB_NAME = "Animal_DB"
        private const val DB_VERSION = 1
    }
}