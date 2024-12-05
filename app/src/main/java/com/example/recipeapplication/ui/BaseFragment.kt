package com.example.recipeapplication.ui

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.example.recipeapplication.data.source.local.RecipeRoomDB
import com.example.recipeapplication.domain.repository.RecipeRepository
import com.example.recipeapplication.domain.repository.RecipeViewModelFactory

open class BaseFragment<VB : ViewBinding> : Fragment() {

    private var _binding: VB? = null
    protected val binding get() = _binding!!

    protected val recipeDao by lazy {
        RecipeRoomDB.getInstance(requireContext()).recipeDao()
    }

    protected val recipeRepository by lazy {
        RecipeRepository.getInstance(recipeDao)
    }

    protected val recipeViewModel by lazy {
        ViewModelProvider(
            this,
            RecipeViewModelFactory(recipeRepository)
        ).get(RecipeViewModel::class.java)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun setBinding(binding: VB) {
        _binding = binding
    }
}


