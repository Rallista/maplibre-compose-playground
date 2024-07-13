import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import androidx.compose.runtime.MutableState
import org.maplibre.android.location.LocationComponentActivationOptions
import org.maplibre.android.location.LocationComponentOptions
import org.maplibre.android.location.engine.LocationEngine
import org.maplibre.android.location.engine.LocationEngineCallback
import org.maplibre.android.location.engine.LocationEngineDefault
import org.maplibre.android.location.engine.LocationEngineRequest
import org.maplibre.android.location.engine.LocationEngineResult
import org.maplibre.android.maps.MapLibreMap
import org.maplibre.android.maps.Style
import com.maplibre.compose.ramani.LocationRequestProperties
import com.maplibre.compose.ramani.LocationStyling

internal fun MapLibreMap.setupLocation(
    context: Context,
    style: Style,
    locationEngine: LocationEngine?,
    locationRequestProperties: LocationRequestProperties?,
    locationStyling: LocationStyling,
    userLocation: MutableState<Location>?,
) {
  if (locationEngine == null || locationRequestProperties == null) return

  val locationEngineRequest = locationRequestProperties.toMapLibre()

  val locationActivationOptions =
      LocationComponentActivationOptions.builder(context, style)
          .locationEngine(locationEngine)
          .locationComponentOptions(locationStyling.toMapLibre(context))
          .locationEngineRequest(locationEngineRequest)
          .build()

  this.locationComponent.activateLocationComponent(locationActivationOptions)

  if (isFineLocationGranted(context) || isCoarseLocationGranted(context)) {
    @SuppressLint("MissingPermission")
    this.locationComponent.isLocationComponentEnabled = true
    userLocation?.let { trackLocation(context, locationEngineRequest, userLocation) }
  } else {
    Log.w("MapLibre", "LocationEngine provided, but permission not granted")
  }
}

private fun isFineLocationGranted(context: Context): Boolean {
  return context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) ==
      PackageManager.PERMISSION_GRANTED
}

private fun isCoarseLocationGranted(context: Context): Boolean {
  return context.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) ==
      PackageManager.PERMISSION_GRANTED
}

@SuppressLint("MissingPermission")
private fun trackLocation(
    context: Context,
    locationEngineRequest: LocationEngineRequest,
    userLocation: MutableState<Location>
) {
  assert(isFineLocationGranted(context) || isCoarseLocationGranted(context))

  val locationEngine = LocationEngineDefault.getDefaultLocationEngine(context)
  locationEngine.requestLocationUpdates(
      locationEngineRequest,
      object : LocationEngineCallback<LocationEngineResult> {
        override fun onSuccess(result: LocationEngineResult?) {
          result?.lastLocation?.let { userLocation.value = it }
        }

        override fun onFailure(exception: Exception) {
          throw exception
        }
      },
      null)
}

private fun LocationStyling.toMapLibre(context: Context): LocationComponentOptions {
  val builder = LocationComponentOptions.builder(context)
  this.accuracyAlpha?.let { builder.accuracyAlpha(it) }
  this.accuracyColor?.let { builder.accuracyColor(it) }
  this.enablePulse?.let { builder.pulseEnabled(it) }
  this.enablePulseFade?.let { builder.pulseFadeEnabled(it) }
  this.pulseColor?.let { builder.pulseColor(it) }

  return builder.build()
}

private fun LocationRequestProperties.toMapLibre(): LocationEngineRequest {
  return LocationEngineRequest.Builder(this.interval)
      .setPriority(this.priority.value)
      .setFastestInterval(this.fastestInterval)
      .setDisplacement(this.displacement)
      .setMaxWaitTime(this.maxWaitTime)
      .build()
}
