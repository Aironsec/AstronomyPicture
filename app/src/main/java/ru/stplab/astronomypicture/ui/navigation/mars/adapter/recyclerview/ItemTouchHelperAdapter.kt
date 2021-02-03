package ru.stplab.astronomypicture.ui.navigation.mars.adapter.recyclerview

interface ItemTouchHelperAdapter {
    fun onItemMove(fromPosition: Int, toPosition: Int)
    fun onItemDismiss(position: Int)
}