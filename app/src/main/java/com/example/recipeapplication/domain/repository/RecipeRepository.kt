package com.example.recipeapplication.domain.repository

import com.example.recipeapplication.data.model.Recipe
import com.example.recipeapplication.data.source.local.RecipeDao
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable

class RecipeRepository(private val recipeDao: RecipeDao) {

    fun getAllRecipes() : Flowable<List<Recipe>> {
        return recipeDao.getAll()
    }

    fun getFindById(id:Int) : Flowable<Recipe> {
        return recipeDao.findById(id)
    }

    fun insertRecipe(recipe: Recipe) : Completable {
        return recipeDao.insert(recipe)
    }

    fun deleteById(id: Int) : Completable {
        return recipeDao.deleteById(id)
    }


    companion object {
        @Volatile
        private var INSTANCE: RecipeRepository? = null

        fun getInstance(recipeDao: RecipeDao): RecipeRepository {
            return INSTANCE ?: synchronized(this) {
                val instance = RecipeRepository(recipeDao)
                INSTANCE = instance
                instance
            }
        }
    }

}