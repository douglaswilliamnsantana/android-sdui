package com.douglassantana.sdui_components.screen

import com.douglassantana.sdui_core.UIComponent

/**
 * PT: Marca os três tipos de slot que um [SduiScreen] reconhece. Existir como um sealed
 *     interface (em vez de uma única classe genérica) permite que [SduiScreenFactory]
 *     distinga cada slot com [filterIsInstance] — a posição dos filhos no JSON não importa.
 *
 * EN: Marks the three slot types a [SduiScreen] recognizes. Being a sealed interface
 *     (instead of one generic class) lets [SduiScreenFactory] tell each slot apart with
 *     [filterIsInstance] — child ordering in the JSON doesn't matter.
 */
sealed interface SduiScreenSlot : UIComponent

data class SduiHeader(override val children: List<UIComponent> = emptyList()) : SduiScreenSlot
data class SduiBody(override val children: List<UIComponent> = emptyList()) : SduiScreenSlot
data class SduiBottom(override val children: List<UIComponent> = emptyList()) : SduiScreenSlot
