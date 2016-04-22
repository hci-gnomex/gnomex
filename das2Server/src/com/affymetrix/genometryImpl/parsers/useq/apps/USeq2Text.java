// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.parsers.useq.apps;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.Enumeration;
import com.affymetrix.genometryImpl.parsers.useq.data.RegionScoreTextData;
import com.affymetrix.genometryImpl.parsers.useq.data.RegionTextData;
import com.affymetrix.genometryImpl.parsers.useq.data.RegionScoreData;
import com.affymetrix.genometryImpl.parsers.useq.data.RegionData;
import com.affymetrix.genometryImpl.parsers.useq.data.PositionScoreTextData;
import com.affymetrix.genometryImpl.parsers.useq.data.PositionTextData;
import com.affymetrix.genometryImpl.parsers.useq.data.PositionScoreData;
import com.affymetrix.genometryImpl.parsers.useq.data.PositionData;
import java.io.InputStream;
import java.io.DataInputStream;
import java.io.BufferedInputStream;
import com.affymetrix.genometryImpl.parsers.useq.SliceInfo;
import com.affymetrix.genometryImpl.parsers.useq.ArchiveInfo;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.io.Writer;
import java.io.PrintWriter;
import java.io.FileWriter;
import com.affymetrix.genometryImpl.parsers.useq.USeqUtilities;
import com.affymetrix.genometryImpl.parsers.useq.USeqArchive;
import java.io.File;

public class USeq2Text
{
    private File[] useqArchives;
    private boolean printBedFormat;
    private boolean printWigFormat;
    private boolean skipZeroBlockBedGraphs;
    private boolean convertScoresToBedFormat;
    
