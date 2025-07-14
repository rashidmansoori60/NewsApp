package com.example.kotlinapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kotlinapp.databinding.ItemBinding
import com.example.kotlinapp.model.ArticleOr
import javax.inject.Inject

class HeadlineAdapter @Inject constructor(private var articles: List<ArticleOr>, var context: Context): RecyclerView.Adapter<HeadlineAdapter.HeadlineViewholder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeadlineViewholder {
        val binding = ItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HeadlineViewholder(binding)
    }

    fun updateartical(Newarticle: List<ArticleOr>?){

        if (Newarticle != null) {
            articles=Newarticle
        }
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: HeadlineViewholder,position: Int) {
           var articl=articles[position]
        holder.binding.textViewTitle.text=articl.title
        holder.binding.textViewDate.text=articl.publishedAt
        holder.binding.textViewDescription.text=articl.description

        Glide.with(context)
            .load(articl.urlToImage)
            .into(holder.binding.imageViewNews)




    }

    override fun getItemCount(): Int {
    return articles.size
    }

    class HeadlineViewholder(val binding: ItemBinding) : RecyclerView.ViewHolder(binding.root)

   

}