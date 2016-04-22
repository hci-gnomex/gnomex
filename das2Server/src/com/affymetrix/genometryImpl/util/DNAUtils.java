// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.util;

public final class DNAUtils
{
    public static String reverseComplement(final String s) {
        if (s == null) {
            return null;
        }
        final StringBuffer buf = new StringBuffer(s);
        reverseComplement(buf);
        return buf.toString();
    }
    
    public static void reverseComplement(final StringBuffer sb) {
        if (null == sb) {
            return;
        }
        for (int i = sb.length() - 1, j = 0; i >= j; --i, ++j) {
            final char c = complementChar(sb.charAt(i));
            sb.setCharAt(i, complementChar(sb.charAt(j)));
            sb.setCharAt(j, c);
        }
    }
    
    private static char complementChar(final char b) {
        switch (b) {
            case 'A': {
                return 'T';
            }
            case 'C': {
                return 'G';
            }
            case 'G': {
                return 'C';
            }
            case 'T': {
                return 'A';
            }
            case 'U': {
                return 'A';
            }
            case 'M': {
                return 'K';
            }
            case 'R': {
                return 'Y';
            }
            case 'Y': {
                return 'R';
            }
            case 'K': {
                return 'M';
            }
            case 'V': {
                return 'B';
            }
            case 'H': {
                return 'D';
            }
            case 'D': {
                return 'H';
            }
            case 'B': {
                return 'V';
            }
            case 'N': {
                return 'X';
            }
            case 'X': {
                return 'X';
            }
            case 'a': {
                return 't';
            }
            case 'c': {
                return 'g';
            }
            case 'g': {
                return 'c';
            }
            case 't': {
                return 'a';
            }
            case 'u': {
                return 'a';
            }
            case 'm': {
                return 'k';
            }
            case 'r': {
                return 'y';
            }
            case 'y': {
                return 'r';
            }
            case 'k': {
                return 'm';
            }
            case 'v': {
                return 'b';
            }
            case 'h': {
                return 'd';
            }
            case 'd': {
                return 'h';
            }
            case 'b': {
                return 'v';
            }
            case 'n': {
                return 'x';
            }
            case 'x': {
                return 'x';
            }
            default: {
                return '-';
            }
        }
    }
}
