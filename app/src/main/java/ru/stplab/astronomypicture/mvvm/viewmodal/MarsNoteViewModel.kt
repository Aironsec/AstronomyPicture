package ru.stplab.astronomypicture.mvvm.viewmodal

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.stplab.astronomypicture.mvvm.model.entity.Note

class MarsNoteViewModel(
    private val liveDataForNote: MutableLiveData<MutableList<Note>> = MutableLiveData()
) : ViewModel() {

    private val data = mutableListOf(Note())

    val getNotes: LiveData<MutableList<Note>>
        get() = liveDataForNote

    init {
        liveDataForNote.value = data
    }

    fun addNote() {
        data.add(Note())
        liveDataForNote.value = data
    }

}