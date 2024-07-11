package com.maplibre.compose.camera

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

// TODO: Discuss this being operated in the camera, not map properties.
@Parcelize
sealed class CameraPitch : Parcelable {
  object Free : CameraPitch()

  data class FreeWithPitch(val minimum: Double, val maximum: Double) : CameraPitch()

  data class Fixed(val pitch: Double) : CameraPitch()

  val rangeValue: Pair<Double, Double>
    get() =
        when (this) {
          is Free -> Pair(0.0, 60.0)
          is FreeWithPitch -> Pair(minimum, maximum)
          is Fixed -> Pair(pitch, pitch)
        }

  override fun toString(): String {
    return when (this) {
      is Free -> "Free"
      is FreeWithPitch -> "FreeWithPitch(minimum=$minimum, maximum=$maximum)"
      is Fixed -> "Fixed(pitch=$pitch)"
    }
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as CameraPitch

    when (this) {
      is Free -> {
        if (other !is Free) return false
      }
      is FreeWithPitch -> {
        when (other) {
          !is FreeWithPitch -> return false
          else -> {
            if (minimum != other.minimum) return false
            if (maximum != other.maximum) return false
          }
        }
      }
      is Fixed -> {
        if (other !is Fixed) return false
        if (pitch != other.pitch) return false
      }
    }

    return true
  }

  override fun hashCode(): Int {
    var result = 0
    when (this) {
      is Free -> {
        result = 31 * result + 0
      }
      is FreeWithPitch -> {
        result = 31 * result + minimum.hashCode()
        result = 31 * result + maximum.hashCode()
      }
      is Fixed -> {
        result = 31 * result + pitch.hashCode()
      }
    }
    return result
  }
}
