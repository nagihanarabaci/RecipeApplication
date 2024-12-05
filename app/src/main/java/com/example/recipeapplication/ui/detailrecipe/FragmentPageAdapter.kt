package com.example.recipeapplication.ui.detailrecipe

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.recipeapplication.common.FragmentArgumentHelper
import com.example.recipeapplication.ui.brieflyrecipe.BrieflyFragment
import com.example.recipeapplication.ui.ingredientsrecipe.RecipeIngredientsFragment
import com.example.recipeapplication.ui.removerecipe.RemoveRecipeFragment

class FragmentPageAdapter (fragmentManager: Fragment,private val foodId : Int?) :
     FragmentStateAdapter(fragmentManager) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return  when (position) {
            0 -> FragmentArgumentHelper.createFragmentWithArgument(
                RecipeIngredientsFragment::class.java, "recipe_id",
                foodId ?: 0
            )
            1 -> FragmentArgumentHelper.createFragmentWithArgument(
                BrieflyFragment::class.java,"recipe_id",
                foodId ?: 0
            )
            2 -> FragmentArgumentHelper.createFragmentWithArgument(
                RemoveRecipeFragment::class.java,"recipe_id",
                foodId ?: 0
            )
            else -> FragmentArgumentHelper.createFragmentWithArgument(
                RecipeIngredientsFragment::class.java, "recipe_id",
                foodId ?: 0
            )
        }

    }
}