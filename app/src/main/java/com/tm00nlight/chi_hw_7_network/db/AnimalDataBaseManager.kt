package com.tm00nlight.chi_hw_7_network.db

import android.content.ContentValues
import android.content.Context
import com.tm00nlight.chi_hw_7_network.data.Animal

class AnimalDataBaseManager(context: Context) {
    private val dataBaseHelper = AnimalDataBaseHelper(context)

    fun insertAnimals(list: List<Animal>) {
        val db = dataBaseHelper.writableDatabase

        val cv = ContentValues()
        list.forEach { animal ->
            cv.apply {
                put(Animal.NAME, animal.name)
                put(Animal.LATIN_NAME, animal.latin_name)
                put(Animal.ANIMAL_TYPE, animal.animal_type)
                put(Animal.IMAGE_LINK, animal.image_link)
            }
            db.insert(Animal.TABLE, null, cv)

            cv.clear()
        }

        db.close()
    }

    fun fetchAnimals(): List<Animal> {
        val db = dataBaseHelper.readableDatabase
        val cursor = db.query(
            Animal.TABLE, null, null, null,
            null, null, null
        )

        if (cursor != null && cursor.count > 0) {
            val animals = ArrayList<Animal>(cursor.count)
            cursor.moveToFirst()
            do {
                var index = cursor.getColumnIndex(Animal.NAME)
                val name = cursor.getString(index)

                index = cursor.getColumnIndex(Animal.LATIN_NAME)
                val latinName = cursor.getString(index)

                index = cursor.getColumnIndex(Animal.ANIMAL_TYPE)
                val animalType = cursor.getString(index)

                index = cursor.getColumnIndex(Animal.IMAGE_LINK)
                val imageLink = cursor.getString(index)

                animals.add(Animal(name, latinName, animalType, imageLink))
            } while (cursor.moveToNext())

            cursor.close()
            db.close()
            return animals
        }

        db.close()
        return emptyList()
    }

    fun deleteAnimals() {
        val db = dataBaseHelper.writableDatabase
        db.delete(Animal.TABLE, null, null)

        db.close()
    }
}