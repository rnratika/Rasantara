package com.example.rasantara.ui.explore

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.ScrollView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.SearchView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.rasantara.R
import com.example.rasantara.data.model.RecipeResponse
import com.example.rasantara.data.remote.ApiConfig
import com.example.rasantara.ui.adapter.RecipeAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ExploreFragment : Fragment(R.layout.fragment_explore) {

    private lateinit var adapter: RecipeAdapter
    private lateinit var rvExplore: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var tvNotFound: TextView
    private lateinit var layoutCategories: ScrollView

    private lateinit var layoutExploreError: LinearLayout
    private lateinit var btnRefresh: Button

    private var lastSearchQuery: String? = null
    private var lastFilterArea: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvExplore = view.findViewById(R.id.rv_explore)
        progressBar = view.findViewById(R.id.pb_explore)
        tvNotFound = view.findViewById(R.id.tv_not_found)
        layoutCategories = view.findViewById(R.id.layout_categories)
        val searchView = view.findViewById<SearchView>(R.id.search_view)

        layoutExploreError = view.findViewById(R.id.layout_explore_error)
        btnRefresh = view.findViewById(R.id.btn_explore_refresh)

        setupRecyclerView()
        setupCategoryCards(view)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty()) {
                    searchRecipeByName(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) {
                    showCategories()
                }
                return false
            }
        })

        btnRefresh.setOnClickListener {
            lastSearchQuery?.let { searchRecipeByName(it) }
            lastFilterArea?.let { filterRecipeByArea(it) }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (layoutCategories.visibility == View.GONE) {
                    showCategories()
                } else {
                    isEnabled = false
                    requireActivity().onBackPressedDispatcher.onBackPressed()
                }
            }
        })
    }

    private fun setupRecyclerView() {
        adapter = RecipeAdapter()
        rvExplore.layoutManager = LinearLayoutManager(requireContext())
        rvExplore.adapter = adapter
    }

    private fun setupCategoryCards(view: View) {
        view.findViewById<CardView>(R.id.card_british).setOnClickListener { filterRecipeByArea("British") }
        view.findViewById<CardView>(R.id.card_italy).setOnClickListener { filterRecipeByArea("Italian") }
        view.findViewById<CardView>(R.id.card_japan).setOnClickListener { filterRecipeByArea("Japanese") }
        view.findViewById<CardView>(R.id.card_mexico).setOnClickListener { filterRecipeByArea("Mexican") }
        view.findViewById<CardView>(R.id.card_canadian).setOnClickListener { filterRecipeByArea("Canadian") }
        view.findViewById<CardView>(R.id.card_chinese).setOnClickListener { filterRecipeByArea("Chinese") }
        view.findViewById<CardView>(R.id.card_spanish).setOnClickListener { filterRecipeByArea("Spanish") }
        view.findViewById<CardView>(R.id.card_thai).setOnClickListener { filterRecipeByArea("Thai") }
        view.findViewById<CardView>(R.id.card_turkish).setOnClickListener { filterRecipeByArea("Turkish") }
        view.findViewById<CardView>(R.id.card_greek).setOnClickListener { filterRecipeByArea("Greek") }

        Glide.with(this).load("https://www.themealdb.com/images/media/meals/1550441882.jpg").into(view.findViewById(R.id.img_british))
        Glide.with(this).load("https://images.unsplash.com/photo-1574071318508-1cdbab80d002?w=500&q=80").into(view.findViewById(R.id.img_italy))
        Glide.with(this).load("https://images.unsplash.com/photo-1579871494447-9811cf80d66c?w=500&q=80").into(view.findViewById(R.id.img_japan))
        Glide.with(this).load("https://images.unsplash.com/photo-1565299585323-38d6b0865b47?w=500&q=80").into(view.findViewById(R.id.img_mexico))
        Glide.with(this).load("https://images.unsplash.com/photo-1554520735-0a6b8b6ce8b7?w=500&q=80").into(view.findViewById(R.id.img_canadian))
        Glide.with(this).load("https://images.unsplash.com/photo-1563245372-f21724e3856d?w=500&q=80").into(view.findViewById(R.id.img_chinese))
        Glide.with(this).load("https://images.unsplash.com/photo-1534080564583-6be75777b70a?w=500&q=80").into(view.findViewById(R.id.img_spanish))
        Glide.with(this).load("https://images.unsplash.com/photo-1559314809-0d155014e29e?w=500&q=80").into(view.findViewById(R.id.img_thai))
        Glide.with(this).load("https://images.unsplash.com/photo-1561651823-34feb02250e4?w=500&q=80").into(view.findViewById(R.id.img_turkish))
        Glide.with(this).load("https://images.unsplash.com/photo-1529312266912-b33cfce2eefd?w=500&q=80").into(view.findViewById(R.id.img_greek))
    }

    private fun showCategories() {
        layoutCategories.visibility = View.VISIBLE
        rvExplore.visibility = View.GONE
        tvNotFound.visibility = View.GONE
        layoutExploreError.visibility = View.GONE

        adapter = RecipeAdapter()
        rvExplore.adapter = adapter
    }

    private fun showErrorState() {
        layoutCategories.visibility = View.GONE
        rvExplore.visibility = View.GONE
        tvNotFound.visibility = View.GONE
        progressBar.visibility = View.GONE
        layoutExploreError.visibility = View.VISIBLE
    }

    private fun searchRecipeByName(query: String) {
        lastSearchQuery = query
        lastFilterArea = null

        layoutCategories.visibility = View.GONE
        rvExplore.visibility = View.GONE
        tvNotFound.visibility = View.GONE
        layoutExploreError.visibility = View.GONE
        progressBar.visibility = View.VISIBLE

        ApiConfig.getApiService().searchRecipes(query).enqueue(object : Callback<RecipeResponse> {
            override fun onResponse(call: Call<RecipeResponse>, response: Response<RecipeResponse>) {
                handleApiResponse(response)
            }
            override fun onFailure(call: Call<RecipeResponse>, t: Throwable) {
                showErrorState()
            }
        })
    }

    private fun filterRecipeByArea(area: String) {
        lastFilterArea = area
        lastSearchQuery = null

        layoutCategories.visibility = View.GONE
        rvExplore.visibility = View.GONE
        tvNotFound.visibility = View.GONE
        layoutExploreError.visibility = View.GONE
        progressBar.visibility = View.VISIBLE

        ApiConfig.getApiService().filterByArea(area).enqueue(object : Callback<RecipeResponse> {
            override fun onResponse(call: Call<RecipeResponse>, response: Response<RecipeResponse>) {
                handleApiResponse(response)
            }
            override fun onFailure(call: Call<RecipeResponse>, t: Throwable) {
                showErrorState()
            }
        })
    }

    private fun handleApiResponse(response: Response<RecipeResponse>) {
        progressBar.visibility = View.GONE
        layoutExploreError.visibility = View.GONE

        if (response.isSuccessful) {
            val list = response.body()?.meals
            if (list != null) {
                rvExplore.visibility = View.VISIBLE
                adapter.setData(list)
                tvNotFound.visibility = View.GONE
            } else {
                rvExplore.visibility = View.GONE
                adapter.setData(emptyList())
                tvNotFound.visibility = View.VISIBLE
            }
        } else {
            showErrorState()
        }
    }
}