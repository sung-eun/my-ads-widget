package com.myads.adsense.data.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponsePayments(
    @SerialName("payments") val payments: List<ResponsePayment>? = null
)

@Serializable
data class ResponsePayment(
    @SerialName("name") val name: String? = null,
    @SerialName("amount") val amount: String? = null
)