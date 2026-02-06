package com.maplibre.compose.car

import androidx.car.app.AppManager
import androidx.car.app.CarContext
import androidx.car.app.Screen
import androidx.car.app.model.Template
import androidx.compose.runtime.Composable
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

abstract class ComposableScreen(
    carContext: CarContext,
    private val surfaceTag: String = "ComposableScreen",
) : Screen(carContext) {
  private val surfaceCallback: ComposeViewSurfaceCallback =
      ComposeViewSurfaceCallback(
          androidContext = carContext, surfaceTag = surfaceTag, content = { content() })

  private val appManager: AppManager = carContext.getCarService(AppManager::class.java)

  init {
    // Add lifecycle observer to handle screen visibility changes
    lifecycle.addObserver(
        object : DefaultLifecycleObserver {
          override fun onStart(owner: LifecycleOwner) {
            appManager.setSurfaceCallback(surfaceCallback)
            surfaceCallback.resume()
          }

          override fun onStop(owner: LifecycleOwner) {
            surfaceCallback.stop()
          }

          override fun onDestroy(owner: LifecycleOwner) {
            surfaceCallback.dispose()
          }
        })
  }

  /**
   * The Composable content to render on the Android Auto surface.
   *
   * This content will be rendered onto a virtual display that is presented on the Android Auto
   * screen. Use this to render your map or other Compose UI elements.
   */
  @Composable abstract fun content()

  /**
   * Returns the Template to display for this screen.
   *
   * For map-based screens, you typically want to return a
   * [androidx.car.app.navigation.model.MapWithContentTemplate] or
   * [androidx.car.app.navigation.model.NavigationTemplate]. The template defines the UI structure
   * (buttons, lists, etc.) while the [content] composable renders on the map surface.
   */
  abstract override fun onGetTemplate(): Template
}
