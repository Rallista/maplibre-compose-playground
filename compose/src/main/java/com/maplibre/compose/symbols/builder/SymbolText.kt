package com.maplibre.compose.symbols.builder

import com.maplibre.compose.symbols.models.SymbolOffset
import org.maplibre.android.style.layers.Property.TEXT_ANCHOR_CENTER
import org.maplibre.android.style.layers.Property.TEXT_JUSTIFY_AUTO

data class SymbolText(
    var text: String,
    var size: Float?,
    var color: String = "Black",
    var justify: String = TEXT_JUSTIFY_AUTO,
    var anchor: String = TEXT_ANCHOR_CENTER,
    var offset: SymbolOffset = SymbolOffset()
) {
  class Builder {
    private var text: String = ""
    private var size: Float? = null
    private var color: String = "Black"
    private var justify: String = TEXT_JUSTIFY_AUTO
    private var anchor: String = TEXT_ANCHOR_CENTER
    private var offset: SymbolOffset = SymbolOffset()

    fun text(text: String) = apply { this.text = text }

    fun textSize(size: Float) = apply { this.size = size }

    fun textColor(color: String) = apply { this.color = color }

    fun textJustify(justify: String) = apply { this.justify = justify }

    fun textAnchor(anchor: String) = apply { this.anchor = anchor }

    fun textOffset(offset: SymbolOffset) = apply { this.offset = offset }

    fun build() =
        SymbolText(text = text, size = size, color = color, justify = justify, anchor = anchor)
  }

  companion object {
    fun text(text: String): SymbolText {
      return Builder().text(text).build()
    }
  }
}
