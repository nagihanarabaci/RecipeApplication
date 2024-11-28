package com.example.recipeapplication.data.source.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.recipeapplication.data.model.Recipe
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable

@Dao
interface RecipeDao {

    @Query("SELECT * FROM Recipe")
    fun getAll() : Flowable<List<Recipe>>

    @Query("SELECT * FROM Recipe WHERE id = :id")
    fun findById(id: Int) : Flowable<Recipe>

    @Insert
    fun insert(recipe: Recipe) : Completable

    @Delete
    fun delete(recipe: Recipe) : Completable

    @Query("DELETE  FROM Recipe WHERE id = :id")
    fun deleteById(id:Int) : Completable

    @Insert
    fun insertItem(ingredientsItem: Recipe) : Completable


}