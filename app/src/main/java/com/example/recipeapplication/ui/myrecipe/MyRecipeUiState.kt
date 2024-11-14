package com.example.recipeapplication.ui.myrecipe

data class MyRecipeUiState(
    val isLoading: Boolean = false,
    val list: List<String> = emptyList(),
)