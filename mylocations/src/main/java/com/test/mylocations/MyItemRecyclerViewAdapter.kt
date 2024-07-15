package com.test.mylocations

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil

import com.test.mylocations.databinding.FragmentItemBinding
import com.test.mylocations.presentation.model.LocationPresentationModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MyItemRecyclerViewAdapter: ListAdapter<LocationPresentationModel,MyItemRecyclerViewAdapter.ViewHolder>(
    object : DiffUtil.ItemCallback<LocationPresentationModel>()
    {
        override fun areItemsTheSame(oldItem: LocationPresentationModel, newItem: LocationPresentationModel): Boolean =
            oldItem === newItem

        override fun areContentsTheSame(oldItem: LocationPresentationModel, newItem: LocationPresentationModel): Boolean =
            oldItem == newItem
    }
)
{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        return ViewHolder(
            FragmentItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        val item = getItem(position)
        holder.idView.text = SimpleDateFormat("H:mm:ss dd/MM/yyyy", Locale.US).format(Date(item.timestamp) )
        "${item.latitude}° N, ${item.longitude}° E".let {
            holder.contentView.text = it
        }
    }


    inner class ViewHolder(binding: FragmentItemBinding) : RecyclerView.ViewHolder(binding.root)
    {
        val idView: TextView = binding.itemNumber
        val contentView: TextView = binding.content

        override fun toString(): String
        {
            return super.toString() + " '" + contentView.text + "'"
        }
    }

}