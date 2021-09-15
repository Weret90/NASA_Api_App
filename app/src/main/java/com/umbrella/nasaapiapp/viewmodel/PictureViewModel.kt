package com.umbrella.nasaapiapp.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umbrella.nasaapiapp.BuildConfig
import com.umbrella.nasaapiapp.model.AppState
import com.umbrella.nasaapiapp.model.Day
import com.umbrella.nasaapiapp.model.api.ApiFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId

private const val ZONE_ID = "America/Los_Angeles"

class PictureViewModel : ViewModel() {
    private val pictureLiveData = MutableLiveData<AppState>()
    fun getPictureLiveData(): LiveData<AppState> = pictureLiveData

    @RequiresApi(Build.VERSION_CODES.O)
    fun makeApiCall(day: Day) {
        viewModelScope.launch(Dispatchers.IO) {
            pictureLiveData.postValue(AppState.Loading)
            try {
                val response =
                    ApiFactory.retrofitService.getPicture(BuildConfig.NASA_API_KEY, getDate(day))
                pictureLiveData.postValue(AppState.Success(response))
            } catch (e: Exception) {
                pictureLiveData.postValue(AppState.Error(e))
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getDate(day: Day): String {
        val todayInUSA = LocalDate.now(ZoneId.of(ZONE_ID))
        return when (day) {
            Day.TODAY -> todayInUSA.toString()
            Day.YESTERDAY -> todayInUSA.minusDays(1).toString()
            Day.BEFORE_YESTERDAY -> todayInUSA.minusDays(2).toString()
        }
    }

    fun clearPictureLiveData() {
        pictureLiveData.value = null
    }
}