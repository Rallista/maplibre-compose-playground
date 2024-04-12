package com.maplibre.example.support

import android.app.PendingIntent
import android.location.Location
import android.os.Looper
import com.mapbox.mapboxsdk.location.engine.LocationEngine
import com.mapbox.mapboxsdk.location.engine.LocationEngineCallback
import com.mapbox.mapboxsdk.location.engine.LocationEngineRequest
import com.mapbox.mapboxsdk.location.engine.LocationEngineResult

// TODO: Port this from what I have in Ferrostar today
class StaticLocationEngine(
    private val center: Location = Location("StaticLocationEngine").apply {
        latitude = 37.7749
        longitude = -122.4194
    }
): LocationEngine {

    override fun getLastLocation(callback: LocationEngineCallback<LocationEngineResult>) {
        callback.onSuccess(LocationEngineResult.create(center))
    }

    override fun requestLocationUpdates(
        p0: LocationEngineRequest,
        p1: LocationEngineCallback<LocationEngineResult>,
        p2: Looper?
    ) {
        // No action necessary - this is not a real location engine
    }

    override fun requestLocationUpdates(p0: LocationEngineRequest, p1: PendingIntent?) {
        // No action necessary - this is not a real location engine
    }

    override fun removeLocationUpdates(p0: LocationEngineCallback<LocationEngineResult>) {
        // No action necessary - this is not a real location engine
    }

    override fun removeLocationUpdates(p0: PendingIntent?) {
        // No action necessary - this is not a real location engine
    }
}