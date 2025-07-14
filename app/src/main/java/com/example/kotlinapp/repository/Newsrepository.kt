package com.example.kotlinapp.repository

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.kotlinapp.network.api.Newsapi
import com.example.kotlinapp.utils.Constance.Companion.API_KEY
import retrofit2.Response
import javax.inject.Inject

class Newsrepository @Inject constructor(var api: Newsapi){


    suspend fun getheadlinefromapi(page: Int):Response<com.example.kotlinapp.model.Response> {

        return api.getheadline(page = page)

    }


    suspend fun getsearchfromapi(q: String):Response<com.example.kotlinapp.model.Response>{
        return api.searchnews(q)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getbusinessfromapi():Response<com.example.kotlinapp.model.Response>{
        return api.getbusiness()
        Log.d("NewsRepository", "Raw response: ${api.getbusiness()}")
    }


    suspend fun get(q: String):Response<com.example.kotlinapp.model.Response>{
      return api.get(q)
    }

}