// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.parsers.useq;

import com.affymetrix.genometryImpl.symmetry.GraphSym;
import com.affymetrix.genometryImpl.GenometryModel;
import com.affymetrix.genometryImpl.parsers.useq.data.RegionScoreText;
import com.affymetrix.genometryImpl.parsers.useq.data.RegionText;
import com.affymetrix.genometryImpl.parsers.BedParser;
import com.affymetrix.genometryImpl.parsers.useq.data.RegionScore;
import com.affymetrix.genometryImpl.parsers.useq.data.Region;
import com.affymetrix.genometryImpl.parsers.useq.data.PositionScoreText;
import com.affymetrix.genometryImpl.parsers.useq.data.PositionText;
import com.affymetrix.genometryImpl.parsers.useq.data.PositionScore;
import com.affymetrix.genometryImpl.symmetry.SymWithProps;
import com.affymetrix.genometryImpl.parsers.useq.data.Position;
import com.affymetrix.genometryImpl.symmetry.UcscBedSym;
import java.util.zip.ZipEntry;
import java.io.DataInputStream;
import java.io.BufferedInputStream;
import java.util.zip.ZipInputStream;
import java.io.InputStream;
import com.affymetrix.genometryImpl.parsers.useq.data.PositionScoreTextData;
import com.affymetrix.genometryImpl.parsers.useq.data.PositionTextData;
import com.affymetrix.genometryImpl.parsers.useq.data.PositionScoreData;
import com.affymetrix.genometryImpl.parsers.useq.data.PositionData;
import com.affymetrix.genometryImpl.parsers.useq.data.RegionScoreTextData;
import com.affymetrix.genometryImpl.parsers.useq.data.RegionTextData;
import com.affymetrix.genometryImpl.parsers.useq.data.RegionScoreData;
import com.affymetrix.genometryImpl.parsers.useq.data.RegionData;
import java.io.IOException;
import com.affymetrix.genometryImpl.parsers.useq.data.USeqData;
import java.util.ArrayList;
import com.affymetrix.genometryImpl.BioSeq;
import java.util.regex.Pattern;
import com.affymetrix.genometryImpl.AnnotatedSeqGroup;
import com.affymetrix.genometryImpl.symmetry.SeqSymmetry;
import java.util.List;
import com.affymetrix.genometryImpl.parsers.graph.GraphParser;

public final class USeqRegionParser implements GraphParser
{
    private List<SeqSymmetry> symlist;
    private String nameOfTrack;
    private AnnotatedSeqGroup group;
    private boolean addAnnotationsToSeq;
    private ArchiveInfo archiveInfo;
    public static final Pattern TAB;
    private boolean forwardStrand;
    private BioSeq bioSeq;
    
    public USeqRegionParser() {
        this.symlist = new ArrayList<SeqSymmetry>();
        this.nameOfTrack = null;
    }
    
