---
name: BomberVPN Android
description: Native Material 3 connection control in the BomberVPN Ink/Mist identity.
colors:
  mist-primary: "#9A5B00"
  mist-on-primary: "#FFFFFF"
  mist-primary-container: "#FFDDB1"
  mist-on-primary-container: "#301B00"
  mist-secondary: "#5E6780"
  mist-on-secondary: "#FFFFFF"
  mist-secondary-container: "#E3E7EF"
  mist-on-secondary-container: "#151C33"
  mist-tertiary: "#137A48"
  mist-on-tertiary: "#FFFFFF"
  mist-background: "#EEF1F6"
  mist-on-background: "#0B1122"
  mist-surface: "#FFFFFF"
  mist-on-surface: "#0B1122"
  mist-surface-variant: "#E3E7EF"
  mist-on-surface-variant: "#5E6780"
  mist-outline: "#747E94"
  mist-outline-variant: "#E3E7EF"
  mist-surface-container-lowest: "#FFFFFF"
  mist-surface-container-low: "#F5F7FA"
  mist-surface-container: "#EEF1F6"
  mist-surface-container-high: "#E3E7EF"
  mist-surface-container-highest: "#D9DFE9"
  mist-error: "#C0362C"
  mist-on-error: "#FFFFFF"
  ink-primary: "#FFB02E"
  ink-on-primary: "#1A1204"
  ink-primary-container: "#5A3700"
  ink-on-primary-container: "#FFDDB1"
  ink-secondary: "#BEC6DB"
  ink-on-secondary: "#232C4A"
  ink-secondary-container: "#232C4A"
  ink-on-secondary-container: "#EEF1F7"
  ink-tertiary: "#3CCB7F"
  ink-on-tertiary: "#002110"
  ink-background: "#0B1122"
  ink-on-background: "#EEF1F7"
  ink-surface: "#151C33"
  ink-on-surface: "#EEF1F7"
  ink-surface-variant: "#232C4A"
  ink-on-surface-variant: "#BEC6DB"
  ink-outline: "#8E97AE"
  ink-outline-variant: "#232C4A"
  ink-surface-container-lowest: "#080D1B"
  ink-surface-container-low: "#10172A"
  ink-surface-container: "#151C33"
  ink-surface-container-high: "#1C2440"
  ink-surface-container-highest: "#232C4A"
  ink-error: "#FF6B6B"
  ink-on-error: "#370001"
typography:
  headline-medium:
    fontFamily: "Android default sans-serif"
    fontSize: "28sp"
    fontWeight: 400
    lineHeight: "36sp"
    letterSpacing: "0sp"
  title-medium:
    fontFamily: "Android default sans-serif"
    fontSize: "16sp"
    fontWeight: 500
    lineHeight: "24sp"
    letterSpacing: "0.15sp"
  body-large:
    fontFamily: "Android default sans-serif"
    fontSize: "16sp"
    fontWeight: 400
    lineHeight: "24sp"
    letterSpacing: "0.5sp"
  body-medium:
    fontFamily: "Android default sans-serif"
    fontSize: "14sp"
    fontWeight: 400
    lineHeight: "20sp"
    letterSpacing: "0.25sp"
  body-small:
    fontFamily: "Android default sans-serif"
    fontSize: "12sp"
    fontWeight: 400
    lineHeight: "16sp"
    letterSpacing: "0.4sp"
  label-large:
    fontFamily: "Android default sans-serif"
    fontSize: "14sp"
    fontWeight: 500
    lineHeight: "20sp"
    letterSpacing: "0.1sp"
rounded:
  material-medium: "12dp"
  progress: "2dp"
spacing:
  step-4: "4dp"
  step-8: "8dp"
  step-12: "12dp"
  step-16: "16dp"
  step-20: "20dp"
  step-24: "24dp"
components:
  connection-button:
    typography: "{typography.label-large}"
    width: "100%"
  secondary-button:
    typography: "{typography.label-large}"
    width: "100%"
  signal-mark:
    size: "88dp"
  subscription-card:
    rounded: "{rounded.material-medium}"
    padding: "2dp 8dp 10dp 16dp"
---

# Design System: BomberVPN Android

## Overview

**Creative North Star: "Dependable connection control"**

A dependable connection control built from the established Ink/Mist identity and native Material 3. Quiet surfaces, amber actions, and the owned signal-bomb mark give the Android client its character; connection state and the next action carry the hierarchy.

This is an extension of the existing v2rayNG surface, with the service engine retaining ownership of connection truth. It uses platform typography, interaction states, and navigation. The pinned brand and native platform were treated as an extension workflow; no replacement-world concept tournament, seed roll, or retrospective comp approval is claimed.

**Key Characteristics:**
- Native Material 3 components and scalable Android typography.
- Ink/Mist by default; wallpaper-derived colors are optional.
- Service state in words and icon treatment; test results remain separate.
- Owned vector identity, with no new shipping raster artwork.

## Colors

Ink is deep navy with warm amber; Mist is pale neutral with a darker amber that remains readable on light surfaces. Frontmatter records the explicit role overrides from `Theme.kt`, paired by `ink-` and `mist-`. Unspecified Material roles retain library defaults. These are Android color roles, not CSS theme classes.

### Primary
Amber marks primary buttons, active connection tint, and Material selection states. Container and on-color pairs are part of the role, not interchangeable accents.

### Secondary
Cool slate roles support tonal buttons and secondary information. The top-bar loading indicator and search cursor use secondary.

### Tertiary
Green tertiary roles remain available in the theme. They are not the home connection indicator: the running signal uses primary. Error roles are semantic alerts, including subscription warnings.

