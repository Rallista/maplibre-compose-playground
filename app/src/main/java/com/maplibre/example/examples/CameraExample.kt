package com.maplibre.example.examples

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.maplibre.example.support.CameraMode
import com.maplibre.example.support.getCameraPositionForMode
import com.maplibre.example.support.getNextCameraMode
import com.maplibre.example.support.locationPermissions
import com.maplibre.example.support.rememberLocationPermissionLauncher
import kotlinx.coroutines.launch
import org.maplibre.compose.camera.CameraPosition
import org.maplibre.compose.camera.rememberCameraState
import org.maplibre.compose.map.MaplibreMap
import org.maplibre.compose.style.BaseStyle
import org.maplibre.spatialk.geojson.Position

@Composable
fun CameraExample() {
  // FLAG: The old library had StaticLocationEngine for simulated locations, and camera modes
  // like TrackingUserLocation / TrackingUserLocationWithBearing that automatically followed
  // the user. The new library uses LocationProvider + LocationTrackingEffect + LocationPuck
  // for location tracking. This example simplifies to demonstrate camera position changes.

  // FLAG: rememberSynchronizedMapViewCamera() with a transform lambda (for conditional padding
  // based on camera mode) has no equivalent. The new CameraState is directly mutable.

  // FLAG: CameraPadding.fractionOfScreen() has no equivalent in the new library.
  // The new CameraPosition.padding uses standard PaddingValues in dp.

  val canChangeCamera = remember { mutableStateOf(false) }
  val currentMode = remember { mutableStateOf(CameraMode.Centered) }
  val scope = rememberCoroutineScope()

  val cameraState =
      rememberCameraState(
          firstPosition =
              CameraPosition(target = Position(longitude = -2.9779, latitude = 53.4106)))

  val permissionLauncher =
      rememberLocationPermissionLauncher(
          onAccess = {
            canChangeCamera.value = true
            currentMode.value = CameraMode.TrackingUserLocation
            scope.launch {
              cameraState.animateTo(getCameraPositionForMode(CameraMode.TrackingUserLocation))
            }
          },
          onFailed = { Log.w("CameraExample", "Location permission denied") })

  Box {
    MaplibreMap(
        modifier = Modifier.fillMaxSize(),
        baseStyle = BaseStyle.Uri("https://demotiles.maplibre.org/style.json"),
        cameraState = cameraState)

    Text(
        "Camera: ${currentMode.value}",
        modifier =
            Modifier.align(Alignment.TopCenter).padding(top = 16.dp, start = 16.dp, end = 16.dp),
        fontSize = 11.sp,
        textAlign = TextAlign.Center)

    Button(
        onClick = {
          if (!canChangeCamera.value) {
            permissionLauncher.launch(locationPermissions)
            return@Button
          }

          val nextMode = getNextCameraMode(currentMode.value)
          currentMode.value = nextMode
          scope.launch { cameraState.animateTo(getCameraPositionForMode(nextMode)) }
        },
        modifier =
            Modifier.align(Alignment.BottomStart)
                .padding(bottom = 32.dp, start = 16.dp, end = 16.dp)) {
          Text("To ${getNextCameraMode(currentMode.value)}")
        }

    Column(
        modifier = Modifier.align(Alignment.BottomEnd).padding(bottom = 150.dp, end = 16.dp),
        horizontalAlignment = Alignment.End) {
          // FLAG: incrementZoom() extension does not exist in the new library.
          // Manually adjusting CameraPosition.zoom instead.
          Button(
              onClick = {
                scope.launch {
                  cameraState.animateTo(
                      cameraState.position.copy(zoom = cameraState.position.zoom + 1.0))
                }
              }) {
                Text("+")
              }

          Button(
              onClick = {
                scope.launch {
                  cameraState.animateTo(
                      cameraState.position.copy(zoom = cameraState.position.zoom - 1.0))
                }
              }) {
                Text("-")
              }
        }
  }
}
