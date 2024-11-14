package com.example.recipeapplication.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.recipeapplication.common.Converters
import com.example.recipeapplication.data.model.Recipe

@Database(entities = [Recipe::class], version = 1)
@TypeConverters(Converters::class)
abstract class RecipeRoomDB : RoomDatabase() {
    abstract fun recipeDao() : RecipeDao
}