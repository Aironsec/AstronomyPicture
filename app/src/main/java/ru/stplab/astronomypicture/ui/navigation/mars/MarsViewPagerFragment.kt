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
import ru.stplab.astronomypicture.ui.navigation.mars.adapter.pageview.AdapterViewPager

private const val MARS = 0
private const val WEATHER = 1
private const val NOTE = 2

class MarsViewPagerFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main_mars, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view_pager.adapter = AdapterViewPager(childFragmentManager)

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
        tab_layout.getTabAt(NOTE)?.customView = null

        when (position) {
            MARS -> {
                setMarsTabHighlighted(layoutInflater)
            }
            WEATHER -> {
                setWeatherTabHighlighted(layoutInflater)
            }
            NOTE -> {
                setNoteTabHighlighted(layoutInflater)
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
        tab_layout.getTabAt(NOTE)?.customView =
            layoutInflater.inflate(R.layout.fragment_mars_custom_tab_note, null)
    }

    private fun setWeatherTabHighlighted(layoutInflater: LayoutInflater) {
        val weather =
            layoutInflater.inflate(R.layout.fragment_mars_custom_tab_weather, null)
        weather.findViewById<AppCompatTextView>(R.id.tab_image_textview)
            .setTextColor(ContextCompat.getColor(requireContext(), R.color.colorAccent))

        tab_layout.getTabAt(MARS)?.customView =
            layoutInflater.inflate(R.layout.fragment_mars_custom_tab_mars, null)
        tab_layout.getTabAt(WEATHER)?.customView = weather
        tab_layout.getTabAt(NOTE)?.customView =
            layoutInflater.inflate(R.layout.fragment_mars_custom_tab_note, null)
    }

    private fun setNoteTabHighlighted(layoutInflater: LayoutInflater) {
        val note =
            layoutInflater.inflate(R.layout.fragment_mars_custom_tab_note, null)
        note.findViewById<AppCompatTextView>(R.id.tab_image_textview)
            .setTextColor(ContextCompat.getColor(requireContext(), R.color.colorAccent))

        tab_layout.getTabAt(MARS)?.customView =
            layoutInflater.inflate(R.layout.fragment_mars_custom_tab_mars, null)
        tab_layout.getTabAt(WEATHER)?.customView =
            layoutInflater.inflate(R.layout.fragment_mars_custom_tab_weather, null)
        tab_layout.getTabAt(NOTE)?.customView = note
    }

}
