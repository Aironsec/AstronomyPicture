package ru.stplab.astronomypicture.mvvm.view.list

interface INoteItemView {
    var pos: Int
    fun textNote(text: String)
    fun descriptionNote(description: String?)
}