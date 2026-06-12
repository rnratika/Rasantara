package com.example.rasantara

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.rasantara.data.local.FavoriteRecipe
import com.example.rasantara.data.local.RecipeDatabase
import com.example.rasantara.data.model.Recipe
import com.example.rasantara.data.model.RecipeResponse
import com.example.rasantara.data.remote.ApiConfig
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.ExecutorService

class RecipeDetailActivity : AppCompatActivity() {

    private lateinit var ivDetailImage: ImageView
    private lateinit var tvDetailName: TextView
    private lateinit var tvDetailCategory: TextView
    private lateinit var tvDetailInstructions: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var btnRefresh: Button

    private lateinit var layoutDetailContent: ScrollView
    private lateinit var layoutDetailError: LinearLayout

    private lateinit var fabFavorite: FloatingActionButton
    private var isFavorite = false
    private lateinit var executorService: ExecutorService
    private var currentRecipe: Recipe? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_detail)

        ivDetailImage = findViewById(R.id.iv_detail_image)
        tvDetailName = findViewById(R.id.tv_detail_name)
        tvDetailCategory = findViewById(R.id.tv_detail_category)
        tvDetailInstructions = findViewById(R.id.tv_detail_instructions)
        progressBar = findViewById(R.id.pb_detail)
        btnRefresh = findViewById(R.id.btn_detail_refresh)

        layoutDetailContent = findViewById(R.id.layout_detail_content)
        layoutDetailError = findViewById(R.id.layout_detail_error)

        fabFavorite = findViewById(R.id.fab_favorite)
        executorService = RecipeDatabase.databaseWriteExecutor

        val recipeId = intent.getStringExtra("EXTRA_ID")

        if (recipeId != null) {
            checkFavoriteStatus(recipeId)
            getRecipeDetail(recipeId)
        } else {
            Toast.makeText(this, "ID Resep tidak ditemukan", Toast.LENGTH_SHORT).show()
        }

        btnRefresh.setOnClickListener {
            if (recipeId != null) {
                getRecipeDetail(recipeId)
            }
        }

        fabFavorite.setOnClickListener {
            if (currentRecipe != null) {
                toggleFavorite(currentRecipe!!)
            } else {
                Toast.makeText(this, "Tunggu data selesai dimuat", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getRecipeDetail(id: String) {
        progressBar.visibility = View.VISIBLE

        layoutDetailContent.visibility = View.GONE
        layoutDetailError.visibility = View.GONE

        val client = ApiConfig.getApiService().getRecipeDetail(id)

        client.enqueue(object : Callback<RecipeResponse> {
            override fun onResponse(call: Call<RecipeResponse>, response: Response<RecipeResponse>) {
                progressBar.visibility = View.GONE

                if (response.isSuccessful) {
                    val recipe = response.body()?.meals?.get(0)
                    if (recipe != null) {
                        layoutDetailContent.visibility = View.VISIBLE
                        layoutDetailError.visibility = View.GONE
                        currentRecipe = recipe

                        tvDetailName.text = recipe.strMeal
                        tvDetailCategory.text = "${recipe.strCategory} • ${recipe.strArea}"
                        tvDetailInstructions.text = recipe.strInstructions

                        Glide.with(this@RecipeDetailActivity)
                            .load(recipe.strMealThumb)
                            .into(ivDetailImage)
                    } else {
                        layoutDetailContent.visibility = View.GONE
                        layoutDetailError.visibility = View.VISIBLE
                        Toast.makeText(this@RecipeDetailActivity, "Data resep kosong", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    layoutDetailContent.visibility = View.GONE
                    layoutDetailError.visibility = View.VISIBLE
                    Toast.makeText(this@RecipeDetailActivity, "Gagal memuat data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<RecipeResponse>, t: Throwable) {
                loadOfflineData(id)
            }
        })
    }

    private fun loadOfflineData(id: String) {
        executorService.execute {
            val favoriteDao = RecipeDatabase.getDatabase(this@RecipeDetailActivity).favoriteDao()
            val offlineRecipe = favoriteDao.getFavoriteById(id)

            runOnUiThread {
                progressBar.visibility = View.GONE
                if (offlineRecipe != null) {
                    layoutDetailContent.visibility = View.VISIBLE
                    layoutDetailError.visibility = View.GONE

                    tvDetailName.text = offlineRecipe.strMeal
                    tvDetailCategory.text = "${offlineRecipe.strCategory} • ${offlineRecipe.strArea}"
                    tvDetailInstructions.text = offlineRecipe.strInstructions

                    Glide.with(this@RecipeDetailActivity)
                        .load(offlineRecipe.strMealThumb)
                        .into(ivDetailImage)
                } else {
                    layoutDetailContent.visibility = View.GONE
                    layoutDetailError.visibility = View.VISIBLE
                    Toast.makeText(this@RecipeDetailActivity, "Tidak ada koneksi internet", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun checkFavoriteStatus(id: String) {
        executorService.execute {
            val favoriteDao = RecipeDatabase.getDatabase(this).favoriteDao()
            isFavorite = favoriteDao.isFavorite(id)

            runOnUiThread {
                setFavoriteIcon()
            }
        }
    }

    private fun toggleFavorite(recipe: Recipe) {
        executorService.execute {
            val favoriteDao = RecipeDatabase.getDatabase(this).favoriteDao()
            val favRecipe = FavoriteRecipe(
                idMeal = recipe.idMeal,
                strMeal = recipe.strMeal,
                strMealThumb = recipe.strMealThumb,
                strCategory = recipe.strCategory,
                strArea = recipe.strArea,
                strInstructions = recipe.strInstructions ?: ""
            )

            if (isFavorite) {
                favoriteDao.delete(favRecipe)
            } else {
                favoriteDao.insert(favRecipe)
            }

            isFavorite = !isFavorite

            runOnUiThread {
                setFavoriteIcon()
                val message = if (isFavorite) "Ditambahkan ke Favorit" else "Dihapus dari Favorit"
                Toast.makeText(this@RecipeDetailActivity, message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setFavoriteIcon() {
        if (isFavorite) {
            fabFavorite.setImageResource(R.drawable.ic_love_filled)
        } else {
            fabFavorite.setImageResource(R.drawable.ic_love_outline)
        }
    }
}