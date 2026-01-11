# AstorMemory

A classic memory matching game featuring AI-generated fantasy creatures called "Astors" with original background music. Built with Kotlin Multiplatform and Compose Multiplatform, targeting both Android and iOS.

## How to Play

Flip cards to reveal Astor creatures and find matching pairs. Clear the board by matching all cards!

**Scoring System:**
- **Starting Score:** Based on difficulty (e.g., 12 pairs = 1200 points)
- **Combo Bonus:** Points increase with consecutive matches
- **Miss Penalty:** Score decreases for mismatched cards

## Features

- Cross-platform game engine (Android & iOS)
- Unique AI-generated Astor creatures fetched dynamically
- Original background music and sound effects
- Multiple difficulty levels (3 to 36 pairs)
- Dark mode support
- Configurable grid layouts
- Local high scores
- Clean, responsive interface
- No registration required

## Videos

### Gameplay Demo
[![Gameplay Demo](https://img.youtube.com/vi/PrEUIlwui3g/0.jpg)](https://www.youtube.com/shorts/PrEUIlwui3g)

### Full App Walkthrough
[![Full App Walkthrough](https://img.youtube.com/vi/uG82Zkc3j08/0.jpg)](https://www.youtube.com/shorts/uG82Zkc3j08)

## Technical Overview

This project demonstrates modern **Kotlin Multiplatform (KMP)** development with shared business logic across platforms.

### Architecture & Tech Stack

**Multiplatform Core:**
- **Kotlin Multiplatform (KMP)** - Shared codebase for Android & iOS
- **Compose Multiplatform** - Declarative UI framework for both platforms
- **SQLDelight** - Type-safe database for local persistence
- **Koin** - Dependency injection
- **Kotlinx Coroutines** - Asynchronous programming

**Platform-Specific:**
- **Android:** SoundPool & MediaPlayer for audio
- **iOS:** AVAudioPlayer for audio playback
- **Shared Preferences (Android)** / **NSUserDefaults (iOS)** for settings persistence

### Key Implementations

- **Expect/Actual Pattern** for platform-specific code (audio, preferences)
- **Dynamic Layout Calculations** for responsive grid sizing
- **Custom Audio System** with sound effects and background music
- **Navigation** with type-safe Compose Navigation
- **State Management** with Kotlin StateFlow

## Download

[![Get it on Google Play](https://play.google.com/intl/en_us/badges/static/images/badges/en_badge_web_generic.png)](https://play.google.com/store/apps/details?id=io.lb.astormemory.app)

*iOS version coming soon to the App Store*

## Credits

- **Original Version:** [AstorMemory (Native Android)](https://github.com/LeonardoBai12/AstorMemory)
- **Astor Creatures:** AI-generated artwork
- **Background Music:** AI-composed tracks

---

*A cross-platform memory game showcasing modern Kotlin Multiplatform development practices.*
