// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.parsers;

import com.affymetrix.genometryImpl.symmetry.SeqSymmetry;
import com.affymetrix.genometryImpl.BioSeq;
import java.io.IOException;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import com.affymetrix.genometryImpl.symmetry.SingletonSymWithProps;
import java.util.List;
import com.affymetrix.genometryImpl.AnnotatedSeqGroup;
import java.io.InputStream;
import java.util.regex.Pattern;

public final class VarParser implements Parser
{
    private static final String GENOMIC_VARIANTS = "Genomic Variants";
    private static final Pattern line_regex;
    
    public static List<SingletonSymWithProps> parse(final InputStream dis, final AnnotatedSeqGroup seq_group) throws IOException {
        final List<SingletonSymWithProps> results = new ArrayList<SingletonSymWithProps>();
        final Thread thread = Thread.currentThread();
        final BufferedReader reader = new BufferedReader(new InputStreamReader(dis));
        String line = reader.readLine();
        String[] column_names = null;
        column_names = VarParser.line_regex.split(line);
        if (column_names == null) {
            throw new IOException("Column names were missing or malformed");
        }
        int line_count = 1;
        while ((line = reader.readLine()) != null && !thread.isInterrupted()) {
            ++line_count;
            final String[] fields = VarParser.line_regex.split(line);
            final int field_count = fields.length;
            if (field_count > column_names.length) {
                throw new IOException("Line " + line_count + " has wrong number of data columns: " + field_count);
            }
            final String variationId = fields[0];
            final String seqid = fields[2];
            final int start = Integer.parseInt(fields[3]);
            final int end = Integer.parseInt(fields[4]);
            BioSeq aseq = seq_group.getSeq(seqid);
            if (aseq == null) {
                aseq = seq_group.addSeq(seqid, end);
            }
            if (start > aseq.getLength()) {
                aseq.setLength(start);
            }
            if (end > aseq.getLength()) {
                aseq.setLength(end);
            }
            final SingletonSymWithProps var = new SingletonSymWithProps(variationId, start, end, aseq);
            var.setProperty("id", variationId);
            var.setProperty("method", "Genomic Variants");
            var.setProperty(column_names[1], fields[1]);
            for (int c = 5; c < fields.length; ++c) {
                final String propName = column_names[c];
                if (!propName.startsWith("Locus")) {
                    var.setProperty(propName, fields[c]);
                }
            }
            results.add(var);
        }
        return results;
    }
    
    @Override
    public List<? extends SeqSymmetry> parse(final InputStream is, final AnnotatedSeqGroup group, final String nameType, final String uri, final boolean annotate_seq) throws Exception {
        return parse(is, group);
    }
    
    static {
        line_regex = Pattern.compile("\\t");
    }
}
