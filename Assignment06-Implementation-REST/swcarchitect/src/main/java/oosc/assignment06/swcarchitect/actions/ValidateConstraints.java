package oosc.assignment06.swcarchitect.actions;

import org.jhotdraw.draw.AttributeKey;
import org.jhotdraw.draw.DrawingEditor;
import org.jhotdraw.draw.DrawingView;
import org.jhotdraw.draw.Figure;
import org.jhotdraw.draw.action.AbstractSelectedAction;

import javax.swing.JOptionPane;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Assignment 06.1 - ValidateConstraints action.
 *
 * Extended constraints:
 * - checks that there are at least 3 "wall" figures (marker attribute)
 * - checks overlap of selected figures using bounding boxes
 * - checks that non-wall figures are inside the union of wall bounds
 * - checks that room size (wall union) exceeds a minimum threshold
 */
public class ValidateConstraints extends AbstractSelectedAction {
    public static final String ID = "oosc.assignment06.action.validateConstraints";

    // Marker attribute used by DesktopApp when creating wall rectangles
    public static final AttributeKey<Boolean> IS_WALL = new AttributeKey<Boolean>("isWall", Boolean.class);

    public ValidateConstraints(DrawingEditor editor) {
        super(editor);
        putValue(NAME, "Validate Constraints");
        putValue(SHORT_DESCRIPTION, "Check minimum wall count and overlaps for selection");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        DrawingView view = getView();
        Component parent = view == null ? null : (Component) view.getComponent();
        if (view == null) {
            JOptionPane.showMessageDialog(parent, "No active view.", "Validate", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Collection<Figure> selected = view.getSelectedFigures();
        int walls = countWalls(selected);
        boolean overlaps = hasOverlaps(selected);
        Rectangle2D.Double room = calculateRoomBounds(selected);
        boolean inside = room != null && allFurnitureInside(selected, room);
        boolean minRoom = room != null && room.width * room.height >= 20000.0;

        StringBuilder sb = new StringBuilder();
        if (walls >= 3) {
            sb.append("OK: Minimum walls (>= 3).\n");
        } else {
            sb.append("FAIL: Minimum walls (need >= 3). Current: ").append(walls).append("\n");
        }

        if (!overlaps) {
            sb.append("OK: No overlaps.\n");
        } else {
            sb.append("FAIL: Overlaps detected.\n");
        }

        if (room == null) {
            sb.append("FAIL: No room bounds (select wall figures).\n");
        } else if (inside) {
            sb.append("OK: Furniture inside room bounds.\n");
        } else {
            sb.append("FAIL: Furniture outside room bounds.\n");
        }

        if (room == null) {
            sb.append("FAIL: Room size not measurable.\n");
        } else if (minRoom) {
            sb.append("OK: Minimum room size met.\n");
        } else {
            sb.append("FAIL: Room area too small.\n");
        }

        JOptionPane.showMessageDialog(parent, sb.toString(), "Validate Constraints", JOptionPane.INFORMATION_MESSAGE);
    }

    private int countWalls(Collection<Figure> figures) {
        int count = 0;
        for (Figure f : figures) {
            Boolean isWall = f.get(IS_WALL);
            if (Boolean.TRUE.equals(isWall)) {
                count++;
            }
        }
        return count;
    }

    private boolean hasOverlaps(Collection<Figure> figures) {
        List<Rectangle2D.Double> rects = new ArrayList<Rectangle2D.Double>();
        for (Figure f : figures) {
            rects.add(f.getBounds());
        }
        for (int i = 0; i < rects.size(); i++) {
            for (int j = i + 1; j < rects.size(); j++) {
                if (rectsIntersect(rects.get(i), rects.get(j))) {
                    return true;
                }
            }
        }
        return false;
    }

    private Rectangle2D.Double calculateRoomBounds(Collection<Figure> figures) {
        Rectangle2D.Double room = null;
        for (Figure f : figures) {
            Boolean isWall = f.get(IS_WALL);
            if (!Boolean.TRUE.equals(isWall)) {
                continue;
            }
            Rectangle2D.Double r = f.getBounds();
            if (room == null) {
                room = new Rectangle2D.Double(r.x, r.y, r.width, r.height);
            } else {
                Rectangle2D.union(room, r, room);
            }
        }
        return room;
    }

    private boolean allFurnitureInside(Collection<Figure> figures, Rectangle2D.Double room) {
        for (Figure f : figures) {
            Boolean isWall = f.get(IS_WALL);
            if (Boolean.TRUE.equals(isWall)) {
                continue;
            }
            Rectangle2D.Double r = f.getBounds();
            if (!contains(room, r)) {
                return false;
            }
        }
        return true;
    }

    private boolean contains(Rectangle2D.Double outer, Rectangle2D.Double inner) {
        return inner.x >= outer.x
                && inner.y >= outer.y
                && inner.x + inner.width <= outer.x + outer.width
                && inner.y + inner.height <= outer.y + outer.height;
    }

    private boolean rectsIntersect(Rectangle2D.Double a, Rectangle2D.Double b) {
        return a.x < b.x + b.width && a.x + a.width > b.x
                && a.y < b.y + b.height && a.y + a.height > b.y;
    }
}
