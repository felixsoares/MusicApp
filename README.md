# MusicApp

An Android music application that allows users to search for songs, save them locally, play 30-second previews, and browse album tracks — all powered by the iTunes Search API.

---

## Technical Specifications

| Property | Value |
|---|---|
| **Language** | Kotlin `2.3.20` |
| **Min SDK** | 30 (Android 11) |
| **Target SDK** | 37 (Android 15) |
| **Compile SDK** | 37 |
| **Version Code** | 1 |
| **Version Name** | 1.0 |
| **Java Compatibility** | VERSION_11 |
| **Application ID** | `com.mobile.felix.musicapp` |
| **Android Gradle Plugin** | `9.2.1` |
| **KSP** | `2.3.9` |
| **Compose BOM** | `2026.05.01` |

---

## Architecture

The project follows **Clean Architecture** combined with the **MVVM** (Model-View-ViewModel) pattern and a **unidirectional data flow** via sealed `Action` interfaces and `StateFlow`.

```
┌──────────────────────────────────────────────────┐
│                  Presentation Layer              │
│  ViewModel → UiState (StateFlow) → Composable    │
│  User Events → Action → ViewModel                │
├──────────────────────────────────────────────────┤
│                   Domain Layer                   │
│  Repository Interfaces · DataSource Interfaces   │
│  Use Cases · Domain Models                       │
├──────────────────────────────────────────────────┤
│                    Data Layer                    │
│  RepositoryImpl · DataSourceImpl                 │
│  Remote (Retrofit/API) · Local (Room/DAO)        │
└──────────────────────────────────────────────────┘
```

### Key Patterns
- **State Hoisting** — UI state owned by parent composables, children receive values and callbacks
- **Pending Actions** — `MutableSharedFlow<Action>` collects user events before dispatching them inside the ViewModel
- **Single Source of Truth** — local Room database as the source of truth for saved songs
- **Dependency Injection** — Hilt manages all dependency graphs, scoped to `SingletonComponent` or `ViewModelComponent` as needed

---

## Project Structure

```
app/src/main/java/com/mobile/felix/musicapp/
│
├── core/
│   ├── data/
│   │   ├── local/
│   │   │   ├── dao/          # Room DAOs
│   │   │   └── entity/       # Room entities
│   │   └── remote/
│   │       └── response/     # API response models
│   ├── di/
│   │   ├── NetworkModule.kt  # Retrofit, OkHttp
│   │   └── CoroutineModule.kt
│   ├── domain/
│   │   ├── Failure.kt        # Sealed error types
│   │   ├── Result.kt         # Sealed result wrapper
│   │   └── Song.kt           # Core domain model
│   ├── mapper/
│   │   └── MusicMapper.kt    # Response → Domain, Domain → Entity
│   └── presentation/
│       ├── Navigation.kt     # NavHost + routes
│       ├── Router.kt         # Typed route definitions
│       ├── AlbumActionSheet.kt
│       ├── ErrorContentView.kt
│       └── LoadingView.kt
│
├── feature/
│   ├── home/
│   │   ├── data/
│   │   │   ├── repository/   # HomeRepositoryImpl
│   │   │   ├── source/       # HomeRemoteDataSourceImpl, HomeLocalDataSourceImpl
│   │   │   └── useCase/      # GetSongsByTermUseCase, GetLocalSongsUseCase, SaveSongUseCase
│   │   ├── di/               # HomeModule (Hilt)
│   │   ├── domain/
│   │   │   ├── repository/   # HomeRepository interface
│   │   │   └── source/       # HomeRemoteDataSource, HomeLocalDataSource interfaces
│   │   └── presentation/
│   │       ├── HomeScreen.kt
│   │       ├── HomeViewModel.kt
│   │       ├── HomeUiState.kt
│   │       └── action/HomeAction.kt
│   │
│   ├── song/
│   │   ├── data/
│   │   │   ├── player/       # AudioPlayerImpl (ExoPlayer)
│   │   │   ├── repository/   # SongRepositoryImpl
│   │   │   ├── source/       # SongLocalDataSourceImpl
│   │   │   └── useCase/      # GetSongUseCase
│   │   ├── di/               # SongModule, AudioPlayerModule (ViewModelScoped)
│   │   ├── domain/
│   │   │   ├── player/       # AudioPlayer interface
│   │   │   ├── repository/   # SongRepository interface
│   │   │   └── source/       # SongLocalDataSource interface
│   │   └── presentation/
│   │       ├── SongScreen.kt
│   │       ├── SongViewModel.kt
│   │       ├── SongUiState.kt
│   │       └── action/SongAction.kt
│   │
│   └── album/
│       ├── data/
│       │   ├── repository/   # AlbumRepositoryImpl
│       │   ├── source/       # AlbumDataSourceImpl
│       │   └── useCase/      # GetAlbumUseCase
│       ├── di/               # AlbumModule (Hilt)
│       ├── domain/
│       │   ├── repository/   # AlbumRepository interface
│       │   └── source/       # AlbumDataSource interface
│       └── presentation/
│           ├── AlbumScreen.kt
│           ├── AlbumViewModel.kt
│           ├── AlbumState.kt
│
app/src/test/                 # Unit tests (JVM)
app/src/androidTest/          # Instrumented tests
```

