package com.example.rasantara.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rasantara.R
import com.example.rasantara.data.local.RecipeDatabase
import com.example.rasantara.ui.main.FavoriteAdapter
import java.util.concurrent.ExecutorService

class FavoriteFragment : Fragment() {

    private lateinit var rvFavorite: RecyclerView
    private lateinit var tvEmpty: TextView
    private lateinit var adapter: FavoriteAdapter
    private lateinit var executorService: ExecutorService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_favorite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvFavorite = view.findViewById(R.id.rv_favorite)
        tvEmpty = view.findViewById(R.id.tv_favorite_empty)

        executorService = RecipeDatabase.databaseWriteExecutor

        rvFavorite.layoutManager = LinearLayoutManager(requireContext())
        adapter = FavoriteAdapter(listOf())
        rvFavorite.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        loadFavoriteRecipes()
    }

    private fun loadFavoriteRecipes() {
        executorService.execute {
            val favoriteDao = RecipeDatabase.getDatabase(requireContext()).favoriteDao()
            val listFavorites = favoriteDao.getAllFavorites()

            activity?.runOnUiThread {
                if (listFavorites.isEmpty()) {
                    tvEmpty.visibility = View.VISIBLE
                    rvFavorite.visibility = View.GONE
                } else {
                    tvEmpty.visibility = View.GONE
                    rvFavorite.visibility = View.VISIBLE
                    adapter.setData(listFavorites)
                }
            }
        }
    }
}