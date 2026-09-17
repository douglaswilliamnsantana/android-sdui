import SwiftUI
import Shared
import FirebaseCore

/// Alterna entre LauncherView (escolha da fonte) e HomeView (tela SDUI), espelhando o
/// switch feito no Android por `MainActivity`. Sem persistência de estado de propósito —
/// perder a escolha e voltar pro Launcher é o comportamento esperado, não um bug.
struct RootView: View {
    @State private var source: ScreenSourceOption? = nil

    var body: some View {
        if let source {
            HomeView(source: source)
        } else {
            LauncherView(onSourceSelected: { source = $0 })
        }
    }
}

@main
struct iOSApp: App {
    init() {
        FirebaseApp.configure()
        AppKoin.shared.start()
    }

    var body: some Scene {
        WindowGroup {
            RootView()
                .sduiTheme()
        }
    }
}
