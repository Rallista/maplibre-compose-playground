# MapLibre Compose Playground

This project is based off of <https://github.com/ramani-maps/ramani-maps> [MPL-2.0 License] and takes a different approach to top level API, emphasizing parity with <https://github.com/stadiamaps/maplibre-swiftui-dsl-playground> for <https://github.com/stadiamaps/ferrostar>.

## Getting Started

We're currently using GitHub to release the package. To install you need: 

In your `settings.gradle`. A personal access token (PAT) is required to access the library through maven, even though the repository is public. 

```groovy
        maven {
            url = "https://maven.pkg.github.com/Rallista/maplibre-compose-playground"
            credentials {
                username = "your_github_username"
                password = "your_github_personal_access_token" 
            }
        }
```

In your app `build.gradle`

```groovy
implementation 'io.github.rallista:maplibre-compose:0.0.7'
```

## Usage

Interact with Maplibre Native from Jetpack Compose using the `MapView`.

```swift
var mapViewCamera = rememberMapViewCamera()

MapView(
    styleUrl = "https://demotiles.maplibre.org/style.json",
    camera = mapViewCamera
)
```

<img src="maplibre-compose-demo.gif" width="400" alt="Demo Animation"/>

### Setting an API Key for the Light and Dark Mode Demo

Copy or move `api_keys_template.xml` from the root directory to `app/src/main/res/values/api_keys.xml` and add your own API key. The demo uses https://stadiamaps.com as the map style provider, but can easily be adjusted to review with another provider.

### Example Scenes

* [Callback Example](app/src/main/java/com/maplibre/example/examples/CameraExample.kt) - Shows several event callbacks that can be implemented from the `MapView`.
* [Camera Example](app/src/main/java/com/maplibre/example/examples/CameraExample.kt) - Shows the basics of camera control using the `MapViewCamera`.
* [Dark and Light Mode Example](app/src/main/java/com/maplibre/example/examples/DarkAndLightModeExample.kt) - Shows usage of rememberMapStyleUrl which dynamically changes the map style based on if the system theme is dark mode. The styles are injected using a Compose wrapper in MainActivity.kt (similar to how a MaterialTheme is injected).
* [Symbol Example](app/src/main/java/com/maplibre/example/examples/SymbolExample.kt) - Shows the basics of Composable symbol creation. 
