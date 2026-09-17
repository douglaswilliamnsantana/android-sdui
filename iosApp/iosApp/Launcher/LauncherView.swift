import SwiftUI

/// Espelha `com.douglassantana.domain.model.ScreenSource` do lado Kotlin, mas como um
/// enum Swift local — o mesmo padrão já usado por `HomeViewModel.swift`, que também
/// nunca importa tipos sealed/genéricos do Kotlin diretamente.
enum ScreenSourceOption {
    case backend
    case remoteConfig
}

/// Equivalente ao LauncherScreen do Android.
/// Puramente apresentacional — sem estado próprio, só emite a escolha do usuário.
struct LauncherView: View {
    @Environment(\.sduiColors)  private var colors
    @Environment(\.sduiSpacing) private var spacing

    let onSourceSelected: (ScreenSourceOption) -> Void

    var body: some View {
        ZStack {
            colors.background.ignoresSafeArea()

            VStack(spacing: spacing.s16) {
                Text("Escolha a fonte da tela SDUI")
                    .foregroundStyle(colors.onSurface)

                Button("Backend") { onSourceSelected(.backend) }
                    .buttonStyle(SduiFilledButtonStyle(colors: colors, spacing: spacing))

                Button("Firebase Remote Config") { onSourceSelected(.remoteConfig) }
                    .buttonStyle(SduiFilledButtonStyle(colors: colors, spacing: spacing))
            }
            .padding(spacing.s24)
        }
    }
}

/// Equivalente ao `AndroidSduiButton` do Android (Material3 `Button` preenchido, largura
/// total, formato pill): fundo `colors.primary`, texto `colors.onPrimary`, cantos
/// totalmente arredondados via `Capsule()`. SwiftUI's default `Button` não tem nenhum
/// desses estilos por padrão — sem isso, fica só texto colorido sem fundo/formato.
private struct SduiFilledButtonStyle: ButtonStyle {
    let colors: SduiColors
    let spacing: SduiSpacing

    func makeBody(configuration: Configuration) -> some View {
        configuration.label
            .frame(maxWidth: .infinity)
            .padding(.vertical, spacing.s12)
            .foregroundStyle(colors.onPrimary)
            .background(colors.primary, in: Capsule())
            .opacity(configuration.isPressed ? 0.7 : 1)
    }
}

#Preview {
    LauncherView(onSourceSelected: { _ in })
        .sduiTheme()
}
