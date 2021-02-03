package ru.stplab.astronomypicture.ui.navigation.mars.adapter.recyclerview

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MotionEventCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_header_for_rv.view.*
import kotlinx.android.synthetic.main.item_note_for_rv.*
import kotlinx.android.synthetic.main.item_note_for_rv.view.*
import ru.stplab.astronomypicture.R
import ru.stplab.astronomypicture.mvvm.model.entity.Note


class AdapterRVNote(private val dragListener: OnStartDragListener) :
    RecyclerView.Adapter<BaseViewHolder<*>>(), ItemTouchHelperAdapter {

    var notes  = mutableListOf<Note>()
        set(value) {
            field = value
            notifyItemInserted(itemCount - 1)
        }

    companion object {
        private const val TYPE_HEADER = 0
        private const val TYPE_NOTE = 1
    }

    inner class VHHeader(itemView: View) : BaseViewHolder<String>(itemView) {
        override fun bind(item: String) {
            itemView.tv_header.text = "Заметки"
        }
    }

    inner class VHNote(override val containerView: View) :
        BaseViewHolder<Note>(containerView), LayoutContainer, ItemTouchHelperViewHolder {

        @SuppressLint("ClickableViewAccessibility")
        override fun bind(item: Note) {
            containerView.imageViewMars.setOnTouchListener { _, event ->
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                    dragListener.onStartDrag(this)
                }
                false
            }

            with(item) {
                et_note.setText(text)
            }
        }

        override fun onItemSelected() {
            containerView.setBackgroundColor(Color.LTGRAY)
        }

        override fun onItemClear() {
            containerView.setBackgroundColor(Color.WHITE)
        }

    }

    override fun getItemViewType(position: Int): Int =
        when (position) {
            0 -> TYPE_HEADER
            else -> TYPE_NOTE
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE_HEADER ->
                VHHeader(inflater.inflate(R.layout.item_header_for_rv, parent, false))

            else -> VHNote(inflater.inflate(R.layout.item_note_for_rv, parent, false))
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        when (holder) {
            is VHNote -> holder.bind(notes[position - 1])
        }
    }

    override fun onBindViewHolder(
        holder: BaseViewHolder<*>,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty())
            super.onBindViewHolder(holder, position, payloads)
        else {
            val combinedChange =
                createCombinedPayload(payloads as List<Change<Note>>)
            val oldData = combinedChange.oldData
            val newData = combinedChange.newData

            if (newData.text != oldData.text) {
                holder.itemView.et_note.setText(newData.text)
            }
        }
    }

    override fun getItemCount(): Int = notes.size + 1

    fun setItems(newItems: List<Note>) {
        val result = DiffUtil.calculateDiff(DiffUtilCallback(notes, newItems), false)
        result.dispatchUpdatesTo(this)
        notes.clear()
        notes.addAll(newItems)
    }

    inner class DiffUtilCallback(
        private var oldItems: List<Note>,
        private var newItems: List<Note>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldItems.size

        override fun getNewListSize(): Int = newItems.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean = true

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldItems[oldItemPosition].text == newItems[newItemPosition].text

        override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any {
            val oldItem = oldItems[oldItemPosition]
            val newItem = newItems[newItemPosition]

            return Change(
                oldItem,
                newItem
            )
        }
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        notes.removeAt(fromPosition).apply {
            notes.add(if (toPosition > fromPosition) toPosition - 1 else toPosition, this)
        }
        notifyItemMoved(fromPosition, toPosition)
    }

    override fun onItemDismiss(position: Int) {
        notes.removeAt(position - 1)
        notifyItemRemoved(position)
    }
}