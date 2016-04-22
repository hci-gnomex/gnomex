// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.util;

import java.util.Collections;
import java.util.Set;
import java.util.Iterator;
import java.io.File;
import java.util.LinkedHashSet;
import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;
import javax.swing.filechooser.FileFilter;

public final class UniFileFilter extends FileFilter
{
    private HashSet<String> filters;
    private String description;
    private String fullDescription;
    private boolean useExtensionsInDescription;
    List<String> compression_endings;
    
    public UniFileFilter() {
        this.filters = null;
        this.description = null;
        this.fullDescription = null;
        this.useExtensionsInDescription = true;
        this.compression_endings = new ArrayList<String>(4);
        this.filters = new LinkedHashSet<String>();
    }
    
    public UniFileFilter(final String extension) {
        this(extension, null);
    }
    
    public UniFileFilter(final String extension, final String description) {
        this();
        if (extension != null) {
            this.addExtension(extension);
        }
        if (description != null) {
            this.setDescription(description);
        }
    }
    
    public UniFileFilter(final String[] filters, final String description) {
        this();
        for (int i = 0; i < filters.length; ++i) {
            this.addExtension(filters[i]);
        }
        if (description != null) {
            this.setDescription(description);
        }
    }
    
    @Override
    public boolean accept(final File f) {
        if (f != null) {
            if (f.isDirectory()) {
                return true;
            }
            final String base_name = this.stripCompressionEndings(f.getName().toLowerCase());
            final Iterator<String> iter = this.filters.iterator();
            while (iter.hasNext()) {
                final String ending = "." + iter.next();
                if (base_name.endsWith(ending)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public void addCompressionEnding(final String ending) {
        this.compression_endings.add(ending.toLowerCase());
    }
    
    public void addCompressionEndings(final String[] endings) {
        for (int i = 0; i < endings.length; ++i) {
            this.addCompressionEnding(endings[i]);
        }
    }
    
    private String stripCompressionEndings(String name) {
        if (this.compression_endings != null) {
            for (final String ending : this.compression_endings) {
                name = stripCompressionEnding(name, ending);
            }
        }
        return name;
    }
    
    private static String stripCompressionEnding(final String name, final String ending) {
        if (name.endsWith(ending)) {
            final int index = name.lastIndexOf(ending);
            return name.substring(0, index);
        }
        return name;
    }
    
    public void addExtension(final String extension) {
        if (this.filters == null) {
            this.filters = new LinkedHashSet<String>(5);
        }
        this.filters.add(extension.toLowerCase());
        this.fullDescription = null;
    }
    
    public Set<String> getExtensions() {
        return Collections.unmodifiableSet((Set<? extends String>)this.filters);
    }
    
    @Override
    public String getDescription() {
        if (this.fullDescription == null) {
            if (this.description == null || this.isExtensionListInDescription()) {
                this.fullDescription = ((this.description == null) ? "(" : (this.description + " ("));
                final Iterator<String> extensions = this.filters.iterator();
                if (extensions.hasNext()) {
                    this.fullDescription = this.fullDescription + "*." + extensions.next();
                    while (extensions.hasNext()) {
                        this.fullDescription = this.fullDescription + ", *." + extensions.next();
                    }
                }
                this.fullDescription += ")";
            }
            else {
                this.fullDescription = this.description;
            }
        }
        return this.fullDescription;
    }
    
    public void setDescription(final String description) {
        this.description = description;
        this.fullDescription = null;
    }
    
    public void setExtensionListInDescription(final boolean b) {
        this.useExtensionsInDescription = b;
        this.fullDescription = null;
    }
    
    public boolean isExtensionListInDescription() {
        return this.useExtensionsInDescription;
    }
}
