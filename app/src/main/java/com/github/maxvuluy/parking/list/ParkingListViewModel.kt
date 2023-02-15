package com.github.maxvuluy.parking.list

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class ParkingListViewModel(application: Application) : AndroidViewModel(application) {
	val parkingList = MutableLiveData<List<ParkingData>>()
	val loading = MutableLiveData(false)
	private val repository = ParkingListRepository()

	init {
		viewModelScope.launch {
			loading.value = true

			parkingList.value = repository.getParkingList()
			if (parkingList.value == null) {
				Toast.makeText(getApplication(), "無法取得停車位資訊", Toast.LENGTH_SHORT).show()
			}

			loading.value = false
		}
	}

}