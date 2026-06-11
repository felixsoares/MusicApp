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
- **Paging 3** — `PagingSource` backed by Room, consumed via `LazyPagingItems` in Compose

---

## Project Structure

```
app/src/main/java/com/mobile/felix/musicapp/
│
├── core/
│   ├── data/
│   │   ├── local/
│   │   │   ├── SongDatabase.kt       # Room database
│   │   │   ├── dao/
│   │   │   │   └── SongDao.kt        # Abstract DAO with @Transaction helpers
│   │   │   └── entity/
│   │   │       └── SongEntity.kt     # Room entity
│   │   └── remote/
│   │       ├── ApiService.kt         # Retrofit interface
│   │       └── response/
│   │           ├── DetailResponse.kt
│   │           └── SearchResponse.kt
│   ├── di/
│   │   ├── NetworkModule.kt          # Retrofit, OkHttp
│   │   └── RoomModule.kt             # Room database, DAO bindings
│   ├── domain/
│   │   ├── Failure.kt                # Sealed error types
│   │   ├── Result.kt                 # Sealed result wrapper
│   │   └── Song.kt                   # Core domain model
│   ├── mapper/
│   │   └── MusicMapper.kt            # Response → Domain, Domain → Entity
│   └── presentation/
│       ├── Navigation.kt             # NavHost + routes
│       ├── Router.kt                 # Typed route definitions
│       ├── AlbumActionSheet.kt       # Bottom sheet for album navigation
│       ├── ErrorContentView.kt       # Reusable error state component
│       ├── LoadingView.kt            # Reusable loading indicator
│       └── SongItem.kt               # Reusable song list item composable
│
├── feature/
│   ├── home/
│   │   ├── data/
│   │   │   ├── repository/
│   │   │   │   └── HomeRepositoryImpl.kt
│   │   │   ├── source/
│   │   │   │   ├── HomeLocalDataSourceImpl.kt
│   │   │   │   └── HomeRemoteDataSourceImpl.kt
│   │   │   └── useCase/
│   │   │       ├── ClearLocalSongsUseCase.kt
│   │   │       ├── GetHomeSongsUseCase.kt      # Returns paged Flow from Room
│   │   │       ├── GetLocalSongsUseCase.kt
│   │   │       ├── GetSongsByTermUseCase.kt
│   │   │       └── SaveSongUseCase.kt
│   │   ├── di/
│   │   │   └── HomeModule.kt
│   │   ├── domain/
│   │   │   ├── repository/
│   │   │   │   └── HomeRepository.kt
│   │   │   └── source/
│   │   │       ├── HomeLocalDataSource.kt
│   │   │       └── HomeRemoteDataSource.kt
│   │   └── presentation/
│   │       ├── HomeScreen.kt
│   │       ├── HomeViewModel.kt
│   │       ├── HomeUiState.kt
│   │       └── action/HomeAction.kt
│   │
│   ├── song/
│   │   ├── data/
│   │   │   ├── player/
│   │   │   │   └── AudioPlayerImpl.kt          # ExoPlayer wrapper
│   │   │   ├── repository/
│   │   │   │   └── SongRepositoryImpl.kt
│   │   │   ├── source/
│   │   │   │   └── SongLocalDataSourceImpl.kt
│   │   │   └── useCase/
│   │   │       └── GetSongUseCase.kt
│   │   ├── di/
│   │   │   ├── AudioPlayerModule.kt             # ViewModelScoped ExoPlayer
│   │   │   └── SongModule.kt
│   │   ├── domain/
│   │   │   ├── player/
│   │   │   │   └── AudioPlayer.kt              # Playback interface
│   │   │   ├── repository/
│   │   │   │   └── SongRepository.kt
│   │   │   └── source/
│   │   │       └── SongLocalDataSource.kt
│   │   └── presentation/
│   │       ├── SongScreen.kt
│   │       ├── SongViewModel.kt
│   │       ├── SongUiState.kt
│   │       └── action/SongAction.kt
│   │
│   ├── album/
│   │   ├── data/
│   │   │   ├── repository/
│   │   │   │   └── AlbumRepositoryImpl.kt
│   │   │   ├── source/
│   │   │   │   └── AlbumDataSourceImpl.kt
│   │   │   └── useCase/
│   │   │       └── GetAlbumUseCase.kt
│   │   ├── di/
│   │   │   └── AlbumModule.kt
│   │   ├── domain/
│   │   │   ├── repository/
│   │   │   │   └── AlbumRepository.kt
│   │   │   └── source/
│   │   │       └── AlbumDataSource.kt
│   │   └── presentation/
│   │       ├── AlbumScreen.kt
│   │       ├── AlbumViewModel.kt
│   │       ├── AlbumState.kt
│   │       └── action/AlbumAction.kt
│   │
│   └── splash/
│       └── SplashScreen.kt
│
└── ui/theme/
    ├── Color.kt
    ├── Theme.kt
    └── Type.kt

app/src/test/
├── TestFixtures.kt                                # Shared fake data for all tests
├── feature/
│   ├── album/
│   │   ├── data/
│   │   │   ├── repository/  AlbumRepositoryImplTest.kt
│   │   │   ├── source/      AlbumDataSourceImplTest.kt
│   │   │   └── useCase/     GetAlbumUseCaseTest.kt
│   │   └── presentation/    AlbumViewModelTest.kt
│   ├── home/
│   │   ├── data/
│   │   │   ├── repository/  HomeRepositoryImplTest.kt
│   │   │   ├── source/      HomeLocalDataSourceImplTest.kt
│   │   │   │                HomeRemoteDataSourceImplTest.kt
│   │   │   └── useCase/     GetLocalSongsUseCaseTest.kt
│   │   │                    GetSongsByTermUseCaseTest.kt
│   │   │                    SaveSongUseCaseTest.kt
│   │   └── presentation/    HomeViewModelTest.kt
│   └── song/
│       ├── data/
│       │   ├── repository/  SongRepositoryImplTest.kt
│       │   ├── source/      SongLocalDataSourceImplTest.kt
│       │   └── useCase/     GetSongUseCaseTest.kt
│       └── presentation/    SongViewModelTest.kt
```

