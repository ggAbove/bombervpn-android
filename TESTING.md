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
- Store upload: not attempted.

## Simplified home and server list (2026-10-29)

API35 emulator, Play Store debug build at branch HEAD.

- Unit: `BomberGroupPolicyTest` (empty-group hiding, imported subscription id, selection fallback); full suites 143/143 per flavor; `lintPlaystoreDebug`, `lintFdroidDebug`.
- Import: subscription from a local HTTP fixture via file picker and via `bombervpn://install-sub`; empty built-in "Default" group hidden, a server of the imported group selected; selection still shown after force-stop and relaunch.
- States: setup (no subscription), ready, running (real connect, `tun0` up), zero servers on the Servers tab with disabled connect button.
- Themes: light, dark, font scale 1.3, dynamic color on and off; server list single and double column.
- Semantics tree (uiautomator): connect control is one clickable node whose child carries the action label; server row is one clickable node containing caption and name; chevron has no label.
- D-pad: one DOWN focuses the connect control, CENTER opens the system VPN consent; CENTER on the server row opens Servers; BACK returns to Home.
- Row ⋮ menu: QR, copy link, copy full config, edit, delete.

Found, not changed (outside scope): keyboard TAB past the bottom navigation moves focus into the closed navigation drawer; ENTER then opens a drawer destination.

Not run:
- TalkBack spoken output: emulator has no audio; semantics checked through the accessibility tree only.
- Tablet / navigation-rail width: no tablet AVD.
- Physical device.

## Release signing (2026-10-29)

- `scripts/build-release.sh`: 10 release APKs (Play Store and F-Droid, 4 ABIs + universal) signed by the release certificate; each verified with apksigner, v2 scheme, one signer.
- Workflow verify step run locally: release APKs pass and get SHA256SUMS; a planted debug APK fails the step.
- API35 emulator, signed Play Store arm64 release: clean install, import via `bombervpn://install-sub` deeplink, server auto-selected, system VPN permission, `tun0` up and validated.
- Upgrade from the published debug-signed preview to the signed release: `INSTALL_FAILED_UPDATE_INCOMPATIBLE` (expected; preview must be uninstalled).
- Not run: the CI workflow itself on GitHub (needs the branch pushed).
