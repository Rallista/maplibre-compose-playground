package com.maplibre.compose.settings

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MarginInsets(
    val start: Int = 0,
    val top: Int = 0,
    val end: Int = 0,
    val bottom: Int = 0
) : Parcelable
