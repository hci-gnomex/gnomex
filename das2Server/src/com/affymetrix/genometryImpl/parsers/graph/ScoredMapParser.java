// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.parsers.graph;

import com.affymetrix.genometryImpl.GenometryModel;
import java.util.List;
import com.affymetrix.genometryImpl.symmetry.SeqSymmetry;
import com.affymetrix.genometryImpl.symmetry.IndexedSingletonSym;
import cern.colt.list.FloatArrayList;
import java.util.ArrayList;
import com.affymetrix.genometryImpl.SeqSpan;
import com.affymetrix.genometryImpl.span.SimpleSeqSpan;
import com.affymetrix.genometryImpl.symmetry.ScoredContainerSym;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import com.affymetrix.genometryImpl.AnnotatedSeqGroup;
import com.affymetrix.genometryImpl.BioSeq;
import java.io.InputStream;
import java.util.regex.Pattern;
import com.affymetrix.genometryImpl.parsers.Parser;

public final class ScoredMapParser implements Parser
{
    static Pattern line_regex;
    
    public void parse(final InputStream istr, final String stream_name, final BioSeq aseq, final AnnotatedSeqGroup seq_group) {
        try {
            final BufferedReader br = new BufferedReader(new InputStreamReader(istr));
            String line = null;
            final String unique_container_name = AnnotatedSeqGroup.getUniqueGraphID(stream_name, seq_group);
            final ScoredContainerSym parent = new ScoredContainerSym();
            parent.setID(unique_container_name);
            parent.addSpan(new SimpleSeqSpan(0, aseq.getLength(), aseq));
            parent.setProperty("method", stream_name);
            parent.setProperty("container sym", Boolean.TRUE);
            line = br.readLine();
            final String[] headers = ScoredMapParser.line_regex.split(line);
            final List<String> score_names = new ArrayList<String>();
            final List<FloatArrayList> score_arrays = new ArrayList<FloatArrayList>(headers.length);
            System.out.println("headers: " + line);
            for (int i = 2; i < headers.length; ++i) {
                score_names.add(headers[i]);
                score_arrays.add(new FloatArrayList());
            }
            int line_count = 0;
            while ((line = br.readLine()) != null) {
                final String[] fields = ScoredMapParser.line_regex.split(line);
                final int min = Integer.parseInt(fields[0]);
                final int max = Integer.parseInt(fields[1]);
                final SeqSymmetry child = new IndexedSingletonSym(min, max, aseq);
                parent.addChild(child);
                for (int field_index = 2; field_index < fields.length; ++field_index) {
                    final FloatArrayList flist = score_arrays.get(field_index - 2);
                    final float score = Float.parseFloat(fields[field_index]);
                    flist.add(score);
                }
                ++line_count;
            }
            System.out.println("data lines in file: " + line_count);
            for (int score_count = score_names.size(), j = 0; j < score_count; ++j) {
                final String score_name = score_names.get(j);
                final FloatArrayList flist2 = score_arrays.get(j);
                final float[] scores = flist2.elements();
                parent.addScores(score_name, scores);
            }
            aseq.addAnnotation(parent);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    @Override
    public List<? extends SeqSymmetry> parse(final InputStream is, final AnnotatedSeqGroup group, final String nameType, final String uri, final boolean annotate_seq) throws Exception {
        this.parse(is, uri, GenometryModel.getGenometryModel().getSelectedSeq(), group);
        return null;
    }
    
    static {
        ScoredMapParser.line_regex = Pattern.compile("\t");
    }
}
