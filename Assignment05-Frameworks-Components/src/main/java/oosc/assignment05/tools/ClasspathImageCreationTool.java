package oosc.assignment05.tools;

import org.jhotdraw.draw.ImageFigure;
import org.jhotdraw.draw.tool.CreationTool;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

/**
 * Base class for furniture tools.
 *
 * Loads an image from the classpath and assigns it to an ImageFigure prototype.
 */
public class ClasspathImageCreationTool extends CreationTool {
    private final String resourcePath;

    public ClasspathImageCreationTool(String resourcePath) {
        super(new ImageFigure());
        this.resourcePath = resourcePath;
    }

    @Override
    public void activate(final org.jhotdraw.draw.DrawingEditor editor) {
        super.activate(editor);

        // Load in a background thread to keep UI responsive.
        final ImageFigure proto = (ImageFigure) getPrototype();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    BufferedImage img = loadImage(resourcePath);
                    proto.setImage(null, img);
                    proto.setBounds(proto.getBounds());
                } catch (IOException e) {
                    // ignore, CreationTool will still function but without image
                    e.printStackTrace();
                }
            }
        }, "architectapp-image-load").start();
    }

    protected BufferedImage loadImage(String path) throws IOException {
        InputStream in = null;
        try {
            in = getClass().getResourceAsStream(path);
            if (in == null) {
                throw new IOException("Resource not found: " + path);
            }
            return ImageIO.read(in);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ignored) {
                    // ignore
                }
            }
        }
    }
}
