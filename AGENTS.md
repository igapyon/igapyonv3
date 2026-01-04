# AGENTS.md

This repository documents its behavior in `docs/README.md` and the other markdown files under `docs/`.
Read those files first to understand the expected behavior and operational details.

Suggested reading order:

1) `docs/README.md`
2) `docs/ARCHITECTURE.md`
3) `docs/OPERATIONS.md`
4) `docs/PROGRAMS.md`

Notes:

- igapyonv3 is a Java-based static site/blog generator that transforms Markdown into HTML.
- Source code lives under `src/main/java/`.
- This project is built with Maven; see `pom.xml` for dependencies and plugin configuration.
- Runtime behavior is driven by `settings.src.md` (FreeMarker-based settings file).
- igapyonv3 users typically run `mvn clean exec:java@igdiary antrun:run` (requires `exec-maven-plugin` `igdiary` entry in `pom.xml`).
- igapyonv3 developers typically build/install with `mvn clean install`.
- HTML output is generated from Markdown and targets Tailwind CSS class usage.
