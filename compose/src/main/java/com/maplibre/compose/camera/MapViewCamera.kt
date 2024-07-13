package com.maplibre.compose.camera

import android.os.Parcelable
import org.maplibre.android.geometry.LatLng
import kotlinx.parcelize.Parcelize

sealed class MapViewCameraDefaults {
  companion object {
    val zoom: Double = 10.0
    val minZoom: Double = 0.0
    val maxZoom: Double =
        24.0 // mbgl/util/constants.hpp#L37 says 25.5, but the map goes dark above 24

    val pitch: CameraPitch = CameraPitch.Free
    val direction: Double = 0.0
  }
}

// TODO: Create Defaults so all inits use the same values.
// TODO: Handle CameraPitch in/out
@Parcelize
data class MapViewCamera(
    val state: CameraState = CameraState.Centered(latitude = 0.0, longitude = 0.0),
    var zoom: Double = MapViewCameraDefaults.zoom,
    val pitch: CameraPitch = MapViewCameraDefaults.pitch,
    val direction: Double = MapViewCameraDefaults.direction
    // TODO: Last change reason (See
    // https://github.com/stadiamaps/maplibre-swiftui-dsl-playground/blob/main/Sources/MapLibreSwiftUI/Models/MapCamera/MapViewCamera.swift#L4C1-L53C2)
) : Parcelable {

  init {
    if (zoom < MapViewCameraDefaults.minZoom) {
      zoom = MapViewCameraDefaults.minZoom
    } else if (zoom > MapViewCameraDefaults.maxZoom) {
      zoom = MapViewCameraDefaults.maxZoom
    }
  }

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
        zoom: Double = MapViewCameraDefaults.zoom,
        pitch: CameraPitch = MapViewCameraDefaults.pitch,
        direction: Double = MapViewCameraDefaults.direction
    ) = MapViewCamera(CameraState.Centered(latitude, longitude), zoom, pitch, direction)

    fun TrackingUserLocation(
        zoom: Double = MapViewCameraDefaults.zoom,
        pitch: CameraPitch = MapViewCameraDefaults.pitch,
        direction: Double = MapViewCameraDefaults.direction
    ) = MapViewCamera(CameraState.TrackingUserLocation, zoom, pitch, direction)

    fun TrackingUserLocationWithBearing(
        zoom: Double = MapViewCameraDefaults.zoom,
        pitch: CameraPitch = MapViewCameraDefaults.pitch,
        direction: Double = MapViewCameraDefaults.direction
    ) = MapViewCamera(CameraState.TrackingUserLocationWithBearing, zoom, pitch, direction)

    internal fun fromCameraPosition(cameraPosition: CameraPosition): MapViewCamera {
      when (cameraPosition.trackingMode) {
        CameraTrackingMode.NONE -> {
          return Centered(
              cameraPosition.target?.latitude ?: 0.0,
              cameraPosition.target?.longitude ?: 0.0,
              cameraPosition.zoom ?: MapViewCameraDefaults.zoom,
              CameraPitch.Free, // TODO: Handle pitch from CameraPosition
              cameraPosition.bearing ?: MapViewCameraDefaults.direction)
        }
        CameraTrackingMode.FOLLOW -> {
          return TrackingUserLocation(
              cameraPosition.zoom ?: MapViewCameraDefaults.zoom,
              CameraPitch.Free, // TODO: Handle pitch from CameraPosition
              cameraPosition.bearing ?: MapViewCameraDefaults.direction)
        }
        CameraTrackingMode.FOLLOW_WITH_BEARING -> {
          return TrackingUserLocationWithBearing(
              cameraPosition.zoom ?: MapViewCameraDefaults.zoom,
              CameraPitch.Free, // TODO: Handle pitch from CameraPosition
              cameraPosition.bearing ?: MapViewCameraDefaults.direction)
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
