package com.example.kotlinapp.ui.viewmodel

import android.util.Log
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
class GeneralVM @Inject constructor(private val repo: Newsrepository): ViewModel() {

    private val _generalstate = MutableStateFlow<Uistate<List<ArticleOr>>>(Uistate.Loading)

    val generalstate: StateFlow<Uistate<List<ArticleOr>>> = _generalstate.asStateFlow()

    fun getgeneral(){
        viewModelScope.launch {
            _generalstate.value= Uistate.Loading

            try {
                val response=repo.get("general")
                if(response.isSuccessful&&response.body()!=null){
                    _generalstate.value= Uistate.Success(response.body()!!.articles)
                }
                else{

                    _generalstate.value= Uistate.Error("Error: ${response.message()}")

                }
            }

            catch (e: Exception){

                _generalstate.value= Uistate.Error("Error: ${e.message}")
                Log.e("generalVM", "Failed to fetch general news", e)

            }

        }

    }

    fun refresh(){
        getgeneral()
    }
}