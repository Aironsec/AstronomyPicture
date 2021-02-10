package ru.stplab.astronomypicture.mvvm.viewmodal

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.stplab.astronomypicture.mvvm.model.repository.PictureRepo
import ru.stplab.astronomypicture.util.LoadingState

class PictureOfTheDayViewModel(private val pictureRepo: PictureRepo) : ViewModel() {

    private val _bottomSheetState = MutableLiveData(false)

    val bottomSheetState: LiveData<Boolean>
        get() = _bottomSheetState

    val data = pictureRepo.data

    fun setBottomSheetState() {
        _bottomSheetState.value?.let {
            _bottomSheetState.value = !it
        }
    }
}
