package ru.stplab.astronomypicture.mvvm.view.list

interface IPictureItemView {
    var pos: Int
    fun loadImage(url: String?)
}