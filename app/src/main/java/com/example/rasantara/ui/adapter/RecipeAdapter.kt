package com.example.rasantara.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.rasantara.R
import com.example.rasantara.RecipeDetailActivity // Pastikan import ini sesuai dengan lokasi Activity Anda
import com.example.rasantara.data.model.Recipe

class RecipeAdapter : RecyclerView.Adapter<RecipeAdapter.ViewHolder>() {

    // 1. Ubah menjadi ArrayList agar bisa dikosongkan dan diisi ulang oleh Retrofit
    private val listRecipe = ArrayList<Recipe>()

    // 2. Fungsi baru untuk menerima data dari API dan me-refresh RecyclerView
    fun setData(items: List<Recipe>?) {
        listRecipe.clear()
        if (items != null) {
            listRecipe.addAll(items)
        }
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgRecipe: ImageView = view.findViewById(R.id.imgRecipe)
        val tvName: TextView = view.findViewById(R.id.tvName)
        val tvCategory: TextView = view.findViewById(R.id.tvCategory)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recipe, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val recipe = listRecipe[position]

        holder.tvName.text = recipe.strMeal
        holder.tvCategory.text = "${recipe.strCategory} • ${recipe.strArea}"

        Glide.with(holder.itemView.context)
            .load(recipe.strMealThumb)
            .into(holder.imgRecipe)

        // Poin 2: Implementasi Intent untuk berpindah antar Activity sudah benar
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, RecipeDetailActivity::class.java)
            // Mengirim ID untuk nanti di-fetch detailnya di RecipeDetailActivity
            intent.putExtra("EXTRA_ID", recipe.idMeal)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = listRecipe.size
}