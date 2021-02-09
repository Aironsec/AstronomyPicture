package ru.stplab.astronomypicture.mvvm.model.api

import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query
import ru.stplab.astronomypicture.mvvm.model.entity.PicturesOfTheDay

interface PictureOfTheDayAPI {

    @GET("planetary/apod")
    fun getPictureOfTheDayAsync(
        @Query("api_key") apiKey: String
    ): Deferred<PicturesOfTheDay>
}
