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
class SportVM @Inject constructor(private val repo: Newsrepository): ViewModel() {

    private val _sportstate = MutableStateFlow<Uistate<List<ArticleOr>>>(Uistate.Loading)

    val sportstate: StateFlow<Uistate<List<ArticleOr>>> = _sportstate.asStateFlow()

    fun getsport(){
     viewModelScope.launch {
         _sportstate.value= Uistate.Loading

         try {
             val response=repo.get("sport")
             if(response.isSuccessful&&response.body()!=null){
                 _sportstate.value= Uistate.Success(response.body()!!.articles)
             }
             else{

                 _sportstate.value= Uistate.Error("Error: ${response.message()}")

             }
         }

         catch (e: Exception){

                _sportstate.value= Uistate.Error("Error: ${e.message}")
             Log.e("SportVM", "Failed to fetch sport news", e)

         }

     }

    }

    fun refresh(){
        getsport()
    }
}