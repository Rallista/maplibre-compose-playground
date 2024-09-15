package com.maplibre.compose.runtime.nodes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.currentComposer
import com.maplibre.compose.ramani.MapApplier
import com.maplibre.compose.settings.MapControls

@Composable
internal fun MapControlsNode(mapControls: MutableState<MapControls>) {

  val mapApplier = currentComposer.applier as MapApplier

  MapComposeNode(mapControls, mapApplier) { map, newMapControls ->
    // Apply the new map controls to the map whenever invoked within the Node.
    newMapControls.attribution?.let { newAttribution ->
      newAttribution.enabled?.let { map.uiSettings.isAttributionEnabled = it }
      newAttribution.gravity?.let { map.uiSettings.attributionGravity = it }
      newAttribution.margins?.let {
        map.uiSettings.setAttributionMargins(it.start, it.top, it.end, it.bottom)
      }
      newAttribution.tintColor?.let { map.uiSettings.setAttributionTintColor(it) }
    }

    newMapControls.compass?.let { newCompass ->
      newCompass.enabled?.let { map.uiSettings.isCompassEnabled = it }
      newCompass.gravity?.let { map.uiSettings.compassGravity = it }
      newCompass.fadeFacingNorth?.let { map.uiSettings.setCompassFadeFacingNorth(it) }
      newCompass.margins?.let {
        map.uiSettings.setCompassMargins(it.start, it.top, it.end, it.bottom)
      }
    }

    newMapControls.logo?.let { newLogo ->
      newLogo.enabled?.let { map.uiSettings.isLogoEnabled = it }
      newLogo.gravity?.let { map.uiSettings.logoGravity = it }
      newLogo.margins?.let { map.uiSettings.setLogoMargins(it.start, it.top, it.end, it.bottom) }
    }
  }
}
