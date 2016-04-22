//
// Decompiled by Procyon v0.5.30
//

package com.affymetrix.genometryImpl.parsers;

import java.util.Arrays;
import java.io.FileOutputStream;
import com.affymetrix.genometryImpl.util.SeqUtils;
import com.affymetrix.genometryImpl.symmetry.SymWithProps;
import java.util.HashMap;
import java.io.FileInputStream;
import java.io.File;
import com.affymetrix.genometryImpl.GenometryModel;
import com.affymetrix.genometryImpl.MutableSeqSpan;
import java.util.Iterator;
import com.affymetrix.genometryImpl.span.SimpleMutableSeqSpan;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.OutputStream;
import java.util.Collection;
import com.affymetrix.genometryImpl.BioSeq;
import java.util.Map;
import com.affymetrix.genometryImpl.util.GeneralUtils;
import com.affymetrix.genometryImpl.symmetry.EfficientProbesetSymA;
import java.io.IOException;
import com.affymetrix.genometryImpl.symmetry.SingletonSymWithIntId;
import com.affymetrix.genometryImpl.SeqSpan;
import com.affymetrix.genometryImpl.span.SimpleSeqSpan;
import com.affymetrix.genometryImpl.symmetry.SimpleSymWithProps;
import com.affymetrix.genometryImpl.SharedProbesetInfo;
import java.io.DataInputStream;
import java.io.BufferedInputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import com.affymetrix.genometryImpl.symmetry.SeqSymmetry;
import com.affymetrix.genometryImpl.AnnotatedSeqGroup;
import java.io.InputStream;
import java.util.List;

public final class ExonArrayDesignParser implements AnnotationWriter, Parser
{
    static boolean USE_FULL_HIERARCHY;
    static boolean DEBUG;
    static List<String> pref_list;

