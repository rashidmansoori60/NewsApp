package com.example.kotlinapp.adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kotlinapp.databinding.BusinessitemBinding
import com.example.kotlinapp.model.ArticleOr
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject


class BusinessAdapter @Inject constructor(private var articles: List<ArticleOr>, var context: Context): RecyclerView.Adapter<BusinessAdapter.BusinessViewholder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BusinessViewholder {
        val binding = BusinessitemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
       return BusinessViewholder(binding)
    }

    fun updateartical(Newarticle: List<ArticleOr>?){

        if (Newarticle != null) {
            articles=Newarticle
        }
        notifyDataSetChanged()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: BusinessViewholder, position: Int) {
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

    class BusinessViewholder(val binding: BusinessitemBinding) : RecyclerView.ViewHolder(binding.root)




    companion object{
    @RequiresApi(Build.VERSION_CODES.O)
    fun formatDate(isoDate: String): String {
        return try {
            val inputFormatter = DateTimeFormatter.ISO_ZONED_DATE_TIME
            val outputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
            val zonedDateTime = ZonedDateTime.parse(isoDate, inputFormatter)
            zonedDateTime.format(outputFormatter)
        } catch (e: Exception) {
            isoDate  // fallback if format fails
        }
    }
}
}