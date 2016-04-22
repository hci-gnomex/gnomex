// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl;

public interface SeqSpan
{
    int getStart();
    
    int getEnd();
    
    int getMin();
    
    int getMax();
    
    int getLength();
    
    boolean isForward();
    
    BioSeq getBioSeq();
    
    double getStartDouble();
    
    double getEndDouble();
    
    double getMaxDouble();
    
    double getMinDouble();
    
    double getLengthDouble();
    
    boolean isIntegral();
}
