package com.example.rasantara.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.rasantara.R
import com.example.rasantara.RecipeDetailActivity
import com.example.rasantara.data.model.RecipeResponse
import com.example.rasantara.data.remote.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// Import model Recipe Anda di sini
import com.example.rasantara.data.model.Recipe

class HomeFragment : Fragment() {

    private lateinit var rvCarousel: RecyclerView
    private lateinit var rvTrending: RecyclerView
    private lateinit var progressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvCarousel = view.findViewById(R.id.rv_home_carousel)
        rvTrending = view.findViewById(R.id.rv_home_trending)
        progressBar = view.findViewById(R.id.pb_home)

        rvCarousel.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        rvTrending.layoutManager = LinearLayoutManager(requireContext())

        getRecipes()
    }

    private fun getRecipes() {
        progressBar.visibility = View.VISIBLE

        val client = ApiConfig.getApiService().searchRecipes("c")

        client.enqueue(object : Callback<RecipeResponse> {
            override fun onResponse(call: Call<RecipeResponse>, response: Response<RecipeResponse>) {
                progressBar.visibility = View.GONE

                if (response.isSuccessful) {
                    val responseBody = response.body()
                    val mealsList = responseBody?.meals

                    if (!mealsList.isNullOrEmpty()) {
                        val shuffledMeals = mealsList.shuffled()

                        val carouselItems = shuffledMeals.take(3)
                        val trendingItems = shuffledMeals.drop(3).take(10)

                        setupCarouselAdapter(carouselItems)
                        setupTrendingAdapter(trendingItems)
                    } else {
                        Toast.makeText(requireContext(), "Data resep kosong", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.e("HomeFragment", "Gagal: ${response.message()}")
                    Toast.makeText(requireContext(), "Gagal mengambil data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<RecipeResponse>, t: Throwable) {
                progressBar.visibility = View.GONE
                Log.e("HomeFragment", "Error: ${t.message}")
                Toast.makeText(requireContext(), "Tidak ada koneksi internet", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setupCarouselAdapter(items: List<Recipe>) {
        rvCarousel.adapter = object : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_home_carousel, parent, false)
                return object : RecyclerView.ViewHolder(view) {}
            }

            override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                val item = items[position]
                val imgBg = holder.itemView.findViewById<ImageView>(R.id.img_carousel_bg)
                val tvTitle = holder.itemView.findViewById<TextView>(R.id.tv_carousel_title)

                tvTitle.text = item.strMeal
                Glide.with(holder.itemView.context).load(item.strMealThumb).into(imgBg)

                holder.itemView.setOnClickListener {
                    val intent = Intent(requireContext(), RecipeDetailActivity::class.java)
                    intent.putExtra("EXTRA_ID", item.idMeal)
                    startActivity(intent)
                }
            }

            override fun getItemCount(): Int = items.size
        }
    }

    private fun setupTrendingAdapter(items: List<Recipe>) {
        rvTrending.adapter = object : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_home_trending, parent, false)
                return object : RecyclerView.ViewHolder(view) {}
            }

            override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                val item = items[position]
                val imgThumb = holder.itemView.findViewById<ImageView>(R.id.img_trending)
                val tvTitle = holder.itemView.findViewById<TextView>(R.id.tv_trending_title)
                val tvSubtitle = holder.itemView.findViewById<TextView>(R.id.tv_trending_subtitle)

                tvTitle.text = item.strMeal

                val category = item.strCategory
                val area = item.strArea
                tvSubtitle.text = "$category • $area"

                Glide.with(holder.itemView.context).load(item.strMealThumb).into(imgThumb)

                holder.itemView.setOnClickListener {
                    val intent = Intent(requireContext(), RecipeDetailActivity::class.java)
                    intent.putExtra("EXTRA_ID", item.idMeal)
                    startActivity(intent)
                }
            }

            override fun getItemCount(): Int = items.size
        }
    }
}