package ru.stplab.astronomypicture.mvvm.model.repository

import ru.stplab.astronomypicture.mvvm.model.entity.Note

class NoteRepo {

    val data: MutableList<Note?> = mutableListOf(null)

    fun addNote() {
        data.add(Note())
    }
}