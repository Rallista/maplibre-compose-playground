package com.maplibre.example

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.maplibre.example.examples.CallbackExample
import com.maplibre.example.examples.CameraExample
import com.maplibre.example.examples.DarkAndLightModeExample
import com.maplibre.example.examples.MapControlsExample
import com.maplibre.example.examples.SymbolExample

@Composable
fun Main() {
  val navController = rememberNavController()

  Scaffold(topBar = { AppTopBar(navController) }) {
    NavHost(
        modifier = Modifier.padding(it), navController = navController, startDestination = "main") {
          composable("main") {
            MainMenu(modifier = Modifier.fillMaxSize(), navController = navController)
          }
          composable("dark") { DarkAndLightModeExample() }
          composable("camera") { CameraExample() }
          composable("callback") { CallbackExample() }
          composable("symbol") { SymbolExample() }
          composable("map-controls") { MapControlsExample() }
        }
  }
}

@Composable
fun MainMenu(modifier: Modifier, navController: NavController) {
  Column(
      modifier = modifier,
      verticalArrangement = Arrangement.Top,
      horizontalAlignment = Alignment.Start) {
        NavigationLink("Dark and Light Mode Example", "dark", navController)
        NavigationLink("Map Callback Example", "callback", navController)
        NavigationLink("Map Camera Control Example", "camera", navController)
        NavigationLink("Symbols Example", "symbol", navController)
        NavigationLink(title = "Map Controls Example", destination = "map-controls", navController)
      }
}

@Composable
fun NavigationLink(title: String, destination: String, navController: NavController) {
  Button(
      onClick = { navController.navigate(destination) },
      modifier = Modifier.fillMaxWidth().height(48.dp),
      colors =
          ButtonDefaults.buttonColors(
              containerColor = MaterialTheme.colorScheme.surface,
              contentColor = MaterialTheme.colorScheme.onSurface)) {
        Text(title, fontSize = MaterialTheme.typography.titleMedium.fontSize)

        Spacer(modifier = Modifier.weight(1f))

        Icon(
            painterResource(R.drawable.chevron_right_24px),
            contentDescription = "Navigate to $destination",
            tint = MaterialTheme.colorScheme.onSurface,
        )
      }

  HorizontalDivider()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(navController: NavController) {
  val navBackStackEntry by navController.currentBackStackEntryAsState()

  TopAppBar(
      title = { Text(text = "MapLibre Compose Example") },
      navigationIcon = {
        if (navBackStackEntry?.destination?.route != "main") {
          IconButton(onClick = { navController.navigateUp() }) {
            Icon(painterResource(R.drawable.chevron_left_24px), contentDescription = "Back")
          }
        }
      },
      colors =
          TopAppBarColors(
              containerColor = MaterialTheme.colorScheme.surfaceVariant,
              scrolledContainerColor = MaterialTheme.colorScheme.surface,
              navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
              titleContentColor = MaterialTheme.colorScheme.onSurface,
              actionIconContentColor = MaterialTheme.colorScheme.onSurface))
}

@Composable
@Preview
fun MainPreview() {
  Main()
}
