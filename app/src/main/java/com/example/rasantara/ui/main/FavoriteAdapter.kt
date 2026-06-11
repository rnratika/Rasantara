package com.example.rasantara.ui.main

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.rasantara.R
import com.example.rasantara.RecipeDetailActivity
import com.example.rasantara.data.local.FavoriteRecipe

class FavoriteAdapter(private var listFavorite: List<FavoriteRecipe>) :
    RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {

    fun setData(newList: List<FavoriteRecipe>) {
        listFavorite = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_home_trending, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listFavorite[position]

        holder.tvTitle.text = item.strMeal
        holder.tvSubtitle.text = "${item.strCategory} • ${item.strArea}"

        Glide.with(holder.itemView.context)
            .load(item.strMealThumb)
            .into(holder.imgThumb)

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, RecipeDetailActivity::class.java)
            intent.putExtra("EXTRA_ID", item.idMeal)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = listFavorite.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgThumb: ImageView = itemView.findViewById(R.id.img_trending)
        val tvTitle: TextView = itemView.findViewById(R.id.tv_trending_title)
        val tvSubtitle: TextView = itemView.findViewById(R.id.tv_trending_subtitle)
    }
}