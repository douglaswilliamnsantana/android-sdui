# Design System — core/designsystem

> EN: Visual foundation of the android-sdui project — centralizes all design tokens, color palettes, typography scale and theme configuration.
>
> PT: Fundação visual do projeto android-sdui — centraliza todos os tokens de design, paletas de cores, escala tipográfica e configuração de tema.

---

## Structure / Estrutura

```
core/designsystem/
└── src/main/
    ├── res/font/
    │   ├── inter_light.ttf
    │   ├── inter_regular.ttf
    │   ├── inter_medium.ttf
    │   ├── inter_semibold.ttf
    │   └── inter_bold.ttf
    └── kotlin/com/douglassantana/designsystem/
        ├── colors/
        │   ├── ColorsLight.kt   → light palette tokens
        │   ├── ColorsDark.kt    → dark palette tokens
        │   └── Color.kt         → Material3 light/dark color schemes
        ├── foundations/
        │   ├── Spacing.kt       → spacing scale
        │   └── Radius.kt        → corner radius scale
        ├── typography/
        │   ├── FontFamily.kt    → Inter font family definition
        │   ├── FontSize.kt      → font size scale
        │   ├── LineHeight.kt    → line height scale
        │   ├── FontWeight.kt    → font weight scale
        │   └── Typography.kt    → Material3 typography scale
        └── theme/
            └── Theme.kt         → AndroidSduiTheme
```

---

## Usage / Uso

```kotlin
AndroidSduiTheme {
    MyScreen()
}
```

EN: The theme automatically switches between light and dark based on the system preference via `isSystemInDarkTheme()`.  
PT: O tema alterna automaticamente entre claro e escuro com base na preferência do sistema via `isSystemInDarkTheme()`.

---

## Theme

### Light Color Scheme
| Role | Token | Hex |
|---|---|---|
| `primary` | `Primary500` | `#2A3F5F` |
| `onPrimary` | `White` | `#FFFFFF` |
| `background` | `Neutral50` | `#F7F8F9` |
| `onBackground` | `Neutral900` | `#1A202C` |
| `surface` | `White` | `#FFFFFF` |
| `onSurface` | `Neutral600` | `#4A5568` |
| `outline` | `Neutral100` | `#EAECEF` |
| `outlineVariant` | `Neutral200` | `#C4C9D4` |
| `error` | `ErrorLight` | `#C0392B` |
| `errorContainer` | `ErrorContainerLight` | `#FADED8` |

### Dark Color Scheme
| Role | Token | Hex |
|---|---|---|
| `primary` | `PrimaryDark400` | `#8F9BB3` |
| `onPrimary` | `NeutralDark50` | `#0F1523` |
| `background` | `NeutralDark50` | `#0F1523` |
| `onBackground` | `NeutralDark900` | `#EAECEF` |
| `surface` | `NeutralDark100` | `#1A2236` |
| `onSurface` | `NeutralDark600` | `#8E96A8` |
| `outline` | `NeutralDark200` | `#2A3347` |
| `outlineVariant` | `NeutralDark400` | `#3D4F6B` |
| `error` | `ErrorDark` | `#E74C3C` |
| `errorContainer` | `ErrorContainerDark` | `#4A1C18` |

---

## Colors

### Primary Light
| Token | Hex |
|---|---|
| `Primary50`  | `#E8EAED` |
| `Primary100` | `#C5CAD3` |
| `Primary200` | `#8F9BB3` |
| `Primary300` | `#5C6B8A` |
| `Primary400` | `#3D5070` |
| `Primary500` | `#2A3F5F` |
| `Primary600` | `#1E2F47` |

### Primary Dark
| Token | Hex |
|---|---|
| `PrimaryDark50`  | `#1E2F47` |
| `PrimaryDark100` | `#2A3F5F` |
| `PrimaryDark200` | `#3D5070` |
| `PrimaryDark300` | `#5C6B8A` |
| `PrimaryDark400` | `#8F9BB3` |
| `PrimaryDark500` | `#C5CAD3` |
| `PrimaryDark600` | `#E8EAED` |

