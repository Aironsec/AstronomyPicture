package ru.stplab.astronomypicture.ui

import android.os.Bundle
import android.view.Gravity
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.stplab.astronomypicture.R
import ru.stplab.astronomypicture.mvvm.viewmodal.MainActivityViewModel
import ru.stplab.astronomypicture.ui.navigation.MainBottomNavigationFragment
import ru.stplab.astronomypicture.util.LoadingState

class MainActivity : AppCompatActivity() {

    private val viewModel by viewModel<MainActivityViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme)
        setContentView(R.layout.activity_main)

        image_view.animate().rotationBy(550f)
            .setInterpolator(AccelerateDecelerateInterpolator()).duration = 2750

        viewModel.loadingState
            .observe(this) {
                renderStatus(it)
            }
    }

    private fun renderStatus(state: LoadingState) {
        when (state.status) {
            LoadingState.Status.FAILED -> toast(state.msg)
            LoadingState.Status.RUNNING -> toast("Loading")
            LoadingState.Status.SUCCESS -> {
                toast("Success")
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container, MainBottomNavigationFragment.newInstance())
                    .commitNow()
            }
        }
    }

    private fun toast(string: String?) {
        Toast.makeText(this, string, Toast.LENGTH_SHORT).apply {
            setGravity(Gravity.BOTTOM, 0, 250)
            show()
        }
    }

}