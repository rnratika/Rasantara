package com.example.rasantara.data.model

import com.google.gson.annotations.SerializedName

data class RecipeResponse(
    @SerializedName("meals")
    val meals: List<Recipe>?
)

data class Recipe(
    @SerializedName("idMeal")
    val idMeal: String,
    @SerializedName("strMeal")
    val strMeal: String,
    @SerializedName("strCategory")
    val strCategory: String,
    @SerializedName("strArea")
    val strArea: String,
    @SerializedName("strInstructions")
    val strInstructions: String,
    @SerializedName("strMealThumb")
    val strMealThumb: String,
    @SerializedName("strYoutube")
    val strYoutube: String?
)