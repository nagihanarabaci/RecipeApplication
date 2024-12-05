package com.example.recipeapplication.ui.myrecipe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipeapplication.data.model.Recipe
import com.example.recipeapplication.databinding.FragmentMyrecipeBinding
import com.example.recipeapplication.ui.BaseFragment

class MyRecipeFragment : BaseFragment<FragmentMyrecipeBinding>() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val viewBinding = FragmentMyrecipeBinding.inflate(inflater, container, false)
        setBinding(viewBinding)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
        observeViewModel()
        recipeViewModel.getAllRecipes()
    }

    private fun setupUI() {
        with(binding) {
            buttonAddRecipe.setOnClickListener {
                navigateToAddRecipe(it)
            }

            recyclerViewRecipe.layoutManager = LinearLayoutManager(requireContext())

            imageViewIcon.setOnApplyWindowInsetsListener { _, insets ->
                imageViewIcon.setPadding(0, insets.systemWindowInsetTop, 0, 0)
                insets
            }
        }
    }

    private fun observeViewModel() {
        recipeViewModel.allRecipes.observe(viewLifecycleOwner) { recipes ->
            setupRecyclerView(recipes)
        }
    }

    private fun setupRecyclerView(recipes: List<Recipe>) {
        val adapter = RecipeAdapter(recipes)
        binding.recyclerViewRecipe.adapter = adapter
    }

    private fun navigateToAddRecipe(view: View) {
        val action = MyRecipeFragmentDirections.actionMyrecipeFragmentToAddrecipeFragment(
            information = "yeni",
            id = 0
        )
        Navigation.findNavController(view).navigate(action)
    }


}