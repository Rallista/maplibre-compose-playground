package com.maplibre.compose.car

import androidx.car.app.AppManager
import androidx.car.app.CarContext
import androidx.car.app.Screen
import androidx.car.app.model.Template
import androidx.compose.runtime.Composable

abstract class ComposableScreen(carContext: CarContext) : Screen(carContext) {

  private val surfaceCallback: ComposeViewSurfaceCallback =
      ComposeViewSurfaceCallback(androidContext = carContext, content = { content() })

  init {
    carContext.getCarService(AppManager::class.java).setSurfaceCallback(surfaceCallback)
  }

  @Composable abstract fun content()

  abstract override fun onGetTemplate(): Template
}
