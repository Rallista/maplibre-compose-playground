/*
 * This file is part of ramani-maps.
 *
 * Copyright (c) 2023 Roman Bapst & Jonas Vautherin.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.maplibre.compose.ramani

import android.content.Context
import android.location.Location
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposableTargetMarker
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCompositionContext
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.viewinterop.AndroidView
import com.maplibre.compose.camera.MapViewCamera
import com.maplibre.compose.camera.extensions.toCameraPosition
import com.maplibre.compose.rememberSaveableMapControls
import com.maplibre.compose.runtime.nodes.MapCameraNode
import com.maplibre.compose.runtime.nodes.MapControlsNode
import com.maplibre.compose.settings.MapControls
import org.maplibre.android.location.engine.LocationEngine
import org.maplibre.android.maps.MapLibreMap
import org.maplibre.android.maps.Style
import org.maplibre.android.style.layers.Layer
import org.maplibre.android.style.sources.Source
import org.maplibre.android.utils.BitmapUtils
import setupLocation

@Retention(AnnotationRetention.BINARY)
@ComposableTargetMarker(description = "Maplibre Composable")
@Target(
    AnnotationTarget.FILE,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.TYPE,
    AnnotationTarget.TYPE_PARAMETER,
)
annotation class MapLibreComposable

/**
 * A composable representing a MapLibre map.
 *
 * @param modifier The modifier applied to the map.
 * @param styleUrl The style url to access the tile provider.
 * @param camera The position of the map camera.
 * @param mapControls The control configuration to be displayed on the map.
 * @param properties Properties being applied to the map.
 * @param locationRequestProperties Properties related to the location marker. If null (which is the
 *   default), then the location will not be enabled on the map. Enabling the location requires
 *   setting this field and getting the location permission in your app.
 * @param locationStyling Styling related to the location marker (color, pulse, etc).
 * @param userLocation If set and if the location is enabled (by setting
 *   [locationRequestProperties], it will be updated to contain the latest user location as known by
 *   the map.
 * @param sources External (user-defined) sources for the map.
 * @param layers External (user-defined) layers for the map.
 * @param images Images to be added to the map and used by external layers (pairs of <id, drawable
 *   code>).
 * @param content The content of the map.
 */
@Composable
internal fun MapLibre(
  modifier: Modifier,
  styleUrl: String,
  camera: MutableState<MapViewCamera>,
  mapControls: State<MapControls> = rememberSaveableMapControls(),
  properties: MapProperties = MapProperties(),
  locationEngine: LocationEngine? = null,
  locationRequestProperties: LocationRequestProperties? = null,
  locationStyling: LocationStyling = LocationStyling(),
  userLocation: MutableState<Location>? = null,
  sources: List<Source>? = null,
  layers: List<Layer>? = null,
  images: List<Pair<String, Int>>? = null,
  onMapReadyCallback: ((Style) -> Unit)? = null,
  onTapGestureCallback: ((MapGestureContext) -> Unit)? = null,
  onLongPressGestureCallback: ((MapGestureContext) -> Unit)? = null,
  content: (@Composable @MapLibreComposable () -> Unit)? = null,
) {
  if (LocalInspectionMode.current) {
    Box(modifier = modifier)
    return
  }

  val context = LocalContext.current
  val map = rememberMapViewWithLifecycle()

  val currentMapControls by rememberUpdatedState(mapControls)
  val currentMapProperties by rememberUpdatedState(properties)
  val currentLocationEngine by rememberUpdatedState(locationEngine)
  val currentLocationRequestProperties by rememberUpdatedState(locationRequestProperties)
  val currentLocationStyling by rememberUpdatedState(locationStyling)
  val currentSources by rememberUpdatedState(sources)
  val currentLayers by rememberUpdatedState(layers)
  val currentImages by rememberUpdatedState(images)
  val currentContent by rememberUpdatedState(content)
  val currentCamera by rememberUpdatedState(camera)
  val parentComposition = rememberCompositionContext()

  AndroidView(modifier = modifier, factory = { map })
  LaunchedEffect(currentMapProperties, currentLocationRequestProperties, currentLocationStyling) {
    disposingComposition {
      val maplibreMap = map.awaitMap()
      val style = maplibreMap.awaitStyle(styleUrl)

      maplibreMap.applyProperties(currentMapProperties)
      maplibreMap.setupLocation(
          context,
          style,
          currentLocationEngine,
          currentLocationRequestProperties,
          currentLocationStyling,
          userLocation,
      )
      maplibreMap.setupEventCallbacks(onTapGestureCallback, onLongPressGestureCallback)
      maplibreMap.addImages(context, currentImages)
      maplibreMap.addSources(currentSources)
      maplibreMap.addLayers(currentLayers)
      camera.value.toCameraPosition(maplibreMap)?.let { maplibreMap.cameraPosition = it }

      // Notify the parent callback that the map is ready with a style.
      // This must include all style modifications from adding images, sources, and layers.
      onMapReadyCallback?.invoke(style)

      map.newComposition(parentComposition, style) {
        CompositionLocalProvider {
          MapCameraNode(camera = currentCamera)
          MapControlsNode(mapControls = currentMapControls)
          currentContent?.invoke()
        }
      }
    }
  }
}

private fun MapLibreMap.applyProperties(properties: MapProperties) {
  properties.maxZoom?.let { this.setMaxZoomPreference(it) }
  // TODO: Add Dynamic camera pitch binding
}

private fun MapLibreMap.addImages(context: Context, images: List<Pair<String, Int>>?) {
  images?.let {
    images
        .mapNotNull { image ->
          val drawable = context.getDrawable(image.second)
          val bitmap = BitmapUtils.getBitmapFromDrawable(drawable)
          bitmap?.let { Pair(image.first, bitmap) }
        }
        .forEach { style!!.addImage(it.first, it.second) }
  }
}

private fun MapLibreMap.addSources(sources: List<Source>?) {
  sources?.let { sources.forEach { style!!.addSource(it) } }
}

private fun MapLibreMap.addLayers(layers: List<Layer>?) {
  layers?.let { layers.forEach { style!!.addLayer(it) } }
}
