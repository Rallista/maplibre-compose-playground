package com.maplibre.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import com.maplibre.compose.camera.MapViewCamera
import com.maplibre.compose.settings.MapControls

@Deprecated(
    "Use rememberSaveableMapViewCamera instead",
    ReplaceWith(
        "rememberSaveableMapViewCamera()", "com.maplibre.compose.rememberSaveableMapViewCamera"))
@Composable
fun rememberMapViewCamera(
    initialCamera: MapViewCamera = MapViewCamera.Default
): MutableState<MapViewCamera> {
  return remember { mutableStateOf(initialCamera) }
}

@Composable
fun rememberSaveableMapViewCamera(
    initialCamera: MapViewCamera = MapViewCamera.Default
): MutableState<MapViewCamera> {
  return rememberSaveable { mutableStateOf(initialCamera) }
}

@Composable
fun rememberSaveableMapControls(
    initialMapControls: MapControls = MapControls()
): MutableState<MapControls> {
  return rememberSaveable { mutableStateOf(initialMapControls) }
}
