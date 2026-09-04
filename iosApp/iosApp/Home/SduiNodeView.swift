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
