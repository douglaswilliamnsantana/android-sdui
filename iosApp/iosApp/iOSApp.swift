import SwiftUI
import Shared

@main
struct iOSApp: App {
    init() {
        AppKoin.shared.start()
    }

    var body: some Scene {
        WindowGroup {
            HomeView()
                .sduiTheme()
        }
    }
}
