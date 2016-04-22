//
// Decompiled by Procyon v0.5.30
//

package com.affymetrix.genometryImpl.parsers;

import com.affymetrix.genometryImpl.comparator.SeqSymMinComparator;
import java.util.Comparator;
import java.util.Collection;
import com.affymetrix.genometryImpl.util.GeneralUtils;
import java.io.OutputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.DataOutputStream;
import java.util.Iterator;
import com.affymetrix.genometryImpl.BioSeq;
import java.util.Map;
import java.io.EOFException;
import com.affymetrix.genometryImpl.SeqSpan;
import com.affymetrix.genometryImpl.span.SimpleSeqSpan;
import com.affymetrix.genometryImpl.symmetry.SimpleSymWithProps;
import com.affymetrix.genometryImpl.symmetry.UcscGeneSym;
import java.io.DataInputStream;
import java.io.BufferedInputStream;
import java.util.HashMap;
import java.util.ArrayList;
import java.io.IOException;
import com.affymetrix.genometryImpl.symmetry.SeqSymmetry;
import com.affymetrix.genometryImpl.AnnotatedSeqGroup;
import java.io.InputStream;
import java.util.regex.Pattern;
import java.util.List;

public final class BrsParser implements AnnotationWriter, IndexWriter, Parser
{
    private static final List<String> pref_list;
    private static final boolean DEBUG = false;
    private static final Pattern line_regex;
    private static final Pattern emin_regex;
    private static final Pattern emax_regex;

    public static List<SeqSymmetry> parse(final InputStream istr, final String annot_type, final AnnotatedSeqGroup seq_group) throws IOException {
        return parse(istr, annot_type, seq_group, true);
    }

