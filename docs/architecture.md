[← Índice](README.md) · [README do projeto](../README.md)

---

# Arquitetura Geral

Visão completa do sistema — camadas, fluxos, decisões de design e diagramas de todos os módulos integrados.

---

## Diagrama de módulos

![Arquitetura Geral — Módulos e Dependências](images/diagram_architecture.png)

---

## Arquitetura em camadas

```
┌─────────────────────────────────────────────────────────┐
│                         app                             │
│  ┌─────────────┐  ┌────────────┐  ┌──────────────────┐ │
│  │  App + Main │  │DesignSystem│  │  feature/home    │ │
│  │  @Hilt init │  │  Material3 │  │  Text component  │ │
│  └─────────────┘  └────────────┘  └──────────────────┘ │
└────────────────────────┬────────────────────────────────┘
                         │ depende de
        ┌────────────────┴──────────────────┐
        ▼                                   ▼
┌───────────────┐               ┌─────────────────────┐
│  sdui-core    │◄──────────────│    sdui-runtime      │
│               │    depende de │                      │
│  Node         │               │  ComponentRenderer   │
│  UIComponent  │               │  RendererRegistry    │
│  ComponentFac │               │                      │
│  ComponentReg │               │  (Compose only)      │
│  SDUIContext  │               └─────────────────────┘
│  UIAction     │
│  ActionHandler│
│  (Kotlin only)│
└───────────────┘
```

**Regra de dependência:** as setas apontam sempre para dentro. `sdui-core` não conhece ninguém; `app` conhece todos.

---

## Fluxo completo de renderização

![Arquitetura em Camadas](images/diagram_architecture.png)

---

## Fluxo de injeção de dependências (Hilt)

![Fluxo de Renderização](images/diagram_sequence.png)

---

## Diagrama completo de classes

![Fluxo de Injeção Hilt](images/diagram_hilt.png)

---

## Decisões de design

### Por que `KClass` como chave no `RendererRegistry` e não `String`?

Usar `KClass<T>` garante que o mapeamento é verificado em compile time. Com `String`, um typo em `"text"` apenas falha silenciosamente em runtime. Com `KClass`, o IDE refatora automaticamente e o compilador avisa sobre inconsistências.

### Por que os filhos são renderizados pelo `RendererRegistry` e não pelo renderer?

Centralizar a renderização recursiva no `RendererRegistry` mantém cada renderer focado em uma única responsabilidade: desenhar **seu** componente. Sem essa separação, cada renderer precisaria chamar `rendererRegistry.Render(child)` manualmente — acoplando-o ao registry.

### Por que `SDUIContext` é passado em cada `create` e não injetado como singleton?

O `SDUIContext` pode variar por tela ou por request — por exemplo, diferentes `ActionHandler` para diferentes navegações, ou `extras` com o userId da sessão atual. Injetar como singleton tornaria impossível ter contextos distintos por tela.

### Por que `UnknownComponent` em vez de lançar exceção?

SDUI recebe dados do servidor — que pode retornar tipos ainda não implementados no app, ou versões novas de tipos com nomes diferentes. Lançar exceção nesse cenário derrubaria o app por um campo desconhecido. O fallback silencioso com `Log.w` permite atualizações graduais do servidor sem forçar atualizações do app.

---

## Como escalar para múltiplas features

Para adicionar uma nova feature (ex: `checkout`):

```
feature/
├── home/          (existente)
│   ├── component/
│   ├── factory/
│   ├── renderer/
│   └── di/
│
└── checkout/      (nova)
    ├── component/
    │   ├── CheckoutButton.kt
    │   └── CheckoutSummary.kt
    ├── factory/
    │   ├── CheckoutButtonFactory.kt
    │   └── CheckoutSummaryFactory.kt
    ├── renderer/
    │   ├── CheckoutButtonRenderer.kt
    │   └── CheckoutSummaryRenderer.kt
    └── di/
        └── CheckoutSDUIModule.kt   ← único ponto de registro
```

O `ComponentRegistry` e o `RendererRegistry` recebem automaticamente os novos componentes via Hilt multibindings — **sem alterar nenhuma classe existente**.

---

[← Índice](README.md)
