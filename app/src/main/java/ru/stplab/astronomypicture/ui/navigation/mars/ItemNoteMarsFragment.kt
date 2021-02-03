package ru.stplab.astronomypicture.ui.navigation.mars

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_note_mars.*
import ru.stplab.astronomypicture.R
import ru.stplab.astronomypicture.mvvm.viewmodal.MarsNoteViewModel
import ru.stplab.astronomypicture.ui.navigation.mars.adapter.recyclerview.AdapterRVNote
import ru.stplab.astronomypicture.ui.navigation.mars.adapter.recyclerview.ItemTouchHelperCallback
import ru.stplab.astronomypicture.ui.navigation.mars.adapter.recyclerview.OnStartDragListener

class ItemNoteMarsFragment : Fragment() {

    private val viewModel by lazy {
        ViewModelProvider(this).get(MarsNoteViewModel::class.java)
    }
    private lateinit var adapter: AdapterRVNote
    private lateinit var itemTouchHelper: ItemTouchHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.fragment_note_mars, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = AdapterRVNote(object : OnStartDragListener {
            override fun onStartDrag(viewHolder: RecyclerView.ViewHolder) {
                itemTouchHelper.startDrag(viewHolder)
            }
        })
        rv_note_mars.layoutManager = LinearLayoutManager(requireContext())
        rv_note_mars.addItemDecoration(DividerItemDecoration(requireContext(), LinearLayoutManager.HORIZONTAL))
        rv_note_mars.adapter = adapter
        itemTouchHelper = ItemTouchHelper(ItemTouchHelperCallback(requireContext(), adapter))
        itemTouchHelper.attachToRecyclerView(rv_note_mars)
        viewModel.getNotes
            .observe(viewLifecycleOwner) {
                adapter.notes = it
//                adapter.setItems(it)
            }

        fab_add_item.setOnClickListener {
            viewModel.addNote()
        }
    }
}