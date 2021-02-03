package ru.stplab.astronomypicture.ui.navigation.mars

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_mars.*
import ru.stplab.astronomypicture.R

class ItemMarsFragment : Fragment() {

    private var isExpandMenu = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_mars, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setInitialState()
    }

    private fun setInitialState() {
        transparent_background.alpha = 0f
        chip_cameraRHA.alpha = 0f
        chip_cameraNAV.alpha = 0f

        chip_cameraFHA.setOnClickListener {
            if (isExpandMenu) collapsedMenu()
            else expandMenu()
        }
        chip_cameraRHA.setOnClickListener { collapsedMenu() }
        chip_cameraNAV.setOnClickListener { collapsedMenu() }
        transparent_background.setOnClickListener { collapsedMenu() }
    }

    private fun expandMenu() {
        isExpandMenu = true
        ObjectAnimator.ofFloat(chip_cameraRHA, "translationY", 130f).start()
        ObjectAnimator.ofFloat(chip_cameraNAV, "translationY", 260f).start()

        chip_cameraRHA.animate()
            .alpha(1f)
            .setDuration(300)
            .setListener(object : AnimatorListenerAdapter(){
                override fun onAnimationEnd(animation: Animator?) {
                    chip_cameraRHA.isClickable = true
                }
            })

        chip_cameraNAV.animate()
            .alpha(1f)
            .setDuration(300)
            .setListener(object : AnimatorListenerAdapter(){
                override fun onAnimationEnd(animation: Animator?) {
                    chip_cameraNAV.isClickable = true
                }
            })

        transparent_background.animate()
            .alpha(0.9f)
            .setDuration(300)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    transparent_background.isClickable = true
                }
            })
    }

    private fun collapsedMenu() {
        isExpandMenu = false
        ObjectAnimator.ofFloat(chip_cameraRHA, "translationY", 0f).start()
        ObjectAnimator.ofFloat(chip_cameraNAV, "translationY", 0f).start()

        chip_cameraRHA.animate()
            .alpha(0f)
            .setDuration(300)
            .setListener(object : AnimatorListenerAdapter(){
                override fun onAnimationEnd(animation: Animator?) {
                    chip_cameraRHA.isClickable = false
                }
            })

        chip_cameraNAV.animate()
            .alpha(0f)
            .setDuration(300)
            .setListener(object : AnimatorListenerAdapter(){
                override fun onAnimationEnd(animation: Animator?) {
                    chip_cameraNAV.isClickable = false
                }
            })

        transparent_background.animate()
            .alpha(0f)
            .setDuration(300)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    transparent_background.isClickable = false
                }
            })
    }
}
