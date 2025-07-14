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
class ScienceVM @Inject constructor(private val repo: Newsrepository): ViewModel() {

    private val _sciencestate = MutableStateFlow<Uistate<List<ArticleOr>>>(Uistate.Loading)

    val sciencestate: StateFlow<Uistate<List<ArticleOr>>> = _sciencestate.asStateFlow()

    fun getscience(){
        viewModelScope.launch {
            _sciencestate.value= Uistate.Loading

            try {
                val response=repo.get("science")
                if(response.isSuccessful&&response.body()!=null){
                    _sciencestate.value= Uistate.Success(response.body()!!.articles)
                }
                else{

                    _sciencestate.value= Uistate.Error("Error: ${response.message()}")

                }
            }

            catch (e: Exception){

                _sciencestate.value= Uistate.Error("Error: ${e.message}")
                Log.e("SportVM", "Failed to fetch sport news", e)

            }

        }

    }

    fun refresh(){
        getscience()
    }
}