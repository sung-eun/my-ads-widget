package com.myads.adsense.data.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseAccount(
    @SerialName("name") val name: String? = null,
    @SerialName("displayName") val displayName: String? = null,
    @SerialName("createTime") val createTime: String? = null
)