    public USeq2Text(final String[] args) {
        this.printBedFormat = false;
        this.printWigFormat = false;
        this.skipZeroBlockBedGraphs = true;
        this.convertScoresToBedFormat = false;
        this.processArgs(args);
        try {
            System.out.println("Processing:");
            for (int i = 0; i < this.useqArchives.length; ++i) {
                System.out.println("\t" + this.useqArchives[i].getName());
                if (this.printWigFormat) {
                    final USeqArchive ua = new USeqArchive(this.useqArchives[i]);
                    if (ua.isStranded()) {
                        File wigFile = new File(this.useqArchives[i].getParentFile(), USeqUtilities.removeExtension(this.useqArchives[i].getName()) + "Plus.wig");
                        this.print2WigFile(this.useqArchives[i], wigFile, "+");
                        wigFile = new File(this.useqArchives[i].getParentFile(), USeqUtilities.removeExtension(this.useqArchives[i].getName()) + "Minus.wig");
                        this.print2WigFile(this.useqArchives[i], wigFile, "-");
                    }
                    else {
                        final File wigFile = new File(this.useqArchives[i].getParentFile(), USeqUtilities.removeExtension(this.useqArchives[i].getName()) + ".wig");
                        this.print2WigFile(this.useqArchives[i], wigFile, null);
                    }
                }
                else {
                    String extension = ".txt";
                    if (this.printBedFormat) {
                        extension = ".bed";
                    }
                    final File txtFile = new File(this.useqArchives[i].getParentFile(), USeqUtilities.removeExtension(this.useqArchives[i].getName()) + extension);
                    print2TextFile(this.useqArchives[i], txtFile, this.printBedFormat, this.convertScoresToBedFormat);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("\nDone!");
    }
    
    public USeq2Text() {
        this.printBedFormat = false;
        this.printWigFormat = false;
        this.skipZeroBlockBedGraphs = true;
        this.convertScoresToBedFormat = false;
    }
    
    public static void print2TextFile(final File inputUseqArchive, final File outputTextFile, final boolean printOuputInBedFormat, final boolean fixBedScores) {
        try {
            final PrintWriter out = new PrintWriter(new FileWriter(outputTextFile));
            final ZipFile zf = new ZipFile(inputUseqArchive);
            final Enumeration<ZipEntry> e = (Enumeration<ZipEntry>)zf.entries();
            ZipEntry ze = e.nextElement();
            if (!ze.getName().equals("archiveReadMe.txt")) {
                throw new IOException("The first zip entry -> " + ze.getName() + ", is not the " + "archiveReadMe.txt" + "! Aborting.");
            }
            final ArchiveInfo ai = new ArchiveInfo(zf.getInputStream(ze), false);
            ai.appendCommentedKeyValues(out);
            while (e.hasMoreElements()) {
                ze = e.nextElement();
                final SliceInfo si = new SliceInfo(ze.getName());
                final DataInputStream dis = new DataInputStream(new BufferedInputStream(zf.getInputStream(ze)));
                final String extension = si.getBinaryType();
                if (printOuputInBedFormat) {
                    if (USeqUtilities.POSITION.matcher(extension).matches()) {
                        new PositionData(dis, si).writeBed(out);
                    }
                    else if (USeqUtilities.POSITION_SCORE.matcher(extension).matches()) {
                        new PositionScoreData(dis, si).writeBed(out, fixBedScores);
                    }
                    else if (USeqUtilities.POSITION_TEXT.matcher(extension).matches()) {
                        new PositionTextData(dis, si).writeBed(out);
                    }
                    else if (USeqUtilities.POSITION_SCORE_TEXT.matcher(extension).matches()) {
                        new PositionScoreTextData(dis, si).writeBed(out, fixBedScores);
                    }
                    else if (USeqUtilities.REGION.matcher(extension).matches()) {
                        new RegionData(dis, si).writeBed(out);
                    }
                    else if (USeqUtilities.REGION_SCORE.matcher(extension).matches()) {
                        new RegionScoreData(dis, si).writeBed(out, fixBedScores);
                    }
                    else if (USeqUtilities.REGION_TEXT.matcher(extension).matches()) {
                        new RegionTextData(dis, si).writeBed(out);
                    }
                    else {
                        if (!USeqUtilities.REGION_SCORE_TEXT.matcher(extension).matches()) {
                            throw new IOException("\nFailed to recognize the binary file extension! " + ze.getName());
                        }
                        new RegionScoreTextData(dis, si).writeBed(out, fixBedScores);
                    }
                }
                else if (USeqUtilities.POSITION.matcher(extension).matches()) {
                    new PositionData(dis, si).writeNative(out);
                }
                else if (USeqUtilities.POSITION_SCORE.matcher(extension).matches()) {
                    new PositionScoreData(dis, si).writeNative(out);
                }
                else if (USeqUtilities.POSITION_TEXT.matcher(extension).matches()) {
                    new PositionTextData(dis, si).writeNative(out);
                }
                else if (USeqUtilities.POSITION_SCORE_TEXT.matcher(extension).matches()) {
                    new PositionScoreTextData(dis, si).writeNative(out);
                }
                else if (USeqUtilities.REGION.matcher(extension).matches()) {
                    new RegionData(dis, si).writeNative(out);
                }
                else if (USeqUtilities.REGION_SCORE.matcher(extension).matches()) {
                    new RegionScoreData(dis, si).writeNative(out);
                }
                else if (USeqUtilities.REGION_TEXT.matcher(extension).matches()) {
                    new RegionTextData(dis, si).writeNative(out);
                }
                else {
                    if (!USeqUtilities.REGION_SCORE_TEXT.matcher(extension).matches()) {
                        throw new IOException("\nFailed to recognize the binary file extension! " + ze.getName());
                    }
                    new RegionScoreTextData(dis, si).writeNative(out);
                }
                dis.close();
            }
            out.close();
        }
        catch (IOException e2) {
            System.err.println("\nError, could not process binary archive!");
            e2.printStackTrace();
        }
    }
    
    public String buildWigHeader(final File inputUseqArchive, final ArchiveInfo ai) throws IOException {
        final StringBuilder header = new StringBuilder();
        final String graphType = ai.getValue("initialGraphStyle");
        if (graphType == null) {
            throw new IOException("\nFailed to identify a graph type.\n");
        }
        if (graphType.equals("HeatMap") || graphType.equals("Stairstep")) {
            header.append("track type=bedGraph");
        }
        else {
            header.append("track type=wiggle_0");
        }
        final String name = inputUseqArchive.getName().replaceAll(".useq", "");
        header.append(" name=\"");
        header.append(name);
        header.append("\"");
        header.append(" description=\"");
        final String desc = ai.getValue("description");
        if (desc != null) {
            header.append(desc);
            header.append(" - ");
        }
        header.append(ai.getVersionedGenome());
        header.append("\"");
        header.append(" visibility=full");
        header.append(" alwaysZero=on");
        final String hexColor = ai.getValue("initialColor");
        if (hexColor != null) {
            final String rgb = USeqUtilities.convertHexadecimal2RGB(hexColor, ",");
            if (rgb == null) {
                throw new IOException("\nFailed to convert the hex color code '" + hexColor + "' to rgb. \n");
            }
            header.append(" color=");
            header.append(rgb);
        }
        if (graphType.equals("Dot")) {
            header.append(" graphType=points");
        }
        return header.toString();
    }
    
    public void print2WigFile(final File inputUseqArchive, final File outputTextFile, final String strand) {
        try {
            final PrintWriter out = new PrintWriter(new FileWriter(outputTextFile));
            final ZipFile zf = new ZipFile(inputUseqArchive);
            final Enumeration<ZipEntry> e = (Enumeration<ZipEntry>)zf.entries();
            ZipEntry ze = e.nextElement();
            if (!ze.getName().equals("archiveReadMe.txt")) {
                throw new IOException("The first zip entry -> " + ze.getName() + ", is not the " + "archiveReadMe.txt" + "! Aborting.");
            }
            final ArchiveInfo ai = new ArchiveInfo(zf.getInputStream(ze), false);
            if (ai.isRegionData()) {
                throw new IOException("\nThis USeq archive looks like it contains region data, not graph data.  Use the native text or bed file output option. \n");
            }
            out.println("# This wig file was generated by converting the '" + inputUseqArchive + "' archive with the USeq2Text application.");
            ai.appendCommentedKeyValues(out);
            out.println(this.buildWigHeader(inputUseqArchive, ai));
            boolean bedGraphFormat = false;
            final String graphType = ai.getValue("initialGraphStyle");
            if (graphType.equals("Stairstep") || graphType.equals("HeatMap")) {
                bedGraphFormat = true;
            }
            if (bedGraphFormat) {
                this.writeBedGraph(zf, e, out, strand);
            }
            else {
                String chromosome = "";
                while (e.hasMoreElements()) {
                    ze = e.nextElement();
                    final SliceInfo si = new SliceInfo(ze.getName());
                    final DataInputStream dis = new DataInputStream(new BufferedInputStream(zf.getInputStream(ze)));
                    final String extension = si.getBinaryType();
                    if (strand != null && !strand.equals(si.getStrand())) {
                        continue;
                    }
                    if (!si.getChromosome().equals(chromosome)) {
                        chromosome = si.getChromosome();
                        out.println("variableStep chrom=" + chromosome);
                    }
                    if (USeqUtilities.POSITION.matcher(extension).matches()) {
                        new PositionData(dis, si).writePositionScore(out);
                    }
                    else if (USeqUtilities.POSITION_SCORE.matcher(extension).matches()) {
                        new PositionScoreData(dis, si).writePositionScore(out);
                    }
                    else if (USeqUtilities.POSITION_TEXT.matcher(extension).matches()) {
                        new PositionTextData(dis, si).writePositionScore(out);
                    }
                    else {
                        if (!USeqUtilities.POSITION_SCORE_TEXT.matcher(extension).matches()) {
                            throw new IOException("\nThis USeq archive looks like it contains region data, not graph data.  Use the native text or bed file output option. \n");
                        }
                        new PositionScoreTextData(dis, si).writePositionScore(out);
                    }
                    dis.close();
                }
            }
            out.close();
        }
        catch (IOException e2) {
            System.err.println("\nError, could not process your binary archive!");
            outputTextFile.delete();
            e2.printStackTrace();
        }
    }
    
    public void writeBedGraph(final ZipFile zf, final Enumeration<ZipEntry> e, final PrintWriter out, final String strand) throws IOException {
        final String tab = "\t";
        String chromosome = null;
        final ArrayList<PositionScoreData> psAL = new ArrayList<PositionScoreData>();
        while (e.hasMoreElements()) {
            final ZipEntry ze = e.nextElement();
            final SliceInfo si = new SliceInfo(ze.getName());
            final DataInputStream dis = new DataInputStream(new BufferedInputStream(zf.getInputStream(ze)));
            final String extension = si.getBinaryType();
            if (strand != null && !strand.equals(si.getStrand())) {
                continue;
            }
            if (chromosome == null) {
                chromosome = si.getChromosome();
            }
            else if (!si.getChromosome().equals(chromosome)) {
                final PositionScoreData merged = PositionScoreData.merge(psAL);
                final int[] positions = merged.getBasePositions();
                final float[] scores = merged.getBaseScores();
                for (int i = 0; i < scores.length; ++i) {
                    final int next = i + 1;
                    if (next == scores.length) {
                        break;
                    }
                    if (scores[i] == scores[next]) {
                        if (!this.skipZeroBlockBedGraphs || scores[i] != 0.0f) {
                            out.print(chromosome);
                            out.print(tab);
                            out.print(positions[i]);
                            out.print(tab);
                            out.print(positions[next] + 1);
                            out.print(tab);
                            out.println(scores[i]);
                            ++i;
                        }
                    }
                    else if (positions[next] - 1 == positions[i]) {
                        if (!this.skipZeroBlockBedGraphs || scores[i] != 0.0f) {
                            out.print(chromosome);
                            out.print(tab);
                            out.print(positions[i]);
                            out.print(tab);
                            out.print(positions[i] + 1);
                            out.print(tab);
                            out.println(scores[i]);
                        }
                    }
                }
                psAL.clear();
                chromosome = si.getChromosome();
            }
            if (USeqUtilities.POSITION_SCORE.matcher(extension).matches()) {
                psAL.add(new PositionScoreData(dis, si));
            }
            else {
                if (!USeqUtilities.POSITION_SCORE_TEXT.matcher(extension).matches()) {
                    throw new IOException("\nThis USeq archive lacks score information thus it cannot be made into a bed graph!  Use the native text or bed file output option. \n");
                }
                new PositionScoreTextData(dis, si).writePositionScore(out);
                final PositionScoreTextData p = new PositionScoreTextData(dis, si);
                psAL.add(new PositionScoreData(p.getBasePositions(), p.getBaseScores(), si));
            }
            dis.close();
        }
        final PositionScoreData merged2 = PositionScoreData.merge(psAL);
        final int[] positions2 = merged2.getBasePositions();
        final float[] scores2 = merged2.getBaseScores();
        for (int j = 0; j < scores2.length; ++j) {
            final int next2 = j + 1;
            if (next2 == scores2.length) {
                break;
            }
            if (scores2[j] == scores2[next2]) {
                if (!this.skipZeroBlockBedGraphs || scores2[j] != 0.0f) {
                    out.print(chromosome);
                    out.print(tab);
                    out.print(positions2[j]);
                    out.print(tab);
                    out.print(positions2[next2] + 1);
                    out.print(tab);
                    out.println(scores2[j]);
                }
            }
        }
    }
    
    public static void main(final String[] args) {
        if (args.length == 0) {
            printDocs();
            System.exit(0);
        }
        new USeq2Text(args);
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
                            this.useqArchives = USeqUtilities.extractFiles(new File(args[++i]), "useq");
                            break;
                        }
                        case 'b': {
                            this.printBedFormat = true;
                            break;
                        }
                        case 'c': {
                            this.convertScoresToBedFormat = true;
                            break;
                        }
                        case 'w': {
                            this.printWigFormat = true;
                            this.printBedFormat = false;
                            break;
                        }
                        case 'h': {
                            printDocs();
                            System.exit(0);
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
        if (this.useqArchives == null || this.useqArchives.length == 0) {
            USeqUtilities.printExit("\nCannot find any xxx.useq USeq archives?\n");
        }
    }
    
    public static void printDocs() {
        System.out.println("\n**************************************************************************************\n**                                USeq 2 Text: Aug 2011                             **\n**************************************************************************************\nConverts USeq archives to text either as minimal native, bed, or wig graph format. \n\n\nOptions:\n-f Full path file/directory containing xxx.useq files.\n-b Print bed format, defaults to native text format.\n-c Convert scores to bed format 0-1000.\n-w Print wig graph format (var step or bed graph), defaults to native format.\n\n\nExample: java -Xmx4G -jar pathTo/USeq/Apps/USeq2Text -f\n      /AnalysisResults/USeqDataArchives/ \n\n**************************************************************************************\n");
    }
    
    public File[] getUseqArchives() {
        return this.useqArchives;
    }
    
    public void setUseqArchives(final File[] useqArchives) {
        this.useqArchives = useqArchives;
    }
    
    public boolean isPrintBedFormat() {
        return this.printBedFormat;
    }
    
    public void setPrintBedFormat(final boolean printBedFormat) {
        this.printBedFormat = printBedFormat;
    }
    
    public boolean isPrintWigFormat() {
        return this.printWigFormat;
    }
    
    public void setPrintWigFormat(final boolean printWigFormat) {
        this.printWigFormat = printWigFormat;
    }
    
    public boolean isSkipZeroBlockBedGraphs() {
        return this.skipZeroBlockBedGraphs;
    }
    
    public void setSkipZeroBlockBedGraphs(final boolean skipZeroBlockBedGraphs) {
        this.skipZeroBlockBedGraphs = skipZeroBlockBedGraphs;
    }
}
