// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.parsers.useq;

import com.affymetrix.genometryImpl.util.SynonymLookup;
import com.affymetrix.genometryImpl.BioSeq;
import com.affymetrix.genometryImpl.parsers.useq.data.USeqData;
import java.util.Iterator;
import java.util.zip.ZipEntry;
import com.affymetrix.genometryImpl.AnnotatedSeqGroup;
import java.util.Arrays;
import com.affymetrix.genometryImpl.parsers.useq.data.PositionScoreData;
import com.affymetrix.genometryImpl.parsers.useq.data.PositionData;
import java.util.HashMap;
import java.io.IOException;
import java.io.DataInputStream;
import java.io.BufferedInputStream;
import java.util.zip.ZipInputStream;
import java.util.ArrayList;
import com.affymetrix.genometryImpl.symmetry.GraphSym;
import java.util.List;
import java.io.InputStream;
import com.affymetrix.genometryImpl.GenometryModel;

public class USeqGraphParser
{
    private GenometryModel gmodel;
    private String stream_name;
    private static final float defaultFloatValue = 1.0f;
    private ArchiveInfo archiveInfo;
    
    public List<GraphSym> parseGraphSyms(final InputStream istr, final GenometryModel gmodel, final String stream_name, ArchiveInfo archiveInfo) {
        this.gmodel = gmodel;
        this.stream_name = stream_name;
        this.archiveInfo = archiveInfo;
        BufferedInputStream bis = null;
        ZipInputStream zis = null;
        final List<GraphSym> graphs = new ArrayList<GraphSym>();
        if (istr instanceof ZipInputStream) {
            zis = (ZipInputStream)istr;
        }
        else {
            if (istr instanceof BufferedInputStream) {
                bis = (BufferedInputStream)istr;
            }
            else {
                bis = new BufferedInputStream(istr);
            }
            zis = new ZipInputStream(bis);
        }
        final DataInputStream dis = new DataInputStream(zis);
        try {
            if (this.archiveInfo == null) {
                zis.getNextEntry();
                this.archiveInfo = new ArchiveInfo(zis, false);
                archiveInfo = this.archiveInfo;
            }
            final String genomeVersion = archiveInfo.getVersionedGenome();
            final AnnotatedSeqGroup asg = gmodel.getSelectedSeqGroup();
            if (asg != null && !asg.isSynonymous(genomeVersion)) {
                throw new IOException("\nGenome versions differ! Cannot load this useq data from " + genomeVersion + " into the current genome in view. Navigate to the correct genome and reload or add a synonym.\n");
            }
            ArrayList al = new ArrayList();
            final HashMap<String, ArrayList> chromData = new HashMap<String, ArrayList>();
            SliceInfo si = null;
            ZipEntry ze;
            while ((ze = zis.getNextEntry()) != null) {
                si = new SliceInfo(ze.getName());
                if (USeqUtilities.POSITION.matcher(si.getBinaryType()).matches()) {
                    final PositionData pd = new PositionData(dis, si);
                    final String chromStrand = si.getChromosome() + si.getStrand();
                    al = chromData.get(chromStrand);
                    if (al == null) {
                        al = new ArrayList();
                        chromData.put(chromStrand, al);
                    }
                    al.add(pd);
                }
                else {
                    if (!USeqUtilities.POSITION_SCORE.matcher(si.getBinaryType()).matches()) {
                        throw new IOException("\nIncorrect file type for graph generation -> " + si.getBinaryType() + " . Aborting USeq graph loading.\n");
                    }
                    final PositionScoreData pd2 = new PositionScoreData(dis, si);
                    final String chromStrand = si.getChromosome() + si.getStrand();
                    al = chromData.get(chromStrand);
                    if (al == null) {
                        al = new ArrayList();
                        chromData.put(chromStrand, al);
                    }
                    al.add(pd2);
                }
            }
            if (USeqUtilities.POSITION.matcher(si.getBinaryType()).matches()) {
                for (final String chromStrand : chromData.keySet()) {
                    al = chromData.get(chromStrand);
                    final PositionData merged = PositionData.merge(al);
                    final int[] xcoords = merged.getBasePositions();
                    final float[] ycoords = new float[xcoords.length];
                    Arrays.fill(ycoords, 1.0f);
                    final GraphSym graf = this.makeGraph(merged.getSliceInfo(), xcoords, ycoords);
                    graphs.add(graf);
                }
            }
            else {
                if (!USeqUtilities.POSITION_SCORE.matcher(si.getBinaryType()).matches()) {
                    throw new IOException("USeq graph parsing for " + si.getBinaryType() + " is not implemented.");
                }
                for (final String chromStrand : chromData.keySet()) {
                    al = chromData.get(chromStrand);
                    final PositionScoreData merged2 = PositionScoreData.merge(al);
                    final int[] xcoords = merged2.getBasePositions();
                    final float[] ycoords = merged2.getBaseScores();
                    final GraphSym graf = this.makeGraph(merged2.getSliceInfo(), xcoords, ycoords);
                    graphs.add(graf);
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            USeqUtilities.safeClose(bis);
            USeqUtilities.safeClose(dis);
            USeqUtilities.safeClose(zis);
        }
        return graphs;
    }
    
    public List<GraphSym> parseGraphSyms(final USeqArchive useqArchive, final USeqData[] useqData, final GenometryModel gmodel, final String stream_name) {
        this.gmodel = gmodel;
        this.stream_name = stream_name;
        this.archiveInfo = useqArchive.getArchiveInfo();
        final List<GraphSym> graphs = new ArrayList<GraphSym>();
        try {
            final String genomeVersion = this.archiveInfo.getVersionedGenome();
            final AnnotatedSeqGroup asg = gmodel.getSelectedSeqGroup();
            if (asg != null && !asg.isSynonymous(genomeVersion)) {
                throw new IOException("\nGenome versions differ! Cannot load this useq data from " + genomeVersion + " into the current genome in view. Navigate to the correct genome and reload or add a synonym.\n");
            }
            if (USeqUtilities.POSITION.matcher(useqArchive.getBinaryDataType()).matches()) {
                for (int i = 0; i < useqData.length; ++i) {
                    if (useqData[i] != null) {
                        final PositionData p = (PositionData)useqData[i];
                        final int[] xcoords = p.getBasePositions();
                        final float[] ycoords = new float[xcoords.length];
                        final GraphSym graf = this.makeGraph(p.getSliceInfo().getChromosome(), p.getSliceInfo().getStrand(), xcoords, ycoords);
                        graphs.add(graf);
                    }
                }
            }
            else {
                if (!USeqUtilities.POSITION_SCORE.matcher(useqArchive.getBinaryDataType()).matches()) {
                    throw new IOException("USeq graph parsing for " + useqArchive.getBinaryDataType() + " is not implemented.");
                }
                for (int i = 0; i < useqData.length; ++i) {
                    if (useqData[i] != null) {
                        final PositionScoreData p2 = (PositionScoreData)useqData[i];
                        final int[] xcoords = p2.getBasePositions();
                        final float[] ycoords = p2.getBaseScores();
                        final GraphSym graf = this.makeGraph(p2.getSliceInfo().getChromosome(), p2.getSliceInfo().getStrand(), xcoords, ycoords);
                        graphs.add(graf);
                    }
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return graphs;
    }
    
    private GraphSym makeGraph(final SliceInfo sliceInfo, final int[] xcoords, final float[] ycoords) {
        final String chrom = sliceInfo.getChromosome();
        final String strand = sliceInfo.getStrand();
        return this.makeGraph(chrom, strand, xcoords, ycoords);
    }
    
    private GraphSym makeGraph(final String chrom, final String strand, final int[] xcoords, final float[] ycoords) {
        final AnnotatedSeqGroup versionGenomeASG = getSeqGroup(this.archiveInfo.getVersionedGenome(), this.gmodel);
        final BioSeq chromosomeBS = determineSeq(versionGenomeASG, chrom, this.archiveInfo.getVersionedGenome());
        checkSeqLength(chromosomeBS, xcoords);
        String id = this.stream_name;
        if (!strand.equals(".")) {
            id += strand;
        }
        final GraphSym graf = new GraphSym(xcoords, ycoords, id, chromosomeBS);
        copyProps(graf, this.archiveInfo.getKeyValues());
        if (strand.equals(".")) {
            graf.setProperty("Graph Strand", GraphSym.GRAPH_STRAND_BOTH);
        }
        else if (strand.equals("+")) {
            graf.setProperty("Graph Strand", GraphSym.GRAPH_STRAND_PLUS);
        }
        else if (strand.equals("-")) {
            graf.setProperty("Graph Strand", GraphSym.GRAPH_STRAND_MINUS);
        }
        if (!graf.getProperties().containsKey("initialGraphStyle")) {
            graf.getProperties().put("initialGraphStyle", "Bar");
        }
        return graf;
    }
    
    public static void checkSeqLength(final BioSeq seq, final int[] xcoords) {
        final int xcount = xcoords.length;
        if (xcoords[xcount - 1] > seq.getLength()) {
            seq.setLength(xcoords[xcount - 1]);
        }
    }
    
    public static void copyProps(final GraphSym graf, final HashMap<String, String> tagvals) {
        for (final String tag : tagvals.keySet()) {
            final String val = tagvals.get(tag);
            graf.setProperty(tag, val);
        }
    }
    
    public static BioSeq determineSeq(final AnnotatedSeqGroup seq_group, final String chromosome, final String versionedGenome) {
        BioSeq seq = seq_group.getSeq(chromosome);
        if (seq == null) {
            final SynonymLookup lookup = SynonymLookup.getDefaultLookup();
            for (final BioSeq testseq : seq_group.getSeqList()) {
                if (lookup.isSynonym(testseq.getID(), chromosome)) {
                    seq = testseq;
                    break;
                }
            }
        }
        if (seq == null) {
            seq = seq_group.addSeq(chromosome, 1000);
        }
        return seq;
    }
    
    public static AnnotatedSeqGroup getSeqGroup(final String versionedGenome, final GenometryModel gmodel) {
        AnnotatedSeqGroup group = null;
        group = gmodel.getSeqGroup(versionedGenome + ":" + versionedGenome);
        if (group == null) {
            group = gmodel.getSeqGroup(versionedGenome);
        }
        if (group == null) {
            group = gmodel.addSeqGroup(versionedGenome);
        }
        return group;
    }
}
