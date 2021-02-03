package ru.stplab.astronomypicture.mvvm.model.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import ru.stplab.astronomypicture.mvvm.model.entity.PODServerResponseData
import ru.stplab.astronomypicture.mvvm.model.entity.PODServerResponseDataList

interface PictureOfTheDayAPI {

    @GET("planetary/apod")
    fun getPictureOfTheDay(
        @Query("api_key") apiKey: String
    ): Call<PODServerResponseData>
}
