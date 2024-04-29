package dev.shushant.network.model

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonPrimitive

typealias CurrencyCode = String
typealias CurrencyName = String

@Serializable(with = CurrenciesResponse.CurrenciesResponseDeserializer::class)
data class CurrenciesResponse(
    val data: Map<CurrencyCode, CurrencyName>,
) {
    companion object CurrenciesResponseDeserializer : KSerializer<CurrenciesResponse> {
        override val descriptor: SerialDescriptor =
            buildClassSerialDescriptor("CurrenciesResponse") {
                element("data", MapSerializer(String.serializer(), String.serializer()).descriptor)
            }

        override fun deserialize(decoder: Decoder): CurrenciesResponse {
            val json =
                Json.decodeFromJsonElement(
                    JsonObject.serializer(),
                    decoder.decodeSerializableValue(
                        JsonElement.serializer(),
                    ),
                )
            val data = json.mapValues { it.value.jsonPrimitive.content }
            return CurrenciesResponse(data)
        }

        override fun serialize(
            encoder: Encoder,
            value: CurrenciesResponse,
        ) {
            // Serialization is not needed for this example
            throw UnsupportedOperationException("Serialization is not supported")
        }
    }
}
