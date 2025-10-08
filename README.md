# SharedPortfolioLite

A modern Android application for managing shared investment portfolios among multiple users. Built with Jetpack Compose and Material Design 3, this app allows teams to track their collective stock investments with real-time price updates from Yahoo Finance API.

![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![Kotlin](https://img.shields.io/badge/Kotlin-0095D5?style=for-the-badge&logo=kotlin&logoColor=white)
![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white)

## 📱 Overview

SharedPortfolioLite enables friends, family, or investment groups to collectively manage a shared stock portfolio. Each member can contribute different amounts, and the app automatically calculates proportional ownership and individual profit/loss in real-time.

## ✨ Features

### Portfolio Management
- **Multi-User Support**: Add team members with their individual USD contributions
- **Stock Tracking**: Track multiple stock positions with ticker symbols (e.g., AAPL, GOOGL, TSLA)
- **Real-Time Price Updates**: Live stock price updates from Yahoo Finance API (every 10 seconds)
- **Proportional Ownership**: Automatic calculation of each user's ownership percentage
- **Performance Tracking**: View initial investment vs. current value with profit/loss calculations
- **Individual & Total Views**: See both individual member performance and overall portfolio health

### User Interface
- **Material Design 3**: Modern, clean interface with dynamic theming
- **Intuitive Cards**: Dedicated cards for users and assets with detailed breakdowns
- **Live Price Indicators**: Visual indicators showing which assets have live price updates
- **Color-Coded P&L**: Green for profits, red for losses with percentage changes
- **Quick Actions**: Edit and delete functionality with confirmation dialogs
- **Floating Action Buttons**: Easy access to add new users or stocks
- **Share Distribution**: View how each stock is distributed among team members

### Data Persistence
- **Room Database**: Local SQLite database for offline data storage
- **Persistent State**: All portfolio data persists across app restarts
- **Offline Support**: View cached prices when internet is unavailable

## 🏗️ Architecture

The app follows **Clean Architecture** principles with **MVVM (Model-View-ViewModel)** pattern:

```
app/
├── api/                                    # Network Layer
│   ├── YahooFinanceApiService.kt          # Retrofit API interface
│   └── YahooFinanceModels.kt              # API response data models
│
├── data/                                   # Data Layer
│   ├── dao/                               # Data Access Objects
│   │   ├── AssetDao.kt                   # Asset database operations
│   │   └── UserDao.kt                    # User database operations
│   ├── database/
│   │   └── AppDatabase.kt                # Room database configuration
│   ├── entities/                         # Data Models
│   │   ├── Asset.kt                      # Stock asset entity
│   │   └── User.kt                       # Team member entity
│   └── repository/                       # Repository Pattern
│       ├── AssetRepository.kt            # Asset data operations
│       └── PortfolioRepository.kt        # Portfolio operations
│
├── ui/                                    # Presentation Layer
│   ├── PortfolioScreen.kt               # Main portfolio screen
│   ├── AddUserDialog.kt                 # Dialog to add new user
│   ├── EditUserDialog.kt                # Dialog to edit user
│   ├── AddAssetDialog.kt                # Dialog to add new stock
│   ├── EditAssetDialog.kt               # Dialog to edit stock
│   ├── components/                      # Reusable UI Components
│   │   ├── AssetCard.kt                # Stock card with live prices
│   │   ├── UserCard.kt                 # User card with P&L
│   │   ├── TotalPortfolioCard.kt       # Portfolio summary card
│   │   ├── SectionHeader.kt            # Section titles
│   │   └── EmptyState.kt               # Empty state UI
│   └── theme/                          # Material Design Theme
│       ├── Color.kt                    # Color palette
│       ├── Theme.kt                    # Theme configuration
│       └── Type.kt                     # Typography
│
├── viewmodels/                          # Business Logic Layer
│   └── PortfolioViewModel.kt          # State management & logic
│
└── MainActivity.kt                      # Application entry point
```

## 🛠️ Tech Stack

### Core Technologies
- **Kotlin** - 100% Kotlin codebase
- **Jetpack Compose** - Modern declarative UI framework
- **Material Design 3** - Latest Material Design components

### Jetpack Libraries
- **Room Database** - Local data persistence with type-safe SQL
- **ViewModel** - Lifecycle-aware state management
- **StateFlow** - Reactive data streams for UI updates
- **Lifecycle** - Android lifecycle-aware components
- **KSP (Kotlin Symbol Processing)** - Annotation processing for Room

### Networking
- **Retrofit 2.9.0** - Type-safe HTTP client
- **OkHttp 4.12.0** - HTTP client with interceptors
- **Gson Converter** - JSON serialization/deserialization
- **Logging Interceptor** - Network debugging

### Build Configuration
- **Gradle Kotlin DSL** - Type-safe build configuration
- **Min SDK**: 24 (Android 7.0)
- **Target SDK**: 36 (Android 14+)
- **Compile SDK**: 36
- **Java Version**: 11

## 📋 Prerequisites

- Android Studio Hedgehog (2023.1.1) or later
- Android SDK 24 or higher
- Internet connection for real-time stock prices
- Java 11 JDK

## 🚀 Getting Started

### Installation

1. **Clone the repository**:
   ```bash
   git clone https://github.com/Emil2423/Shared-portfolio-lite.git
   cd Sharedportfoliolite
   ```

2. **Open in Android Studio**:
   - Launch Android Studio
   - Select `File` → `Open`
   - Navigate to the cloned directory and select it

3. **Sync Gradle**:
   - Android Studio will automatically sync Gradle dependencies
   - If not, click `File` → `Sync Project with Gradle Files`
   - Wait for the sync to complete

4. **Run the app**:
   - Connect an Android device via USB (with USB debugging enabled) OR
   - Start an Android Emulator (API 24+)
   - Click the `Run` button (▶️) or press `Shift + F10`

### Configuration

The app uses Yahoo Finance's public API for real-time stock prices. No API key is required.

**Customize Update Interval** (Optional):

Edit `PortfolioViewModel.kt`:
```kotlin
// Change from 10000ms (10 seconds) to your preferred interval
viewModel.startRealTimePriceUpdates(asset.ticker, intervalMs = 10000)
```

## 📖 How to Use

### Adding Team Members

1. Tap the **"User"** floating action button (bottom-right, blue)
2. Enter the member's name
3. Enter their contribution amount in USD
4. Tap **"Add"** to save

Example: Alice contributes $5,000, Bob contributes $3,000 (total pool: $8,000)

### Adding Stock Assets

1. Tap the **"Stock"** floating action button (bottom-right, purple)
2. Fill in the details:
   - **Ticker Symbol**: Stock symbol (e.g., `AAPL`, `GOOGL`, `TSLA`, `MSFT`)
   - **Purchase Price**: The price per share you bought it at (USD)
   - **Number of Shares**: Total shares owned by the group
3. Tap **"Add"** to start tracking

The app will automatically start fetching live prices from Yahoo Finance.

### Understanding the Portfolio View

#### Total Portfolio Card
- Shows overall initial investment
- Displays current total portfolio value
- Calculates total profit/loss with percentage

#### Team Members Section
- Lists all members with their contributions
- Shows each member's proportional ownership percentage
- Displays individual initial investment and current value
- Color-coded profit/loss indicators (green = gain, red = loss)

#### Portfolio Assets Section
- Lists all tracked stocks
- Shows purchase price vs. current live price
- Displays total shares and price change percentage
- **Share Distribution**: Shows how each stock is split among members
- **Live indicator**: Shows when prices are being updated in real-time

### Editing & Deleting

- **Edit**: Tap the pencil icon (✏️) on any user or asset card
- **Delete**: Tap the trash icon (🗑️) to remove (with confirmation)

