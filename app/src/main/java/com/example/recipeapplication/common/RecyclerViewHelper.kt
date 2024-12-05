package com.example.recipeapplication.common

import android.annotation.SuppressLint
import android.content.Context
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeapplication.data.model.RecyclerViewItems
import com.example.recipeapplication.ui.addrecipe.RecyclerViewItemAdapter

object RecyclerViewHelper {
    @SuppressLint("NotifyDataSetChanged")
    fun setupRecyclerView(
        recyclerView: RecyclerView,
        editText: EditText,
        list: MutableList<RecyclerViewItems>,
        context: Context
    ) {
        recyclerView.layoutManager = LinearLayoutManager(context)
        val adapter = RecyclerViewItemAdapter(list)
        recyclerView.adapter = adapter
        val newItemText = editText.text.toString()
        if (newItemText.isNotEmpty()) {
            list.add(RecyclerViewItems(newItemText))
            adapter.notifyDataSetChanged()
            editText.text.clear()
        }
    }
}