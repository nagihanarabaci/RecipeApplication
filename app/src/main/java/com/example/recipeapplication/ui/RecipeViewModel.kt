package com.example.recipeapplication.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recipeapplication.data.model.Recipe
import com.example.recipeapplication.domain.repository.RecipeRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class RecipeViewModel(private val repository: RecipeRepository) : ViewModel() {

    private val _allRecipes = MutableLiveData<List<Recipe>>()
    val allRecipes: LiveData<List<Recipe>> get() = _allRecipes

    private val _singleRecipe = MutableLiveData<Recipe>()
    val singleRecipe: LiveData<Recipe> get() = _singleRecipe

    private val _recipeStatus = MutableLiveData<Boolean>()
    val recipeStatus: LiveData<Boolean> get() = _recipeStatus

    private val compositeDisposable = CompositeDisposable()


    fun getAllRecipes() {
        compositeDisposable.add(
            repository.getAllRecipes().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ recipes -> _allRecipes.postValue(recipes) },
                    { throwable -> throwable.printStackTrace() })
        )
    }

    fun getRecipeById(id: Int) {
        compositeDisposable.add(
            repository.getFindById(id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ recipe -> _singleRecipe.postValue(recipe) },
                    { throwable -> throwable.printStackTrace() })
        )
    }

    fun insertRecipe(recipe: Recipe) {
        compositeDisposable.add(
            repository.insertRecipe(recipe).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ _recipeStatus.value = true }, { throwable ->
                    _recipeStatus.value = false
                    throwable.printStackTrace()
                })
        )
    }

    fun deleteById(id: Int) {
        compositeDisposable.add(repository.deleteById(id).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ _recipeStatus.value = true }, { throwable ->
                _recipeStatus.value = false
                throwable.printStackTrace()
            })
        )
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }


}