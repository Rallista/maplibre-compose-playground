package com.maplibre.auto

import android.content.Intent
import androidx.car.app.CarAppService
import androidx.car.app.Screen
import androidx.car.app.Session
import androidx.car.app.SessionInfo
import androidx.car.app.validation.HostValidator

class ExampleCarAppService : CarAppService() {
  override fun createHostValidator(): HostValidator {
    // For development - in production use HostValidator.Builder() with specific hosts
    return HostValidator.ALLOW_ALL_HOSTS_VALIDATOR
  }

  override fun onCreateSession(sessionInfo: SessionInfo): Session {
    return ExampleSession()
  }
}

class ExampleSession : Session() {
  override fun onCreateScreen(intent: Intent): Screen {
    return ExampleMapScreen(carContext)
  }
}
