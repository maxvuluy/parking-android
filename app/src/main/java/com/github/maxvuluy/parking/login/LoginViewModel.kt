package com.github.maxvuluy.parking.login

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.*
import com.github.maxvuluy.parking.ParkingApplication
import com.github.maxvuluy.parking.RpcCall
import com.github.maxvuluy.parking.rpc.RpcHttpError
import com.github.maxvuluy.parking.rpc.RpcSuccess
import kotlinx.coroutines.launch
import org.json.JSONException
import java.io.InterruptedIOException
import java.net.ConnectException
import java.net.UnknownHostException

class LoginViewModel(application: Application) : AndroidViewModel(application) {

	val username = MutableLiveData<String>()
	val password = MutableLiveData<String>()
	val loading = MutableLiveData(false)
	val loggedIn = MutableLiveData(false)

	private suspend fun login() = try {
		when (val rpcResult = RpcCall.login(username.value ?: "", password.value ?: "")) {
			is RpcSuccess -> rpcResult.result
			is RpcHttpError -> {
				Toast.makeText(getApplication(), "登入失敗（${rpcResult.error}）", Toast.LENGTH_SHORT).show()
				null
			}
		}
	} catch (e: ConnectException) {
		e.printStackTrace()
		Toast.makeText(getApplication(), "網路異常，登入失敗", Toast.LENGTH_SHORT).show()
		null
	} catch (e: InterruptedIOException) {
		e.printStackTrace()
		Toast.makeText(getApplication(), "網路異常，登入失敗", Toast.LENGTH_SHORT).show()
		null
	} catch (e: UnknownHostException) {
		e.printStackTrace()
		Toast.makeText(getApplication(), "網路異常，登入失敗", Toast.LENGTH_SHORT).show()
		null
	} catch (e: JSONException) {
		e.printStackTrace()
		Toast.makeText(getApplication(), "資料異常，登入失敗", Toast.LENGTH_SHORT).show()
		null
	}

	fun loginClicked() {
		viewModelScope.launch {
			loading.value = true

			getApplication<ParkingApplication>().userData = login()
			loggedIn.value = getApplication<ParkingApplication>().userData != null

			loading.value = false
		}
	}

}