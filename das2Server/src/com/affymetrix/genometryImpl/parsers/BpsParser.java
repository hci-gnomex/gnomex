//
// Decompiled by Procyon v0.5.30
//

package com.affymetrix.genometryImpl.parsers;

import java.io.FileOutputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.File;
import com.affymetrix.genometryImpl.util.Timer;
import java.util.Iterator;
import com.affymetrix.genometryImpl.util.SeqUtils;
import com.affymetrix.genometryImpl.symmetry.SeqSymmetryConverter;
import java.io.DataOutputStream;
import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.io.IOException;
import com.affymetrix.genometryImpl.BioSeq;
import java.util.Map;
import java.util.Comparator;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.affymetrix.genometryImpl.util.GeneralUtils;
import java.io.EOFException;
import com.affymetrix.genometryImpl.SeqSpan;
import com.affymetrix.genometryImpl.span.SimpleSeqSpan;
import com.affymetrix.genometryImpl.symmetry.SimpleSymWithProps;
import java.util.ArrayList;
import com.affymetrix.genometryImpl.symmetry.SeqSymmetry;
import java.util.HashMap;
import com.affymetrix.genometryImpl.symmetry.UcscPslSym;
import com.affymetrix.genometryImpl.AnnotatedSeqGroup;
import java.io.DataInputStream;
import java.util.List;
import com.affymetrix.genometryImpl.comparator.UcscPslComparator;

public final class BpsParser implements AnnotationWriter, IndexWriter, Parser
{
    private static final UcscPslComparator comp;
    private static final List<String> pref_list;
    private static final int estimated_count = 80000;

    public static List<UcscPslSym> parse(final DataInputStream dis, final String annot_type, AnnotatedSeqGroup query_group, AnnotatedSeqGroup target_group, final boolean annot_query, final boolean annot_target) throws IOException {
        if (query_group == null) {
            query_group = new AnnotatedSeqGroup("Query");
            query_group.setUseSynonyms(false);
        }
        if (target_group == null) {
            target_group = new AnnotatedSeqGroup("Target");
            target_group.setUseSynonyms(false);
        }
        final Map<String, SeqSymmetry> target2sym = new HashMap<String, SeqSymmetry>();
        final Map<String, SeqSymmetry> query2sym = new HashMap<String, SeqSymmetry>();
        final List<UcscPslSym> results = new ArrayList<UcscPslSym>(80000);
        int count = 0;
        try {
            final Thread thread = Thread.currentThread();
            while (!thread.isInterrupted()) {
                final int matches = dis.readInt();
                final int mismatches = dis.readInt();
                final int repmatches = dis.readInt();
                final int ncount = dis.readInt();
                final int qNumInsert = dis.readInt();
                final int qBaseInsert = dis.readInt();
                final int tNumInsert = dis.readInt();
                final int tBaseInsert = dis.readInt();
                final boolean qforward = dis.readBoolean();
                final String qname = dis.readUTF();
                final int qsize = dis.readInt();
                final int qmin = dis.readInt();
                final int qmax = dis.readInt();
                BioSeq queryseq = query_group.getSeq(qname);
                if (queryseq == null) {
                    queryseq = query_group.addSeq(qname, qsize);
                }
                if (queryseq.getLength() < qsize) {
                    queryseq.setLength(qsize);
                }
                final String tname = dis.readUTF();
                final int tsize = dis.readInt();
                final int tmin = dis.readInt();
                final int tmax = dis.readInt();
                BioSeq targetseq = target_group.getSeq(tname);
                if (targetseq == null) {
                    targetseq = target_group.addSeq(tname, tsize);
                }
                if (targetseq.getLength() < tsize) {
                    targetseq.setLength(tsize);
                }
                final int blockcount = dis.readInt();
                final int[] blockSizes = new int[blockcount];
                final int[] qmins = new int[blockcount];
                final int[] tmins = new int[blockcount];
                for (int i = 0; i < blockcount; ++i) {
                    blockSizes[i] = dis.readInt();
                }
                for (int i = 0; i < blockcount; ++i) {
                    qmins[i] = dis.readInt();
                }
                for (int i = 0; i < blockcount; ++i) {
                    tmins[i] = dis.readInt();
                }
                ++count;
                final UcscPslSym sym = new UcscPslSym(annot_type, matches, mismatches, repmatches, ncount, qNumInsert, qBaseInsert, tNumInsert, tBaseInsert, qforward, queryseq, qmin, qmax, targetseq, tmin, tmax, blockcount, blockSizes, qmins, tmins);
                results.add(sym);
                if (annot_query) {
                    SimpleSymWithProps query_parent_sym = (SimpleSymWithProps) query2sym.get(qname);
                    if (query_parent_sym == null) {
                        query_parent_sym = new SimpleSymWithProps();
                        query_parent_sym.addSpan(new SimpleSeqSpan(0, queryseq.getLength(), queryseq));
                        query_parent_sym.setProperty("method", annot_type);
                        query_parent_sym.setProperty("preferred_formats", BpsParser.pref_list);
                        query_parent_sym.setProperty("container sym", Boolean.TRUE);
                        queryseq.addAnnotation(query_parent_sym);
                        query2sym.put(qname, query_parent_sym);
                    }
                    query_group.addToIndex(sym.getID(), sym);
                    query_parent_sym.addChild(sym);
                }
                if (annot_target) {
                    SimpleSymWithProps target_parent_sym = (SimpleSymWithProps) target2sym.get(tname);
                    if (target_parent_sym == null) {
                        target_parent_sym = new SimpleSymWithProps();
                        target_parent_sym.addSpan(new SimpleSeqSpan(0, targetseq.getLength(), targetseq));
                        target_parent_sym.setProperty("method", annot_type);
                        target_parent_sym.setProperty("preferred_formats", BpsParser.pref_list);
                        target_parent_sym.setProperty("container sym", Boolean.TRUE);
                        targetseq.addAnnotation(target_parent_sym);
                        target2sym.put(tname, target_parent_sym);
                    }
                    target_parent_sym.addChild(sym);
                    target_group.addToIndex(sym.getID(), sym);
                }
            }
        }
        catch (EOFException ex) {}
        finally {
            GeneralUtils.safeClose(dis);
        }
        if (count == 0) {
            Logger.getLogger(BpsParser.class.getName()).log(Level.INFO, "BPS total counts == 0 ???");
        }
        else {
            Collections.sort(results, BpsParser.comp);
        }
        return results;
    }

