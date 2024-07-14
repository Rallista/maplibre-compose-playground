package com.maplibre.compose.camera.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * @property Instant Move the camera instantaneously to the new position.
 * @property Ease Gradually move the camera over [animationDurationMs] milliseconds.
 * @property Fly Move the camera using a transition that evokes a powered flight over
 *   [animationDurationMs] milliseconds.
 */
@Parcelize
sealed class CameraMotion : Parcelable {
  data object Instant : CameraMotion()

  data class Ease(val animationDurationMs: Int) : CameraMotion() {
    override fun equals(other: Any?): Boolean {
      return other is Ease && animationDurationMs == other.animationDurationMs
    }

    override fun hashCode(): Int {
      return this::class.hashCode() + 31 * animationDurationMs
    }
  }

  data class Fly(val animationDurationMs: Int) : CameraMotion() {
    override fun equals(other: Any?): Boolean {
      return other is Fly && animationDurationMs == other.animationDurationMs
    }

    override fun hashCode(): Int {
      return this::class.hashCode() + 31 * animationDurationMs
    }
  }
}
