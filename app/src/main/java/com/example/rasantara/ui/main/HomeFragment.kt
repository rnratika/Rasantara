package com.example.rasantara.ui.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rasantara.R
import com.example.rasantara.data.model.RecipeResponse
import com.example.rasantara.data.remote.ApiConfig
import com.example.rasantara.ui.adapter.RecipeAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {

    private lateinit var rvRecipes: RecyclerView
    private lateinit var progressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvRecipes = view.findViewById(R.id.rv_recipes)
        progressBar = view.findViewById(R.id.progress_bar)

        rvRecipes.layoutManager = LinearLayoutManager(requireContext())

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
                    if (responseBody != null && responseBody.meals != null) {
                        val adapter = RecipeAdapter(responseBody.meals)
                        rvRecipes.adapter = adapter
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
}