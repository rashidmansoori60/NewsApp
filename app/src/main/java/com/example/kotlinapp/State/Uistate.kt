package com.example.kotlinapp.State



sealed interface Uistate<out T>{
   data object Loading: Uistate<Nothing>

    data class Success<T>(var data:T): Uistate<T>

    data class  Error(var message: String): Uistate<Nothing>

}