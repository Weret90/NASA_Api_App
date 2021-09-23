package com.umbrella.nasaapiapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umbrella.nasaapiapp.BuildConfig
import com.umbrella.nasaapiapp.model.MeteorsLoadState
import com.umbrella.nasaapiapp.model.api.ApiFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MeteorsViewModel : ViewModel() {

    private val meteorsLiveDate = MutableLiveData<MeteorsLoadState>()
    fun getMeteorsLiveData(): LiveData<MeteorsLoadState> = meteorsLiveDate

    fun makeApiCall() {
        viewModelScope.launch(Dispatchers.IO) {
            meteorsLiveDate.postValue(MeteorsLoadState.Loading)
            try {
                val response =
                    ApiFactory.retrofitService.getMeteorsNearEarth(BuildConfig.NASA_API_KEY)
                meteorsLiveDate.postValue(MeteorsLoadState.Success(response))
            } catch (e: Exception) {
                meteorsLiveDate.postValue(MeteorsLoadState.Error(e))
            }
        }
    }
}