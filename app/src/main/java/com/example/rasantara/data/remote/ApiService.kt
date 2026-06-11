package com.example.rasantara.data.remote

import com.example.rasantara.data.model.RecipeResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("search.php")
    fun searchRecipes(
        @Query("s") query: String
    ): Call<RecipeResponse>

    @GET("filter.php")
    fun getRecipesByCategory(@Query("c") category: String): Call<RecipeResponse>

    @GET("lookup.php")
    fun getRecipeDetail(@Query("i") id: String): Call<RecipeResponse>

    @GET("filter.php")
    fun filterByArea(
        @Query("a") area: String
    ): Call<RecipeResponse>
}