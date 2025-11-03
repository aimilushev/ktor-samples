package io.ktor.samples.mpp.client

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.url
import io.ktor.client.statement.bodyAsText
import io.ktor.http.Url
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.time.measureTimedValue

internal expect val ApplicationDispatcher: CoroutineDispatcher

class ApplicationApi {
    private val client: HttpClient = initHttpClient()

    private val address = Url("https://api.thecatapi.com/v1/images/search")

    @OptIn(DelicateCoroutinesApi::class)
    fun about(callback: (String) -> Unit) {
        GlobalScope.launch(ApplicationDispatcher) {
            val result: String = client.get {
                url(address.toString())
            }.bodyAsText()

            callback(result)
        }
    }

    private fun initHttpClient(): HttpClient {
        val (httpClient, time) = measureTimedValue { HttpClient() }
        println("HttpClient initialized in $time")
        return httpClient
    }
}
