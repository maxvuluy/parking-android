package com.github.maxvuluy.parking.timezone

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.github.maxvuluy.parking.ParkingApplication
import com.github.maxvuluy.parking.RpcCall
import com.github.maxvuluy.parking.UserData
import com.github.maxvuluy.parking.rpc.RpcHttpError
import com.github.maxvuluy.parking.rpc.RpcSuccess
import kotlinx.coroutines.launch
import org.json.JSONException
import java.io.InterruptedIOException
import java.net.ConnectException
import java.net.UnknownHostException

class TimezoneViewModel(application: Application) : AndroidViewModel(application) {

	val username
		get() = getApplication<ParkingApplication>().userData?.username
	val timezone = MutableLiveData(getApplication<ParkingApplication>().userData?.timezone)

	init {
		timezone.observeForever {
			if (it == null || it == getApplication<ParkingApplication>().userData?.timezone) {
				return@observeForever
			}

			viewModelScope.launch {
				if (updateTimezone(it) == null) {
					Toast.makeText(getApplication(), "更新時區失敗", Toast.LENGTH_SHORT).show()
				}
			}
		}
	}

	private suspend fun updateTimezone(timezone: String): UserData? {
		val userData = getApplication<ParkingApplication>().userData ?: return null
		return try {
			when (val rpcResult = RpcCall.updateUserTimezone(userData, timezone)) {
				is RpcSuccess -> rpcResult.result
				is RpcHttpError -> null
			}
		} catch (e: ConnectException) {
			e.printStackTrace()
			null
		} catch (e: InterruptedIOException) {
			e.printStackTrace()
			null
		} catch (e: UnknownHostException) {
			e.printStackTrace()
			null
		} catch (e: JSONException) {
			e.printStackTrace()
			null
		}
	}

}