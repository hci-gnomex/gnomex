// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.event;

import com.affymetrix.genometryImpl.symmetry.GraphSym;
import com.affymetrix.genometryImpl.symmetry.SeqSymmetry;

public interface PropertyHandler
{
    String[][] getPropertiesRow(final SeqSymmetry p0, final PropertyHolder p1);
    
    String[][] getGraphPropertiesRowColumn(final GraphSym p0, final int p1, final PropertyHolder p2);
    
    void showGraphProperties(final GraphSym p0, final int p1, final PropertyHolder p2);
}
