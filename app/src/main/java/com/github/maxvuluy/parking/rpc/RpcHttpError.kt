package com.github.maxvuluy.parking.rpc

data class RpcHttpError<T>(val responseCode: Int, val error: String?) : RpcResult<T>()