### Neutral
Background frames the application; surface hosts the app bar and menus. On-surface is normal text; on-surface-variant is supporting text. In Ink, the old sketch's muted value is the outline role, not the shipped supporting-text role. Container levels provide tonal separation.

System light/dark is the default, with explicit user overrides. Dynamic colors are off by default and use Android wallpaper-derived Material schemes on Android 12+ when enabled; fixed Ink/Mist values must not be forced into those components.

**The Service Truth Rule.** The signal mark and status follow the service state; connection testing reports its own result.

## Typography

**Display Font:** No custom display face. Native Android default sans-serif through Material 3.
**Body Font:** The same platform family; no bundled brand font or custom `Typography` is installed by `AppTheme`.

### Hierarchy
The frontmatter records the default Material roles used by home/help: headline-medium for state and help title; title-medium for subscription grouping; body-large for help instructions; body-medium for status and setup hints; body-small for privacy text; label-large for buttons. The app bar and navigation inherit their Material component typography. The subscription title overrides body-medium weight to semibold. Search text explicitly uses 16sp. Native font scaling applies; sp values are not fixed pixels.

**The Native Roles Rule.** Use Material typography roles and Android font scaling instead of adding a separate display type system.

## Layout

The scaffold supplies system-safe insets. Home and help are vertically scrollable, centered, and capped at 560dp before internal padding. Home uses 24dp horizontal and 20dp vertical padding, with 12dp item gaps and an additional 8dp spacer before subscriptions. Help uses 24dp padding and 16dp gaps.

Below 600dp window width, three destinations use a bottom navigation bar. At 600dp and above, they use a navigation rail and an 88dp start allowance. Width is measured from the current window in density-independent units. System Back returns from Servers or Help to Home. Server content retains the upstream pager/list/grid and has 80dp bottom content padding; it does not inherit the home width cap.

Connection action minimum height is 56dp. Home/help secondary and support actions have 48dp minimum height, fill available width, and can grow with text. These are minimums, not fixed token heights.

## Elevation & Depth

Home/help have no added card backdrop, gradient, glow, or bespoke shadow. Tonal surfaces provide separation while Material components retain native elevation and interaction treatments. The subscription information surface uses surface-container-high at 97% opacity and an outline-variant border at 50% opacity with a 0.5dp stroke; its source does not add an elevation. Menus and drawer retain their native Material behavior.

## Shapes

Buttons inherit Material 3 capsule shapes and state treatment; there is no custom radius override. Subscription information uses the default Material medium shape (the frontmatter's material-medium). Its progress bar uses the progress radius. The signal-bomb silhouette is an owned XML vector adapted from the existing BrandMark.vue, not a Unicode glyph or raster illustration.

## Components

### Buttons
The connection button is filled primary with on-primary content. It is disabled without a selection unless the service is already running. The selected-server and scan actions use OutlinedButton; clipboard import uses FilledTonalButton and disables during loading; account/log actions use TextButton. Support uses Button. Native ripple, focus, disabled treatment, and button padding are inherited, with no bespoke hover animation. Do not approximate these states in Android with web CSS.

### Inputs / Fields
The top-bar search is a single-line OutlinedTextField with transparent container and border in both focused and unfocused states. Text uses on-surface, placeholder uses on-surface-variant, cursor and selection handles use secondary, and selection background uses secondary at 40% alpha. It requests focus on entry, has the Search IME action, and shows a clear action when nonempty.

### Cards / Containers
Subscription information is a compact inherited data surface, using actual parsed subscription values. Internal padding is in frontmatter; external padding is 12dp horizontally and 4dp vertically. The title truncates to one line. Usage and expiry use body-small; warning text and progress use error. No subscription-card styling is applied to the home action stack.

### Navigation
Home, Servers, and Help use native NavigationBarItem or NavigationRailItem with vector icons and labels. Active indicators, interaction states, and labels follow Material defaults. The surface-colored top app bar contains the app name, drawer, search, import, and more actions. Advanced upstream destinations remain in the drawer.

### Signal and status
The home mark uses the frontmatter size and is decorative to accessibility because adjacent text expresses state. Compose applies a single tint: primary while running, on-surface-variant otherwise, overriding the vector's intrinsic two-color fills. Setup, ready/disconnected, and running text derive from selection and service state. Loading shows a linear indicator. No ornamental pulse or custom connection animation is installed.

## Do's and Don'ts

### Do:
- **Do** use MaterialTheme color roles so light, dark, and optional dynamic themes remain coherent.
- **Do** retain native font scaling, scrollable content, safe insets, and the home/help width cap.
- **Do** keep status text and the signal tint tied to service state.
- **Do** retain explicit minimum action heights and allow longer translated labels to wrap.

### Don't:
- **Don't** claim protection or successful internet access merely because a connection was requested.
- **Don't** introduce gradients, glow, shield art, or invented connection metrics.
- **Don't** replace the owned vector mark with stock VPN imagery.
- **Don't** promote inherited diagnostic colors or compact utility controls into new brand primitives.

Not canonized: inherited semantic/toast colors and compact subscription utility icon sizing are outside this brand extension; no remediation or new accessibility certification is claimed here. Native system typography is deliberate for this Android task surface, not a decorative display-face precedent.

Evidence: `PRODUCT.md`, `.impeccable/android-brief.md`, `Theme.kt`, `BomberHome.kt`, `MainScreen.kt`, `MainTopBar.kt`, `MainSubscriptionInfo.kt`, `Components.kt`, and the mark/navigation drawable XML in `V2rayNG/app/src/main`. This document describes source behavior; the separate finish review owns runtime findings and its ship verdict.
