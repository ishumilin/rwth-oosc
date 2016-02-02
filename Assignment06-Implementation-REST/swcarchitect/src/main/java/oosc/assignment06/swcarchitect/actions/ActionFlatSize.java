package oosc.assignment06.swcarchitect.actions;

import org.jhotdraw.draw.DrawingEditor;
import org.jhotdraw.draw.DrawingView;
import org.jhotdraw.draw.Figure;
import org.jhotdraw.draw.action.AbstractSelectedAction;

import javax.swing.JOptionPane;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.geom.Rectangle2D;
import java.util.Collection;

/**
 * Assignment 06.1 - Flat size action.
 *
 * Sums up the areas (width*height) of the bounds of all currently selected figures.
 */
public class ActionFlatSize extends AbstractSelectedAction {
    public static final String ID = "oosc.assignment06.action.flatSize";

    public ActionFlatSize(DrawingEditor editor) {
        super(editor);
        putValue(NAME, "Flat Size");
        putValue(SHORT_DESCRIPTION, "Calculate total area of selected figures");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        double area = calculateFlatArea();
        Component parent = getView() == null ? null : (Component) getView().getComponent();
        JOptionPane.showMessageDialog(parent,
                "Total selected area: " + area,
                "Flat Size",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public double calculateFlatArea() {
        DrawingView view = getView();
        if (view == null) {
            return 0.0;
        }

        Collection<Figure> selected = view.getSelectedFigures();
        double total = 0.0;
        for (Figure f : selected) {
            Rectangle2D.Double r = f.getBounds();
            total += r.width * r.height;
        }
        return total;
    }
}
