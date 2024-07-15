package com.test.mylocations.list

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import com.test.mylocations.MainViewModel
import com.test.mylocations.MyItemRecyclerViewAdapter
import com.test.mylocations.R

/**
 * A fragment representing a list of Items.
 */
class ItemFragment : Fragment()
{

    private var columnCount = 1

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        val view = inflater.inflate(R.layout.fragment_item_list, container, false)

        // Set the adapter
        if (view is RecyclerView)
        {
            with(view) {
                addItemDecoration(DividerItemDecoration(requireContext(),RecyclerView.VERTICAL))
                layoutManager = when
                {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else             -> GridLayoutManager(context, columnCount)
                }
                adapter = MyItemRecyclerViewAdapter()
            }
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        val vm = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        vm.location.observe(viewLifecycleOwner, Observer {
            it?:return@Observer
            ((view as? RecyclerView)?.adapter  as? MyItemRecyclerViewAdapter)?.submitList(it)
        })
    }

    companion object
    {
        const val ARG_COLUMN_COUNT = "column-count"
    }
}