# Vehicle Companion

A simple Android app for managing your vehicles and finding interesting places to visit.

## Running

To run the app:

```bash
# Build debug APK
./gradlew assembleDebug

# Install on connected device/emulator
./gradlew installDebug
```

## Architecture & Design Choices

The app follows MVVM with a clean architecture approach to keep things maintainable and testable.

**Key decisions:**

- **Jetpack Compose** for UI
- **Hilt** for dependency injection
- **Room** for local storage
- **Repository pattern** for abstracting data sources
- **StateFlow** for reactive state management
- **Retrofit** for network calls

The structure is pretty straightforward:

- `ui/` - Compose screens and ViewModels
- `data/` - Repositories, database, and network layer
- `di/` - Hilt modules
- `navigation/` - Navigation graph

## How to Run Tests

```bash
# Run all unit tests
./gradlew test

# Run tests with coverage report
./gradlew testDebugUnitTest

# Run specific test class
./gradlew test --tests "*.VehicleRepositoryTest"
```

## Error/Empty State Handling

The app handles common scenarios gracefully:

**Empty states:**

- Garage shows "No vehicles yet" message with an Add button
- Places search shows message when no results found

**Error handling:**

- Network errors display retry-able error messages
- Form validation shows inline errors
- Loading states prevent multiple submissions

ViewModels expose UI state as sealed classes.

## What I'd Build Next

If I had more time, here's what would come next:

1. **Trip planning** - Connect places into routes
2. **Car files** - Track car documents, insurance, fuel logs, etc.
3. **Reminders** - Oil changes, inspections, etc. based on mileage or time
4. **Photo uploads** - Add vehicle photos
5. **Offline mode** - Cache places data for road trips without signal
