package ru.stplab.astronomypicture.ui.navigation.mars

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_note_mars.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.stplab.astronomypicture.R
import ru.stplab.astronomypicture.mvvm.viewmodal.MarsNoteViewModel
import ru.stplab.astronomypicture.ui.navigation.mars.adapter.recyclerview.AdapterRVNote
import ru.stplab.astronomypicture.ui.navigation.mars.adapter.recyclerview.ItemTouchHelperCallback
import ru.stplab.astronomypicture.ui.navigation.mars.adapter.recyclerview.OnStartDragListener
import ru.stplab.astronomypicture.util.ActionListRV

class ItemNoteMarsFragment : Fragment() {

    private val viewModel by viewModel<MarsNoteViewModel>()
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

        adapter = AdapterRVNote(viewModel.noteListViewModal, object : OnStartDragListener {
            override fun onStartDrag(viewHolder: RecyclerView.ViewHolder) {
                itemTouchHelper.startDrag(viewHolder)
            }
        })

        rv_note_mars.layoutManager = LinearLayoutManager(requireContext())
        rv_note_mars.addItemDecoration(DividerItemDecoration(requireContext(), LinearLayoutManager.HORIZONTAL))
        rv_note_mars.adapter = adapter
        itemTouchHelper = ItemTouchHelper(ItemTouchHelperCallback(requireContext(), viewModel.noteListViewModal))
        itemTouchHelper.attachToRecyclerView(rv_note_mars)

        viewModel.actionRV
            .observe(viewLifecycleOwner) {
                when(it.status) {
                    ActionListRV.Action.MOVED -> it.fromPos?.let { it1 ->
                        it.toPos?.let { it2 ->
                            adapter.notifyItemMoved(it1, it2)
                        }
                    }
                    ActionListRV.Action.REMOVED -> it.fromPos?.let { it1 ->
                        adapter.notifyItemRemoved(it1)
                    }
                    ActionListRV.Action.INSERT -> it?.fromPos?.let { it1 ->
                        adapter.notifyItemInserted(it1)
                    }
                }
            }

        fab_add_item.setOnClickListener {
            viewModel.addNote()
        }
    }
}