package com.emil.sharedportfoliolite.api

import com.google.gson.annotations.SerializedName

data class YahooFinanceResponse(
    @SerializedName("chart")
    val chart: Chart
)

data class Chart(
    @SerializedName("result")
    val result: List<Result>?,
    @SerializedName("error")
    val error: Error?
)

data class Result(
    @SerializedName("meta")
    val meta: Meta
)

data class Meta(
    @SerializedName("regularMarketPrice")
    val regularMarketPrice: Double
)

data class Error(
    @SerializedName("code")
    val code: String?,
    @SerializedName("description")
    val description: String?
)