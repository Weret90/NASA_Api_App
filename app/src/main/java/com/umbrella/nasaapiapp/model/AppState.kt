package com.umbrella.nasaapiapp.model

sealed class AppState {
    object Loading : AppState()
    data class Success(val response: PictureDTO) : AppState()
    data class Error(val error: Throwable) : AppState()
}