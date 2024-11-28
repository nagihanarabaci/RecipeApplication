package com.example.recipeapplication.ui.brieflyrecipe

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
import com.example.recipeapplication.databinding.FragmentBrieflyBinding
import com.google.gson.Gson
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers


class BrieflyFragment : Fragment() {
    private var _binding : FragmentBrieflyBinding? = null
    private val binding get() = _binding!!

    private lateinit var db : RecipeRoomDB
    private lateinit var recipeDao: RecipeDao

    private val mDisposable = CompositeDisposable()
    private var foodId: Int? = null


    companion object {
        fun newInstance(foodId: Int?): BrieflyFragment {
            val fragment = BrieflyFragment()
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
    ): View {
        _binding = FragmentBrieflyBinding.inflate(inflater,container,false)
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
            foodId?.let {
                mDisposable.add(
                    recipeDao.findById(it)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this@BrieflyFragment::handleResponse)
                )

            }
           rvBriefly.layoutManager = LinearLayoutManager(requireContext())
            foodId?.let { loadRecipe(it) }
        }


    }

    private fun handleResponse(recipes : Recipe) {
        binding.textViewBrieflyName.text = recipes.name
        binding.textViewBrieflyTime.text = recipes.times
        binding.textViewBrieflyServings.text = recipes.servings
    }


    private fun loadRecipe(recipeId:Int){
        mDisposable.add(
            recipeDao.findById(recipeId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({recipe->
                    val briefly = recipe.briefly.toRecyclerViewItemsList()
                    val adapter = RecipeBrieflyAdapter(briefly)
                    binding.rvBriefly.adapter = adapter
                },{ error->Log.e("RecipeDetailActivity", "Error loading recipe", error) })
        )

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        mDisposable.clear()
    }
}



