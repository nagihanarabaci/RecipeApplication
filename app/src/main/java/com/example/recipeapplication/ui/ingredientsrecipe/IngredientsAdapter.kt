package com.example.recipeapplication.ui.ingredientsrecipe

import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeapplication.R
import com.example.recipeapplication.common.colorList
import com.example.recipeapplication.data.model.RecyclerViewItems
import com.example.recipeapplication.databinding.IngredientsListItemBinding

class IngredientsAdapter(val recipeList:List<RecyclerViewItems>) : RecyclerView.Adapter<IngredientsAdapter.IngredientsViewHolder>() {
    class IngredientsViewHolder(val binding:IngredientsListItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientsViewHolder {
        val ingredientsListItemBinding = IngredientsListItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return IngredientsViewHolder(ingredientsListItemBinding)
    }

    override fun getItemCount(): Int {
        return recipeList.size
    }

    override fun onBindViewHolder(holder: IngredientsViewHolder, position: Int) {
        holder.binding.textViewIngredientsText.text = recipeList[position].text

        val backgroundColor = colorList[position % colorList.size]
        val drawable = ContextCompat.getDrawable(holder.itemView.context, R.drawable.recyclerview_number_bg)
        if (drawable is GradientDrawable) {
            drawable.setColor(backgroundColor)
            holder.binding.imageViewIngredients.background = drawable
        }
    }
}