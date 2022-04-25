package com.myads.adsense.data

import io.ktor.client.engine.*
import io.ktor.client.engine.okhttp.*

actual object HttpClientEngineProvider {
    actual val httpClientEngine: HttpClientEngine = OkHttp.create()
}