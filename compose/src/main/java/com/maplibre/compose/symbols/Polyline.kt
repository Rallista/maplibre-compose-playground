/*
 * This file is part of ramani-maps.
 *
 * Copyright (c) 2023 Roman Bapst & Jonas Vautherin.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.maplibre.compose.symbols

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeNode
import androidx.compose.runtime.currentComposer
import androidx.compose.ui.platform.LocalContext
import com.maplibre.compose.ramani.MapApplier
import com.maplibre.compose.ramani.MapLibreComposable
import com.maplibre.compose.ramani.PolyLineNode
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.plugins.annotation.LineOptions
import org.maplibre.android.style.layers.Property

@Composable
@MapLibreComposable
fun Polyline(
    points: List<LatLng>,
    // Either `color` or `linePatternId` should be non-null
    color: String? = null,
    lineWidth: Float,
    zIndex: Int = 0,
    isDraggable: Boolean = false,
    isDashed: Boolean = false,
    lineCap: String = Property.LINE_CAP_ROUND,
    lineJoin: String = Property.LINE_JOIN_ROUND,
    linePatternId: Int? = null
) {
  val context = LocalContext.current
  val mapApplier = currentComposer.applier as MapApplier

  mapApplier.style.addImageFromResourceId(context = context, resourceId = linePatternId)

  ComposeNode<PolyLineNode, MapApplier>(
      factory = {
        val lineManager = mapApplier.getOrCreateLineManagerForZIndex(zIndex)
        val lineOptions =
            LineOptions()
                .withLatLngs(points)
                .withLineColor(color)
                .withLineWidth(lineWidth)
                .withDraggable(isDraggable)
                .withLineJoin(lineJoin)
                .withLinePattern(linePatternId?.toString())

        lineManager.lineCap = lineCap

        if (isDashed) {
          lineManager.lineDasharray = arrayOf(1.0f, 4.0f)
        }

        val polyLine = lineManager.create(lineOptions)
        PolyLineNode(lineManager, polyLine)
      },
      update = {
        set(points) {
          polyLine.latLngs = points
          lineManager.update(polyLine)
        }

        set(color) {
          polyLine.lineColor = color
          lineManager.update(polyLine)
        }

        set(lineWidth) { polyLine.lineWidth = lineWidth }
      })
}
