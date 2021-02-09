package ru.stplab.astronomypicture.mvvm.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import org.jetbrains.annotations.NotNull

@Entity(tableName = "pictures")
data class PicturesOfTheDay(
    @PrimaryKey
    @Expose val date: String,
    @Expose val explanation: String?,
    @Expose val mediaType: String?,
    @Expose val title: String?,
    @Expose val url: String?
)
