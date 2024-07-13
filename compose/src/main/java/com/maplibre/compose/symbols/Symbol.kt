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

import android.graphics.drawable.VectorDrawable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeNode
import androidx.compose.runtime.currentComposer
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import com.maplibre.compose.ramani.MapApplier
import com.maplibre.compose.ramani.MapLibreComposable
import com.maplibre.compose.ramani.SymbolNode
import com.maplibre.compose.symbols.builder.SymbolText
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.plugins.annotation.SymbolOptions

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
    text: SymbolText? = null,
    onTap: () -> Unit = {},
    onLongPress: () -> Unit = {}
) {
  val context = LocalContext.current
  val mapApplier = currentComposer.applier as MapApplier

  imageId?.let {
    if (mapApplier.style.getImage("$imageId") == null) {
      val vectorDrawable = context.getDrawable(imageId) as? VectorDrawable
      if (vectorDrawable == null) {
        mapApplier.style.addImage("$imageId", ImageBitmap.imageResource(imageId).asAndroidBitmap())
      } else {
        vectorDrawable.let { drawable -> mapApplier.style.addImage("$imageId", drawable) }
      }
    }
  }

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
        }

        text?.let {
          symbolOptions =
              symbolOptions
                  .withTextField(text.text)
                  .withTextColor(text.color)
                  .withTextSize(text.size)
                  .withTextJustify(text.justify)
                  .withTextAnchor(text.anchor)
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
