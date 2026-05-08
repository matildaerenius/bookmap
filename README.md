<div align="center">

# 📚 BookMap

<p align="center">
  <img src="https://img.shields.io/badge/Kotlin-%237F52FF.svg?style=for-the-badge&logo=kotlin&logoColor=white" alt="Kotlin" />
  <img src="https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white" alt="Android" />
  <img src="https://img.shields.io/badge/Jetpack%20Compose-4285F4?style=for-the-badge&logo=android&logoColor=white" alt="Jetpack Compose" />
</p>

[![Android CI](https://github.com/matildaerenius/bookmap/actions/workflows/android.yml/badge.svg)](https://github.com/matildaerenius/bookmap/actions/workflows/android.yml)

> **A final thesis project for the Java Developer program at Nackademin, created in collaboration with BookBeat** 🎓

</div>

<br>

**Bookmap** is a native Android application that connects literature with geography. The app fetches a curated list of book-related locations from a remote JSON source (GitHub Gist), syncs them with detailed book data from the public BookBeat API, and displays them as interactive markers using the Google Maps SDK 📍

<br>

## ✨ Features
* 💾 Uses a local Room database as the single source of truth, enabling instant load times and offline accessibility
* ❤️ Save books to a dedicated list. Favorited books dynamically update on the map with custom heart markers
* 🖼️ Tap any marker to reveal a detailed bottom sheet containing the book cover, author information, and the unique connection between the real-world spot and the story
* ✅ Mark specific locations as "visited" once you have been there, allowing you to build a personal footprint of your literary adventures in Stockholm
* 🚶 Ready to visit a spot? Tap the navigation icon on any book to instantly open walking directions in your phone's native Google Maps app
* 🎛️ Easily toggle the map view to show all available books, exclusively your saved favorites, or locations you have already explored

## 🛠 Tech Stack
* **Language:** Kotlin 
* **UI:** Jetpack Compose
* **Architecture:** MVVM, Clean Architecture
* **Dependency Injection:** Dagger Hilt
* **Networking:** Retrofit, Kotlinx Serialization
* **Local Storage:** Room
* **Mapping:** Google Maps SDK for Android 
* **Image Loading:** Coil
* **CI/CD:** GitHub Actions

## 📱 Screenshots

<p align="center">
  <img src="docs/images/onboarding_screen.png" alt="BookMap Onboarding Screen" width="230"/>
  &nbsp;&nbsp;&nbsp;&nbsp;
  <img src="docs/images/map_screen.png" alt="BookMap Map Screen" width="230"/>
  <br><br>
  <img src="docs/images/detail_sheet.png" alt="BookMap Detail Sheet" width="230"/>
  &nbsp;&nbsp;&nbsp;&nbsp;
  <img src="docs/images/favorite_screen.png" alt="BookMap Favorites Screen" width="230"/>
</p>

## 🚀 Getting Started
To build and run this project locally, you will need a valid Google Maps API key. 

### 1. Clone the repository
Open your terminal and run the following command:
```bash
git clone [https://github.com/matildaerenius/bookmap.git](https://github.com/matildaerenius/bookmap.git)
```

### 2. Set up the API Key
* Create a file named `local.properties` in the root directory of the project.
* Add your Google Maps API key to the file like this:
  ```properties
  MAPS_API_KEY=your_actual_api_key_here
  ```
* The project uses the **Secrets Gradle Plugin for Android** to securely inject this key during the build process.

### 3. Build and Run
* Open the cloned directory in **Android Studio**.
* Let Gradle sync and download all dependencies.
* Click the **Run** button to deploy the app to your emulator or physical device.

## 📖 Documentation
Curious about the process? Check out the thesis report detailing the architecture, design choices, and implementation, available in the [Project Wiki](https://github.com/matildaerenius/bookmap/wiki)
