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
import java.io.FileInputStream;
import java.io.File;
import java.io.DataInputStream;
import java.io.BufferedInputStream;
import com.affymetrix.genometryImpl.util.Timer;
import java.util.Map;
import com.affymetrix.genometryImpl.symmetry.SymWithProps;
import com.affymetrix.genometryImpl.symmetry.MutableSeqSymmetry;
import java.util.HashMap;
import java.io.BufferedReader;
import java.util.Iterator;
import com.affymetrix.genometryImpl.AnnotatedSeqGroup;
import com.affymetrix.genometryImpl.symmetry.UcscGffSym;
import com.affymetrix.genometryImpl.SeqSpan;
import com.affymetrix.genometryImpl.span.SimpleSeqSpan;
import com.affymetrix.genometryImpl.symmetry.SimpleSymWithProps;
import com.affymetrix.genometryImpl.GenometryModel;
import java.io.InputStream;
import java.io.IOException;
import com.affymetrix.genometryImpl.BioSeq;
import com.affymetrix.genometryImpl.symmetry.EfficientSnpSym;
import java.util.Comparator;
import java.util.Collections;
import com.affymetrix.genometryImpl.comparator.SeqSymMinComparator;
import java.util.ArrayList;
import java.io.DataOutputStream;
import com.affymetrix.genometryImpl.symmetry.SeqSymmetry;
import java.util.List;
import java.util.regex.Pattern;

public class BsnpParser implements Parser
{
    private static final Pattern line_regex;

    private static void outputBsnpFormat(final List<SeqSymmetry> parents, final String genome_version, final DataOutputStream dos) throws IOException {
        final int pcount = parents.size();
        dos.writeUTF(genome_version);
        dos.writeInt(pcount);
        for (int i = 0; i < pcount; ++i) {
            final SeqSymmetry parent = parents.get(i);
            final BioSeq seq = parent.getSpanSeq(0);
            final String seqid = seq.getID();
            final int snp_count = parent.getChildCount();
            dos.writeUTF(seqid);
            dos.writeInt(snp_count);
        }
        int total_snp_count = 0;
        for (int j = 0; j < pcount; ++j) {
            final SeqSymmetry parent2 = parents.get(j);
            final BioSeq seq2 = parent2.getSpanSeq(0);
            final int snp_count = parent2.getChildCount();
            final List<SeqSymmetry> snps = new ArrayList<SeqSymmetry>(snp_count);
            for (int k = 0; k < snp_count; ++k) {
                snps.add(parent2.getChild(k));
            }
            Collections.sort(snps, new SeqSymMinComparator(seq2));
            for (int k = 0; k < snp_count; ++k) {
                final EfficientSnpSym snp = (EfficientSnpSym) snps.get(k);
                final int base_coord = snp.getSpan(0).getMin();
                dos.writeInt(base_coord);
                ++total_snp_count;
            }
        }
        System.out.println("total snps output to bsnp file: " + total_snp_count);
    }

    private static List<SeqSymmetry> readGffFormat(final InputStream istr, final GenometryModel gmodel) throws IOException {
        final AnnotatedSeqGroup seq_group = gmodel.addSeqGroup("Test Group");
        final List<SeqSymmetry> results = new ArrayList<SeqSymmetry>();
        final GFFParser gff_parser = new GFFParser();
        gff_parser.parse(istr, seq_group, true);
        final int problem_count = 0;
        for (final BioSeq aseq : seq_group.getSeqList()) {
            final int acount = aseq.getAnnotationCount();
            final String seqid = aseq.getID();
            System.out.println("seq = " + seqid + ", annots = " + acount);
            if (acount >= 1) {
                final SimpleSymWithProps new_psym = new SimpleSymWithProps();
                final BioSeq seq = new BioSeq(seqid, aseq.getVersion(), 1000000000);
                new_psym.addSpan(new SimpleSeqSpan(0, 1000000000, seq));
                new_psym.setProperty("container sym", Boolean.TRUE);
                for (int k = 0; k < acount; ++k) {
                    final SeqSymmetry psym = aseq.getAnnotation(k);
                    final int child_count = psym.getChildCount();
                    System.out.println("    child annots: " + child_count);
                    for (int i = 0; i < child_count; ++i) {
                        final UcscGffSym csym = (UcscGffSym)psym.getChild(i);
                        final int coord = csym.getSpan(0).getMin();
                        csym.getFeatureType();
                        final EfficientSnpSym snp_sym = new EfficientSnpSym(new_psym, coord);
                        new_psym.addChild(snp_sym);
                    }
                }
                results.add(new_psym);
            }
        }
        System.out.println("problems: " + problem_count);
        return results;
    }

