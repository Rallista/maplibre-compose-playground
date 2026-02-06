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
    private val surfaceTag: String = "ComposeViewSurfaceCallback",
    private val onVisibleAreaChanged: ((Rect) -> Unit)? = null,
    private val onStableAreaChanged: ((Rect) -> Unit)? = null,
    private val onScroll: ((distanceX: Float, distanceY: Float) -> Unit)? = null,
    private val onFling: ((velocityX: Float, velocityY: Float) -> Unit)? = null,
    private val onScale: ((focusX: Float, focusY: Float, scaleFactor: Float) -> Unit)? = null,
    private val content: @Composable () -> Unit,
) : SurfaceCallback {

  private val displayMetrics: DisplayMetrics = androidContext.resources.displayMetrics
  private val displayManager: DisplayManager =
      androidContext.getSystemService(DisplayManager::class.java)

  private var virtualDisplay: VirtualDisplay? = null
  private var presentation: Presentation? = null
  private var isLifecycleInitialized = false
  private var isDisposed = false

  private val lifecycleOwner = ComposeViewLifecycleOwner()
  private val composeView =
      ComposeView(androidContext).apply {
        setViewTreeLifecycleOwner(lifecycleOwner)
        setViewTreeSavedStateRegistryOwner(lifecycleOwner)
        setContent { content.invoke() }
      }

  override fun onSurfaceAvailable(surfaceContainer: SurfaceContainer) {
    if (isDisposed) {
      return
    }

    // Create virtual display with the surface from Android Auto
    virtualDisplay =
        displayManager.createVirtualDisplay(
            "ComposeViewVirtualDisplay-$surfaceTag",
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
    cleanupSurface()
  }

  private fun cleanupSurface() {
    presentation?.dismiss()
    presentation = null

    if (isLifecycleInitialized && !isDisposed) {
      lifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE)
      lifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_STOP)
    }
    // Don't destroy lifecycle to allow reuse on next onSurfaceAvailable

    virtualDisplay?.release()
    virtualDisplay = null
  }

  fun dispose() {
    if (isDisposed) {
      return
    }
    isDisposed = true

    cleanupSurface()

    if (isLifecycleInitialized) {
      lifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    }
  }

  fun resume() {
    if (isDisposed || !isLifecycleInitialized) {
      return
    }
    lifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
  }

  fun stop() {
    if (isDisposed) {
      return
    }
    cleanupSurface()
  }

  override fun onVisibleAreaChanged(visibleArea: Rect) {
    super.onVisibleAreaChanged(visibleArea)
    onVisibleAreaChanged?.invoke(visibleArea)
  }

  override fun onStableAreaChanged(stableArea: Rect) {
    super.onStableAreaChanged(stableArea)
    onStableAreaChanged?.invoke(stableArea)
  }

  override fun onScroll(distanceX: Float, distanceY: Float) {
    super.onScroll(distanceX, distanceY)
    onScroll?.invoke(distanceX, distanceY)
  }

  override fun onFling(velocityX: Float, velocityY: Float) {
    super.onFling(velocityX, velocityY)
    onFling?.invoke(velocityX, velocityY)
  }

  override fun onScale(focusX: Float, focusY: Float, scaleFactor: Float) {
    super.onScale(focusX, focusY, scaleFactor)
    onScale?.invoke(focusX, focusY, scaleFactor)
  }
}
