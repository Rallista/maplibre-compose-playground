package com.maplibre.compose.car

import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import androidx.savedstate.SavedStateRegistryController
import androidx.savedstate.SavedStateRegistryOwner

class ComposeViewLifecycleOwner : SavedStateRegistryOwner {
  private var lifecycleRegistry: LifecycleRegistry = LifecycleRegistry(this)
  private var savedStateRegistryController: SavedStateRegistryController =
      SavedStateRegistryController.Companion.create(this)

  override val lifecycle: Lifecycle = lifecycleRegistry
  override val savedStateRegistry = savedStateRegistryController.savedStateRegistry

  fun handleLifecycleEvent(event: Lifecycle.Event) {
    lifecycleRegistry.handleLifecycleEvent(event)
  }

  fun performRestore(savedState: Bundle?) {
    savedStateRegistryController.performRestore(savedState)
  }
}
