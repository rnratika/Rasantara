package com.example.rasantara.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.rasantara.R
import com.example.rasantara.data.model.Recipe

class RecipeAdapter(private val listRecipe: List<Recipe>) : RecyclerView.Adapter<RecipeAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgRecipe: ImageView = view.findViewById(R.id.img_recipe)
        val tvName: TextView = view.findViewById(R.id.tv_recipe_name)
        val tvCategory: TextView = view.findViewById(R.id.tv_recipe_category)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recipe, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val recipe = listRecipe[position]
        holder.tvName.text = recipe.strMeal
        holder.tvCategory.text = "${recipe.strCategory} • ${recipe.strArea}"

        // Glide untuk memuat gambar dari URL internet ke ImageView
        Glide.with(holder.itemView.context)
            .load(recipe.strMealThumb)
            .into(holder.imgRecipe)
    }

    override fun getItemCount(): Int = listRecipe.size
}