package com.maplibre.compose.camera

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
sealed class CameraState : Parcelable {
  data class Centered(
      val latitude: Double,
      val longitude: Double,
      val zoom: Double = MapViewCameraDefaults.ZOOM,
      val pitch: Double = MapViewCameraDefaults.PITCH,
      val direction: Double = MapViewCameraDefaults.DIRECTION
  ) : CameraState() {
    override fun equals(other: Any?): Boolean {
      return other is Centered &&
          latitude == other.latitude &&
          longitude == other.longitude &&
          zoom == other.zoom &&
          pitch == other.pitch &&
          direction == other.direction
    }

    override fun hashCode(): Int {
      var result = this::class.hashCode()
      result = 31 * result + latitude.hashCode()
      result = 31 * result + longitude.hashCode()
      result = 31 * result + zoom.hashCode()
      result = 31 * result + pitch.hashCode()
      result = 31 * result + direction.hashCode()
      return result
    }
  }

  data class TrackingUserLocation(
      val zoom: Double = MapViewCameraDefaults.ZOOM,
      val pitch: Double = MapViewCameraDefaults.PITCH,
      val direction: Double = MapViewCameraDefaults.DIRECTION
  ) : CameraState() {
    override fun equals(other: Any?): Boolean {
      return other is TrackingUserLocation &&
          zoom == other.zoom &&
          pitch == other.pitch &&
          direction == other.direction
    }

    override fun hashCode(): Int {
      var result = this::class.hashCode()
      result = 31 * result + zoom.hashCode()
      result = 31 * result + pitch.hashCode()
      result = 31 * result + direction.hashCode()
      return result
    }
  }

  data class TrackingUserLocationWithBearing(
      val zoom: Double = MapViewCameraDefaults.ZOOM,
      val pitch: Double = MapViewCameraDefaults.PITCH
  ) : CameraState() {
    override fun equals(other: Any?): Boolean {
      return other is TrackingUserLocationWithBearing && zoom == other.zoom && pitch == other.pitch
    }

    override fun hashCode(): Int {
      var result = this::class.hashCode()
      result = 31 * result + zoom.hashCode()
      result = 31 * result + pitch.hashCode()
      return result
    }
  }

  // TODO: Bounding box & showcase
}
