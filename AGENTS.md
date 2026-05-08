# FlosunPomodoro — AGENTS.md

## Build & Run

```sh
./gradlew assembleDebug          # full build
./gradlew :app:assembleDebug     # app-only (skip :core assemble if already built)
```

## Test

```sh
./gradlew test                   # all unit tests
./gradlew :app:test              # app unit tests
./gradlew :core:test             # core unit tests
./gradlew connectedAndroidTest   # instrumentation tests (emulator/device required)
```

## Key Structure

| Path | Purpose |
|---|---|
| `:app` | Main app: MVVM screens, Room DB, DI, services, UI (Compose) |
| `:core` | Reusable Compose UI: date/time pickers, custom components |
| `libraries/shared.aar` | Prebuilt binary dependency |
| `gradle/libs.versions.toml` | Single source of truth for all dependency versions |
| `fastlane/` | CI/CD: `test`, `beta` (Crashlytics), `deploy` (Play Store), `distribute` (Firebase) |

## Required Files

- **`local.properties`** (at root) — must contain `versionCode`, `versionName`, supabase credentials (`supabaseUrl`, `supabasePublishableKey`), crypto config, OAuth client IDs. Every key becomes a `BuildConfig` field.
- **`keystore.properties`** (at root) — debug/release signing. Already committed with dev credentials.

## Non-Obvious

- `additionalSourceDirectory/kotlin` is declared in both `:app` and `:core` `build.gradle.kts` as an extra source set. The directory may or may not exist.
- Room schemas export to `app/schemas/`. There are two schema directories — one under the correct namespace (`com.flosun.pomodoro`) and one under a typo (`com.flosunn.pomodoro`). Both are active.
- Namespace mismatch: `:app` is `com.flosun.pomodoro`, `:core` is `com.flosunn.core` (double `n`).
- Uses **Navigation 3** (`androidx.navigation3`), not the standard Navigation Compose. Navigate via `NavBackStack<NavKey>` with `NavRoute` sealed class entries.
- ProGuard rules files exist but `isMinifyEnabled = false` for both `debug` and `release` builds.
- Hilt DI with KSP (Kotlin Symbol Processing), not kapt.
- ABIs filtered to `arm64-v8a` only.
- Two map SDKs co-exist: Google Maps Compose (`maps-compose`) and MapLibre Compose (`maplibre-compose`).

## Dependencies (notable)

| Category | Libs |
|---|---|
| DI | Hilt + KSP |
| DB | Room + KSP |
| Backend | Supabase (Postgrest, Auth, Realtime), Ktor |
| Auth | Google Credentials API, Biometric |
| Async | Kotlin Coroutines |
| Image | Coil 3 |
| Media | Media3 (ExoPlayer) |
| Maps | maps-compose + maplibre-compose |
| Storage | DataStore Preferences, EncryptedStorage (AES) |
| Style | Official Kotlin code style, Compose Material 3 |
