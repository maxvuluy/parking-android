package com.github.maxvuluy.parking

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.github.maxvuluy.parking.databinding.ActivityMainBinding
import com.github.maxvuluy.parking.login.LoginViewModel

class MainActivity : AppCompatActivity() {

	private lateinit var binding: ActivityMainBinding

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		val viewModel = ViewModelProvider(this).get<LoginViewModel>()
		binding = ActivityMainBinding.inflate(layoutInflater).apply {
			this.viewModel = viewModel
			lifecycleOwner = this@MainActivity
		}
		setContentView(binding.root)

		binding.apply {
			buttonLogin.setOnClickListener {
				viewModel.loginClicked()
			}
		}

		viewModel.loggedIn.observe(this) {
			if (it) {
				val intent = Intent(this, ParkingActivity::class.java)
				startActivity(intent)
			}
		}
	}

}