# Full Sol review — 2026-10-29

Scope: upstream aa4b05503a504a3f2e43818e6ee8ebf1caaa3df0 through merged fork 15f6673e6923cdebd7c1f930ef8ede6f491dd6fa plus local fixes in fix/preview-update-version. Includes untracked regression test. Reviewer: GPT-6.1 Sol, Codex provider. Five sequential rounds; user explicitly extended the initial three-round limit by two. Effort and usage were not exposed.

## Verdict

APPROVE on the cumulative reviewed scope. Rounds 1–3 explicitly reported incomplete coverage and were not treated as full approval. Round 4 completed service-state and import-storage traces; round 5 completed update/release consumers. No open demonstrated findings remain. This is code-review approval, not independent verification on every supported device.

## Findings and changes

S1 / should / resolved: preview release tags caused NumberFormatException in UpdateCheckerManager.compareVersions. Accepted and fixed locally: numeric core, prerelease ordering, stable precedence and ignored build metadata. Five regression cases failed on the old function; all six focused tests pass with the fix. Version advanced from 1.0.0/code754 to 1.0.1-preview.1/code755 so the next preview follows the version name already installed. Effective codes: Playstore 4000755; F-Droid 5075500–5075504. Sol confirmed the fix in rounds 2 and 5.

No declined findings. No S2+ defects established. Three implementation/test files changed: build.gradle.kts, UpdateCheckerManager.kt and UpdateCheckerManagerTest.kt. Published binaries remain unchanged; no commit, push or new release was performed for this review.

## Cumulative coverage

- Fork diff, applicable root/UI/service instructions, design/product/build documentation, themes, resources, locales, navigation, identity, shortcuts and workflows.
- Home actions through MainActivity permission handling, LauncherManager, CoreServiceManager and VPN/proxy/root callbacks; stop and reverse state via MessageHelper, MainRepository and MainViewModel. Global selected GUID remains distinct from browsed group.
- External import through UrlSchemeActivity, parser, retained UrlImportViewModel, AngConfigManager and MmkvManager; subscription/profile writes and MainActivity.onNewIntent group/profile refresh.
- Update endpoints, version comparison, ABI/flavor asset selection, confirmation and ACTION_VIEW consumer, packaging codes and signing boundaries. Live GitHub metadata independently inspected by Sol.

## Validation

Parent ran six focused regression tests, full unit suites (138 tests per distribution, zero failures/errors/skips), Playstore Kotlin compile, both debug assemblies and both lints. All passed. Sol read tests and independently traced consumers but did not rerun Gradle or runtime checks. Previous API35 runtime evidence remains described in TESTING.md.

## Release limitations

- Current public v1.0.0-preview.1 APK still has the original comparator and reports versionName 1.0.0. Existing installations require a manually downloaded fixed APK; source changes do not repair installed binaries.
- No stable release exists: releases/latest returns 404, leading to the existing generic failure toast for stable-only checks.
- Asset selection requires exact ABI/flavor; it has no universal fallback. Current public release contains Playstore arm64 and universal only, no F-Droid assets. No new reachable fork regression was established against this release; future releases must account for this contract.
- ACTION_VIEW delegates download/install externally. Production signing continuity is not established by the debug release or workflow secrets.

## Not run

Independent Sol Gradle/device runs; device download/install and signer compatibility; physical-device/API24/API37, endurance, root/proxy traffic and TalkBack audio checks. These are coverage limitations, not demonstrated defects.
