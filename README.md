# 📦 Godown Inventory Management App

[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)
[![Kotlin](https://img.shields.io/badge/Language-Kotlin-orange.svg)](https://kotlinlang.org/)
[![Firebase](https://img.shields.io/badge/Database-Firebase_Realtime_DB%2FFirestore-ff69b4)](https://firebase.google.com/)

## 🌟 About the Project

This is a robust mobile application designed to manage inventory for a warehouse or 'godown'. Built natively for Android using **Kotlin and XML**, the application provides a centralized and real-time solution for tracking stock, managing costs, and optimizing warehouse operations.

The application leverages **Google Firebase** for secure, scalable, and real-time data storage, ensuring that inventory information is always up-to-date across all connected devices.

## ✨ Key Features

* **Comprehensive Item Tracking:** Stores all vital item details:
    * Item Name
    * **Quantity**
    * **Unit Price** (Cost to acquire)
    * **Selling Price**
    * **Location** within the godown (e.g., Aisle 5, Shelf C)
    * **Category** (e.g., Electronics, Raw Materials, Finished Goods)
* **Real-time Database:** Data synchronization powered by Firebase Firestore or Realtime Database.
* **Intuitive UI:** Traditional XML layouts provide a familiar and robust Android user interface.
* **Search and Filter:** Easy search functionality to locate items quickly.

## ⚙️ Technology Stack

* **Language:** Kotlin
* **UI/Frontend:** Android XML Layouts
* **Backend/Database:** Google Firebase (Firestore or Realtime Database)
* **Build System:** Gradle

## 🚀 Getting Started

To set up and run this project locally, you must first configure your own Firebase project.

### Prerequisites

* Android Studio (Latest Stable Version)
* A Google Account for Firebase setup.

### Installation & Firebase Setup

1.  **Clone the repository:**
    ```bash
    git clone [YOUR REPO URL HERE]
    cd [YOUR REPO NAME]
    ```

2.  **Create a Firebase Project:**
    * Go to the [Firebase Console](https://console.firebase.google.com/) and create a new project.
    * Click the **Android icon** to add a new Android app.
    * Enter your package name (found in your `AndroidManifest.xml` or `build.gradle` file, usually `com.example.inventoryapp`).
    * Follow the instructions to download the **`google-services.json`** file.

3.  **Add `google-services.json`:**
    * Place the downloaded `google-services.json` file directly into your application module directory (e.g., `app/google-services.json`).

4.  **Enable Database:**
    * In the Firebase Console, navigate to **Build** -> **[Firestore Database / Realtime Database]** and ensure it is enabled.

5.  **Run the application:**
    * Open the project in Android Studio.
    * Select an emulator or a physical device and click **Run** (the green triangle).

## 💡 Usage

The application flows through the following main screens:

1.  **Dashboard/Home:** Displays quick summaries and category lists.
2.  **Add Item Screen:** Form for inputting new item details (Name, Quantity, Prices, Location, Category).
3.  **Inventory List:** Shows all stored items, often with filtering and search capabilities.
4.  **Item Details:** Displays the specific details of a single item, allowing for editing or deletion.

## 🤝 Contributing

Contributions are welcome! If you have suggestions for improving inventory logic, UI, or bug fixes, please follow these steps:

1.  Fork the Project.
2.  Create your Feature Branch (`git checkout -b feature/new-search-filter`).
3.  Commit your Changes (`git commit -m 'Feat: Added new search filter capability'`).
4.  Push to the Branch (`git push origin feature/new-search-filter`).
5.  Open a Pull Request.

## 📄 License

Distributed under the **MIT License**. See the `LICENSE` file for more details.

---
*Project Link: https://github.com/Gaurav-banwal/inventory_system.git*
