package com.example.orderbike.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.orderbike.databinding.BikeItemListBinding
import com.example.orderbike.model.Feature


class BikeListAdapter : ListAdapter<Feature, BikeListAdapter.FeatureViewHolder>(ItemGroupDiffUtill()) {

    var onItemClick: ((Feature) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeatureViewHolder {
        return FeatureViewHolder(
            BikeItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }


    override fun onBindViewHolder(holder: FeatureViewHolder, position: Int) {
        val cardoption = getItem(position)

        holder.binding.label.text = cardoption.properties?.label
        holder.binding.noOfBike.text = cardoption.properties?.bikes
        holder.binding.noOfPlace.text = cardoption.properties?.free_racks
        holder.binding.distance.text= cardoption.distance.toString().plus("km")
    }


    class ItemGroupDiffUtill : DiffUtil.ItemCallback<Feature>() {
        override fun areItemsTheSame(oldItem: Feature, newItem: Feature): Boolean {
            return oldItem.id === newItem.id
        }

        override fun areContentsTheSame(oldItem: Feature, newItem: Feature): Boolean {
            return oldItem == newItem
        }

    }


    inner class FeatureViewHolder(val binding: BikeItemListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(getItem(adapterPosition))
            }
        }

    }
}