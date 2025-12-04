/*
 * This file is part of ramani-maps.
 *
 * Copyright (c) 2023 Roman Bapst & Jonas Vautherin.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.maplibre.compose.ramani

import androidx.compose.runtime.AbstractApplier
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Composition
import androidx.compose.runtime.CompositionContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.math.abs
import kotlinx.coroutines.awaitCancellation
import org.maplibre.android.gestures.MoveGestureDetector
import org.maplibre.android.gestures.RotateGestureDetector
import org.maplibre.android.gestures.StandardScaleGestureDetector
import org.maplibre.android.maps.MapLibreMap
import org.maplibre.android.maps.MapView
import org.maplibre.android.maps.Style
import org.maplibre.android.plugins.annotation.AnnotationManager
import org.maplibre.android.plugins.annotation.Circle
import org.maplibre.android.plugins.annotation.CircleManager
import org.maplibre.android.plugins.annotation.Fill
import org.maplibre.android.plugins.annotation.FillManager
import org.maplibre.android.plugins.annotation.Line
import org.maplibre.android.plugins.annotation.LineManager
import org.maplibre.android.plugins.annotation.OnCircleClickListener
import org.maplibre.android.plugins.annotation.OnCircleDragListener
import org.maplibre.android.plugins.annotation.OnCircleLongClickListener
import org.maplibre.android.plugins.annotation.OnSymbolClickListener
import org.maplibre.android.plugins.annotation.OnSymbolLongClickListener
import org.maplibre.android.plugins.annotation.Symbol
import org.maplibre.android.plugins.annotation.SymbolManager

internal suspend inline fun disposingComposition(factory: () -> Composition) {
  val composition = factory()
  try {
    awaitCancellation()
  } finally {
    composition.dispose()
  }
}

/**
 * Creates a new composition for the MapView with the given parent context and content. This is used
 * by Android Auto integration and can be used by other external integrations.
 */
suspend fun MapView.newComposition(
    parent: CompositionContext,
    style: Style,
    content: @Composable () -> Unit,
): Composition {
  val map = awaitMap()
  return Composition(MapApplier(map, this, style), parent).apply { setContent(content) }
}

internal suspend fun MapLibreMap.awaitStyle(styleUrl: String) = suspendCoroutine { continuation ->
  setStyle(styleUrl) { style -> continuation.resume(style) }
}

internal interface MapNode {
  fun onAttached() {}

  fun onRemoved() {}

  fun onCleared() {}
}

private object MapNodeRoot : MapNode

