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
class EntertainmentVM @Inject constructor(private val repo: Newsrepository): ViewModel() {

    private val _entertainmentstate = MutableStateFlow<Uistate<List<ArticleOr>>>(Uistate.Loading)

    val entertainmentstate: StateFlow<Uistate<List<ArticleOr>>> = _entertainmentstate.asStateFlow()

    fun getEntertainment(){
        viewModelScope.launch {
            _entertainmentstate.value= Uistate.Loading

            try {
                val response=repo.get("Entertainment India")
                if(response.isSuccessful&&response.body()!=null){
                    _entertainmentstate.value= Uistate.Success(response.body()!!.articles)
                }
                else{

                    _entertainmentstate.value= Uistate.Error("Error: ${response.message()}")

                }
            }

            catch (e: Exception){

                _entertainmentstate.value= Uistate.Error("Error: ${e.message}")
                Log.e("EnterVm", "Failed to fetch sport news", e)

            }

        }

    }

    fun refresh(){
        getEntertainment()
    }
}