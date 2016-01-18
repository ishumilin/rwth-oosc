package oosc.assignment05.tools;

import org.jhotdraw.draw.ImageFigure;
import org.jhotdraw.draw.tool.CreationTool;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Lets the user pick an image file from disk and place it as an ImageFigure.
 */
public class ReferenceImageTool extends CreationTool {
    private final JFrame parent;

    public ReferenceImageTool(JFrame parent) {
        super(new ImageFigure());
        this.parent = parent;
    }

    @Override
    public void activate(final org.jhotdraw.draw.DrawingEditor editor) {
        super.activate(editor);

        final JFileChooser chooser = new JFileChooser();
        int res = chooser.showOpenDialog(parent);
        if (res != JFileChooser.APPROVE_OPTION) {
            return;
        }

        final File file = chooser.getSelectedFile();
        final ImageFigure proto = (ImageFigure) getPrototype();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    BufferedImage img = ImageIO.read(file);
                    if (img == null) {
                        throw new IOException("Unsupported image format.");
                    }
                    proto.setImage(null, img);
                    proto.setBounds(proto.getBounds());
                } catch (final IOException ex) {
                    ex.printStackTrace();
                    javax.swing.SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            JOptionPane.showMessageDialog(parent,
                                    ex.getClass().getSimpleName() + ": " + ex.getMessage(),
                                    "Image Load Failed",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    });
                }
            }
        }, "architectapp-ref-image-load").start();
    }
}
