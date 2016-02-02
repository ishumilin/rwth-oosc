package oosc.assignment06.swcarchitect.actions;

import java.util.List;

/**
 * Minimal action stub: computes total area from a list of rectangle-like figures.
 * This mimics the course concept without external framework dependencies.
 */
public class FlatSizeAction {
    public double calculateArea(List<RectFigure> figures) {
        double total = 0.0;
        for (RectFigure fig : figures) {
            total += fig.getWidth() * fig.getHeight();
        }
        return total;
    }

    public static final class RectFigure {
        private final double width;
        private final double height;

        public RectFigure(double width, double height) {
            this.width = width;
            this.height = height;
        }

        public double getWidth() {
            return width;
        }

        public double getHeight() {
            return height;
        }
    }
}