package ru.stplab.astronomypicture.mvvm.viewmodal.list

import ru.stplab.astronomypicture.mvvm.view.list.IPictureItemView

interface IPictureListViewPager<V: IPictureItemView> {
    var itemClickListener: ((V) -> Unit)?
    fun bindView(view: V)
    fun getCount(): Int
}