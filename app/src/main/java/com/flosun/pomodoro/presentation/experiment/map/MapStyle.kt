package com.flosun.pomodoro.presentation.experiment.map

import org.flosun.shared.resources.Res
import org.maplibre.compose.layers.Anchor
import org.maplibre.compose.style.BaseStyle

interface MapStyle {
    val displayName: String
    val base: BaseStyle
    val isDark: Boolean
    val anchorBelowSymbols: Anchor
}


enum class Protomaps(override val isDark: Boolean = false) : MapStyle {
    Light,
    Dark(true),
    White,
    Grayscale,
    Black(true);

    override val displayName = name

    override val base =
        BaseStyle.Uri("https://api.protomaps.com/styles/v5/${name.lowercase()}/en.json?key=73c45a97eddd43fb")

    override val anchorBelowSymbols = Anchor.Below("address_label")
}

enum class OpenFreeMap(override val isDark: Boolean = false) : MapStyle {
    Bright,
    Liberty,
    Positron;

    override val displayName = name

    override val base = BaseStyle.Uri("https://tiles.openfreemap.org/styles/${name.lowercase()}")

    override val anchorBelowSymbols = Anchor.Below("waterway_line_label")
}

enum class Versatiles(override val isDark: Boolean = false) : MapStyle {
    Colorful,
    Eclipse(true),
    Graybeard;

    override val displayName = name

    override val base = BaseStyle.Uri("file:///android_asset/${name.lowercase()}.json")

    override val anchorBelowSymbols = Anchor.Below("label-address-housenumber")
}

enum class OtherStyles(
    override val displayName: String,
    override val base: BaseStyle,
    override val isDark: Boolean = false,
    override val anchorBelowSymbols: Anchor = Anchor.Top,
) : MapStyle {
    OpenStreetMaps(
        displayName = "OpenStreetMaps Carto",
        base = BaseStyle.Uri("file:///android_asset/osm-raster.json"),
    ),
    Americana(
        displayName = "Americana",
        base = BaseStyle.Uri("https://americanamap.org/style.json")
    ),
}