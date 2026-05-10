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

---

## ✨ Features
* 💾 **Local Database:** Room-powered single source of truth for instant offline access.
* ❤️ **Interactive Favorites:** Save books and see them as custom heart markers on the map.
* 🖼️ **Rich Summaries:** Tap markers to see book covers, authors, and location connections.
* ✅ **Journey Tracker:** Mark locations as visited to log your literary adventures.
* 🚶 **Live Navigation:** Direct integration with Google Maps for walking directions.
* 🎛️ **Dynamic Filters:** Toggle the map between all books, favorites, or visited spots.

---

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

---

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

---

## 🚀 Getting Started

### Prerequisites
* Android Studio (Koala Feature Drop or newer recommended)
* Minimum SDK: 24 (Android 7.0)
* Target SDK: 36 (Android 16)
  
To build and run this project locally, you will need a valid Google Maps API key. 

### 1. Clone the repository
Open your terminal and run the following command:
```
git clone https://github.com/matildaerenius/bookmap.git
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

---

## 📖 Documentation
For a deeper dive into the development process, architectural decisions, and technical details of BookMap, please refer to the resources below:

* 📄 **[Thesis Report (PDF)](docs/report.pdf)**: The formal final report covering the project's background, methodology, and implementation results
  
* 🌐 **[Project Wiki](https://github.com/matildaerenius/bookmap/wiki)**: A supplementary knowledge base featuring architectural diagrams, technical specifications, and a roadmap for future features.

---

## 👩‍💻 Author
**Matilda Erenius**

[![LinkedIn](https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white)](https://www.linkedin.com/in/matildaerenius)
[![GitHub](https://img.shields.io/badge/GitHub-100000?style=for-the-badge&logo=github&logoColor=white)](https://github.com/matildaerenius)

---

## 📄 License
This project is licensed under the Apache-2.0 License.
