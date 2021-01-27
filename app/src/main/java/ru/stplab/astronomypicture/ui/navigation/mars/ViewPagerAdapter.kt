package ru.stplab.astronomypicture.ui.navigation.mars

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class ViewPagerAdapter(fragmentManager: FragmentManager) :
    FragmentStatePagerAdapter(fragmentManager) {

    override fun getCount(): Int = 2

    override fun getItem(position: Int): Fragment =
        when (position) {
            0 -> ItemMarsFragment()
            1 -> ItemWeatherMarsFragment()
            else -> ItemMarsFragment()
        }
}
