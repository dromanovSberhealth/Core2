package ru.kondachok.core2.sample.data.api

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.get

private const val API_HOST = "http://private-db490c-magicball.apiary-mock.com"

class MagicBallApi {

    private val magicBallClient = HttpClient(OkHttp) {
        install(JsonFeature) {
            serializer = KotlinxSerializer()
        }
    }

    suspend fun getAnswers(): List<ApiAnswer> {
        return magicBallClient.get("$API_HOST/answers")
    }
}
