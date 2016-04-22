// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.util;

import java.net.URL;
import com.affymetrix.genometryImpl.symmetry.CompositeGraphSym;
import com.affymetrix.genometryImpl.symmetry.CompositeMismatchGraphSym;
import java.util.Arrays;
import com.affymetrix.genometryImpl.general.GenericFeature;
import com.affymetrix.genometryImpl.parsers.Parser;
import java.io.IOException;
import java.util.Collections;
import com.affymetrix.genometryImpl.parsers.graph.GraphParser;
import com.affymetrix.genometryImpl.AnnotatedSeqGroup;
import java.io.InputStream;
import com.affymetrix.genometryImpl.parsers.FileTypeHandler;
import com.affymetrix.genometryImpl.parsers.FileTypeCategory;
import com.affymetrix.genometryImpl.parsers.FileTypeHolder;
import java.util.Iterator;
import java.util.List;
import com.affymetrix.genometryImpl.symmetry.GraphIntervalSym;
import com.affymetrix.genometryImpl.SeqSpan;
import com.affymetrix.genometryImpl.BioSeq;
import cern.colt.list.FloatArrayList;
import cern.colt.list.IntArrayList;
import com.affymetrix.genometryImpl.symmetry.SeqSymmetry;
import com.affymetrix.genometryImpl.symmetry.GraphSym;

public final class GraphSymUtils
{
    public static final String PREF_APPLY_PERCENTAGE_FILTER = "apply graph percentage filter";
    private static final int MAX_INITCAP = 1048576;
    
    public static GraphSym transformGraphSym(final GraphSym original_graf, final SeqSymmetry mapsym) {
        if (original_graf.getPointCount() == 0) {
            return null;
        }
        final BioSeq fromseq = original_graf.getGraphSeq();
        final SeqSpan fromspan = mapsym.getSpan(fromseq);
        if (fromseq == null || fromspan == null) {
            return null;
        }
        final GraphSym new_graf = null;
        final BioSeq toseq = SeqUtils.getOtherSeq(mapsym, fromseq);
        final SeqSpan tospan = mapsym.getSpan(toseq);
        if (toseq == null || tospan == null) {
            return null;
        }
        final double graf_base_length = original_graf.getMaxXCoord() - original_graf.getMinXCoord();
        final double points_per_base = original_graf.getPointCount() / graf_base_length;
        int initcap = (int)(points_per_base * toseq.getLength() * 1.5);
        if (initcap > 1048576) {
            initcap = 1048576;
        }
        final IntArrayList new_xcoords = new IntArrayList(initcap);
        final FloatArrayList new_ycoords = new FloatArrayList(initcap);
        IntArrayList new_wcoords = null;
        if (hasWidth(original_graf)) {
            new_wcoords = new IntArrayList(initcap);
        }
        addCoords(mapsym, fromseq, toseq, original_graf, new_xcoords, new_ycoords, new_wcoords);
        return createGraphSym(new_xcoords, new_ycoords, original_graf, toseq, new_wcoords, new_graf);
    }
    
    private static void addCoords(final SeqSymmetry mapsym, final BioSeq fromseq, final BioSeq toseq, final GraphSym original_graf, final IntArrayList new_xcoords, final FloatArrayList new_ycoords, final IntArrayList new_wcoords) {
        final List<SeqSymmetry> leaf_syms = SeqUtils.getLeafSyms(mapsym);
        for (final SeqSymmetry leafsym : leaf_syms) {
            final SeqSpan fspan = leafsym.getSpan(fromseq);
            final SeqSpan tspan = leafsym.getSpan(toseq);
            if (fspan != null) {
                if (tspan == null) {
                    continue;
                }
                final boolean opposite_spans = fspan.isForward() ^ tspan.isForward();
                final int ostart = fspan.getStart();
                final int oend = fspan.getEnd();
                double scale = tspan.getLengthDouble() / fspan.getLengthDouble();
                if (opposite_spans) {
                    scale = -scale;
                }
                final double offset = tspan.getStartDouble() - scale * fspan.getStartDouble();
                final int kmax = original_graf.getPointCount();
                int start_index = 0;
                if (!hasWidth(original_graf)) {
                    start_index = original_graf.determineBegIndex(ostart - 1);
                }
                for (int k = start_index; k < kmax; ++k) {
                    final int old_xcoord = original_graf.getGraphXCoord(k);
                    if (old_xcoord >= oend) {
                        break;
                    }
                    int new_x2coord;
                    int new_xcoord = new_x2coord = (int)(scale * old_xcoord + offset);
                    if (hasWidth(original_graf)) {
                        final int old_x2coord = old_xcoord + ((GraphIntervalSym)original_graf).getGraphWidthCoord(k);
                        new_x2coord = (int)(scale * old_x2coord + offset);
                        if (new_x2coord >= tspan.getEnd()) {
                            new_x2coord = tspan.getEnd();
                        }
                    }
                    final int tstart = tspan.getStart();
                    if (new_xcoord < tstart) {
                        if (!hasWidth(original_graf)) {
                            continue;
                        }
                        if (new_x2coord <= tstart) {
                            continue;
                        }
                        new_xcoord = tstart;
                    }
                    new_xcoords.add(new_xcoord);
                    new_ycoords.add(original_graf.getGraphYCoord(k));
                    if (hasWidth(original_graf)) {
                        final int new_wcoord = new_x2coord - new_xcoord;
                        new_wcoords.add(new_wcoord);
                    }
                }
            }
        }
    }
    
