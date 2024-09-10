package com.maplibre.compose.symbols

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.VectorDrawable
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.res.imageResource
import com.mapbox.mapboxsdk.maps.Style

@SuppressLint("ComposableNaming")
@Composable
internal fun Style.addImageFromResourceId(context: Context, resourceId: Int?) {
  resourceId?.let {
    val id = it.toString()
    if (this.getImage(id) == null) {
      val vectorDrawable = context.getDrawable(it) as? VectorDrawable
      if (vectorDrawable != null) {
        this.addImage(id, vectorDrawable)
      } else {
        this.addImage(id, ImageBitmap.imageResource(it).asAndroidBitmap())
      }
    }
  }
}
