//
// Decompiled by Procyon v0.5.30
//

package com.affymetrix.genometryImpl.parsers;

import com.affymetrix.genometryImpl.comparator.SeqSymMinComparator;
import java.util.Comparator;
import java.util.Collection;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.File;
import java.io.DataOutputStream;
import com.affymetrix.genometryImpl.SupportsCdsSpan;
import java.io.OutputStream;
import java.util.Iterator;
import com.affymetrix.genometryImpl.BioSeq;
import java.util.Map;
import com.affymetrix.genometryImpl.util.GeneralUtils;
import java.io.EOFException;
import com.affymetrix.genometryImpl.SeqSpan;
import com.affymetrix.genometryImpl.span.SimpleSeqSpan;
import com.affymetrix.genometryImpl.symmetry.SimpleSymWithProps;
import com.affymetrix.genometryImpl.symmetry.UcscGeneSym;
import java.io.BufferedInputStream;
import java.util.HashMap;
import java.util.ArrayList;
import com.affymetrix.genometryImpl.util.Timer;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.InputStream;
import com.affymetrix.genometryImpl.symmetry.SeqSymmetry;
import com.affymetrix.genometryImpl.AnnotatedSeqGroup;
import java.io.DataInputStream;
import java.util.regex.Pattern;
import java.util.List;

public final class BgnParser implements AnnotationWriter, IndexWriter, Parser
{
    private static final boolean DEBUG = false;
    private static List<String> pref_list;
    private static final Pattern line_regex;
    private static final Pattern emin_regex;
    private static final Pattern emax_regex;

