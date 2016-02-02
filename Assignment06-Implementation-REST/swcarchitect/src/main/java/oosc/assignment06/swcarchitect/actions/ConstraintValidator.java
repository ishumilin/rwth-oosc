package oosc.assignment06.swcarchitect.actions;

import java.util.List;

/**
 * Minimal validator for room constraints.
 */
public class ConstraintValidator {
    public boolean hasMinimumWalls(int wallCount) {
        return wallCount >= 3;
    }

    public boolean hasOverlaps(List<Rect> rects) {
        for (int i = 0; i < rects.size(); i++) {
            for (int j = i + 1; j < rects.size(); j++) {
                if (rects.get(i).intersects(rects.get(j))) {
                    return true;
                }
            }
        }
        return false;
    }

    public static final class Rect {
        private final double x;
        private final double y;
        private final double w;
        private final double h;

        public Rect(double x, double y, double w, double h) {
            this.x = x;
            this.y = y;
            this.w = w;
            this.h = h;
        }

        public boolean intersects(Rect other) {
            return x < other.x + other.w && x + w > other.x
                && y < other.y + other.h && y + h > other.y;
        }
    }
}