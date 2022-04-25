package com.myads.adsense.data

import io.ktor.client.engine.*

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object HttpClientEngineProvider {
    val httpClientEngine: HttpClientEngine
}