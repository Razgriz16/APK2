package com.example.oriencoop_score.utility



data class ApiResponse<T>(
    val count: Int,
    val data: List<T>,
    val error_code: Int
)


interface ProductoRepository<T> {
    suspend fun fetchProducto(rut: String): Result<ApiResponse<T>>
}