internal class MapApplier(val map: MapLibreMap, val mapView: MapView, val style: Style) :
    AbstractApplier<MapNode>(MapNodeRoot) {
  private val decorations = mutableListOf<MapNode>()

  private val circleManagerMap = mutableMapOf<Int, CircleManager>()
  private val fillManagerMap = mutableMapOf<Int, FillManager>()
  private val symbolManagerMap = mutableMapOf<Int, SymbolManager>()
  private val lineManagerMap = mutableMapOf<Int, LineManager>()

  private val zIndexReferenceAnnotationManagerMap =
      mutableMapOf<Int, AnnotationManager<*, *, *, *, *, *>>()

  init {
    attachMapListeners()
  }

  private fun attachMapListeners() {
    map.addOnScaleListener(
        object : MapLibreMap.OnScaleListener {
          override fun onScaleBegin(detector: StandardScaleGestureDetector) {
            decorations.filterIsInstance<MapObserverNode>().forEach { it.onMapScaled() }
          }

          override fun onScale(detector: StandardScaleGestureDetector) {
            decorations.filterIsInstance<MapObserverNode>().forEach { it.onMapScaled() }
          }

          override fun onScaleEnd(detector: StandardScaleGestureDetector) {}
        })

    map.addOnMoveListener(
        object : MapLibreMap.OnMoveListener {
          override fun onMoveBegin(detector: MoveGestureDetector) {
            decorations.filterIsInstance<MapObserverNode>().forEach { it.onMapMoved() }
          }

          override fun onMove(detector: MoveGestureDetector) {
            decorations.filterIsInstance<MapObserverNode>().forEach { it.onMapMoved() }
          }

          override fun onMoveEnd(detector: MoveGestureDetector) {}
        })

    map.addOnRotateListener(
        object : MapLibreMap.OnRotateListener {
          override fun onRotateBegin(detector: RotateGestureDetector) {
            decorations.filterIsInstance<MapObserverNode>().forEach {
              it.onMapRotated(map.cameraPosition.bearing)
            }
          }

          override fun onRotate(detector: RotateGestureDetector) {
            decorations.filterIsInstance<MapObserverNode>().forEach {
              it.onMapRotated(map.cameraPosition.bearing)
            }
          }

          override fun onRotateEnd(detector: RotateGestureDetector) {
            decorations.filterIsInstance<MapObserverNode>().forEach {
              it.onMapRotated(map.cameraPosition.bearing)
            }
          }
        })
  }

  fun getOrCreateCircleManagerForZIndex(zIndex: Int): CircleManager {
    circleManagerMap[zIndex]?.let {
      return it
    }

    val layerInsertInfo = getLayerInsertInfoForZIndex(zIndex)

    val circleManager =
        layerInsertInfo?.let {
          CircleManager(
              mapView,
              map,
              style,
              if (it.insertPosition == LayerInsertMethod.INSERT_BELOW) it.referenceLayerId
              else null,
              if (it.insertPosition == LayerInsertMethod.INSERT_ABOVE) it.referenceLayerId
              else null)
        }
            ?: run {
              CircleManager(mapView, map, style, "mapbox-location-pulsing-circle-layer", null)
            }

    circleManagerMap[zIndex] = circleManager

    if (!zIndexReferenceAnnotationManagerMap.containsKey(zIndex)) {
      zIndexReferenceAnnotationManagerMap[zIndex] = circleManager
    }

    circleManager.addClickListener(
        OnCircleClickListener { annotation ->
          decorations
              .findInputCallback<CircleNode, Circle, Unit>(
                  nodeMatchPredicate = {
                    it.circle.id == annotation?.id &&
                        it.circleManager.layerId == circleManager.layerId
                  },
                  nodeInputCallback = { onTap })
              ?.invoke(annotation!!)
          true
        })

    circleManager.addLongClickListener(
        OnCircleLongClickListener { annotation ->
          decorations
              .findInputCallback<CircleNode, Circle, Unit>(
                  nodeMatchPredicate = {
                    it.circle.id == annotation?.id &&
                        it.circleManager.layerId == circleManager.layerId
                  },
                  nodeInputCallback = { onLongPress })
              ?.invoke(annotation!!)
          true
        })

    circleManager.addDragListener(
        object : OnCircleDragListener {
          override fun onAnnotationDragStarted(annotation: Circle?) {
            decorations
                .findInputCallback<CircleNode, Circle, Unit>(
                    nodeMatchPredicate = {
                      it.circle.id == annotation?.id &&
                          it.circleManager.layerId == circleManager.layerId
                    },
                    nodeInputCallback = { onCircleDragged })
                ?.invoke(annotation!!)
          }

          override fun onAnnotationDrag(annotation: Circle?) {
            decorations
                .findInputCallback<CircleNode, Circle, Unit>(
                    nodeMatchPredicate = {
                      it.circle.id == annotation?.id &&
                          it.circleManager.layerId == circleManager.layerId
                    },
                    nodeInputCallback = { onCircleDragged })
                ?.invoke(annotation!!)
          }

          override fun onAnnotationDragFinished(annotation: Circle?) {
            decorations
                .findInputCallback<CircleNode, Circle, Unit>(
                    nodeMatchPredicate = {
                      it.circle.id == annotation?.id &&
                          it.circleManager.layerId == circleManager.layerId
                    },
                    nodeInputCallback = { onCircleDragStopped })
                ?.invoke(annotation!!)
          }
        })

    return circleManagerMap[zIndex]!!
  }

  private fun getLayerInsertInfoForZIndex(zIndex: Int): LayerInsertInfo? {
    val keys = zIndexReferenceAnnotationManagerMap.keys.sorted()

    if (keys.isEmpty()) {
      return null
    }

    val closestLayerIndex = keys.map { abs(it - zIndex) }.withIndex().minBy { it.value }.index

    return LayerInsertInfo(
        zIndexReferenceAnnotationManagerMap[keys[closestLayerIndex]]?.layerId!!,
        if (zIndex > keys[closestLayerIndex]) LayerInsertMethod.INSERT_ABOVE
        else LayerInsertMethod.INSERT_BELOW)
  }

  fun getOrCreateSymbolManagerForZIndex(zIndex: Int): SymbolManager {
    symbolManagerMap[zIndex]?.let {
      return it
    }
    val layerInsertInfo = getLayerInsertInfoForZIndex(zIndex)

    val symbolManager =
        layerInsertInfo?.let {
          SymbolManager(
              mapView,
              map,
              style,
              if (it.insertPosition == LayerInsertMethod.INSERT_BELOW) it.referenceLayerId
              else null,
              if (it.insertPosition == LayerInsertMethod.INSERT_ABOVE) it.referenceLayerId
              else null,
              null)
        }
            ?: run {
              SymbolManager(mapView, map, style, "mapbox-location-pulsing-circle-layer", null)
            }

    symbolManager.iconAllowOverlap = true

    if (!zIndexReferenceAnnotationManagerMap.containsKey(zIndex)) {
      zIndexReferenceAnnotationManagerMap[zIndex] = symbolManager
    }

    symbolManager.addClickListener(
        OnSymbolClickListener { annotation ->
          decorations
              .findInputCallback<SymbolNode, Symbol, Unit>(
                  nodeMatchPredicate = {
                    it.symbol.id == annotation?.id &&
                        it.symbolManager.layerId == symbolManager.layerId
                  },
                  nodeInputCallback = { onTap })
              ?.invoke(annotation!!)
          true
        })

    symbolManager.addLongClickListener(
        OnSymbolLongClickListener { annotation ->
          decorations
              .findInputCallback<SymbolNode, Symbol, Unit>(
                  nodeMatchPredicate = {
                    it.symbol.id == annotation?.id &&
                        it.symbolManager.layerId == symbolManager.layerId
                  },
                  nodeInputCallback = { onLongPress })
              ?.invoke(annotation!!)
          true
        })

    symbolManagerMap[zIndex] = symbolManager
    return symbolManager
  }

  fun getOrCreateFillManagerForZIndex(zIndex: Int): FillManager {
    fillManagerMap[zIndex]?.let {
      return it
    }
    val layerInsertInfo = getLayerInsertInfoForZIndex(zIndex)
    val fillManager =
        layerInsertInfo?.let {
          FillManager(
              mapView,
              map,
              style,
              if (it.insertPosition == LayerInsertMethod.INSERT_BELOW) it.referenceLayerId
              else null,
              if (it.insertPosition == LayerInsertMethod.INSERT_ABOVE) it.referenceLayerId
              else null,
              null)
        } ?: run { FillManager(mapView, map, style, "mapbox-location-pulsing-circle-layer", null) }

    if (!zIndexReferenceAnnotationManagerMap.containsKey(zIndex)) {
      zIndexReferenceAnnotationManagerMap[zIndex] = fillManager
    }

    fillManagerMap[zIndex] = fillManager
    return fillManager
  }

  fun getOrCreateLineManagerForZIndex(zIndex: Int): LineManager {
    lineManagerMap[zIndex]?.let {
      return it
    }
    val layerInsertInfo = getLayerInsertInfoForZIndex(zIndex)
    val lineManager =
        layerInsertInfo?.let {
          LineManager(
              mapView,
              map,
              style,
              if (it.insertPosition == LayerInsertMethod.INSERT_BELOW) it.referenceLayerId
              else null,
              if (it.insertPosition == LayerInsertMethod.INSERT_ABOVE) it.referenceLayerId
              else null,
              null)
        } ?: run { LineManager(mapView, map, style, "mapbox-location-pulsing-circle-layer", null) }

    if (!zIndexReferenceAnnotationManagerMap.containsKey(zIndex)) {
      zIndexReferenceAnnotationManagerMap[zIndex] = lineManager
    }

    lineManagerMap[zIndex] = lineManager
    return lineManager
  }

  override fun insertBottomUp(index: Int, instance: MapNode) {
    // TODO: implement properly
  }

  override fun insertTopDown(index: Int, instance: MapNode) {
    decorations.add(index, instance)
    instance.onAttached()
  }

  override fun move(from: Int, to: Int, count: Int) {}

  override fun onClear() {
    decorations.forEach { it.onCleared() }
    decorations.clear()
  }

  override fun remove(index: Int, count: Int) {
    val toRemove = decorations.subList(index, index + count)
    toRemove.forEach { it.onRemoved() }
    toRemove.clear()
  }
}

