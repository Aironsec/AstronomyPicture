package ru.stplab.astronomypicture.mvvm.model.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.stplab.astronomypicture.BuildConfig
import ru.stplab.astronomypicture.mvvm.model.api.PictureOfTheDayAPI
import ru.stplab.astronomypicture.mvvm.model.dao.PictureDao

class PictureRepo(
    private val apiPicture: PictureOfTheDayAPI,
    private val pictureDao: PictureDao
) {
    val data = pictureDao.getPictureDay()

    suspend fun refresh() =
        withContext(Dispatchers.IO) {
            val apiKey = BuildConfig.NASA_API_KEY
            val pic = apiPicture.getPictureOfTheDayAsync(apiKey).await()
            pictureDao.addPicture(pic)
        }
}