// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.util;

public final class NibbleIterator implements SearchableCharIterator
{
    private final int length;
    private final byte[] nibble_array;
    private static final char[] nibble2char;
    private static final byte[] char2nibble;
    private static final int hifilter = 15;
    private static final int lofilter = 240;
    private static final int[] offsets;
    private static final int[] filters;
    private static final int one_mask = 1;
    
    public NibbleIterator(final byte[] nibs, final int len) {
        this.length = len;
        this.nibble_array = nibs;
    }
    
    private char charAt(final int pos) {
        return charAt(this.nibble_array, pos);
    }
    
    private static char charAt(final byte[] nibbles, final int pos) {
        final int index = pos & 0x1;
        final int offset = NibbleIterator.offsets[index];
        final int filter = NibbleIterator.filters[index];
        final byte by = nibbles[pos >> 1];
        final int arrIndex = (by & filter) >>> offset;
        return NibbleIterator.nibble2char[arrIndex];
    }
    
    @Override
    public String substring(final int start, final int end) {
        return nibblesToString(this.nibble_array, start, end);
    }
    
    @Override
    public int getLength() {
        return this.length;
    }
    
    @Override
    public int indexOf(final String str, int fromIndex) {
        final char[] querychars = str.toCharArray();
        final int max = this.length - str.length();
        if (fromIndex >= this.length) {
            if (this.length == 0 && fromIndex == 0 && str.length() == 0) {
                return 0;
            }
            return -1;
        }
        else {
            if (fromIndex < 0) {
                fromIndex = 0;
            }
            if (str.length() == 0) {
                return fromIndex;
            }
            final int strOffset = 0;
            final char first = querychars[strOffset];
            int i = fromIndex;
        Label_0073:
            while (true) {
                if (i <= max && this.charAt(i) != first) {
                    ++i;
                }
                else {
                    if (i > max) {
                        return -1;
                    }
                    int j = i + 1;
                    final int end = j + str.length() - 1;
                    int k = strOffset + 1;
                    while (j < end) {
                        if (this.charAt(j++) != querychars[k++]) {
                            ++i;
                            continue Label_0073;
                        }
                    }
                    return i;
                }
            }
        }
    }
    
    public static byte[] stringToNibbles(final String str, final int start, final int end) {
        if (start >= end) {
            System.out.println("in NibbleIterator.stringToNibbles(), start >= end NOT YET IMPLEMENTED");
            return null;
        }
        final int length = end - start;
        final int extra_nibble = length & 0x1;
        final byte[] nibbles = new byte[length / 2 + extra_nibble];
        int byte_index;
        char ch1;
        char ch2;
        byte hinib;
        byte lonib;
        byte two_nibbles;
        for (int i = 0; i < length - 1; ++i, ch2 = str.charAt(i + start), hinib = NibbleIterator.char2nibble[ch1], lonib = NibbleIterator.char2nibble[ch2], two_nibbles = (byte)((hinib << 4) + lonib), nibbles[byte_index] = two_nibbles, ++i) {
            byte_index = i >> 1;
            ch1 = str.charAt(i + start);
        }
        if (extra_nibble > 0) {
            final int byte_index2 = length - 1 >> 1;
            final char ch3 = str.charAt(length - 1 + start);
            final byte hinib2 = NibbleIterator.char2nibble[ch3];
            final byte singlet_nibble = (byte)(hinib2 << 4);
            nibbles[byte_index2] = singlet_nibble;
        }
        return nibbles;
    }
    
    public static String nibblesToString(final byte[] nibbles, final int start, final int end) {
        String residues = null;
        final boolean forward = start <= end;
        final int min = Math.min(start, end);
        final int max = Math.max(start, end);
        char[] charArr = nibblesToCharArr(nibbles, min, max);
        residues = new String(charArr);
        charArr = null;
        if (!forward) {
            residues = DNAUtils.reverseComplement(residues);
        }
        return residues;
    }
    
    public static char[] nibblesToCharArr(final byte[] nibbles, final int start, final int end) {
        final char[] charArr = new char[end - start];
        for (int pos = start; pos < end; ++pos) {
            charArr[pos - start] = charAt(nibbles, pos);
        }
        return charArr;
    }
    
    static {
        nibble2char = new char[] { 'A', 'C', 'G', 'T', 'N', 'M', 'R', 'W', 'S', 'Y', 'K', 'V', 'H', 'D', 'B', 'U' };
        (char2nibble = new byte[256])[65] = (NibbleIterator.char2nibble[97] = 0);
        NibbleIterator.char2nibble[67] = (NibbleIterator.char2nibble[99] = 1);
        NibbleIterator.char2nibble[71] = (NibbleIterator.char2nibble[103] = 2);
        NibbleIterator.char2nibble[84] = (NibbleIterator.char2nibble[116] = 3);
        NibbleIterator.char2nibble[78] = (NibbleIterator.char2nibble[110] = 4);
        NibbleIterator.char2nibble[77] = (NibbleIterator.char2nibble[109] = 5);
        NibbleIterator.char2nibble[82] = (NibbleIterator.char2nibble[114] = 6);
        NibbleIterator.char2nibble[87] = (NibbleIterator.char2nibble[119] = 7);
        NibbleIterator.char2nibble[83] = (NibbleIterator.char2nibble[115] = 8);
        NibbleIterator.char2nibble[89] = (NibbleIterator.char2nibble[121] = 9);
        NibbleIterator.char2nibble[75] = (NibbleIterator.char2nibble[107] = 10);
        NibbleIterator.char2nibble[86] = (NibbleIterator.char2nibble[118] = 11);
        NibbleIterator.char2nibble[72] = (NibbleIterator.char2nibble[104] = 12);
        NibbleIterator.char2nibble[68] = (NibbleIterator.char2nibble[100] = 13);
        NibbleIterator.char2nibble[66] = (NibbleIterator.char2nibble[98] = 14);
        NibbleIterator.char2nibble[85] = (NibbleIterator.char2nibble[117] = 15);
        offsets = new int[] { 4, 0 };
        filters = new int[] { 240, 15 };
    }
}
