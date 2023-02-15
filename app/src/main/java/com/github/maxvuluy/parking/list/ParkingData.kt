package com.github.maxvuluy.parking.list

data class ParkingData(val id: String, val name: String, val address: String, val totalCar: Int) {

	var availableCar = 0
	var chargeStation = false
	var standby = 0
	var charging = 0

	fun updateAvailable(availableCar: Int) {
		this.availableCar = availableCar
	}

	fun updateAvailable(availableCar: Int, standby: Int, charging: Int) {
		this.availableCar = availableCar
		chargeStation = true
		this.standby = standby
		this.charging = charging
	}

}
