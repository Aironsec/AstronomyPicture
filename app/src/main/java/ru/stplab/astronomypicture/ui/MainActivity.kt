package ru.stplab.astronomypicture.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import ru.stplab.astronomypicture.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null)
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, PictureOfTheDayFragment.newInstance())
                .commitNow()
    }
}