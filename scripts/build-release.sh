#!/usr/bin/env bash
# Build signed Bomber VPN release APKs locally (macOS).
#
# The release keystore lives outside the repository. By default it is read from the macOS
# Keychain items created on 2026-10-29:
#   bombervpn-android-keystore-password  keystore and key password
#   bombervpn-android-keystore-b64       base64 copy of the .jks (used when the file is missing)
# Override with BOMBER_KEYSTORE=/path/to.jks and BOMBER_KEYSTORE_PASSWORD=... if needed.
#
# Usage: scripts/build-release.sh [gradle tasks...]   (default: both flavors)
set -euo pipefail

ROOT="$(cd "$(dirname "$0")/.." && pwd)"
ALIAS="${BOMBER_KEY_ALIAS:-bombervpn}"
# SHA-256 of the release signing certificate. Public; a mismatch means the wrong key.
EXPECTED_CERT_SHA256="b15d324a8399110b4e8d6805e515360aad88edb21846c078ff1df4d91c2f0c9b"

KEYSTORE="${BOMBER_KEYSTORE:-$HOME/.bombervpn-signing/bombervpn-release.jks}"
CLEANUP=""
if [[ ! -f "$KEYSTORE" ]]; then
  CLEANUP="$(mktemp -d -t bombervpn-release)"
  KEYSTORE="$CLEANUP/release.jks"
  security find-generic-password -s bombervpn-android-keystore-b64 -w | base64 -d > "$KEYSTORE"
fi
trap 'if [[ -n "$CLEANUP" ]]; then rm -rf "$CLEANUP"; fi' EXIT

if [[ -z "${BOMBER_KEYSTORE_PASSWORD:-}" ]]; then
  BOMBER_KEYSTORE_PASSWORD="$(security find-generic-password -s bombervpn-android-keystore-password -w)"
fi

TASKS=("$@")
# CI also regenerates the license page (licenseFdroidReleaseReport); locally that only churns formatting.
[[ ${#TASKS[@]} -gt 0 ]] || TASKS=(:app:assemblePlaystoreRelease :app:assembleFdroidRelease)

cd "$ROOT/V2rayNG"
# Gradle maps ORG_GRADLE_PROJECT_* to project properties; `env` allows the dotted names bash
# cannot export, and keeps the password out of the command line.
env \
  "ORG_GRADLE_PROJECT_android.injected.signing.store.file=$KEYSTORE" \
  "ORG_GRADLE_PROJECT_android.injected.signing.store.password=$BOMBER_KEYSTORE_PASSWORD" \
  "ORG_GRADLE_PROJECT_android.injected.signing.key.alias=$ALIAS" \
  "ORG_GRADLE_PROJECT_android.injected.signing.key.password=$BOMBER_KEYSTORE_PASSWORD" \
  ./gradlew "${TASKS[@]}" --console=plain

SDK_DIR="$(sed -n 's/^sdk.dir=//p' local.properties 2>/dev/null || true)"
APKSIGNER="$(ls "${ANDROID_HOME:-$SDK_DIR}"/build-tools/*/apksigner 2>/dev/null | sort -V | tail -1)"
[[ -x "$APKSIGNER" ]] || { echo "apksigner not found" >&2; exit 1; }

status=0
while IFS= read -r apk; do
  out="$("$APKSIGNER" verify -v --print-certs "$apk" 2>/dev/null || true)"
  # apksigner prints "V2 Signer: ..." or "Signer #1 ..." depending on schemes; exactly one signer allowed.
  digests="$(sed -n 's/.*certificate SHA-256 digest: //p' <<<"$out" | sort -u)"
  digest="$(head -1 <<<"$digests")"
  if [[ "$digests" == "$EXPECTED_CERT_SHA256" ]] && grep -q '^Number of signers: 1$' <<<"$out"; then
    echo "OK   $(basename "$apk")"
  else
    echo "BAD  $(basename "$apk") signed by '${digest:-unsigned}'" >&2
    status=1
  fi
done < <(find app/build/outputs/apk -path '*/release/*.apk' | sort)
exit $status
