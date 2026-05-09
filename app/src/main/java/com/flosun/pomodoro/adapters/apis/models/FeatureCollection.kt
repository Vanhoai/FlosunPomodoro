package com.flosun.pomodoro.adapters.apis.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FeatureCollection(
    val type: String,
    val query: List<String>,
    val attribution: String,
    val features: List<Feature>,
)

@Serializable
data class Feature(
    val id: String,
    val type: String,
    val text: String,
    @SerialName("place_name") val placeName: String,
    val relevance: Double,
    val language: String? = null,
    @SerialName("text_en") val textEn: String? = null,
    @SerialName("language_en") val languageEn: String? = null,
    @SerialName("place_name_en") val placeNameEn: String? = null,
    @SerialName("text_zh") val textZh: String? = null,
    @SerialName("language_zh") val languageZh: String? = null,
    @SerialName("place_name_zh") val placeNameZh: String? = null,
    @SerialName("place_type") val placeType: List<String>? = null,
    val properties: Properties,
    val geometry: Geometry,
    val bbox: List<Double>,
    val center: List<Double> = emptyList(),
    val context: List<Context> = emptyList(),
)

@Serializable
data class Properties(
    val ref: String,
    @SerialName("country_code") val countryCode: String? = null,
    val kind: String? = null,
    @SerialName("place_designation") val placeDesignation: String? = null,
)

@Serializable
data class Geometry(
    val type: String,
    val coordinates: List<Double>,
)

@Serializable
data class Context(
    val id: String,
    val ref: String,
    val text: String,
    @SerialName("country_code") val countryCode: String? = null,
    val wikidata: String? = null,
    val kind: String? = null,
    @SerialName("place_designation") val placeDesignation: String? = null,
    @SerialName("feature_tags") val featureTags: Map<String, String>? = null,
    val categories: List<String>? = null,
)