package com.example.recipeapplication.common

import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.recipeapplication.R
import com.example.recipeapplication.data.model.RecyclerViewItems
import com.example.recipeapplication.ui.RecipeViewModel
import com.google.gson.Gson


// JSON string'ini listeye dönüştüren extension fonksiyon
fun String.toRecyclerViewItemsList(): List<RecyclerViewItems> {
    return Gson().fromJson(this, Array<RecyclerViewItems>::class.java).toList()
}

// Sayfalar arasında geçişlerde bekleme süresi
fun Fragment.navigateWithDelay() {
    Handler(Looper.getMainLooper()).postDelayed({
        findNavController().navigate(R.id.myrecipeFragment)
    }, 2000)
}

// ViewModelden id'ye göre tariflleri alan fonksiyon
fun RecipeViewModel.loadRecipeDataById(id: Int?) {
    id?.let {
        getRecipeById(it)
    }
}
