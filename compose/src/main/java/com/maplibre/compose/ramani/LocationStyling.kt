/*
 * This file is part of ramani-maps.
 *
 * Copyright (c) 2024 Roman Bapst & Jonas Vautherin.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.maplibre.compose.ramani

import android.os.Parcelable
import androidx.annotation.ColorInt
import kotlinx.parcelize.Parcelize

/**
 * @property accuracyAlpha Opacity of the accuracy view between 0 (transparent) and 1 (opaque).
 * @property accuracyColor Color of the accuracy view.
 * @property enablePulse Enable the location pulsing circle.
 * @property enablePulseFade Enable the fading of the pulsing circle.
 * @property pulseColor Color of the pulsing circle.
 */
@Parcelize
class LocationStyling(
    var accuracyAlpha: Float? = null,
    @ColorInt var accuracyColor: Int? = null,
    var enablePulse: Boolean? = null,
    var enablePulseFade: Boolean? = null,
    @ColorInt var pulseColor: Int? = null,
) : Parcelable {
  constructor(
      locationStyling: LocationStyling
  ) : this(
      locationStyling.accuracyAlpha,
      locationStyling.accuracyColor,
      locationStyling.enablePulse,
      locationStyling.enablePulseFade,
      locationStyling.pulseColor,
  )

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as LocationStyling

    if (accuracyAlpha != other.accuracyAlpha) return false
    if (accuracyColor != other.accuracyColor) return false
    if (enablePulse != other.enablePulse) return false
    if (enablePulseFade != other.enablePulseFade) return false
    return pulseColor == other.pulseColor
  }

  override fun hashCode(): Int {
    var result = accuracyAlpha?.hashCode() ?: 0
    result = 31 * result + (accuracyColor ?: 0)
    result = 31 * result + (enablePulse?.hashCode() ?: 0)
    result = 31 * result + (enablePulseFade?.hashCode() ?: 0)
    result = 31 * result + (pulseColor ?: 0)
    return result
  }

  class Builder {
    private var accuracyAlpha: Float? = null
    @ColorInt private var accuracyColor: Int? = null
    private var enablePulse: Boolean? = null
    private var enablePulseFade: Boolean? = null
    @ColorInt private var pulseColor: Int? = null

    fun accuracyAlpha(accuracyAlpha: Float?) = apply { this.accuracyAlpha = accuracyAlpha }

    fun accuracyColor(@ColorInt accuracyColor: Int?) = apply { this.accuracyColor = accuracyColor }

    fun enablePulse(enablePulse: Boolean?) = apply { this.enablePulse = enablePulse }

    fun enablePulseFade(enablePulseFade: Boolean?) = apply {
      this.enablePulseFade = enablePulseFade
    }

    fun pulseColor(@ColorInt pulseColor: Int?) = apply { this.pulseColor = pulseColor }

    fun build() =
        LocationStyling(accuracyAlpha, accuracyColor, enablePulse, enablePulseFade, pulseColor)
  }

  companion object {
    val Default =
        Builder()
            .accuracyAlpha(0.5f)
            .accuracyColor(0x00000000)
            .enablePulse(true)
            .enablePulseFade(true)
            .pulseColor(0x00000000)
            .build()
  }
}
