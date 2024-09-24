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
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions
import com.mapbox.mapboxsdk.style.layers.Property.ICON_ANCHOR_CENTER
import com.maplibre.compose.ramani.MapApplier
import com.maplibre.compose.ramani.MapLibreComposable
import com.maplibre.compose.ramani.SymbolNode
import com.maplibre.compose.symbols.builder.SymbolText
import com.maplibre.compose.symbols.models.SymbolOffset
import com.maplibre.compose.symbols.models.toMaplibreOffset

@Composable
@MapLibreComposable
fun Symbol(
    center: LatLng,
    size: Float = 1f,
    color: String = "#000000",
    isDraggable: Boolean = false,
    zIndex: Int = 0,
    imageId: Int? = null,
    imageRotation: Float? = null,
    imageAnchor: String = ICON_ANCHOR_CENTER,
    imageOffset: SymbolOffset = SymbolOffset(),
    text: SymbolText? = null,
    onTap: () -> Unit = {},
    onLongPress: () -> Unit = {}
) {
  val context = LocalContext.current
  val mapApplier = currentComposer.applier as MapApplier

  mapApplier.style.addImageFromResourceId(context = context, resourceId = imageId)

  ComposeNode<SymbolNode, MapApplier>(
      factory = {
        val symbolManager = mapApplier.getOrCreateSymbolManagerForZIndex(zIndex)
        var symbolOptions = SymbolOptions().withDraggable(isDraggable).withLatLng(center)

        imageId?.let {
          symbolOptions =
              symbolOptions
                  .withIconImage(imageId.toString())
                  .withIconColor(color)
                  .withIconSize(size)
                  .withIconRotate(imageRotation)
                  .withIconAnchor(imageAnchor)
                  .withIconOffset(imageOffset.toMaplibreOffset())
        }

        text?.let {
          symbolOptions =
              symbolOptions
                  .withTextField(text.text)
                  .withTextColor(text.color)
                  .withTextSize(text.size)
                  .withTextJustify(text.justify)
                  .withTextAnchor(text.anchor)
                  .withTextOffset(text.offset.toMaplibreOffset())
        }

        val symbol = symbolManager.create(symbolOptions)
        SymbolNode(symbolManager, symbol, onTap = { onTap() }, onLongPress = { onLongPress() })
      },
      update = {
        set(center) {
          symbol.latLng = center
          symbolManager.update(symbol)
        }

        set(text) {
          symbol.textField = text?.text
          symbolManager.update(symbol)
        }

        set(color) { symbol.iconColor = color }

        set(imageRotation) { symbol.iconRotate = imageRotation }
      })
}
