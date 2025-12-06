package com.maplibre.compose.car

import android.app.Presentation
import android.content.Context
import android.graphics.Rect
import android.hardware.display.DisplayManager
import android.hardware.display.VirtualDisplay
import android.os.Bundle
import android.util.DisplayMetrics
import androidx.car.app.SurfaceCallback
import androidx.car.app.SurfaceContainer
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.savedstate.SavedStateRegistryController
import androidx.savedstate.SavedStateRegistryOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner

class ComposeViewSurfaceCallback(
  context: Context,
  private val content: @Composable () -> Unit,
) : SurfaceCallback {

  val displayMetrics: DisplayMetrics = context.resources.displayMetrics
  val displayManager: DisplayManager = context.getSystemService(DisplayManager::class.java)

  val virtualDisplay: VirtualDisplay = displayManager.createVirtualDisplay(
    "ComposeViewVirtualDisplay",
    displayMetrics.widthPixels,
    displayMetrics.heightPixels,
    displayMetrics.densityDpi,
    null,
    0
  )

  val lifecycleOwner = ComposeViewLifecycleOwner()
  val presentation = Presentation(context, virtualDisplay.display)
  val composeView = ComposeView(context).apply {
    setViewTreeLifecycleOwner(lifecycleOwner)
    setViewTreeSavedStateRegistryOwner(lifecycleOwner)
    setContent {
      content.invoke()
    }
  }

  override fun onSurfaceAvailable(surfaceContainer: SurfaceContainer) {
    presentation.setContentView(composeView)

    lifecycleOwner.performRestore(null)
    lifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
    lifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_START)
    lifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)

    presentation.show()
  }

  override fun onSurfaceDestroyed(surfaceContainer: SurfaceContainer) {
    presentation.dismiss()
    lifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    lifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_STOP)
    lifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
  }

  override fun onVisibleAreaChanged(visibleArea: Rect) {
    super.onVisibleAreaChanged(visibleArea)
    // TODO: Evaluate visible area change behavior on screen
  }

  override fun onStableAreaChanged(stableArea: Rect) {
    super.onStableAreaChanged(stableArea)
    // TODO: Evaluate stable area change behavior on screen
  }

  override fun onScroll(distanceX: Float, distanceY: Float) {
    super.onScroll(distanceX, distanceY)
    // TODO: Evaluate scroll behavior on screen
  }

  override fun onFling(velocityX: Float, velocityY: Float) {
    super.onFling(velocityX, velocityY)
    // TODO: Evaluate fling behavior on screen
  }

  override fun onScale(focusX: Float, focusY: Float, scaleFactor: Float) {
    super.onScale(focusX, focusY, scaleFactor)
    // TODO: Evaluate scale behavior on screen
  }
}

class ComposeViewLifecycleOwner : SavedStateRegistryOwner {
  private var lifecycleRegistry: LifecycleRegistry = LifecycleRegistry(this)
  private var savedStateRegistryController: SavedStateRegistryController = SavedStateRegistryController.Companion.create(this)

  override val lifecycle: Lifecycle = lifecycleRegistry
  override val savedStateRegistry = savedStateRegistryController.savedStateRegistry

  fun handleLifecycleEvent(event: Lifecycle.Event) {
    lifecycleRegistry.handleLifecycleEvent(event)
  }

  fun performRestore(savedState: Bundle?) {
    savedStateRegistryController.performRestore(savedState)
  }
}