    public List<SeqSymmetry> parse(final InputStream istr, final AnnotatedSeqGroup group, final boolean annotate_seq, final String default_type) throws IOException {
        final Map<String, Object> tagvals = new LinkedHashMap<String, Object>();
        DataInputStream dis = null;
        final List<SeqSymmetry> results = new ArrayList<SeqSymmetry>();
        try {
            BufferedInputStream bis;
            if (istr instanceof BufferedInputStream) {
                bis = (BufferedInputStream)istr;
            }
            else {
                bis = new BufferedInputStream(istr);
            }
            dis = new DataInputStream(bis);
            final String format = dis.readUTF();
            final int format_version = dis.readInt();
            final String seq_group_name = dis.readUTF();
            final String seq_group_version = dis.readUTF();
            final String seq_group_id = seq_group_name + seq_group_version;
            if (seq_group_id == null) {
                System.err.println("ead file does not specify a genome name or version, these are required!");
                return null;
            }
            if (!group.isSynonymous(seq_group_id)) {
                System.err.println("In ExonArrayDesignParser, mismatch between AnnotatedSeqGroup argument: " + group.getID() + " and group name+version in ead file: " + seq_group_id);
                return null;
            }
            final String specified_type = dis.readUTF();
            String annot_type;
            if (specified_type == null || specified_type.length() <= 0) {
                annot_type = default_type;
            }
            else {
                annot_type = specified_type;
            }
            final int probe_length = dis.readInt();
            final String id_prefix = dis.readUTF();
            for (int tagval_count = dis.readInt(), i = 0; i < tagval_count; ++i) {
                final String tag = dis.readUTF();
                final String val = dis.readUTF();
                tagvals.put(tag, val);
            }
            tagvals.put("method", annot_type);
            final int seq_count = dis.readInt();
            int total_tcluster_count = 0;
            int total_ecluster_count = 0;
            int total_psr_count = 0;
            int total_probeset_count = 0;
            int total_probe_count = 0;
            for (int seqindex = 0; seqindex < seq_count && !Thread.currentThread().isInterrupted(); ++seqindex) {
                final String seqid = dis.readUTF();
                final int seq_length = dis.readInt();
                final int transcript_cluster_count = dis.readInt();
                total_tcluster_count += transcript_cluster_count;
                BioSeq aseq = group.getSeq(seqid);
                if (aseq == null) {
                    aseq = group.addSeq(seqid, seq_length);
                }
                final SharedProbesetInfo shared_info = new SharedProbesetInfo(aseq, probe_length, id_prefix, tagvals);
                final SimpleSymWithProps container_sym = new SimpleSymWithProps(transcript_cluster_count);
                container_sym.addSpan(new SimpleSeqSpan(0, aseq.getLength(), aseq));
                container_sym.setProperty("method", annot_type);
                container_sym.setProperty("preferred_formats", ExonArrayDesignParser.pref_list);
                container_sym.setProperty("container sym", Boolean.TRUE);
                if (ExonArrayDesignParser.USE_FULL_HIERARCHY) {
                    for (int tindex = 0; tindex < transcript_cluster_count; ++tindex) {
                        final int tcluster_id = dis.readInt();
                        final int tstart = dis.readInt();
                        final int tend = dis.readInt();
                        final int exon_cluster_count = dis.readInt();
                        total_ecluster_count += exon_cluster_count;
                        final SingletonSymWithIntId tcluster = new SingletonSymWithIntId(tstart, tend, aseq, id_prefix, tcluster_id);
                        results.add(tcluster);
                        container_sym.addChild(tcluster);
                        for (int eindex = 0; eindex < exon_cluster_count; ++eindex) {
                            final int ecluster_id = dis.readInt();
                            final int estart = dis.readInt();
                            final int eend = dis.readInt();
                            final int psr_count = dis.readInt();
                            total_psr_count += psr_count;
                            final SingletonSymWithIntId ecluster = new SingletonSymWithIntId(estart, eend, aseq, id_prefix, ecluster_id);
                            tcluster.addChild(ecluster);
                            for (int psr_index = 0; psr_index < psr_count; ++psr_index) {
                                final int psr_id = dis.readInt();
                                final int psr_start = dis.readInt();
                                final int psr_end = dis.readInt();
                                final int probeset_count = dis.readInt();
                                total_probeset_count += probeset_count;
                                final SingletonSymWithIntId psr = new SingletonSymWithIntId(psr_start, psr_end, aseq, id_prefix, psr_id);
                                ecluster.addChild(psr);
                                for (int probeset_index = 0; probeset_index < probeset_count; ++probeset_index) {
                                    final int nid = dis.readInt();
                                    final int b = dis.readByte();
                                    final int probe_count = Math.abs(b);
                                    total_probe_count += probe_count;
                                    final boolean forward = b >= 0;
                                    if (probe_count == 0) {
                                        throw new IOException("Probe_count is zero for '" + nid + "'");
                                    }
                                    final int[] cmins = new int[probe_count];
                                    for (int pindex = 0; pindex < probe_count; ++pindex) {
                                        final int min = dis.readInt();
                                        cmins[pindex] = min;
                                    }
                                    final SeqSymmetry probeset_sym = new EfficientProbesetSymA(shared_info, cmins, forward, nid);
                                    psr.addChild(probeset_sym);
                                }
                            }
                        }
                    }
                }
                else {
                    for (int tindex = 0; tindex < transcript_cluster_count; ++tindex) {
                        final int tcluster_id = dis.readInt();
                        final int tstart = dis.readInt();
                        final int tend = dis.readInt();
                        final int probeset_count2 = dis.readInt();
                        total_probeset_count += probeset_count2;
                        final SingletonSymWithIntId tcluster = new SingletonSymWithIntId(tstart, tend, aseq, id_prefix, tcluster_id);
                        results.add(tcluster);
                        container_sym.addChild(tcluster);
                        for (int probeset_index2 = 0; probeset_index2 < probeset_count2; ++probeset_index2) {
                            final int nid2 = dis.readInt();
                            final int b2 = dis.readByte();
                            final int probe_count2 = Math.abs(b2);
                            total_probe_count += probe_count2;
                            final boolean forward2 = b2 >= 0;
                            if (probe_count2 == 0) {
                                throw new IOException("Probe_count is zero for '" + nid2 + "'");
                            }
                            final int[] cmins2 = new int[probe_count2];
                            for (int pindex2 = 0; pindex2 < probe_count2; ++pindex2) {
                                final int min2 = dis.readInt();
                                cmins2[pindex2] = min2;
                            }
                            final SeqSymmetry probeset_sym2 = new EfficientProbesetSymA(shared_info, cmins2, forward2, nid2);
                            tcluster.addChild(probeset_sym2);
                        }
                    }
                }
                if (annotate_seq && transcript_cluster_count > 0) {
                    aseq.addAnnotation(container_sym);
                }
            }
            System.out.println("transcript_cluster count: " + total_tcluster_count);
            if (ExonArrayDesignParser.DEBUG) {
                System.out.println("exon_cluster count: " + total_ecluster_count);
                System.out.println("psr count: " + total_psr_count);
            }
            System.out.println("probeset count: " + total_probeset_count);
            System.out.println("probe count: " + total_probe_count);
            System.out.println("finished parsing exon array design file");
        }
        finally {
            GeneralUtils.safeClose(dis);
        }
        return results;
    }

