# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build Commands

```bash
# Build all modules
./gradlew build

# Android
./gradlew assembleDebug
./gradlew assembleRelease
./gradlew installDebug

# iOS: open iosApp/iosApp.xcodeproj in Xcode
```

There is no test suite currently — test directories exist but are empty.

## Architecture

**Target platforms:** Android (API 24+) and iOS only. Desktop/Web are not targeted.

**Module structure (Clean Architecture):**

| Module | Role |
|---|---|
| `composeApp` | App entry point, navigation, screens, ViewModels |
| `game/domain` | Use cases, repository interfaces, business logic |
| `game/data` | Repository implementations, data sources |
| `designsystem` | Shared Compose UI components, themes, animations |
| `platform` | `expect`/`actual` abstractions for audio & preferences |
| `impl/database` | SQLDelight score persistence |
| `common/common_shared` | Shared models: `AstorCard`, `Score`, `Resource<T>`, exceptions |
| `common/common_data` | Shared data utilities: `DatabaseService` |

**Dependency flow:** `composeApp` → `game/data` → `game/domain` → `common/common_shared`

## Key Patterns

**Dependency Injection (Koin 4.1.1):** All modules wired in `composeApp/.../di/AppModules.kt`. Use cases are factories; repositories and preferences are singletons. Platform-specific bindings live in `platformModule` (expect/actual).

**State management:** `GameViewModel` exposes `MutableStateFlow` for game state. Global app settings (dark mode, mute, layout, difficulty) live in `AppState` (`mutableStateOf`).

**Platform abstraction:** `AppPreferences` (SharedPreferences / NSUserDefaults), `AudioPlayer`, and `MusicPlayer` are `expect` declarations in `platform` with platform-specific `actual` implementations.

**Navigation:** Type-safe route objects (`Menu`, `Game`, `GameOver`, `HighScores`, `Settings`) in `AstorMemoryApp.kt`. Music track changes are triggered on route transitions.

**Resource pattern:** `Resource<T>` sealed class (`Loading` / `Success` / `Error`) is used throughout the data/domain boundary.

**Scoring:** Base `amount * 100` per pair matched, +10 combo bonus per consecutive match, recalculated on mismatch via `CalculateScoreUseCase`.

**Database:** SQLDelight (`scoreEntity` table), queried with coroutine extensions. Scores indexed by `amount` and `score DESC`.

## Build Logic

Convention plugins live in `build-logic/convention/`:
- `AndroidAppMultiplatformConventionPlugin`
- `AndroidLibraryMultiplatformConventionPlugin`
- `ComposeMultiplatformConventionPlugin`
- `KotlinMultiplatformConventionPlugin`

Key versions: Kotlin 2.3.0, Compose Multiplatform 1.9.3, Koin 4.1.1, SQLDelight 2.2.1, Gradle 8.13.2.
