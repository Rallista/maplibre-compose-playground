package com.maplibre.compose

import android.app.PendingIntent
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.os.Handler
import android.os.Looper
import java.lang.Exception
import java.util.Timer
import java.util.TimerTask
import org.maplibre.android.location.engine.LocationEngine
import org.maplibre.android.location.engine.LocationEngineCallback
import org.maplibre.android.location.engine.LocationEngineRequest
import org.maplibre.android.location.engine.LocationEngineResult

/**
 * A simple class that provides static location updates to a MapLibre view.
 *
 * This is not driven by a location provider (such as the Google fused client), but rather by
 * updates provided one at a time.
 *
 * Beyond the obvious use case in testing and Compose previews, this is also useful if you are doing
 * some processing of raw location data (ex: determining whether to snap locations to a road) and
 * selectively passing the updates on to the map view. You can provide a new location by setting the
 * ``lastLocation`` property.
 *
 * This class does not ever perform any authorization checks. That is the responsibility of the
 * caller.
 */
class StaticLocationEngine : LocationEngine {
  @Volatile
  var lastLocation: Location? = null
    @Synchronized set

  private var callbackTimer: Timer? = null
  private val callbacks: MutableList<Pair<Handler, LocationEngineCallback<LocationEngineResult>>> =
      mutableListOf()

  private var pendingIntentTimer: Timer? = null
  private val pendingIntents: MutableList<PendingIntent> = mutableListOf()

  override fun getLastLocation(callback: LocationEngineCallback<LocationEngineResult>) {
    val loc = lastLocation
    if (loc != null) {
      callback.onSuccess(LocationEngineResult.create(loc))
    } else {
      callback.onFailure(Exception("No location set"))
    }
  }

  override fun requestLocationUpdates(
      request: LocationEngineRequest,
      callback: LocationEngineCallback<LocationEngineResult>,
      looper: Looper?
  ) {
    // Register the callback
    callbacks.add(Pair(Handler(looper ?: Looper.getMainLooper()), callback))
    if (callbackTimer == null) {
      // If a timer isn't already running, create one
      callbackTimer =
          Timer().apply {
            scheduleAtFixedRate(
                object : TimerTask() {
                  override fun run() {
                    lastLocation?.let {
                      val result = LocationEngineResult.create(it)
                      for ((handler, callback) in callbacks) {
                        handler.post { callback.onSuccess(result) }
                      }
                    }
                  }
                },
                0,
                1000)
          }
    }
  }

  override fun requestLocationUpdates(
      request: LocationEngineRequest,
      pendingIntent: PendingIntent?
  ) {
    if (pendingIntent != null) {
      pendingIntents.add(pendingIntent)
      if (pendingIntentTimer == null) {
        // If a timer isn't already running, create one
        pendingIntentTimer =
            Timer().apply {
              scheduleAtFixedRate(
                  object : TimerTask() {
                    override fun run() {
                      lastLocation?.let {
                        for (intent in pendingIntents) {
                          val update = Intent()
                          update.putExtra(LocationManager.KEY_LOCATION_CHANGED, it)
                        }
                      }
                    }
                  },
                  0,
                  1000)
            }
      }
    }
  }

  override fun removeLocationUpdates(callback: LocationEngineCallback<LocationEngineResult>) {
    callbacks.removeIf { it.second == callback }
    if (callbacks.isEmpty()) {
      callbackTimer?.cancel()
      callbackTimer = null
    }
  }

  override fun removeLocationUpdates(intent: PendingIntent?) {
    pendingIntents.remove(intent)
    if (pendingIntents.isEmpty()) {
      pendingIntentTimer?.cancel()
      pendingIntentTimer = null
    }
  }
}