    @Override
    public boolean writeAnnotations(final Collection<? extends SeqSymmetry> syms, final BioSeq aseq, final String type, final OutputStream outstream) throws IOException {
        boolean success = false;
        DataOutputStream dos = null;
        try {
            if (outstream instanceof DataOutputStream) {
                dos = (DataOutputStream)outstream;
            }
            else if (outstream instanceof BufferedOutputStream) {
                dos = new DataOutputStream(outstream);
            }
            else {
                dos = new DataOutputStream(outstream);
            }
            final List<BioSeq> oneseq = new ArrayList<BioSeq>();
            oneseq.add(aseq);
            SeqSymmetry tcluster_exemplar = null;
            if (syms.size() > 0) {
                tcluster_exemplar = (SeqSymmetry)syms.iterator().next();
                writeEadHeader(tcluster_exemplar, type, oneseq, dos);
                writeSeqWithAnnots(syms, aseq, dos);
            }
            dos.flush();
            success = true;
        }
        catch (Exception ex) {
            final IOException ioe = new IOException(ex.getLocalizedMessage());
            ioe.initCause(ex);
            throw ioe;
        }
        return success;
    }

    private static void writeEadHeader(final SeqSymmetry tcluster_exemplar, final String annot_type, final List<BioSeq> seqs, final DataOutputStream dos) throws IOException {
        final BioSeq seq0 = seqs.get(0);
        final AnnotatedSeqGroup group = seq0.getSeqGroup();
        EfficientProbesetSymA probeset_exemplar;
        if (ExonArrayDesignParser.USE_FULL_HIERARCHY) {
            final SeqSymmetry exon_cluster = tcluster_exemplar.getChild(0);
            final SeqSymmetry psr = exon_cluster.getChild(0);
            probeset_exemplar = (EfficientProbesetSymA)psr.getChild(0);
        }
        else {
            probeset_exemplar = (EfficientProbesetSymA)tcluster_exemplar.getChild(0);
        }
        final int probe_length = probeset_exemplar.getProbeLength();
        final String id_prefix = probeset_exemplar.getIDPrefix();
        final String groupid = group.getID();
        dos.writeUTF("ead");
        dos.writeInt(1);
        dos.writeUTF(groupid);
        dos.writeUTF("");
        dos.writeUTF(annot_type);
        dos.writeInt(probe_length);
        dos.writeUTF(id_prefix);
        dos.writeInt(0);
        dos.writeInt(seqs.size());
    }

