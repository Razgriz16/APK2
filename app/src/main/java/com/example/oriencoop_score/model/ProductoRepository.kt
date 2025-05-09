package com.example.oriencoop_score.model

import com.example.oriencoop_score.utility.Result
import com.google.gson.annotations.SerializedName


data class ApiResponse<T>(
    @SerializedName("count") val count: Int,
    @SerializedName("data") val data: List<T>,
    @SerializedName("error_code") val error_code: Int
)


interface ProductoRepository<T> {
    suspend fun fetchProducto(rut: String, token: String?=null): Result<ApiResponse<T>>
}

