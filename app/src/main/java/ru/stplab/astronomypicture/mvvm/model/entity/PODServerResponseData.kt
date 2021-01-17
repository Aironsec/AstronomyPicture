package ru.stplab.astronomypicture.mvvm.model.entity

import com.google.gson.annotations.Expose

data class PODServerResponseData(

    @Expose val date: String?,
    @Expose val explanation: String?,
    @Expose val mediaType: String?,
    @Expose val title: String?,
    @Expose val url: String?
)