    @Override
    public List<SeqSymmetry> parse(final DataInputStream dis, final String annot_type, final AnnotatedSeqGroup group) {
        try {
            return this.parse(dis, annot_type, group, false);
        }
        catch (IOException ex) {
            Logger.getLogger(BgnParser.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public List<SeqSymmetry> parse(final InputStream istr, final String annot_type, final AnnotatedSeqGroup seq_group, final boolean annotate_seq) throws IOException {
        if (seq_group == null) {
            throw new IllegalArgumentException("BgnParser called with seq_group null.");
        }
        final Timer tim = new Timer();
        tim.start();
        final List<SeqSymmetry> annots = new ArrayList<SeqSymmetry>();
        final List<SeqSymmetry> results = new ArrayList<SeqSymmetry>(15000);
        final Map<String, SeqSymmetry> chrom2sym = new HashMap<String, SeqSymmetry>();
        int total_exon_count = 0;
        int count = 0;
        BufferedInputStream bis = null;
        DataInputStream dis = null;
        boolean reached_EOF = false;
        try {
            bis = new BufferedInputStream(istr);
            dis = new DataInputStream(bis);
            final Thread thread = Thread.currentThread();
            while (!thread.isInterrupted()) {
                final String name = dis.readUTF();
                final String chrom_name = dis.readUTF();
                final String strand = dis.readUTF();
                final boolean forward = strand.equals("+") || strand.equals("++");
                final int tmin = dis.readInt();
                final int tmax = dis.readInt();
                final int cmin = dis.readInt();
                final int cmax = dis.readInt();
                final int ecount = dis.readInt();
                final int[] emins = new int[ecount];
                final int[] emaxs = new int[ecount];
                for (int i = 0; i < ecount; ++i) {
                    emins[i] = dis.readInt();
                }
                for (int i = 0; i < ecount; ++i) {
                    emaxs[i] = dis.readInt();
                }
                BioSeq chromseq = seq_group.getSeq(chrom_name);
                if (chromseq == null) {
                    chromseq = seq_group.addSeq(chrom_name, 0);
                }
                final UcscGeneSym sym = new UcscGeneSym(annot_type, name, name, chromseq, forward, tmin, tmax, cmin, cmax, emins, emaxs);
                if (seq_group != null) {
                    seq_group.addToIndex(name, sym);
                }
                results.add(sym);
                if (tmax > chromseq.getLength()) {
                    chromseq.setLength(tmax);
                }
                if (annotate_seq) {
                    SimpleSymWithProps parent_sym = (SimpleSymWithProps) chrom2sym.get(chrom_name);
                    if (parent_sym == null) {
                        parent_sym = new SimpleSymWithProps();
                        parent_sym.addSpan(new SimpleSeqSpan(0, chromseq.getLength(), chromseq));
                        parent_sym.setProperty("method", annot_type);
                        parent_sym.setProperty("preferred_formats", BgnParser.pref_list);
                        parent_sym.setProperty("container sym", Boolean.TRUE);
                        annots.add(parent_sym);
                        chrom2sym.put(chrom_name, parent_sym);
                    }
                    parent_sym.addChild(sym);
                }
                total_exon_count += ecount;
                ++count;
            }
        }
        catch (EOFException ex2) {
            reached_EOF = true;
        }
        catch (IOException ioe) {
            throw ioe;
        }
        catch (Exception ex) {
            String message = "Problem processing BGN file";
            final String m1 = ex.getMessage();
            if (m1 != null && m1.length() > 0) {
                message = message + ": " + m1;
            }
            final IOException ioe2 = new IOException(message);
            ioe2.initCause(ex);
            throw ioe2;
        }
        finally {
            GeneralUtils.safeClose(bis);
            GeneralUtils.safeClose(dis);
        }
        if (annotate_seq) {
            for (final SeqSymmetry annot : annots) {
                final BioSeq chromseq2 = annot.getSpan(0).getBioSeq();
                chromseq2.addAnnotation(annot);
            }
        }
        if (!reached_EOF) {
            System.out.println("File loading was terminated early.");
        }
        return results;
    }

    @Override
    public void writeSymmetry(final SeqSymmetry gsym, final BioSeq targetSeq, final OutputStream os) throws IOException {
        final SeqSpan tspan = gsym.getSpan(0);
        SeqSpan cspan;
        String name;
        if (gsym instanceof UcscGeneSym) {
            final UcscGeneSym ugs = (UcscGeneSym)gsym;
            cspan = ugs.getCdsSpan();
            name = ugs.getName();
        }
        else if (gsym instanceof SupportsCdsSpan) {
            cspan = ((SupportsCdsSpan)gsym).getCdsSpan();
            name = gsym.getID();
        }
        else {
            cspan = tspan;
            name = gsym.getID();
        }
        final BioSeq seq = tspan.getBioSeq();
        DataOutputStream dos = null;
        if (os instanceof DataOutputStream) {
            dos = (DataOutputStream)os;
        }
        else {
            dos = new DataOutputStream(os);
        }
        dos.writeUTF(name);
        dos.writeUTF(seq.getID());
        if (tspan.isForward()) {
            dos.writeUTF("+");
        }
        else {
            dos.writeUTF("-");
        }
        dos.writeInt(tspan.getMin());
        dos.writeInt(tspan.getMax());
        dos.writeInt(cspan.getMin());
        dos.writeInt(cspan.getMax());
        dos.writeInt(gsym.getChildCount());
        final int childcount = gsym.getChildCount();
        for (int k = 0; k < childcount; ++k) {
            final SeqSpan child = gsym.getChild(k).getSpan(seq);
            dos.writeInt(child.getMin());
        }
        for (int k = 0; k < childcount; ++k) {
            final SeqSpan child = gsym.getChild(k).getSpan(seq);
            dos.writeInt(child.getMax());
        }
    }

    public void writeBinary(final String file_name, final List<SeqSymmetry> annots) throws IOException {
        DataOutputStream dos = null;
        try {
            dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(new File(file_name))));
            for (final SeqSymmetry gsym : annots) {
                this.writeSymmetry(gsym, null, dos);
            }
        }
        finally {
            GeneralUtils.safeClose(dos);
        }
    }

    public void convertTextToBinary(final String text_file, final String bin_file, final AnnotatedSeqGroup seq_group) {
        System.out.println("loading file: " + text_file);
        final int count = 0;
        long flength = 0L;
        final int max_tlength = Integer.MIN_VALUE;
        final int max_exons = Integer.MIN_VALUE;
        final int max_spliced_length = Integer.MIN_VALUE;
        final int total_exon_count = 0;
        final int biguns = 0;
        final int big_spliced = 0;
        final Timer tim = new Timer();
        tim.start();
        FileInputStream fis = null;
        DataOutputStream dos = null;
        BufferedOutputStream bos = null;
        BufferedReader br = null;
        try {
            final File fil = new File(text_file);
            flength = fil.length();
            fis = new FileInputStream(fil);
            br = new BufferedReader(new InputStreamReader(fis));
            final File outfile = new File(bin_file);
            final FileOutputStream fos = new FileOutputStream(outfile);
            bos = new BufferedOutputStream(fos);
            dos = new DataOutputStream(bos);
            this.writeLines(br, count, seq_group, dos, biguns, total_exon_count, max_exons, max_tlength, tim, flength, max_spliced_length, big_spliced);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        finally {
            GeneralUtils.safeClose(fis);
            GeneralUtils.safeClose(br);
            GeneralUtils.safeClose(dos);
            GeneralUtils.safeClose(bos);
        }
    }

    private void writeLines(final BufferedReader br, int count, final AnnotatedSeqGroup seq_group, final DataOutputStream dos, int biguns, int total_exon_count, int max_exons, int max_tlength, final Timer tim, final long flength, final int max_spliced_length, final int big_spliced) throws NumberFormatException, IOException {
        String line = null;
        while ((line = br.readLine()) != null) {
            ++count;
            final String[] fields = BgnParser.line_regex.split(line);
            final String name = fields[0];
            final String chrom = fields[1];
            if (seq_group != null && seq_group.getSeq(chrom) == null) {
                System.out.println("sequence not recognized, ignoring: " + chrom);
            }
            else {
                final String strand = fields[2];
                final String txStart = fields[3];
                final String txEnd = fields[4];
                final String cdsStart = fields[5];
                final String cdsEnd = fields[6];
                final String exonCount = fields[7];
                final String exonStarts = fields[8];
                final String exonEnds = fields[9];
                final int tmin = Integer.parseInt(txStart);
                final int tmax = Integer.parseInt(txEnd);
                final int tlength = tmax - tmin;
                final int cmin = Integer.parseInt(cdsStart);
                final int cmax = Integer.parseInt(cdsEnd);
                final int ecount = Integer.parseInt(exonCount);
                final String[] emins = BgnParser.emin_regex.split(exonStarts);
                final String[] emaxs = BgnParser.emax_regex.split(exonEnds);
                dos.writeUTF(name);
                dos.writeUTF(chrom);
                dos.writeUTF(strand);
                dos.writeInt(tmin);
                dos.writeInt(tmax);
                dos.writeInt(cmin);
                dos.writeInt(cmax);
                dos.writeInt(ecount);
                if (ecount != emins.length || ecount != emaxs.length) {
                    System.out.println("EXON COUNTS DON'T MATCH UP FOR " + name + " !!!");
                }
                else {
                    for (int i = 0; i < ecount; ++i) {
                        Integer.parseInt(emins[i]);
                    }
                    for (int i = 0; i < ecount; ++i) {
                        final int emax = Integer.parseInt(emaxs[i]);
                        dos.writeInt(emax);
                    }
                }
                if (tlength >= 500000) {
                    ++biguns;
                }
                total_exon_count += ecount;
                max_exons = Math.max(max_exons, ecount);
                max_tlength = Math.max(max_tlength, tlength);
            }
        }
    }

    @Override
    public boolean writeAnnotations(final Collection<? extends SeqSymmetry> syms, final BioSeq seq, final String type, final OutputStream outstream) {
        System.out.println("in BgnParser.writeAnnotations()");
        try {
            final DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(outstream));
            for (final SeqSymmetry sym : syms) {
                if (!(sym instanceof UcscGeneSym)) {
                    System.err.println("trying to output non-UcscGeneSym as UcscGeneSym!");
                }
                this.writeSymmetry(sym, null, dos);
            }
            dos.flush();
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public Comparator<SeqSymmetry> getComparator(final BioSeq seq) {
        return new SeqSymMinComparator(seq);
    }

    @Override
    public int getMin(final SeqSymmetry sym, final BioSeq seq) {
        final SeqSpan span = sym.getSpan(seq);
        return span.getMin();
    }

    @Override
    public int getMax(final SeqSymmetry sym, final BioSeq seq) {
        final SeqSpan span = sym.getSpan(seq);
        return span.getMax();
    }

    @Override
    public List<String> getFormatPrefList() {
        return BgnParser.pref_list;
    }

    @Override
    public String getMimeType() {
        return "binary/bgn";
    }

    @Override
    public List<? extends SeqSymmetry> parse(final InputStream is, final AnnotatedSeqGroup group, final String nameType, final String uri, final boolean annotate_seq) throws Exception {
        return this.parse(is, uri, group, annotate_seq);
    }

    static {
        (BgnParser.pref_list = new ArrayList<String>()).add("bgn");
        line_regex = Pattern.compile("\t");
        emin_regex = Pattern.compile(",");
        emax_regex = Pattern.compile(",");
    }
}
