# 💰 Finad - Your Smart Personal Finance Companion

> **Transform your financial life with the most intuitive expense tracking app built for modern Android**

[![Android](https://img.shields.io/badge/Android-API%2031+-green.svg)](https://developer.android.com/about/versions/android-13)
[![Compose](https://img.shields.io/badge/Jetpack%20Compose-1.5.14-blue.svg)](https://developer.android.com/jetpack/compose)
[![Material 3](https://img.shields.io/badge/Material%203-Latest-orange.svg)](https://m3.material.io/)

## 🚀 Why Choose Finad?

**Finad** is not just another expense tracker – it's your personal financial intelligence platform. Built with cutting-edge Android technologies and designed with your financial success in mind.

### ✨ What Makes Finad Special

- **🎯 Smart Categorization** - Automatically categorize expenses with intelligent AI-powered suggestions
- **📊 Visual Analytics** - Beautiful stacked bar charts and category breakdowns to understand your spending patterns
- **🔐 Secure Google Sign-In** - One-tap authentication with enterprise-grade security
- **☁️ Cloud Sync** - Your data is always safe and accessible across devices
- **🎨 Modern Material 3 Design** - Stunning, intuitive interface that adapts to your device
- **⚡ Lightning Fast** - Built with Jetpack Compose for buttery-smooth performance

## 📱 Features That Matter

### 🎯 **Intelligent Expense Tracking**

- Add expenses with just a few taps
- Automatic category detection
- Smart date and amount recognition
- Photo receipt capture (coming soon)

### 📈 **Powerful Analytics**

- Real-time spending insights
- Category-based expense breakdowns
- Monthly and yearly trend analysis
- Custom date range filtering

### 🔒 **Bank-Level Security**

- Google OAuth 2.0 authentication
- Encrypted local storage with Room database
- Secure API communication
- Privacy-first design

### 🎨 **Beautiful User Experience**

- Material 3 design language
- Dark/light theme support
- Smooth animations and transitions
- Intuitive navigation

## 🛠 Built with Modern Android Technologies

- **Jetpack Compose** - Next-generation UI toolkit
- **Material 3** - Latest design system
- **Room Database** - Robust local data storage
- **Retrofit** - Type-safe HTTP client
- **ViewModel** - Reactive state management
- **Navigation Compose** - Seamless navigation

## 📸 Screenshots

_[Screenshots would be added here showing the app's beautiful interface]_

## 🚀 Getting Started

### Prerequisites

- Android Studio Arctic Fox or later
- Android SDK API level 31+
- Google Cloud Console project

### Quick Setup

1. **Clone the repository**

   ```bash
   git clone https://github.com/lfelipessilva/finad.git
   cd finad
   ```

2. **Configure your environment**

   ```bash
   cp local.properties.template local.properties
   ```

3. **Add your credentials to `local.properties`**

   ```properties
   GOOGLE_CLIENT_ID=your_google_client_id_here
   BACKEND_URL=your_backend_url_here
   ```

4. **Build and run**
   ```bash
   ./gradlew build
   ```

### Google Cloud Setup

1. Go to [Google Cloud Console](https://console.cloud.google.com/)
2. Create a new project or select existing one
3. Enable Google Sign-In API
4. Create OAuth 2.0 credentials for Android
5. Add your client ID to `local.properties`

## 🏗 Architecture

Finad follows clean architecture principles with a modern Android stack:

```
📱 UI Layer (Compose)
├── 🎨 Material 3 Components
├── 📊 Custom Charts & Visualizations
└── 🧭 Navigation & State Management

📊 Business Logic Layer
├── 🏪 ViewModels
├── 🔄 Use Cases
└── 🎯 State Management

💾 Data Layer
├── 🗄️ Room Database (Local)
├── 🌐 Retrofit (Remote)
└── 🔐 Secure Storage
```

## 🔧 Development

### Building for Development

```bash
# Debug build
./gradlew assembleDebug

# Release build
./gradlew assembleRelease

# Run tests
./gradlew test
```

### Key Dependencies

- **UI**: Jetpack Compose, Material 3
- **Data**: Room, Retrofit, OkHttp
- **Auth**: Google Sign-In, Credentials API
- **Images**: Coil for image loading
- **JSON**: Moshi for serialization

## 🤝 Contributing

We welcome contributions! Please see our [Contributing Guidelines](CONTRIBUTING.md) for details.

### Development Setup

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests if applicable
5. Submit a pull request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- Google for the amazing Android platform
- JetBrains for the excellent Kotlin language
- The Android developer community for inspiration

## 📞 Support

- 📧 Email: support@finad.app
- 🐛 Issues: [GitHub Issues](https://github.com/lfelipessilva/finad/issues)
- 📖 Documentation: [Wiki](https://github.com/lfelipessilva/finad/wiki)

---

**Made with ❤️ for Android developers and financial enthusiasts**

_Transform your financial future with Finad - where smart technology meets personal finance._
