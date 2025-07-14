package com.example.kotlinapp.network.api

import android.os.Build
import android.util.Size
import androidx.annotation.RequiresApi
import com.example.kotlinapp.utils.Constance.Companion.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import java.time.LocalDate
import java.util.Locale

interface Newsapi {
    @GET("everything")
    suspend fun getheadline(
        @Query("sortBy")
        sortBy: String="publishedAt",
        @Query("pageSize")
        pageSize: Int=50,
        @Query("page") page: Int = 1,
        @Query("apikey")
        apikey: String=API_KEY,
        @Query("q")
        q: String="india"
    ): Response<com.example.kotlinapp.model.Response>
    @GET("everything")
    suspend fun searchnews(
        @Query("q")
        searchQuery: String,
        @Query("sortby")
        sortBy: String="publishedAt",
        @Query("apikey")
        apiKey:String=API_KEY

    ): Response<com.example.kotlinapp.model.Response>


    @RequiresApi(Build.VERSION_CODES.O)
    @GET("everything")
    suspend fun getbusiness(
        @Query("apikey")
        apikey: String=API_KEY,
        @Query("q")
        q: String="india business",
        @Query("sortBy")
        sortBy: String="publishedAt",




    ): Response<com.example.kotlinapp.model.Response>



    @GET("everything")
    suspend fun get(
        @Query("q")
        searchQuery: String,
        @Query("apikey")
        apikey: String=API_KEY,
        @Query("sortBy")
        sortBy: String="publishedAt"
        ): Response<com.example.kotlinapp.model.Response>
}