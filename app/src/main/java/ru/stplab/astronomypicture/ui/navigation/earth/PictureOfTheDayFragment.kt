package ru.stplab.astronomypicture.ui.navigation.earth

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.IconMarginSpan
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
import coil.load
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.bottom_sheet_layout.*
import kotlinx.android.synthetic.main.fragment_earth.*
import kotlinx.android.synthetic.main.fragment_earth.image_view
import kotlinx.android.synthetic.main.fragment_earth.input_edit_text
import kotlinx.android.synthetic.main.fragment_earth.input_layout
import kotlinx.android.synthetic.main.fragment_earth.main
import kotlinx.android.synthetic.main.fragment_earth_start.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.stplab.astronomypicture.R
import ru.stplab.astronomypicture.mvvm.model.entity.PicturesOfTheDay
import ru.stplab.astronomypicture.mvvm.viewmodal.PictureOfTheDayViewModel
import ru.stplab.astronomypicture.util.LoadingState
import java.util.*


class PictureOfTheDayFragment : Fragment() {

    private var isEnlarge = false
    private val highlight =
        "interstellar|constellation|constellation|perturbation|radiation|asteroid|astronaut|ionosphere|" +
                "background|radiation|black hole|orbit|jupiter|observatory|astronomer|astronomy|" +
                "earth|phase|galaxy|gibbous|eclipse|uranus|comet|cosmonaut|corona|light-year|lunar|" +
                "mars|mercury|meteor|meteor shower|meteorite|milky way|moon|nebula|nadir|neptune|" +
                "nova|neutron star|planet|planetoid|lanetary nebula|pluto|pulsar|rocket|radiant|" +
                "satellite|solstice|solar|solar system|solar wind|supernova|space|space station|" +
                "star|shooting star|sun|sunspot|telescope|vacuum|venus|umbra|zenith|nasa|" +
                "alpha centauri|quasar"

    companion object {
        fun newInstance() = PictureOfTheDayFragment()
    }

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private val viewModel by viewModel<PictureOfTheDayViewModel>()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.data
            .observe(viewLifecycleOwner) { data ->
                data?.let { renderData(it) }
            }
        viewModel.loadingState
            .observe(viewLifecycleOwner) {
                renderStatus(it)
            }
        viewModel.bottomSheetState
            .observe(viewLifecycleOwner) {
                when (it) {
                    false -> bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                    else -> bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                }
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

        region_title.setOnClickListener {
            viewModel.setBottomSheetState()
        }
    }

    private fun renderStatus(state: LoadingState) {
        when (state.status) {
            LoadingState.Status.FAILED -> toast(state.msg)
            LoadingState.Status.RUNNING -> toast("Loading")
            LoadingState.Status.SUCCESS -> toast("Success")
        }
    }

    @SuppressLint("SetJavaScriptEnabled", "UseCompatLoadingForDrawables")
    private fun renderData(data: PicturesOfTheDay) {
        val url = data.url
        val title = data.title
        val description = data.explanation
        val mediaType = data.mediaType

        title?.let { it ->
            val regex = """($highlight)""".toRegex(RegexOption.IGNORE_CASE)
            var matchResult = regex.find(it)
            val spannable = SpannableString(it)

            val drawable = resources.getDrawable(R.drawable.ic_notification_important_24, resources.newTheme())
            val bitmap = drawableToBitmap(drawable)

            while (matchResult != null) {
                spannable.setSpan(
                    ForegroundColorSpan(Color.RED),
                    matchResult.range.first, matchResult.range.last + 1,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                spannable.setSpan(
                    IconMarginSpan(bitmap, 20),
                    0, spannable.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                matchResult = matchResult.next()
            }
            bottom_sheet_title.text = spannable
        } ?: bottom_sheet_title.text.let { "No Title" }

        description?.let {
            val regex = """($highlight)""".toRegex(RegexOption.IGNORE_CASE)
            val descriptionHtml = regex.replace(it) { match ->
                "<FONT COLOR='#FF9018'>${match.value.toUpperCase(Locale.ROOT)}</FONT>"
            }
            bottom_sheet_description.text = Html.fromHtml(descriptionHtml)
        } ?: bottom_sheet_description.text.let { "No Descriptions" }

        if (url.isNullOrEmpty()) {
            toast("Link is empty")
        } else {
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
                    crossfade(true)
                    target {
                        image_view.setImageDrawable(it)
                        main.transitionToEnd()
                    }
                }
            }
        }
    }

    private fun drawableToBitmap(drawable: Drawable): Bitmap {
        if (drawable is BitmapDrawable) {
            return drawable.bitmap
        }
        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }

//    @SuppressLint("SetJavaScriptEnabled")
//    private fun renderData(data: PictureOfTheDayData) {
//        when (data) {
//            is PictureOfTheDayData.Success -> {
//                val serverResponseData = data.serverResponseData
//                val url = serverResponseData.url
//                val title = serverResponseData.title
//                val description = serverResponseData.explanation
//                val mediaType = serverResponseData.mediaType
//
//                title?.let {
//                    bottom_sheet_title.text = it
//                } ?: bottom_sheet_title.text.let { "No Title" }
//
//                description?.let {
//                    bottom_sheet_description.text = it
//                } ?: bottom_sheet_description.text.let { "No Descriptions" }
//
//                if (url.isNullOrEmpty()) {
//                    //showError("Сообщение, что ссылка пустая")
//                    toast("Link is empty")
//                } else {
//                    //showSuccess()
//                    if (mediaType == "video") {
//                        with(web_view) {
//                            clearCache(true)
//                            clearHistory()
//                            settings.javaScriptEnabled = true
//                            settings.javaScriptCanOpenWindowsAutomatically = true
//                            loadUrl(url)
//                        }
//                    } else {
//                        image_view.load(url) {
//                            lifecycle(this@PictureOfTheDayFragment)
//                            error(R.drawable.ic_load_error_vector)
//                            placeholder(R.drawable.ic_no_photo_vector)
//                            crossfade(true)
//                            target {
//                                image_view.setImageDrawable(it)
//                                main.transitionToEnd()
//                            }
//                        }
////                        image_view.load(url) {
////                            lifecycle(this@PictureOfTheDayFragment)
////                            crossfade(true)
////                            error(R.drawable.ic_load_error_vector)
////                            placeholder(R.drawable.ic_no_photo_vector)
////                        }
//                    }
//
//                }
//            }
//            is PictureOfTheDayData.Loading -> {
//                //showLoading()
//            }
//            is PictureOfTheDayData.Error -> {
//                //showError(data.error.message)
//                toast(data.error.message)
//            }
//        }
//    }

    private fun setBottomSheetBehavior(bottomSheet: ConstraintLayout) {
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
    }

    private fun toast(string: String?) {
        Toast.makeText(context, string, Toast.LENGTH_SHORT).apply {
            setGravity(Gravity.BOTTOM, 0, 440)
            show()
        }
    }
}