---

## Features

| Screen           | Description                                                                                          |
|------------------|------------------------------------------------------------------------------------------------------|
| **Splash**       | Entry screen shown on app launch before navigating to Home                                           |
| **Home**         | Search songs via iTunes API with debounced query, paginated results, and saved songs list            |
| **Song**         | Play 30-second audio preview with playback controls (play/pause, fast-forward, rewind, repeat, seek) |
| **Album**        | View all tracks of an album fetched from the iTunes lookup API                                       |

---

## Libraries

### Core Android
| Library | Version | Purpose |
|---|---|---|
| Core KTX | `1.19.0` | Kotlin extensions for Android framework |
| Activity Compose | `1.13.0` | `ComponentActivity` with Compose support |

### UI
| Library | Version | Purpose |
|---|---|---|
| Jetpack Compose BOM | `2026.05.01` | Compose dependency management |
| Material 3 | via BOM | Design system components |
| Material Icons Extended | via BOM | Extended Material icon set |
| Compose Runtime | `1.11.2` | Core Compose runtime |
| Compose Animation | `1.11.2` | Animated visibility and transitions |
| Compose Navigation | `2.9.0` | Type-safe navigation with `NavHost` |
| Coil Compose | `2.7.0` | Async image loading in Compose |

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
| Kotlinx Serialization JSON | `1.8.1` | Kotlin-native serialization (type-safe routes) |

### Local Storage
| Library | Version | Purpose |
|---|---|---|
| Room Runtime | `2.7.1` | SQLite ORM |
| Room KTX | `2.7.1` | Coroutine and Flow extensions |
| Room Paging | `2.7.1` | `PagingSource` integration with Room |
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
| Paging Runtime | `3.3.6` | Pagination core |
| Paging Compose | `3.3.6` | `LazyPagingItems` for Compose |

### Testing
| Library | Version | Purpose |
|---|---|---|
| JUnit 4 | `4.13.2` | Unit test runner |
| MockK | `1.13.17` | Kotlin-first mocking library |
| MockK Android | `1.13.17` | Instrumented mocking support |
| kotlinx-coroutines-test | `1.10.2` | `runTest`, `TestDispatcher`, `advanceUntilIdle` |
| Espresso Core | `3.7.0` | UI instrumented tests |
| Compose UI Test JUnit4 | via BOM | Compose instrumented tests |
| AndroidX JUnit | `1.3.0` | AndroidX JUnit runner |

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
- **DataSource interfaces** — separate local and remote data contracts

### Data
- **RepositoryImpl** — orchestrates remote and local data sources, wraps results in `Result<T>`
- **DataSourceImpl** — executes the actual network call or database query
- **SongDao** — abstract Room DAO with public `@Transaction` methods (`replaceSearchResults`, `clearSearch`) and protected internal helpers; ensures atomic multi-step DB operations
- **Mapper** — converts `Response` → `Domain`, `Domain` → `Entity` (pure functions, no side effects)

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
                                           │
                                     PagingSource
                                    (Paging 3 + Room)
```

---

## Search Session Management

The home screen uses two display modes driven by the search query:

| Query state | Data source | Room filter |
|---|---|---|
| **Blank** | Static `List<Song>` from ViewModel state | `isCachedDetails = 1` |
| **Non-blank** | `LazyPagingItems<Song>` via Paging 3 | `isFromHomeSearch = 1` |

When a new search is submitted, `SongDao.replaceSearchResults()` runs atomically inside a `@Transaction`:
1. Deletes unsaved results (`isCachedDetails = 0`)
2. Resets `isFromHomeSearch` on all remaining songs
3. Inserts new results with `IGNORE` conflict strategy (preserves `isCachedDetails` for previously saved songs)
4. Tags all incoming results as the active search session

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
