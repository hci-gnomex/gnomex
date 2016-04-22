// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.parsers.useq.apps;

import java.util.regex.Matcher;
import java.io.Writer;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Arrays;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.FileReader;
import com.affymetrix.genometryImpl.parsers.useq.data.PositionScore;
import com.affymetrix.genometryImpl.parsers.useq.data.PositionScoreData;
import com.affymetrix.genometryImpl.parsers.useq.data.PositionScoreTextData;
import com.affymetrix.genometryImpl.parsers.useq.data.PositionScoreText;
import com.affymetrix.genometryImpl.parsers.useq.data.PositionTextData;
import com.affymetrix.genometryImpl.parsers.useq.data.PositionText;
import com.affymetrix.genometryImpl.parsers.useq.data.PositionData;
import com.affymetrix.genometryImpl.parsers.useq.data.Position;
import com.affymetrix.genometryImpl.parsers.useq.data.RegionTextData;
import com.affymetrix.genometryImpl.parsers.useq.data.RegionText;
import com.affymetrix.genometryImpl.parsers.useq.data.RegionScoreTextData;
import com.affymetrix.genometryImpl.parsers.useq.data.RegionScoreText;
import com.affymetrix.genometryImpl.parsers.useq.data.RegionScoreData;
import com.affymetrix.genometryImpl.parsers.useq.data.RegionScore;
import java.util.Iterator;
import com.affymetrix.genometryImpl.parsers.useq.data.RegionData;
import com.affymetrix.genometryImpl.parsers.useq.data.Region;
import com.affymetrix.genometryImpl.parsers.useq.SliceInfo;
import java.io.IOException;
import com.affymetrix.genometryImpl.parsers.useq.ArchiveInfo;
import com.affymetrix.genometryImpl.parsers.useq.USeqUtilities;
import com.affymetrix.genometryImpl.parsers.useq.USeqArchive;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.File;

public class Text2USeq
{
    private int chromosomeColumnIndex;
    private int strandColumnIndex;
    private int beginningColumnIndex;
    private int endingColumnIndex;
    private int[] textColumnIndexs;
    private int scoreColumnIndex;
    private int rowChunkSize;
    private File[] inputFiles;
    private String versionedGenome;
    private boolean makeGraph;
    private int graphStyle;
    private String color;
    private String description;
    public static String[] GRAPH_STYLES;
    private File tempSplitTextDirectory;
    private int maxIndex;
    private File[] outputDirectories;
    private File workingBinarySaveDirectory;
    private HashMap<String, File> chromStrandFileHash;
    private ArrayList<File> files2Zip;
    public static final Pattern PATTERN_TAB;
    public static final Pattern PATTERN_STRAND;
    
    public Text2USeq(final String[] args) {
        this.chromosomeColumnIndex = -1;
        this.strandColumnIndex = -1;
        this.beginningColumnIndex = -1;
        this.endingColumnIndex = -1;
        this.textColumnIndexs = null;
        this.scoreColumnIndex = -1;
        this.rowChunkSize = 10000;
        this.versionedGenome = null;
        this.makeGraph = true;
        this.graphStyle = 0;
        this.color = null;
        this.description = null;
        this.tempSplitTextDirectory = null;
        this.files2Zip = new ArrayList<File>();
        final long startTime = System.currentTimeMillis();
        this.processArgs(args);
        for (int i = 0; i < this.inputFiles.length; ++i) {
            System.out.println("Processing " + this.inputFiles[i]);
            System.out.println("\tSplitting by chromosome and possibly strand...");
            (this.tempSplitTextDirectory = new File(this.inputFiles[i].getParentFile(), "TempDir" + USeqArchive.createRandowWord(7))).mkdir();
            this.chromStrandFileHash = splitFileByChromosomeAndStrand(this.inputFiles[i], this.tempSplitTextDirectory, this.chromosomeColumnIndex, this.strandColumnIndex, true);
            if (this.chromStrandFileHash == null || this.chromStrandFileHash.size() == 0) {
                System.err.println("\nFailed to parse genomic data text file, aborting!\n");
            }
            else if (this.strandBad()) {
                USeqUtilities.deleteDirectory(this.tempSplitTextDirectory);
                System.err.println("\nError: convert your strand information to +, -, or .  Skipping useq conversion.");
            }
            else {
                this.outputDirectories[i] = USeqUtilities.makeDirectory(this.inputFiles[i], ".TempDelMe");
                this.workingBinarySaveDirectory = this.outputDirectories[i];
                this.files2Zip.clear();
                this.writeReadMeTxt(this.inputFiles[i]);
                System.out.println("\tParsing, slicing, and writing binary data...");
                if (!this.sliceWriteSplitData()) {
                    USeqUtilities.deleteDirectory(this.tempSplitTextDirectory);
                    USeqUtilities.deleteDirectory(this.workingBinarySaveDirectory);
                    USeqUtilities.printErrAndExit("\nFailed to convert split data to binary, aborting!\n");
                }
                System.out.println("\tZipping...");
                final String zipName = USeqUtilities.removeExtension(this.workingBinarySaveDirectory.getName()) + ".useq";
                final File zipFile = new File(this.inputFiles[i].getParentFile(), zipName);
                final File[] files = new File[this.files2Zip.size()];
                this.files2Zip.toArray(files);
                USeqUtilities.zip(files, zipFile);
                USeqUtilities.deleteDirectory(this.workingBinarySaveDirectory);
                USeqUtilities.deleteDirectory(this.tempSplitTextDirectory);
            }
        }
        final double diffTime = (System.currentTimeMillis() - startTime) / 1000.0;
        System.out.println("\nDone! " + Math.round(diffTime) + " seconds\n");
    }
    
