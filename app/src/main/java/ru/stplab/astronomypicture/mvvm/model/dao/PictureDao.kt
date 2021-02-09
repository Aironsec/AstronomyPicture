package ru.stplab.astronomypicture.mvvm.model.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.stplab.astronomypicture.mvvm.model.entity.PicturesOfTheDay

@Dao
interface PictureDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addPicture(picture: PicturesOfTheDay)

    @Query("SELECT * FROM pictures ORDER BY date DESC LIMIT 1")
    fun getPictureDay(): LiveData<PicturesOfTheDay?>
}