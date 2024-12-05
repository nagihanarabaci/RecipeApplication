package com.example.recipeapplication.ui.detailrecipe

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.recipeapplication.common.TabLayoutHelper
import com.example.recipeapplication.common.loadRecipeDataById
import com.example.recipeapplication.common.showMotionToast
import com.example.recipeapplication.data.model.Recipe
import com.example.recipeapplication.databinding.FragmentRecipeDetailBinding
import com.example.recipeapplication.ui.BaseFragment
import www.sanju.motiontoast.MotionToastStyle


class RecipeDetailFragment : BaseFragment<FragmentRecipeDetailBinding>() {

    private var recipeId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        recipeId = arguments?.let { RecipeDetailFragmentArgs.fromBundle(it).id }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val viewBinding = FragmentRecipeDetailBinding.inflate(inflater, container, false)
        setBinding(viewBinding)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
        recipeViewModel.loadRecipeDataById(recipeId)
        observeViewModel()

    }


    private fun setupUI() {

        val adapter = FragmentPageAdapter(this,recipeId)
        binding.viewPager2.adapter = adapter

        binding.imageButtonCancel.setOnClickListener { parentFragmentManager.popBackStack() }
        binding.imageButtonFavorite.setOnClickListener {
            requireContext().showMotionToast(
                "Bilgilendirme",
                "Şu anda bu işlem yapılandırılmamıştır",
                style = MotionToastStyle.INFO
            )
        }

        TabLayoutHelper.setupTabs(
            binding.tableLayout,
            binding.viewPager2,
            listOf("Briefly", "Ingredients", "Remove Recipe"),
            requireContext()
        )
    }

    private fun observeViewModel() {
        recipeViewModel.singleRecipe.observe(viewLifecycleOwner) { recipe ->
            recipe?.let {
                updateRecipeDetails(it)
            }
        }
    }

    private fun updateRecipeDetails(recipe: Recipe) {
        val bitmap = BitmapFactory.decodeByteArray(recipe.image, 0, recipe.image.size)
        binding.imageViewDetailFood.setImageBitmap(bitmap)
    }

}