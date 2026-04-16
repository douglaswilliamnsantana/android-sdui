package com.douglassantana.core

/**
 * Interface base para todos os componentes de UI do sistema SDUI.
 *
 * Um [UIComponent] é o resultado da transformação de um [Node] por uma
 * [ComponentFactory]. Representa
 * um componente já tipado e pronto para ser renderizado pelo
 * [RendererRegistry].
 *
 * A propriedade [children] permite que componentes sejam organizados em árvore.
 * O [RendererRegistry] renderiza
 * os filhos automaticamente após renderizar o componente pai.
 *
 * ---
 *
 * Base interface for all UI components in the SDUI system.
 *
 * A [UIComponent] is the result of transforming a [Node] through a
 * [ComponentFactory]. It represents
 * a typed component ready to be rendered by the
 * [RendererRegistry].
 *
 * The [children] property allows components to be organized as a tree.
 * The [RendererRegistry] renders
 * children automatically after rendering the parent component.
 */
interface UIComponent {
    val children: List<UIComponent>
        get() = emptyList()
}