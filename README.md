# Finance App

A personal finance management Android application built with Jetpack Compose.

## Setup

### Prerequisites

- Android Studio
- Android SDK (API level 31+)
- Google Cloud Console project with OAuth 2.0 credentials

### Configuration

This project uses sensitive configuration values that should be stored securely. Follow these steps to set up your environment:

1. **Copy the template file:**

   ```bash
   cp local.properties.template local.properties
   ```

2. **Edit `local.properties` and add your actual values:**

   ```properties
   # Google OAuth Client ID for Google Sign-In
   GOOGLE_CLIENT_ID=your_actual_google_client_id_here

   # Backend API URL
   BACKEND_URL=your_actual_backend_url_here
   ```

3. **Get your Google Client ID:**

   - Go to [Google Cloud Console](https://console.cloud.google.com/)
   - Create or select your project
   - Enable the Google Sign-In API
   - Go to Credentials → Create Credentials → OAuth 2.0 Client IDs
   - Create a new OAuth 2.0 client ID for Android
   - Use the generated client ID in your `local.properties`

4. **Set your Backend URL:**
   - Replace `your_actual_backend_url_here` with your actual backend API URL
   - The URL should point to your backend API endpoint

### Security Notes

- The `local.properties` file is automatically excluded from version control via `.gitignore`
- Never commit your actual `local.properties` file to version control
- The `local.properties.template` file shows the required structure without sensitive data
- Configuration values are automatically injected into the app via BuildConfig

### Building the App

After setting up your `local.properties`:

```bash
./gradlew build
```

## Features

- Google Sign-In authentication
- Expense tracking and management
- Category-based expense organization
- Data synchronization with backend
- Modern Material 3 UI with Jetpack Compose

## Architecture

- **UI Layer**: Jetpack Compose with Material 3
- **Data Layer**: Room database for local storage, Retrofit for API calls
- **Authentication**: Google Sign-In with custom backend integration
- **State Management**: ViewModel with Compose state
