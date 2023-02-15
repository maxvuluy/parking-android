package com.github.maxvuluy.parking.rpc

data class RpcSuccess<T>(val result: T) : RpcResult<T>()