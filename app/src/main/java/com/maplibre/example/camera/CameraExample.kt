package com.maplibre.example.camera

import android.Manifest
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.maplibre.compose.camera.MapViewCamera
import com.maplibre.compose.MapView
import com.maplibre.compose.camera.CameraState
import com.maplibre.example.support.StaticLocationEngine
import com.maplibre.example.support.locationPermissions
import com.maplibre.example.support.rememberLocationPermissionLauncher

@Composable
fun CameraExample() {

    val canChangeCamera = remember { mutableStateOf(false) }

    val mapViewCamera = remember { mutableStateOf(MapViewCamera.Default) }
//    val mapViewCamera = rememberUpdatedState(MapViewCamera.Default)
    val nextCameraState = getNextCamera(mapViewCamera.value.state)

    // TODO: This could use improvement to handle failure cases
    //      (see logs for warnings about LocationEngine)
    val permissionLauncher = rememberLocationPermissionLauncher(
        onAccess = {
            canChangeCamera.value = true
            mapViewCamera.value = MapViewCamera.TrackingUserLocation()
        },
        onFailed = {
            Log.w("CameraExample", "Location permission denied")
        }
    )
    
    Scaffold {
        Box(modifier = Modifier.padding(it)) {
            MapView(
                styleUrl = "https://demotiles.maplibre.org/style.json", // TODO: Move to user setting
                camera = mapViewCamera,
                locationEngine = remember { StaticLocationEngine() }
            )

            Text(
                "CameraState: ${mapViewCamera.value.state}",
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 16.dp, start = 16.dp, end = 16.dp),
                textAlign = TextAlign.Center
            )

            Button(
                onClick = {
                    if (!canChangeCamera.value) {
                        permissionLauncher.launch(locationPermissions)
                        return@Button
                    }

                    mapViewCamera.value = getNextCamera(mapViewCamera.value.state)
                },
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(bottom = 32.dp, start = 16.dp)
            ) {
                Text("Change to ${nextCameraState.state}")
            }
        }
    }
}

fun getNextCamera(currentState: CameraState): MapViewCamera {
    return when (currentState) {
        is CameraState.Centered -> MapViewCamera.TrackingUserLocation()
        CameraState.TrackingUserLocation -> MapViewCamera.Default
        CameraState.TrackingUserLocationWithBearing -> MapViewCamera.Default
    }
}