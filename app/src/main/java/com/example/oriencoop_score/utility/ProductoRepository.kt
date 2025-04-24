package com.example.oriencoop_score.utility

import com.example.oriencoop_score.model.ApiResponse


interface ProductoRepository<T> {
    suspend fun fetchProducto(rut: String): Result<ApiResponse<T>>
}

