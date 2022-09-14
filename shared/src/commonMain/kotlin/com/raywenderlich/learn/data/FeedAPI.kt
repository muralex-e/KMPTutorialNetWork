package com.raywenderlich.learn.data

import HttpClientLogger
import com.raywenderlich.learn.APP_NAME
import com.raywenderlich.learn.X_APP_NAME
import com.raywenderlich.learn.data.model.GravatarProfile
import com.soywiz.korio.net.http.Http
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.serialization.kotlinx.json.*
import kotlin.native.concurrent.ThreadLocal

import io.ktor.client.plugins.ContentNegotiation
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json


public const val GRAVATAR_URL = "https://en.gravatar.com/"
public const val GRAVATAR_RESPONSE_FORMAT = ".json"

@ThreadLocal
public object FeedAPI {

    private val nonStrictJson = Json { isLenient = true; ignoreUnknownKeys = true }

    private val client: HttpClient = HttpClient {

        defaultRequest {
            header(X_APP_NAME, APP_NAME)
        }

        install(ContentNegotiation) {
            json(nonStrictJson)
        }

        install(Logging) {
            logger = HttpClientLogger
            level = LogLevel.ALL
        }
    }

    public suspend fun fetchRWEntry(feedUrl: String): HttpResponse {
        return   client.get(feedUrl)
    }

    public suspend fun fetchMyGravatar(hash: String): GravatarProfile =
        client.get("$GRAVATAR_URL$hash$GRAVATAR_RESPONSE_FORMAT") {
            //header(X_APP_NAME, APP_NAME)
        }.body()
}



