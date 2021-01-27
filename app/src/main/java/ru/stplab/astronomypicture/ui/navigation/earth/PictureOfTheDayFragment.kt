package ru.stplab.astronomypicture.ui.navigation.earth

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.transition.ChangeBounds
import android.transition.ChangeImageTransform
import android.transition.TransitionManager
import android.transition.TransitionSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.bottom_sheet_layout.*
import kotlinx.android.synthetic.main.fragment_earth.*
import ru.stplab.astronomypicture.R
import ru.stplab.astronomypicture.mvvm.model.PictureOfTheDayData
import ru.stplab.astronomypicture.mvvm.viewmodal.PictureOfTheDayViewModel

class PictureOfTheDayFragment : Fragment() {

    var isEnlarge = false

    companion object {
        fun newInstance() = PictureOfTheDayFragment()
    }

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private val viewModel: PictureOfTheDayViewModel by lazy {
        ViewModelProvider(this).get(PictureOfTheDayViewModel::class.java)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.getImageVideoData()
            .observe(viewLifecycleOwner) {
                renderData(it)
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_earth_start, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBottomSheetBehavior(view.findViewById(R.id.bottom_sheet_container))
        input_layout.setEndIconOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("https://en.wikipedia.org/wiki/${input_edit_text.text.toString()}")
            })
        }

// TODO: 27.01.2021 Почему не срабатывает click??
        image_view.setOnClickListener {
            isEnlarge = !isEnlarge
            TransitionManager.beginDelayedTransition(
                main, TransitionSet()
                    .addTransition(ChangeBounds())
                    .addTransition(ChangeImageTransform())
            )

            val params: ViewGroup.LayoutParams = image_view.layoutParams
            params.height =
                if (isEnlarge) ViewGroup.LayoutParams.MATCH_PARENT else ViewGroup.LayoutParams.WRAP_CONTENT
            image_view.layoutParams = params
            image_view.scaleType =
                if (isEnlarge) ImageView.ScaleType.CENTER_CROP else ImageView.ScaleType.FIT_CENTER
        }
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
                        image_view.load(url){
                            lifecycle(this@PictureOfTheDayFragment)
                            error(R.drawable.ic_load_error_vector)
                            placeholder(R.drawable.ic_no_photo_vector)
                            crossfade(true)
                            target {
                                image_view.setImageDrawable(it)
                                main.transitionToEnd()
                            }
                        }
//                        image_view.load(url) {
//                            lifecycle(this@PictureOfTheDayFragment)
//                            crossfade(true)
//                            error(R.drawable.ic_load_error_vector)
//                            placeholder(R.drawable.ic_no_photo_vector)
//                        }
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
