package com.umbrella.nasaapiapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umbrella.nasaapiapp.BuildConfig
import com.umbrella.nasaapiapp.model.AppState
import com.umbrella.nasaapiapp.model.api.ApiFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PictureViewModel : ViewModel() {
    private val pictureLiveData = MutableLiveData<AppState>()
    fun getPictureLiveData(): LiveData<AppState> = pictureLiveData

    fun makeApiCall() {
        viewModelScope.launch(Dispatchers.IO) {
            pictureLiveData.postValue(AppState.Loading)
            try {
                val response = ApiFactory.retrofitService.getPicture(BuildConfig.NASA_API_KEY)
                pictureLiveData.postValue(AppState.Success(response))
            } catch (e: Exception) {
                pictureLiveData.postValue(AppState.Error(e))
            }
        }
    }
}