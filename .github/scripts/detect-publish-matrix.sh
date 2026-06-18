#!/usr/bin/env bash
# Emits the set of (loader, mc) combos to publish for a release: only those whose
# functional inputs changed since the previous tag. Every release bumps mod.version,
# so that line is deliberately ignored — otherwise each release would look fully
# changed and we'd republish byte-identical mods to Modrinth/CurseForge.
#
# Env:
#   TAG        release tag being published, e.g. v5.1.0 (required)
#   FORCE_ALL  "true" to publish every combo, ignoring the diff (optional)
#
# Writes `any=<bool>` and `matrix=<json>` to $GITHUB_OUTPUT when set (CI), else to
# stdout (local runs). Diagnostics go to stderr so stdout stays parseable.
set -euo pipefail

: "${TAG:?TAG is required}"

# Previous release tag, resolved from this tag's parent commit.
PREV="$(git describe --tags --abbrev=0 "${TAG}^" 2>/dev/null || true)"

# Every (loader, mc) combo present at this tag, discovered from disk so this script
# needs no hardcoded version list to keep in sync with the build matrix.
combos=()
for l in fabric neoforge; do
  for m in $(ls -1 "$l/versions" 2>/dev/null); do
    combos+=("$l/$m")
  done
done

want_all=0
CHANGED=""
if [ "${FORCE_ALL:-false}" = "true" ] || [ -z "$PREV" ]; then
  echo "Publishing ALL combos (force_all=${FORCE_ALL:-false}, prev='${PREV:-none}')." >&2
  want_all=1
else
  echo "Diffing ${PREV}..${TAG}" >&2
  CHANGED="$(git diff --name-only "$PREV" "$TAG")"
  echo "Changed files:" >&2
  echo "$CHANGED" >&2

  # Global triggers: shared build infra that affects every jar's content.
  # settings.gradle.kts is excluded — its only routine change is adding a version,
  # which the per-combo rule below already covers.
  if echo "$CHANGED" | grep -qE '^(buildSrc/|stonecutter\.gradle\.kts|build\.gradle\.kts|gradle/|gradlew)'; then
    want_all=1
  fi
  # Root gradle.properties counts only if a line other than mod.version changed.
  ver_diff="$(git diff "$PREV" "$TAG" -- gradle.properties || true)"
  non_ver="$(printf '%s\n' "$ver_diff" | grep -E '^[+-]' | grep -vE '^(\+\+\+|---)' | grep -vE '^[+-]mod\.version=' || true)"
  if [ -n "$non_ver" ]; then want_all=1; fi
fi

include=""
add() { [ -n "$include" ] && include+=","; include+="{\"loader\":\"$1\",\"mc\":\"$2\"}"; }

for c in "${combos[@]}"; do
  l="${c%/*}"
  m="${c#*/}"
  if [ "$want_all" -eq 1 ]; then
    add "$l" "$m"
    continue
  fi
  pub=0
  # Per-loader: shared source/build script changes affect all of its versions.
  if echo "$CHANGED" | grep -qE "^${l}/(src/|build\.gradle\.kts|gradle\.properties)"; then pub=1; fi
  # Per-combo: this version's pinned deps changed (also covers newly added dirs).
  if echo "$CHANGED" | grep -qE "^${l}/versions/${m}/"; then pub=1; fi
  [ "$pub" -eq 1 ] && add "$l" "$m" || true
done

out="${GITHUB_OUTPUT:-/dev/stdout}"
if [ -z "$include" ]; then
  echo "No version changed; nothing to publish." >&2
  {
    echo "any=false"
    echo 'matrix={"include":[]}'
  } >> "$out"
else
  echo "Publishing: $include" >&2
  {
    echo "any=true"
    echo "matrix={\"include\":[$include]}"
  } >> "$out"
fi
