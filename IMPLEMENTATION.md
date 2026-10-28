# BomberVPN Android delivery

User request: fully working v2rayNG fork in BomberVPN style, impeccable, Material You, full send.

## Scope
- Separate Android application ID cc.bmbrvpn.android, branded icon/name, fork-specific update URLs.
- Material 3 home, servers, help; Ink/Mist and optional dynamic color; RU and EN primary content.
- Existing real VPN/proxy/root service and server management retained.
- Clipboard, QR and subscription imports; dedicated bombervpn scheme; subscription updates and connectivity tests.
- Telegram account/support links; no authentication or payment reimplementation.
- APK build, unit tests, emulator flow checks, independent design and Sol review, PR merge.

## Verification
Build native inputs matching pinned submodules. Run Playstore debug unit tests, compile, assemble, lint where required. Emulator: empty/import/invalid link, real VPN handshake using local fixture, disconnect/reconnect, theme, large text, navigation, persistence, activity recreation. Record unsupported checks honestly.

## Tooling
Android SDK installing to /opt/homebrew/share/android-commandlinetools. AAR revision v26.10.21. Signing for installable debug APK is provided by Gradle; production signing requires a durable owner-controlled key.

## Pre-PR verification snapshot
- Fork: ggAbove/bombervpn-android, branch feat/bombervpn, upstream base aa4b05503a504a3f2e43818e6ee8ebf1caaa3df0.
- Native build + both flavors: 132 tests each, debug assemble, lint passed.
- Runtime VPN/import/design checks recorded in TESTING.md.
- Security and architecture reviews: no actionable findings. Impeccable fix verdict: two fixes resolved; final DESIGN.md + design.json written by documenter.
- Sol review: approved in one round, no findings. Next gates at this snapshot: PR/CI/merge and APK delivery. Production signing not supplied; no signed-release claim.
