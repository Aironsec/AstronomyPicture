package ru.stplab.astronomypicture.mvvm.viewmodal

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.stplab.astronomypicture.mvvm.model.repository.PictureRepo
import ru.stplab.astronomypicture.util.LoadingState

class PictureOfTheDayViewModel(private val pictureRepo: PictureRepo) : ViewModel() {

    private val _loadingState = MutableLiveData<LoadingState>()
    private val _bottomSheetState = MutableLiveData(false)

    val bottomSheetState: LiveData<Boolean>
        get() = _bottomSheetState
    val loadingState: LiveData<LoadingState>
        get() = _loadingState

    val data = pictureRepo.data

    init {
        fetchData()
    }

    private fun fetchData() {
        viewModelScope.launch {
            try {
                _loadingState.value = LoadingState.LOADING
                pictureRepo.refresh()
                _loadingState.value = LoadingState.LOADED
            } catch (e: Exception) {
                _loadingState.value = LoadingState.error(e.message)
            }
        }
    }

    fun setBottomSheetState() {
        _bottomSheetState.value?.let {
            _bottomSheetState.value = !it
        }
    }

//    fun getImageVideoData(): LiveData<PictureOfTheDayData> {
//        sendServerRequestImageAndVideo()
//        return liveDataForViewToObserve
//    }
//
//    private fun sendServerRequestImageAndVideo() {
//        liveDataForViewToObserve.value = PictureOfTheDayData.Loading(null)
//        val apiKey: String = BuildConfig.NASA_API_KEY
//        if (apiKey.isBlank()) {
//            PictureOfTheDayData.Error(Throwable("You need API key"))
//        } else {
//            retrofitImpl.getRetrofitImpl().getPictureOfTheDayAsync(apiKey).enqueue(object :
//                Callback<PicturesOfTheDay> {
//                override fun onResponse(
//                    call: Call<PicturesOfTheDay>,
//                    response: Response<PicturesOfTheDay>
//                ) {
//                    if (response.isSuccessful && response.body() != null) {
//                        liveDataForViewToObserve.value =
//                            PictureOfTheDayData.Success(response.body()!!)
//                    } else {
//                        val message = response.message()
//                        if (message.isNullOrEmpty()) {
//                            liveDataForViewToObserve.value =
//                                PictureOfTheDayData.Error(Throwable("Unidentified error"))
//                        } else {
//                            liveDataForViewToObserve.value =
//                                PictureOfTheDayData.Error(Throwable(message))
//                        }
//                    }
//                }
//
//                override fun onFailure(call: Call<PicturesOfTheDay>, t: Throwable) {
//                    liveDataForViewToObserve.value = PictureOfTheDayData.Error(t)
//                }
//            })
//        }
//    }
}
