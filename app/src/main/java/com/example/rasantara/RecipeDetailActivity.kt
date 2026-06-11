package com.example.rasantara

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.rasantara.data.model.RecipeResponse
import com.example.rasantara.data.remote.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RecipeDetailActivity : AppCompatActivity() {

    private lateinit var ivDetailImage: ImageView
    private lateinit var tvDetailName: TextView
    private lateinit var tvDetailCategory: TextView
    private lateinit var tvDetailInstructions: TextView
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_detail)

        ivDetailImage = findViewById(R.id.iv_detail_image)
        tvDetailName = findViewById(R.id.tv_detail_name)
        tvDetailCategory = findViewById(R.id.tv_detail_category)
        tvDetailInstructions = findViewById(R.id.tv_detail_instructions)
        progressBar = findViewById(R.id.pb_detail)

        val recipeId = intent.getStringExtra("EXTRA_ID")

        if (recipeId != null) {
            getRecipeDetail(recipeId)
        } else {
            Toast.makeText(this, "ID Resep tidak ditemukan", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getRecipeDetail(id: String) {
        progressBar.visibility = View.VISIBLE

        val client = ApiConfig.getApiService().getRecipeDetail(id)

        client.enqueue(object : Callback<RecipeResponse> {
            override fun onResponse(call: Call<RecipeResponse>, response: Response<RecipeResponse>) {
                progressBar.visibility = View.GONE

                if (response.isSuccessful) {
                    val recipe = response.body()?.meals?.get(0) // Ambil data resep pertama
                    if (recipe != null) {

                        tvDetailName.text = recipe.strMeal
                        tvDetailCategory.text = "${recipe.strCategory} • ${recipe.strArea}"
                        tvDetailInstructions.text = recipe.strInstructions

                        Glide.with(this@RecipeDetailActivity)
                            .load(recipe.strMealThumb)
                            .into(ivDetailImage)
                    }
                } else {
                    Toast.makeText(this@RecipeDetailActivity, "Gagal memuat data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<RecipeResponse>, t: Throwable) {
                progressBar.visibility = View.GONE
                Toast.makeText(this@RecipeDetailActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}