    @Override
    public boolean writeAnnotations(final Collection<? extends SeqSymmetry> syms, final BioSeq seq, final String type, final OutputStream outstream) {
        DataOutputStream dos = null;
        try {
            dos = new DataOutputStream(new BufferedOutputStream(outstream));
            for (SeqSymmetry sym : syms) {
                if (!(sym instanceof UcscPslSym)) {
                    final int spancount = sym.getSpanCount();
                    if (spancount == 1) {
                        sym = SeqSymmetryConverter.convertToPslSym(sym, type, seq);
                    }
                    else {
                        final BioSeq seq2 = SeqUtils.getOtherSeq(sym, seq);
                        sym = SeqSymmetryConverter.convertToPslSym(sym, type, seq2, seq);
                    }
                }
                this.writeSymmetry(sym, seq, dos);
            }
            dos.flush();
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        finally {
            GeneralUtils.safeClose(dos);
        }
        return true;
    }

    @Override
    public Comparator<UcscPslSym> getComparator(final BioSeq seq) {
        return BpsParser.comp;
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
        ((UcscPslSym)sym).outputBpsFormat(dos);
    }

    @Override
    public int getMin(final SeqSymmetry sym, final BioSeq seq) {
        return ((UcscPslSym)sym).getTargetMin();
    }

    @Override
    public int getMax(final SeqSymmetry sym, final BioSeq seq) {
        return ((UcscPslSym)sym).getTargetMax();
    }

    @Override
    public List<String> getFormatPrefList() {
        return BpsParser.pref_list;
    }

    @Override
    public List<UcscPslSym> parse(final DataInputStream dis, final String annot_type, final AnnotatedSeqGroup group) {
        try {
            return parse(dis, annot_type, null, group, false, false);
        }
        catch (IOException ex) {
            Logger.getLogger(BpsParser.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public String getMimeType() {
        return "binary/bps";
    }

    public static void main(final String[] args) throws IOException {
        if (args.length == 2) {
            final String text_file = args[0];
            final String bin_file = args[1];
            convertPslToBps(text_file, bin_file);
        }
        else {
            System.out.println("Usage:  java ... BpsParser <text infile> <binary outfile>");
            System.exit(1);
        }
    }

    private static void convertPslToBps(final String psl_in, final String bps_out) {
        System.out.println("reading text psl file");
        final List<UcscPslSym> psl_syms = readPslFile(psl_in);
        System.out.println("done reading text psl file, annot count = " + psl_syms.size());
        System.out.println("writing binary psl file");
        writeBinary(bps_out, psl_syms);
        System.out.println("done writing binary psl file");
    }

    private static List<UcscPslSym> readPslFile(final String file_name) {
        final Timer tim = new Timer();
        tim.start();
        List<UcscPslSym> results = null;
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        try {
            final File fil = new File(file_name);
            final long flength = fil.length();
            fis = new FileInputStream(fil);
            InputStream istr = null;
            final byte[] bytebuf = new byte[(int)flength];
            bis = new BufferedInputStream(fis);
            bis.read(bytebuf);
            bis.close();
            final ByteArrayInputStream bytestream = (ByteArrayInputStream)(istr = new ByteArrayInputStream(bytebuf));
            final PSLParser parser = new PSLParser();
            results = parser.parse(istr, file_name, null, null, false, false);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        finally {
            GeneralUtils.safeClose(bis);
            GeneralUtils.safeClose(fis);
        }
        Logger.getLogger(BpsParser.class.getName()).log(Level.INFO, "finished reading PSL file, time to read = {0}", tim.read() / 1000.0f);
        return results;
    }

    private static void writeBinary(final String file_name, final List<UcscPslSym> syms) {
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        DataOutputStream dos = null;
        try {
            final File outfile = new File(file_name);
            fos = new FileOutputStream(outfile);
            bos = new BufferedOutputStream(fos);
            dos = new DataOutputStream(bos);
            for (final UcscPslSym psl : syms) {
                psl.outputBpsFormat(dos);
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        finally {
            GeneralUtils.safeClose(dos);
            GeneralUtils.safeClose(bos);
            GeneralUtils.safeClose(fos);
        }
    }

    @Override
    public List<? extends SeqSymmetry> parse(final InputStream is, final AnnotatedSeqGroup group, final String nameType, final String uri, final boolean annotate_seq) throws Exception {
        final DataInputStream dis = new DataInputStream(is);
        return parse(dis, uri, null, group, false, annotate_seq);
    }

    static {
        comp = new UcscPslComparator();
        (pref_list = new ArrayList<String>()).add("bps");
        BpsParser.pref_list.add("psl");
    }
}
