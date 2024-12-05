package com.example.recipeapplication.ui.brieflyrecipe

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.recipeapplication.common.loadRecipeDataById
import com.example.recipeapplication.common.toRecyclerViewItemsList
import com.example.recipeapplication.data.model.Recipe
import com.example.recipeapplication.data.model.RecyclerViewItems
import com.example.recipeapplication.data.source.local.RecipeDao
import com.example.recipeapplication.data.source.local.RecipeRoomDB
import com.example.recipeapplication.databinding.FragmentBrieflyBinding
import com.example.recipeapplication.domain.repository.RecipeRepository
import com.example.recipeapplication.domain.repository.RecipeViewModelFactory
import com.example.recipeapplication.ui.BaseFragment
import com.example.recipeapplication.ui.RecipeViewModel
import com.google.gson.Gson
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers


class BrieflyFragment : BaseFragment<FragmentBrieflyBinding>() {

    private var recipeId: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val viewBinding = FragmentBrieflyBinding.inflate(inflater, container, false)
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
        recipeViewModel.loadRecipeDataById(recipeId)
        observeViewModel()

    }

    private fun setupRecyclerView() {
        binding.rvBriefly.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun observeViewModel() {
        recipeViewModel.singleRecipe.observe(viewLifecycleOwner) { recipe ->
            recipe?.let {
                bindRecipeDetails(it)
                bindBrieflyAdapter(it)
            }
        }
    }

    private fun bindRecipeDetails(recipe: Recipe) {
        with(binding) {
            textViewBrieflyName.text = recipe.name
            textViewBrieflyTime.text = recipe.times
            textViewBrieflyServings.text = recipe.servings
        }
    }

    private fun bindBrieflyAdapter(recipe: Recipe) {
        val brieflyItems = recipe.briefly.toRecyclerViewItemsList()
        val adapter = RecipeBrieflyAdapter(brieflyItems)
        binding.rvBriefly.adapter = adapter
    }

}



