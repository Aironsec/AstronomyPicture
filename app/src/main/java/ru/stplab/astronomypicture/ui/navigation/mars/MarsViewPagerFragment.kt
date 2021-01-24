package ru.stplab.astronomypicture.ui.navigation.mars

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.fragment_main_mars.*
import ru.stplab.astronomypicture.R

private const val MARS = 0
private const val WEATHER = 1

class MarsViewPagerFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main_mars, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view_pager.adapter = ViewPagerAdapter(childFragmentManager)

        view_pager.addOnPageChangeListener(listenerPageChange)
        tab_layout.setupWithViewPager(view_pager)
        setHighlightedTab(MARS)
    }

    private val listenerPageChange = object : ViewPager.OnPageChangeListener {
        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
        override fun onPageScrollStateChanged(state: Int) {}

        override fun onPageSelected(position: Int) = setHighlightedTab(position)
    }

    private fun setHighlightedTab(position: Int) {
        val layoutInflater = LayoutInflater.from(requireContext())

        tab_layout.getTabAt(MARS)?.customView = null
        tab_layout.getTabAt(WEATHER)?.customView = null

        when (position) {
            MARS -> {
                setMarsTabHighlighted(layoutInflater)
            }
            WEATHER -> {
                setWeatherTabHighlighted(layoutInflater)
            }
            else -> {
                setMarsTabHighlighted(layoutInflater)
            }
        }
    }

    private fun setMarsTabHighlighted(layoutInflater: LayoutInflater) {
        val mars =
            layoutInflater.inflate(R.layout.fragment_mars_custom_tab_mars, null)
        mars.findViewById<AppCompatTextView>(R.id.tab_image_textview)
            .setTextColor(ContextCompat.getColor(requireContext(), R.color.colorAccent))
        tab_layout.getTabAt(MARS)?.customView = mars
        tab_layout.getTabAt(WEATHER)?.customView =
            layoutInflater.inflate(R.layout.fragment_mars_custom_tab_weather, null)
    }

    private fun setWeatherTabHighlighted(layoutInflater: LayoutInflater) {
        val weather =
            layoutInflater.inflate(R.layout.fragment_mars_custom_tab_weather, null)
        weather.findViewById<AppCompatTextView>(R.id.tab_image_textview)
            .setTextColor(ContextCompat.getColor(requireContext(), R.color.colorAccent))

        tab_layout.getTabAt(MARS)?.customView =
            layoutInflater.inflate(R.layout.fragment_mars_custom_tab_mars, null)
        tab_layout.getTabAt(WEATHER)?.customView = weather
    }

}
