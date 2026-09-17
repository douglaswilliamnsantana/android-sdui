package com.douglassantana.sdui_core

/**
 * Identificadores de tipo conhecidos pelo SDUI, usados tanto por [com.douglassantana.sdui_core.Node.type]
 * (o que o servidor manda) quanto por [com.douglassantana.sdui_core.factory.ComponentFactory.type]
 * (a chave sob a qual a factory se registra no [com.douglassantana.sdui_core.registry.ComponentRegistry]).
 *
 * Centralizar essas strings aqui evita duplicação entre factories e transforma um typo em
 * erro de compilação (referência quebrada) em vez de um `UnknownComponent` silencioso em runtime.
 *
 * ---
 *
 * Type identifiers known to the SDUI system, used both by [com.douglassantana.sdui_core.Node.type]
 * (what the server sends) and by [com.douglassantana.sdui_core.factory.ComponentFactory.type]
 * (the key under which the factory registers itself in the [com.douglassantana.sdui_core.registry.ComponentRegistry]).
 *
 * Centralizing these strings here avoids duplication across factories and turns a typo into
 * a compile error (broken reference) instead of a silent `UnknownComponent` at runtime.
 */
object SduiNodeType {
    const val TEXT = "text"
    const val APP_BAR = "app_bar"
    const val SCREEN = "screen"
    const val HEADER = "header"
    const val BODY = "body"
    const val BOTTOM = "bottom"
}
