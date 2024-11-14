package com.example.recipeapplication.ui

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.recipeapplication.ui.brieflyrecipe.BrieflyFragment
import com.example.recipeapplication.ui.ingredientsrecipe.RecipeIngredientsFragment
import com.example.recipeapplication.ui.myrecipe.MyRecipeFragment

class FragmentPageAdapter (fragmentManager: Fragment,private val foodId : Int?) :
     FragmentStateAdapter(fragmentManager) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return  when (position) {
            0 -> RecipeIngredientsFragment.newInstance(foodId)
            1 -> BrieflyFragment.newInstance(foodId)
            2 -> MyRecipeFragment()
            else -> RecipeIngredientsFragment.newInstance(foodId)
        }

    }
}