package com.github.maxvuluy.parking

import android.util.Log
import com.github.maxvuluy.parking.http.HttpConnection
import com.github.maxvuluy.parking.http.HttpError
import com.github.maxvuluy.parking.http.HttpSuccess
import com.github.maxvuluy.parking.list.ParkingData
import com.github.maxvuluy.parking.rpc.RpcHttpError
import com.github.maxvuluy.parking.rpc.RpcResult
import com.github.maxvuluy.parking.rpc.RpcSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

object RpcCall {

	private const val APPLICATION_ID = "vqYuKPOkLQLYHhk4QTGsGKFwATT4mBIGREI2m8eD"

	suspend fun login(username: String, password: String): RpcResult<UserData> {
		val requestProperties = mapOf("X-Parse-Application-Id" to APPLICATION_ID)
		val parameterList = JSONObject().put("username", username).put("password", password)
		val httpResult = withContext(Dispatchers.IO) {
			HttpConnection.httpJson(
				"noodoe-app-development.web.app",
				"api/login",
				"POST",
				requestProperties,
				parameterList
			)
		}

		val result = when (httpResult) {
			is HttpSuccess -> httpResult.result.also {
				Log.d("RpcCall", "login: $it")
			}
			is HttpError -> return RpcHttpError(httpResult.responseCode, httpResult.error)
		}

		val jsonObject = JSONObject(result)
		with(jsonObject) {
			val objectId = getString("objectId")
			val sessionToken = getString("sessionToken")
			val timezone = getString("timezone")

			return RpcSuccess(UserData(username, objectId, sessionToken, timezone))
		}
	}

	suspend fun updateUserTimezone(userData: UserData, timezone: String): RpcResult<UserData> {
		val requestProperties = mapOf(
			"X-Parse-Application-Id" to APPLICATION_ID,
			"X-Parse-Session-Token" to userData.sessionToken
		)
		val parameterList = JSONObject().put("timezone", timezone)
		val httpResult = withContext(Dispatchers.IO) {
			HttpConnection.httpJson(
				"noodoe-app-development.web.app",
				"api/users/${userData.objectId}",
				"PUT",
				requestProperties,
				parameterList
			)
		}

		return when (httpResult) {
			is HttpSuccess -> {
				userData.timezone = timezone
				RpcSuccess(userData)
			}
			is HttpError -> RpcHttpError(httpResult.responseCode, httpResult.error)
		}
	}

	suspend fun parkingDesc(): RpcResult<Map<String, ParkingData>> {
		val httpResult = withContext(Dispatchers.IO) {
			HttpConnection.httpGet(
				"tcgbusfs.blob.core.windows.net", "blobtcmsv/TCMSV_alldesc.json", null
			)
		}

		val result = when (httpResult) {
			is HttpSuccess -> httpResult.result.also {
				Log.d("RpcCall", "parkingDesc: $it")
			}
			is HttpError -> return RpcHttpError(httpResult.responseCode, httpResult.error)
		}

		val parkArray = JSONObject(result).getJSONObject("data").getJSONArray("park")
		val parkingMap = buildMap<String, ParkingData> {
			for (i in 0 until parkArray.length()) {
				val parkObject = parkArray.getJSONObject(i)
				with(parkObject) {
					val id = getString("id")
					val name = getString("name")
					val address = getString("address")
					val totalCar = getInt("totalcar")

					this@buildMap.put(id, ParkingData(id, name, address, totalCar))
				}
			}
		}

		return RpcSuccess(parkingMap)
	}

	suspend fun parkingAvailable(parkingMap: Map<String, ParkingData>): RpcResult<Boolean> {
		val httpResult = withContext(Dispatchers.IO) {
			HttpConnection.httpGet(
				"tcgbusfs.blob.core.windows.net", "blobtcmsv/TCMSV_allavailable.json", null
			)
		}

		val result = when (httpResult) {
			is HttpSuccess -> httpResult.result.also {
				Log.d("RpcCall", "parkingAvailable: $it")
			}
			is HttpError -> return RpcHttpError(httpResult.responseCode, httpResult.error)
		}

		val parkArray = JSONObject(result).getJSONObject("data").getJSONArray("park")
		for (i in 0 until parkArray.length()) {
			val parkObject = parkArray.getJSONObject(i)
			with(parkObject) {
				val id = getString("id")
				val availableCar = getInt("availablecar")
				val chargeStation = optJSONObject("ChargeStation")

				val parkingData = parkingMap[id] ?: return@with
				if (chargeStation != null) {
					var standby = 0
					var charging = 0

					val socketArray = chargeStation.getJSONArray("scoketStatusList")
					for (j in 0 until socketArray.length()) {
						when (socketArray.getJSONObject(j).getString("spot_status")) {
							"待機中" -> standby++
							"充電中" -> charging++
						}
					}

					parkingData.updateAvailable(availableCar, standby, charging)
				} else {
					parkingData.updateAvailable(availableCar)
				}
			}
		}

		return RpcSuccess(true)
	}

}