package com.maplibre.compose.surface

import android.graphics.Rect

interface SurfaceGestureCallback {

  fun onVisibleAreaChanged(visibleArea: Rect) {}

  fun onStableAreaChanged(stableArea: Rect) {}

  fun onScroll(distanceX: Float, distanceY: Float) {}

  fun onFling(velocityX: Float, velocityY: Float) {}

  fun onScale(focusX: Float, focusY: Float, scaleFactor: Float) {}
}
