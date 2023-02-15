package com.github.maxvuluy.parking.list

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.get
import com.github.maxvuluy.parking.R
import com.github.maxvuluy.parking.databinding.FragmentParkingListBinding
import com.github.maxvuluy.parking.timezone.TimezoneFragment

class ParkingListFragment : Fragment() {

	companion object {
		fun newInstance() = ParkingListFragment()
	}

	private lateinit var binding: FragmentParkingListBinding
	private lateinit var viewModel: ParkingListViewModel

	private val adapter = ParkingListAdapter()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		viewModel = ViewModelProvider(this).get()
	}

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
	): View {
		binding = FragmentParkingListBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		binding.apply {
			viewModel = this@ParkingListFragment.viewModel
			lifecycleOwner = viewLifecycleOwner

			recyclerView.adapter = adapter

			buttonTimezone.setOnClickListener {
				parentFragmentManager
					.beginTransaction()
					.replace(R.id.container, TimezoneFragment.newInstance())
					.addToBackStack(null)
					.commit()
			}
		}

		viewModel.parkingList.observe(viewLifecycleOwner) {
			if (it != null) {
				adapter.submitList(it)
			}
		}
	}

}