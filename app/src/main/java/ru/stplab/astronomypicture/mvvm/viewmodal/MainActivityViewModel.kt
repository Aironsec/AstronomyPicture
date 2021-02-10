package ru.stplab.astronomypicture.mvvm.viewmodal

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.stplab.astronomypicture.mvvm.model.repository.PictureRepo
import ru.stplab.astronomypicture.util.LoadingState

class MainActivityViewModel(private val pictureRepo: PictureRepo) : ViewModel() {

    private val _loadingState = MutableLiveData<LoadingState>()

    val loadingState: LiveData<LoadingState>
        get() = _loadingState

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

}