package com.example.kotlinapp.di

import android.app.Application
import android.content.Context
import com.example.kotlinapp.network.api.Newsapi
import com.example.kotlinapp.repository.Newsrepository
import com.example.kotlinapp.utils.Constance.Companion.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
      fun getloggin(): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        return logging

    }
    @Provides
    @Singleton
    fun getclient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient{
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun getretrofir(client: OkHttpClient): Retrofit {
      return  Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create())
            .client(client).build()
    }

    @Provides
    @Singleton
    fun getapi(retrofit: Retrofit): Newsapi{
       return retrofit.create(Newsapi::class.java)
}
    @Provides
    @Singleton
    fun getRepository(api: Newsapi): Newsrepository{
        return Newsrepository(api)
    }

}