data class LayerInsertInfo(val referenceLayerId: String, val insertPosition: LayerInsertMethod)

enum class LayerInsertMethod {
  INSERT_BELOW,
  INSERT_ABOVE
}

internal class CircleNode(
    val circleManager: CircleManager,
    val circle: Circle,
    var onCircleDragged: (Circle) -> Unit,
    var onCircleDragStopped: (Circle) -> Unit,
    var onTap: (Circle) -> Unit,
    var onLongPress: (Circle) -> Unit,
) : MapNode {
  override fun onRemoved() {
    circleManager.delete(circle)
  }

  override fun onCleared() {
    circleManager.delete(circle)
  }
}

internal class SymbolNode(
    val symbolManager: SymbolManager,
    val symbol: Symbol,
    val onTap: (Symbol) -> Unit,
    val onLongPress: (Symbol) -> Unit,
) : MapNode {
  override fun onRemoved() {
    symbolManager.delete(symbol)
  }

  override fun onCleared() {
    symbolManager.delete(symbol)
  }
}

internal class PolyLineNode(
    val lineManager: LineManager,
    val polyLine: Line,
) : MapNode {
  override fun onRemoved() {
    lineManager.delete(polyLine)
  }

  override fun onCleared() {
    lineManager.delete(polyLine)
  }
}

internal class FillNode(
    val fillManager: FillManager,
    val fill: Fill,
) : MapNode {
  override fun onRemoved() {
    fillManager.delete(fill)
  }

  override fun onCleared() {
    fillManager.delete(fill)
  }
}

internal class MapObserverNode(
    var onMapMoved: () -> Unit,
    var onMapScaled: () -> Unit,
    var onMapRotated: (Double) -> Unit,
) : MapNode {
  override fun onRemoved() {}
}

private inline fun <reified NodeT : MapNode, I, O> Iterable<MapNode>.findInputCallback(
    nodeMatchPredicate: (NodeT) -> Boolean,
    nodeInputCallback: NodeT.() -> ((I) -> O)?,
): ((I) -> O)? {
  val callback: ((I) -> O)? = null
  for (item in this) {
    if (item is NodeT && nodeMatchPredicate(item)) {
      // Found a matching node
      return nodeInputCallback(item)
    }
  }
  return callback
}
