package oosc.assignment05.gui;

import org.jhotdraw.draw.DefaultDrawing;
import org.jhotdraw.draw.DefaultDrawingEditor;
import org.jhotdraw.draw.DefaultDrawingView;
import org.jhotdraw.draw.Drawing;
import org.jhotdraw.draw.ImageFigure;
import org.jhotdraw.draw.Figure;
import org.jhotdraw.draw.RectangleFigure;
import org.jhotdraw.draw.AttributeKeys;
import org.jhotdraw.draw.io.ImageInputFormat;
import org.jhotdraw.draw.tool.CreationTool;
import org.jhotdraw.draw.tool.SelectionTool;

import oosc.assignment06.swcarchitect.actions.ActionFlatSize;
import oosc.assignment06.swcarchitect.actions.ActionSpaceMetrics;
import oosc.assignment06.swcarchitect.actions.ValidateConstraints;
import oosc.assignment05.io.DrawingFileSupport;
import oosc.assignment05.tools.AddBathTub;
import oosc.assignment05.tools.AddBed;
import oosc.assignment05.tools.AddChair;
import oosc.assignment05.tools.AddSofa;
import oosc.assignment05.tools.AddTable;
import oosc.assignment05.tools.ReferenceImageTool;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static org.jhotdraw.draw.AttributeKeys.CANVAS_FILL_COLOR;
import static org.jhotdraw.draw.AttributeKeys.CANVAS_HEIGHT;
import static org.jhotdraw.draw.AttributeKeys.CANVAS_WIDTH;

/**
 * A small JHotDraw7-based Swing GUI.
 *
 * Features:
 * - DrawingView (DefaultDrawingView)
 * - Tool buttons for adding furniture images
 * - Reference image tool (choose arbitrary image file)
 * - File -> Open..., Save As... with background I/O
 *
 * Java SE 7/8 compatible (no streams/lambdas).
 */
public class ArchitectApp extends JFrame {
    private final DefaultDrawingView view;
    private final DefaultDrawingEditor editor;
    private final Drawing drawing;
    private final DrawingFileSupport fileSupport;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ArchitectApp app = new ArchitectApp();
                app.setVisible(true);
            }
        });
    }

    public ArchitectApp() {
        super("OOSC 5.3 - ArchitectApp");

        this.drawing = createDrawing();

        this.view = new DefaultDrawingView();
        this.view.setDrawing(drawing);
        this.view.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));

        this.editor = new DefaultDrawingEditor();
        this.editor.add(view);
        this.editor.setTool(new SelectionTool());

        this.fileSupport = new DrawingFileSupport(this, drawing, view);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setJMenuBar(createMenuBar());
        add(createToolBar(), BorderLayout.NORTH);

        JPanel center = new JPanel(new BorderLayout());
        center.add(view.getComponent(), BorderLayout.CENTER);
        add(center, BorderLayout.CENTER);

        pack();
        setSize(900, 650);
        setLocationRelativeTo(null);
    }

    private Drawing createDrawing() {
        DefaultDrawing d = new DefaultDrawing();
        d.set(CANVAS_WIDTH, 800d);
        d.set(CANVAS_HEIGHT, 600d);
        d.set(CANVAS_FILL_COLOR, Color.WHITE);

        // Enable loading arbitrary images via ImageInputFormat
        ImageFigure prototype = new ImageFigure(0, 0, 200, 150);
        ImageInputFormat imageInput = new ImageInputFormat(prototype);
        d.addInputFormat(imageInput);

        return d;
    }

    private JMenuBar createMenuBar() {
        JMenuBar bar = new JMenuBar();
        JMenu file = new JMenu("File");

        JMenuItem open = new JMenuItem("Open...");
        open.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    fileSupport.openAsync();
                } catch (Exception ex) {
                    showError("Open failed", ex);
                }
            }
        });

        JMenuItem saveAs = new JMenuItem("Save As...");
        saveAs.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    fileSupport.saveAsAsync();
                } catch (Exception ex) {
                    showError("Save failed", ex);
                }
            }
        });

        file.add(open);
        file.add(saveAs);
        bar.add(file);

        JMenu actions = new JMenu("Actions");
        actions.add(new JMenuItem(new ActionFlatSize(editor)));
        actions.add(new JMenuItem(new ActionSpaceMetrics(editor)));
        actions.add(new JMenuItem(new ValidateConstraints(editor)));
        bar.add(actions);
        return bar;
    }

    private JToolBar createToolBar() {
        JToolBar tb = new JToolBar();
        tb.setFloatable(false);

        tb.add(createToolButton("Select", new SelectionTool()));
        tb.addSeparator();

        tb.add(createToolButton("Chair", new AddChair()));
        tb.add(createToolButton("Table", new AddTable()));
        tb.add(createToolButton("Bed", new AddBed()));
        tb.add(createToolButton("Sofa", new AddSofa()));
        tb.add(createToolButton("BathTub", new AddBathTub()));
        tb.add(createToolButton("Wall", new CreationTool(createWallFigure())));
        tb.addSeparator();
        tb.add(createToolButton("Image...", new ReferenceImageTool(this)));

        tb.addSeparator();
        tb.add(new JButton(new ActionFlatSize(editor)));
        tb.add(new JButton(new ActionSpaceMetrics(editor)));
        tb.add(new JButton(new ValidateConstraints(editor)));

        return tb;
    }

    private JButton createToolButton(String label, final org.jhotdraw.draw.tool.Tool tool) {
        JButton btn = new JButton(label);
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editor.setTool(tool);
            }
        });
        return btn;
    }

    private void showError(String title, Exception ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this,
                ex.getClass().getSimpleName() + ": " + ex.getMessage(),
                title,
                JOptionPane.ERROR_MESSAGE);
    }

    private Figure createWallFigure() {
        RectangleFigure f = new RectangleFigure(10, 10, 200, 30);
        f.set(AttributeKeys.FILL_COLOR, new Color(0x4ECDC4));
        f.set(ValidateConstraints.IS_WALL, Boolean.TRUE);
        return f;
    }
}
