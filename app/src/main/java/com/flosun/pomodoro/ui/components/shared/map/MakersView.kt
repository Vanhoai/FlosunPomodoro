package com.flosun.pomodoro.ui.components.shared.map

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import com.flosun.pomodoro.R
import kotlinx.serialization.json.JsonObject
import org.maplibre.compose.expressions.dsl.image
import org.maplibre.compose.layers.SymbolLayer
import org.maplibre.compose.sources.GeoJsonData
import org.maplibre.compose.sources.rememberGeoJsonSource
import org.maplibre.compose.util.ClickResult
import org.maplibre.spatialk.geojson.Feature
import org.maplibre.spatialk.geojson.FeatureCollection
import org.maplibre.spatialk.geojson.Point

@SuppressLint("UnrememberedMutableState")
@Composable
fun MarkersView(state: MapState) {
    if (state.selectedLocation == null) return

    val clickFeatures by derivedStateOf {
        FeatureCollection(
            listOf(
                Feature(
                    geometry = Point(state.selectedLocation!!.position),
                    properties = JsonObject(mapOf()),
                )
            )
        )
    }

    SymbolLayer(
        id = "click-markers",
        source = rememberGeoJsonSource(data = GeoJsonData.Features(clickFeatures)),
        iconImage = image(painterResource(R.drawable.maker)),
        onClick = { _ -> ClickResult.Consume },
    )
}
