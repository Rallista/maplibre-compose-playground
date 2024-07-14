package com.maplibre.compose.camera.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CameraPadding(
    val start: Double = 0.0,
    val top: Double = 0.0,
    val end: Double = 0.0,
    val bottom: Double = 0.0
) : Parcelable {
  companion object {
    fun fromCameraPosition(padding: DoubleArray?): CameraPadding {
      return CameraPadding(
          start = padding?.getOrNull(0) ?: 0.0,
          top = padding?.getOrNull(1) ?: 0.0,
          end = padding?.getOrNull(2) ?: 0.0,
          bottom = padding?.getOrNull(3) ?: 0.0)
    }
  }

  fun toDoubleArray(): DoubleArray {
    return doubleArrayOf(start, top, end, bottom)
  }
}