    private boolean strandBad() {
        if (this.strandColumnIndex == -1) {
            return false;
        }
        final String name = this.chromStrandFileHash.keySet().iterator().next();
        return !Text2USeq.PATTERN_STRAND.matcher(name).matches();
    }
    
    private void writeReadMeTxt(final File sourceFile) {
        try {
            final ArchiveInfo ai = new ArchiveInfo(this.versionedGenome, null);
            if (this.makeGraph) {
                ai.setDataType("graph");
                ai.setInitialGraphStyle(Text2USeq.GRAPH_STYLES[this.graphStyle]);
            }
            else {
                ai.setDataType("region");
            }
            ai.setOriginatingDataSource(sourceFile.toString());
            if (this.color != null) {
                ai.setInitialColor(this.color);
            }
            if (this.description != null) {
                ai.setDescription(this.description);
            }
            final File readme = ai.writeReadMeFile(this.workingBinarySaveDirectory);
            this.files2Zip.add(readme);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private boolean sliceWriteSplitData() {
        try {
            if (this.endingColumnIndex == -1) {
                if (this.scoreColumnIndex == -1) {
                    if (this.textColumnIndexs == null) {
                        this.sliceWritePositionData();
                    }
                    else {
                        this.sliceWritePositionTextData();
                    }
                }
                else if (this.textColumnIndexs == null) {
                    this.sliceWritePositionScoreData();
                }
                else {
                    this.sliceWritePositionScoreTextData();
                }
            }
            else if (this.scoreColumnIndex == -1) {
                if (this.textColumnIndexs == null) {
                    this.sliceWriteRegionData();
                }
                else {
                    this.sliceWriteRegionTextData();
                }
            }
            else if (this.textColumnIndexs == null) {
                this.sliceWriteRegionScoreData();
            }
            else {
                this.sliceWriteRegionScoreTextData();
            }
        }
        catch (Exception e) {
            System.err.println("Error slicing and writing data!");
            e.printStackTrace();
            return false;
        }
        return true;
    }
    
    private void sliceWriteRegionData() throws Exception {
        for (final String chromStrand : this.chromStrandFileHash.keySet()) {
            final String chromosome = chromStrand.substring(0, chromStrand.length() - 1);
            final String strand = chromStrand.substring(chromStrand.length() - 1);
            final SliceInfo sliceInfo = new SliceInfo(chromosome, strand, 0, 0, 0, null);
            int beginningIndex = 0;
            int endIndex = 0;
            final Region[] reg = this.makeRegions(this.chromStrandFileHash.get(chromStrand));
            if (!Region.checkStartStops(reg)) {
                throw new Exception("\nError: one or more of your stop coordinates is less than your start coordinate.  Start must always be less than or equal to Stop.\n");
            }
            final int numberReg = reg.length;
            do {
                Region[] slice;
                if (this.rowChunkSize == -1) {
                    beginningIndex = 0;
                    endIndex = numberReg;
                    slice = reg;
                }
                else {
                    beginningIndex = endIndex;
                    endIndex = beginningIndex + this.rowChunkSize;
                    if (endIndex > numberReg) {
                        endIndex = numberReg;
                    }
                    else {
                        final int endBP = reg[endIndex - 1].getStart();
                        for (int i = endIndex; i < numberReg; ++i) {
                            if (reg[i].getStart() != endBP) {
                                break;
                            }
                            ++endIndex;
                        }
                    }
                    final int num = endIndex - beginningIndex;
                    slice = new Region[num];
                    System.arraycopy(reg, beginningIndex, slice, 0, num);
                }
                RegionData.updateSliceInfo(slice, sliceInfo);
                final RegionData rd = new RegionData(slice, sliceInfo);
                final File savedFile = rd.write(this.workingBinarySaveDirectory, true);
                this.files2Zip.add(savedFile);
            } while (endIndex != numberReg);
        }
    }
    
    private void sliceWriteRegionScoreData() throws Exception {
        for (final String chromStrand : this.chromStrandFileHash.keySet()) {
            final String chromosome = chromStrand.substring(0, chromStrand.length() - 1);
            final String strand = chromStrand.substring(chromStrand.length() - 1);
            final SliceInfo sliceInfo = new SliceInfo(chromosome, strand, 0, 0, 0, null);
            int beginningIndex = 0;
            int endIndex = 0;
            final RegionScore[] reg = this.makeRegionScores(this.chromStrandFileHash.get(chromStrand));
            if (!Region.checkStartStops(reg)) {
                throw new Exception("\nError: one or more of your stop coordinates is less than your start coordinate.  Start must always be less than or equal to Stop.\n");
            }
            final int numberReg = reg.length;
            do {
                RegionScore[] slice;
                if (this.rowChunkSize == -1) {
                    beginningIndex = 0;
                    endIndex = numberReg;
                    slice = reg;
                }
                else {
                    beginningIndex = endIndex;
                    endIndex = beginningIndex + this.rowChunkSize;
                    if (endIndex > numberReg) {
                        endIndex = numberReg;
                    }
                    else {
                        final int endBP = reg[endIndex - 1].getStart();
                        for (int i = endIndex; i < numberReg; ++i) {
                            if (reg[i].getStart() != endBP) {
                                break;
                            }
                            ++endIndex;
                        }
                    }
                    final int num = endIndex - beginningIndex;
                    slice = new RegionScore[num];
                    System.arraycopy(reg, beginningIndex, slice, 0, num);
                }
                RegionScoreData.updateSliceInfo(slice, sliceInfo);
                final RegionScoreData rd = new RegionScoreData(slice, sliceInfo);
                final File savedFile = rd.write(this.workingBinarySaveDirectory, true);
                this.files2Zip.add(savedFile);
            } while (endIndex != numberReg);
        }
    }
    
    private void sliceWriteRegionScoreTextData() throws Exception {
        for (final String chromStrand : this.chromStrandFileHash.keySet()) {
            final String chromosome = chromStrand.substring(0, chromStrand.length() - 1);
            final String strand = chromStrand.substring(chromStrand.length() - 1);
            final SliceInfo sliceInfo = new SliceInfo(chromosome, strand, 0, 0, 0, null);
            int beginningIndex = 0;
            int endIndex = 0;
            final RegionScoreText[] reg = this.makeRegionScoreTexts(this.chromStrandFileHash.get(chromStrand));
            if (!Region.checkStartStops(reg)) {
                throw new Exception("\nError: one or more of your stop coordinates is less than your start coordinate.  Start must always be less than or equal to Stop.\n");
            }
            final int numberReg = reg.length;
            do {
                RegionScoreText[] slice;
                if (this.rowChunkSize == -1) {
                    beginningIndex = 0;
                    endIndex = numberReg;
                    slice = reg;
                }
                else {
                    beginningIndex = endIndex;
                    endIndex = beginningIndex + this.rowChunkSize;
                    if (endIndex > numberReg) {
                        endIndex = numberReg;
                    }
                    else {
                        final int endBP = reg[endIndex - 1].getStart();
                        for (int i = endIndex; i < numberReg; ++i) {
                            if (reg[i].getStart() != endBP) {
                                break;
                            }
                            ++endIndex;
                        }
                    }
                    final int num = endIndex - beginningIndex;
                    slice = new RegionScoreText[num];
                    System.arraycopy(reg, beginningIndex, slice, 0, num);
                }
                RegionScoreTextData.updateSliceInfo(slice, sliceInfo);
                final RegionScoreTextData rd = new RegionScoreTextData(slice, sliceInfo);
                final File savedFile = rd.write(this.workingBinarySaveDirectory, true);
                this.files2Zip.add(savedFile);
            } while (endIndex != numberReg);
        }
    }
    
    private void sliceWriteRegionTextData() throws Exception {
        for (final String chromStrand : this.chromStrandFileHash.keySet()) {
            final String chromosome = chromStrand.substring(0, chromStrand.length() - 1);
            final String strand = chromStrand.substring(chromStrand.length() - 1);
            final SliceInfo sliceInfo = new SliceInfo(chromosome, strand, 0, 0, 0, null);
            int beginningIndex = 0;
            int endIndex = 0;
            final RegionText[] reg = this.makeRegionTexts(this.chromStrandFileHash.get(chromStrand));
            if (!Region.checkStartStops(reg)) {
                throw new Exception("\nError: one or more of your stop coordinates is less than your start coordinate.  Start must always be less than or equal to Stop.\n");
            }
            final int numberReg = reg.length;
            do {
                RegionText[] slice;
                if (this.rowChunkSize == -1) {
                    beginningIndex = 0;
                    endIndex = numberReg;
                    slice = reg;
                }
                else {
                    beginningIndex = endIndex;
                    endIndex = beginningIndex + this.rowChunkSize;
                    if (endIndex > numberReg) {
                        endIndex = numberReg;
                    }
                    else {
                        final int endBP = reg[endIndex - 1].getStart();
                        for (int i = endIndex; i < numberReg; ++i) {
                            if (reg[i].getStart() != endBP) {
                                break;
                            }
                            ++endIndex;
                        }
                    }
                    final int num = endIndex - beginningIndex;
                    slice = new RegionText[num];
                    System.arraycopy(reg, beginningIndex, slice, 0, num);
                }
                RegionTextData.updateSliceInfo(slice, sliceInfo);
                final RegionTextData rd = new RegionTextData(slice, sliceInfo);
                final File savedFile = rd.write(this.workingBinarySaveDirectory, true);
                this.files2Zip.add(savedFile);
            } while (endIndex != numberReg);
        }
    }
    
    private void sliceWritePositionData() throws Exception {
        for (final String chromStrand : this.chromStrandFileHash.keySet()) {
            final String chromosome = chromStrand.substring(0, chromStrand.length() - 1);
            final String strand = chromStrand.substring(chromStrand.length() - 1);
            final SliceInfo sliceInfo = new SliceInfo(chromosome, strand, 0, 0, 0, null);
            int beginningIndex = 0;
            int endIndex = 0;
            final Position[] positions = this.makePositions(this.chromStrandFileHash.get(chromStrand));
            final int numberPositions = positions.length;
            do {
                Position[] slice;
                if (this.rowChunkSize == -1) {
                    beginningIndex = 0;
                    endIndex = numberPositions;
                    slice = positions;
                }
                else {
                    beginningIndex = endIndex;
                    endIndex = beginningIndex + this.rowChunkSize;
                    if (endIndex > numberPositions) {
                        endIndex = numberPositions;
                    }
                    else {
                        final int endBP = positions[endIndex - 1].getPosition();
                        for (int i = endIndex; i < numberPositions; ++i) {
                            if (positions[i].getPosition() != endBP) {
                                break;
                            }
                            ++endIndex;
                        }
                    }
                    final int num = endIndex - beginningIndex;
                    slice = new Position[num];
                    System.arraycopy(positions, beginningIndex, slice, 0, num);
                }
                PositionData.updateSliceInfo(slice, sliceInfo);
                final PositionData pd = new PositionData(slice, sliceInfo);
                final File savedFile = pd.write(this.workingBinarySaveDirectory, true);
                this.files2Zip.add(savedFile);
            } while (endIndex != numberPositions);
        }
    }
    
    private void sliceWritePositionTextData() throws Exception {
        for (final String chromStrand : this.chromStrandFileHash.keySet()) {
            final String chromosome = chromStrand.substring(0, chromStrand.length() - 1);
            final String strand = chromStrand.substring(chromStrand.length() - 1);
            final SliceInfo sliceInfo = new SliceInfo(chromosome, strand, 0, 0, 0, null);
            int beginningIndex = 0;
            int endIndex = 0;
            final PositionText[] positions = this.makePositionTexts(this.chromStrandFileHash.get(chromStrand));
            final int numberPositions = positions.length;
            do {
                PositionText[] slice;
                if (this.rowChunkSize == -1) {
                    beginningIndex = 0;
                    endIndex = numberPositions;
                    slice = positions;
                }
                else {
                    beginningIndex = endIndex;
                    endIndex = beginningIndex + this.rowChunkSize;
                    if (endIndex > numberPositions) {
                        endIndex = numberPositions;
                    }
                    else {
                        final int endBP = positions[endIndex - 1].getPosition();
                        for (int i = endIndex; i < numberPositions; ++i) {
                            if (positions[i].getPosition() != endBP) {
                                break;
                            }
                            ++endIndex;
                        }
                    }
                    final int num = endIndex - beginningIndex;
                    slice = new PositionText[num];
                    System.arraycopy(positions, beginningIndex, slice, 0, num);
                }
                PositionTextData.updateSliceInfo(slice, sliceInfo);
                final PositionTextData pd = new PositionTextData(slice, sliceInfo);
                final File savedFile = pd.write(this.workingBinarySaveDirectory, true);
                this.files2Zip.add(savedFile);
            } while (endIndex != numberPositions);
        }
    }
    
    private void sliceWritePositionScoreTextData() throws Exception {
        for (final String chromStrand : this.chromStrandFileHash.keySet()) {
            final String chromosome = chromStrand.substring(0, chromStrand.length() - 1);
            final String strand = chromStrand.substring(chromStrand.length() - 1);
            final SliceInfo sliceInfo = new SliceInfo(chromosome, strand, 0, 0, 0, null);
            int beginningIndex = 0;
            int endIndex = 0;
            final PositionScoreText[] positions = this.makePositionScoreTexts(this.chromStrandFileHash.get(chromStrand));
            final int numberPositions = positions.length;
            do {
                PositionScoreText[] slice;
                if (this.rowChunkSize == -1) {
                    beginningIndex = 0;
                    endIndex = numberPositions;
                    slice = positions;
                }
                else {
                    beginningIndex = endIndex;
                    endIndex = beginningIndex + this.rowChunkSize;
                    if (endIndex > numberPositions) {
                        endIndex = numberPositions;
                    }
                    else {
                        final int endBP = positions[endIndex - 1].getPosition();
                        for (int i = endIndex; i < numberPositions; ++i) {
                            if (positions[i].getPosition() != endBP) {
                                break;
                            }
                            ++endIndex;
                        }
                    }
                    final int num = endIndex - beginningIndex;
                    slice = new PositionScoreText[num];
                    System.arraycopy(positions, beginningIndex, slice, 0, num);
                }
                PositionScoreTextData.updateSliceInfo(slice, sliceInfo);
                final PositionScoreTextData pd = new PositionScoreTextData(slice, sliceInfo);
                final File savedFile = pd.write(this.workingBinarySaveDirectory, true);
                this.files2Zip.add(savedFile);
            } while (endIndex != numberPositions);
        }
    }
    
    private void sliceWritePositionScoreData() throws Exception {
        for (final String chromStrand : this.chromStrandFileHash.keySet()) {
            final String chromosome = chromStrand.substring(0, chromStrand.length() - 1);
            final String strand = chromStrand.substring(chromStrand.length() - 1);
            final SliceInfo sliceInfo = new SliceInfo(chromosome, strand, 0, 0, 0, null);
            final PositionScore[] positions = this.makePositionScores(this.chromStrandFileHash.get(chromStrand));
            final PositionScoreData psd = new PositionScoreData(positions, sliceInfo);
            psd.sliceWritePositionScoreData(this.rowChunkSize, this.workingBinarySaveDirectory, this.files2Zip);
        }
    }
    
    private Position[] makePositions(final File file) {
        final ArrayList<Position> al = new ArrayList<Position>();
        String[] tokens = null;
        String line = null;
        try {
            final BufferedReader in = new BufferedReader(new FileReader(file));
            while ((line = in.readLine()) != null) {
                tokens = Text2USeq.PATTERN_TAB.split(line);
                al.add(new Position(Integer.parseInt(tokens[this.beginningColumnIndex])));
            }
            in.close();
            final Position[] d = new Position[al.size()];
            al.toArray(d);
            Arrays.sort(d);
            return d;
        }
        catch (Exception e) {
            System.out.println("Could not parse an int value from '" + tokens[this.endingColumnIndex] + "', malformed line -> " + line);
            e.printStackTrace();
            return null;
        }
    }
    
    private PositionScore[] makePositionScores(final File file) {
        final ArrayList<PositionScore> al = new ArrayList<PositionScore>();
        String[] tokens = null;
        String line = null;
        try {
            final BufferedReader in = new BufferedReader(new FileReader(file));
            while ((line = in.readLine()) != null) {
                tokens = Text2USeq.PATTERN_TAB.split(line);
                al.add(new PositionScore(Integer.parseInt(tokens[this.beginningColumnIndex]), Float.parseFloat(tokens[this.scoreColumnIndex])));
            }
            in.close();
            final PositionScore[] d = new PositionScore[al.size()];
            al.toArray(d);
            Arrays.sort(d);
            return d;
        }
        catch (Exception e) {
            System.out.println("Could not parse an int or float value from malformed line -> " + line);
            e.printStackTrace();
            return null;
        }
    }
    
    private PositionText[] makePositionTexts(final File file) {
        final ArrayList<PositionText> al = new ArrayList<PositionText>();
        String[] tokens = null;
        String line = null;
        try {
            final BufferedReader in = new BufferedReader(new FileReader(file));
            while ((line = in.readLine()) != null) {
                tokens = Text2USeq.PATTERN_TAB.split(line);
                al.add(new PositionText(Integer.parseInt(tokens[this.beginningColumnIndex]), this.concatinateTextColumns(tokens)));
            }
            in.close();
            final PositionText[] d = new PositionText[al.size()];
            al.toArray(d);
            Arrays.sort(d);
            return d;
        }
        catch (Exception e) {
            System.out.println("Could not parse an int or float value from malformed line -> " + line);
            e.printStackTrace();
            return null;
        }
    }
    
    private PositionScoreText[] makePositionScoreTexts(final File file) {
        final ArrayList<PositionScoreText> al = new ArrayList<PositionScoreText>();
        String[] tokens = null;
        String line = null;
        try {
            final BufferedReader in = new BufferedReader(new FileReader(file));
            while ((line = in.readLine()) != null) {
                tokens = Text2USeq.PATTERN_TAB.split(line);
                al.add(new PositionScoreText(Integer.parseInt(tokens[this.beginningColumnIndex]), Float.parseFloat(tokens[this.scoreColumnIndex]), this.concatinateTextColumns(tokens)));
            }
            in.close();
            final PositionScoreText[] d = new PositionScoreText[al.size()];
            al.toArray(d);
            Arrays.sort(d);
            return d;
        }
        catch (Exception e) {
            System.out.println("Could not parse an int or float value from malformed line -> " + line);
            e.printStackTrace();
            return null;
        }
    }
    
    private Region[] makeRegions(final File file) {
        final ArrayList<Region> al = new ArrayList<Region>();
        String[] tokens = null;
        String line = null;
        try {
            final BufferedReader in = new BufferedReader(new FileReader(file));
            while ((line = in.readLine()) != null) {
                tokens = Text2USeq.PATTERN_TAB.split(line);
                al.add(new Region(Integer.parseInt(tokens[this.beginningColumnIndex]), Integer.parseInt(tokens[this.endingColumnIndex])));
            }
            in.close();
            final Region[] d = new Region[al.size()];
            al.toArray(d);
            Arrays.sort(d);
            return d;
        }
        catch (Exception e) {
            System.out.println("Could not parse an int value from '" + tokens[this.endingColumnIndex] + "', malformed line -> " + line);
            e.printStackTrace();
            return null;
        }
    }
    
    private RegionScore[] makeRegionScores(final File file) {
        final ArrayList<RegionScore> al = new ArrayList<RegionScore>();
        String[] tokens = null;
        String line = null;
        try {
            final BufferedReader in = new BufferedReader(new FileReader(file));
            while ((line = in.readLine()) != null) {
                tokens = Text2USeq.PATTERN_TAB.split(line);
                al.add(new RegionScore(Integer.parseInt(tokens[this.beginningColumnIndex]), Integer.parseInt(tokens[this.endingColumnIndex]), Float.parseFloat(tokens[this.scoreColumnIndex])));
            }
            in.close();
            final RegionScore[] d = new RegionScore[al.size()];
            al.toArray(d);
            Arrays.sort(d);
            return d;
        }
        catch (Exception e) {
            System.out.println("Could not parse an int or float value from malformed line -> " + line);
            e.printStackTrace();
            return null;
        }
    }
    
    private RegionText[] makeRegionTexts(final File file) {
        final ArrayList<RegionText> al = new ArrayList<RegionText>();
        String[] tokens = null;
        String line = null;
        try {
            final BufferedReader in = new BufferedReader(new FileReader(file));
            while ((line = in.readLine()) != null) {
                tokens = Text2USeq.PATTERN_TAB.split(line);
                al.add(new RegionText(Integer.parseInt(tokens[this.beginningColumnIndex]), Integer.parseInt(tokens[this.endingColumnIndex]), this.concatinateTextColumns(tokens)));
            }
            in.close();
            final RegionText[] d = new RegionText[al.size()];
            al.toArray(d);
            Arrays.sort(d);
            return d;
        }
        catch (Exception e) {
            System.out.println("Could not parse an int or float value from malformed line -> " + line);
            e.printStackTrace();
            return null;
        }
    }
    
    private RegionScoreText[] makeRegionScoreTexts(final File file) {
        final ArrayList<RegionScoreText> al = new ArrayList<RegionScoreText>();
        String[] tokens = null;
        String line = null;
        try {
            final BufferedReader in = new BufferedReader(new FileReader(file));
            while ((line = in.readLine()) != null) {
                tokens = Text2USeq.PATTERN_TAB.split(line);
                al.add(new RegionScoreText(Integer.parseInt(tokens[this.beginningColumnIndex]), Integer.parseInt(tokens[this.endingColumnIndex]), Float.parseFloat(tokens[this.scoreColumnIndex]), this.concatinateTextColumns(tokens)));
            }
            in.close();
            final RegionScoreText[] d = new RegionScoreText[al.size()];
            al.toArray(d);
            Arrays.sort(d);
            return d;
        }
        catch (Exception e) {
            System.out.println("Could not parse an int or float value from malformed line -> " + line);
            e.printStackTrace();
            return null;
        }
    }
    
    private String concatinateTextColumns(final String[] tokens) {
        if (this.textColumnIndexs.length == 1) {
            return tokens[this.textColumnIndexs[0]];
        }
        final StringBuilder sb = new StringBuilder(tokens[this.textColumnIndexs[0]]);
        for (int i = 1; i < this.textColumnIndexs.length; ++i) {
            sb.append("\t");
            sb.append(tokens[this.textColumnIndexs[i]]);
        }
        return sb.toString();
    }
    
    public static HashMap<String, File> splitFileByChromosomeAndStrand(final File dataFile, final File saveDirectory, final int chromosomeColumnIndex, final int strandColumnIndex, final boolean skipSpliceJunctions) {
        final Pattern tab = Pattern.compile("\\t");
        final Pattern spliceJunction = Pattern.compile(".+_\\d+_\\d+");
        final HashMap<String, PrintWriter> chromOut = new HashMap<String, PrintWriter>();
        final HashMap<String, File> chromFile = new HashMap<String, File>();
        try {
            final BufferedReader in = USeqUtilities.fetchBufferedReader(dataFile);
            String[] tokens = null;
            String currentChrom = "";
            PrintWriter out = null;
            String strand = ".";
            int counter = 0;
            String line;
            while ((line = in.readLine()) != null) {
                try {
                    line = line.trim();
                    if (line.length() == 0) {
                        continue;
                    }
                    if (line.startsWith("#")) {
                        continue;
                    }
                    if (line.contains("chrAdapter")) {
                        continue;
                    }
                    tokens = tab.split(line);
                    trim(tokens);
                    final String chromosome = tokens[chromosomeColumnIndex];
                    if (skipSpliceJunctions && spliceJunction.matcher(chromosome).matches()) {
                        continue;
                    }
                    if (strandColumnIndex != -1) {
                        strand = tokens[strandColumnIndex];
                    }
                    final String chromStrand = chromosome + strand;
                    if (!currentChrom.equals(chromStrand)) {
                        currentChrom = chromStrand;
                        if (chromOut.containsKey(chromStrand)) {
                            out = chromOut.get(chromStrand);
                        }
                        else {
                            final File f = new File(saveDirectory, chromStrand);
                            out = new PrintWriter(new FileWriter(f));
                            chromOut.put(chromStrand, out);
                            chromFile.put(chromStrand, f);
                        }
                    }
                    out.println(line);
                }
                catch (Exception e2) {
                    System.out.println("\nProblem parsing line -> " + line + " Skipping!");
                    if (counter++ == 1000) {
                        System.out.println("Too many malformed lines.  Aborting.");
                        return null;
                    }
                    continue;
                }
            }
            in.close();
            final Iterator<PrintWriter> it = chromOut.values().iterator();
            while (it.hasNext()) {
                it.next().close();
            }
            return chromFile;
        }
        catch (Exception e) {
            e.printStackTrace();
            return chromFile;
        }
    }
    
    public static void main(final String[] args) {
        if (args.length == 0) {
            printDocs();
            System.exit(0);
        }
        new Text2USeq(args);
    }
    
    public void processArgs(final String[] args) {
        final Pattern pat = Pattern.compile("-[a-z]");
        System.out.println("\nArguments: " + USeqUtilities.stringArrayToString(args, " ") + "\n");
        for (int i = 0; i < args.length; ++i) {
            final String lcArg = args[i].toLowerCase();
            final Matcher mat = pat.matcher(lcArg);
            if (mat.matches()) {
                final char test = args[i].charAt(1);
                try {
                    switch (test) {
                        case 'f': {
                            this.inputFiles = USeqUtilities.extractFiles(new File(args[++i]));
                            break;
                        }
                        case 'b': {
                            this.beginningColumnIndex = Integer.parseInt(args[++i]);
                            break;
                        }
                        case 'e': {
                            this.endingColumnIndex = Integer.parseInt(args[++i]);
                            break;
                        }
                        case 'v': {
                            this.scoreColumnIndex = Integer.parseInt(args[++i]);
                            break;
                        }
                        case 't': {
                            this.textColumnIndexs = USeqUtilities.stringArrayToInts(args[++i], ",");
                            break;
                        }
                        case 's': {
                            this.strandColumnIndex = Integer.parseInt(args[++i]);
                            break;
                        }
                        case 'c': {
                            this.chromosomeColumnIndex = Integer.parseInt(args[++i]);
                            break;
                        }
                        case 'i': {
                            this.rowChunkSize = Integer.parseInt(args[++i]);
                            break;
                        }
                        case 'g': {
                            this.versionedGenome = args[++i];
                            break;
                        }
                        case 'd': {
                            this.description = args[++i];
                            break;
                        }
                        case 'h': {
                            this.color = args[++i];
                            break;
                        }
                        case 'r': {
                            this.graphStyle = Integer.parseInt(args[++i]);
                            break;
                        }
                        default: {
                            USeqUtilities.printExit("\nProblem, unknown option! " + mat.group());
                            break;
                        }
                    }
                }
                catch (Exception e) {
                    USeqUtilities.printExit("\nSorry, something doesn't look right with this parameter: -" + test + "\n");
                }
            }
        }
        if (this.inputFiles == null || this.inputFiles.length == 0) {
            USeqUtilities.printErrAndExit("\nCannot find your input files?\n");
        }
        if (this.chromosomeColumnIndex == -1 || this.beginningColumnIndex == -1) {
            USeqUtilities.printErrAndExit("\nPlease enter a chromosome and or position column indexes\n");
        }
        if (this.versionedGenome == null) {
            USeqUtilities.printErrAndExit("\nPlease enter a genome version following DAS/2 notation (e.g. H_sapiens_Mar_2006, M_musculus_Jul_2007, C_elegans_May_2008).\n");
        }
        this.outputDirectories = new File[this.inputFiles.length];
        this.maxIndex = -1;
        if (this.beginningColumnIndex > this.maxIndex) {
            this.maxIndex = this.beginningColumnIndex;
        }
        if (this.endingColumnIndex > this.maxIndex) {
            this.maxIndex = this.endingColumnIndex;
        }
        if (this.scoreColumnIndex > this.maxIndex) {
            this.maxIndex = this.scoreColumnIndex;
        }
        if (this.textColumnIndexs != null) {
            for (int x = 0; x < this.textColumnIndexs.length; ++x) {
                if (this.textColumnIndexs[x] > this.maxIndex) {
                    this.maxIndex = this.textColumnIndexs[x];
                }
            }
        }
        if (this.strandColumnIndex > this.maxIndex) {
            this.maxIndex = this.strandColumnIndex;
        }
        if (this.chromosomeColumnIndex > this.maxIndex) {
            this.maxIndex = this.chromosomeColumnIndex;
        }
        if (this.beginningColumnIndex > this.maxIndex) {
            this.maxIndex = this.beginningColumnIndex;
        }
        if (this.endingColumnIndex != -1) {
            this.makeGraph = false;
        }
        if (this.color != null && !ArchiveInfo.COLOR_HEX_FORM.matcher(this.color).matches()) {
            USeqUtilities.printErrAndExit("\nCannot parse a hexidecimal color code (e.g. #CCFF33) from your color choice?! -> " + this.color);
        }
    }
    
    public static void trim(final String[] s) {
        for (int i = 0; i < s.length; ++i) {
            s[i] = s[i].trim();
        }
    }
    
    public static void printDocs() {
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < Text2USeq.GRAPH_STYLES.length; ++i) {
            sb.append("      " + i + "\t" + Text2USeq.GRAPH_STYLES[i] + "\n");
        }
        System.out.println("\n**************************************************************************************\n**                              Text 2 USeq: April 2011                             **\n**************************************************************************************\nConverts text genomic data files (e.g. xxx.bed, xxx.gff, xxx.sgr, etc.) to\nbinary USeq archives (xxx.useq).  Assumes interbase coordinates. Only select\nthe columns that contain relevant information.  For example, if your data isn't\nstranded, or you want to ignore strands, then skip the -s option.  If your data\ndoesn't have a value/ score then skip the -v option. Etc. Use the USeq2Text app to\nconvert back to text xxx.bed format. \n\nOptions:\n-f Full path file/directory containing tab delimited genomic data files.\n-g Genome verison using DAS notation (e.g. H_sapiens_Mar_2006, M_musculus_Jul_2007),\n      see http://genome.ucsc.edu/FAQ/FAQreleases#release1\n-c Chromosome column index\n-b Position/Beginning column index\n-s (Optional) Strand column index (+, -, or .; NOT F, R)\n-e (Optional) End column index\n-v (Optional) Value column index\n-t (Optional) Text column index(s), comma delimited, no spaces, defines which columns\n      to join using a tab.\n-i (Optional) Index size for slicing split chromosome data (e.g. # rows per slice),\n      defaults to 10000.\n-r (Optional) For graphs, select a style, defaults to 0\n" + (Object)sb + "-h (Optional) Color, hexadecimal (e.g. #6633FF), enclose in quotations\n" + "-d (Optional) Description, enclose in quotations \n" + "\nExample: java -Xmx4G -jar pathTo/USeq/Apps/Text2USeq -f\n" + "      /AnalysisResults/BedFiles/ -c 0 -b 1 -e 2 -i 5000 -h '#6633FF'\n" + "      -d 'Final processed chIP-Seq results for Bcd and Hunchback, 30M reads'\n" + "      -g H_sapiens_Feb_2009 \n\n" + "Indexes for common formats:\n" + "       bed3 -c 0 -b 1 -e 2\n" + "       bed5 -c 0 -b 1 -e 2 -t 3 -v 4 -s 5\n" + "       bed12 -c 0 -b 1 -e 2 -t 3,6,7,8,9,10,11 -v 4 -s 5\n" + "       gff w/scr,stnd,name -c 0 -b 3 -e 4 -v 5 -s 6 -t 8\n" + "\n" + "**************************************************************************************\n");
    }
    
    static {
        Text2USeq.GRAPH_STYLES = new String[] { "Bar", "Stairstep", "HeatMap", "Line" };
        PATTERN_TAB = Pattern.compile("\\t");
        PATTERN_STRAND = Pattern.compile(".*[+-\\.]$");
    }
}
