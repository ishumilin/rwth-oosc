package oosc.assignment05.io;

import org.jhotdraw.draw.Drawing;
import org.jhotdraw.draw.DrawingView;
import org.jhotdraw.draw.io.InputFormat;
import org.jhotdraw.draw.io.OutputFormat;
import org.jhotdraw.gui.filechooser.ExtensionFileFilter;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import java.io.FileInputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * Encapsulates Open/Save-As functionality for a JHotDraw Drawing.
 *
 * Uses background threads to keep the GUI responsive.
 */
public class DrawingFileSupport {
    private final JFrame parent;
    private final Drawing drawing;
    private final DrawingView view;

    private final JFileChooser openChooser;
    private final JFileChooser saveChooser;

    private final HashMap<FileFilter, InputFormat> inputFormats;
    private final HashMap<FileFilter, OutputFormat> outputFormats;

    public DrawingFileSupport(JFrame parent, Drawing drawing, DrawingView view) {
        this.parent = parent;
        this.drawing = drawing;
        this.view = view;

        this.openChooser = new JFileChooser();
        this.saveChooser = new JFileChooser();
        this.inputFormats = new HashMap<FileFilter, InputFormat>();
        this.outputFormats = new HashMap<FileFilter, OutputFormat>();

        initFileChoosers();
    }

    private void initFileChoosers() {
        // Open formats
        List<InputFormat> ins = drawing.getInputFormats();
        for (int i = 0; i < ins.size(); i++) {
            InputFormat f = ins.get(i);
            FileFilter ff = f.getFileFilter();
            openChooser.addChoosableFileFilter(ff);
            inputFormats.put(ff, f);
        }

        // Save formats
        List<OutputFormat> outs = drawing.getOutputFormats();
        for (int i = 0; i < outs.size(); i++) {
            OutputFormat f = outs.get(i);
            FileFilter ff = f.getFileFilter();
            saveChooser.addChoosableFileFilter(ff);
            outputFormats.put(ff, f);
        }
    }

    public void openAsync() {
        int res = openChooser.showOpenDialog(parent);
        if (res != JFileChooser.APPROVE_OPTION) {
            return;
        }

        final File file = openChooser.getSelectedFile();
        final FileFilter chosen = openChooser.getFileFilter();
        final InputFormat format = inputFormats.get(chosen);
        if (format == null) {
            JOptionPane.showMessageDialog(parent,
                    "No input format is associated with this file filter.",
                    "Open",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        setUiEnabled(false);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    FileInputStream in = null;
                    try {
                        in = new FileInputStream(file);
                        format.read(in, drawing, true);
                    } finally {
                        if (in != null) {
                            try {
                                in.close();
                            } catch (IOException ignored) {
                                // ignore
                            }
                        }
                    }
                } catch (IOException e) {
                    showErrorLater("Open failed", e);
                } finally {
                    setUiEnabledLater(true);
                }
            }
        }, "architectapp-open").start();
    }

    public void saveAsAsync() {
        int res = saveChooser.showSaveDialog(parent);
        if (res != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File raw = saveChooser.getSelectedFile();
        final FileFilter chosen = saveChooser.getFileFilter();
        final OutputFormat format = outputFormats.get(chosen);
        if (format == null) {
            JOptionPane.showMessageDialog(parent,
                    "No output format is associated with this file filter.",
                    "Save As",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        final File file = ensureExtension(raw, format);

        setUiEnabled(false);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    format.write(file.toURI(), drawing);
                } catch (IOException e) {
                    showErrorLater("Save failed", e);
                } finally {
                    setUiEnabledLater(true);
                }
            }
        }, "architectapp-save").start();
    }

    private File ensureExtension(File file, OutputFormat format) {
        // Prefer the format's file filter behavior (which may append extension)
        // but fall back to OutputFormat.getFileExtension().
        FileFilter ff = format.getFileFilter();
        if (ff instanceof ExtensionFileFilter) {
            return ((ExtensionFileFilter) ff).makeAcceptable(file);
        }

        String ext = format.getFileExtension();
        if (ext == null || ext.length() == 0) {
            return file;
        }
        String name = file.getName();
        String lower = name.toLowerCase();
        String dotExt = "." + ext.toLowerCase();
        if (lower.endsWith(dotExt)) {
            return file;
        }
        return new File(file.getParentFile(), name + dotExt);
    }

    private void setUiEnabled(final boolean enabled) {
        view.setEnabled(enabled);
    }

    private void setUiEnabledLater(final boolean enabled) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                setUiEnabled(enabled);
            }
        });
    }

    private void showErrorLater(final String title, final Exception ex) {
        ex.printStackTrace();
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JOptionPane.showMessageDialog(parent,
                        ex.getClass().getSimpleName() + ": " + ex.getMessage(),
                        title,
                        JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
