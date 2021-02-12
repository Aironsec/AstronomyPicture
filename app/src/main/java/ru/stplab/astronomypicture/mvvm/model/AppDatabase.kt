package ru.stplab.astronomypicture.mvvm.model

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.stplab.astronomypicture.mvvm.model.dao.PictureDao
import ru.stplab.astronomypicture.mvvm.model.entity.PicturesOfTheDay

@Database(entities = [PicturesOfTheDay::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase(){
    abstract val pictureDao : PictureDao

    companion object{
        const val NAME_DB = "astronomy_picture.db"
    }
}