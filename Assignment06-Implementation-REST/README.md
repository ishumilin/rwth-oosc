# Assignment 06 – Implementation & REST

This folder contains the solution implementation for Assignment 06 split into:

- **6.1 Desktop Actions** (JHotDraw/SWCArchitect-style actions)
- **6.2 REST Webservice** (Spring Boot 1.3.x REST resource + static UI)

## Task Summary
- 6.1: Implement actions (flat size, constraint validation) as real JHotDraw actions
- 6.2: Provide a minimal REST webservice and a simple UI to consume it

## Included Materials
### 6.1 Desktop (JHotDraw)
- `swcarchitect/src/main/java/oosc/assignment06/swcarchitect/DesktopApp.java`
- `swcarchitect/src/main/java/oosc/assignment06/swcarchitect/actions/ActionFlatSize.java`
- `swcarchitect/src/main/java/oosc/assignment06/swcarchitect/actions/ValidateConstraints.java`

### 6.2 REST Webservice (Spring Boot)
- `webservice/pom.xml`
- `webservice/src/main/java/oosc/assignment06/webservice/SwcArchitectWebserviceApplication.java`
- `webservice/src/main/java/oosc/assignment06/webservice/rest/ImageResource.java`
- `webservice/src/main/java/oosc/assignment06/webservice/domain/Image.java`
- `webservice/src/main/resources/static/oosc/app/index.html`

### Legacy stubs (kept for reference)
- `rest-api/src/main/java/oosc/assignment06/rest/RestDemo.java`
- `swcarchitect/src/main/java/oosc/assignment06/swcarchitect/actions/FlatSizeAction.java`
- `swcarchitect/src/main/java/oosc/assignment06/swcarchitect/actions/ConstraintValidator.java`

---

## 6.1 Desktop Actions — Compile & Run (Java 7/8)

From the repo root:

```bash
javac -source 1.7 -target 1.7 \
  -cp "Assignment05-Frameworks-Components/lib/annotations.jar:Assignment05-Frameworks-Components/lib/jsr305.jar:Assignment05-Frameworks-Components/lib/quaqua.jar" \
  $(find Assignment05-Frameworks-Components/lib/jhotdraw7-src/main/java -name "*.java" | grep -v "/samples/") \
  $(find Assignment06-Implementation-REST/swcarchitect/src/main/java -name "*.java")

CLASSPATH="Assignment05-Frameworks-Components/lib/annotations.jar:Assignment05-Frameworks-Components/lib/jsr305.jar:Assignment05-Frameworks-Components/lib/quaqua.jar:Assignment05-Frameworks-Components/lib/jhotdraw7-src/main/java:Assignment06-Implementation-REST/swcarchitect/src/main/java" \
  java oosc.assignment06.swcarchitect.DesktopApp

### Integrated Desktop App (Assignment 5 + 6.1)

Assignment 6.1 actions are also wired into the Assignment 5 `ArchitectApp` GUI
to reflect the progressive build. Run it with:

```bash
CLASSPATH="Assignment05-Frameworks-Components/lib/annotations.jar:Assignment05-Frameworks-Components/lib/jsr305.jar:Assignment05-Frameworks-Components/lib/quaqua.jar:Assignment05-Frameworks-Components/lib/jhotdraw7-src/main/java:Assignment05-Frameworks-Components/src/main/java:Assignment05-Frameworks-Components/src/main/resources:Assignment06-Implementation-REST/swcarchitect/src/main/java" \
  java oosc.assignment05.gui.ArchitectApp
```

---

## 6.2 REST Webservice — Build & Run (Java 8)

This uses Spring Boot **1.3.1.RELEASE**. Use **Maven 3.3.x** (e.g. 3.3.9).

Verify Maven version:

```bash
mvn -v
```

```bash
cd Assignment06-Implementation-REST/webservice

# Run tests (optional)
mvn test

# Run the service
mvn spring-boot:run
```

Endpoints:
- `GET http://localhost:8080/info`
- `GET http://localhost:8080/images` (JSON)
- `GET http://localhost:8080/images/{index}` (JPEG stream)
- `POST http://localhost:8080/images` (body: URL as plain text)
- `GET http://localhost:8080/blueprints` (JSON)
- `POST http://localhost:8080/blueprints` (JSON body: `{ "name": "...", "content": "{\"room\":{\"width\":800,\"height\":600},\"furniture\":[{\"width\":50,\"height\":40}]}" }`)
- `GET http://localhost:8080/comments` (JSON)
- `POST http://localhost:8080/comments` (JSON body: `{ "blueprintId": 1, "author": "...", "text": "..." }`)
- `GET http://localhost:8080/moderation` (JSON)
- `POST http://localhost:8080/moderation/{blueprintId}/approve`
- `POST http://localhost:8080/moderation/{blueprintId}/reject`
- `GET http://localhost:8080/blueprints/{id}/space` (JSON metrics)
- `GET http://localhost:8080/blueprints/space` (JSON metrics list)

Static UI:
- `http://localhost:8080/oosc/app/index.html`

