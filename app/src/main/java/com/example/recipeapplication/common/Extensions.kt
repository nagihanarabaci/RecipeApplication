package com.example.recipeapplication.common

import com.example.recipeapplication.data.model.RecyclerViewItems
import com.google.gson.Gson



// JSON string'ini listeye dönüştüren extension function
fun String.toRecyclerViewItemsList(): List<RecyclerViewItems> {
    return Gson().fromJson(this, Array<RecyclerViewItems>::class.java).toList()
}
