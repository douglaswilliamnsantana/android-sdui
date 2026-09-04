package com.douglassantana.sdui_components.appbar

import com.douglassantana.sdui_core.Node
import com.douglassantana.sdui_core.UIComponent
import com.douglassantana.sdui_core.action.UIAction
import com.douglassantana.sdui_core.context.SDUIContext
import com.douglassantana.sdui_core.factory.ComponentFactory
import com.douglassantana.sdui_core.factory.SduiJson
import kotlinx.serialization.json.decodeFromJsonElement

/**
 * PT: [com.douglassantana.sdui_runtime.compose.ComponentRenderer] nunca recebe
 *     [SDUIContext] — por isso os cliques em `leftAction`/`rightAction` são resolvidos
 *     aqui, onde o contexto (e o `actionHandler`) ainda está disponível, e guardados
 *     como callbacks prontos em [SduiAppBar].
 *
 * EN: [com.douglassantana.sdui_runtime.compose.ComponentRenderer] never receives
 *     [SDUIContext] — so `leftAction`/`rightAction` clicks are resolved here, where the
 *     context (and its `actionHandler`) is still available, and stored as ready-to-use
 *     callbacks on [SduiAppBar].
 */
class SduiAppBarFactory : ComponentFactory<SduiAppBarProps> {

    override fun type() = "app_bar"

    override fun parseProps(node: Node): SduiAppBarProps =
        SduiJson.decodeFromJsonElement(node.props)

    override fun create(
        props: SduiAppBarProps,
        context: SDUIContext,
        children: List<UIComponent>
    ) = SduiAppBar(
        type = props.type,
        title = props.title,
        leftIcon = props.leftIcon,
        leftIconAction = props.leftAction?.toNavigateAction(context),
        rightIcon = props.rightIcon,
        rightIconAction = props.rightAction?.toNavigateAction(context),
    )

    private fun String.toNavigateAction(context: SDUIContext): () -> Unit = {
        context.actionHandler?.handle(UIAction.Navigate(this))
    }
}
