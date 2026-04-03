<div align="center">

# 📚 BookMap

[![Android CI](https://github.com/matildaerenius/bookmap/actions/workflows/android.yml/badge.svg)](https://github.com/matildaerenius/bookmap/actions/workflows/android.yml)

> **A final thesis project for the Java Developer program at Nackademin, created in collaboration with BookBeat** 🎓

</div>

---

**Bookmap** is an Android application that connects literature with geography. The app fetches a curated list of book related locations from a remote JSON source (GitHub Gist) and displays them as interactive markers on a map using the Google Maps SDK 📍

## ✨ Features

## 🛠 Tech Stack
* **Language:** Kotlin 
* **UI Toolkit:** Jetpack Compose
* **Architecture:** MVVM, Clean Architecture
* **Dependency Injection:** Dagger Hilt
* **Networking:** Retrofit, Kotlinx Serialization
* **Local Storage:** Room Database
* **Mapping:** Google Maps SDK for Android (with Compose Maps)
* **Image Loading:** Coil
* **CI/CD:** GitHub Actions

## 🚀 Getting Started
To build and run this project locally, you will need a valid Google Maps API key. 

1. Clone the repository.
2. Create a file named `local.properties` in the root directory of the project.
3. Add your API key to the file like this:
   `MAPS_API_KEY=your_actual_api_key_here`
4. The project uses the **Secrets Gradle Plugin for Android** to securely inject this key during the build process.
5. Build and run the project via Android Studio.

## 📖 Documentation
