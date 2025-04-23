package com.example.oriencoop_score.model

import com.google.gson.annotations.SerializedName

data class UserResponse(
    @SerializedName("data") val data: List<UserData>,
    @SerializedName("error_code") val errorCode: Int,
    @SerializedName("error_message") val errorMessage: String,
)

data class UserData(
    @SerializedName("apellidomaterno") val apellidomaterno: String,
    @SerializedName("apellidopaterno") val apellidopaterno: String,
    @SerializedName("email") val email: String,
    @SerializedName("nombres") val nombres: String,
)

data class Mail(
    @SerializedName("data") val data: String,
    @SerializedName("error_code") val errorCode: Int,
    @SerializedName("error_message") val errorMessage: String,
)

data class AddressResponse(
    @SerializedName("data") val data: List<AddressData>,
    @SerializedName("error_code") val errorCode: Int,
    @SerializedName("error_message") val errorMessage: String
)

data class AddressData(
    @SerializedName("callenumero") val calleNumero: String,
)