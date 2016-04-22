// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometry.genopub;

import java.io.Serializable;
import java.util.Comparator;

public class AnnotationComparator implements Comparator<Annotation>, Serializable
{
    @Override
    public int compare(final Annotation a1, final Annotation a2) {
        return a1.getIdAnnotation().compareTo(a2.getIdAnnotation());
    }
}
