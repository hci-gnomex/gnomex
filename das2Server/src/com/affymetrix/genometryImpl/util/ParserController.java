// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.util;

import com.affymetrix.genometryImpl.parsers.FileTypeHolder;
import com.affymetrix.genometryImpl.parsers.IndexWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.affymetrix.genometryImpl.parsers.PSLParser;
import java.io.DataInputStream;
import com.affymetrix.genometryImpl.symmetry.GraphSym;
import com.affymetrix.genometryImpl.general.GenericFeature;
import com.affymetrix.genometryImpl.BioSeq;
import com.affymetrix.genometryImpl.parsers.CytobandParser;
import com.affymetrix.genometryImpl.parsers.GFFParser;
import com.affymetrix.genometryImpl.parsers.ExonArrayDesignParser;
import com.affymetrix.genometryImpl.parsers.Bprobe1Parser;
import java.io.BufferedInputStream;
import com.affymetrix.genometryImpl.symmetry.SeqSymmetry;
import com.affymetrix.genometryImpl.AnnotatedSeqGroup;
import com.affymetrix.genometryImpl.parsers.AnnotsXmlParser;
import java.util.List;
import java.io.InputStream;

public final class ParserController
{
    static List<? extends SeqSymmetry> parse(final InputStream instr, final List<AnnotsXmlParser.AnnotMapElt> annotList, final String stream_name, final AnnotatedSeqGroup seq_group, final String type_prefix) {
        InputStream str = null;
        List<? extends SeqSymmetry> results = null;
        try {
            if (!(instr instanceof BufferedInputStream)) {
                str = new BufferedInputStream(instr);
            }
            if (stream_name.endsWith(".bp1") || stream_name.endsWith(".bp2")) {
                System.out.println("loading via Bprobe1Parser: " + stream_name);
                final Bprobe1Parser bp1_reader = new Bprobe1Parser();
                if (type_prefix != null) {
                    bp1_reader.setTypePrefix(type_prefix);
                }
                final String annot_type = getAnnotType(annotList, stream_name, ".bp", type_prefix);
                results = bp1_reader.parse(str, seq_group, true, annot_type, false);
                System.out.println("done loading via Bprobe1Parser: " + stream_name);
            }
            else if (stream_name.endsWith(".ead")) {
                System.out.println("loading via ExonArrayDesignParser");
                final String annot_type2 = getAnnotType(annotList, stream_name, ".ead", type_prefix);
                final ExonArrayDesignParser parser = new ExonArrayDesignParser();
                parser.parse(str, seq_group, true, annot_type2);
                System.out.println("done loading via ExonArrayDesignParser: " + stream_name);
            }
            else {
                if (stream_name.endsWith(".gff") || stream_name.endsWith(".gtf")) {
                    System.out.println("loading via GFFParser: " + stream_name);
                    final GFFParser parser2 = new GFFParser();
                    parser2.addFeatureFilter("intron");
                    parser2.addFeatureFilter("splice3");
                    parser2.addFeatureFilter("splice5");
                    parser2.addFeatureFilter("prim_trans");
                    parser2.addFeatureFilter("gene");
                    parser2.addFeatureFilter("transcript");
                    parser2.setGroupTag("transcript_id");
                    parser2.setUseDefaultSource(true);
                    parser2.setUseTrackLines(false);
                    final String annot_type = (type_prefix == null) ? stream_name.substring(0, stream_name.length() - 4) : type_prefix;
                    return parser2.parse(str, annot_type, seq_group, true);
                }
                if (stream_name.endsWith(".cyt")) {
                    System.out.println("loading via CytobandParser: " + stream_name);
                    final CytobandParser parser3 = new CytobandParser();
                    return parser3.parse(str, seq_group, true);
                }
                if (stream_name.endsWith(".bgr") || stream_name.endsWith(".bar")) {
                    final List<GraphSym> graphs = GraphSymUtils.readGraphs(str, stream_name, seq_group, null);
                    GraphSymUtils.processGraphSyms(graphs, stream_name, null);
                    return graphs;
                }
                System.out.println("Can't parse, format not recognized: " + stream_name);
            }
        }
        catch (Exception ex) {
            System.err.println("Error loading file: " + stream_name);
            ex.printStackTrace();
        }
        finally {
            GeneralUtils.safeClose(str);
        }
        return results;
    }
    
    static List<? extends SeqSymmetry> parseIndexed(final InputStream str, final List<AnnotsXmlParser.AnnotMapElt> annotList, final String stream_name, final AnnotatedSeqGroup seq_group, final String type_prefix) {
        try {
            final IndexWriter iWriter = getIndexWriter(stream_name);
            final DataInputStream dis = new DataInputStream(str);
            final String extension = getExtension(stream_name);
            final String annot_type = getAnnotType(annotList, stream_name, extension, type_prefix);
            System.out.println("Indexing " + stream_name);
            if (extension.equals(".link.psl")) {
                try {
                    return ((PSLParser)iWriter).parse(dis, annot_type, null, seq_group, null, false, true, false);
                }
                catch (IOException ex) {
                    Logger.getLogger(ParserController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            return iWriter.parse(dis, annot_type, seq_group);
        }
        finally {
            GeneralUtils.safeClose(str);
        }
    }
    
    public static String getExtension(final String stream_name) {
        if (stream_name.endsWith(".link.psl")) {
            return stream_name.substring(stream_name.lastIndexOf(".link.psl"), stream_name.length());
        }
        if (stream_name.lastIndexOf(".") >= 0) {
            return stream_name.substring(stream_name.lastIndexOf("."), stream_name.length());
        }
        return "";
    }
    
    public static IndexWriter getIndexWriter(final String stream_name) {
        return FileTypeHolder.getInstance().getFileTypeHandlerForURI(stream_name).getIndexWriter(stream_name);
    }
    
    public static String getAnnotType(final List<AnnotsXmlParser.AnnotMapElt> annotsList, final String stream_name, final String extension, final String type_name) {
        if (stream_name.endsWith(".cyt")) {
            return "__cytobands";
        }
        if (annotsList != null) {
            System.out.println("\tChecking annots mapping!");
            final AnnotsXmlParser.AnnotMapElt annotMapElt = AnnotsXmlParser.AnnotMapElt.findFileNameElt(stream_name, annotsList);
            if (annotMapElt != null) {
                System.out.println("\t\tTitle " + annotMapElt.title);
                return annotMapElt.title;
            }
        }
        if (type_name != null) {
            return type_name;
        }
        if (extension == null) {
            return stream_name;
        }
        return stream_name.substring(0, stream_name.lastIndexOf(extension));
    }
}
