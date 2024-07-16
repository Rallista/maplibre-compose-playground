package com.maplibre.compose.camera.models

import android.os.Parcelable
import com.maplibre.compose.camera.MapViewCameraDefaults
import kotlinx.parcelize.Parcelize

@Parcelize
sealed class CameraPitchRange : Parcelable {
  data object Free : CameraPitchRange()

  data class FreeWithPitch(val minimum: Double, val maximum: Double) : CameraPitchRange()

  data class Fixed(val pitch: Double) : CameraPitchRange()

  val rangeValue: Pair<Double, Double>
    get() =
        when (this) {
          is Free -> Pair(MapViewCameraDefaults.MIN_PITCH, MapViewCameraDefaults.MAX_PITCH)
          is FreeWithPitch -> Pair(minimum, maximum)
          is Fixed -> Pair(pitch, pitch)
        }

  companion object
}
