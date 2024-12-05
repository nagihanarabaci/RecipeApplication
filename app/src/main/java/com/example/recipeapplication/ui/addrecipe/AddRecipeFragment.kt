package com.example.recipeapplication.ui.addrecipe


import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.recipeapplication.common.GalleryHelper
import com.example.recipeapplication.common.RecyclerViewHelper
import com.example.recipeapplication.common.navigateWithDelay
import com.example.recipeapplication.common.showMotionToast
import com.example.recipeapplication.data.model.Recipe
import com.example.recipeapplication.data.model.RecyclerViewItems
import com.example.recipeapplication.databinding.FragmentAddrecipeBinding
import com.example.recipeapplication.ui.BaseFragment
import com.google.gson.Gson
import www.sanju.motiontoast.MotionToastStyle
import java.io.ByteArrayOutputStream

class AddRecipeFragment : BaseFragment<FragmentAddrecipeBinding>(){

    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>

    private var secilenGorsel: Uri? = null
    private var secilenBitmap: Bitmap? = null


    private val ingredientsList = mutableListOf<RecyclerViewItems>()
    private val brieflyList = mutableListOf<RecyclerViewItems>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerLauncher()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val viewBinding = FragmentAddrecipeBinding.inflate(inflater, container, false)
        setBinding(viewBinding)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            imageViewRecipeChoose.setOnClickListener {
                GalleryHelper.checkPermissionAndLaunch(
                    this@AddRecipeFragment,
                    view,
                    permissionLauncher,
                    activityResultLauncher
                )
            }
            buttonAdd.setOnClickListener {
                saveRecipe(ingredientsList, brieflyList)
            }
            buttonAddIngredients.setOnClickListener {
                RecyclerViewHelper.setupRecyclerView(
                    rvIngredients,
                    editTextIngredients,
                    ingredientsList,
                    requireContext()
                )
            }
            buttonAddBriefly.setOnClickListener {
                RecyclerViewHelper.setupRecyclerView(
                    rvBrieflyList,
                    editTextBriefly,
                    brieflyList,
                    requireContext()
                )
            }

        }

    }

    private fun saveRecipe(
        ingredientsItems: List<RecyclerViewItems>,
        brieflyItems: List<RecyclerViewItems>
    ) {
        if (!isRecipeDataValid(ingredientsItems, brieflyItems)) {
            showMissingFieldsToast()
            return
        }

        val recipe = createRecipeObject(ingredientsItems, brieflyItems)
        insertRecipeIntoViewModel(recipe)
    }

    private fun isRecipeDataValid(
        ingredientsItems: List<RecyclerViewItems>,
        brieflyItems: List<RecyclerViewItems>
    ): Boolean {
        return binding.editTextFoodName.text.isNotEmpty() &&
                ingredientsItems.isNotEmpty() &&
                brieflyItems.isNotEmpty() &&
                binding.editTextTimes.text.isNotEmpty() &&
                binding.editTextServings.text.isNotEmpty() &&
                secilenBitmap != null
    }

    private fun createRecipeObject(
        ingredientsItems: List<RecyclerViewItems>,
        brieflyItems: List<RecyclerViewItems>
    ): Recipe {
        val name = binding.editTextFoodName.text.toString().trim()
        val ingredient = Gson().toJson(ingredientsItems).trim()
        val briefly = Gson().toJson(brieflyItems).trim()
        val times = binding.editTextTimes.text.toString().trim()
        val servings = binding.editTextServings.text.toString().trim()
        val smallBitmap = GalleryHelper.createSmallBitmap(secilenBitmap!!, 300)
        val outputStream = ByteArrayOutputStream()
        smallBitmap.compress(Bitmap.CompressFormat.PNG, 50, outputStream)
        val byteArray = outputStream.toByteArray()

        return Recipe(name, ingredient, times, servings, briefly, byteArray)
    }

    private fun insertRecipeIntoViewModel(recipe: Recipe) {
        recipeViewModel.insertRecipe(recipe)
        recipeViewModel.recipeStatus.observe(viewLifecycleOwner) { success ->
            if (success) {
                showInsertSuccessToastAndNavigate()
            } else {
                showInsertFailedToastAndNavigate()
            }
        }
    }

    private fun showInsertSuccessToastAndNavigate() {
        requireContext().showMotionToast(
            "Ekleme Başarılı",
            "Ürün Ekleme İşleminiz başarılı",
            style = MotionToastStyle.SUCCESS
        )
        navigateWithDelay()
    }

    private fun showInsertFailedToastAndNavigate() {
        requireContext().showMotionToast(
            "Ekleme Başarısız",
            "Ürün Ekleme İşleminiz başarısız tekrar deneyiniz.",
            style = MotionToastStyle.ERROR
        )
        navigateWithDelay()
    }

    private fun showMissingFieldsToast() {
        requireContext().showMotionToast(
            "Eksik Bilgi",
            "Lütfen tüm alanları doldurunuz",
            style = MotionToastStyle.WARNING
        )
    }


    private fun registerLauncher() {
        activityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == AppCompatActivity.RESULT_OK) {
                    val intentFromResult = result.data
                    if (intentFromResult != null) {
                        secilenGorsel = intentFromResult.data
                        try {
                            // Burada android version kontrol
                            if (Build.VERSION.SDK_INT >= 28) {
                                val source = ImageDecoder.createSource(
                                    requireActivity().contentResolver,
                                    secilenGorsel!!
                                )
                                secilenBitmap = ImageDecoder.decodeBitmap(source)
                                binding.imageViewRecipeChoose.setImageBitmap(secilenBitmap)
                            } else {
                                secilenBitmap = MediaStore.Images.Media.getBitmap(
                                    requireActivity().contentResolver,
                                    secilenGorsel!!
                                )
                                binding.imageViewRecipeChoose.setImageBitmap(secilenBitmap)
                            }
                        } catch (e: Exception) {
                            println(e.localizedMessage)
                        }
                    }
                }
            }
        permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
                if (result) {
                    // izin verildi
                    // galeriye gidilebilir
                    val intentToGalery =
                        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    activityResultLauncher.launch(intentToGalery)
                } else {
                    // izin verilmedi
                    Toast.makeText(
                        requireContext(),
                        "Galeriye erişim için izin verilmedi.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }

}