// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.util;

import java.util.regex.Matcher;
import java.math.BigInteger;
import java.util.regex.Pattern;
import java.security.MessageDigest;

public final class UrlToFileName
{
    static MessageDigest md5_generator;
    static boolean md5_init;
    public static final Pattern char_encode_pattern;
    static final Pattern char_decode_pattern;
    static String encoded_query_start;
    
    static void initializeMd5() {
        try {
            UrlToFileName.md5_generator = MessageDigest.getInstance("MD5");
        }
        catch (Exception ex) {
            UrlToFileName.md5_generator = null;
            ex.printStackTrace();
        }
        UrlToFileName.md5_init = true;
    }
    
    public static String toMd5(final String s) {
        if (!UrlToFileName.md5_init) {
            initializeMd5();
        }
        if (UrlToFileName.md5_generator != null) {
            final byte[] md5_digest = UrlToFileName.md5_generator.digest(s.getBytes());
            final BigInteger md5_big_int = new BigInteger(md5_digest);
            return md5_big_int.toString(16);
        }
        return s;
    }
    
    public static String encode(final String url) {
        final Matcher char_encode_matcher = UrlToFileName.char_encode_pattern.matcher(url);
        final StringBuffer buf = new StringBuffer();
        while (char_encode_matcher.find()) {
            final String grp = char_encode_matcher.group();
            final char ch = grp.charAt(0);
            char_encode_matcher.appendReplacement(buf, "&#" + (int)ch + ";");
        }
        char_encode_matcher.appendTail(buf);
        final String result = buf.toString();
        return result;
    }
    
    static {
        UrlToFileName.md5_init = false;
        char_encode_pattern = Pattern.compile("[\\\\/\\:\\*\\?\\\"\\<\\>\\|\\+]");
        char_decode_pattern = Pattern.compile("&#(\\d+);");
        UrlToFileName.encoded_query_start = "&#63;";
    }
}
