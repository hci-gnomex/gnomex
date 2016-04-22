//
// Decompiled by Procyon v0.5.30
//

package com.affymetrix.genometryImpl.parsers;

import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.File;
import java.util.HashMap;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.OutputStream;
import java.util.Collection;
import com.affymetrix.genometryImpl.BioSeq;
import java.util.Iterator;
import java.util.Map;
import com.affymetrix.genometryImpl.symmetry.EfficientProbesetSymA;
import com.affymetrix.genometryImpl.SeqSpan;
import com.affymetrix.genometryImpl.span.SimpleSeqSpan;
import com.affymetrix.genometryImpl.symmetry.SimpleSymWithProps;
import com.affymetrix.genometryImpl.SharedProbesetInfo;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import com.affymetrix.genometryImpl.util.GeneralUtils;
import java.io.DataInputStream;
import java.io.BufferedInputStream;
import com.affymetrix.genometryImpl.GenometryModel;
import java.io.IOException;
import com.affymetrix.genometryImpl.symmetry.SeqSymmetry;
import com.affymetrix.genometryImpl.AnnotatedSeqGroup;
import java.io.InputStream;
import java.util.List;

public final class Bprobe1Parser implements AnnotationWriter, Parser
{
    private static final boolean DEBUG = false;
    static List<String> pref_list;
    String type_prefix;

    public Bprobe1Parser() {
        this.type_prefix = null;
    }

    public void setTypePrefix(final String prefix) {
        this.type_prefix = prefix;
    }

    public String getTypePrefix() {
        return this.type_prefix;
    }

    public List<SeqSymmetry> parse(final InputStream istr, final AnnotatedSeqGroup group, final boolean annotate_seq, final String default_type) throws IOException {
        return this.parse(istr, group, annotate_seq, default_type, false);
    }

