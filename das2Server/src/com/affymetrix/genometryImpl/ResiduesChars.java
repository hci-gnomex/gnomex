// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl;

import java.util.HashMap;
import java.util.Map;

public enum ResiduesChars
{
    A(new char[] { 'A', 'a' }, 0), 
    T(new char[] { 'T', 't' }, 1), 
    G(new char[] { 'G', 'g' }, 2), 
    C(new char[] { 'C', 'c' }, 3), 
    N(new char[] { 'N', 'n', 'D', 'd', '_' }, 4);
    
    static final Map<Character, Integer> rcMap;
    static final Map<Integer, Character> reverseRcMap;
    char[] chars;
    int value;
    
    public static char getCharFor(final int j) {
        return ResiduesChars.reverseRcMap.get(j);
    }
    
    private ResiduesChars(final char[] chars, final int value) {
        this.chars = chars;
        this.value = value;
    }
    
    public int getValue() {
        return this.value;
    }
    
    @Override
    public String toString() {
        return String.valueOf(this.chars[0]);
    }
    
    public static int getValue(final char ch) {
        final Integer val = ResiduesChars.rcMap.get(ch);
        if (val != null) {
            return val;
        }
        return -1;
    }
    
    static {
        rcMap = new HashMap<Character, Integer>(10);
        reverseRcMap = new HashMap<Integer, Character>(5);
        for (final ResiduesChars rc : values()) {
            for (final char ch : rc.chars) {
                ResiduesChars.rcMap.put(ch, rc.value);
            }
            ResiduesChars.reverseRcMap.put(rc.value, rc.chars[0]);
        }
    }
}