    private static void writeSeqWithAnnots(final Collection<? extends SeqSymmetry> syms, final BioSeq aseq, final DataOutputStream dos) throws IOException {
        final String seqid = aseq.getID();
        System.out.println("seqid: " + seqid + ", annot count: " + syms.size());
        dos.writeUTF(seqid);
        dos.writeInt(aseq.getLength());
        dos.writeInt(syms.size());
        final Iterator siter = syms.iterator();
        final MutableSeqSpan mutspan = new SimpleMutableSeqSpan(0, 0, aseq);
        while (siter.hasNext() && !Thread.currentThread().isInterrupted()) {
            final SingletonSymWithIntId psym = (SingletonSymWithIntId)siter.next();
            writeTranscriptCluster(psym, mutspan, dos);
        }
    }

    private static void writeTranscriptCluster(final SingletonSymWithIntId tsym, final MutableSeqSpan scratch_span, final DataOutputStream dos) throws IOException {
        final SeqSpan tspan = tsym.getSpan(0);
        MutableSeqSpan mutspan = scratch_span;
        if (mutspan == null) {
            mutspan = new SimpleMutableSeqSpan(0, 0, tspan.getBioSeq());
        }
        if (ExonArrayDesignParser.USE_FULL_HIERARCHY) {
            final int exon_cluster_count = tsym.getChildCount();
            dos.writeInt(tsym.getIntID());
            dos.writeInt(tspan.getStart());
            dos.writeInt(tspan.getEnd());
            dos.writeInt(exon_cluster_count);
            for (int i = 0; i < exon_cluster_count; ++i) {
                final SingletonSymWithIntId esym = (SingletonSymWithIntId)tsym.getChild(i);
                final SeqSpan espan = esym.getSpan(0);
                final int psr_count = esym.getChildCount();
                dos.writeInt(esym.getIntID());
                dos.writeInt(espan.getStart());
                dos.writeInt(espan.getEnd());
                dos.writeInt(psr_count);
                for (int k = 0; k < psr_count; ++k) {
                    final SingletonSymWithIntId psym = (SingletonSymWithIntId)esym.getChild(k);
                    final SeqSpan pspan = psym.getSpan(0);
                    final int probeset_count = psym.getChildCount();
                    dos.writeInt(psym.getIntID());
                    dos.writeInt(pspan.getStart());
                    dos.writeInt(pspan.getEnd());
                    dos.writeInt(probeset_count);
                    for (int m = 0; m < probeset_count; ++m) {
                        final EfficientProbesetSymA probeset_sym = (EfficientProbesetSymA)psym.getChild(m);
                        writeProbeset(probeset_sym, mutspan, dos);
                    }
                }
            }
        }
        else {
            final int probeset_count2 = tsym.getChildCount();
            dos.writeInt(tsym.getIntID());
            dos.writeInt(tspan.getStart());
            dos.writeInt(tspan.getEnd());
            dos.writeInt(probeset_count2);
            for (int j = 0; j < probeset_count2; ++j) {
                final EfficientProbesetSymA probeset_sym2 = (EfficientProbesetSymA)tsym.getChild(j);
                writeProbeset(probeset_sym2, mutspan, dos);
            }
        }
    }

    private static void writeProbeset(final EfficientProbesetSymA psym, final MutableSeqSpan mutspan, final DataOutputStream dos) throws IOException {
        final SeqSpan pspan = psym.getSpan(0);
        final int child_count = psym.getChildCount();
        final int intid = psym.getIntID();
        dos.writeInt(intid);
        final byte strand_and_count = (byte)(pspan.isForward() ? child_count : (-child_count));
        dos.writeByte(strand_and_count);
        for (int i = 0; i < child_count; ++i) {
            final SeqSpan cspan = psym.getChildSpan(i, pspan.getBioSeq(), mutspan);
            dos.writeInt(cspan.getMin());
        }
    }

    @Override
    public String getMimeType() {
        return "binary/ead";
    }

