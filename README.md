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

## 🛠 Tech Stack
* **Language:** Kotlin 
* **UI Toolkit:** Jetpack Compose
* **Architecture:** MVVM, Clean Architecture
* **Dependency Injection:** Dagger Hilt
* **Networking:** Retrofit, Kotlinx Serialization
* **Local Storage:** Room Database
* **Mapping:** Google Maps SDK for Android 
* **Image Loading:** Coil
* **CI/CD:** GitHub Actions

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
* Click the **Run** button (Shift + F10) to deploy the app to your emulator or physical device.

## 📖 Documentation
Curious about the process? Check out the thesis report detailing the architecture, design choices, and implementation of BookMap, available in the [Project Wiki](https://github.com/matildaerenius/bookmap/wiki)
