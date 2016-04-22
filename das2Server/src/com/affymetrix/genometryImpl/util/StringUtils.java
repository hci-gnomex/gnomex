// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.util;

import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;
import java.awt.FontMetrics;

public final class StringUtils
{
    private static final String ELLIPSIS = "\u2026";
    private static final String SPACE = " ";
    private static final String SEPRATORS = "\\s+/\\\\._-";
    private static final String SEPRATOR_REGEX = "(?<=[\\s+/\\\\._-])";
    
    public static boolean isAllDigits(final CharSequence cseq) {
        final int char_count = cseq.length();
        boolean all_digits = true;
        for (int i = 0; i < char_count; ++i) {
            final char ch = cseq.charAt(i);
            if (!Character.isDigit(ch)) {
                all_digits = false;
                break;
            }
        }
        return all_digits;
    }
    
    public static String[] wrap(final String toWrap, final FontMetrics metrics, final int pixels) {
        return wrap(toWrap, metrics, pixels, 0);
    }
    
    public static String[] wrap(final String toWrap, final FontMetrics metrics, final int pixels, final int maxLines) {
        final List<String> lines = new ArrayList<String>();
        final StringBuilder buffer = new StringBuilder();
        final int spaceWidth = metrics.stringWidth(" ");
        final int ellipsisWidth = metrics.stringWidth("\u2026");
        int remainingWidth = pixels;
        if (pixels < 0 || maxLines < 0) {
            throw new IllegalArgumentException("Neither pixels nor maxLines can be less than 0");
        }
        if (maxLines == 1) {
            remainingWidth -= ellipsisWidth;
        }
        for (final String word : toWrap.split("(?<=[\\s+/\\\\._-])")) {
            final int wordWidth = metrics.stringWidth(word);
            if (wordWidth + spaceWidth > remainingWidth) {
                if (maxLines != 0 && maxLines - 1 == lines.size()) {
                    buffer.append("\u2026");
                    break;
                }
                if (buffer.length() > 0) {
                    buffer.append(" ");
                    lines.add(buffer.toString());
                }
                buffer.setLength(0);
                buffer.append(word);
                remainingWidth = pixels - wordWidth;
                if (maxLines != 0 && maxLines - 1 == lines.size()) {
                    remainingWidth -= ellipsisWidth;
                }
            }
            else {
                buffer.append(word);
                remainingWidth -= wordWidth;
            }
        }
        if (buffer.length() > 0) {
            lines.add(buffer.toString());
        }
        return lines.toArray(new String[lines.size()]);
    }
}
