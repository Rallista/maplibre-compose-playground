package com.maplibre.compose.runtime.nodes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeNode
import androidx.compose.runtime.DisallowComposableCalls
import androidx.compose.runtime.MutableState
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.maplibre.compose.ramani.MapApplier
import com.maplibre.compose.ramani.MapNode

/**
 * A ComposeNode that applies a map update to the MapboxMap whenever the a state changes.
 *
 * @param state The state that will be observed for changes.
 * @param mapApplier The MapApplier that will be used to apply the update.
 * @param applyUpdate The function that will be called to apply the update to the MapboxMap. This
 *   function is called whenever the state changes and when the map is attached.
 */
@Composable
internal fun <T> MapComposeNode(
    state: MutableState<T>,
    mapApplier: MapApplier,
    applyUpdate: @DisallowComposableCalls (MapboxMap, T) -> Unit
) =
    ComposeNode<MapStateNode<T>, MapApplier>(
        factory = { MapStateNode(mapApplier.map, state, applyUpdate) },
        update = { update(state.value) { updatedState -> applyUpdate(map, updatedState) } })

private class MapStateNode<T>(
    val map: MapboxMap,
    var state: MutableState<T>,
    var applyUpdate: (MapboxMap, T) -> Unit
) : MapNode {
  override fun onAttached() {
    applyUpdate(map, state.value)
  }
}
