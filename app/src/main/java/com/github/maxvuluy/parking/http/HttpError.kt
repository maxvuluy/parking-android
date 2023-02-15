package com.github.maxvuluy.parking.http

data class HttpError(val responseCode: Int, val error: String?) : HttpResult()