    public static List<SeqSymmetry> parse(final InputStream istr, final String annot_type, final AnnotatedSeqGroup seq_group, final boolean annotate_seq) throws IOException {
        final List<SeqSymmetry> annots = new ArrayList<SeqSymmetry>();
        final List<SeqSymmetry> results = new ArrayList<SeqSymmetry>(15000);
        final Map<String, SeqSymmetry> chrom2sym = new HashMap<String, SeqSymmetry>();
        int total_exon_count = 0;
        int count = 0;
        final BufferedInputStream bis = new BufferedInputStream(istr);
        DataInputStream dis = null;
        try {
            dis = new DataInputStream(bis);
            final Thread thread = Thread.currentThread();
            while (!thread.isInterrupted()) {
                final String geneName = dis.readUTF();
                String name = dis.readUTF();
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
                    chromseq = seq_group.addSeq(chrom_name, tmax, annot_type);
                }
                if (name.length() == 0 && geneName.length() == 0) {
                    name = seq_group.getID();
                }
                final UcscGeneSym sym = new UcscGeneSym(annot_type, geneName, name, chromseq, forward, tmin, tmax, cmin, cmax, emins, emaxs);
                if (geneName.length() != 0) {
                    seq_group.addToIndex(geneName, sym);
                }
                if (name.length() != 0) {
                    seq_group.addToIndex(name, sym);
                }
                results.add(sym);
                if (chromseq.getLength() < tmax) {
                    chromseq.setLength(tmax);
                }
                if (annotate_seq) {
                    SimpleSymWithProps parent_sym = (SimpleSymWithProps) chrom2sym.get(chrom_name);
                    if (parent_sym == null) {
                        parent_sym = new SimpleSymWithProps();
                        parent_sym.addSpan(new SimpleSeqSpan(0, chromseq.getLength(), chromseq));
                        parent_sym.setProperty("method", annot_type);
                        parent_sym.setProperty("preferred_formats", BrsParser.pref_list);
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
        catch (EOFException ex) {}
        if (annotate_seq) {
            for (final SeqSymmetry annot : annots) {
                final BioSeq chromseq2 = annot.getSpan(0).getBioSeq();
                chromseq2.addAnnotation(annot);
            }
        }
        return results;
    }

    private void outputBrsFormat(final UcscGeneSym gsym, final DataOutputStream dos) throws IOException {
        final SeqSpan tspan = gsym.getSpan(0);
        final SeqSpan cspan = gsym.getCdsSpan();
        final BioSeq seq = tspan.getBioSeq();
        dos.writeUTF(gsym.getGeneName());
        dos.writeUTF(gsym.getName());
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

    private void convertTextToBinary(final String file_name, final String bin_file) {
        Logger.getLogger(BrsParser.class.getName()).log(Level.FINE, "loading file: {0}", file_name);
        int count = 0;
        long flength = 0L;
        int max_tlength = Integer.MIN_VALUE;
        int max_exons = Integer.MIN_VALUE;
        int total_exon_count = 0;
        final int big_spliced = 0;
        DataOutputStream dos = null;
        FileInputStream fis = null;
        BufferedReader br = null;
        BufferedInputStream bis = null;
        try {
            final File fil = new File(file_name);
            flength = fil.length();
            fis = new FileInputStream(fil);
            bis = new BufferedInputStream(fis);
            final byte[] bytebuf = new byte[(int)flength];
            bis.read(bytebuf);
            final ByteArrayInputStream bytestream = new ByteArrayInputStream(bytebuf);
            br = new BufferedReader(new InputStreamReader(bytestream));
            final File outfile = new File(bin_file);
            final FileOutputStream fos = new FileOutputStream(outfile);
            final BufferedOutputStream bos = new BufferedOutputStream(fos);
            dos = new DataOutputStream(bos);
            String line;
            while ((line = br.readLine()) != null) {
                ++count;
                int field_index = 0;
                final String[] fields = BrsParser.line_regex.split(line);
                final boolean text_includes_genename = fields.length > 10;
                String geneName = null;
                if (text_includes_genename) {
                    geneName = fields[field_index++];
                }
                final String name = fields[field_index++];
                final String chrom = fields[field_index++];
                final String strand = fields[field_index++];
                final String txStart = fields[field_index++];
                final String txEnd = fields[field_index++];
                final String cdsStart = fields[field_index++];
                final String cdsEnd = fields[field_index++];
                final String exonCount = fields[field_index++];
                final String exonStarts = fields[field_index++];
                final String exonEnds = fields[field_index++];
                final int tmin = Integer.parseInt(txStart);
                final int tmax = Integer.parseInt(txEnd);
                final int tlength = tmax - tmin;
                final int cmin = Integer.parseInt(cdsStart);
                final int cmax = Integer.parseInt(cdsEnd);
                final int ecount = Integer.parseInt(exonCount);
                final String[] emins = BrsParser.emin_regex.split(exonStarts);
                final String[] emaxs = BrsParser.emax_regex.split(exonEnds);
                if (!text_includes_genename) {
                    geneName = name;
                }
                dos.writeUTF(geneName);
                dos.writeUTF(name);
                dos.writeUTF(chrom);
                dos.writeUTF(strand);
                dos.writeInt(tmin);
                dos.writeInt(tmax);
                dos.writeInt(cmin);
                dos.writeInt(cmax);
                dos.writeInt(ecount);
                if (ecount != emins.length || ecount != emaxs.length) {
                    System.out.println("EXON COUNTS DON'T MATCH UP FOR " + geneName + " !!!");
                }
                else {
                    for (int i = 0; i < ecount; ++i) {
                        final int emin = Integer.parseInt(emins[i]);
                        dos.writeInt(emin);
                    }
                    for (int i = 0; i < ecount; ++i) {
                        final int emax = Integer.parseInt(emaxs[i]);
                        dos.writeInt(emax);
                    }
                }
                if (tlength >= 500000) {}
                total_exon_count += ecount;
                max_exons = Math.max(max_exons, ecount);
                max_tlength = Math.max(max_tlength, tlength);
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        finally {
            GeneralUtils.safeClose(br);
            GeneralUtils.safeClose(bis);
            GeneralUtils.safeClose(fis);
            GeneralUtils.safeClose(dos);
        }
        System.out.println("line count = " + count);
        System.out.println("file length = " + flength);
        System.out.println("max genomic transcript length: " + max_tlength);
        System.out.println("max exons in single transcript: " + max_exons);
        System.out.println("total exons: " + total_exon_count);
        System.out.println("spliced transcripts > 65000: " + big_spliced);
    }

    public static void main(final String[] args) {
        String text_file = null;
        String bin_file = null;
        if (args.length == 2) {
            text_file = args[0];
            bin_file = args[1];
        }
        else {
            System.out.println("Usage:  java ... BrsParser <text infile> <binary outfile>");
            System.exit(1);
        }
        final BrsParser test = new BrsParser();
        test.convertTextToBinary(text_file, bin_file);
        System.exit(0);
    }

    @Override
    public boolean writeAnnotations(final Collection<? extends SeqSymmetry> syms, final BioSeq seq, final String type, final OutputStream outstream) {
        System.out.println("in BrsParser.writeAnnotations()");
        boolean success = true;
        try {
            final DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(outstream));
            for (final SeqSymmetry sym : syms) {
                if (!(sym instanceof UcscGeneSym)) {
                    System.err.println("trying to output non-UcscGeneSym as UcscGeneSym!");
                }
                this.outputBrsFormat((UcscGeneSym)sym, dos);
            }
            dos.flush();
        }
        catch (Exception ex) {
            ex.printStackTrace();
            success = false;
        }
        return success;
    }

    @Override
    public void writeSymmetry(final SeqSymmetry sym, final BioSeq seq, final OutputStream os) throws IOException {
        DataOutputStream dos = null;
        if (os instanceof DataOutputStream) {
            dos = (DataOutputStream)os;
        }
        else {
            dos = new DataOutputStream(os);
        }
        this.outputBrsFormat((UcscGeneSym)sym, dos);
    }

    @Override
    public List<SeqSymmetry> parse(final DataInputStream dis, final String annot_type, final AnnotatedSeqGroup group) {
        try {
            return parse(dis, annot_type, group, false);
        }
        catch (IOException ex) {
            Logger.getLogger(BrsParser.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
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
        return BrsParser.pref_list;
    }

    @Override
    public String getMimeType() {
        return "binary/brs";
    }

    @Override
    public List<? extends SeqSymmetry> parse(final InputStream is, final AnnotatedSeqGroup group, final String nameType, final String uri, final boolean annotate_seq) throws Exception {
        return parse(is, uri, group, annotate_seq);
    }

    static {
        (pref_list = new ArrayList<String>()).add("brs");
        line_regex = Pattern.compile("\t");
        emin_regex = Pattern.compile(",");
        emax_regex = Pattern.compile(",");
    }
}
