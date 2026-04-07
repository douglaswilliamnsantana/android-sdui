import Shared
import SwiftUI

// MARK: - Color bridge

extension Color {
    init(_ token: SduiColor) {
        self.init(
            .sRGB,
            red: token.red,
            green: token.green,
            blue: token.blue,
            opacity: token.alpha)
    }
}

// MARK: - Color scheme

struct SduiColors {
    let primary: Color
    let onPrimary: Color
    let background: Color
    let onBackground: Color
    let surface: Color
    let onSurface: Color
    let outline: Color
    let outlineVariant: Color
    let error: Color
    let errorContainer: Color

    static let light = SduiColors(
        primary: Color(SduiColorTokens.Light.shared.primary),
        onPrimary: Color(SduiColorTokens.Light.shared.onPrimary),
        background: Color(SduiColorTokens.Light.shared.background),
        onBackground: Color(SduiColorTokens.Light.shared.onBackground),
        surface: Color(SduiColorTokens.Light.shared.surface),
        onSurface: Color(SduiColorTokens.Light.shared.onSurface),
        outline: Color(SduiColorTokens.Light.shared.outline),
        outlineVariant: Color(SduiColorTokens.Light.shared.outlineVariant),
        error: Color(SduiColorTokens.Light.shared.error),
        errorContainer: Color(SduiColorTokens.Light.shared.errorContainer)
    )

    static let dark = SduiColors(
        primary: Color(SduiColorTokens.Dark.shared.primary),
        onPrimary: Color(SduiColorTokens.Dark.shared.onPrimary),
        background: Color(SduiColorTokens.Dark.shared.background),
        onBackground: Color(SduiColorTokens.Dark.shared.onBackground),
        surface: Color(SduiColorTokens.Dark.shared.surface),
        onSurface: Color(SduiColorTokens.Dark.shared.onSurface),
        outline: Color(SduiColorTokens.Dark.shared.outline),
        outlineVariant: Color(SduiColorTokens.Dark.shared.outlineVariant),
        error: Color(SduiColorTokens.Dark.shared.error),
        errorContainer: Color(SduiColorTokens.Dark.shared.errorContainer)
    )
}

// MARK: - Spacing & Radius

struct SduiSpacing {
    let s2: CGFloat = CGFloat(SduiSpacingTokens.shared.spacing2)
    let s4: CGFloat = CGFloat(SduiSpacingTokens.shared.spacing4)
    let s8: CGFloat = CGFloat(SduiSpacingTokens.shared.spacing8)
    let s12: CGFloat = CGFloat(SduiSpacingTokens.shared.spacing12)
    let s16: CGFloat = CGFloat(SduiSpacingTokens.shared.spacing16)
    let s24: CGFloat = CGFloat(SduiSpacingTokens.shared.spacing24)
    let s32: CGFloat = CGFloat(SduiSpacingTokens.shared.spacing32)
    let s48: CGFloat = CGFloat(SduiSpacingTokens.shared.spacing48)
}

struct SduiRadius {
    let none: CGFloat = CGFloat(SduiRadiusTokens.shared.none)
    let xSmall: CGFloat = CGFloat(SduiRadiusTokens.shared.xSmall)
    let small: CGFloat = CGFloat(SduiRadiusTokens.shared.small)
    let medium: CGFloat = CGFloat(SduiRadiusTokens.shared.medium)
    let large: CGFloat = CGFloat(SduiRadiusTokens.shared.large)
    let xLarge: CGFloat = CGFloat(SduiRadiusTokens.shared.xLarge)
    let full: CGFloat = CGFloat(SduiRadiusTokens.shared.full)
}

// MARK: - Environment key

private struct SduiColorsKey: EnvironmentKey {
    static let defaultValue = SduiColors.light
}

private struct SduiSpacingKey: EnvironmentKey {
    static let defaultValue = SduiSpacing()
}

private struct SduiRadiusKey: EnvironmentKey {
    static let defaultValue = SduiRadius()
}

extension EnvironmentValues {
    var sduiColors: SduiColors {
        get { self[SduiColorsKey.self] }
        set { self[SduiColorsKey.self] = newValue }
    }
    var sduiSpacing: SduiSpacing {
        get { self[SduiSpacingKey.self] }
        set { self[SduiSpacingKey.self] = newValue }
    }
    var sduiRadius: SduiRadius {
        get { self[SduiRadiusKey.self] }
        set { self[SduiRadiusKey.self] = newValue }
    }
}

// MARK: - Theme modifier (equivalent to AndroidSduiTheme { })

struct SduiTheme: ViewModifier {
    @Environment(\.colorScheme) private var colorScheme

    func body(content: Content) -> some View {
        let colors = colorScheme == .dark ? SduiColors.dark : SduiColors.light
        content
            .environment(\.sduiColors, colors)
            .environment(\.sduiSpacing, SduiSpacing())
            .environment(\.sduiRadius, SduiRadius())
            .background(colors.background)
    }
}

extension View {
    /// Wrap your root view with this — equivalent to `AndroidSduiTheme { }` in Compose.
    func sduiTheme() -> some View {
        modifier(SduiTheme())
    }
}
