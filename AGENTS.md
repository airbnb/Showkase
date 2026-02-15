# AGENTS.md

This file provides guidance to AI coding agents working with code in this repository.

## What is Showkase?

Showkase is an annotation-processor-based Android library for organizing and browsing Jetpack Compose UI components, colors, and typography. It processes `@ShowkaseComposable`, `@ShowkaseColor`, `@ShowkaseTypography`, and `@Preview` annotations to generate a browsable UI catalog and screenshot test files.

## Build Commands

```bash
# Build entire project
./gradlew build

# Run all checks (unit tests + detekt linting)
./gradlew check

# Run checks with KSP instead of KAPT (default is KAPT)
./gradlew check -PuseKsp=true

# Run detekt linting only
./gradlew detekt

# Build a single module
./gradlew :showkase-processor:build

# Run processor unit tests
./gradlew :showkase-processor:test

# Run a single test class
./gradlew :showkase-processor:test --tests ShowkaseMetadataTest

# Run a single test method
./gradlew :showkase-processor:test --tests ShowkaseMetadataTest.isTopLevelFunction

# Paparazzi screenshot tests
./gradlew :showkase-screenshot-testing-paparazzi-sample:verifyPaparazziDebug -PuseKsp=true

# Instrumented tests (requires emulator/device)
./gradlew connectedCheck
```

## Architecture

### Module Structure

- **`showkase-annotation`** — Pure Java library defining the annotations (`@ShowkaseComposable`, `@ShowkaseColor`, `@ShowkaseTypography`, `@ShowkaseRoot`). No Android dependencies.
- **`showkase-processor`** — Annotation processor (supports both KSP and KAPT) that processes annotations and generates browser metadata, extension functions, and screenshot test files. Uses XProcessing (`androidx.room:room-compiler-processing`) as a unified abstraction over KSP/KAPT and KotlinPoet for code generation.
- **`showkase`** — Android library providing the runtime UI browser (`ShowkaseBrowserActivity` and Compose screens). Contains the `Showkase` object, `ShowkaseProvider` interface, and data models (`ShowkaseBrowserComponent`, `ShowkaseBrowserColor`, `ShowkaseBrowserTypography`).
- **`showkase-screenshot-testing`** / **`showkase-screenshot-testing-paparazzi`** / **`showkase-screenshot-testing-shot`** — Screenshot testing integrations for Paparazzi and Shot frameworks.
- **`showkase-processor-testing`** — Test harness for the annotation processor using kotlin-compile-testing and XProcessing testing APIs.
- **`sample`**, **`sample-submodule`**, **`sample-submodule-2`** — Sample app demonstrating multi-module usage.

### Annotation Processing Flow

1. `ShowkaseProcessor` (entry point) extends `BaseProcessor` which unifies KSP and KAPT via XProcessing.
2. Processing rounds collect annotated elements into `ShowkaseMetadata` models (components, colors, typography).
3. Writer classes generate code:
   - `ShowkaseBrowserWriter` → `ShowkaseProvider` implementation
   - `ShowkaseBrowserPropertyWriter` → individual component/color/typography properties
   - `ShowkaseExtensionFunctionsWriter` → `Showkase.getBrowserIntent()` and `Showkase.getMetadata()`
   - `ShowkaseScreenshotTestWriter` / `PaparazziShowkaseScreenshotTestWriter` → screenshot test files

### Multi-Module Support

One module declares `@ShowkaseRoot` to aggregate UI elements from all dependent modules. The processor generates `@ShowkaseRootCodegen`-annotated code in the root module's package, collecting metadata from all submodules.

### KSP vs KAPT

- Default is KAPT; pass `-PuseKsp=true` to use KSP.
- The `useKsp` property in `build.gradle` files controls which plugin is applied in sample/test modules.
- Processor compiler options: `skipPrivatePreviews`, `requireShowkaseComposableAnnotation`, `multiPreviewType` (KAPT only for custom preview annotations).

## Testing

- **Unit tests**: JUnit 4 with Strikt assertions. Processor tests use kotlin-compile-testing.
- **Screenshot tests**: Paparazzi (JVM-based, no device needed) and Shot (requires emulator).
- **Instrumented tests**: Standard Android instrumented tests via `connectedCheck`.
- Processor tests in `showkase-processor-testing` require JDK 16+ JVM args for compiler module access (configured in its `build.gradle`).

## Code Style

- Kotlin official code style (`kotlin.code.style=official`)
- Detekt for static analysis (config: `detekt/detekt.yml`)
- Java 17 toolchain
