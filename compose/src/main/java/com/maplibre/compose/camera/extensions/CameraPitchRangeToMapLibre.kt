package com.maplibre.compose.camera.extensions

import com.maplibre.compose.camera.MapViewCameraDefaults
import com.maplibre.compose.camera.models.CameraPitchRange
import kotlin.math.roundToInt

/**
 * Converts a MapLibre minimum and maximum pitch a CameraPitchRange.
 *
 * Automatically clamps the minimum and maximum pitch values to the MapViewCameraDefaults.MIN_PITCH
 * and MapViewCameraDefaults.MAX_PITCH.
 *
 * @param minPitch The minimum pitch value.
 * @param maxPitch The maximum pitch value.
 * @return The CameraPitchRange.
 */
fun CameraPitchRange.Companion.fromMapLibre(minPitch: Double, maxPitch: Double): CameraPitchRange {
  val minFinal =
      if (minPitch < MapViewCameraDefaults.MIN_PITCH) {
        MapViewCameraDefaults.MIN_PITCH
      } else {
        minPitch.roundToInt().toDouble()
      }

  val maxFinal =
      if (maxPitch > MapViewCameraDefaults.MAX_PITCH) {
        MapViewCameraDefaults.MAX_PITCH
      } else {
        maxPitch.roundToInt().toDouble()
      }

  if (minFinal == MapViewCameraDefaults.MIN_PITCH && maxFinal == MapViewCameraDefaults.MAX_PITCH) {
    return CameraPitchRange.Free
  }

  if (minFinal == maxFinal) {
    return CameraPitchRange.Fixed(minFinal)
  }

  return CameraPitchRange.FreeWithPitch(minFinal, maxFinal)
}

/**
 * Converts a CameraPitchRange to a value range for easy application to a MapLibre map.
 *
 * @return The MapLibre minimum and maximum pitch equivalent of the CameraPitchRange.
 */
fun CameraPitchRange.toMapLibre(): Pair<Double, Double> {
  return when (this) {
    is CameraPitchRange.Free ->
        Pair(MapViewCameraDefaults.MIN_PITCH, MapViewCameraDefaults.MAX_PITCH)
    is CameraPitchRange.FreeWithPitch -> Pair(minimum, maximum)
    is CameraPitchRange.Fixed -> Pair(pitch, pitch)
  }
}
