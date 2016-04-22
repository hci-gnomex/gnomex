// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.util;

import java.util.Iterator;
import java.util.Set;
import javax.swing.Icon;
import java.awt.Component;
import javax.swing.JOptionPane;
import java.util.Collections;
import java.io.File;
import javax.swing.filechooser.FileFilter;
import javax.swing.JFileChooser;

public class UniFileChooser extends JFileChooser
{
    static final long serialVersionUID = 1L;
    private static UniFileChooser static_file_chooser;
    private String description;
    private String extension;
    private FileFilter current_file_filter;
    
    protected UniFileChooser() {
        this.description = "Any file (*.*)";
        this.extension = "";
    }
    
    public UniFileChooser(final String description, final String extension) {
        this();
        this.reinitialize(description, extension);
    }
    
    public static UniFileChooser getFileChooser(final String description, final String extension) {
        if (UniFileChooser.static_file_chooser == null) {
            UniFileChooser.static_file_chooser = new UniFileChooser(description, extension);
        }
        else {
            UniFileChooser.static_file_chooser.reinitialize(description, extension);
        }
        return UniFileChooser.static_file_chooser;
    }
    
    public static UniFileChooser getAXMLFileChooser() {
        return getFileChooser("AXML file", "axml");
    }
    
    public static UniFileChooser getXMLFileChooser() {
        return getFileChooser("XML file", "xml");
    }
    
    public void reinitialize(final String description, final String extension) {
        if (description == null || extension == null || "".equals(extension)) {
            throw new IllegalArgumentException("description and extension cannot be null");
        }
        if (extension.indexOf(46) != -1) {
            throw new IllegalArgumentException("extension should not contain '.'");
        }
        if (this.description != description || this.extension != extension) {
            this.description = description;
            this.extension = extension;
            final FileFilter[] filters = this.getChoosableFileFilters();
            for (int i = 0; i < filters.length; ++i) {
                this.removeChoosableFileFilter(filters[i]);
            }
            this.addChoosableFileFilter(this.current_file_filter = new UniFileFilter(extension, description));
        }
        this.setFileFilter(this.current_file_filter);
        this.setMultiSelectionEnabled(false);
        this.setFileSelectionMode(0);
        this.rescanCurrentDirectory();
        this.setSelectedFile(null);
    }
    
    @Override
    public void approveSelection() {
        final File f = this.getSelectedFile();
        if (f.isDirectory()) {
            this.setSelectedFile(null);
            this.setCurrentDirectory(f);
            return;
        }
        final FileFilter filter = this.getFileFilter();
        UniFileFilter uni_filter = null;
        Set<String> extensions = Collections.emptySet();
        if (filter instanceof UniFileFilter) {
            uni_filter = (UniFileFilter)filter;
            extensions = uni_filter.getExtensions();
        }
        else {
            if (!filter.accept(f)) {
                this.getToolkit().beep();
                return;
            }
            super.approveSelection();
        }
        if (this.getDialogType() == 0) {
            if (f.exists()) {
                super.approveSelection();
            }
            else if (!extensions.isEmpty()) {
                this.getToolkit().beep();
                for (final String ext : extensions) {
                    final File file2 = applyExtension(f, ext);
                    if (file2.exists()) {
                        this.setSelectedFile(file2);
                    }
                }
            }
        }
        else if (this.getDialogType() == 1) {
            for (final String ext : extensions) {
                if (f.getName().endsWith("." + ext)) {
                    if (!f.exists()) {
                        super.approveSelection();
                        return;
                    }
                    this.getToolkit().beep();
                    final String[] options = { "Yes", "No" };
                    if (0 == JOptionPane.showOptionDialog(this, "Overwrite Existing File?", "File Exists", 0, 3, null, options, options[1])) {
                        super.approveSelection();
                    }
                    return;
                }
            }
            if (!extensions.isEmpty()) {
                this.getToolkit().beep();
                final String first_extension = extensions.iterator().next();
                this.setSelectedFile(applyExtension(f, first_extension));
            }
        }
    }
    
    private static File applyExtension(final File f, final String extension) {
        final String name = f.getName();
        final String dotExtension = "." + extension;
        if (name.endsWith(".")) {
            return new File(name + extension);
        }
        if (!name.endsWith(dotExtension)) {
            return new File(name + dotExtension);
        }
        return f;
    }
}
