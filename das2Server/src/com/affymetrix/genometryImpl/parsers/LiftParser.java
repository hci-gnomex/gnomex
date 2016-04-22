// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.parsers;

import java.util.Iterator;
import com.affymetrix.genometryImpl.util.SeqUtils;
import java.io.EOFException;
import com.affymetrix.genometryImpl.SeqSpan;
import com.affymetrix.genometryImpl.span.SimpleSeqSpan;
import com.affymetrix.genometryImpl.symmetry.SeqSymmetry;
import com.affymetrix.genometryImpl.symmetry.SimpleSymWithProps;
import com.affymetrix.genometryImpl.symmetry.MutableSeqSymmetry;
import com.affymetrix.genometryImpl.BioSeq;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import com.affymetrix.genometryImpl.util.Timer;
import java.io.IOException;
import com.affymetrix.genometryImpl.util.GeneralUtils;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.affymetrix.genometryImpl.AnnotatedSeqGroup;
import com.affymetrix.genometryImpl.GenometryModel;
import java.util.regex.Pattern;

public final class LiftParser
{
    private static final Pattern re_tab;
    private static final Pattern re_name;
    private static final int CHROM_START = 0;
    private static final int COMBO_NAME = 1;
    private static final int MATCH_LENGTH = 2;
    private static final int CHROM_NAME = 3;
    private static final int CHROM_LENGTH = 4;
    private static final int CONTIG_NAME_SUBFIELD = 1;
    private static final boolean SET_COMPOSITION = true;
    
    public static AnnotatedSeqGroup loadChroms(final String file_name, final GenometryModel gmodel, final String genome_version) throws IOException {
        Logger.getLogger(LiftParser.class.getName()).log(Level.FINE, "trying to load lift file: {0}", file_name);
        FileInputStream fistr = null;
        AnnotatedSeqGroup result = null;
        try {
            final File fil = new File(file_name);
            fistr = new FileInputStream(fil);
            result = parse(fistr, gmodel, genome_version);
        }
        finally {
            GeneralUtils.safeClose(fistr);
        }
        return result;
    }
    
    public static AnnotatedSeqGroup parse(final InputStream istr, final GenometryModel gmodel, final String genome_version) throws IOException {
        return parse(istr, gmodel, genome_version, true);
    }
    
    public static AnnotatedSeqGroup parse(final InputStream istr, final GenometryModel gmodel, final String genome_version, final boolean annotate_seq) throws IOException {
        Logger.getLogger(LiftParser.class.getName()).log(Level.FINE, "parsing in lift file");
        final Timer tim = new Timer();
        tim.start();
        int contig_count = 0;
        int chrom_count = 0;
        final AnnotatedSeqGroup seq_group = gmodel.addSeqGroup(genome_version);
        final BufferedReader br = new BufferedReader(new InputStreamReader(istr));
        try {
            final Thread thread = Thread.currentThread();
            String line;
            while ((line = br.readLine()) != null && !thread.isInterrupted()) {
                if (line.length() != 0 && !line.equals("")) {
                    if (line.startsWith("#")) {
                        continue;
                    }
                    final String[] fields = LiftParser.re_tab.split(line);
                    final int chrom_start = Integer.parseInt(fields[0]);
                    final int match_length = Integer.parseInt(fields[2]);
                    final String chrom_name = fields[3];
                    final int chrom_length = Integer.parseInt(fields[4]);
                    final String tempname = fields[1];
                    final String[] splitname = LiftParser.re_name.split(tempname);
                    final String contig_name = splitname[1];
                    BioSeq contig = seq_group.getSeq(contig_name);
                    if (contig == null) {
                        contig = new BioSeq(contig_name, "", match_length);
                    }
                    ++contig_count;
                    BioSeq chrom = seq_group.getSeq(chrom_name);
                    if (chrom == null) {
                        ++chrom_count;
                        chrom = seq_group.addSeq(chrom_name, chrom_length);
                        chrom.setVersion(genome_version);
                    }
                    MutableSeqSymmetry comp = (MutableSeqSymmetry)chrom.getComposition();
                    if (comp == null) {
                        comp = new SimpleSymWithProps();
                        ((SimpleSymWithProps)comp).setProperty("method", "contigs");
                        chrom.setComposition(comp);
                        if (annotate_seq) {
                            chrom.addAnnotation(comp);
                        }
                    }
                    final SimpleSymWithProps csym = new SimpleSymWithProps();
                    csym.addSpan(new SimpleSeqSpan(chrom_start, chrom_start + match_length, chrom));
                    csym.addSpan(new SimpleSeqSpan(0, match_length, contig));
                    csym.setProperty("method", "contig");
                    csym.setProperty("id", contig.getID());
                    comp.addChild(csym);
                }
            }
        }
        catch (EOFException ex) {
            Logger.getLogger(LiftParser.class.getName()).log(Level.FINE, "reached end of lift file");
        }
        for (final BioSeq chrom2 : seq_group.getSeqList()) {
            final MutableSeqSymmetry comp2 = (MutableSeqSymmetry)chrom2.getComposition();
            if (comp2 != null) {
                final SeqSpan chromspan = SeqUtils.getChildBounds(comp2, chrom2);
                comp2.addSpan(chromspan);
            }
        }
        Logger.getLogger(LiftParser.class.getName()).log(Level.INFO, "chroms loaded, load time = {0}", tim.read() / 1000.0f);
        Logger.getLogger(LiftParser.class.getName()).log(Level.INFO, "contig count: {0}", contig_count);
        Logger.getLogger(LiftParser.class.getName()).log(Level.INFO, "chrom count: {0}", chrom_count);
        return seq_group;
    }
    
    static {
        re_tab = Pattern.compile("\t");
        re_name = Pattern.compile("/");
    }
}
