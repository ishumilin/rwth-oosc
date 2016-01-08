# Assignment 4 – Use Cases

## UC1: Create Blueprint
**Goal:** Define apartment layout with walls, doors, windows, and dimensions.

Main flow:
1. User selects “Create New Blueprint”.
2. System optionally imports background image.
3. User draws walls, doors, windows, sets dimensions.
4. System validates constraints.
5. User saves blueprint.

## UC2: Insert Furniture
**Goal:** Place furniture in a valid layout.

Main flow:
1. User selects furniture from catalog / import / draw new item.
2. User positions items in layout.
3. System validates constraints.
4. User saves layout.

## UC3: Modify Furniture Layout
**Goal:** Resize, move, group existing furniture.

Main flow:
1. User selects furniture item.
2. User chooses resize / move / group.
3. System validates updated layout.

## UC4: Validate Drawing
**Goal:** Ensure layout is physically feasible.

Main flow:
1. System checks closed shapes for rooms.
2. System checks for overlaps.
3. System returns success or error.