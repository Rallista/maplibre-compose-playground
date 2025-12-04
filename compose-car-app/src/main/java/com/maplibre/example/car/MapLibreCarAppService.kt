package com.maplibre.example.car

import android.content.Intent
import androidx.car.app.CarAppService
import androidx.car.app.Screen
import androidx.car.app.Session
import androidx.car.app.validation.HostValidator

/**
 * CarAppService that provides MapLibre navigation functionality for Android Auto.
 *
 * This service demonstrates how to use the MapLibre Compose library with Android Auto using the
 * high-performance VirtualDisplay/Presentation rendering approach.
 */
class MapLibreCarAppService : CarAppService() {

  override fun createHostValidator(): HostValidator {
    // Allow all hosts for development. In production, you should validate specific hosts.
    return HostValidator.ALLOW_ALL_HOSTS_VALIDATOR
  }

  override fun onCreateSession(): Session {
    return MapLibreCarSession()
  }
}

/** Session for the MapLibre Car App. */
class MapLibreCarSession : Session() {

  override fun onCreateScreen(intent: Intent): Screen {
    return MapLibreCarScreen(carContext)
  }
}
