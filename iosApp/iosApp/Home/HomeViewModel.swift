import Shared
import FirebaseRemoteConfig

/// Equivalente ao HomeViewModel do Android.
/// Usa ObservableObject + @Published (iOS 14+) em vez de StateFlow/collectAsState.
@MainActor
final class HomeViewModel: ObservableObject {

    @Published private(set) var node: NodeReader? = nil
    @Published private(set) var isLoading: Bool = true
    @Published private(set) var error: String? = nil

    private let source: ScreenSourceOption
    private let sdk: SduiSdk

    init(source: ScreenSourceOption, sdk: SduiSdk = SduiSdk()) {
        self.source = source
        self.sdk = sdk
        Task { await loadScreen() }
    }

    func retry() {
        Task { await loadScreen() }
    }

    private func loadScreen() async {
        isLoading = true
        error = nil
        do {
            switch source {
            case .backend:
                node = try await sdk.fetchScreen(route: "/home")
            case .remoteConfig:
                // Fetched directly via the native Firebase iOS SDK (not through Kotlin) —
                // "home" must stay in sync with Android's key derivation
                // (route.path.removePrefix("/") in RemoteConfigSduiRepositoryImpl).
                let remoteConfig = RemoteConfig.remoteConfig()
                _ = try await remoteConfig.fetchAndActivate()
                let json = remoteConfig.configValue(forKey: "home").stringValue
                node = try sdk.parseScreen(json: json)
            }
        } catch {
            self.error = error.localizedDescription
        }
        isLoading = false
    }
}
