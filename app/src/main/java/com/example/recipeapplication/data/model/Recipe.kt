package com.example.recipeapplication.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Recipe(

    @ColumnInfo(name = "name")
    var name : String,

    @ColumnInfo(name = "ingredient")
    var ingredient : String,

    @ColumnInfo(name = "times")
    var times : String,

    @ColumnInfo(name = "servings")
    var servings : String,

    @ColumnInfo(name = "briefly")
    var briefly : String,

    @ColumnInfo(name = "image")
    var image : ByteArray,


){
    @PrimaryKey(autoGenerate = true)
    var id = 0
}
