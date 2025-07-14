package com.example.kotlinapp.adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kotlinapp.adapter.BusinessAdapter.Companion.formatDate
import com.example.kotlinapp.databinding.BusinessitemBinding
import com.example.kotlinapp.model.ArticleOr
import javax.inject.Inject


class HealthAdapter @Inject constructor(private var articles: List<ArticleOr>, var context: Context): RecyclerView.Adapter<HealthAdapter.HealthViewholder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HealthViewholder {
        val binding = BusinessitemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HealthViewholder(binding)
    }

    fun updateartical(Newarticle: List<ArticleOr>?){

        if (Newarticle != null) {
            articles=Newarticle
        }
        notifyDataSetChanged()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: HealthViewholder, position: Int) {
        var articl=articles[position]
        holder.binding.textViewSource.text=articl.source.name
        holder.binding.textViewTitle.text=articl.title
        holder.binding.textViewDate.text=formatDate(articl.publishedAt)
        holder.binding.textViewDescription.text=articl.description

        Glide.with(context)
            .load(articl.urlToImage)
            .into(holder.binding.imageViewNews)




    }

    override fun getItemCount(): Int {
        return articles.size
    }

    class HealthViewholder(val binding: BusinessitemBinding) : RecyclerView.ViewHolder(binding.root)





}