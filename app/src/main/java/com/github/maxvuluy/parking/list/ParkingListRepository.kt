package com.github.maxvuluy.parking.list

import com.github.maxvuluy.parking.RpcCall
import com.github.maxvuluy.parking.rpc.RpcHttpError
import com.github.maxvuluy.parking.rpc.RpcSuccess
import org.json.JSONException
import java.io.InterruptedIOException
import java.net.ConnectException
import java.net.UnknownHostException

class ParkingListRepository {

	private suspend fun desc() = try {
		when (val rpcResult = RpcCall.parkingDesc()) {
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

	private suspend fun available(parkingMap: Map<String, ParkingData>) = try {
		when (val rpcResult = RpcCall.parkingAvailable(parkingMap)) {
			is RpcSuccess -> rpcResult.result
			is RpcHttpError -> false
		}
	} catch (e: ConnectException) {
		e.printStackTrace()
		false
	} catch (e: InterruptedIOException) {
		e.printStackTrace()
		false
	} catch (e: UnknownHostException) {
		e.printStackTrace()
		false
	} catch (e: JSONException) {
		e.printStackTrace()
		false
	}

	suspend fun getParkingList(): List<ParkingData>? {
		val parkingMap = desc()
		return if (parkingMap != null && available(parkingMap)) {
			parkingMap.values.toList()
		} else {
			null
		}
	}

}