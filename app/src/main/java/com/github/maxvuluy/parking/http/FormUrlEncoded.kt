package com.github.maxvuluy.parking.http

data class FormUrlEncoded(val name: String, val value: String) {

	val form
		get() = "$name=$value"

}