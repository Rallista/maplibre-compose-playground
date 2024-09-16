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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.maplibre.compose.symbols.builder.SymbolText
import com.maplibre.compose.symbols.models.SymbolOffset
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.style.layers.Property.ICON_ANCHOR_CENTER

@Composable
fun UpdateCenter(coord: LatLng, centerUpdated: (LatLng) -> Unit) {
  centerUpdated(coord)
}

@Composable
fun CircleWithItem(
    center: LatLng,
    radius: Float,
    dragRadius: Float = radius,
    isDraggable: Boolean = false,
    color: String = "Yellow",
    borderColor: String = "Black",
    borderWidth: Float = 0.0f,
    opacity: Float = 1.0f,
    zIndex: Int = 0,
    imageId: Int? = null,
    imageRotation: Float? = null,
    imageAnchor: String = ICON_ANCHOR_CENTER,
    imageOffset: SymbolOffset = SymbolOffset(),
    itemSize: Float = 0.0f,
    text: SymbolText? = null,
    onCenterChanged: (LatLng) -> Unit = {},
    onDragStopped: () -> Unit = {},
    onTap: () -> Unit = {},
    onLongPress: () -> Unit = {}
) {
  val draggableCenterState = remember { mutableStateOf(center) }

  UpdateCenter(coord = center, centerUpdated = { draggableCenterState.value = it })

  // Invisible circle, this is the draggable
  Circle(
      center = draggableCenterState.value,
      radius = dragRadius,
      isDraggable = isDraggable,
      color = "Transparent",
      borderColor = borderColor,
      borderWidth = 0.0f,
      zIndex = zIndex + 1,
      onCenterDragged = { onCenterChanged(it) },
      onDragFinished = {
        draggableCenterState.value = center
        onDragStopped()
      },
      onTap = onTap,
      onLongPress = onLongPress)

  // Display circle
  Circle(
      center = center,
      radius = radius,
      isDraggable = false,
      color = color,
      opacity = opacity,
      zIndex = zIndex,
      borderColor = borderColor,
      borderWidth = borderWidth,
      onCenterDragged = {})

  imageId?.let {
    Symbol(
        center = center,
        color = "Black",
        isDraggable = false,
        imageId = imageId,
        imageRotation = imageRotation,
        imageAnchor = imageAnchor,
        imageOffset = imageOffset,
        size = itemSize,
        zIndex = zIndex + 1,
    )
  }

  text?.let {
    Symbol(
        center = center,
        color = "Black",
        isDraggable = false,
        text = text,
        size = itemSize,
        zIndex = zIndex + 1)
  }
}
