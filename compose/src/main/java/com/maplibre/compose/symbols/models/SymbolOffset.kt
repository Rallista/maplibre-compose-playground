package com.maplibre.compose.symbols.models

/**
 * The offset of a symbol.
 *
 * @param x The x offset. Positive values indicate right, while negative values indicate left.
 * @param y The y offset. Positive values indicate up, while negative values indicate down.
 */
data class SymbolOffset(val x: Float = 0.0f, val y: Float = 0.0f)

internal fun SymbolOffset.toMaplibreOffset(): Array<Float> {
  return arrayOf(x, y)
}
