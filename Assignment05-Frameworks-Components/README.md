# Assignment 05 – Frameworks & Components

This folder contains the solution documentation and a runnable JHotDraw demo for Assignment 05 (Framework analysis and component identification).

## Task Summary
- Map required features to JHotDraw framework elements.
- Identify reusable components and justify component boundaries.
- Provide a minimal runnable JHotDraw demo (Drawing, DrawingView, Figure, Tool, Image import).

## Included Materials
- `src/main/java/oosc/assignment05/frameworks/JHotDrawDemo.java`
- `src/main/java/oosc/assignment05/gui/ArchitectApp.java` (5.3-style GUI)
- `src/main/java/oosc/assignment05/tools/*` (furniture + reference image tools)
- `src/main/java/oosc/assignment05/io/DrawingFileSupport.java` (open/save-as helpers)
- `src/main/resources/oosc/images/` (furniture PNGs)
- `lib/` (JHotDraw 7.6 sources + required JARs)

## Compile & Run (Java SE 7/8)
Use Java 7 or Java 8. If multiple JDKs are installed, set JAVA_HOME
to the older JDK before compiling/running.

From the repo root:

```bash
# Compile JHotDraw 7.6 sources and the Assignment 05 demo
# (Example: export JAVA_HOME=/path/to/jdk7 or jdk8)
javac -source 1.7 -target 1.7 \
  -cp "Assignment05-Frameworks-Components/lib/annotations.jar:Assignment05-Frameworks-Components/lib/jsr305.jar:Assignment05-Frameworks-Components/lib/quaqua.jar" \
  $(find Assignment05-Frameworks-Components/lib/jhotdraw7-src/main/java -name "*.java" | grep -v "/samples/") \
  $(find Assignment05-Frameworks-Components/src/main/java -name "*.java")

# Run the demo
CLASSPATH="Assignment05-Frameworks-Components/lib/annotations.jar:Assignment05-Frameworks-Components/lib/jsr305.jar:Assignment05-Frameworks-Components/lib/quaqua.jar:Assignment05-Frameworks-Components/lib/jhotdraw7-src/main/java:Assignment05-Frameworks-Components/src/main/java" \
  java oosc.assignment05.frameworks.JHotDrawDemo
```

The demo opens a small drawing window and loads a sample image into the drawing.

---

## ArchitectApp (Exercise 5.3 style GUI)

This is a small Swing GUI that mimics the behavior of the reference jar:

- `JFrame` with embedded `DefaultDrawingView`
- `File -> Open...` and `File -> Save As...` using JHotDraw `InputFormat` / `OutputFormat`
- Tool buttons for adding furniture images (from classpath resources)
- Tool button to place an arbitrary image from disk
- Assignment 6.1 actions integrated (Flat Size + Validate Constraints)
- Wall tool (rectangle) to support validation demo

### Run from compiled classes

After running the compile command above, run:

```bash
CLASSPATH="Assignment05-Frameworks-Components/lib/annotations.jar:Assignment05-Frameworks-Components/lib/jsr305.jar:Assignment05-Frameworks-Components/lib/quaqua.jar:Assignment05-Frameworks-Components/lib/jhotdraw7-src/main/java:Assignment05-Frameworks-Components/src/main/java:Assignment05-Frameworks-Components/src/main/resources:Assignment06-Implementation-REST/swcarchitect/src/main/java" \
  java oosc.assignment05.gui.ArchitectApp
```

### Build a runnable JAR

Note: this creates a runnable jar containing **compiled classes + required JHotDraw resource bundles + furniture PNGs**.
External libraries (`annotations.jar`, `jsr305.jar`, `quaqua.jar`) are **not** shaded into the jar.

```bash
# 1) Compile into target/classes
rm -rf Assignment05-Frameworks-Components/target/classes
mkdir -p Assignment05-Frameworks-Components/target/classes

javac -source 1.7 -target 1.7 -d Assignment05-Frameworks-Components/target/classes \
  -cp "Assignment05-Frameworks-Components/lib/annotations.jar:Assignment05-Frameworks-Components/lib/jsr305.jar:Assignment05-Frameworks-Components/lib/quaqua.jar" \
  $(find Assignment05-Frameworks-Components/lib/jhotdraw7-src/main/java -name "*.java" | grep -v "/samples/") \
  $(find Assignment05-Frameworks-Components/src/main/java -name "*.java") \
  $(find Assignment06-Implementation-REST/swcarchitect/src/main/java -name "*.java")

# 2) Copy required resources (furniture images)
mkdir -p Assignment05-Frameworks-Components/target/classes/oosc/images
cp Assignment05-Frameworks-Components/src/main/resources/oosc/images/*.png \
  Assignment05-Frameworks-Components/target/classes/oosc/images/

# 3) Create runnable jar with Main-Class
printf '%s\n' 'Manifest-Version: 1.0' 'Main-Class: oosc.assignment05.gui.ArchitectApp' '' \
  > Assignment05-Frameworks-Components/target/manifest.mf

jar cfm Assignment05-Frameworks-Components/target/architectapp.jar \
  Assignment05-Frameworks-Components/target/manifest.mf \
  -C Assignment05-Frameworks-Components/target/classes .

# 4) Add JHotDraw resource bundles (required at runtime)
jar uf Assignment05-Frameworks-Components/target/architectapp.jar \
  -C Assignment05-Frameworks-Components/lib/jhotdraw7-src/main/java org/jhotdraw/draw/Labels.properties \
  -C Assignment05-Frameworks-Components/lib/jhotdraw7-src/main/java org/jhotdraw/gui/Labels.properties \
  -C Assignment05-Frameworks-Components/lib/jhotdraw7-src/main/java org/jhotdraw/app/Labels.properties \
  -C Assignment05-Frameworks-Components/lib/jhotdraw7-src/main/java org/jhotdraw/undo/Labels.properties
```

Run the jar:

```bash
java -cp "Assignment05-Frameworks-Components/lib/annotations.jar:Assignment05-Frameworks-Components/lib/jsr305.jar:Assignment05-Frameworks-Components/lib/quaqua.jar:Assignment05-Frameworks-Components/target/architectapp.jar" \
  -jar Assignment05-Frameworks-Components/target/architectapp.jar
```

