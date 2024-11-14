package com.example.recipeapplication.ui.addrecipe

data class AddRecipeUiState(
    val isLoading: Boolean = false,
    val list: List<String> = emptyList(),
)