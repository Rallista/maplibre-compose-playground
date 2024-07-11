package com.maplibre.example.support

import android.Manifest
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable

val locationPermissions =
    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)

@Composable
fun rememberLocationPermissionLauncher(
    onAccess: () -> Unit,
    onFailed: () -> Unit
): ManagedActivityResultLauncher<Array<String>, Map<String, Boolean>> {
  return rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
      permissions ->
    when {
      permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
        onAccess()
      }
      permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
        onAccess()
      }
      else -> {
        onFailed()
      }
    }
  }
}
