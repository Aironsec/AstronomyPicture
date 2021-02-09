package ru.stplab.astronomypicture.di

import android.app.Application
import androidx.room.Room
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.stplab.astronomypicture.mvvm.model.AppDatabase
import ru.stplab.astronomypicture.mvvm.model.AppDatabase.Companion.NAME_DB
import ru.stplab.astronomypicture.mvvm.model.api.PictureOfTheDayAPI
import ru.stplab.astronomypicture.mvvm.model.dao.PictureDao
import ru.stplab.astronomypicture.mvvm.model.repository.PictureRepo
import ru.stplab.astronomypicture.mvvm.viewmodal.PictureOfTheDayViewModel
import java.util.concurrent.TimeUnit

val viewModelModule = module {
    single { PictureOfTheDayViewModel(get()) }
}

val apiModule = module {
    fun providePictureOfTheDayAPI(retrofit: Retrofit) =
        retrofit.create(PictureOfTheDayAPI::class.java)
    single { providePictureOfTheDayAPI(get()) }
}

val netModule = module {

    val baseUrl = "https://api.nasa.gov/"

    fun provideOkHttpClient() = OkHttpClient.Builder()
        .callTimeout(30, TimeUnit.SECONDS)
        .addNetworkInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()

    fun provideGson() = GsonBuilder()
        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        .excludeFieldsWithoutExposeAnnotation()
        .create()

    fun provideRetrofit(factory: Gson, client: OkHttpClient) = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create(factory))
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .client(client)
        .build()

    single { provideOkHttpClient() }
    single { provideGson() }
    single { provideRetrofit(get(), get()) }
}

val databaseModule = module {
    fun provideDatabase(application: Application) =
        Room.databaseBuilder(application, AppDatabase::class.java, NAME_DB)
            .fallbackToDestructiveMigration()
            .build()

    fun provideDao(database: AppDatabase) = database.pictureDao

    single { provideDatabase(androidApplication()) }
    single { provideDao(get()) }
}

val repositoryModule = module {
    fun  providePictureRepo(api: PictureOfTheDayAPI, dao: PictureDao) = PictureRepo (api, dao)

    single { providePictureRepo(get(), get()) }
}