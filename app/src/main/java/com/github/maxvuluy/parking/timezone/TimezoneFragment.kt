package com.github.maxvuluy.parking.timezone

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.github.maxvuluy.parking.databinding.FragmentTimezoneBinding

class TimezoneFragment : Fragment(), OnItemSelectedListener {

	companion object {
		fun newInstance() = TimezoneFragment()
	}

	private lateinit var binding: FragmentTimezoneBinding
	private lateinit var viewModel: TimezoneViewModel

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		viewModel = ViewModelProvider(this).get()
	}

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
	): View {
		binding = FragmentTimezoneBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		binding.apply {
			viewModel = this@TimezoneFragment.viewModel
			lifecycleOwner = viewLifecycleOwner

			toolbar.setNavigationOnClickListener {
				parentFragmentManager.popBackStack()
			}

			val adapter = spinnerTimezone.adapter as ArrayAdapter<String>
			spinnerTimezone.setSelection(adapter.getPosition(this@TimezoneFragment.viewModel.timezone))
			spinnerTimezone.onItemSelectedListener = this@TimezoneFragment
		}
	}

	override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
		val adapter = parent?.adapter ?: return
		viewModel.onTimezoneSelected(adapter.getItem(position) as String)
	}

	override fun onNothingSelected(parent: AdapterView<*>?) {

	}

}