### Neutral Light
| Token | Hex |
|---|---|
| `Neutral50`  | `#F7F8F9` |
| `Neutral100` | `#EAECEF` |
| `Neutral200` | `#C4C9D4` |
| `Neutral400` | `#8E96A8` |
| `Neutral600` | `#4A5568` |
| `Neutral900` | `#1A202C` |

### Neutral Dark
| Token | Hex |
|---|---|
| `NeutralDark50`  | `#0F1523` |
| `NeutralDark100` | `#1A2236` |
| `NeutralDark200` | `#2A3347` |
| `NeutralDark400` | `#3D4F6B` |
| `NeutralDark600` | `#8E96A8` |
| `NeutralDark900` | `#EAECEF` |

### Semantic Light
| Token | Hex | Container |
|---|---|---|
| `SuccessLight` | `#1E8449` | `SuccessContainerLight` `#D5F5E3` |
| `ErrorLight`   | `#C0392B` | `ErrorContainerLight` `#FADED8` |
| `WarningLight` | `#9A6700` | `WarningContainerLight` `#FEF3CD` |
| `InfoLight`    | `#1A6FA8` | `InfoContainerLight` `#D6EAF8` |

### Semantic Dark
| Token | Hex | Container |
|---|---|---|
| `SuccessDark` | `#2ECC71` | `SuccessContainerDark` `#1A4731` |
| `ErrorDark`   | `#E74C3C` | `ErrorContainerDark` `#4A1C18` |
| `WarningDark` | `#F39C12` | `WarningContainerDark` `#4A3200` |
| `InfoDark`    | `#3498DB` | `InfoContainerDark` `#1A3A4A` |

---

## Foundations

### Spacing
| Token | Value | Usage / Uso |
|---|---|---|
| `Spacing.spacing2`  | `2.dp`  | Micro gaps |
| `Spacing.spacing4`  | `4.dp`  | Icon inner padding |
| `Spacing.spacing8`  | `8.dp`  | Small gaps between elements |
| `Spacing.spacing12` | `12.dp` | Compact component padding |
| `Spacing.spacing16` | `16.dp` | Default content padding |
| `Spacing.spacing24` | `24.dp` | Section spacing |
| `Spacing.spacing32` | `32.dp` | Large section spacing |
| `Spacing.spacing48` | `48.dp` | Screen-level padding |
| `Spacing.spacing72` | `72.dp` | Hero spacing |
| `Spacing.spacing96` | `96.dp` | Full-screen sections |

```kotlin
Modifier.padding(Spacing.spacing16)
Modifier.padding(horizontal = Spacing.spacing24, vertical = Spacing.spacing16)
```

### Radius
| Token | Value | Usage / Uso |
|---|---|---|
| `Radius.RadiusNone`   | `0.dp`  | Sharp corners |
| `Radius.RadiusXSmall` | `2.dp`  | Subtle rounding (chips, tags) |
| `Radius.RadiusSmall`  | `4.dp`  | Buttons, inputs |
| `Radius.RadiusMedium` | `8.dp`  | Cards, dialogs |
| `Radius.RadiusLarge`  | `16.dp` | Sheets, modals |
| `Radius.RadiusXLarge` | `24.dp` | Large cards |
| `Radius.RadiusFull`   | `50.dp` | Pills, avatars |

```kotlin
RoundedCornerShape(Radius.RadiusMedium)
Modifier.clip(RoundedCornerShape(Radius.RadiusFull))
```

---

## Typography

**Font / Fonte:** Inter 24pt — loaded locally / carregada localmente em `res/font/`

EN: Inter was chosen for its excellent readability on digital interfaces and its wide adoption in modern design systems.  
PT: A Inter foi escolhida pela excelente legibilidade em interfaces digitais e sua ampla adoção em design systems modernos.

### Font Family
| File | Weight |
|---|---|
| `inter_light.ttf`    | `300` Light |
| `inter_regular.ttf`  | `400` Regular |
| `inter_medium.ttf`   | `500` Medium |
| `inter_semibold.ttf` | `600` SemiBold |
| `inter_bold.ttf`     | `700` Bold |

