package com.maplibre.compose

import android.content.res.Configuration
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.maplibre.compose.camera.models.CameraPadding
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class CameraPaddingTests {
  @get:Rule val composeTestRule = createComposeRule()

  @Test
  fun test_createFromPadding_granular_ltr() {
    composeTestRule.setContent {
      val density = 2.0f

      CompositionLocalProvider(
        LocalDensity provides Density(density, 1.0f),
        LocalLayoutDirection provides LayoutDirection.Ltr
      ) {
        val cameraPadding = CameraPadding.padding(start = 10.dp, top = 20.dp, end = 30.dp, bottom = 40.dp)

        assertEquals(20.0, cameraPadding.left, 0.1)
        assertEquals(40.0, cameraPadding.top, 0.1)
        assertEquals(60.0, cameraPadding.right, 0.1)
        assertEquals(80.0, cameraPadding.bottom, 0.1)
      }
    }
  }

  @Test
  fun test_createFromPadding_granular_rtl() {
    composeTestRule.setContent {
      val density = 2.0f

      CompositionLocalProvider(
        LocalDensity provides Density(density, 1.0f),
        LocalLayoutDirection provides LayoutDirection.Rtl
      ) {
        val cameraPadding = CameraPadding.padding(start = 10.dp, top = 20.dp, end = 30.dp, bottom = 40.dp)

        assertEquals(60.0, cameraPadding.left, 0.1)
        assertEquals(40.0, cameraPadding.top, 0.1)
        assertEquals(20.0, cameraPadding.right, 0.1)
        assertEquals(80.0, cameraPadding.bottom, 0.1)
      }
    }
  }

  @Test
  fun test_createFromPadding_horizontalVertical() {
    composeTestRule.setContent {
      val density = 4.0f

      CompositionLocalProvider(
        LocalDensity provides Density(density, 1.0f),
        LocalLayoutDirection provides LayoutDirection.Ltr
      ) {
        val cameraPadding = CameraPadding.padding(horizontal = 10.dp, vertical = 20.dp)

        assertEquals(40.0, cameraPadding.left, 0.1)
        assertEquals(40.0, cameraPadding.right, 0.1)

        assertEquals(80.0, cameraPadding.top, 0.1)
        assertEquals(80.0, cameraPadding.bottom, 0.1)
      }
    }
  }

  @Test
  fun test_createFromPadding_all() {
    composeTestRule.setContent {
      val density = 2.0f

      CompositionLocalProvider(
        LocalDensity provides Density(density, 1.0f),
        LocalLayoutDirection provides LayoutDirection.Ltr
      ) {
        val cameraPadding = CameraPadding.padding(20.dp)

        assertEquals(40.0, cameraPadding.left, 0.1)
        assertEquals(40.0, cameraPadding.right, 0.1)
        assertEquals(40.0, cameraPadding.top, 0.1)
        assertEquals(40.0, cameraPadding.bottom, 0.1)
      }
    }
  }

  @Test
  fun test_createFromPadding_paddingValues() {
    composeTestRule.setContent {
      val density = 4.0f
      val paddingValues = PaddingValues(all = 10.dp)

      CompositionLocalProvider(
        LocalDensity provides Density(density, 1.0f),
        LocalLayoutDirection provides LayoutDirection.Ltr
      ) {
        val cameraPadding = CameraPadding.padding(paddingValues)

        assertEquals(40.0, cameraPadding.left, 0.1)
        assertEquals(40.0, cameraPadding.right, 0.1)

        assertEquals(40.0, cameraPadding.top, 0.1)
        assertEquals(40.0, cameraPadding.bottom, 0.1)
      }
    }
  }

  // Fraction of Screen Tests

  @Test
  fun test_fractionOfScreen_ltr() {
    composeTestRule.setContent {
      val density = 2.0f
      val configuration = Configuration().apply {
        screenWidthDp = 1000
        screenHeightDp = 1000
      }

      CompositionLocalProvider(
        LocalDensity provides Density(density, 1.0f),
        LocalLayoutDirection provides LayoutDirection.Ltr,
        LocalConfiguration provides configuration
      ) {
        val cameraPadding = CameraPadding.fractionOfScreen(start = 0.1f, top = 0.2f, end = 0.3f, bottom = 0.4f)

        assertEquals(200.0, cameraPadding.left, 0.1)
        assertEquals(400.0, cameraPadding.top, 0.1)
        assertEquals(600.0, cameraPadding.right, 0.1)
        assertEquals(800.0, cameraPadding.bottom, 0.1)
      }
    }
  }

  @Test
  fun test_fractionOfScreen_rtl() {
    composeTestRule.setContent {
      val density = 2.0f
      val configuration = Configuration().apply {
        screenWidthDp = 1000
        screenHeightDp = 1000
      }

      CompositionLocalProvider(
        LocalDensity provides Density(density, 1.0f),
        LocalLayoutDirection provides LayoutDirection.Rtl,
        LocalConfiguration provides configuration
      ) {
        val cameraPadding = CameraPadding.fractionOfScreen(start = 0.1f, top = 0.2f, end = 0.3f, bottom = 0.4f)

        assertEquals(600.0, cameraPadding.left, 0.1)
        assertEquals(400.0, cameraPadding.top, 0.1)
        assertEquals(200.0, cameraPadding.right, 0.1)
        assertEquals(800.0, cameraPadding.bottom, 0.1)
      }
    }
  }

}
