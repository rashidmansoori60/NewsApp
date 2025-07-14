package com.example.kotlinapp.ui.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlinapp.State.Uistate
import com.example.kotlinapp.model.ArticleOr
import com.example.kotlinapp.repository.Newsrepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    application: Application,
    private val repository: Newsrepository
) :ViewModel() {

    // LiveData to expose articles to UI
    private val _newastate = MutableStateFlow<Uistate<List<ArticleOr>>>(Uistate.Loading)
     val newsstate : StateFlow<Uistate<List<ArticleOr>>> = _newastate.asStateFlow()


    fun getNews() {

            viewModelScope.launch {
                _newastate.value = Uistate.Loading

                val articleList = mutableListOf<ArticleOr>()

            articleList.clear()
            try {
                for(page in 1..3){
                val response = repository.getheadlinefromapi(page=page)
                if (response.isSuccessful && response.body() != null) {
                    var art= response.body()?.articles?:emptyList()
                    articleList.addAll(art)

                } else {
                   _newastate.value= Uistate.Error("Error: ${response.message()}")
                }
            }
                _newastate.value= Uistate.Success(articleList)

            }catch (e: Exception) {
                _newastate.value = Uistate.Error("Exception: ${e.message}")
            }

        }
    }

    fun refresh() {
        getNews()  // Simply call fetch again
    }
}
