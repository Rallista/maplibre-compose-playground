package com.maplibre.compose.camera

import android.os.Parcelable
import com.mapbox.mapboxsdk.geometry.LatLng
import kotlinx.parcelize.Parcelize

// TODO: Create Defaults so all inits use the same values.
// TODO: Handle CameraPitch in/out
@Parcelize
data class MapViewCamera(
    val state: CameraState = CameraState.Centered(latitude = 0.0, longitude = 0.0),
    val zoom: Double = 10.0,
    val pitch: CameraPitch = CameraPitch.Free,
    val direction: Double = 0.0
    // TODO: Last change reason (See
    // https://github.com/stadiamaps/maplibre-swiftui-dsl-playground/blob/main/Sources/MapLibreSwiftUI/Models/MapCamera/MapViewCamera.swift#L4C1-L53C2)
) : Parcelable {

  override fun toString(): String {
    return "MapViewCamera(state=$state, zoom=$zoom, pitch=$pitch, direction=$direction)"
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as MapViewCamera

    if (state != other.state) return false
    if (zoom != other.zoom) return false
    if (pitch != other.pitch) return false
    if (direction != other.direction) return false
    return true
  }

  override fun hashCode(): Int {
    var result = state.hashCode()
    result = 31 * result + zoom.hashCode()
    result = 31 * result + pitch.hashCode()
    result = 31 * result + direction.hashCode()
    return result
  }

  companion object {
    val Default = MapViewCamera()

    fun Centered(
        latitude: Double,
        longitude: Double,
        zoom: Double = 10.0,
        pitch: CameraPitch = CameraPitch.Free,
        direction: Double = 0.0
    ) = MapViewCamera(CameraState.Centered(latitude, longitude), zoom, pitch, direction)

    fun TrackingUserLocation(
        zoom: Double = 10.0,
        pitch: CameraPitch = CameraPitch.Free,
        direction: Double = 0.0
    ) = MapViewCamera(CameraState.TrackingUserLocation, zoom, pitch, direction)

    fun TrackingUserLocationWithBearing(
        zoom: Double = 10.0,
        pitch: CameraPitch = CameraPitch.Free,
        direction: Double = 0.0
    ) = MapViewCamera(CameraState.TrackingUserLocationWithBearing, zoom, pitch, direction)

    internal fun fromCameraPosition(cameraPosition: CameraPosition): MapViewCamera {
      when (cameraPosition.trackingMode) {
        CameraTrackingMode.NONE -> {
          return Centered(
              cameraPosition.target?.latitude ?: 0.0,
              cameraPosition.target?.longitude ?: 0.0,
              cameraPosition.zoom ?: 10.0,
              CameraPitch.Free,
              cameraPosition.bearing ?: 0.0)
        }
        CameraTrackingMode.FOLLOW -> {
          return TrackingUserLocation(
              cameraPosition.zoom ?: 10.0, CameraPitch.Free, cameraPosition.bearing ?: 0.0)
        }
        CameraTrackingMode.FOLLOW_WITH_BEARING -> {
          return TrackingUserLocationWithBearing(
              cameraPosition.zoom ?: 10.0, CameraPitch.Free, cameraPosition.bearing ?: 0.0)
        }
      }
    }
  }

  fun toCameraPosition(): CameraPosition {
    when (state) {
      is CameraState.Centered -> {
        return CameraPosition(
            target = LatLng(state.latitude, state.longitude),
            zoom = zoom,
            pitch = pitch,
            bearing = direction,
            trackingMode = CameraTrackingMode.NONE)
      }
      is CameraState.TrackingUserLocation -> {
        return CameraPosition(
            zoom = zoom,
            pitch = pitch,
            bearing = direction,
            trackingMode = CameraTrackingMode.FOLLOW)
      }
      is CameraState.TrackingUserLocationWithBearing -> {
        return CameraPosition(
            zoom = zoom,
            pitch = pitch,
            bearing = direction,
            trackingMode = CameraTrackingMode.FOLLOW_WITH_BEARING)
      }
      else -> return CameraPosition()
    }
  }
}
