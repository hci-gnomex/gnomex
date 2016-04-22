// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.parsers;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.OutputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.Reader;
import java.io.InputStreamReader;
import com.affymetrix.genometryImpl.GenometryModel;
import java.io.FileInputStream;
import java.io.File;
import com.affymetrix.genometryImpl.util.GeneralUtils;
import java.io.DataInputStream;
import java.io.BufferedInputStream;
import com.affymetrix.genometryImpl.AnnotatedSeqGroup;
import java.io.InputStream;
import com.affymetrix.genometryImpl.symmetry.SymWithProps;
import com.affymetrix.genometryImpl.span.SimpleSeqSpan;
import com.affymetrix.genometryImpl.symmetry.SimpleSymWithProps;
import java.util.ArrayList;
import com.affymetrix.genometryImpl.symmetry.MutableSeqSymmetry;
import java.io.BufferedReader;
import java.io.IOException;
import com.affymetrix.genometryImpl.SeqSpan;
import com.affymetrix.genometryImpl.BioSeq;
import com.affymetrix.genometryImpl.symmetry.LeafSingletonSymmetry;
import java.io.DataOutputStream;
import com.affymetrix.genometryImpl.symmetry.SeqSymmetry;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public final class BrptParser implements Parser
{
    static Pattern line_regex;
    Map source_hash;
    Map type_hash;
    static boolean TEST_BINARY_PARSE;
    
    public BrptParser() {
        this.source_hash = new HashMap();
        this.type_hash = new HashMap();
    }
    
    private static void outputBrptFormat(final List<SeqSymmetry> parents, final String genome_version, final DataOutputStream dos) throws IOException {
        final int pcount = parents.size();
        dos.writeUTF(genome_version);
        dos.writeInt(pcount);
        for (int i = 0; i < pcount; ++i) {
            final SeqSymmetry parent = parents.get(i);
            final BioSeq seq = parent.getSpanSeq(0);
            final String seqid = seq.getID();
            final int rpt_count = parent.getChildCount();
            dos.writeUTF(seqid);
            dos.writeInt(rpt_count);
        }
        for (int i = 0; i < pcount; ++i) {
            final SeqSymmetry parent = parents.get(i);
            for (int rpt_count2 = parent.getChildCount(), k = 0; k < rpt_count2; ++k) {
                final LeafSingletonSymmetry rpt = (LeafSingletonSymmetry)parent.getChild(k);
                final SeqSpan span = rpt.getSpan(0);
                final int start = span.getStart();
                final int end = span.getEnd();
                dos.writeInt(start);
                dos.writeInt(end);
            }
        }
    }
    
    private static List<SeqSymmetry> readTextFormat(final BufferedReader br) throws IOException {
        final Map<String, MutableSeqSymmetry> id2psym = new HashMap<String, MutableSeqSymmetry>();
        final ArrayList<SeqSymmetry> parent_syms = new ArrayList<SeqSymmetry>();
        int repeat_count = 0;
        int pos_count = 0;
        int neg_count = 0;
        try {
            String line;
            while ((line = br.readLine()) != null) {
                final String[] fields = BrptParser.line_regex.split(line);
                final String seqid = fields[5].intern();
                BioSeq seq = null;
                MutableSeqSymmetry psym = id2psym.get(seqid);
                if (psym == null) {
                    psym = new SimpleSymWithProps();
                    seq = new BioSeq(seqid, seqid, 1000000000);
                    psym.addSpan(new SimpleSeqSpan(0, 1000000000, seq));
                    ((SymWithProps)psym).setProperty("container sym", Boolean.TRUE);
                    id2psym.put(seqid, psym);
                    parent_syms.add(psym);
                }
                else {
                    seq = psym.getSpanSeq(0);
                }
                final int min = Integer.parseInt(fields[6]);
                final int max = Integer.parseInt(fields[7]);
                final String strand = fields[9];
                int start;
                int end;
                if (strand.equals("-")) {
                    start = max;
                    end = min;
                    ++neg_count;
                }
                else {
                    start = min;
                    end = max;
                    ++pos_count;
                }
                final LeafSingletonSymmetry rpt_sym = new LeafSingletonSymmetry(start, end, seq);
                psym.addChild(rpt_sym);
                ++repeat_count;
            }
        }
        finally {}
        System.out.println("repeat count: " + repeat_count);
        System.out.println("repeats on + strand: " + pos_count);
        System.out.println("repeats on - strand: " + neg_count);
        return parent_syms;
    }
    
    public static List<SeqSymmetry> parse(final InputStream istr, final String annot_type, final AnnotatedSeqGroup seq_group, final boolean annot_seq) throws IOException {
        System.out.println("parsing brpt file");
        List<SeqSymmetry> rpt_syms = null;
        BufferedInputStream bis = null;
        DataInputStream dis = null;
        try {
            if (istr instanceof BufferedInputStream) {
                bis = (BufferedInputStream)istr;
            }
            else {
                bis = new BufferedInputStream(istr);
            }
            dis = new DataInputStream(bis);
            final String genome_version = dis.readUTF();
            final int seq_count = dis.readInt();
            final int[] rpt_counts = new int[seq_count];
            final String[] seqids = new String[seq_count];
            final BioSeq[] seqs = new BioSeq[seq_count];
            System.out.println("genome version: " + genome_version);
            System.out.println("seqs: " + seq_count);
            int total_rpt_count = 0;
            for (int i = 0; i < seq_count; ++i) {
                final String seqid = dis.readUTF();
                seqids[i] = seqid;
                BioSeq aseq = seq_group.getSeq(seqid);
                if (aseq == null) {
                    aseq = seq_group.addSeq(seqid, 0);
                }
                seqs[i] = aseq;
                rpt_counts[i] = dis.readInt();
                total_rpt_count += rpt_counts[i];
            }
            System.out.println("total rpts: " + total_rpt_count);
            rpt_syms = new ArrayList<SeqSymmetry>(total_rpt_count);
            for (int i = 0; i < seq_count; ++i) {
                final BioSeq aseq2 = seqs[i];
                final int rpt_count = rpt_counts[i];
                System.out.println("seqid: " + seqids[i] + ", rpts: " + rpt_counts[i]);
                final SimpleSymWithProps psym = new SimpleSymWithProps();
                psym.setProperty("type", annot_type);
                psym.setProperty("container sym", Boolean.TRUE);
                psym.addSpan(new SimpleSeqSpan(0, 1000000000, aseq2));
                if (annot_seq && aseq2 != null) {
                    aseq2.addAnnotation(psym);
                }
                for (int k = 0; k < rpt_count; ++k) {
                    final int start = dis.readInt();
                    final int end = dis.readInt();
                    final LeafSingletonSymmetry rpt = new LeafSingletonSymmetry(start, end, aseq2);
                    psym.addChild(rpt);
                    rpt_syms.add(rpt);
                }
            }
        }
        finally {
            GeneralUtils.safeClose(bis);
            GeneralUtils.safeClose(dis);
        }
        return rpt_syms;
    }
    
    public static void main(final String[] args) {
        try {
            if (BrptParser.TEST_BINARY_PARSE) {
                final String binfile = args[0];
                System.out.println("parsing in rpt data from .brpt file: " + binfile);
                final File ifil = new File(binfile);
                final InputStream istr = new FileInputStream(ifil);
                final GenometryModel gmodel = GenometryModel.getGenometryModel();
                final AnnotatedSeqGroup seq_group = gmodel.addSeqGroup("Test Group");
                parse(istr, "rpt", seq_group, true);
                System.out.println("finished parsing in rpt data from .brpt file");
            }
            else if (args.length >= 2) {
                final String genome_version = args[0];
                final String text_infile = args[1];
                String bin_outfile;
                if (args.length >= 3) {
                    bin_outfile = args[2];
                }
                else if (text_infile.endsWith(".txt")) {
                    bin_outfile = text_infile.substring(0, text_infile.length() - 4) + ".brpt";
                }
                else {
                    bin_outfile = text_infile + ".brpt";
                }
                final File ifil2 = new File(text_infile);
                final BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(ifil2)));
                System.out.println("reading in text data from: " + text_infile);
                final List<SeqSymmetry> parent_syms = readTextFormat(br);
                final File ofil = new File(bin_outfile);
                final DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(ofil)));
                System.out.println("outputing binary data to: " + bin_outfile);
                outputBrptFormat(parent_syms, genome_version, dos);
                System.out.println("finished converting text data to binary .brpt format");
            }
            else {
                System.out.println("Usage:  java ... BsnpParser <genome_version> <text infile> [<binary outfile>]");
                System.exit(1);
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    @Override
    public List<? extends SeqSymmetry> parse(final InputStream is, final AnnotatedSeqGroup group, final String nameType, final String uri, final boolean annotate_seq) throws Exception {
        final List<SeqSymmetry> alist = parse(is, uri, group, annotate_seq);
        Logger.getLogger(BrptParser.class.getName()).log(Level.FINE, "total repeats loaded: {0}", alist.size());
        return alist;
    }
    
    static {
        BrptParser.line_regex = Pattern.compile("\\s+");
        BrptParser.TEST_BINARY_PARSE = false;
    }
}
