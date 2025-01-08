# Telegram Filter App

[![CI](https://github.com/lucascalheiros/TelegramFilterApp/actions/workflows/android.yml/badge.svg)](https://github.com/lucascalheiros/TelegramFilterApp/actions/workflows/android.yml)
![GitHub License](https://img.shields.io/github/license/lucascalheiros/TelegramFilterApp)
![Kotlin](https://img.shields.io/badge/kotlin-2.1.0-blue.svg?logo=kotlin)
![Android](https://img.shields.io/badge/Android-yellow?logo=Android)

This is a utility app built with [TdLib](https://core.telegram.org/tdlib) to filter messages and emit notifications based on custom criteria defined by the user.

The idea for this app originated from a desire to filter Black Friday deals from Telegram deals channels. Initially, the plan was to create a Filter Bot; however, Telegram bots cannot subscribe to channels or access messages. This limitation led to the development of a filter companion app. While this is a relatively simple app, it is well-constructed and architected as part of my showcase projects. (Check out a different architecture and tech stack in this project: [Showcase-App-WaterReminder](https://github.com/lucascalheiros/Showcase-App-WaterReminder).)

## Build

To build this application properly, you will need the following:

1. **Valid `SetTdlibParameters`**  
   Follow the instructions at [Telegram API Documentation](https://core.telegram.org/api/obtaining_api_id) to obtain an `api_id` and configure the required parameters in the file:  
   `data/src/main/java/com/github/lucascalheiros/data/frameworks/telegram/SetTdlibParameters.kt`.

2. **Google Services JSON**  
   Set up a Firebase project by following [this guide](https://firebase.google.com/docs/android/setup#console), and replace the `app/google-services.json` file with the one from your Firebase project.

After completing these steps, provide the FCM credential for the app you created during the Telegram setup at [My Telegram Apps](https://my.telegram.org/apps). Once configured, press the **Run** button in Android Studio to launch the app.

## Technologies

* UI: Compose, Material, SplashScreen API, Jetpack Navigation 
* Async: Coroutines
* Reactive: Flow/Channels
* Storage: Room
* Dependency Injecntion: Hilt/Dagger
* Firebase: Crashlytics, Analytics, FCM
* Tests: Junit, Mockk, Roboeletric
* Relevant architectures: MVI+Reducer, Clean Architecture, Modular, Single Activity app
* TdLib
* Jni lib

## Architecture

### UI Layer Architecture: MVI with a Reducer for State Management

This app adopts the MVI (Model-View-Intent) architecture for the UI layer, with a Reducer to handle state management. Each screen includes the following core components and their responsibilities:

1. **UiState**  
   An immutable data class representing the complete state of the view at any given time.

2. **UiEvents**  
   An enum-like structure to emit low-priority, single-use events. High-priority or persistent events are better represented as part of the `UiState` and handled manually.

3. **Intents**  
   An enum-like structure representing all user actions while interacting with the UI. Intents are dispatched to the `ViewModel` to trigger state changes, produce events, or interact with the app's domain logic.

4. **Actions**  
   An enum-like structure representing the final actions that update the state.

5. **Reducer**  
   A pure function that receives an `Action` and the current `UiState` and produces a resultant state.

6. **ViewModel**  
   This is the familiar Android `ViewModel` component. In this architecture, it:  
   - Holds the `UiState` and `UiEvents` flows.  
   - Exposes the intent dispatcher function.  
   - Maps the `Intents` into `Actions`.  
   - Acts as middleware between the domain and the reducer, calling domain functions required by `Intents` and generates `Actions` to update the screen state.

---

#### Advantages of Using a Reducer in MVI

The Reducer enhances the separation of concerns, simplifying testing and maintenance:  
- **Improved Separation of Concerns**: State change logic is isolated in the Reducer, keeping the `ViewModel` smaller and easier to manage over time.  
- **Ease of Testing**: As a pure function, the Reducer makes testing straightforward, with `Action` → `State` transitions being explicit and predictable.

---

##### Considerations and Trade-offs

While the Reducer brings structural benefits, it also introduces some boilerplate:  
- **Boilerplate Code**: Implementing the `Reducer` and `Actions` involves additional setup compared to managing state changes directly in the `ViewModel`.  
- **Complexity for Simple Scenarios**: For straightforward cases, this approach might feel overly elaborate.

### App Architecture: Modular Clean Architecture

Since this is a very simple app I opted for a simple layered modularization with 3 major modules and other minors modules. For a more granular modularization please checkout: https://github.com/lucascalheiros/Showcase-App-WaterReminder which uses modularization based in features and separated domain/data modules.

* **app**: Application entrypoint and UI layer
* **domain**: Pure kotlin module responsible for the domain layer (business rules)
* **data**: Data layer, responsible for the interaction with external frameworks and storage.

This is a domain driven architecture, so both `app` an `data` modules will depend on `domain` module, and the `app` module will have no direct interaction with classes from the `data` module, with exception to enable the dependency injection.

As Additional minor modules:
* **analytics**: Following best practices to share analytics and crashlytics capabilities to other modules
* **common**: Some utilitary functions, logs and dispatcher qualifiers
* **fuzzysearch**: Implementation of [Levenshtein distance](https://www.geeksforgeeks.org/introduction-to-levenshtein-distance/) algorithm in c++



