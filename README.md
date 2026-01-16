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
- Multiple difficulty levels (1 to 30 pairs)
- Dark mode support
- Configurable grid layouts
- Local high scores
- Clean, responsive interface
- No registration required

## Videos

### Gameplay Demo
[![Gameplay Demo](https://img.youtube.com/vi/k8vIy1fPf0U/0.jpg)](https://www.youtube.com/shorts/k8vIy1fPf0U)

### Full App Walkthrough
[![Full App Walkthrough](https://img.youtube.com/vi/u7r2X-dAKH8/0.jpg)](https://www.youtube.com/shorts/u7r2X-dAKH8)

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
- **Background Music:** AI-composed tracks
- **Astor Creatures:** Assets from:
- [Mythic Monsters I](https://willibab.itch.io/mythic-monsters-i)
- [Mythic Monsters II](https://willibab.itch.io/mythic-monsters-ii)
- [Lucky Bestiary](https://luckycassette.itch.io/lucky-bestiary)
- [Farm Animals](https://finalbossblues.itch.io/farm-animals-sprite-pack)
- [Font Pixellari](https://www.dafont.com/pt/pixellari.font)
- [Retro 8-Bit Monster Pack](https://willibab.itch.io/retro-8-bit-monster-pack)
---

*A cross-platform memory game showcasing modern Kotlin Multiplatform development practices.*

## Privacy Policy

https://phenomenal-biscuit-d94f8a.netlify.app