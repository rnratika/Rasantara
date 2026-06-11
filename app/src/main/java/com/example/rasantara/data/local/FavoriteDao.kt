package com.example.rasantara.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(favoriteRecipe: FavoriteRecipe)

    @Delete
    fun delete(favoriteRecipe: FavoriteRecipe)

    @Query("SELECT * FROM favorite_recipe")
    fun getAllFavorites(): List<FavoriteRecipe>

    @Query("SELECT EXISTS(SELECT * FROM favorite_recipe WHERE idMeal = :id)")
    fun isFavorite(id: String): Boolean

    @Query("SELECT * FROM favorite_recipe WHERE idMeal = :id")
    fun getFavoriteById(id: String): FavoriteRecipe?
}