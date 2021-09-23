package com.umbrella.nasaapiapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umbrella.nasaapiapp.BuildConfig
import com.umbrella.nasaapiapp.model.PhotosFromMarsLoadState
import com.umbrella.nasaapiapp.model.api.ApiFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PhotosFromMarsViewModel : ViewModel() {

    private val photosLiveData = MutableLiveData<PhotosFromMarsLoadState>()
    fun getPhotosLiveData(): LiveData<PhotosFromMarsLoadState> = photosLiveData

    fun makeApiCall() {
        viewModelScope.launch(Dispatchers.IO) {
            photosLiveData.postValue(PhotosFromMarsLoadState.Loading)
            try {
                val response =
                    ApiFactory.retrofitService.getPhotosFromMars(BuildConfig.NASA_API_KEY)
                photosLiveData.postValue(PhotosFromMarsLoadState.Success(response))
            } catch (e: Exception) {
                photosLiveData.postValue(PhotosFromMarsLoadState.Error(e))
            }
        }
    }
}