package oosc.assignment06.swcarchitect;

import oosc.assignment06.swcarchitect.actions.ActionFlatSize;
import oosc.assignment06.swcarchitect.actions.ValidateConstraints;
import org.jhotdraw.draw.DefaultDrawing;
import org.jhotdraw.draw.DefaultDrawingEditor;
import org.jhotdraw.draw.DefaultDrawingView;
import org.jhotdraw.draw.Drawing;
import org.jhotdraw.draw.Figure;
import org.jhotdraw.draw.RectangleFigure;
import org.jhotdraw.draw.AttributeKeys;
import org.jhotdraw.draw.tool.CreationTool;
import org.jhotdraw.draw.tool.SelectionTool;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;

import static org.jhotdraw.draw.AttributeKeys.CANVAS_FILL_COLOR;
import static org.jhotdraw.draw.AttributeKeys.CANVAS_HEIGHT;
import static org.jhotdraw.draw.AttributeKeys.CANVAS_WIDTH;

/**
 * Assignment 06.1 - Desktop app stub.
 *
 * Provides a small JHotDraw editor and two actions:
 * - Flat size (sum of selected figures bounds)
 * - Constraint validation (minimum walls + overlap check)
 */
public class DesktopApp extends JFrame {
    private final DefaultDrawingView view;
    private final DefaultDrawingEditor editor;
    private final Drawing drawing;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new DesktopApp().setVisible(true);
            }
        });
    }

    public DesktopApp() {
        super("Assignment 06.1 - Desktop Actions");

        this.drawing = createDrawing();
        this.view = new DefaultDrawingView();
        this.view.setDrawing(drawing);
        this.view.setPreferredSize(new Dimension(900, 650));

        this.editor = new DefaultDrawingEditor();
        this.editor.add(view);
        this.editor.setTool(new SelectionTool());

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setJMenuBar(createMenuBar());
        add(createToolBar(), BorderLayout.NORTH);
        add(view.getComponent(), BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
    }

    private Drawing createDrawing() {
        DefaultDrawing d = new DefaultDrawing();
        d.set(CANVAS_WIDTH, 900d);
        d.set(CANVAS_HEIGHT, 650d);
        d.set(CANVAS_FILL_COLOR, Color.WHITE);
        return d;
    }

    private JMenuBar createMenuBar() {
        JMenuBar bar = new JMenuBar();

        JMenu tools = new JMenu("Tools");
        tools.add(new JMenuItem(new AbstractAction("Select") {
            @Override
            public void actionPerformed(ActionEvent e) {
                editor.setTool(new SelectionTool());
            }
        }));
        tools.add(new JMenuItem(new AbstractAction("Add Wall (Rectangle)") {
            @Override
            public void actionPerformed(ActionEvent e) {
                editor.setTool(new CreationTool(createWallFigure()));
            }
        }));
        bar.add(tools);

        JMenu actions = new JMenu("Actions");
        actions.add(new JMenuItem(new ActionFlatSize(editor)));
        actions.add(new JMenuItem(new ValidateConstraints(editor)));
        bar.add(actions);

        return bar;
    }

    private JToolBar createToolBar() {
        JToolBar tb = new JToolBar();
        tb.setFloatable(false);

        JButton select = new JButton("Select");
        select.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editor.setTool(new SelectionTool());
            }
        });
        tb.add(select);

        JButton wall = new JButton("Wall");
        wall.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editor.setTool(new CreationTool(createWallFigure()));
            }
        });
        tb.add(wall);

        tb.addSeparator();
        tb.add(new JButton(new ActionFlatSize(editor)));
        tb.add(new JButton(new ValidateConstraints(editor)));

        return tb;
    }

    private Figure createWallFigure() {
        RectangleFigure f = new RectangleFigure(10, 10, 200, 30);
        f.set(AttributeKeys.FILL_COLOR, new Color(0x4ECDC4));
        // Use the figure's attribute map as a marker
        f.set(ValidateConstraints.IS_WALL, Boolean.TRUE);
        return f;
    }
}
