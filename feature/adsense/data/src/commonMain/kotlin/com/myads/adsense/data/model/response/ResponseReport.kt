package com.myads.adsense.data.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseReport(
    @SerialName("headers") val headers: List<ResponseReportHeader>? = null,
    @SerialName("totals") val totalCell: ResponseReportCell? = null,
    @SerialName("startDate") val startDate: ResponseReportDate? = null,
    @SerialName("endDate") val endDate: ResponseReportDate? = null
)

@Serializable
data class ResponseReportHeader(
    @SerialName("name") val name: String? = null,
    @SerialName("type") val type: String? = null
)

@Serializable
data class ResponseReportCell(
    @SerialName("cells") val cells: List<ResponseReportValue>? = null
)

@Serializable
data class ResponseReportValue(

    @SerialName("value") val value: String? = null
)

@Serializable
data class ResponseReportDate(
    @SerialName("year") val year: String? = null,
    @SerialName("month") val month: String? = null,
    @SerialName("day") val day: String? = null
)