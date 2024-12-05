package com.example.recipeapplication.ui.ingredientsrecipe

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipeapplication.common.loadRecipeDataById
import com.example.recipeapplication.common.toRecyclerViewItemsList
import com.example.recipeapplication.data.model.Recipe
import com.example.recipeapplication.databinding.FragmentRecipeIngredientsBinding
import com.example.recipeapplication.ui.BaseFragment

class RecipeIngredientsFragment : BaseFragment<FragmentRecipeIngredientsBinding>() {

    private var recipeId: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val viewBinding = FragmentRecipeIngredientsBinding.inflate(inflater, container, false)
        setBinding(viewBinding)
        return binding.root
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            recipeId = it.getInt("recipe_id", 0)
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeViewModel()
        recipeViewModel.loadRecipeDataById(recipeId)
    }

    private fun setupRecyclerView() {
        binding.rvIngredientsItems.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun observeViewModel() {
        recipeViewModel.singleRecipe.observe(viewLifecycleOwner) { recipe ->
            recipe?.let {
                bindIngredientsAdapter(it)
            }
        }
    }

    private fun bindIngredientsAdapter(recipe: Recipe) {
        val ingredients = recipe.ingredient.toRecyclerViewItemsList()
        val adapter = IngredientsAdapter(ingredients)
        binding.rvIngredientsItems.adapter = adapter
    }

}