    public static AnnotatedSeqGroup getSeqGroup(final InputStream istr, AnnotatedSeqGroup group, final GenometryModel gmodel) {
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
            final String format = dis.readUTF();
            final int format_version = dis.readInt();
            final boolean version2 = format.equals("bp2");
            final String seq_group_name = dis.readUTF();
            final String seq_group_version = dis.readUTF();
            final String seq_group_id = seq_group_name + seq_group_version;
            if (seq_group_id == null) {
                System.err.println("bprobe1 file does not specify a genome name or version, these are required!");
                return null;
            }
            if (group.isSynonymous(seq_group_id)) {
                return group;
            }
            group = gmodel.getSeqGroup(seq_group_id);
            if (group == null) {
                group = gmodel.addSeqGroup(seq_group_id);
            }
            return group;
        }
        catch (Exception ex) {
            System.err.println("Error parsing file");
            return null;
        }
        finally {
            GeneralUtils.safeClose(dis);
            GeneralUtils.safeClose(bis);
        }
    }

    public List<SeqSymmetry> parse(final InputStream istr, final AnnotatedSeqGroup group, final boolean annotate_seq, final String default_type, final boolean populate_id_hash) throws IOException {
        System.out.println("in Bprobe1Parser, populating id hash: " + populate_id_hash);
        final Map<String, Object> tagvals = new LinkedHashMap<String, Object>();
        final Map<String, Object> seq2syms = new LinkedHashMap<String, Object>();
        final Map<String, Integer> seq2lengths = new LinkedHashMap<String, Integer>();
        DataInputStream dis = null;
        String id_prefix = "";
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
            final boolean version2 = format.equals("bp2");
            final String seq_group_name = dis.readUTF();
            final String seq_group_version = dis.readUTF();
            final String seq_group_id = seq_group_name + seq_group_version;
            if (seq_group_id == null) {
                System.err.println("bprobe1 file does not specify a genome name or version, these are required!");
                return null;
            }
            if (!group.isSynonymous(seq_group_id)) {
                System.err.println("In Bprobe1Parser, mismatch between AnnotatedSeqGroup argument: " + group.getID() + " and group name+version in bprobe1 file: " + seq_group_id);
                return null;
            }
            final String specified_type = dis.readUTF();
            String annot_type;
            if (specified_type == null || specified_type.length() <= 0) {
                annot_type = default_type;
            }
            else if (this.type_prefix == null) {
                annot_type = specified_type;
            }
            else {
                annot_type = this.type_prefix + specified_type;
            }
            final int probe_length = dis.readInt();
            if (version2) {
                id_prefix = dis.readUTF();
                if (!id_prefix.endsWith(":")) {
                    id_prefix += ":";
                }
            }
            for (int seq_count = dis.readInt(), i = 0; i < seq_count; ++i) {
                final String seqid = dis.readUTF();
                final int seq_length = dis.readInt();
                final int probeset_count = dis.readInt();
                final SeqSymmetry[] syms = new SeqSymmetry[probeset_count];
                seq2syms.put(seqid, syms);
                seq2lengths.put(seqid, seq_length);
            }
            for (int tagval_count = dis.readInt(), j = 0; j < tagval_count; ++j) {
                final String tag = dis.readUTF();
                final String val = dis.readUTF();
                tagvals.put(tag, val);
            }
            tagvals.put("method", annot_type);
            for (final String seqid2 : seq2syms.keySet()) {
                final SeqSymmetry[] syms2 = (SeqSymmetry[]) seq2syms.get(seqid2);
                final int probeset_count2 = syms2.length;
                BioSeq aseq = group.getSeq(seqid2);
                final SharedProbesetInfo shared_info = new SharedProbesetInfo(aseq, probe_length, id_prefix, tagvals);
                if (aseq == null) {
                    final int seqlength = seq2lengths.get(seqid2);
                    aseq = group.addSeq(seqid2, seqlength);
                }
                final SimpleSymWithProps container_sym = new SimpleSymWithProps(probeset_count2);
                container_sym.addSpan(new SimpleSeqSpan(0, aseq.getLength(), aseq));
                container_sym.setProperty("method", annot_type);
                container_sym.setProperty("preferred_formats", Bprobe1Parser.pref_list);
                container_sym.setProperty("container sym", Boolean.TRUE);
                for (int k = 0; k < probeset_count2; ++k) {
                    final int nid = dis.readInt();
                    final int b = dis.readByte();
                    final int probe_count = Math.abs(b);
                    final boolean forward = b >= 0;
                    if (probe_count == 0) {
                        throw new IOException("Probe_count is zero for '" + nid + "'");
                    }
                    final int[] cmins = new int[probe_count];
                    for (int l = 0; l < probe_count; ++l) {
                        final int min = dis.readInt();
                        cmins[l] = min;
                    }
                    final SeqSymmetry psym = new EfficientProbesetSymA(shared_info, cmins, forward, nid);
                    container_sym.addChild(syms2[k] = psym);
                    results.add(psym);
                    if (populate_id_hash) {
                        group.addToIndex(psym.getID(), psym);
                    }
                }
                if (annotate_seq) {
                    aseq.addAnnotation(container_sym);
                }
            }
            System.out.println("finished parsing probeset file");
        }
        finally {
            GeneralUtils.safeClose(dis);
        }
        return results;
    }

    @Override
    public boolean writeAnnotations(final Collection<? extends SeqSymmetry> syms, final BioSeq aseq, final String type, final OutputStream outstream) {
        boolean success = false;
        final AnnotatedSeqGroup group = aseq.getSeqGroup();
        final String groupid = group.getID();
        final String seqid = aseq.getID();
        final int acount = syms.size();
        int probe_length = 0;
        String id_prefix = "";
        if (syms.size() > 0) {
            final EfficientProbesetSymA fsym = (EfficientProbesetSymA)syms.iterator().next();
            probe_length = fsym.getProbeLength();
            id_prefix = fsym.getIDPrefix();
        }
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
            dos.writeUTF("bp2");
            dos.writeInt(1);
            dos.writeUTF(groupid);
            dos.writeUTF("");
            dos.writeUTF(type);
            dos.writeInt(probe_length);
            dos.writeUTF(id_prefix);
            dos.writeInt(1);
            dos.writeUTF(seqid);
            dos.writeInt(aseq.getLength());
            dos.writeInt(syms.size());
            dos.writeInt(0);
            for (final SeqSymmetry psym : syms) {
                writeProbeset(psym, aseq, dos);
            }
            dos.flush();
            success = true;
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return success;
    }

    public static void convertGff(final String gff_file, final String output_file, final String genome_id, final String version_id, final String annot_type, final String id_prefix) throws IOException {
        final AnnotatedSeqGroup seq_group = new AnnotatedSeqGroup(genome_id);
        final int probe_length = 25;
        final Map<String, String> tagvals = new HashMap<String, String>();
        tagvals.put("tagval_test_1", "testing1");
        tagvals.put("tagval_test_2", "testing2");
        List<? extends SeqSymmetry> annots = new ArrayList<SeqSymmetry>();
        BufferedInputStream bis = null;
        try {
            final GFFParser gff_parser = new GFFParser();
            bis = new BufferedInputStream(new FileInputStream(new File(gff_file)));
            annots = gff_parser.parse(bis, seq_group, false);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        finally {
            GeneralUtils.safeClose(bis);
        }
        BufferedOutputStream bos = null;
        DataOutputStream dos = null;
        try {
            final int total_annot_count = annots.size();
            final int seq_count = seq_group.getSeqCount();
            System.out.println("done parsing, seq count = " + seq_count + ", total annot count = " + total_annot_count);
            bos = new BufferedOutputStream(new FileOutputStream(new File(output_file)));
            dos = new DataOutputStream(bos);
            dos.writeUTF("bp2");
            dos.writeInt(1);
            dos.writeUTF(seq_group.getID());
            dos.writeUTF(version_id);
            dos.writeUTF(annot_type);
            dos.writeInt(probe_length);
            dos.writeUTF(id_prefix);
            dos.writeInt(seq_count);
            for (final BioSeq aseq : seq_group.getSeqList()) {
                final String seqid = aseq.getID();
                final int seq_length = aseq.getLength();
                final int container_count = aseq.getAnnotationCount();
                int annot_count = 0;
                for (int i = 0; i < container_count; ++i) {
                    final SeqSymmetry cont = aseq.getAnnotation(i);
                    annot_count += cont.getChildCount();
                }
                System.out.println("seqid: " + seqid + ", annot count: " + annot_count);
                dos.writeUTF(seqid);
                dos.writeInt(seq_length);
                dos.writeInt(annot_count);
            }
            final int tagval_count = tagvals.size();
            dos.writeInt(tagval_count);
            for (final Map.Entry<String, String> ent : tagvals.entrySet()) {
                final String tag = ent.getKey();
                final String val = ent.getValue();
                dos.writeUTF(tag);
                dos.writeUTF(val);
            }
            for (final BioSeq aseq2 : seq_group.getSeqList()) {
                for (int container_count2 = aseq2.getAnnotationCount(), j = 0; j < container_count2; ++j) {
                    final SeqSymmetry cont2 = aseq2.getAnnotation(j);
                    for (int annot_count2 = cont2.getChildCount(), k = 0; k < annot_count2; ++k) {
                        writeProbeset(cont2.getChild(k), aseq2, dos);
                    }
                }
            }
        }
        finally {
            GeneralUtils.safeClose(bos);
            GeneralUtils.safeClose(dos);
        }
    }

    protected static void writeProbeset(final SeqSymmetry psym, final BioSeq aseq, final DataOutputStream dos) throws IOException {
        final SeqSpan pspan = psym.getSpan(aseq);
        final int child_count = psym.getChildCount();
        int intid;
        if (psym instanceof EfficientProbesetSymA) {
            intid = ((EfficientProbesetSymA)psym).getIntID();
        }
        else {
            intid = Integer.parseInt(psym.getID());
        }
        dos.writeInt(intid);
        final byte strand_and_count = (byte)(pspan.isForward() ? child_count : (-child_count));
        dos.writeByte(strand_and_count);
        for (int m = 0; m < child_count; ++m) {
            final SeqSymmetry csym = psym.getChild(m);
            dos.writeInt(csym.getSpan(aseq).getMin());
        }
    }

    @Override
    public String getMimeType() {
        return "binary/bp2";
    }

    public static void main(final String[] args) throws IOException {
        String in_file = "";
        String out_file = "";
        String id_prefix = "";
        String genomeid = "";
        String versionid = "";
        String annot_type = "";
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
            System.out.println("Usage:  java ... Bprobe1Parser <GFF infile> <BP1 outfile> <id_prefix> <annot type> <genomeid> [<version>]");
            System.out.println("Example:  java ... Bprobe1Parser foo.gff foo.bp1 HuEx HuEx-1_0-st-Probes H_sapiens_Jul_2003");
            System.exit(1);
        }
        System.out.println("Creating a '.bp2' format file: ");
        System.out.println("Input '" + in_file + "'");
        System.out.println("Output '" + out_file + "'");
        convertGff(in_file, out_file, genomeid, versionid, annot_type, id_prefix);
        System.out.println("DONE!  Finished converting GFF file to BP2 file.");
        System.out.println("");
    }

    @Override
    public List<? extends SeqSymmetry> parse(final InputStream is, final AnnotatedSeqGroup group, final String nameType, final String uri, final boolean annotate_seq) throws Exception {
        return this.parse(is, group, annotate_seq, uri, true);
    }

    static {
        (Bprobe1Parser.pref_list = new ArrayList<String>()).add("bp2");
    }
}
