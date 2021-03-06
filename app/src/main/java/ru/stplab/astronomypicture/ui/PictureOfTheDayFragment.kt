package ru.stplab.astronomypicture.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil.api.load
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.bottom_sheet_layout.*
import kotlinx.android.synthetic.main.item_view_pager.*
import kotlinx.android.synthetic.main.main_fragment.*
import ru.stplab.astronomypicture.R
import ru.stplab.astronomypicture.mvvm.model.PictureOfTheDayData
import ru.stplab.astronomypicture.mvvm.viewmodal.PictureOfTheDayViewModel
import ru.stplab.astronomypicture.ui.chips.ChipsFragment

class PictureOfTheDayFragment : Fragment() {

    companion object {
        fun newInstance() = PictureOfTheDayFragment()
        private var isMain = true
    }

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private val viewModel: PictureOfTheDayViewModel by lazy {
        ViewModelProvider(this).get(PictureOfTheDayViewModel::class.java)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.getImageData()
            .observe(viewLifecycleOwner) {
                renderData(it)
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBottomSheetBehavior(view.findViewById(R.id.bottom_sheet_container))
        chip_theme1.isChecked = true

        chip_theme1.setOnClickListener {
            with(requireActivity()) {
                setTheme(R.style.AppTheme)
                recreate()
            }
        }

        chip_theme2.setOnClickListener {
            with(requireActivity()) {
                setTheme(R.style.AppTheme2)
                recreate()
            }

        }

        input_layout.setEndIconOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("https://en.wikipedia.org/wiki/${input_edit_text.text.toString()}")
            })
        }
//        setBottomAppBar(view)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_bottom_bar, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.app_bar_fav -> toast("Favourite")
            R.id.app_bar_settings -> activity?.supportFragmentManager?.beginTransaction()
                ?.add(R.id.container, ChipsFragment())?.addToBackStack(null)?.commit()
            android.R.id.home -> {
                activity?.let {
                    BottomNavigationDrawerFragment().show(it.supportFragmentManager, "tag")
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun renderData(data: PictureOfTheDayData) {
        when (data) {
            is PictureOfTheDayData.Success -> {
                val serverResponseData = data.serverResponseData
                val url = serverResponseData.url
                val title = serverResponseData.title
                val description = serverResponseData.explanation
                val mediaType = serverResponseData.mediaType

                title?.let {
                    bottom_sheet_title.text = it
                } ?: bottom_sheet_title.text.let { "No Title" }

                description?.let {
                    bottom_sheet_description.text = it
                } ?: bottom_sheet_description.text.let { "No Descriptions" }

                if (url.isNullOrEmpty()) {
                    //showError("Сообщение, что ссылка пустая")
                    toast("Link is empty")
                } else {
                    //showSuccess()
                    if (mediaType == "video") {
                        with(web_view) {
                            clearCache(true)
                            clearHistory()
                            settings.javaScriptEnabled = true
                            settings.javaScriptCanOpenWindowsAutomatically = true
                            loadUrl(url)
                        }
                    } else {
                        image_view.load(url) {
                            lifecycle(this@PictureOfTheDayFragment)
                            error(R.drawable.ic_load_error_vector)
                            placeholder(R.drawable.ic_no_photo_vector)
                        }
                    }
                }
            }
            is PictureOfTheDayData.Loading -> {
                //showLoading()
            }
            is PictureOfTheDayData.Error -> {
                //showError(data.error.message)
                toast(data.error.message)
            }
        }
    }

//    private fun setBottomAppBar(view: View) {
//        val context = activity as MainActivity
//        context.setSupportActionBar(view.findViewById(R.id.bottom_app_bar))
//        setHasOptionsMenu(true)
//        fab.setOnClickListener {
//            if (isMain) {
//                isMain = false
//                bottom_app_bar.navigationIcon = null
//                bottom_app_bar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_END
//                fab.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_back_fab))
//                bottom_app_bar.replaceMenu(R.menu.menu_bottom_bar_other_screen)
//            } else {
//                isMain = true
//                bottom_app_bar.navigationIcon =
//                    ContextCompat.getDrawable(context, R.drawable.ic_hamburger_menu_bottom_bar)
//                bottom_app_bar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_CENTER
//                fab.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_plus_fab))
//                bottom_app_bar.replaceMenu(R.menu.menu_bottom_bar)
//            }
//        }
//    }

    private fun setBottomSheetBehavior(bottomSheet: ConstraintLayout) {
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    private fun toast(string: String?) {
        Toast.makeText(context, string, Toast.LENGTH_SHORT).apply {
            setGravity(Gravity.BOTTOM, 0, 250)
            show()
        }
    }
}
