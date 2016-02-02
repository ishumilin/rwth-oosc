package oosc.assignment06.swcarchitect.actions;

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
 * Calculates usable/free/used space based on selected figures.
 *
 * - Room bounds: union of selected wall figures
 * - Used: sum of non-wall figure bounds
 * - Free: usable - used
 */
public class ActionSpaceMetrics extends AbstractSelectedAction {
    public static final String ID = "oosc.assignment06.action.spaceMetrics";

    public ActionSpaceMetrics(DrawingEditor editor) {
        super(editor);
        putValue(NAME, "Space Metrics");
        putValue(SHORT_DESCRIPTION, "Calculate usable/free/used space for selection");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        DrawingView view = getView();
        Component parent = view == null ? null : (Component) view.getComponent();
        if (view == null) {
            JOptionPane.showMessageDialog(parent, "No active view.", "Space Metrics", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Collection<Figure> selected = view.getSelectedFigures();
        Rectangle2D.Double room = calculateRoomBounds(selected);
        if (room == null) {
            JOptionPane.showMessageDialog(parent,
                    "Select at least one wall figure to define room bounds.",
                    "Space Metrics",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        double usable = room.width * room.height;
        double used = calculateUsedArea(selected);
        double free = Math.max(0.0, usable - used);

        String msg = "Usable area: " + usable + "\n"
                + "Used area: " + used + "\n"
                + "Free area: " + free;

        JOptionPane.showMessageDialog(parent, msg, "Space Metrics", JOptionPane.INFORMATION_MESSAGE);
    }

    private Rectangle2D.Double calculateRoomBounds(Collection<Figure> figures) {
        Rectangle2D.Double room = null;
        for (Figure f : figures) {
            if (Boolean.TRUE.equals(f.get(ValidateConstraints.IS_WALL))) {
                Rectangle2D.Double r = f.getBounds();
                if (room == null) {
                    room = new Rectangle2D.Double(r.x, r.y, r.width, r.height);
                } else {
                    Rectangle2D.union(room, r, room);
                }
            }
        }
        return room;
    }

    private double calculateUsedArea(Collection<Figure> figures) {
        double total = 0.0;
        for (Figure f : figures) {
            if (Boolean.TRUE.equals(f.get(ValidateConstraints.IS_WALL))) {
                continue;
            }
            Rectangle2D.Double r = f.getBounds();
            total += r.width * r.height;
        }
        return total;
    }
}