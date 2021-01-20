package ru.stplab.astronomypicture.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import ru.stplab.astronomypicture.R
import ru.stplab.astronomypicture.mvvm.model.entity.PODServerResponseData

class PictureOfTheDayAdapter(private val data: PODServerResponseData) : RecyclerView.Adapter<PictureOfTheDayAdapter.PagerVH>() {

    val pictures = listOf(data)

    inner class PagerVH(override val containerView: View) : LayoutContainer,
        RecyclerView.ViewHolder(containerView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerVH =
        PagerVH(
            LayoutInflater.from(parent.context).inflate(R.layout.item_view_pager, parent, false)
        )

    override fun onBindViewHolder(holder: PagerVH, position: Int) {

    }

    override fun getItemCount(): Int = 1
}