    public List<SeqSymmetry> parse(final USeqArchive useqArchive, final USeqData[] useqData, final AnnotatedSeqGroup group, final String stream_name) {
        this.group = group;
        this.symlist = new ArrayList<SeqSymmetry>();
        this.nameOfTrack = stream_name;
        this.archiveInfo = useqArchive.getArchiveInfo();
        try {
            final String genomeVersion = this.archiveInfo.getVersionedGenome();
            if (group.getAllVersions().size() != 0 && !group.isSynonymous(genomeVersion)) {
                throw new IOException("\nGenome versions differ! Cannot load this useq data from " + genomeVersion + " into the current genome in view. Navigate to the correct genome and reload or add a synonym.\n");
            }
            final String dataType = useqArchive.getBinaryDataType();
            for (int i = 0; i < useqData.length; ++i) {
                if (useqData[i] != null) {
                    final SliceInfo sliceInfo = useqData[i].getSliceInfo();
                    if (sliceInfo.getStrand().equals("-")) {
                        this.forwardStrand = false;
                    }
                    else {
                        this.forwardStrand = true;
                    }
                    this.setBioSeq(sliceInfo.getChromosome());
                    if (USeqUtilities.REGION.matcher(dataType).matches()) {
                        this.parseRegionData((RegionData)useqData[i]);
                    }
                    else if (USeqUtilities.REGION_SCORE.matcher(dataType).matches()) {
                        this.parseRegionScoreData((RegionScoreData)useqData[i]);
                    }
                    else if (USeqUtilities.REGION_TEXT.matcher(dataType).matches()) {
                        this.parseRegionTextData((RegionTextData)useqData[i]);
                    }
                    else if (USeqUtilities.REGION_SCORE_TEXT.matcher(dataType).matches()) {
                        this.parseRegionScoreTextData((RegionScoreTextData)useqData[i]);
                    }
                    else if (USeqUtilities.POSITION.matcher(dataType).matches()) {
                        this.parsePositionData((PositionData)useqData[i]);
                    }
                    else if (USeqUtilities.POSITION_SCORE.matcher(dataType).matches()) {
                        this.parsePositionScoreData((PositionScoreData)useqData[i]);
                    }
                    else if (USeqUtilities.POSITION_TEXT.matcher(dataType).matches()) {
                        this.parsePositionTextData((PositionTextData)useqData[i]);
                    }
                    else {
                        if (!USeqUtilities.POSITION_SCORE_TEXT.matcher(dataType).matches()) {
                            throw new IOException("Unknown USeq data type, '" + dataType + "', for parsing region data from " + this.nameOfTrack + "\n");
                        }
                        this.parsePositionScoreTextData((PositionScoreTextData)useqData[i]);
                    }
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return this.symlist;
    }
    
    public List<SeqSymmetry> parse(final InputStream istr, final AnnotatedSeqGroup group, final String stream_name, final boolean addAnnotationsToSeq, final ArchiveInfo ai) {
        this.group = group;
        this.symlist = new ArrayList<SeqSymmetry>();
        this.nameOfTrack = stream_name;
        this.addAnnotationsToSeq = addAnnotationsToSeq;
        this.archiveInfo = ai;
        BufferedInputStream bis = null;
        ZipInputStream zis = null;
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
        this.parse(zis);
        return this.symlist;
    }
    
    private void parse(final ZipInputStream zis) {
        final DataInputStream dis = new DataInputStream(zis);
        try {
            if (this.archiveInfo == null) {
                zis.getNextEntry();
                this.archiveInfo = new ArchiveInfo(zis, false);
            }
            final String genomeVersion = this.archiveInfo.getVersionedGenome();
            if (this.group.getAllVersions().size() != 0 && !this.group.isSynonymous(genomeVersion)) {
                throw new IOException("\nGenome versions differ! Cannot load this useq data from " + genomeVersion + " into the current genome in view. Navigate to the correct genome and reload or add a synonym.\n");
            }
            ZipEntry ze;
            while ((ze = zis.getNextEntry()) != null) {
                final SliceInfo sliceInfo = new SliceInfo(ze.getName());
                final String dataType = sliceInfo.getBinaryType();
                if (sliceInfo.getStrand().equals("-")) {
                    this.forwardStrand = false;
                }
                else {
                    this.forwardStrand = true;
                }
                this.setBioSeq(sliceInfo.getChromosome());
                if (USeqUtilities.REGION.matcher(dataType).matches()) {
                    this.parseRegionData(new RegionData(dis, sliceInfo));
                }
                else if (USeqUtilities.REGION_SCORE.matcher(dataType).matches()) {
                    this.parseRegionScoreData(new RegionScoreData(dis, sliceInfo));
                }
                else if (USeqUtilities.REGION_TEXT.matcher(dataType).matches()) {
                    this.parseRegionTextData(new RegionTextData(dis, sliceInfo));
                }
                else if (USeqUtilities.REGION_SCORE_TEXT.matcher(dataType).matches()) {
                    this.parseRegionScoreTextData(new RegionScoreTextData(dis, sliceInfo));
                }
                else if (USeqUtilities.POSITION.matcher(dataType).matches()) {
                    this.parsePositionData(new PositionData(dis, sliceInfo));
                }
                else if (USeqUtilities.POSITION_SCORE.matcher(dataType).matches()) {
                    this.parsePositionScoreData(new PositionScoreData(dis, sliceInfo));
                }
                else if (USeqUtilities.POSITION_TEXT.matcher(dataType).matches()) {
                    this.parsePositionTextData(new PositionTextData(dis, sliceInfo));
                }
                else {
                    if (!USeqUtilities.POSITION_SCORE_TEXT.matcher(dataType).matches()) {
                        throw new IOException("Unknown USeq data type, '" + dataType + "', for parsing region data from  -> '" + ze.getName() + "' in " + this.nameOfTrack + "\n");
                    }
                    this.parsePositionScoreTextData(new PositionScoreTextData(dis, sliceInfo));
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            USeqUtilities.safeClose(dis);
            USeqUtilities.safeClose(zis);
        }
    }
    
    private void parsePositionData(final PositionData pd) {
        final Position[] r = pd.getPositions();
        final float score = Float.NEGATIVE_INFINITY;
        for (int i = 0; i < r.length; ++i) {
            final int start = r[i].getPosition();
            final SymWithProps bedline_sym = new UcscBedSym(this.nameOfTrack, this.bioSeq, start, start + 1, null, score, this.forwardStrand, Integer.MIN_VALUE, Integer.MIN_VALUE, new int[] { start }, new int[] { start + 1 });
            this.symlist.add(bedline_sym);
            if (this.addAnnotationsToSeq) {
                this.bioSeq.addAnnotation(bedline_sym);
            }
        }
        if (r[r.length - 1].getPosition() + 1 > this.bioSeq.getLength()) {
            this.bioSeq.setLength(r[r.length - 1].getPosition() + 1);
        }
    }
    
    private void parsePositionScoreData(final PositionScoreData pd) {
        final PositionScore[] r = pd.getPositionScores();
        for (int i = 0; i < r.length; ++i) {
            final int start = r[i].getPosition();
            final SymWithProps bedline_sym = new UcscBedSym(this.nameOfTrack, this.bioSeq, start, start + 1, null, r[i].getScore(), this.forwardStrand, Integer.MIN_VALUE, Integer.MIN_VALUE, new int[] { start }, new int[] { start + 1 });
            this.symlist.add(bedline_sym);
            if (this.addAnnotationsToSeq) {
                this.bioSeq.addAnnotation(bedline_sym);
            }
        }
        if (r[r.length - 1].getPosition() + 1 > this.bioSeq.getLength()) {
            this.bioSeq.setLength(r[r.length - 1].getPosition() + 1);
        }
    }
    
    private void parsePositionTextData(final PositionTextData pd) {
        final PositionText[] r = pd.getPositionTexts();
        final float score = Float.NEGATIVE_INFINITY;
        for (int i = 0; i < r.length; ++i) {
            final int start = r[i].getPosition();
            final SymWithProps bedline_sym = new UcscBedSym(this.nameOfTrack, this.bioSeq, start, start + 1, r[i].getText(), score, this.forwardStrand, Integer.MIN_VALUE, Integer.MIN_VALUE, new int[] { start }, new int[] { start + 1 });
            this.symlist.add(bedline_sym);
            if (this.addAnnotationsToSeq) {
                this.bioSeq.addAnnotation(bedline_sym);
            }
        }
        if (r[r.length - 1].getPosition() + 1 > this.bioSeq.getLength()) {
            this.bioSeq.setLength(r[r.length - 1].getPosition() + 1);
        }
    }
    
    private void parsePositionScoreTextData(final PositionScoreTextData pd) {
        final PositionScoreText[] r = pd.getPositionScoreTexts();
        for (int i = 0; i < r.length; ++i) {
            final int start = r[i].getPosition();
            final SymWithProps bedline_sym = new UcscBedSym(this.nameOfTrack, this.bioSeq, start, start + 1, r[i].getText(), r[i].getScore(), this.forwardStrand, Integer.MIN_VALUE, Integer.MIN_VALUE, new int[] { start }, new int[] { start + 1 });
            this.symlist.add(bedline_sym);
            if (this.addAnnotationsToSeq) {
                this.bioSeq.addAnnotation(bedline_sym);
            }
        }
        if (r[r.length - 1].getPosition() + 1 > this.bioSeq.getLength()) {
            this.bioSeq.setLength(r[r.length - 1].getPosition() + 1);
        }
    }
    
    private void parseRegionData(final RegionData pd) {
        final Region[] r = pd.getRegions();
        final float score = Float.NEGATIVE_INFINITY;
        for (int i = 0; i < r.length; ++i) {
            final SymWithProps bedline_sym = new UcscBedSym(this.nameOfTrack, this.bioSeq, r[i].getStart(), r[i].getStop(), null, score, this.forwardStrand, Integer.MIN_VALUE, Integer.MIN_VALUE, new int[] { r[i].getStart() }, new int[] { r[i].getStop() });
            this.symlist.add(bedline_sym);
            if (this.addAnnotationsToSeq) {
                this.bioSeq.addAnnotation(bedline_sym);
            }
        }
        if (r[r.length - 1].getStop() > this.bioSeq.getLength()) {
            this.bioSeq.setLength(r[r.length - 1].getStop());
        }
    }
    
    private void parseRegionScoreData(final RegionScoreData pd) {
        final RegionScore[] r = pd.getRegionScores();
        for (int i = 0; i < r.length; ++i) {
            final SymWithProps bedline_sym = new UcscBedSym(this.nameOfTrack, this.bioSeq, r[i].getStart(), r[i].getStop(), null, r[i].getScore(), this.forwardStrand, Integer.MIN_VALUE, Integer.MIN_VALUE, new int[] { r[i].getStart() }, new int[] { r[i].getStop() });
            this.symlist.add(bedline_sym);
            if (this.addAnnotationsToSeq) {
                this.bioSeq.addAnnotation(bedline_sym);
            }
        }
        if (r[r.length - 1].getStop() > this.bioSeq.getLength()) {
            this.bioSeq.setLength(r[r.length - 1].getStop());
        }
    }
    
    private void parseRegionTextData(final RegionTextData pd) {
        final RegionText[] r = pd.getRegionTexts();
        final float score = Float.NEGATIVE_INFINITY;
        for (int i = 0; i < r.length; ++i) {
            final String[] tokens = USeqRegionParser.TAB.split(r[i].getText());
            SymWithProps bedline_sym;
            if (tokens.length == 7) {
                final int min = r[i].getStart();
                final int max = r[i].getStop();
                final String annot_name = tokens[0];
                final int thick_min = Integer.parseInt(tokens[1]);
                final int thick_max = Integer.parseInt(tokens[2]);
                final int blockCount = Integer.parseInt(tokens[4]);
                final int[] blockSizes = BedParser.parseIntArray(tokens[5]);
                if (blockCount != blockSizes.length) {
                    System.out.println("WARNING: block count does not agree with block sizes.  Ignoring " + annot_name + " on " + this.bioSeq);
                    return;
                }
                final int[] blockStarts = BedParser.parseIntArray(tokens[6]);
                if (blockCount != blockStarts.length) {
                    System.out.println("WARNING: block size does not agree with block starts.  Ignoring " + annot_name + " on " + this.bioSeq);
                    return;
                }
                final int[] blockMins = BedParser.makeBlockMins(min, blockStarts);
                final int[] blockMaxs = BedParser.makeBlockMaxs(blockSizes, blockMins);
                bedline_sym = new UcscBedSym(this.nameOfTrack, this.bioSeq, min, max, annot_name, score, this.forwardStrand, thick_min, thick_max, blockMins, blockMaxs);
            }
            else {
                bedline_sym = new UcscBedSym(this.nameOfTrack, this.bioSeq, r[i].getStart(), r[i].getStop(), r[i].getText(), score, this.forwardStrand, Integer.MIN_VALUE, Integer.MIN_VALUE, new int[] { r[i].getStart() }, new int[] { r[i].getStop() });
            }
            this.symlist.add(bedline_sym);
            if (this.addAnnotationsToSeq) {
                this.bioSeq.addAnnotation(bedline_sym);
            }
        }
        if (r[r.length - 1].getStop() > this.bioSeq.getLength()) {
            this.bioSeq.setLength(r[r.length - 1].getStop());
        }
    }
    
    private void parseRegionScoreTextData(final RegionScoreTextData pd) {
        final RegionScoreText[] r = pd.getRegionScoreTexts();
        for (int i = 0; i < r.length; ++i) {
            final String[] tokens = USeqRegionParser.TAB.split(r[i].getText());
            SymWithProps bedline_sym;
            if (tokens.length == 7) {
                final int min = r[i].getStart();
                final int max = r[i].getStop();
                final String annot_name = tokens[0];
                final int thick_min = Integer.parseInt(tokens[1]);
                final int thick_max = Integer.parseInt(tokens[2]);
                final int blockCount = Integer.parseInt(tokens[4]);
                final int[] blockSizes = BedParser.parseIntArray(tokens[5]);
                if (blockCount != blockSizes.length) {
                    System.out.println("WARNING: block count does not agree with block sizes.  Ignoring " + annot_name + " on " + this.bioSeq);
                    return;
                }
                final int[] blockStarts = BedParser.parseIntArray(tokens[6]);
                if (blockCount != blockStarts.length) {
                    System.out.println("WARNING: block size does not agree with block starts.  Ignoring " + annot_name + " on " + this.bioSeq);
                    return;
                }
                final int[] blockMins = BedParser.makeBlockMins(min, blockStarts);
                final int[] blockMaxs = BedParser.makeBlockMaxs(blockSizes, blockMins);
                bedline_sym = new UcscBedSym(this.nameOfTrack, this.bioSeq, min, max, annot_name, r[i].getScore(), this.forwardStrand, thick_min, thick_max, blockMins, blockMaxs);
            }
            else {
                bedline_sym = new UcscBedSym(this.nameOfTrack, this.bioSeq, r[i].getStart(), r[i].getStop(), r[i].getText(), r[i].getScore(), this.forwardStrand, Integer.MIN_VALUE, Integer.MIN_VALUE, new int[] { r[i].getStart() }, new int[] { r[i].getStop() });
            }
            this.symlist.add(bedline_sym);
            if (this.addAnnotationsToSeq) {
                this.bioSeq.addAnnotation(bedline_sym);
            }
        }
        if (r[r.length - 1].getStop() > this.bioSeq.getLength()) {
            this.bioSeq.setLength(r[r.length - 1].getStop());
        }
    }
    
    private void setBioSeq(final String chromosome) {
        this.bioSeq = this.group.getSeq(chromosome);
        if (this.bioSeq == null) {
            this.bioSeq = this.group.addSeq(chromosome, 0);
        }
    }
    
    @Override
    public List<? extends SeqSymmetry> parse(final InputStream is, final AnnotatedSeqGroup group, final String nameType, final String uri, final boolean annotate_seq) throws Exception {
        if (annotate_seq) {
            return this.parse(is, group, uri, true, null);
        }
        final ZipInputStream zis = new ZipInputStream(is);
        zis.getNextEntry();
        final ArchiveInfo archiveInfo = new ArchiveInfo(zis, false);
        if (archiveInfo.getDataType().equals("graph")) {
            final USeqGraphParser gp = new USeqGraphParser();
            return gp.parseGraphSyms(zis, GenometryModel.getGenometryModel(), uri, archiveInfo);
        }
        return this.parse(zis, group, uri, false, archiveInfo);
    }
    
    @Override
    public List<GraphSym> readGraphs(final InputStream istr, final String stream_name, final AnnotatedSeqGroup seq_group, final BioSeq seq) throws IOException {
        return new USeqGraphParser().parseGraphSyms(istr, GenometryModel.getGenometryModel(), stream_name, null);
    }
    
    @Override
    public void writeGraphFile(final GraphSym gsym, final AnnotatedSeqGroup seq_group, final String file_name) throws IOException {
    }
    
    static {
        TAB = Pattern.compile("\\t");
    }
}
