package com.maplibre.compose.camera.extensions

import com.maplibre.compose.camera.MapViewCameraDefaults

fun validPitch(pitch: Double): Double {
  return if (pitch < MapViewCameraDefaults.MIN_PITCH) {
    MapViewCameraDefaults.MIN_PITCH
  } else if (pitch > MapViewCameraDefaults.MAX_PITCH) {
    MapViewCameraDefaults.MAX_PITCH
  } else {
    pitch
  }
}

fun validZoom(zoom: Double): Double {
  return if (zoom < MapViewCameraDefaults.MIN_ZOOM) {
    MapViewCameraDefaults.MIN_ZOOM
  } else if (zoom > MapViewCameraDefaults.MAX_ZOOM) {
    MapViewCameraDefaults.MAX_ZOOM
  } else {
    zoom
  }
}

fun validDirection(direction: Double): Double {
  val normalizedDirection = direction % 360.0
  return if (normalizedDirection < 0.0) {
    normalizedDirection + 360.0
  } else {
    normalizedDirection
  }
}
