package com.example.recipeapplication.ui.addrecipe

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeapplication.data.model.RecyclerViewItems
import com.example.recipeapplication.databinding.RecylerviewItemBinding

class RecyclerViewItemAdapter(val ingredientsList: List<RecyclerViewItems>) : RecyclerView.Adapter<RecyclerViewItemAdapter.RecyclerViewHolder>() {
    class RecyclerViewHolder(val binding : RecylerviewItemBinding): RecyclerView.ViewHolder(binding.root){}
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val recylerviewItemBinding = RecylerviewItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return RecyclerViewHolder(recylerviewItemBinding)
    }

    override fun getItemCount(): Int {
        return ingredientsList.size
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        holder.binding.textViewId.text = (position+1).toString()
        holder.binding.textViewItems.text = ingredientsList[position].text
        holder.binding.textView.text = "."
    }

}