---

## Features

| Screen           | Description                                                                                          |
|------------------|------------------------------------------------------------------------------------------------------|
| **Home**         | Search songs via iTunes API with debounced query, save songs locally, browse saved songs             |
| **Song**         | Play 30-second audio preview with playback controls (play/pause, fast-forward, rewind, repeat, seek) |
| **Album**        | View all tracks of an album fetched from the iTunes lookup API                                       |
| **Splashscreen** | Initial screen displayed to users when they launch an application                                    |

---

## Libraries

### UI
| Library | Version | Purpose |
|---|---|---|
| Jetpack Compose BOM | `2026.05.01` | Compose dependency management |
| Material 3 | via BOM | Design system components |
| Compose Animation | `1.11.2` | Animated visibility and transitions |
| Compose Navigation | `2.9.0` | Type-safe navigation with `NavHost` |
| Coil | `2.7.0` | Async image loading in Compose |

### Architecture & DI
| Library | Version | Purpose |
|---|---|---|
| Hilt Android | `2.59.2` | Dependency injection |
| Hilt Navigation Compose | `1.2.0` | `hiltViewModel()` in Compose |
| Lifecycle Runtime KTX | `2.10.0` | `collectAsStateWithLifecycle`, `LifecycleEventEffect` |

### Network
| Library | Version | Purpose |
|---|---|---|
| Retrofit | `2.11.0` | HTTP client |
| Retrofit Gson Converter | `2.11.0` | JSON deserialization |
| OkHttp Logging Interceptor | `4.12.0` | Network request logging |
| Gson | `2.11.0` | JSON parsing |
| Kotlinx Serialization JSON | `1.8.1` | Kotlin-native serialization (routes) |

### Local Storage
| Library | Version | Purpose |
|---|---|---|
| Room Runtime | `2.7.1` | SQLite ORM |
| Room KTX | `2.7.1` | Coroutine and Flow extensions |
| Room Compiler (KSP) | `2.7.1` | DAO code generation |

### Media
| Library | Version | Purpose |
|---|---|---|
| Media3 ExoPlayer | `1.3.1` | Audio playback engine |
| Media3 Session | `1.3.1` | Media session management |
| Media3 UI | `1.3.1` | Media UI components |

### Paging
| Library | Version | Purpose |
|---|---|---|
| Paging Runtime | `3.3.6` | Pagination support |
| Paging Compose | `3.3.6` | Compose integration for paging |

### Testing
| Library | Version | Purpose |
|---|---|---|
| JUnit 4 | `4.13.2` | Unit test runner |
| MockK | `1.13.17` | Kotlin-first mocking library |
| kotlinx-coroutines-test | `1.10.2` | `runTest`, `TestDispatcher`, `advanceUntilIdle` |
| Espresso Core | `3.7.0` | UI instrumented tests |
| Compose UI Test JUnit4 | via BOM | Compose instrumented tests |

---

## Layer Responsibilities

### Presentation
- **Composables** — stateless UI; receive state and emit events
- **ViewModel** — holds `MutableStateFlow<UiState>`, collects `MutableSharedFlow<Action>`, calls use cases
- **UiState** — immutable data class representing the full screen state
- **Action** — sealed interface representing all possible user intents

### Domain
- **Use Cases** — single-responsibility classes that delegate to a repository
- **Repository interfaces** — abstractions that decouple domain from data implementation
- **DataSource interfaces** — separate local and remote data contract

### Data
- **RepositoryImpl** — orchestrates remote and local data sources, wraps results in `Result<T>`
- **DataSourceImpl** — executes the actual network call or database query
- **Mapper** — converts `Response` → `Domain`, `Domain` → `Entity` (pure functions)

---

## Data Flow

```
User interaction
      │
      ▼
  Composable  ──── Action ────►  ViewModel
      ▲                              │
      │                        Use Case(s)
   UiState  ◄──── StateFlow ──       │
                               Repository
                              /          \
                      Remote DS        Local DS
                    (Retrofit API)   (Room DAO)
```

---

## Configuration

The base URL is stored in `network.properties` at the project root and injected at build time via `BuildConfig`:

```properties
BASE_URL=https://itunes.apple.com/
```

---

## Running Tests

```bash
# Unit tests
./gradlew :app:testDebugUnitTest

# Instrumented tests
./gradlew :app:connectedDebugAndroidTest
```

