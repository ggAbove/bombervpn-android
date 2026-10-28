# Product

## Register

product

## Users

Ordinary people in Russia, not technical. They open Bomber VPN from a phone, inside Telegram, usually because something they rely on just stopped working (YouTube, Instagram, a messenger, a work tool). They are often mildly stressed and in a hurry. They do not know or care what VLESS, Hysteria or a node is. The job: get a working connection back in as few taps as possible, pay without friction, and trust that it will keep working.

A smaller share are technical users who want to see locations, protocols and device limits, but the product is designed for the first group.

## Product Purpose

Bomber VPN sells and delivers VPN subscriptions through a Telegram bot (@openbomber_vpn_bot) and a Telegram Mini App (app.bmbrvpn.cc). Supporting surfaces: web cabinet (cabinet.bmbrvpn.cc) for users without Telegram, the subscription/install page (sub.bmbrvpn.cc), bot messages and banners, the news channel (@bombervpn_news).

Success: a new user goes from "nothing works" to "connected" in one sitting without contacting support; paying users renew without thinking about it; the brand is recognizable in a Telegram chat list and feels like a dependable tool, not a gamble.

## Brand Personality

Quiet, precise, dependable. The product is a utility that should disappear once it works. Confidence comes from calm, exact typography and restraint, not from noise or claims. The name "Bomber" carries the character on its own; the design does not need to shout it.

Voice: short, plain Russian (and English), sentence case, concrete instructions. No hype, no slang, no exclamation marks, no fear-selling about surveillance.

References (for the feel, not to copy): Linear and Raycast (tight, precise type, one accent, dark surfaces that feel engineered), iOS Settings (systemic, clean, grouped lists, nothing decorative).

## Anti-references

- Typical VPN marketing: NordVPN / Surfshark / Proton look, blue-green palettes, shields with checkmarks, glowing globes, "military-grade encryption" imagery.
- Crypto / casino: neon on black, acid accents, gold, rockets, glow.
- Government services and banks: corporate blue-white, faceless stock UI.
- Cute: mascots, pastel, sticker-style illustrations.
- The current state: stock violet gradient #667eea→#764ba2 everywhere, rainbow per-provider gradients.

## Design Principles

1. Utility first: the connection status and the next action are the loudest things on any screen; brand never competes with them.
2. Native, then ours: respect Telegram light/dark and platform conventions; express the brand through type, one accent and precise details, not through repainting everything.
3. Calm under stress: users arrive when something broke; every screen should lower anxiety with clear state, plain words and one obvious next step.
4. One accent, earned: a single brand color marks what matters (primary action, active state); everything else is neutral.
5. Consistent everywhere: avatar, bot banners, mini app, cabinet and install page share one mark, one palette and one voice.

## Accessibility & Inclusion

WCAG 2.2 AA: text contrast ≥4.5:1 (≥3:1 for large text) in both Telegram light and dark themes. Touch targets ≥44px. Respect prefers-reduced-motion. Never encode status by color alone (connected / expired / error always have text + icon). Russian and English UI; layouts must survive longer English and Russian strings.

## Platform

android

## Android client scope

Native BomberVPN fork of v2rayNG. User-authorized Material You design and full-send delivery. Connect using imported subscriptions, choose servers, test connectivity, and reach the existing Telegram account/support. No embedded panel credentials or invented account data.
