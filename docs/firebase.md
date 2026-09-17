[← Índice](README.md) · [README do projeto](../README.md)

---

# Firebase — Remote Config

Como o Firebase Remote Config está integrado no projeto: uma fonte alternativa (junto do
mock server HTTP) para a tela SDUI, escolhida pelo usuário na tela de seleção
(`feature:launcher`). Ver também [`feature:launcher`](../feature/launcher/README.md) e
[`feature:home`](../feature/home/README.md).

---

## Projeto Firebase

Projeto: `android-sdui-54fee` (console.firebase.google.com). Apps registrados:

| App | Bundle/Package ID |
|---|---|
| Android | `com.douglassantana.android_sdui` |
| iOS | `com.douglassantana.iosApp` |

Remote Config habilitado, sem template publicado por padrão — use o
[CLI do mock server](#populando-dados-de-teste) pra publicar uma tela de teste.

---

## Por que SDK nativo por plataforma (não GitLive)

O Firebase Remote Config está disponível como um wrapper Kotlin Multiplatform (SDK da
GitLive), mas este projeto usa o **SDK nativo de cada plataforma** — Firebase Android SDK
em Kotlin, Firebase iOS SDK em Swift (via SPM) — em vez disso:

- O SDK iOS do Firebase é distribuído como Swift Package, e não é diretamente
  cinterop-ável a partir do Kotlin/Native sem ferramental extra (é exatamente isso que o
  SDK da GitLive resolve por baixo dos panos, mas adiciona uma camada de risco de build a
  mais).
- Cada plataforma já tem acesso ao SDK oficial, mais estável e melhor documentado, sem
  depender de um wrapper de terceiros.

A consequência arquitetural: **Android** passa as duas fontes (`Backend`/`RemoteConfig`)
pelo mesmo `FetchScreenUseCase`/`SduiRepository` (`core:data`); **iOS** busca o Remote
Config direto em Swift e só usa o Kotlin compartilhado pra parsear o JSON
(`SduiSdk.parseScreen`) — ver [`docs/ios.md`](ios.md#sduisdk) pro código exato.

---

## Android

**Onde as coisas vivem:**

| Peça | Local |
|---|---|
| `google-services.json` | `app/google-services.json` (baixado do console, não versionado) |
| Plugin Gradle | `com.google.gms.google-services` (`app/build.gradle.kts`) |
| Firebase BOM | `gradle/libs.versions.toml` → `firebaseBom` |
| Implementação real | `core/data/src/androidMain/.../remoteconfig/AndroidFirebaseRemoteConfigClient.kt` |
| Wiring Koin | `core/data/src/androidMain/.../remoteconfig/RemoteConfigClient.android.kt` (`actual platformRemoteConfigModule()`) |
| Init | `App.kt` → `FirebaseApp.initializeApp(this)` |

`AndroidFirebaseRemoteConfigClient` chama `fetchAndActivate()` (via
`kotlinx-coroutines-play-services`'s `Task<T>.await()`) e depois `getString(key)`. A chave
é derivada de `Route.path` em `RemoteConfigSduiRepositoryImpl`: `"/home"` → `"home"`.

**Setup local:** baixe `google-services.json` no console Firebase (Configurações do Projeto
→ seus apps → app Android) e coloque em `app/google-services.json`. Sem esse arquivo,
`:app:assembleDebug` falha (o plugin `google-services` exige o arquivo presente).

---

## iOS

**Onde as coisas vivem:**

| Peça | Local |
|---|---|
| `GoogleService-Info.plist` | `iosApp/iosApp/GoogleService-Info.plist` (baixado do console, não versionado) |
| SPM package | `firebase-ios-sdk` (`iosApp.xcodeproj`, produtos `FirebaseCore` + `FirebaseRemoteConfig`) |
| Init | `iOSApp.swift` → `FirebaseApp.configure()` |
| Fetch | `HomeViewModel.swift`, case `.remoteConfig` — `RemoteConfig.remoteConfig().fetchAndActivate()` |

**Setup local:** baixe `GoogleService-Info.plist` no console Firebase (app iOS) e coloque em
`iosApp/iosApp/GoogleService-Info.plist`. Se o SPM package ainda não estiver resolvido, abra
o projeto no Xcode uma vez (File → Packages → Resolve Package Versions) ou rode:
```bash
xcodebuild -resolvePackageDependencies -project iosApp/iosApp.xcodeproj -scheme iosApp
```

---

## Convenção de chave

A chave do parâmetro Remote Config é sempre a rota sem a barra inicial: rota `/home` →
chave `home`. Essa derivação é código no Android (`route.path.removePrefix("/")`) mas
precisa ser mantida manualmente em sincronia no Swift (hardcoded `"home"` em
`HomeViewModel.swift`) e no [CLI do mock server](#populando-dados-de-teste) — não há um
`Route` compartilhado do lado Swift/Node pra garantir isso automaticamente.

---

## Populando dados de teste

O repositório [`android-sdui-mock-server`](https://github.com/douglaswilliamnsantana/android-sdui-mock-server)
tem um script CLI (`npm run push-remote-config`) que publica um JSON local como parâmetro
do Remote Config, usando o Firebase Admin SDK — útil pra testar telas diferentes sem editar
nada no console manualmente.

```bash
cd android-sdui-mock-server
cp .env.example .env   # edite FIREBASE_SERVICE_ACCOUNT_PATH
npm run push-remote-config -- --key home --file ./screens/home.json
```

Requer uma service-account key (console Firebase → Configurações do Projeto → Contas de
Serviço → Gerar nova chave privada) — baixe o JSON, salve fora do controle de versão e
aponte `FIREBASE_SERVICE_ACCOUNT_PATH` pra ele (ver `.env.example` no mock server). Detalhes
completos no README do mock server.

---

[← Índice](README.md)
