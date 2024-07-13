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
import org.maplibre.android.location.engine.LocationEngineRequest
import com.maplibre.compose.ramani.LocationPriority.PRIORITY_BALANCED_POWER_ACCURACY
import com.maplibre.compose.ramani.LocationPriority.PRIORITY_HIGH_ACCURACY
import com.maplibre.compose.ramani.LocationPriority.PRIORITY_LOW_POWER
import com.maplibre.compose.ramani.LocationPriority.PRIORITY_NO_POWER
import kotlinx.parcelize.Parcelize

/**
 * @property PRIORITY_HIGH_ACCURACY Request the most accurate location.
 * @property PRIORITY_BALANCED_POWER_ACCURACY Request coarse location that is battery optimized.
 * @property PRIORITY_LOW_POWER Request coarse ~10km accuracy location.
 * @property PRIORITY_NO_POWER Request passive location (no locations will be returned unless a
 *   different client requests location updates).
 */
@Parcelize
enum class LocationPriority(val value: Int) : Parcelable {
  PRIORITY_HIGH_ACCURACY(LocationEngineRequest.PRIORITY_HIGH_ACCURACY),
  PRIORITY_BALANCED_POWER_ACCURACY(LocationEngineRequest.PRIORITY_BALANCED_POWER_ACCURACY),
  PRIORITY_LOW_POWER(LocationEngineRequest.PRIORITY_LOW_POWER),
  PRIORITY_NO_POWER(LocationEngineRequest.PRIORITY_NO_POWER)
}

@Parcelize
class LocationRequestProperties(
    var priority: LocationPriority = PRIORITY_HIGH_ACCURACY,
    var interval: Long = 1000L,
    var fastestInterval: Long = 0L,
    var displacement: Float = 0F,
    var maxWaitTime: Long = 0L
) : Parcelable {
  constructor(
      locationRequestProperties: LocationRequestProperties
  ) : this(
      locationRequestProperties.priority,
      locationRequestProperties.interval,
      locationRequestProperties.fastestInterval,
      locationRequestProperties.displacement,
      locationRequestProperties.maxWaitTime,
  )

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as LocationRequestProperties

    if (priority != other.priority) return false
    if (interval != other.interval) return false
    if (fastestInterval != other.fastestInterval) return false
    if (displacement != other.displacement) return false
    return maxWaitTime == other.maxWaitTime
  }

  override fun hashCode(): Int {
    var result = priority.hashCode()
    result = 31 * result + interval.hashCode()
    result = 31 * result + fastestInterval.hashCode()
    result = 31 * result + displacement.hashCode()
    result = 31 * result + maxWaitTime.hashCode()
    return result
  }

  class Builder {
    private var priority: LocationPriority = PRIORITY_HIGH_ACCURACY
    private var interval: Long = 1000L
    private var fastestInterval: Long = 0L
    private var displacement: Float = 0F
    private var maxWaitTime: Long = 0L

    fun priority(priority: LocationPriority) = apply { this.priority = priority }

    fun interval(interval: Long) = apply { this.interval = interval }

    fun fastestInterval(fastestInterval: Long) = apply { this.fastestInterval = fastestInterval }

    fun displacement(displacement: Float) = apply { this.displacement = displacement }

    fun maxWaitTime(maxWaitTime: Long) = apply { this.maxWaitTime = maxWaitTime }

    fun build() =
        LocationRequestProperties(priority, interval, fastestInterval, displacement, maxWaitTime)
  }

  companion object {
    val Default =
        Builder()
            .priority(PRIORITY_HIGH_ACCURACY)
            .interval(5000L)
            .fastestInterval(1000L)
            .displacement(0F)
            .maxWaitTime(8000L)
            .build()
  }
}
