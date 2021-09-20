package com.umbrella.nasaapiapp.model

sealed class PictureLoadState {
    object Loading : PictureLoadState()
    data class Success(val response: PictureDTO) : PictureLoadState()
    data class Error(val error: Throwable) : PictureLoadState()
}

sealed class PhotosFromMarsLoadState {
    object Loading : PhotosFromMarsLoadState()
    data class Success(val response: PhotosFromMarsDTO) : PhotosFromMarsLoadState()
    data class Error(val error: Throwable) : PhotosFromMarsLoadState()
}

sealed class MeteorsLoadState {
    object Loading : MeteorsLoadState()
    data class Success(val response: MeteorsDTO) : MeteorsLoadState()
    data class Error(val error: Throwable) : MeteorsLoadState()
}

