package ru.stplab.astronomypicture.ui.navigation.mars.adapter.pageview

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import ru.stplab.astronomypicture.ui.navigation.mars.ItemMarsFragment
import ru.stplab.astronomypicture.ui.navigation.mars.ItemNoteMarsFragment
import ru.stplab.astronomypicture.ui.navigation.mars.ItemWeatherMarsFragment

class AdapterViewPager(fragmentManager: FragmentManager) :
    FragmentStatePagerAdapter(fragmentManager) {

    override fun getCount(): Int = 3

    override fun getItem(position: Int): Fragment =
        when (position) {
            0 -> ItemMarsFragment()
            1 -> ItemWeatherMarsFragment()
            2 -> ItemNoteMarsFragment()
            else -> ItemMarsFragment()
        }
}
