package ru.stplab.astronomypicture.mvvm.viewmodal.list

interface IListViewModal<V> {
    var itemClickListener: ((V) -> Unit)?
    fun bindView(view: V)
    fun getCount(): Int
}