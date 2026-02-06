package com.maplibre.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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

/**
 * Creates a bi-directionally synchronized [MapViewCamera] state.
 *
 * This is useful when you need to maintain a camera state outside of a Composable (e.g., in a
 * ViewModel, CarAppTemplate, etc).
 *
 * @param externalCamera The mutable state camera that belongs to your ViewModel, CarAppTemplate,
 *   etc.
 * @param transformExternalToLocal An optional transform applied when syncing from external to local
 *   camera. This can be used to apply a [CameraPadding] that requires composable context.
 * @param transformLocalToExternal Transform applied when syncing from local to external camera.
 * @return A [MutableState] of [MapViewCamera] that should be passed to the [MapView]'s camera
 *   parameter.
 * @sample
 *
 * ```
 * val mapViewCamera = rememberSaveableMapViewCamera()
 *
 * @Composable
 * fun content() {
 *     val cameraPadding = CameraPadding.fractionOfScreen(top = 0.6f)
 *     val localCamera = rememberSynchronizedMapViewCamera(
 *         externalCamera = mapViewCamera,
 *         transformExternalToLocal = { it.copy(padding = cameraPadding) }
 *     )
 *
 *     MapView(camera = localCamera, ...)
 * }
 * ```
 */
@Composable
fun rememberSynchronizedMapViewCamera(
    externalCamera: MutableState<MapViewCamera>,
    transformExternalToLocal: (MapViewCamera) -> MapViewCamera = { it },
    transformLocalToExternal: (MapViewCamera) -> MapViewCamera = { it },
): MutableState<MapViewCamera> {
  val localCamera = rememberSaveableMapViewCamera(transformExternalToLocal(externalCamera.value))

  LaunchedEffect(externalCamera.value) {
    val transformed = transformExternalToLocal(externalCamera.value)
    if (localCamera.value != transformed) {
      localCamera.value = transformed
    }
  }

  LaunchedEffect(localCamera.value) {
    val transformed = transformLocalToExternal(localCamera.value)
    if (externalCamera.value != transformed) {
      externalCamera.value = transformed
    }
  }

  return localCamera
}
