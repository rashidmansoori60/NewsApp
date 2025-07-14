package com.example.kotlinapp.ui.viewmodel

import android.util.Log
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
import kotlin.Exception

@HiltViewModel
class SearchViewModel @Inject constructor(private val repostry: Newsrepository): ViewModel() {

    private val _searchstate = MutableStateFlow<Uistate<List<ArticleOr>>>(Uistate.Loading)
    val searchstate : StateFlow<Uistate<List<ArticleOr>>> = _searchstate.asStateFlow()


    private val _isSearching = MutableStateFlow(false)
    val isSearching: StateFlow<Boolean> = _isSearching.asStateFlow()


    fun getsearch(q: String){
        viewModelScope.launch {
            _searchstate.value= Uistate.Loading

            try {
                val response=repostry.getsearchfromapi(q)
                if(response.isSuccessful&&response.body()!=null){

                    _isSearching.value =true


                    _searchstate.value= Uistate.Success(response.body()!!.articles)
                }
                else{
                    _searchstate.value= Uistate.Error("Error: ${response.message()}")
                    _isSearching.value=false
                }
            }
            catch(e: Exception) {
                _searchstate.value= Uistate.Error("Exception: ${e.message}")
                _isSearching.value=false
            }


        }
    }
    fun clearSearch() {
        _searchstate.value= Uistate.Success(emptyList())
        _isSearching.value = false

    }
}