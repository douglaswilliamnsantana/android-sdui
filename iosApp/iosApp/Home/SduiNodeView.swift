import SwiftUI
import Shared

// MARK: - SDUI recursive renderer

struct SduiNodeView: View {
    let reader: NodeReader

    var body: some View {
        switch reader.type {
        case "text":
            SduiTextView(reader: reader)
        case "column", "screen":
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
        default:
            EmptyView()
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
