package com.example.recipeapplication.data.source.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.recipeapplication.data.model.Recipe

@Database(entities = [Recipe::class], version = 1)
abstract class RecipeRoomDB : RoomDatabase() {
    abstract fun recipeDao() : RecipeDao

    companion object {
        @Volatile
        private var INSTANCE: RecipeRoomDB? = null

        fun getInstance(context: Context): RecipeRoomDB {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RecipeRoomDB::class.java,
                    "Recipes"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}