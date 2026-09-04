package com.douglassantana.sdui_components.screen

import com.douglassantana.sdui_core.IProps

/**
 * PT: Props compartilhadas por "header", "body" e "bottom" — nenhum dos três tem
 *     configuração própria por enquanto, apenas agrupam os filhos que o servidor mandar.
 *
 * EN: Props shared by "header", "body" and "bottom" — none of the three has its own
 *     configuration yet, they just group whatever children the server sends.
 */
data object SduiScreenSlotProps : IProps
