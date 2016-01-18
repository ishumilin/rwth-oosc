package oosc.assignment05.frameworks;

import org.jhotdraw.draw.DefaultDrawing;
import org.jhotdraw.draw.DefaultDrawingEditor;
import org.jhotdraw.draw.DefaultDrawingView;
import org.jhotdraw.draw.Drawing;
import org.jhotdraw.draw.ImageFigure;
import org.jhotdraw.draw.io.ImageInputFormat;
import org.jhotdraw.draw.tool.ImageTool;
import org.jhotdraw.draw.tool.SelectionTool;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static org.jhotdraw.draw.AttributeKeys.CANVAS_FILL_COLOR;
import static org.jhotdraw.draw.AttributeKeys.CANVAS_HEIGHT;
import static org.jhotdraw.draw.AttributeKeys.CANVAS_WIDTH;

/**
 * Minimal runnable demo using JHotDraw 7.6.
 * Demonstrates Drawing, DrawingView, Figure, Tool, and Image input usage.
 */
public class JHotDrawDemo {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    launch();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private static void launch() throws IOException {
        Drawing drawing = new DefaultDrawing();
        drawing.set(CANVAS_WIDTH, 640d);
        drawing.set(CANVAS_HEIGHT, 480d);
        drawing.set(CANVAS_FILL_COLOR, Color.WHITE);

        DefaultDrawingView view = new DefaultDrawingView();
        view.setDrawing(drawing);
        view.setPreferredSize(new Dimension(640, 480));

        DefaultDrawingEditor editor = new DefaultDrawingEditor();
        editor.add(view);

        ImageFigure imageFigure = new ImageFigure(0, 0, 200, 150);
        ImageInputFormat imageInput = new ImageInputFormat(imageFigure);
        drawing.addInputFormat(imageInput);

        BufferedImage image = createSampleImage();
        File imageFile = writeTempImage(image);
        imageInput.read(imageFile, drawing, false);

        ImageTool imageTool = new ImageTool(imageFigure);
        editor.setTool(imageTool);

        JFrame frame = new JFrame("Assignment 05 - JHotDraw Demo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(view.getComponent(), BorderLayout.CENTER);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        editor.setTool(new SelectionTool());
    }

    private static BufferedImage createSampleImage() {
        BufferedImage image = new BufferedImage(200, 150, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        g.setColor(new Color(0x1E88E5));
        g.fillRect(0, 0, 200, 150);
        g.setColor(Color.WHITE);
        g.fillOval(50, 35, 100, 80);
        g.dispose();
        return image;
    }

    private static File writeTempImage(BufferedImage image) throws IOException {
        File tempFile = File.createTempFile("assignment05-demo", ".png");
        tempFile.deleteOnExit();
        try (FileOutputStream out = new FileOutputStream(tempFile)) {
            ImageIO.write(image, "PNG", out);
        }
        return tempFile;
    }
}
