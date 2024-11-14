package com.example.recipeapplication.ui.myrecipe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.recipeapplication.common.collect
import com.example.recipeapplication.data.model.Recipe
import com.example.recipeapplication.data.source.local.RecipeDao
import com.example.recipeapplication.data.source.local.RecipeRoomDB
import com.example.recipeapplication.databinding.FragmentMyrecipeBinding
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class MyRecipeFragment : Fragment() {

    private var _binding: FragmentMyrecipeBinding? = null
    private val binding get() = _binding!!

    private lateinit var db : RecipeRoomDB
    private lateinit var recipeDao: RecipeDao

    private val mDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = Room.databaseBuilder(requireContext(),RecipeRoomDB::class.java,"Recipes").build()
        recipeDao = db.recipeDao()
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMyrecipeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            buttonAddRecipe.setOnClickListener {
                addRecipe(it)
            }
            recyclerViewRecipe.layoutManager = LinearLayoutManager(requireContext())

            imageView4.setOnApplyWindowInsetsListener { v, insets ->
                imageView4.setPadding(0,insets.systemWindowInsetTop,0,0)
                insets
            }

//            buttonAddRecipe.setOnApplyWindowInsetsListener { v, insets ->
//                buttonAddRecipe.setPadding(0,0,0,insets.systemWindowInsetBottom,+16)
//                insets
//            }


        }

        getRecipes()
    }

    private fun getRecipes() {
        mDisposable.add(
            recipeDao.getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleResponse)
        )
    }

    private fun handleResponse(recipes : List<Recipe>) {
        val adapter = RecipeAdapter(requireContext(),recipes)
        binding.recyclerViewRecipe.adapter = adapter
    }

    fun addRecipe(view: View) {
        val action = MyRecipeFragmentDirections.actionMyrecipeFragmentToAddrecipeFragment(information = "yeni", id = 0)
        Navigation.findNavController(view).navigate(action)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        mDisposable.clear()
    }
}