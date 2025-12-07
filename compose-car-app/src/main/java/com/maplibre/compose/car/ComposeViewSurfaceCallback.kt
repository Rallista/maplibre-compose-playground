package com.maplibre.compose.car

import android.app.Presentation
import android.content.Context
import android.graphics.Rect
import android.hardware.display.DisplayManager
import android.hardware.display.VirtualDisplay
import android.util.DisplayMetrics
import androidx.car.app.SurfaceCallback
import androidx.car.app.SurfaceContainer
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner

class ComposeViewSurfaceCallback(
    private val androidContext: Context,
    private val content: @Composable () -> Unit,
) : SurfaceCallback {

  private val displayMetrics: DisplayMetrics = androidContext.resources.displayMetrics
  private val displayManager: DisplayManager =
      androidContext.getSystemService(DisplayManager::class.java)

  private var virtualDisplay: VirtualDisplay? = null
  private var presentation: Presentation? = null
  private var isLifecycleInitialized = false

  private val lifecycleOwner = ComposeViewLifecycleOwner()
  private val composeView =
      ComposeView(androidContext).apply {
        setViewTreeLifecycleOwner(lifecycleOwner)
        setViewTreeSavedStateRegistryOwner(lifecycleOwner)
        setContent { content.invoke() }
      }

  override fun onSurfaceAvailable(surfaceContainer: SurfaceContainer) {
    // Create virtual display with the surface from Android Auto
    virtualDisplay =
        displayManager.createVirtualDisplay(
            "ComposeViewVirtualDisplay",
            surfaceContainer.width,
            surfaceContainer.height,
            displayMetrics.densityDpi,
            surfaceContainer.surface,
            0)

    // Remove ComposeView from its parent if it has one
    composeView.parent?.let { parent ->
      (parent as? android.view.ViewGroup)?.removeView(composeView)
    }

    // Create presentation on the virtual display
    presentation =
        Presentation(androidContext, virtualDisplay!!.display).apply { setContentView(composeView) }

    // Only initialize lifecycle on first call
    if (!isLifecycleInitialized) {
      lifecycleOwner.performRestore(null)
      lifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
      isLifecycleInitialized = true
    }

    lifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_START)
    lifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)

    presentation?.show()
  }

  override fun onSurfaceDestroyed(surfaceContainer: SurfaceContainer) {
    presentation?.dismiss()
    lifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    lifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_STOP)
    // Don't destroy lifecycle to allow reuse on next onSurfaceAvailable

    virtualDisplay?.release()
    virtualDisplay = null
    presentation = null
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
