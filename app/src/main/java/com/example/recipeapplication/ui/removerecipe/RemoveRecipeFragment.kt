package com.example.recipeapplication.ui.removerecipe

import android.app.Activity
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat.getFont
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.recipeapplication.R
import com.example.recipeapplication.data.model.Recipe
import com.example.recipeapplication.data.source.local.RecipeDao
import com.example.recipeapplication.data.source.local.RecipeRoomDB
import com.example.recipeapplication.databinding.FragmentRemoveRecipeBinding
import com.example.recipeapplication.ui.ingredientsrecipe.RecipeIngredientsFragment
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToast.Companion.GRAVITY_BOTTOM
import www.sanju.motiontoast.MotionToastStyle


class RemoveRecipeFragment : Fragment() {
    private var _binding : FragmentRemoveRecipeBinding? = null
    private val binding get() = _binding!!

    private val mDisposable = CompositeDisposable()
    private var foodId: Int? = null


    private lateinit var db : RecipeRoomDB
    private lateinit var recipeDao: RecipeDao


    companion object {
        fun newInstance(foodId: Int?): RemoveRecipeFragment {
            val fragment = RemoveRecipeFragment()
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
        // Inflate the layout for this fragment
        _binding = FragmentRemoveRecipeBinding.inflate(inflater,container,false)
        return binding.root
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        db = Room.databaseBuilder(requireContext(), RecipeRoomDB::class.java,"Recipes").build()
        recipeDao = db.recipeDao()

        arguments?.let {
            foodId = it.getInt("FOOD_ID",0)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            gifRemoveRecipe.setOnClickListener {
                foodId?.let { removeRecipe(it) }
            }

        }

    }


    fun removeRecipe(recipeId:Int) {
        if (recipeId != null){
            mDisposable.add(
                recipeDao.deleteById(recipeId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this@RemoveRecipeFragment::handleResponseForInsert)
            )
        }
    }

    private fun handleResponseForInsert(){
        //bir önceki fragmenta dön

        showMotionSuccesToast("Ürün Silme İşleminiz başaarılı")

        Handler(Looper.getMainLooper()).postDelayed({
            findNavController().navigate(R.id.myrecipeFragment)
        }, 2500)
    }

    private fun showMotionSuccesToast(message: String) {
        MotionToast.createColorToast(
            context as Activity,
            "Ürün Listeden Kaldırıldı",
            message,
            MotionToastStyle.SUCCESS,
            GRAVITY_BOTTOM,
            MotionToast.LONG_DURATION,
            getFont(requireContext(), www.sanju.motiontoast.R.font.helvetica_regular)
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        mDisposable.clear()
    }


}