import SwiftUI
import Shared

// MARK: - SDUI recursive renderer

struct SduiNodeView: View {
    let reader: NodeReader

    var body: some View {
        switch reader.type {
        case "text":
            SduiTextView(reader: reader)
        case "column":
            VStack(alignment: .leading, spacing: 0) {
                ForEach(Array((reader.children as! [NodeReader]).enumerated()), id: \.offset) { _, child in
                    SduiNodeView(reader: child)
                }
            }
        case "row":
            HStack(spacing: 0) {
                ForEach(Array((reader.children as! [NodeReader]).enumerated()), id: \.offset) { _, child in
                    SduiNodeView(reader: child)
                }
            }
        case "screen":
            SduiScreenView(reader: reader)
        case "app_bar":
            SduiAppBarView(reader: reader)
        default:
            EmptyView()
        }
    }
}

// MARK: - Screen component (header / body / bottom)

/// Equivalente ao SduiScreen do Android: três regiões fixas — header, body, bottom —
/// identificadas pelo `type` de cada filho no JSON, não pela posição.
struct SduiScreenView: View {
    let reader: NodeReader

    private var slots: [NodeReader] { reader.children as! [NodeReader] }
    private var headerSlot: NodeReader? { slots.first { $0.type == "header" } }
    private var bodySlot: NodeReader? { slots.first { $0.type == "body" } }
    private var bottomSlot: NodeReader? { slots.first { $0.type == "bottom" } }

    var body: some View {
        VStack(spacing: 0) {
            if let headerSlot {
                SduiScreenSlotView(reader: headerSlot)
            }
            if let bodySlot {
                SduiScreenSlotView(reader: bodySlot)
                    .frame(maxHeight: .infinity)
            }
            if let bottomSlot {
                SduiScreenSlotView(reader: bottomSlot)
            }
        }
    }
}

/// Renderiza os filhos de um slot "header"/"body"/"bottom" — o slot em si não tem
/// identidade visual própria, só agrupa conteúdo (equivalente a SduiHeader/SduiBody/
/// SduiBottom no Android).
private struct SduiScreenSlotView: View {
    let reader: NodeReader

    var body: some View {
        VStack(alignment: .leading, spacing: 0) {
            ForEach(Array((reader.children as! [NodeReader]).enumerated()), id: \.offset) { _, child in
                SduiNodeView(reader: child)
            }
        }
    }
}

// MARK: - AppBar component

/// Equivalente ao AndroidSduiAppBar + SduiAppBarFactory/Renderer do Android: mesmos
/// quatro tipos ("small"/"center-aligned"/"medium"/"large") e o mesmo preset fixo de
/// ícones ("back"/"close"/"menu"/"search"/"more"), lidos diretamente do JSON — não há
/// ComponentFactory/Registry no lado iOS, então a leitura de props acontece aqui mesmo.
struct SduiAppBarView: View {
    let reader: NodeReader

    private var type: String { reader.stringProp(key: "type") ?? "small" }
    private var title: String { reader.stringProp(key: "title") ?? "" }
    private var leftSymbol: String? { reader.stringProp(key: "leftIcon")?.toSFSymbol() }
    private var rightSymbol: String? { reader.stringProp(key: "rightIcon")?.toSFSymbol() }
    private var leftAction: String? { reader.stringProp(key: "leftAction") }
    private var rightAction: String? { reader.stringProp(key: "rightAction") }

    var body: some View {
        switch type {
        case "medium":
            SduiAppBarLargeStyle(
                title: title, titleFont: .title2,
                leftSymbol: leftSymbol, onLeftTap: { dispatch(leftAction) },
                rightSymbol: rightSymbol, onRightTap: { dispatch(rightAction) }
            )
        case "large":
            SduiAppBarLargeStyle(
                title: title, titleFont: .largeTitle,
                leftSymbol: leftSymbol, onLeftTap: { dispatch(leftAction) },
                rightSymbol: rightSymbol, onRightTap: { dispatch(rightAction) }
            )
        case "center-aligned":
            SduiAppBarRow(
                title: title, centered: true,
                leftSymbol: leftSymbol, onLeftTap: { dispatch(leftAction) },
                rightSymbol: rightSymbol, onRightTap: { dispatch(rightAction) }
            )
        default:
            SduiAppBarRow(
                title: title, centered: false,
                leftSymbol: leftSymbol, onLeftTap: { dispatch(leftAction) },
                rightSymbol: rightSymbol, onRightTap: { dispatch(rightAction) }
            )
        }
    }

    /// PT: Não há ActionHandler real conectado ainda em nenhuma das duas plataformas
    ///     (SDUIContext() do Android também nasce com actionHandler nulo) — por ora só
    ///     registra a rota no console, pra dar visibilidade durante o teste manual.
    /// EN: No real ActionHandler is wired up yet on either platform (Android's
    ///     SDUIContext() also starts with a null actionHandler) — for now this just
    ///     logs the route, to give visibility during manual testing.
    private func dispatch(_ route: String?) {
        guard let route else { return }
        print("SDUI navigate: \(route)")
    }
}

