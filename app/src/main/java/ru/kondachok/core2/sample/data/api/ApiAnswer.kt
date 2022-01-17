package ru.kondachok.core2.sample.data.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiAnswer(@SerialName("text") val text: String)