    private static List<SeqSymmetry> readTextFormat(final BufferedReader br) {
        int snp_count = 0;
        int weird_length_count = 0;
        final Map<String, SeqSymmetry> id2psym = new HashMap<String, SeqSymmetry>();
        final List<SeqSymmetry> parent_syms = new ArrayList<SeqSymmetry>();
        try {
            String line;
            while ((line = br.readLine()) != null) {
                final String[] fields = BsnpParser.line_regex.split(line);
                final String seqid = fields[1].intern();
                MutableSeqSymmetry psym = (MutableSeqSymmetry)id2psym.get(seqid);
                if (psym == null) {
                    psym = new SimpleSymWithProps();
                    final BioSeq seq = new BioSeq(seqid, seqid, 1000000000);
                    psym.addSpan(new SimpleSeqSpan(0, 1000000000, seq));
                    ((SymWithProps)psym).setProperty("container sym", Boolean.TRUE);
                    id2psym.put(seqid, psym);
                    parent_syms.add(psym);
                }
                final int min = Integer.parseInt(fields[2]);
                final int max = Integer.parseInt(fields[3]);
                final int length = max - min;
                if (length != 1) {
                    System.out.println("length != 1: " + line);
                    ++weird_length_count;
                }
                final EfficientSnpSym snp_sym = new EfficientSnpSym(psym, min);
                psym.addChild(snp_sym);
                ++snp_count;
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        System.out.println("snp count: " + snp_count);
        System.out.println("weird length count: " + weird_length_count);
        return parent_syms;
    }

    public static List<SeqSymmetry> parse(final InputStream istr, final String annot_type, final AnnotatedSeqGroup seq_group, final boolean annot_seq) throws IOException {
        final Timer tim = new Timer();
        tim.start();
        List<SeqSymmetry> snp_syms = null;
        BufferedInputStream bis;
        if (istr instanceof BufferedInputStream) {
            bis = (BufferedInputStream)istr;
        }
        else {
            bis = new BufferedInputStream(istr);
        }
        final DataInputStream dis = new DataInputStream(bis);
        dis.readUTF();
        final int seq_count = dis.readInt();
        final int[] snp_counts = new int[seq_count];
        final String[] seqids = new String[seq_count];
        final BioSeq[] seqs = new BioSeq[seq_count];
        int total_snp_count = 0;
        for (int i = 0; i < seq_count; ++i) {
            final String seqid = dis.readUTF();
            seqids[i] = seqid;
            BioSeq aseq = seq_group.getSeq(seqid);
            if (aseq == null) {
                aseq = seq_group.addSeq(seqid, 0);
            }
            seqs[i] = aseq;
            snp_counts[i] = dis.readInt();
            total_snp_count += snp_counts[i];
        }
        snp_syms = new ArrayList<SeqSymmetry>(total_snp_count);
        for (int i = 0; i < seq_count; ++i) {
            final BioSeq aseq2 = seqs[i];
            final int snp_count = snp_counts[i];
            final SimpleSymWithProps psym = new SimpleSymWithProps();
            psym.setProperty("type", annot_type);
            psym.setProperty("container sym", Boolean.TRUE);
            if (aseq2 != null) {
                psym.addSpan(new SimpleSeqSpan(0, aseq2.getLength(), aseq2));
            }
            else {
                psym.addSpan(new SimpleSeqSpan(0, 1000000000, null));
            }
            if (annot_seq && aseq2 != null) {
                aseq2.addAnnotation(psym);
            }
            int base_coord = 0;
            for (int k = 0; k < snp_count; ++k) {
                base_coord = dis.readInt();
                final EfficientSnpSym snp = new EfficientSnpSym(psym, base_coord);
                psym.addChild(snp);
                snp_syms.add(snp);
            }
            if (aseq2 != null && aseq2.getLength() < base_coord) {
                aseq2.setLength(base_coord);
            }
        }
        tim.print();
        return snp_syms;
    }

    public static void main(final String[] args) {
        final GenometryModel gmodel = GenometryModel.getGenometryModel();
        try {
            if (args.length >= 2) {
                final String genome_version = args[0];
                final String text_infile = args[1];
                String bin_outfile;
                if (args.length >= 3) {
                    bin_outfile = args[2];
                }
                else if (text_infile.endsWith(".txt") || text_infile.endsWith(".gff")) {
                    bin_outfile = text_infile.substring(0, text_infile.length() - 4) + ".bsnp";
                }
                else {
                    bin_outfile = text_infile + ".bsnp";
                }
                final File ifil = new File(text_infile);
                List<SeqSymmetry> parent_syms = new ArrayList<SeqSymmetry>();
                if (text_infile.endsWith(".txt")) {
                    final BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(ifil)));
                    System.out.println("reading in text data from: " + text_infile);
                    parent_syms = readTextFormat(br);
                    br.close();
                }
                else if (text_infile.endsWith(".gff")) {
                    final InputStream istr = new FileInputStream(ifil);
                    System.out.println("reading in gff data from: " + text_infile);
                    parent_syms = readGffFormat(istr, gmodel);
                    istr.close();
                }
                final File ofil = new File(bin_outfile);
                final DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(ofil)));
                System.out.println("outputting binary data to: " + bin_outfile);
                outputBsnpFormat(parent_syms, genome_version, dos);
                dos.close();
                System.out.println("finished converting text data to binary .bsnp format");
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
        Logger.getLogger(BsnpParser.class.getName()).log(Level.FINE, "total snps loaded: " + alist.size());
        return alist;
    }

    static {
        line_regex = Pattern.compile("\\s+");
    }
}