    private static GraphSym createGraphSym(final IntArrayList new_xcoords, final FloatArrayList new_ycoords, final GraphSym original_graf, final BioSeq toseq, final IntArrayList new_wcoords, GraphSym new_graf) {
        final String newid = original_graf.getID();
        new_xcoords.trimToSize();
        final int[] new_xcoordArr = new_xcoords.elements();
        new_ycoords.trimToSize();
        final float[] new_ycoordArr = new_ycoords.elements();
        if (!hasWidth(original_graf)) {
            new_graf = new GraphSym(new_xcoordArr, new_ycoordArr, newid, toseq);
        }
        else {
            new_wcoords.trimToSize();
            final int[] new_wcoordArr = new_wcoords.elements();
            new_graf = new GraphIntervalSym(new_xcoordArr, new_wcoordArr, new_ycoordArr, newid, toseq);
        }
        new_graf.setGraphName(original_graf.getGraphName());
        return new_graf;
    }
    
    public static boolean isAGraphExtension(final String ext) {
        if (ext == null || ext.isEmpty()) {
            return false;
        }
        final FileTypeHandler fth = FileTypeHolder.getInstance().getFileTypeHandler(ext);
        return fth != null && fth.getFileTypeCategory() == FileTypeCategory.Graph;
    }
    
    public static List<GraphSym> readGraphs(final InputStream istr, final String stream_name, final AnnotatedSeqGroup seq_group, final BioSeq seq) throws IOException {
        final StringBuffer stripped_name = new StringBuffer();
        GeneralUtils.unzipStream(istr, stream_name, stripped_name);
        final String sname = stripped_name.toString().toLowerCase();
        final FileTypeHandler fileTypeHandler = FileTypeHolder.getInstance().getFileTypeHandlerForURI(sname);
        if (fileTypeHandler != null) {
            final Parser parser = fileTypeHandler.getParser();
            if (parser instanceof GraphParser) {
                List<GraphSym> grafs = ((GraphParser)parser).readGraphs(istr, stream_name, seq_group, seq);
                if (grafs == null) {
                    grafs = Collections.emptyList();
                }
                return grafs;
            }
        }
        throw new IOException("Unrecognized filename for a graph file:\n" + stream_name);
    }
    
    public static String getUniqueGraphID(final String id, final BioSeq seq) {
        return AnnotatedSeqGroup.getUniqueGraphID(id, seq);
    }
    
    public static void processGraphSyms(final List<GraphSym> grafs, final String original_stream_name, final GenericFeature feature) {
        if (grafs == null) {
            return;
        }
        for (GraphSym gsym : grafs) {
            final BioSeq gseq = gsym.getGraphSeq();
            if (gseq != null) {
                final String gid = gsym.getID();
                final String newid = getUniqueGraphID(gid, gseq);
                if (!newid.equals(gid)) {
                    gsym.setID(newid);
                }
            }
            gsym.lockID();
            if (gseq != null) {
                gseq.addAnnotation(gsym);
            }
            gsym.setProperty("source_url", original_stream_name);
            gsym.getGraphState().getTierStyle().setFeature(feature);
            if (gsym.getGraphName() != null && gsym.getGraphName().indexOf("TransFrag") >= 0) {
                gsym = convertTransFragGraph(gsym);
            }
        }
    }
    
    public static void writeGraphFile(final GraphSym gsym, final AnnotatedSeqGroup seq_group, final String file_name) throws IOException {
        final FileTypeHandler fileTypeHandler = FileTypeHolder.getInstance().getFileTypeHandlerForURI(file_name);
        if (fileTypeHandler != null) {
            final Parser parser = fileTypeHandler.getParser();
            if (parser instanceof GraphParser) {
                ((GraphParser)parser).writeGraphFile(gsym, seq_group, file_name);
                return;
            }
        }
        throw new IOException("Graph file name does not have the correct extension");
    }
    
