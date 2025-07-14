package com.example.kotlinapp.ui.viewmodel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
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
class Businessviewmodel @Inject constructor( private val repository: Newsrepository): ViewModel() {

    private val _businessuistate=MutableStateFlow<Uistate<List<ArticleOr>>>(Uistate.Loading);

    val businessuistate: StateFlow<Uistate<List<ArticleOr>>> = _businessuistate.asStateFlow()

    @RequiresApi(Build.VERSION_CODES.O)
    fun getbusiness(){
        viewModelScope.launch {

            _businessuistate.value = Uistate.Loading



        try {
            val respons=repository.getbusinessfromapi()
            Log.d("BusinessViewModel", "API returned: ${respons.body()?.totalResults} articles")
            if(respons.isSuccessful&&respons.body()!=null){

                _businessuistate.value= Uistate.Success(respons.body()!!.articles)



            }
            else {


                _businessuistate.value = Uistate.Error("Error: ${respons.message()}")
            }

        }
        catch (e: Exception) {


            _businessuistate.value = Uistate.Error("Error: ${e.message}")


            Log.e("BusinessViewModel", "Failed to fetch business news", e)
        }


        }
    }


@RequiresApi(Build.VERSION_CODES.O)
fun refresh(){
    getbusiness()
}
}