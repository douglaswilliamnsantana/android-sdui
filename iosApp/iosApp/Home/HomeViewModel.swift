import Shared

/// Equivalente ao HomeViewModel do Android.
/// Usa ObservableObject + @Published (iOS 14+) em vez de StateFlow/collectAsState.
@MainActor
final class HomeViewModel: ObservableObject {

    @Published private(set) var node: NodeReader? = nil
    @Published private(set) var isLoading: Bool = true
    @Published private(set) var error: String? = nil

    private let sdk: SduiSdk

    init(sdk: SduiSdk = SduiSdk()) {
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
            node = try await sdk.fetchScreen(route: "/home")
        } catch {
            self.error = error.localizedDescription
        }
        isLoading = false
    }
}
