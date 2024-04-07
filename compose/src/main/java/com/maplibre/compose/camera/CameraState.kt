package com.maplibre.compose.camera

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
sealed class CameraState: Parcelable {
    data class Centered(val latitude: Double, val longitude: Double) : CameraState()
    data object TrackingUserLocation : CameraState()
    data object TrackingUserLocationWithBearing : CameraState()
    // TODO: Bounding box & showcase

    override fun toString(): String {
        return when (this) {
            is Centered -> "Centered(latitude=$latitude, longitude=$longitude)"
            is TrackingUserLocation -> "TrackingUserLocation"
            is TrackingUserLocationWithBearing -> "TrackingUserLocationWithBearing"
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CameraState

        when (this) {
            is Centered -> {
                when (other) {
                    !is Centered -> return false
                    else -> {
                        if (latitude != other.latitude) return false
                        if (longitude != other.longitude) return false
                    }
                }
            }
            is TrackingUserLocation -> {
                if (other !is TrackingUserLocation) return false
            }
            is TrackingUserLocationWithBearing -> {
                if (other !is TrackingUserLocationWithBearing) return false
            }
        }

        return true
    }

    override fun hashCode(): Int {
        var result = 0
        when (this) {
            is Centered -> {
                result = 31 * result + latitude.hashCode()
                result = 31 * result + longitude.hashCode()
            }
            is TrackingUserLocation -> {
                result = 31 * result + 1
            }
            is TrackingUserLocationWithBearing -> {
                result = 31 * result + 2
            }
        }
        return result
    }
}
