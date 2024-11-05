package com.maplibre.compose.camera

import android.os.Parcelable
import com.mapbox.mapboxsdk.geometry.LatLngBounds
import com.maplibre.compose.camera.extensions.validDirection
import com.maplibre.compose.camera.extensions.validPitch
import com.maplibre.compose.camera.extensions.validZoom
import com.maplibre.compose.camera.models.CameraMotion
import kotlinx.parcelize.Parcelize

@Parcelize
sealed class CameraState : Parcelable {
  data class Centered(
      val latitude: Double,
      val longitude: Double,
      var zoom: Double = MapViewCameraDefaults.ZOOM,
      var pitch: Double = MapViewCameraDefaults.PITCH,
      var direction: Double = MapViewCameraDefaults.DIRECTION,
      val motion: CameraMotion = MapViewCameraDefaults.MOTION
  ) : CameraState() {

    init {
      // Ensure that the zoom and pitch are within the min and max values.
      zoom = validZoom(zoom)
      pitch = validPitch(pitch)
      direction = validDirection(direction)
    }

    override fun equals(other: Any?): Boolean {
      return other is Centered &&
          latitude == other.latitude &&
          longitude == other.longitude &&
          zoom == other.zoom &&
          pitch == other.pitch &&
          direction == other.direction &&
          motion == other.motion
    }

    override fun hashCode(): Int {
      var result = this::class.hashCode()
      result = 31 * result + latitude.hashCode()
      result = 31 * result + longitude.hashCode()
      result = 31 * result + zoom.hashCode()
      result = 31 * result + pitch.hashCode()
      result = 31 * result + direction.hashCode()
      result = 31 * result + motion.hashCode()
      return result
    }
  }

  data class BoundingBox(
      val bounds: LatLngBounds,
      var pitch: Double = MapViewCameraDefaults.PITCH,
      var direction: Double = MapViewCameraDefaults.DIRECTION,
      val motion: CameraMotion = MapViewCameraDefaults.MOTION
  ) : CameraState() {
    init {
      // Ensure that the pitch and direction are within the min and max values.
      pitch = validPitch(pitch)
      direction = validDirection(direction)
    }

    override fun equals(other: Any?): Boolean {
      return other is BoundingBox &&
          bounds == other.bounds &&
          pitch == other.pitch &&
          direction == other.direction &&
          motion == other.motion
    }

    override fun hashCode(): Int {
      var result = this::class.hashCode()
      result = 31 * result + bounds.hashCode()
      result = 31 * result + pitch.hashCode()
      result = 31 * result + direction.hashCode()
      result = 31 * result + motion.hashCode()
      return result
    }
  }

  data class TrackingUserLocation(
      var zoom: Double = MapViewCameraDefaults.ZOOM,
      var pitch: Double = MapViewCameraDefaults.PITCH,
      var direction: Double = MapViewCameraDefaults.DIRECTION
  ) : CameraState() {

    init {
      // Ensure that the zoom and pitch are within the min and max values.
      zoom = validZoom(zoom)
      pitch = validPitch(pitch)
      direction = validDirection(direction)
    }

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
      var zoom: Double = MapViewCameraDefaults.ZOOM,
      var pitch: Double = MapViewCameraDefaults.PITCH
  ) : CameraState() {
    init {
      // Ensure that the zoom and pitch are within the min and max values.
      zoom = validZoom(zoom)
      pitch = validPitch(pitch)
    }

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
}
