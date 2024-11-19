package com.example.recipeapplication.ui.myrecipe

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat.*
import androidx.navigation.Navigation
import androidx.navigation.Navigation.findNavController

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipeapplication.data.model.Recipe
import com.example.recipeapplication.databinding.CardDesignBinding

import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToast.Companion.GRAVITY_BOTTOM
import www.sanju.motiontoast.MotionToastStyle


class RecipeAdapter (private val context: Context,val recipeList: List<Recipe>) : RecyclerView.Adapter<RecipeAdapter.RecipeHolder>() {

    class RecipeHolder(val binding: CardDesignBinding) : RecyclerView.ViewHolder(binding.root) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeHolder {
        val cardDesignBinding = CardDesignBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return RecipeHolder(cardDesignBinding)
    }

    override fun getItemCount(): Int {
        return recipeList.size
    }


    override fun onBindViewHolder(holder: RecipeHolder, position: Int) {
        holder.binding.textViewFoodNameRc.text = recipeList[position].name
        holder.binding.textViewTimeRc.text = buildString {
            append(recipeList[position].times)
            append(" min.")
        }
        holder.binding.textViewServingsRc.text = buildString {
            append(recipeList[position].servings)
            append(" per.")
        }

        holder.itemView.setOnClickListener {
            val action = MyRecipeFragmentDirections.actionMyrecipeFragmentToAddrecipeFragment(information = "eski", id = recipeList[position].id)
            findNavController(it).navigate(action)
        }

        Glide.with(holder.itemView.context)
            .load(recipeList[position].image)
            .into(holder.binding.imageViewFoodImage)
        //.transform(CircleCrop()) bu imagei yuvarlak yapar

        // Favori butonuna tıklama işlevi
        holder.binding.buttonFavorite.setOnClickListener {
            showMotionToast("Şu anda bu işlem yapılandırılmamıştır")
        }


        holder.binding.cardView.setOnClickListener {
            val action = MyRecipeFragmentDirections.actionMyrecipeFragmentToRecipeDetailFragment(id= recipeList[position].id)
            findNavController(it).navigate(action)
        }

    }


    private fun showMotionToast(message: String) {
        MotionToast.createColorToast(
            context as Activity,
            "Bilgilendirme",
            message,
            MotionToastStyle.INFO,
            GRAVITY_BOTTOM,
            MotionToast.LONG_DURATION,
            getFont(context, www.sanju.motiontoast.R.font.helvetica_regular)
        )
    }





}