package com.example.recipeapplication.ui.addrecipe

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat.getFont
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room


import com.example.recipeapplication.R
import com.example.recipeapplication.common.collect
import com.example.recipeapplication.data.model.Recipe
import com.example.recipeapplication.data.model.RecyclerViewItems
import com.example.recipeapplication.data.source.local.RecipeDao
import com.example.recipeapplication.data.source.local.RecipeRoomDB
import com.example.recipeapplication.databinding.FragmentAddrecipeBinding
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToast.Companion.GRAVITY_BOTTOM
import www.sanju.motiontoast.MotionToastStyle
import java.io.ByteArrayOutputStream

class AddRecipeFragment : Fragment() {

    private var _binding: FragmentAddrecipeBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<AddRecipeViewModel>()

    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>

    private var secilenGorsel : Uri?=null
    private var secilenBitmap : Bitmap?=null
    private var secilenTarif :Recipe ?=null
    // işlemleri yaptığımızda kullanmak için
    private val mDisposable = CompositeDisposable()

    private lateinit var db : RecipeRoomDB
    private lateinit var recipeDao: RecipeDao

    val ingredientsList = mutableListOf<RecyclerViewItems>()
    val brieflysList = mutableListOf<RecyclerViewItems>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerLauncher()

        db = Room.databaseBuilder(requireContext(),RecipeRoomDB::class.java,"Recipes").build()
        recipeDao = db.recipeDao()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentAddrecipeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            imageViewRecipeChoose.setOnClickListener { imageSave(it) }
            buttonAdd.setOnClickListener { saveRecipe(it,ingredientsList,brieflysList) }
            buttonRemove.setOnClickListener { removeRecipe(it) }
            buttonAddIngredients.setOnClickListener { addIngredientsItem(rvIngredients,editTextIngredients,ingredientsList) }
            buttonAddBriefly.setOnClickListener { addIngredientsItem(rvBrieflyList,editTextBriefly,brieflysList) }
            arguments?.let { it ->
                // ön sayfadan gelen bilgi gelenin kayıtlı mı yoksa ona göre kayıt etmek için bir değişkene atarız.
                val newInformation = AddRecipeFragmentArgs.fromBundle(it).information
                if (newInformation == "yeni"){
                    // yeni tarif ekleme kısmı
                    secilenTarif= null
                    buttonRemove.isEnabled = false
                    buttonAdd.isEnabled = true
                    editTextFoodName.setText("")
                    editTextIngredients.setText("")
                    editTextBriefly.setText("")
                    editTextTimes.setText("")
                    editTextServings.setText("")

                }else{
                    // eski eklenmiş tarif silme işlemini yapabiliriz.

                    buttonRemove.isEnabled = true
                    buttonAdd.isEnabled = false

                    val id = AddRecipeFragmentArgs.fromBundle(it).id

                    mDisposable.add(
                        recipeDao.findById(id)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(this@AddRecipeFragment::handleResponse)
                    )
                }
            }
        }

    }

    private fun handleResponse(recipe: Recipe){
        binding.editTextFoodName.setText(recipe.name)
        binding.editTextIngredients.setText(recipe.ingredient)
        binding.editTextBriefly.setText(recipe.briefly)
        binding.editTextTimes.setText(recipe.times)
        binding.editTextServings.setText(recipe.servings)
        val bitmap = BitmapFactory.decodeByteArray(recipe.image,0,recipe.image.size)
        binding.imageViewRecipeChoose.setImageBitmap(bitmap)
        secilenTarif = recipe
    }

    fun saveRecipe(view: View,items: List<RecyclerViewItems>,brieflyItems:List<RecyclerViewItems>) {

        val name = binding.editTextFoodName.text.toString().trim()
        val ingredient = Gson().toJson(items).trim()
        val briefly = Gson().toJson(brieflyItems).trim()
        val times = binding.editTextTimes.text.toString().trim()
        val servings = binding.editTextServings.text.toString().trim()

        if (name.isEmpty() || ingredient.isEmpty() || briefly.isEmpty() || times.isEmpty() || servings.isEmpty() || secilenBitmap==null) {
            showMotionToast("Lütfen tüm alanları doldurunuz")
        }else {
            val smallBitmap = smallBitmapCreate(secilenBitmap!!,300)
            val outputStream = ByteArrayOutputStream()
            smallBitmap.compress(Bitmap.CompressFormat.PNG,50,outputStream)
            val byteArray = outputStream.toByteArray()

            val recipe = Recipe(name,ingredient,times,servings,briefly,byteArray)


            mDisposable.add(
                recipeDao.insert(recipe)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::handleResponseForInsert))

        }

    }

    private fun handleResponseForInsert(){
        //bir önceki fragmenta dön
        findNavController().navigate(R.id.myrecipeFragment)
    }


    fun removeRecipe(view: View) {

        if (secilenTarif != null){
            mDisposable.add(
                recipeDao.delete(recipe = secilenTarif!!)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::handleResponseForInsert)
            )
        }

    }

    fun imageSave(view: View){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            if (ContextCompat.checkSelfPermission(requireContext(),android.Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED){
                // izin alma durumu galeriye erişmek için
                if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),android.Manifest.permission.READ_MEDIA_IMAGES)){
                    // snackbar gösterme kısmı, kullanıcıdan izin alma nedenini gösterme
                    Snackbar.make(view, "Galeriye erişim için izin veriniz." , Snackbar.LENGTH_INDEFINITE).setAction(
                        "İzin ver",
                        View.OnClickListener{
                            // izin isteyeceğiz.
                            permissionLauncher.launch(android.Manifest.permission.READ_MEDIA_IMAGES)
                        }
                    ).show()

                } else{
                    // izin isteyeceğiz
                    permissionLauncher.launch(android.Manifest.permission.READ_MEDIA_IMAGES)
                }
            } else{
                // galeriye erişimim var
                val intentToGalery = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToGalery)

            }
        }else{
            if (ContextCompat.checkSelfPermission(requireContext(),android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                // izin alma durumu galeriye erişmek için
                if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),android.Manifest.permission.READ_EXTERNAL_STORAGE)){
                    // snackbar gösterme kısmı, kullanıcıdan izin alma nedenini gösterme
                    Snackbar.make(view, "Galeriye erişim için izin veriniz." , Snackbar.LENGTH_INDEFINITE).setAction(
                        "İzin ver",
                        View.OnClickListener{
                            // izin isteyeceğiz.
                            permissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                        }
                    ).show()

                } else{
                    // izin isteyeceğiz
                    permissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                }
            } else{
                // galeriye erişimim var
                val intentToGalery = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToGalery)

            }
        }

    }

    private fun registerLauncher(){
        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK){
                val intentFromResult = result.data
                if (intentFromResult != null){
                    secilenGorsel = intentFromResult.data
                    try {
                        // Burada android version kontrol
                        if (Build.VERSION.SDK_INT >= 28){
                            val source = ImageDecoder.createSource(requireActivity().contentResolver,secilenGorsel!!)
                            secilenBitmap = ImageDecoder.decodeBitmap(source)
                            binding.imageViewRecipeChoose.setImageBitmap(secilenBitmap)
                        } else{
                            secilenBitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver,secilenGorsel!!)
                            binding.imageViewRecipeChoose.setImageBitmap(secilenBitmap)
                        }
                    }catch (e:Exception){
                        println(e.localizedMessage)
                    }
                }
            }
        }
        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
            if (result){
                // izin verildi
                // galeriye gidilebilir
                val intentToGalery = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToGalery)
            } else{
                // izin verilmedi
                Toast.makeText(requireContext(),"Galeriye erişim için izin verilmedi." ,Toast.LENGTH_LONG).show()
            }
        }
    }

    // Seçtiğimiz görselleri veritabanına kayderderken küçültmek için yazılan fonksiyon
    private fun smallBitmapCreate(userChooseBitmap : Bitmap,maxSize : Int) : Bitmap{
        var width = userChooseBitmap.width
        var height = userChooseBitmap.height
        val bitmapScale : Double = width.toDouble() / height.toDouble()
        if (bitmapScale > 17){
            // görsel yatay
            width = maxSize
            val smallHeight = width / bitmapScale
            height = smallHeight.toInt()
        }else {
            // görsel dikey
            height = maxSize
            val smallWidth = height * bitmapScale
            width = smallWidth.toInt()
        }
        return Bitmap.createScaledBitmap(userChooseBitmap,width,height,true)
    }



    fun addIngredientsItem(recyclerView: RecyclerView , editText: EditText, list:MutableList<RecyclerViewItems>){
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val adapter = RecyclerViewItemAdapter(list)
        recyclerView.adapter = adapter
            val newingredient =editText.text.toString()
            if (newingredient.isNotEmpty()){
                val newItem = RecyclerViewItems(newingredient)
                list.add(newItem)
                adapter.notifyDataSetChanged()
                editText.text.clear()
            }

    }


    private fun showMotionToast(message: String) {
        MotionToast.createColorToast(
            context as Activity,
            "EKSİK BİLGİ",
            message,
            MotionToastStyle.WARNING,
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