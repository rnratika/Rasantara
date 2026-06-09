package com.example.rasantara.data.remote

import com.example.rasantara.data.model.RecipeResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    // cari resep berdasarkan nama
    @GET("search.php")
    fun searchRecipes(@Query("s") query: String): Call<RecipeResponse>

    // dapatkan resep berdasarkan kategori
    @GET("filter.php")
    fun getRecipesByCategory(@Query("c") category: String): Call<RecipeResponse>

    // detail resep berdasarkan ID
    @GET("lookup.php")
    fun getRecipeDetail(@Query("i") id: String): Call<RecipeResponse>
}