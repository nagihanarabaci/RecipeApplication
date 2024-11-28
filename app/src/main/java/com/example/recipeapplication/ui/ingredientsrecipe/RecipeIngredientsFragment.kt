package com.example.recipeapplication.ui.ingredientsrecipe

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.recipeapplication.common.toRecyclerViewItemsList
import com.example.recipeapplication.data.model.Recipe
import com.example.recipeapplication.data.model.RecyclerViewItems
import com.example.recipeapplication.data.source.local.RecipeDao
import com.example.recipeapplication.data.source.local.RecipeRoomDB
import com.example.recipeapplication.databinding.FragmentRecipeIngredientsBinding
import com.google.gson.Gson
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class RecipeIngredientsFragment : Fragment() {
    private var _binding: FragmentRecipeIngredientsBinding? = null
    private val binding get() = _binding!!

    private lateinit var db : RecipeRoomDB
    private lateinit var recipeDao: RecipeDao

    private val mDisposable = CompositeDisposable()
    private var foodId: Int? = null

    companion object {
        fun newInstance(foodId: Int?): RecipeIngredientsFragment {
            val fragment = RecipeIngredientsFragment()
            val args = Bundle()
            if (foodId != null) {
                args.putInt("FOOD_ID", foodId)
            } // ID'yi ekle
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRecipeIngredientsBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        db = Room.databaseBuilder(requireContext(),RecipeRoomDB::class.java,"Recipes").build()
        recipeDao = db.recipeDao()


        arguments?.let {
            foodId = it.getInt("FOOD_ID", 0)
        }
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            rvIngredientsItems.layoutManager = LinearLayoutManager(requireContext())
            foodId?.let { loadRecipe(it) }
        }

    }


    private fun loadRecipe(recipeId:Int){
        mDisposable.add(
            recipeDao.findById(recipeId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({recipe->
                    val ingredients = recipe.ingredient.toRecyclerViewItemsList()
                    val adapter = IngredientsAdapter(ingredients)
                    binding.rvIngredientsItems.adapter = adapter

                },{ error-> Log.e("RecipeIngredientsFragment", "Error loading recipe", error) })
        )
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        mDisposable.clear()
    }


}