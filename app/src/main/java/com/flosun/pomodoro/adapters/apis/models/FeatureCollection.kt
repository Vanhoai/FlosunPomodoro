package com.flosun.pomodoro.adapters.apis.models

import com.google.gson.annotations.SerializedName
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
    @SerializedName("place_name") val placeName: String? = null,
    val relevance: Double,
    val language: String? = null,
    @SerializedName("text_en") val textEn: String? = null,
    @SerializedName("language_en") val languageEn: String? = null,
    @SerializedName("place_name_en") val placeNameEn: String? = null,
    @SerializedName("text_zh") val textZh: String? = null,
    @SerializedName("language_zh") val languageZh: String? = null,
    @SerializedName("place_name_zh") val placeNameZh: String? = null,
    @SerializedName("place_type") val placeType: List<String>? = null,
    val properties: Properties,
    val geometry: Geometry,
    val bbox: List<Double>,
    val center: List<Double>,
    val context: List<Context>,
)

@Serializable
data class Properties(
    val ref: String,
    @SerializedName("country_code") val countryCode: String? = null,
    val kind: String,
    @SerializedName("place_designation") val placeDesignation: String? = null,
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
    @SerializedName("country_code") val countryCode: String? = null,
    val wikidata: String? = null,
    val kind: String? = null,
    @SerializedName("place_designation") val placeDesignation: String? = null,
    @SerializedName("feature_tags") val featureTags: Map<String, String>? = null,
    val categories: List<String>? = null,
)