    public static void main(final String[] args) throws IOException {
        final GenometryModel gmodel = GenometryModel.getGenometryModel();
        final boolean WRITE = false;
        final boolean READ = true;
        final String default_in_file = "c:/data/chp_data_exon/HuEx-1_0-st-v2.design-annot-hg18/gff";
        final String default_out_file = "c:/data/chp_data_exon/HuEx-1_0-st-v2.design-annot-hg18/ead/HuEx-1_0-st-v2_3level.ead";
        final String default_genome_id = "H_sapiens_Mar_2006";
        final String default_id_prefix = "HuEx-1_0-st-v2:";
        final String default_annot_type = "HuEx-1_0-st-v2";
        String in_file = default_in_file;
        String out_file = default_out_file;
        String id_prefix = default_id_prefix;
        String genomeid = default_genome_id;
        String versionid = "";
        String annot_type = default_annot_type;
        if (args.length == 5 || args.length == 6) {
            in_file = args[0];
            out_file = args[1];
            id_prefix = args[2];
            annot_type = args[3];
            genomeid = args[4];
            if (args.length == 6) {
                versionid = args[5];
            }
        }
        else {
            System.out.println("Usage:  java ... ExonArrayDesignParser <GFF infile> <EAD outfile> <id_prefix> <annot type> <genomeid> [<version>]");
            System.out.println("Example:  java ... ExonArrayDesignParser foo.gff foo.ead 'HuEx-1_0-st-v2:' HuEx-1_0-st-v2 H_sapiens_Mar_2006");
            System.exit(1);
        }
        if (WRITE) {
            System.out.println("Creating a '.ead' format file: ");
            System.out.println("Input '" + in_file + "'");
            System.out.println("Output '" + out_file + "'");
            final ExonArrayDesignParser parser = new ExonArrayDesignParser();
            convertGff(in_file, out_file, genomeid, annot_type, id_prefix);
            System.out.println("DONE!  Finished converting GFF file to EAD file.");
            System.out.println("");
        }
        if (READ) {
            try {
                final BufferedInputStream bis = new BufferedInputStream(new FileInputStream(new File(out_file)));
                final AnnotatedSeqGroup group = gmodel.addSeqGroup(genomeid + versionid);
                final ExonArrayDesignParser parser2 = new ExonArrayDesignParser();
                final List results = parser2.parse(bis, group, true, annot_type);
                System.out.println("Finished reading ead file, transcript_clusters: " + results.size());
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private static void convertGff(final String in_file, final String out_file, final String genome_id, final String annot_type, final String id_prefix) {
        final AnnotatedSeqGroup seq_group = new AnnotatedSeqGroup(genome_id);
        final int probe_length = 25;
        try {
            final File gff_file = new File(in_file);
            final List<File> gfiles = new ArrayList<File>();
            if (gff_file.isDirectory()) {
                System.out.println("processing all gff files in directory: " + in_file);
                final File[] fils = gff_file.listFiles();
                for (int i = 0; i < fils.length; ++i) {
                    final File fil = fils[i];
                    final String fname = fil.getName();
                    if (fname.endsWith(".gff") || fname.endsWith(".gtf")) {
                        gfiles.add(fil);
                    }
                }
            }
            else {
                gfiles.add(gff_file);
            }
            int printcount = 0;
            final Map<BioSeq, SimpleSymWithProps> seq2container = new HashMap<BioSeq, SimpleSymWithProps>();
            final Map<BioSeq, SharedProbesetInfo> seq2info = new HashMap<BioSeq, SharedProbesetInfo>();
            for (final File gfile : gfiles) {
                if (Thread.currentThread().isInterrupted()) {
                    break;
                }
                System.out.println("parsing gff file: " + gfile.getPath());
                final GFFParser gff_parser = new GFFParser();
                final BufferedInputStream bis = new BufferedInputStream(new FileInputStream(gfile));
                final List annots = gff_parser.parse(bis, ".", seq_group, false, false);
                System.out.println("top-level annots: " + annots.size());
                for (int tcount = annots.size(), tindex = 0; tindex < tcount && !Thread.currentThread().isInterrupted(); ++tindex) {
                    final SymWithProps tcluster = (SymWithProps)annots.get(tindex);
                    final SeqSpan tspan = (SeqSpan)tcluster.getSpan(0);
                    final BioSeq aseq = (BioSeq)tspan.getBioSeq();
                    SharedProbesetInfo shared_info = seq2info.get(aseq);
                    if (shared_info == null) {
                        shared_info = new SharedProbesetInfo(aseq, probe_length, id_prefix, null);
                        seq2info.put(aseq, shared_info);
                    }
                    SimpleSymWithProps container = seq2container.get(aseq);
                    if (container == null) {
                        container = new SimpleSymWithProps();
                        container.addSpan(new SimpleSeqSpan(0, aseq.getLength(), aseq));
                        container.setProperty("method", annot_type);
                        container.setProperty("preferred_formats", ExonArrayDesignParser.pref_list);
                        container.setProperty("container sym", Boolean.TRUE);
                        seq2container.put(aseq, container);
                    }
                    String tid = tcluster.getID();
                    if (tid == null) {
                        tid = (String)tcluster.getProperty("transcript_cluster_id");
                    }
                    final SingletonSymWithIntId new_tcluster = new SingletonSymWithIntId(tspan.getStart(), tspan.getEnd(), aseq, id_prefix, Integer.parseInt(tid));
                    container.addChild(new_tcluster);
                    for (int ecount = tcluster.getChildCount(), eindex = 0; eindex < ecount; ++eindex) {
                        final SymWithProps ecluster = (SymWithProps)tcluster.getChild(eindex);
                        final SeqSpan espan = ecluster.getSpan(0);
                        String eid = ecluster.getID();
                        if (eid == null) {
                            eid = (String)ecluster.getProperty("exon_cluster_id");
                        }
                        if (eid == null) {
                            eid = (String)ecluster.getProperty("intron_cluster_id");
                        }
                        final SingletonSymWithIntId new_ecluster = new SingletonSymWithIntId(espan.getStart(), espan.getEnd(), aseq, id_prefix, Integer.parseInt(eid));
                        if (ExonArrayDesignParser.USE_FULL_HIERARCHY) {
                            new_tcluster.addChild(new_ecluster);
                        }
                        for (int psrcount = ecluster.getChildCount(), psrindex = 0; psrindex < psrcount; ++psrindex) {
                            final SymWithProps psr = (SymWithProps)ecluster.getChild(psrindex);
                            final SeqSpan psrspan = psr.getSpan(0);
                            String psrid = psr.getID();
                            if (psrid == null) {
                                psrid = (String)psr.getProperty("psr_id");
                            }
                            final SingletonSymWithIntId new_psr = new SingletonSymWithIntId(psrspan.getStart(), psrspan.getEnd(), aseq, id_prefix, Integer.parseInt(psrid));
                            if (ExonArrayDesignParser.USE_FULL_HIERARCHY) {
                                new_ecluster.addChild(new_psr);
                            }
                            for (int probeset_count = psr.getChildCount(), probeset_index = 0; probeset_index < probeset_count; ++probeset_index) {
                                final SymWithProps probeset = (SymWithProps)psr.getChild(probeset_index);
                                final SeqSpan probeset_span = probeset.getSpan(0);
                                String probeset_id = probeset.getID();
                                if (probeset_id == null) {
                                    probeset_id = (String)probeset.getProperty("probeset_id");
                                }
                                final int probeset_nid = Integer.parseInt(probeset_id);
                                final int probecount = probeset.getChildCount();
                                final int[] probemins = new int[probecount];
                                if (printcount < 1 && tcluster.getChildCount() > 1) {
                                    System.out.println("transcript_cluster_id: " + tid);
                                    System.out.println("exon_cluster_id: " + eid);
                                    System.out.println("psr_id: " + psrid);
                                    System.out.println("probeset_id: " + probeset_id);
                                    System.out.println("probeset_nid: " + probeset_nid);
                                }
                                for (int probeindex = 0; probeindex < probecount; ++probeindex) {
                                    final SeqSymmetry probe = probeset.getChild(probeindex);
                                    probemins[probeindex] = probe.getSpan(0).getMin();
                                }
                                final EfficientProbesetSymA new_probeset = new EfficientProbesetSymA(shared_info, probemins, probeset_span.isForward(), probeset_nid);
                                if (ExonArrayDesignParser.USE_FULL_HIERARCHY) {
                                    new_psr.addChild(new_probeset);
                                }
                                else {
                                    new_tcluster.addChild(new_probeset);
                                }
                            }
                        }
                    }
                    if (printcount < 1 && tcluster.getChildCount() > 1) {
                        SeqUtils.printSymmetry(tcluster);
                        System.out.println("###########################");
                        SeqUtils.printSymmetry(new_tcluster);
                        ++printcount;
                    }
                }
                bis.close();
            }
            for (final Map.Entry<BioSeq, SimpleSymWithProps> ent : seq2container.entrySet()) {
                final BioSeq aseq2 = ent.getKey();
                final SeqSymmetry container2 = ent.getValue();
                aseq2.addAnnotation(container2);
            }
            final FileOutputStream fos = new FileOutputStream(new File(out_file));
            writeAnnotations(annot_type, seq_group, fos);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static boolean writeAnnotations(final String annot_type, final AnnotatedSeqGroup group, final OutputStream outstream) throws IOException {
        boolean success = false;
        try {
            DataOutputStream dos;
            if (outstream instanceof DataOutputStream) {
                dos = (DataOutputStream)outstream;
            }
            else if (outstream instanceof BufferedOutputStream) {
                dos = new DataOutputStream(outstream);
            }
            else {
                dos = new DataOutputStream(new BufferedOutputStream(outstream));
            }
            final int scount = group.getSeqCount();
            final List<BioSeq> seqs = group.getSeqList();
            SeqSymmetry tcluster_exemplar = null;
            if (seqs.size() > 0) {
                final BioSeq aseq = group.getSeq(0);
                final SymWithProps typesym = aseq.getAnnotation(annot_type);
                final SeqSymmetry container = typesym.getChild(0);
                tcluster_exemplar = container.getChild(0);
            }
            writeEadHeader(tcluster_exemplar, annot_type, seqs, dos);
            for (int i = 0; i < scount; ++i) {
                final BioSeq aseq2 = group.getSeq(i);
                final SymWithProps typesym2 = aseq2.getAnnotation(annot_type);
                final List<SeqSymmetry> syms = new ArrayList<SeqSymmetry>();
                for (int container_count = typesym2.getChildCount(), k = 0; k < container_count; ++k) {
                    final SeqSymmetry csym = typesym2.getChild(k);
                    for (int tcount = csym.getChildCount(), m = 0; m < tcount; ++m) {
                        final SeqSymmetry tcluster = csym.getChild(m);
                        syms.add(tcluster);
                    }
                }
                writeSeqWithAnnots(syms, aseq2, dos);
            }
            dos.flush();
            success = true;
        }
        catch (Exception ex) {
            final IOException ioe = new IOException(ex.getLocalizedMessage());
            ioe.initCause(ex);
            throw ioe;
        }
        return success;
    }

    @Override
    public List<? extends SeqSymmetry> parse(final InputStream is, final AnnotatedSeqGroup group, final String nameType, final String uri, final boolean annotate_seq) throws Exception {
        return this.parse(is, group, annotate_seq, uri);
    }

    static {
        ExonArrayDesignParser.USE_FULL_HIERARCHY = false;
        ExonArrayDesignParser.DEBUG = false;
        ExonArrayDesignParser.pref_list = Arrays.asList("ead");
    }
}
