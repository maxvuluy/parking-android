package com.github.maxvuluy.parking.http

import android.util.Log
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URI
import java.net.URL
import java.util.*

@Suppress("unused")
object HttpConnection {

	private const val TAG = "HttpConnection"

	fun httpGet(host: String, file: String?, parameterList: List<FormUrlEncoded>?): HttpResult {
		val path = file?.let {
			"/$it"
		}
		val query = parameterList?.joinToString("&") {
			it.form
		}
		val httpURLConnection = URI("https", host, path, query, null).toURL().also {
			Log.d(TAG, "url: $it")
		}.openConnection() as HttpURLConnection

		return connect(httpURLConnection.apply {
			connectTimeout = 10000
			readTimeout = 10000
		})
	}

	fun httpJson(
		host: String,
		file: String?,
		requestMethod: String,
		requestProperties: Map<String, String>,
		parameterList: JSONObject
	): HttpResult {
		val httpURLConnection = URL("https", host, file).also {
			Log.d(TAG, "url: $it")
		}.openConnection() as HttpURLConnection

		return connect(httpURLConnection.apply {
			connectTimeout = 10000
			readTimeout = 10000
			doOutput = true
			this.requestMethod = requestMethod

			setRequestProperty("Content-Type", "application/json")
			requestProperties.forEach {
				setRequestProperty(it.key, it.value)
			}
			outputStream.use {
				it.write(parameterList.toString().toByteArray())
			}
		})
	}

	private fun connect(httpURLConnection: HttpURLConnection): HttpResult {
		val responseCode = httpURLConnection.responseCode.also {
			Log.d(TAG, "responseCode: $it")
		}
		return if (responseCode == HttpURLConnection.HTTP_OK) {
			httpURLConnection.inputStream.reader().use {
				HttpSuccess(it.readText())
			}
		} else {
			val error = httpURLConnection.errorStream?.reader()?.use {
				it.readText()
			}
			HttpError(responseCode, error)
		}
	}

}