package com.maplibre.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import com.maplibre.compose.camera.MapViewCamera

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
