package ru.snowadv.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EmojisResponseDto(
    @SerialName("name_to_codepoint")
    val nameToCodepoint: Map<String, String>,
)