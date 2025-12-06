package com.maplibre.example.auto

import android.location.Location
import androidx.car.app.CarContext
import androidx.car.app.model.Action
import androidx.car.app.model.ActionStrip
import androidx.car.app.model.ItemList
import androidx.car.app.model.PlaceListMapTemplate
import androidx.car.app.model.Template
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.maplibre.compose.camera.MapViewCamera
import com.maplibre.compose.camera.extensions.incrementZoom
import com.maplibre.compose.car.MapViewScreen
import com.maplibre.compose.ramani.LocationRequestProperties
import com.maplibre.compose.ramani.LocationStyling
import org.maplibre.android.location.engine.LocationEngine
import org.maplibre.android.maps.Style

class ExampleMapScreen(
    carContext: CarContext,
    styleUrl: String = "https://demotiles.maplibre.org/style.json",
    locationEngine: LocationEngine? = null,
    locationRequestProperties: LocationRequestProperties = LocationRequestProperties.Default,
    locationStyling: LocationStyling = LocationStyling.Default,
    userLocation: MutableState<Location>? = null,
    onMapReadyCallback: ((Style) -> Unit)? = null
) : MapViewScreen(
    carContext = carContext,
    styleUrl = styleUrl,
    camera = mutableStateOf(MapViewCamera.Default),
    locationEngine = locationEngine,
    locationRequestProperties = locationRequestProperties,
    locationStyling = locationStyling,
    userLocation = userLocation,
    onMapReadyCallback = onMapReadyCallback
) {

    override fun onGetTemplate(): Template {
        return PlaceListMapTemplate.Builder()
            .setTitle("MapLibre Demo")
            .setItemList(
                ItemList.Builder()
                    .build()
            )
            .setActionStrip(
                ActionStrip.Builder()
                    .addAction(
                        Action.Builder()
                            .setTitle("Zoom In")
                            .setOnClickListener {
                                camera.value = camera.value.incrementZoom(1.0)
                            }
                            .build()
                    )
                    .addAction(
                        Action.Builder()
                            .setTitle("Zoom Out")
                            .setOnClickListener {
                                camera.value = camera.value.incrementZoom(-1.0)
                            }
                            .build()
                    )
                    .build()
            )
            .build()
    }
}