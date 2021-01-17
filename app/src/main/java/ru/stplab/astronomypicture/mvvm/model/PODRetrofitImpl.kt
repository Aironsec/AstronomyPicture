package ru.stplab.astronomypicture.mvvm.model

import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.stplab.astronomypicture.mvvm.model.api.PictureOfTheDayAPI
import ru.stplab.astronomypicture.mvvm.model.entity.PODServerResponseData
import ru.stplab.astronomypicture.mvvm.model.entity.PODServerResponseDataList
import java.io.IOException

class PODRetrofitImpl {

    private val baseUrl = "https://api.nasa.gov/"

    fun getRetrofitImpl(): PictureOfTheDayAPI {
        val podRetrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson()))
            .client(createOkHttpClient(PODInterceptor()))
            .build()
        return podRetrofit.create(PictureOfTheDayAPI::class.java)
    }

//    class MyClassDeserializer : JsonDeserializer<PODServerResponseDataList> {
//        override fun deserialize(
//            json: JsonElement?,
//            typeOfT: Type?,
//            context: JsonDeserializationContext?
//        ): PODServerResponseDataList {
//            val jsonObject = json?.asJsonObject
//            return PODServerResponseDataList(listOf(PODServerResponseData(
//                jsonObject?.get("date")?.asString,
//                jsonObject?.get("explanation")?.asString,
//                jsonObject?.get("media_type")?.asString,
//                jsonObject?.get("title")?.asString,
//                jsonObject?.get("url")?.asString,
//            )))
//        }
//    }
// TODO: 18.01.2021 Не работает!!
    private val deserializer: JsonDeserializer<PODServerResponseDataList> =
        JsonDeserializer { json, _, _ ->
            val jsonObject = json.asJsonObject
            PODServerResponseDataList(listOf(PODServerResponseData(
                jsonObject.get("date").asString,
                jsonObject.get("explanation").asString,
                jsonObject.get("media_type").asString,
                jsonObject.get("title").asString,
                jsonObject.get("url").asString,
            )))
        }

    // TODO: 18.01.2021 Не работает?
    private fun customGson() = GsonBuilder()
        .registerTypeAdapter(PODServerResponseDataList::class.java, deserializer)
        .create()

    private fun gson() = GsonBuilder()
        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        .excludeFieldsWithoutExposeAnnotation()
        .setLenient()
        .create()

    private fun createOkHttpClient(interceptor: Interceptor): OkHttpClient {
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(interceptor)
        httpClient.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        return httpClient.build()
    }

    inner class PODInterceptor : Interceptor {

        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
            return chain.proceed(chain.request())
        }
    }
}