/// Layout de linha única — usado por "small" (título alinhado ao ícone esquerdo) e
/// "center-aligned" (título centralizado via ZStack, independente do tamanho dos ícones).
private struct SduiAppBarRow: View {
    let title: String
    let centered: Bool
    let leftSymbol: String?
    let onLeftTap: () -> Void
    let rightSymbol: String?
    let onRightTap: () -> Void

    var body: some View {
        ZStack {
            if centered {
                Text(title).font(.headline).lineLimit(1)
            }
            HStack(spacing: 8) {
                SduiAppBarIconButton(symbol: leftSymbol, action: onLeftTap)
                if !centered {
                    Text(title).font(.headline).lineLimit(1)
                }
                Spacer()
                SduiAppBarIconButton(symbol: rightSymbol, action: onRightTap)
            }
        }
        .padding(.horizontal, 16)
        .padding(.vertical, 12)
    }
}

/// Layout de duas linhas — usado por "medium"/"large": ícones em cima, título grande
/// embaixo, alinhado à esquerda. Equivalente ao MediumTopAppBar/LargeTopAppBar do Android.
private struct SduiAppBarLargeStyle: View {
    let title: String
    let titleFont: Font
    let leftSymbol: String?
    let onLeftTap: () -> Void
    let rightSymbol: String?
    let onRightTap: () -> Void

    var body: some View {
        VStack(alignment: .leading, spacing: 4) {
            HStack {
                SduiAppBarIconButton(symbol: leftSymbol, action: onLeftTap)
                Spacer()
                SduiAppBarIconButton(symbol: rightSymbol, action: onRightTap)
            }
            Text(title)
                .font(titleFont)
                .fontWeight(.semibold)
                .lineLimit(1)
        }
        .padding(.horizontal, 16)
        .padding(.top, 8)
        .padding(.bottom, 12)
    }
}

/// Nenhum botão é desenhado quando `symbol` é nulo — mesmo comportamento do
/// AndroidSduiAppBar quando leftIcon/rightIcon é nulo.
private struct SduiAppBarIconButton: View {
    let symbol: String?
    let action: () -> Void

    var body: some View {
        if let symbol {
            Button(action: action) {
                Image(systemName: symbol)
            }
        }
    }
}

/// Mesmo preset fixo de nomes do Android (ver IconExtensions.kt) — mapeado para SF
/// Symbols em vez de Material Icons. Nomes fora da lista são usados como está,
/// direto como nome de SF Symbol (inofensivo se inválido: a Image renderiza vazia).
private extension String {
    func toSFSymbol() -> String {
        switch self {
        case "back":   return "chevron.left"
        case "close":  return "xmark"
        case "menu":   return "line.3.horizontal"
        case "search": return "magnifyingglass"
        case "more":   return "ellipsis"
        default:       return self
        }
    }
}

// MARK: - Text component

struct SduiTextView: View {
    let reader: NodeReader

    private var text:       String      { reader.stringProp(key: "text") ?? "" }
    private var color:      String?     { style?.stringProp(key: "color") }
    private var fontSize:   CGFloat     { CGFloat(style?.doubleProp(key: "fontSize")?.doubleValue ?? 16) }
    private var fontWeight: String?     { style?.stringProp(key: "fontWeight") }
    private var style:      NodeReader? { reader.objectProp(key: "style") }

    private var padding: EdgeInsets {
        guard let p = style?.objectProp(key: "padding") else { return .init() }
        func dp(_ key: String) -> CGFloat { CGFloat(p.doubleProp(key: key)?.doubleValue ?? 0) }
        return EdgeInsets(top: dp("top"), leading: dp("start"), bottom: dp("bottom"), trailing: dp("end"))
    }

    var body: some View {
        Text(text)
            .font(.system(size: fontSize, weight: swiftWeight))
            .foregroundStyle(colorValue)
            .padding(padding)
    }

    private var colorValue: Color {
        guard let hex = color else { return .primary }
        return Color(hex: hex) ?? .primary
    }

    private var swiftWeight: Font.Weight {
        switch fontWeight {
        case "bold":      return .bold
        case "semi-bold": return .semibold
        case "light":     return .light
        case "medium":    return .medium
        default:          return .regular
        }
    }
}

// MARK: - Hex color helper

extension Color {
    init?(hex: String) {
        let h = hex.trimmingCharacters(in: .init(charactersIn: "#"))
        guard h.count == 6, let value = UInt64(h, radix: 16) else { return nil }
        self.init(
            .sRGB,
            red:   Double((value >> 16) & 0xFF) / 255,
            green: Double((value >> 8)  & 0xFF) / 255,
            blue:  Double( value        & 0xFF) / 255
        )
    }
}
