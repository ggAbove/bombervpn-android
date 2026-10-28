# Verification record

Environment: dedicated BomberVPN_API35 Android 15 / API 35 arm64 emulator (Pixel 7 dimensions), macOS host. Native inputs: exact AndroidLibXrayLite tag v26.10.21 and the checked-in HEV gitlink; all four ABIs built.

## Automated checks

From `V2rayNG/`:

```
./gradlew :app:testPlaystoreDebugUnitTest :app:testFdroidDebugUnitTest :app:assemblePlaystoreDebug :app:assembleFdroidDebug :app:lintPlaystoreDebug :app:lintFdroidDebug
```

Passed: 132 JUnit tests per distribution, zero failures/errors/skips, both APK assemblies and both lint tasks. New regression coverage: outer URL decoding, legacy scheme, nested encodings/fragments, invalid/duplicate URL parameters, one import per retained ViewModel, empty/failed result, sanitized exception reporting. Android Lint retains inherited warnings; no new suppressions were introduced.

## Runtime evidence

- Both distributions install alongside each other under separate Bomber package IDs.
- Empty state disables Connect and explains subscription import; Russian and English content exercised.
- `bombervpn://install-sub` imported a local HTTP subscription and downloaded its VLESS profile. `install-config` imported another profile while MainActivity was open; the list refreshed without restarting the process.
- Local sing-box VLESS fixture on host loopback: Android VPN permission accepted, tunnel started; server logged proxied DNS and HTTPS traffic. In-app current-server test succeeded (17 ms in the observed run). No customer subscription or production server was used.
- Light/dark Ink/Mist, wallpaper colors light/dark, font scale 1.3, and 1600×2560 at 240dpi expanded layout captured in `.impeccable/review/`. Theme and size recreation retained selected profile and navigation.
- Help scroll reaches support/privacy content; test control disabled while disconnected. Server selection, help navigation and system Back exercised. Tab/Enter opened Servers; D-pad Right/Center opened Help. Native semantics exposed Russian action labels and selected navigation state.
- TalkBack enabled and bound; double-tap activated Help. Spoken audio not recorded. A first-run configuration/permission transition on the hardware renderer produced an ANR with no focused window; emulator also reported invalid color-buffer handles. After restarting the dedicated emulator with SwiftShader, TalkBack navigation succeeded. No app-side workaround was added on this evidence.

## Reproducible external-import regression

On a dedicated emulator with the debug APK installed:

```
python3 scripts/android-import-smoke.py cc.bmbrvpn.android.fdroid
```

The script adds one synthetic local VLESS profile and checks that it appears while the same application process remains running. It neither starts a VPN nor clears data. Observed regression proof: temporarily removing only MainActivity.onNewIntent caused the missing-profile assertion to fail; restoring the callback made the same scenario pass. Final source and APK were rebuilt after restoring the callback.

## Not run

- Physical-device gestures, battery/background endurance, Android 7/API24 and Android37 runtime matrix: only API35 emulator available in this run.
- Root-mode and proxy-only end-to-end traffic: core implementations unchanged; VPN mode exercised.
- TalkBack spoken-label/audio audit: emulator launched without audio; focus/activation and semantics checked separately.
- Camera decoding from a physical QR image: existing scanner retained; unit/runtime import paths tested.
- Production-signed release and store upload: no owner signing key supplied. Delivered binaries are debug builds; signing workflow remains manual.
