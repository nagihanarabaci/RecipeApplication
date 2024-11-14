package com.example.recipeapplication.ui.myrecipe

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MyRecipeViewModel : ViewModel() {

    private var _uiState = MutableStateFlow(MyRecipeUiState())
    val uiState: StateFlow<MyRecipeUiState> = _uiState.asStateFlow()

}