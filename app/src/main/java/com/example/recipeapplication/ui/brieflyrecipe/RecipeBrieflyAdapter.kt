package com.example.recipeapplication.ui.brieflyrecipe

import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeapplication.R
import com.example.recipeapplication.common.colorList
import com.example.recipeapplication.data.model.RecyclerViewItems
import com.example.recipeapplication.databinding.BrieflyListItemBinding


class RecipeBrieflyAdapter(val recipeList: List<RecyclerViewItems>) : RecyclerView.Adapter<RecipeBrieflyAdapter.RecipeDetailViewHolder>() {
    class RecipeDetailViewHolder(val binding: BrieflyListItemBinding) :RecyclerView.ViewHolder(binding.root){}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeDetailViewHolder {
        val brieflyLayoutBinding = BrieflyListItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return RecipeDetailViewHolder(brieflyLayoutBinding)
    }

    override fun getItemCount(): Int {
        return recipeList.size
    }

    override fun onBindViewHolder(holder: RecipeDetailViewHolder, position: Int) {

        holder.binding.textViewBrieflyStepText.text = recipeList[position].text
        holder.binding.textViewBrieflyStepNumber.text = (position+1).toString()

        val backgroundColor = colorList[position % colorList.size]
        val drawable = ContextCompat.getDrawable(holder.itemView.context, R.drawable.recyclerview_number_bg)
        if (drawable is GradientDrawable) {
            drawable.setColor(backgroundColor)
            holder.binding.textViewBrieflyStepNumber.background = drawable
        }




    }


}