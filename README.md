# NYTimes News App

This is a simple Android application that fetches and displays the most popular news articles from the New York Times API. The app is built using modern Android development practices, including Jetpack Compose for the UI, Kotlin for the programming language, and other Jetpack libraries for enhanced functionality.

## Features

*   **Displays Recent News:** Fetches and displays the most recent news articles from the NYTimes API.
*   **Categorized News:** Allows users to view news from "Yesterday", "Last 7 days", and "Last 30 days".
*   **Detailed News View:** Provides a detailed view of each news article, including the title, abstract, published date, and an image (if available).
*   **Open in Browser:** Allows users to open the full article in their default web browser.
*   **Loading State:** Shows a loading indicator while fetching news.
*   **Error Handling:** Displays an error message if there's a problem fetching the news.
* **Modern UI:** The app uses Jetpack Compose to create a modern and responsive user interface.

-------

<img src="https://github.com/user-attachments/assets/677614b9-0b78-44c6-9fdd-3ab56ae9dcd6" height="400" alt="Screenshot"/>
<img src="https://github.com/user-attachments/assets/54f8d48e-b2e1-4c47-9763-4c7cea249642" height="400" alt="Screenshot"/>
<img src="https://github.com/user-attachments/assets/e1f8f03e-bfc5-438a-be53-e4a292c30167" height="400" alt="Screenshot"/>
<img src="https://github.com/user-attachments/assets/782d26af-01d7-4a37-8c49-0a24189f2fb3" height="400" alt="Screenshot"/>
<img src="https://github.com/user-attachments/assets/dcd38a4e-9371-4c72-8730-9005059cbecb" height="400" alt="Screenshot"/>

## Tech Stack

*   **Kotlin:** The primary programming language.
*   **Jetpack Compose:** For building the user interface.
*   **Jetpack Libraries:**
    * `ViewModel`: For managing UI-related data in a lifecycle-conscious way.
    * `LiveData`: For observing data changes.
    * `Compose Runtime`: For the core of the compose framework.
    * `Compose Material 3`: For the material design components.
    * `Compose foundation`: For the basic building blocks of the UI.
    * `Compose activity`: For the integration with the activity.
    * `Compose lifecycle`: For the integration with the lifecycle.
*   **Coil:** For image loading and caching.
*   **NYTimes API:** For fetching news data.

## Build Configuration and Local Properties

This project uses Gradle as its build system. Here's a summary of the key build configuration elements, focusing on Gradle versions and the use of local properties:

### Gradle Versions

*   **Gradle Plugin:** The project uses version `8.7.0` of the Android Gradle plugin.
*   **Kotlin Gradle Plugin:** The Kotlin Gradle plugin version used is `2.0.0`.
*   **Gradle Wrapper:** The project is configured to use Gradle version `8.9`.

### Local Properties (`local.properties`)

This project utilizes a `local.properties` file to manage sensitive information, such as API keys, that should not be committed to version control.

**How to Use:**

1.  **Create `local.properties`:** If it doesn't exist (Note: Android studio will always automatically create this), create a file named `local.properties` in the root directory of your project.
2.  **Add Properties:** Add your API keys or other sensitive information to this file in the format `key=value`. In this project's case, we added NYTimesAPIKey="{{Your news api key}}"
3.  **Clean and rebuild:** Clea and Rebuild the project again to access NYTimesAPIKey in the BuildConfig