### Font Size
| Token | Value | Style |
|---|---|---|
| `FontSize.DisplayLarge`  | `57.sp` | Display |
| `FontSize.DisplayMedium` | `45.sp` | Display |
| `FontSize.DisplaySmall`  | `36.sp` | Display |
| `FontSize.HeadlineLarge`  | `32.sp` | Headline |
| `FontSize.HeadlineMedium` | `28.sp` | Headline |
| `FontSize.HeadlineSmall`  | `24.sp` | Headline |
| `FontSize.TitleLarge`  | `22.sp` | Title |
| `FontSize.TitleMedium` | `16.sp` | Title |
| `FontSize.TitleSmall`  | `14.sp` | Title |
| `FontSize.BodyLarge`  | `16.sp` | Body |
| `FontSize.BodyMedium` | `14.sp` | Body |
| `FontSize.BodySmall`  | `12.sp` | Body |
| `FontSize.LabelLarge`  | `14.sp` | Label |
| `FontSize.LabelMedium` | `12.sp` | Label |
| `FontSize.LabelSmall`  | `11.sp` | Label |

### Line Height
| Token | Value |
|---|---|
| `LineHeight.DisplayLarge`  | `64.sp` |
| `LineHeight.DisplayMedium` | `52.sp` |
| `LineHeight.DisplaySmall`  | `44.sp` |
| `LineHeight.HeadlineLarge`  | `40.sp` |
| `LineHeight.HeadlineMedium` | `36.sp` |
| `LineHeight.HeadlineSmall`  | `32.sp` |
| `LineHeight.TitleLarge`  | `28.sp` |
| `LineHeight.TitleMedium` | `24.sp` |
| `LineHeight.TitleSmall`  | `20.sp` |
| `LineHeight.BodyLarge`  | `24.sp` |
| `LineHeight.BodyMedium` | `20.sp` |
| `LineHeight.BodySmall`  | `16.sp` |
| `LineHeight.LabelLarge`  | `20.sp` |
| `LineHeight.LabelMedium` | `16.sp` |
| `LineHeight.LabelSmall`  | `16.sp` |

### Font Weight
| Token | Value |
|---|---|
| `FontWeight.Light`    | `300` |
| `FontWeight.Normal`   | `400` |
| `FontWeight.Medium`   | `500` |
| `FontWeight.SemiBold` | `600` |
| `FontWeight.Bold`     | `700` |

### Typography Scale
| Style | FontSize | LineHeight | FontWeight | Usage / Uso |
|---|---|---|---|---|
| `displayLarge`  | `57.sp` | `64.sp` | Bold | Hero sections, splash |
| `displayMedium` | `45.sp` | `52.sp` | Bold | Hero sections |
| `displaySmall`  | `36.sp` | `44.sp` | Bold | Hero sections |
| `headlineLarge`  | `32.sp` | `40.sp` | SemiBold | Section headers |
| `headlineMedium` | `28.sp` | `36.sp` | SemiBold | Section headers |
| `headlineSmall`  | `24.sp` | `32.sp` | SemiBold | Section headers |
| `titleLarge`  | `22.sp` | `28.sp` | SemiBold | Screen titles |
| `titleMedium` | `16.sp` | `24.sp` | Medium | Card headers |
| `titleSmall`  | `14.sp` | `20.sp` | Medium | Subtitles |
| `bodyLarge`  | `16.sp` | `24.sp` | Normal | Main content |
| `bodyMedium` | `14.sp` | `20.sp` | Normal | Descriptions |
| `bodySmall`  | `12.sp` | `16.sp` | Normal | Captions |
| `labelLarge`  | `14.sp` | `20.sp` | Medium | Buttons |
| `labelMedium` | `12.sp` | `16.sp` | Medium | Tags, chips |
| `labelSmall`  | `11.sp` | `16.sp` | Medium | Helper texts |

```kotlin
Text(text = "Title", style = MaterialTheme.typography.titleLarge)
Text(text = "Body", style = MaterialTheme.typography.bodyMedium)
Text(text = "Button", style = MaterialTheme.typography.labelLarge)
```

---

## Back / Voltar

[← README do projeto](../../README.md)