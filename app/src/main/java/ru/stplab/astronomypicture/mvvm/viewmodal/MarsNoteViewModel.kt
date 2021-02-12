package ru.stplab.astronomypicture.mvvm.viewmodal

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.stplab.astronomypicture.mvvm.model.entity.Note
import ru.stplab.astronomypicture.mvvm.model.repository.NoteRepo
import ru.stplab.astronomypicture.mvvm.view.list.INoteItemView
import ru.stplab.astronomypicture.mvvm.viewmodal.list.INoteListViewModal
import ru.stplab.astronomypicture.ui.navigation.mars.adapter.recyclerview.ItemTouchHelperAdapter
import ru.stplab.astronomypicture.util.ActionListRV

class MarsNoteViewModel(private val noteRepo: NoteRepo) : ViewModel() {

//    private val _noteLiveData = MutableLiveData<List<Note?>>()
    private val _actionRVLiveData = MutableLiveData<ActionListRV>()

    inner class NoteListViewModal : INoteListViewModal, ItemTouchHelperAdapter {
        override var itemClickListener: ((INoteItemView) -> Unit)? = null

        var notes = mutableListOf<Note?>()

        override fun bindView(view: INoteItemView) {
            val note = notes[view.pos]
            note?.let {
                view.textNote(note.text)
                view.descriptionNote(note.description)
            }

        }

        override fun getCount(): Int = notes.size

        override fun onItemMove(fromPosition: Int, toPosition: Int) {
            notes.removeAt(fromPosition).apply {
                notes.add(if (toPosition > fromPosition) toPosition - 1 else toPosition, this)
            }
            _actionRVLiveData.value = ActionListRV.moved(fromPosition, toPosition)
        }

        override fun onItemDismiss(position: Int) {
            notes.removeAt(position - 1)
            _actionRVLiveData.value = ActionListRV.removed(position)
        }
    }

    val noteListViewModal = NoteListViewModal()

    init {
        noteListViewModal.notes = noteRepo.data
    }

    val actionRV: LiveData<ActionListRV>
        get() = _actionRVLiveData

//    fun getNotes(): LiveData<List<Note?>>{
//        return _noteLiveData
//    }

    fun addNote() {
        noteRepo.addNote()
        _actionRVLiveData.value = ActionListRV.insert(noteListViewModal.getCount())
    }
}