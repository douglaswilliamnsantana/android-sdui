package com.douglassantana.designsystem.components.appbar

/**
 * PT: Os quatro tipos de app bar do Material 3 — ver
 *     https://developer.android.com/develop/ui/compose/components/app-bars
 *
 * EN: Material 3's four app bar types — see
 *     https://developer.android.com/develop/ui/compose/components/app-bars
 */
enum class AppBarType {
    /** PT: Telas que não exigem muita navegação ou ações. / EN: Screens that don't need much navigation or actions. */
    Small,

    /** PT: Telas com uma única ação principal. / EN: Screens with a single primary action. */
    CenterAligned,

    /** PT: Telas com quantidade moderada de navegação e ações. / EN: Screens needing a moderate amount of navigation and actions. */
    Medium,

    /** PT: Telas que exigem muita navegação e ações. / EN: Screens needing lots of navigation and actions. */
    Large,
}
