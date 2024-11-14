package com.example.recipeapplication.common

import androidx.room.TypeConverter
import com.example.recipeapplication.data.model.RecyclerViewItems
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    @TypeConverter
    fun fromRecyclerViewItemsList(value: List<RecyclerViewItems>):String {
        val gson = Gson()
        return gson.toJson(value)
    }

    @TypeConverter
    fun toRecyclerViewItemList(value : String):List<RecyclerViewItems> {
        val gson = Gson()
        val listType = object : TypeToken<List<RecyclerViewItems>>() {}.type
        return gson.fromJson(value,listType)
    }
}