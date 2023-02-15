package com.github.maxvuluy.parking

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.maxvuluy.parking.list.ParkingListFragment

class ParkingActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_parking)
		if (savedInstanceState == null) {
			supportFragmentManager.beginTransaction()
				.replace(R.id.container, ParkingListFragment.newInstance())
				.commitNow()
		}
	}
}