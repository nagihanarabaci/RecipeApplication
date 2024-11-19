package com.example.recipeapplication.ui

import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat.getFont
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.room.Room
import androidx.viewpager2.widget.ViewPager2
import com.example.recipeapplication.data.model.Recipe
import com.example.recipeapplication.data.source.local.RecipeDao
import com.example.recipeapplication.data.source.local.RecipeRoomDB
import com.example.recipeapplication.databinding.FragmentRecipeDetailBinding
import com.google.android.material.tabs.TabLayout
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToast.Companion.GRAVITY_BOTTOM
import www.sanju.motiontoast.MotionToastStyle


class RecipeDetailFragment : Fragment() {
    private var _binding: FragmentRecipeDetailBinding? = null
    private val binding get() = _binding!!
    private val disposables = CompositeDisposable()

    private lateinit var db : RecipeRoomDB
    private lateinit var recipeDao: RecipeDao

    private  var foodImage : Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        db = Room.databaseBuilder(requireContext(),RecipeRoomDB::class.java,"Recipes").build()
        recipeDao = db.recipeDao()

        val args: RecipeDetailFragmentArgs by navArgs()
        foodImage = args.id
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipeDetailBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = FragmentPageAdapter(this,foodImage)

        with(binding) {
            arguments?.let {
                foodImage = RecipeDetailFragmentArgs.fromBundle(it).id
                disposables.add(
                    recipeDao.findById(foodImage!!)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this@RecipeDetailFragment::handleResponse)
                )

            }

            imageButtonCancel.setOnClickListener { parentFragmentManager.popBackStack() }

            imageButtonFavorite.setOnClickListener { showMotionToast("Şu anda bu işlem gerceklestirilemiyor..") }
            viewPager2.adapter = adapter
            tableLayout.addTab(tableLayout.newTab().setText("Ingredients"))
            tableLayout.addTab(tableLayout.newTab().setText("Briefly"))
            tableLayout.addTab(tableLayout.newTab().setText("Remove Recipe"))

            for (i in 0 until tableLayout.getTabCount()) {
                val tab: TabLayout.Tab? = tableLayout.getTabAt(i)
                if (tab != null) {
                    val tabTextView = TextView(requireContext())
                    tabTextView.text = tab.text
                    tabTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f) // Boyutu ayarla
                    tabTextView.gravity = Gravity.CENTER
                    tabTextView.isAllCaps = true
                    tabTextView.setTextColor(Color.BLACK)
                    tab.setCustomView(tabTextView)
                }
            }


            tableLayout.addOnTabSelectedListener(object :TabLayout.OnTabSelectedListener{
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    if (tab != null){
                        viewPager2.currentItem = tab.position

                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {

                }

            })

            viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    tableLayout.selectTab(tableLayout.getTabAt(position))
                }
            })
        }

    }

    private fun handleResponse(recipe: Recipe){
        val bitmap = BitmapFactory.decodeByteArray(recipe.image,0,recipe.image.size)
        binding.imageViewDetailFood.setImageBitmap(bitmap)
    }




    private fun showMotionToast(message: String) {
        MotionToast.createColorToast(
            requireActivity(),
            "Bilgilendirme",
            message,
            MotionToastStyle.INFO,
            GRAVITY_BOTTOM,
            MotionToast.LONG_DURATION,
            getFont(requireContext(), www.sanju.motiontoast.R.font.helvetica_regular)
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}