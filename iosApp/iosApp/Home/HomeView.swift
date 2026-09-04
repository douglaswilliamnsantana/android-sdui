import SwiftUI
import Shared

/// Equivalente ao HomeScreen do Android.
/// Observa o HomeViewModel e reage a loading / error / sucesso.
struct HomeView: View {
    @Environment(\.sduiColors)  private var colors
    @Environment(\.sduiSpacing) private var spacing

    @StateObject private var viewModel = HomeViewModel()

    var body: some View {
        ZStack {
            colors.background.ignoresSafeArea()

            if viewModel.isLoading {
                ProgressView("Loading…")
                    .tint(colors.primary)

            } else if let error = viewModel.error {
                VStack(spacing: spacing.s16) {
                    Image(systemName: "exclamationmark.triangle")
                        .font(.system(size: 48))
                        .foregroundStyle(colors.error)
                    Text(error)
                        .font(.caption)
                        .foregroundStyle(colors.onSurface)
                        .multilineTextAlignment(.center)
                    Button("Retry") { viewModel.retry() }
                        .foregroundStyle(colors.primary)
                }
                .padding(spacing.s24)

            } else if let reader = viewModel.node {
                if reader.type == "screen" {
                    // "screen" pins its own header/bottom slots and expands body to fill —
                    // wrapping it in a ScrollView would collapse it to its intrinsic height
                    // instead, defeating that layout.
                    SduiNodeView(reader: reader)
                        .frame(maxWidth: .infinity, maxHeight: .infinity)
                } else {
                    ScrollView {
                        SduiNodeView(reader: reader)
                            .frame(maxWidth: .infinity, alignment: .leading)
                    }
                }
            }
        }
    }
}

#Preview {
    HomeView()
        .sduiTheme()
}
