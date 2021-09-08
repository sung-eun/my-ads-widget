package com.myads.adsense.data.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseAccountList(
    @SerialName("accounts") val accounts: List<ResponseAccount> = emptyList(),
    @SerialName("nextPageToken") val nextPageToken: String? = null
)