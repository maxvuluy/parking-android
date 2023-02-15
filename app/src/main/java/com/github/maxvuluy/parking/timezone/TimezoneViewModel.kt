package com.github.maxvuluy.parking.timezone

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
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
	val timezone
		get() = getApplication<ParkingApplication>().userData?.timezone

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

	fun onTimezoneSelected(timezone: String) {
		if (timezone == getApplication<ParkingApplication>().userData?.timezone) {
			return
		}

		viewModelScope.launch {
			if (updateTimezone(timezone) == null) {
				Toast.makeText(getApplication(), "更新時區失敗", Toast.LENGTH_SHORT).show()
			}
		}
	}

}