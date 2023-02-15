package com.github.maxvuluy.parking.databinding

import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener

@BindingAdapter("android:selectedItem", "android:selectedItemAttrChanged", requireAll = false)
fun setSelectedItem(view: Spinner, selectedItem: String?, listener: InverseBindingListener?) {
	if (selectedItem != null) {
		val position = (view.adapter as ArrayAdapter<String>).getPosition(selectedItem)
		if (position != view.selectedItemPosition) {
			view.setSelection(position)
		}
	}

	if (listener != null) {
		view.onItemSelectedListener = object : OnItemSelectedListener {
			override fun onItemSelected(
				parent: AdapterView<*>?,
				view: View?,
				position: Int,
				id: Long
			) {
				listener.onChange()
			}

			override fun onNothingSelected(parent: AdapterView<*>?) {

			}
		}
	}
}

@InverseBindingAdapter(attribute = "android:selectedItem")
fun getSelectedItem(view: Spinner): String {
	return view.selectedItem as String
}