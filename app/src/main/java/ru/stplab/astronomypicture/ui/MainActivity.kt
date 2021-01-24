package ru.stplab.astronomypicture.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.stplab.astronomypicture.R
import ru.stplab.astronomypicture.ui.navigation.MainBottomNavigationFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null)
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainBottomNavigationFragment.newInstance())
                .commitNow()
    }

}