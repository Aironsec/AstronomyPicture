package ru.stplab.astronomypicture.ui.navigation

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.main_bottom_navigation_fragment.*
import ru.stplab.astronomypicture.R
import ru.stplab.astronomypicture.ui.navigation.earth.PictureOfTheDayFragment
import ru.stplab.astronomypicture.ui.navigation.mars.MarsViewPagerFragment
import ru.stplab.astronomypicture.ui.navigation.settings.SettingsFragment

class MainBottomNavigationFragment : Fragment() {

    companion object {
        fun newInstance() = MainBottomNavigationFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_bottom_navigation_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bottom_navigation_view.setOnNavigationItemSelectedListener(bottomNavigationListener)
        bottom_navigation_view.selectedItemId = R.id.bottom_view_earth
    }

    private val bottomNavigationListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when(item.itemId) {
            R.id.bottom_view_earth -> {
                childFragmentManager.beginTransaction()
                    .replace(R.id.bottom_navigation_container, PictureOfTheDayFragment.newInstance())
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit()
                true
            }
            R.id.bottom_view_mars -> {
                childFragmentManager.beginTransaction()
                    .replace(R.id.bottom_navigation_container, MarsViewPagerFragment())
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit()
                true
            }
            R.id.bottom_view_settings -> {
                childFragmentManager.beginTransaction()
                    .replace(R.id.bottom_navigation_container, SettingsFragment())
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit()
                true
            }
            else -> {
                childFragmentManager.beginTransaction()
                    .replace(R.id.bottom_navigation_container, PictureOfTheDayFragment())
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commitAllowingStateLoss()
                true
            }
        }
    }
}
