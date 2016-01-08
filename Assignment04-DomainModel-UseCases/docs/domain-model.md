# Assignment 4 – Domain Model

**Domain:** 2D architecture / interior design application.

## Key Concepts

- **Layout**: collection of rooms, walls, openings, and dimensions.
- **Room**: bounded area with walls, doors, windows, and furniture.
- **Furniture**: items that occupy space; grouped and positioned on a layout.
- **Measurement**: dimensions, area calculations, and constraints.
- **Blueprint**: saved layout state with metadata.

## Relationships

- Layout **contains** Rooms and Furniture.
- Room **contains** Walls, Windows, Doors, and Furniture.
- Furniture **occupies** Measurement areas.

## Constraints

- A room must have at least three walls.
- Furniture must not overlap walls or other furniture.
- Layout must form a closed shape before publishing.