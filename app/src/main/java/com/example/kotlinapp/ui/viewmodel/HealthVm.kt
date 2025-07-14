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
class HealthVm @Inject constructor(private val repo: Newsrepository): ViewModel() {

    private val _healthstate = MutableStateFlow<Uistate<List<ArticleOr>>>(Uistate.Loading)

    val healthstate: StateFlow<Uistate<List<ArticleOr>>> = _healthstate.asStateFlow()

    fun gethealth(){
        viewModelScope.launch {
            _healthstate.value= Uistate.Loading

            try {
                val response=repo.get("health")
                if(response.isSuccessful&&response.body()!=null){
                    _healthstate.value= Uistate.Success(response.body()!!.articles)
                }
                else{

                    _healthstate.value= Uistate.Error("Error: ${response.message()}")

                }
            }

            catch (e: Exception){

                _healthstate.value= Uistate.Error("Error: ${e.message}")
                Log.e("SportVM", "Failed to fetch sport news", e)

            }

        }

    }

    fun refresh(){
        gethealth()
    }
}