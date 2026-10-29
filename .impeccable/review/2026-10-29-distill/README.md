# Distill pass — 2026-10-29

Scope: home, servers tab, help; brand pinned (Ink/Mist, amber, signal-bomb mark). Decisions by owner: round connect control with the mark; keep three tabs but simplify; import selects a server, does not auto-connect.

Changes
- Home: one round connect control (amber when running, amber ring when ready, muted when setup); headline + single next-step line; selected server as a chevron row; import actions only while no server exists; "Your subscription" section and intro paragraph removed.
- Top bar: search / import / more only on Servers.
- Servers: single ⋮ per row; empty groups hidden while another has servers (fixes the empty "Default" tab after import); first server auto-selected after import; empty-state hint; connect button disabled without a selection.
- Help: intro paragraph removed; support before log.

Evidence (emulator BomberVPN_API35, Pixel-class 1080×2400)
- r2-home-dark / r2-home-light: ready state, both themes.
- tap2: running state after a real connect to the local fixture.
- r2-setup-dark: first run, no subscription.
- r2-home-light-large: font scale 1.3, nothing clipped.
- e2e-after-import-servers / -home: subscription imported from a local HTTP fixture via the file picker; Default group hidden, server selected.
- final-servers-empty-dark: zero servers.
- help-dark: help tab.
- servers-2col-dynamic-and-more-menu: double column with dynamic color, light and dark, and the row ⋮ menu.
- home-dynamic-vs-brand: home with wallpaper colors next to the default brand theme.

Validation: assemblePlaystoreDebug, unit tests 143/143 per flavor, lintPlaystoreDebug + lintFdroidDebug clean. New pure helpers in BomberGroupPolicy.kt with 5 tests.

Not run: tablet width (no tablet AVD), TalkBack audio, physical device. Help tab copy untouched beyond removing the intro.
