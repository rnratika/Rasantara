package com.example.rasantara.data.model

data class RecipeResponse(
    val meals: List<Recipe>?
)

data class Recipe(
    val idMeal: String,
    val strMeal: String,
    val strCategory: String,
    val strArea: String,
    val strInstructions: String,
    val strMealThumb: String,
    val strYoutube: String?
)