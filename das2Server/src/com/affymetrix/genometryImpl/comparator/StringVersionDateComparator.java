// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.comparator;

import java.util.Arrays;
import java.util.List;
import java.io.Serializable;
import java.util.Comparator;

public class StringVersionDateComparator implements Comparator<String>, Serializable
{
    public static final long serialVersionUID = 1L;
    private static final String[] month_array;
    private static final List<String> months;
    
    @Override
    public int compare(final String name1, final String name2) {
        final String[] parts1 = name1.split("_");
        final String[] parts2 = name2.split("_");
        final int count1 = parts1.length;
        final int count2 = parts2.length;
        final String yearA = parts1[count1 - 1];
        final String yearB = parts2[count2 - 1];
        int year1 = -1;
        int year2 = -1;
        try {
            year1 = Integer.parseInt(yearA);
        }
        catch (Exception ex) {}
        try {
            year2 = Integer.parseInt(yearB);
        }
        catch (Exception ex2) {}
        if (year1 == -1 && year2 == -1) {
            return 0;
        }
        if (year1 == -1) {
            return 1;
        }
        if (year2 == -1) {
            return -1;
        }
        if (year1 > year2) {
            return -1;
        }
        if (year2 > year1) {
            return 1;
        }
        final String monthA = parts1[count1 - 2];
        final String monthB = parts2[count2 - 2];
        final int month1 = StringVersionDateComparator.months.indexOf(monthA);
        final int month2 = StringVersionDateComparator.months.indexOf(monthB);
        if (month1 == -1 && month2 == -1) {
            return 0;
        }
        if (month1 == -1) {
            return 1;
        }
        if (month2 == -1) {
            return -1;
        }
        return Integer.valueOf(month2).compareTo(Integer.valueOf(month1));
    }
    
    static {
        month_array = new String[] { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };
        months = Arrays.asList(StringVersionDateComparator.month_array);
    }
}
