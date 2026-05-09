# Section Comment Plugin for IntelliJ and other IDE

Inserts a clean section separator comments that fill exactly to your right margin/hard wrap guide:

```
// ─── Fields ──────────────────────────────────────────────────────────────────────────────────────────────────────────
// ─── Constructor ─────────────────────────────────────────────────────────────────────────────────────────────────────
// ─── Private Methods ─────────────────────────────────────────────────────────────────────────────────────────────────
```

## Features

- **AZERTY and QWERTY friendly shortcut**: `Ctrl+Shift+;` (Windows/Linux) or `Cmd+Shift+;` (macOS)
- **Auto-detects comment style** from file extension:
  - `//` -> Java, Kotlin, JS/TS, C/C++, C#, Go, Swift
  - `#`  -> Python, Ruby, Shell, YAML
  - `/* */` -> CSS
  - `<!-- -->` -> HTML, XML
- **Configurable line length** — Intellij default is 120 (match your Editor -> Code Style -> Hard wrap)
- Available in **Code menu** and **right-click menu**

## Build & Install

### Requirements
- JDK 17+
- IntelliJ IDEA (any edition, 2022.1+)
- Or Android Studio ()

### Steps

```bash
# 1. Clone / unzip this project
cd section-comment-plugin

# 2. Build the plugin zip
./gradlew buildPlugin
# Output: build/distributions/section-comment-plugin-1.0.0.zip

# 3. Install in IntelliJ:
#    Settings → Plugins → ⚙ → Install Plugin from Disk...
#    Select: build/distributions/section-comment-plugin-1.0.0.zip
```

### Run in a sandbox IDE (for development)

```bash
./gradlew runIde
```

## Configuration

**Settings → Tools → Section Comments**

Set the *Right margin / line length* to match your project's code style.  
You can check your current right margin at **Editor → Code Style → (language) → Right Margin**.

## Customization

To change the fill character (`─`) edit `InsertSectionCommentAction.java`:

```java
String fill = "─".repeat(fillLength);  // U+2500
// Other options: "━" (bold), "=" , "-"
```

To change the default shortcut, edit `plugin.xml`:

```xml
<keyboard-shortcut keymap="$default" first-keystroke="ctrl shift SLASH"/>
```
