package com.flosun.pomodoro.presentation.experiment.map

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.flosun.pomodoro.R
import com.flosun.pomodoro.core.constants.DEBUG_TAG
import kotlinx.serialization.json.JsonObject
import org.maplibre.compose.expressions.dsl.image
import org.maplibre.compose.layers.SymbolLayer
import org.maplibre.compose.map.MapOptions
import org.maplibre.compose.map.MaplibreMap
import org.maplibre.compose.sources.GeoJsonData
import org.maplibre.compose.sources.rememberGeoJsonSource
import org.maplibre.compose.util.ClickResult
import org.maplibre.spatialk.geojson.Feature
import org.maplibre.spatialk.geojson.FeatureCollection
import org.maplibre.spatialk.geojson.Point
import timber.log.Timber


@Composable
fun MapView(
    modifier: Modifier = Modifier,
    state: MapState = rememberMapState(),
) {
    MaplibreMap(
        modifier = modifier,
        styleState = state.styleState,
        cameraState = state.cameraState,
        baseStyle = state.selectedStyle.base,
        onMapClick = { position, offset ->
            state.selectedLocation = MapClickEvent(position, offset)
            ClickResult.Pass
        },
        options = MapOptions(
            renderOptions = state.renderOption,
            ornamentOptions = state.ornamentOptions,
        )
    ) {
        Markers(state)
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
fun Markers(state: MapState) {
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
