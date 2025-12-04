# MapLibre Compose Android Auto

This module provides Android Auto support for MapLibre Compose using the high-performance VirtualDisplay and Presentation rendering approach.

## Overview

The implementation is based on the approach described in [MapLibre Android Auto Sample PR #13](https://github.com/maplibre/MapLibre-Android-Auto-Sample/pull/13), which uses Android's `Presentation` class with a `VirtualDisplay` to render the MapView directly on the Android Auto surface. This is significantly more performant than the bitmap-based approach.

## Key Components

### `MapViewPresentation`
Internal presentation class that displays a MapView on a VirtualDisplay for Android Auto.

### `MapSurfaceCallback`
SurfaceCallback implementation that manages the VirtualDisplay and Presentation lifecycle.

### `CarMapViewContainer`
Container class that manages the MapView instance lifecycle and provides it in a format compatible with Android Auto rendering.

## Features

- ✅ **High Performance**: VirtualDisplay + Presentation rendering (no bitmap overhead)
- ✅ **Composable Content Support**: Use the same MapLibre composables (Symbol, Circle, Polyline, etc.) in Android Auto
- ✅ **Lifecycle Safe**: Proper cleanup and lifecycle management
- ✅ **Easy Integration**: Simple API that mirrors the main MapView composable

## Usage

### 1. Add Dependency

```kotlin
dependencies {
    implementation(project(":compose-car-app"))
}
```

### 2. Create a CarAppService

```kotlin
class MyCarAppService : CarAppService() {
    override fun createHostValidator(): HostValidator {
        return HostValidator.ALLOW_ALL_HOSTS_VALIDATOR
    }
    
    override fun onCreateSession(): Session {
        return MyCarSession()
    }
}

class MyCarSession : Session() {
    override fun onCreateScreen(intent: Intent): Screen {
        return MyMapScreen(carContext)
    }
}
```

### 3. Create a Screen with MapView

```kotlin
class MyMapScreen(carContext: CarContext) : Screen(carContext) {
    private val mapContainer: CarMapViewContainer
    private val surfaceCallback: MapSurfaceCallback
    
    init {
        mapContainer = CarMapViewContainer(
            carContext = carContext,
            styleUrl = "https://demotiles.maplibre.org/style.json",
            onMapReady = { map, style ->
                // Configure your map here
                map.cameraPosition = CameraPosition.Builder()
                    .target(LatLng(37.7749, -122.4194))
                    .zoom(12.0)
                    .build()
            }
        )
        
        surfaceCallback = MapSurfaceCallback(
            context = carContext,
            mapViewProvider = { mapContainer.setupMap() },
            onCleanup = { mapContainer.cleanUpMap() }
        )
        
        lifecycle.addObserver(mapContainer)
    }
    
    override fun onGetTemplate(): Template {
        return NavigationTemplate.Builder()
            .build()
            .also {
                carContext.getCarService(AppManager::class.java)
                    .setSurfaceCallback(surfaceCallback)
            }
    }
}
```

### 4. Using Composable Content (Advanced)

You can use the same MapLibre composables in Android Auto that you use in your main app:

```kotlin
class MyMapScreen(carContext: CarContext) : Screen(carContext) {
    private val mapContainer: CarMapViewContainer
    private val surfaceCallback: MapSurfaceCallback
    
    init {
        // Get CompositionContext (in a real app, pass from your Compose UI)
        val composeView = ComposeView(carContext)
        var compositionContext: CompositionContext? = null
        composeView.setContent {
            compositionContext = rememberCompositionContext()
        }
        
        mapContainer = CarMapViewContainer(
            carContext = carContext,
            styleUrl = "https://demotiles.maplibre.org/style.json",
            compositionContext = compositionContext,
            onMapReady = { map, style ->
                // Configure map
            },
            content = {
                // Use MapLibre composables here!
                Symbol(
                    center = LatLng(37.7749, -122.4194),
                    imageId = "marker",
                    onClick = { /* Handle click */ }
                )
                
                Circle(
                    center = LatLng(37.8715, -122.2730),
                    radius = 5f,
                    color = Color.Blue
                )
                
                Polyline(
                    points = listOf(/* your route */),
                    color = Color.Red,
                    width = 3f
                )
                
                // All MapLibre composables work here!
            }
        )
        
        surfaceCallback = MapSurfaceCallback(
            context = carContext,
            mapViewProvider = { mapContainer.setupMap() },
            onCleanup = { mapContainer.cleanUpMap() }
        )
        
        lifecycle.addObserver(mapContainer)
    }
    
    override fun onGetTemplate(): Template {
        return NavigationTemplate.Builder()
            .build()
            .also {
                carContext.getCarService(AppManager::class.java)
                    .setSurfaceCallback(surfaceCallback)
            }
    }
}
```

See `MapLibreCarScreenWithComposables.kt` in the app module for a complete example.

### 5. Update AndroidManifest.xml

```xml
<uses-permission android:name="android.permission.FOREGROUND_SERVICE" />

<service
    android:name=".MyCarAppService"
    android:exported="true">
    <intent-filter>
        <action android:name="androidx.car.app.CarAppService" />
        <category android:name="androidx.car.app.category.NAVIGATION"/>
    </intent-filter>
</service>
```

## Testing

Test your Android Auto app using:
1. **Desktop Head Unit (DHU)**: Download from the Android Auto developer site
2. **Real Android Auto device**: Connect your phone to a compatible head unit
3. **Android Automotive OS Emulator**: Test on AAOS directly

## Performance

This implementation uses direct rendering via `VirtualDisplay` and `Presentation`, which is significantly faster than bitmap-based approaches. The MapView renders natively on the virtual display without the overhead of texture capture and canvas drawing.

## See Also

- [MapLibre Android Auto Sample](https://github.com/maplibre/MapLibre-Android-Auto-Sample)
- [Blog post on MapLibre on Android Auto](https://helw.net/2025/11/16/maplibre-on-android-auto/)
- [Android Auto Template Documentation](https://developer.android.com/training/cars/apps)
