# Bento Land, Android coding exercise

Build a small Android app for a Tokyo bento shop from the attached design.

**Time: 30 minutes.** Start a fresh project, use whatever tooling you normally
use. Claude Code, Copilot, Cursor and friends are all fine, we use them too.

## The design

Open `design/Bento Land.dc.html` in a browser (keep the other files in that
folder next to it). Three screens:

1. **Menu**: shop header, category chips, scrolling list of bento
2. **Detail**: photo header, description, what's in the box, quantity stepper, buy bar
3. **Confirmation**: order receipt with subtotal, tax and total

The two typefaces are Zen Maru Gothic and Zen Kaku Gothic New, both on Google
Fonts. Bundle the TTFs, use downloadable fonts, or skip them and use the system
font. We are not counting APK size and we are not going to fault you for it.

## The API

One public endpoint, plain GET, no auth:

```
https://bentoland-menu.s3.ap-northeast-1.amazonaws.com/menu.json
```

Ten items. Each one has `name`, `name_ja`, `description`, `long_description`,
`price` (integer yen), `calories`, `category`, `tag`, `sold_out`, `contents[]`
and an `image_url`.

Each item also carries `kanji` and a `gradient` pair. That is the placeholder
tile from the design: draw it behind the photo while the photo loads, and leave
it showing if the photo never arrives.

## What we'd like to see

- **Menu** and **Detail** working against the live API, close to the design.
- Prices formatted like the design: `¥880`, `¥1,280`.
- The category chips filter the list.
- The sold-out item renders as sold out and cannot be ordered.
- Confirmation screen if you have time. Tax is 8%, rounded to the yen.

Don't worry about a backend for orders, a real cart, or tests. Thirty minutes
is thirty minutes: get the menu and detail feeling right rather than getting
all three screens half done.

## Before you start the clock

Get a project building and running first, then start the 30 minutes. Setup is
not the exercise and we are not timing it.

Two things that can eat ten minutes if you are scaffolding a fresh Compose
project and reaching for the newest of everything:

- **AGP 9 has Kotlin support built in.** Applying `org.jetbrains.kotlin.android`
  on top of it is a hard error, and the `kotlin { compilerOptions { } }` block
  goes away with it. Set the JVM target in `android { compileOptions { } }`.
- **The current androidx releases want `compileSdk = 37`,** which is not in the
  stable SDK channel yet. You need
  `sdkmanager --channel=3 "platforms;android-37.1" "build-tools;37.0.0"`.

The simplest way past both is to take whatever Android Studio's new-project
template gives you and not bump anything to "latest". That set is internally
consistent, and nothing in this exercise needs a recent library. We are not
grading your Gradle file.

## Sending it back

A zip or a GitHub link with:

1. The project.
2. A short `NOTES.md`: what you finished, what you'd do next, and anything in
   the design or the API you had to make a judgement call on.
3. If you used an AI assistant, export the session transcript and include it.
   Claude Code writes one per session under `~/.claude/projects/`; Cursor and
   Copilot both have an export in the chat pane. Messy is expected, we read it
   to see how you work, not to grade tidiness. Have a skim before you send it
   and pull anything personal.

We'll go through it together on a call.
