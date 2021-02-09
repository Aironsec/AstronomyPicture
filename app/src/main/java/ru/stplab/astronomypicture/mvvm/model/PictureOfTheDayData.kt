package ru.stplab.astronomypicture.mvvm.model

import ru.stplab.astronomypicture.mvvm.model.entity.PicturesOfTheDay

sealed class PictureOfTheDayData {
    data class Success(val serverResponseData: PicturesOfTheDay) : PictureOfTheDayData()
    data class Error(val error: Throwable) : PictureOfTheDayData()
    data class Loading(val progress: Int?) : PictureOfTheDayData()
}
