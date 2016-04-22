// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.parsers;

import java.io.IOException;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import com.affymetrix.genometryImpl.AnnotatedSeqGroup;
import com.affymetrix.genometryImpl.GenometryModel;
import java.io.InputStream;
import java.util.regex.Pattern;

public final class ChromInfoParser
{
    private static final Pattern tab_regex;
    
    public static final AnnotatedSeqGroup parse(final InputStream istr, final GenometryModel gmodel, final String genome_version) throws IOException {
        final AnnotatedSeqGroup seq_group = gmodel.addSeqGroup(genome_version);
        final BufferedReader dis = new BufferedReader(new InputStreamReader(istr));
        String line;
        while ((line = dis.readLine()) != null && !Thread.currentThread().isInterrupted()) {
            if (line.length() != 0) {
                if (line.startsWith("#")) {
                    continue;
                }
                final String[] fields = ChromInfoParser.tab_regex.split(line);
                if (fields.length == 0) {
                    continue;
                }
                if (fields.length == 1) {
                    System.out.println("WARNING: chromInfo line does not match.  Ignoring: " + line);
                }
                else {
                    final String chrom_name = fields[0];
                    int chrLength = 0;
                    try {
                        chrLength = Integer.parseInt(fields[1]);
                    }
                    catch (NumberFormatException ex) {
                        System.out.println("WARNING: chromInfo line does not match.  Ignoring: " + line);
                        continue;
                    }
                    seq_group.addSeq(chrom_name, chrLength);
                }
            }
        }
        return seq_group;
    }
    
    static {
        tab_regex = Pattern.compile("\t");
    }
}
