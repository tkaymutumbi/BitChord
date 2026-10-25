# Fork notes

This is a personal fork of [BitChord](https://github.com/kushagrasinghx/BitChord).

## Scope: mobile only

This fork targets the **Android phone app** (`app/`) only. The desktop app
(`desktopApp/`, `desktopUi/`), the web player (`web/`), TV and Wear OS are not
built, tested or maintained here. Upstream v1.8.1 removes the desktop modules;
that is fine for this fork.

## Builds: release only

Only build **release** APKs (signed, minified, ready to install). Do not build
or hand over debug builds: the goal is an app that is ready to use as-is.
Use `assembleProdRelease`, not `assembleDebug` or `installDebug`.

## Fork-only features

- **Sleep timer** on the Now Playing screen (not in upstream).

## Syncing with upstream

Release work lands on upstream's `vX.Y.Z` branches before `main`. Merge the
release branch (for example `v1.8.1`), not `main`, and commit local changes first.
