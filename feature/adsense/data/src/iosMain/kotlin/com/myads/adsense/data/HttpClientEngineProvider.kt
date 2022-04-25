package com.myads.adsense.data

import io.ktor.client.engine.*
import io.ktor.client.engine.ios.*

actual object HttpClientEngineProvider {
    actual val httpClientEngine: HttpClientEngine = Ios.create()
}