package com.github.maxvuluy.parking.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.maxvuluy.parking.databinding.AdapterParkingListBinding

class ParkingListAdapter : ListAdapter<ParkingData, ParkingListAdapter.ViewHolder>(DIFF_CALLBACK) {

	companion object {
		private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ParkingData>() {
			override fun areItemsTheSame(oldItem: ParkingData, newItem: ParkingData) =
				newItem.id == oldItem.id

			override fun areContentsTheSame(oldItem: ParkingData, newItem: ParkingData) =
				newItem == oldItem
		}
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
		AdapterParkingListBinding.inflate(
			LayoutInflater.from(parent.context), parent, false
		)
	)

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		with(holder) {
			with(getItem(position)) {
				binding.parkingData = this
			}
		}
	}

	class ViewHolder(val binding: AdapterParkingListBinding) : RecyclerView.ViewHolder(binding.root)

}