    public static float[] calcPercents2Scores(final float[] scores, final float bins_per_percent) {
        final int max_sample_size = 100000;
        final float abs_max_percent = 100.0f;
        final float percents_per_bin = 1.0f / bins_per_percent;
        final int num_scores = scores.length;
        float[] ordered_scores;
        if (num_scores > 2 * max_sample_size) {
            final int sample_step = num_scores / max_sample_size;
            int sample_index = 0;
            ordered_scores = new float[max_sample_size];
            for (int i = 0; i < max_sample_size; ++i) {
                ordered_scores[i] = scores[sample_index];
                sample_index += sample_step;
            }
        }
        else {
            ordered_scores = new float[num_scores];
            System.arraycopy(scores, 0, ordered_scores, 0, num_scores);
        }
        Arrays.sort(ordered_scores);
        final int num_percents = (int)(abs_max_percent * bins_per_percent + 1.0f);
        final float[] percent2score = new float[num_percents];
        final float scores_per_percent = ordered_scores.length / 100.0f;
        if (ordered_scores.length > 0) {
            for (float percent = 0.0f; percent <= abs_max_percent; percent += percents_per_bin) {
                int score_index = (int)(percent * scores_per_percent);
                if (score_index >= ordered_scores.length) {
                    score_index = ordered_scores.length - 1;
                }
                percent2score[Math.round(percent * bins_per_percent)] = ordered_scores[score_index];
            }
            percent2score[percent2score.length - 1] = ordered_scores[ordered_scores.length - 1];
        }
        return percent2score;
    }
    
    private static GraphSym convertTransFragGraph(final GraphSym trans_frag_graph) {
        final int xcount = trans_frag_graph.getPointCount();
        if (xcount < 2) {
            return null;
        }
        final int transfrag_max_spacer = 20;
        final BioSeq seq = trans_frag_graph.getGraphSeq();
        final IntArrayList newx = new IntArrayList();
        final FloatArrayList newy = new FloatArrayList();
        int xmin = trans_frag_graph.getMinXCoord();
        float y_at_xmin = trans_frag_graph.getGraphYCoord(0);
        int prevx = xmin;
        float prevy = y_at_xmin;
        int curx = xmin;
        float cury = y_at_xmin;
        for (int i = 1; i < xcount; ++i) {
            curx = trans_frag_graph.getGraphXCoord(i);
            cury = trans_frag_graph.getGraphYCoord(i);
            if (curx - prevx > transfrag_max_spacer) {
                newx.add(xmin);
                newy.add(y_at_xmin);
                newx.add(prevx);
                newy.add(prevy);
                if (i == xcount - 2) {
                    System.out.println("breaking, i = " + i + ", xcount = " + xcount);
                    break;
                }
                xmin = curx;
                y_at_xmin = cury;
                ++i;
            }
            prevx = curx;
            prevy = cury;
        }
        newx.add(xmin);
        newy.add(y_at_xmin);
        newx.add(curx);
        newy.add(cury);
        final String newid = getUniqueGraphID(trans_frag_graph.getGraphName(), seq);
        newx.trimToSize();
        newy.trimToSize();
        final GraphSym span_graph = new GraphSym(newx.elements(), newy.elements(), newid, seq);
        span_graph.setProperties(trans_frag_graph.cloneProperties());
        span_graph.setProperty("TransFrag", "TransFrag");
        return span_graph;
    }
    
    public static void addChildGraph(final GraphSym cgraf, final String id, final String name, final String stream_name, final SeqSpan overlapSpan) {
        final BioSeq aseq = cgraf.getGraphSeq();
        final GraphSym pgraf = getParentGraph(id, name, stream_name, aseq, cgraf);
        cgraf.removeSpan(cgraf.getSpan(aseq));
        cgraf.addSpan(overlapSpan);
        pgraf.addChild(cgraf);
        pgraf.setProperties(cgraf.getProperties());
    }
    
    private static GraphSym getParentGraph(String id, String name, final String stream_name, final BioSeq aseq, final GraphSym cgraf) {
        if (id.endsWith(".useq") || name.endsWith(".useq")) {
            final Object obj = cgraf.getProperty("Graph Strand");
            if (obj != null) {
                String strand = null;
                final Integer strInt = (Integer)obj;
                if (strInt.equals(GraphSym.GRAPH_STRAND_PLUS)) {
                    strand = "+";
                }
                else if (strInt.equals(GraphSym.GRAPH_STRAND_MINUS)) {
                    strand = "-";
                }
                if (strand != null) {
                    id += strand;
                    name += strand;
                }
            }
        }
        GraphSym pgraf = (GraphSym)aseq.getAnnotation(id);
        if (pgraf == null) {
            if (cgraf.getCategory() == FileTypeCategory.Mismatch) {
                pgraf = new CompositeMismatchGraphSym(id, aseq);
            }
            else {
                pgraf = new CompositeGraphSym(id, aseq);
            }
            pgraf.setGraphName(name);
            aseq.addAnnotation(pgraf);
        }
        return pgraf;
    }
    
    private static boolean hasWidth(final GraphSym graf) {
        return graf instanceof GraphIntervalSym;
    }
    
    public static String getGraphNameForURL(final URL furl) {
        String name = furl.getFile();
        final int index = name.lastIndexOf(47);
        if (index > 0) {
            final String last_name = name.substring(index + 1);
            if (last_name.length() > 0) {
                name = GeneralUtils.URLDecode(last_name);
            }
        }
        return name;
    }
    
    public static String getGraphNameForFile(String name) {
        final int index = name.lastIndexOf(System.getProperty("file.separator"));
        if (index > 0) {
            final String last_name = name.substring(index + 1);
            if (last_name.length() > 0) {
                name = last_name;
            }
        }
        return name;
    }
}
