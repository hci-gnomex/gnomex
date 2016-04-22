// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.parsers.useq.apps;

import java.util.regex.Matcher;
import com.affymetrix.genometryImpl.parsers.useq.data.PositionScoreData;
import com.affymetrix.genometryImpl.parsers.useq.SliceInfo;
import java.util.HashSet;
import com.affymetrix.genometryImpl.parsers.useq.data.PositionScore;
import java.io.BufferedReader;
import java.io.IOException;
import com.affymetrix.genometryImpl.parsers.useq.ArchiveInfo;
import com.affymetrix.genometryImpl.parsers.useq.USeqUtilities;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.io.File;

public class Wig2USeq
{
    private File[] files;
    private float skipValue;
    private float negativeSkipValue;
    private ArrayList<File> files2Zip;
    private int rowChunkSize;
    private File saveDirectory;
    private String versionedGenome;
    private int graphStyle;
    private String color;
    private String description;
    private File workingWigFile;
    private Pattern number;
    private Pattern space;
    private Pattern equal;
    
    public Wig2USeq(final String[] args) {
        this.skipValue = Float.MIN_VALUE;
        this.files2Zip = new ArrayList<File>();
        this.rowChunkSize = 100000;
        this.versionedGenome = null;
        this.graphStyle = 1;
        this.color = null;
        this.description = null;
        this.workingWigFile = null;
        this.number = Pattern.compile("^\\d");
        this.space = Pattern.compile("\\s");
        this.equal = Pattern.compile("=");
        this.processArgs(args);
        try {
            System.out.println("\nParsing...");
            for (int x = 0; x < this.files.length; ++x) {
                this.workingWigFile = this.files[x];
                System.out.println("\t" + this.workingWigFile);
                final String type = this.parseWigFileType();
                if (type == null) {
                    USeqUtilities.printExit("\nCould not parse the file type from this file, aborting. -> " + this.workingWigFile + "\n\t" + "Looking for a line containing 'type=bedGraph' or starting with 'fixedStep' or 'variableStep'.");
                }
                this.saveDirectory = USeqUtilities.makeDirectory(this.workingWigFile, ".TempDelMe");
                if (type.equals("variableStep")) {
                    this.parseVariableStepWigFile();
                }
                else if (type.equals("fixedStep")) {
                    this.graphStyle = 1;
                    this.parseFixedStepWigFile();
                }
                else if (type.equals("bedGraph")) {
                    this.graphStyle = 1;
                    this.parseBedGraphFile();
                }
                else {
                    USeqUtilities.printExit("Not implemented " + type);
                }
                this.writeReadMeTxt();
                System.out.println("\tZipping...");
                this.zipIt();
            }
        }
        catch (Exception e) {
            System.out.println("\nProblem parsing " + this.workingWigFile.getName() + "! Skipping.\n");
            e.printStackTrace();
        }
    }
    
    private void zipIt() {
        final String zipName = USeqUtilities.removeExtension(this.saveDirectory.getName()) + ".useq";
        final File zipFile = new File(this.workingWigFile.getParentFile(), zipName);
        final File[] files = new File[this.files2Zip.size()];
        this.files2Zip.toArray(files);
        USeqUtilities.zip(files, zipFile);
        USeqUtilities.deleteDirectory(this.saveDirectory);
        this.files2Zip.clear();
    }
    
    private void writeReadMeTxt() {
        try {
            final ArchiveInfo ai = new ArchiveInfo(this.versionedGenome, null);
            ai.setDataType("graph");
            ai.setInitialGraphStyle(Text2USeq.GRAPH_STYLES[this.graphStyle]);
            ai.setOriginatingDataSource(this.workingWigFile.toString());
            if (this.color != null) {
                ai.setInitialColor(this.color);
            }
            if (this.description != null) {
                ai.setDescription(this.description);
            }
            final File readme = ai.writeReadMeFile(this.saveDirectory);
            this.files2Zip.add(0, readme);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private String parseWigFileType() throws IOException {
        final BufferedReader in = USeqUtilities.fetchBufferedReader(this.workingWigFile);
        String type = null;
        int counter = 0;
        String line;
        while ((line = in.readLine()) != null) {
            line = line.trim();
            if (line.length() == 0) {
                continue;
            }
            if (line.contains("type=bedGraph")) {
                type = "bedGraph";
                break;
            }
            if (line.startsWith("fixedStep")) {
                type = "fixedStep";
                break;
            }
            if (line.startsWith("variableStep")) {
                type = "variableStep";
                break;
            }
            if (++counter > 1000) {
                return null;
            }
        }
        in.close();
        return type;
    }
    
    public void parseBedGraphFile() throws IOException {
        final BufferedReader in = USeqUtilities.fetchBufferedReader(this.workingWigFile);
        final Pattern space = Pattern.compile("\\s+");
        ArrayList<PositionScore> al = new ArrayList<PositionScore>();
        String currentChromosome = "";
        String[] tokens = null;
        final HashSet<String> chroms = new HashSet<String>();
        float score = 0.0f;
        String line;
        while ((line = in.readLine()) != null) {
            line = line.trim();
            if (line.length() == 0) {
                continue;
            }
            if (line.contains("type=bedGraph")) {
                break;
            }
        }
        while ((line = in.readLine()) != null) {
            tokens = space.split(line);
            if (tokens.length != 4) {
                continue;
            }
            score = Float.parseFloat(tokens[3]);
            if (this.skipValue == Float.MIN_VALUE || score > this.skipValue || score < this.negativeSkipValue) {
                break;
            }
        }
        currentChromosome = tokens[0];
        chroms.add(currentChromosome);
        int start = Integer.parseInt(tokens[1]);
        int stop = Integer.parseInt(tokens[2]);
        if (score != 0.0f) {
            int pos = start - 1;
            if (pos < 0) {
                pos = 0;
            }
            al.add(new PositionScore(pos, 0.0f));
        }
        al.add(new PositionScore(start, score));
        al.add(new PositionScore(stop - 1, score));
        while ((line = in.readLine()) != null) {
            tokens = space.split(line);
            if (tokens.length != 4) {
                continue;
            }
            if (!tokens[0].equals(currentChromosome)) {
                if (chroms.contains(tokens[0])) {
                    throw new IOException("This file is not sorted by chromosome! " + tokens[0] + " has been parsed before! Aborting");
                }
                chroms.add(tokens[0]);
                System.out.println("\t\t" + currentChromosome + "\t" + al.size());
                final PositionScore[] ps = new PositionScore[al.size()];
                al.toArray(ps);
                final SliceInfo sliceInfo = new SliceInfo(currentChromosome, ".", 0, 0, 0, null);
                final PositionScoreData data = new PositionScoreData(ps, sliceInfo);
                PositionScoreData.updateSliceInfo(ps, sliceInfo);
                data.sliceWritePositionScoreData(this.rowChunkSize, this.saveDirectory, this.files2Zip);
                currentChromosome = tokens[0];
                al.clear();
            }
            score = Float.parseFloat(tokens[3]);
            if (this.skipValue != Float.MIN_VALUE && score <= this.skipValue && score >= this.negativeSkipValue) {
                continue;
            }
            final int newStart = Integer.parseInt(tokens[1]);
            if (newStart != stop) {
                al.add(new PositionScore(stop, 0.0f));
                final int newStartMinOne = newStart - 1;
                if (newStartMinOne != stop) {
                    al.add(new PositionScore(newStartMinOne, 0.0f));
                }
            }
            start = newStart;
            stop = Integer.parseInt(tokens[2]);
            al.add(new PositionScore(start, score));
            al.add(new PositionScore(stop - 1, score));
        }
        al.add(new PositionScore(stop, 0.0f));
        System.out.println("\t\t" + currentChromosome + "\t" + al.size());
        final PositionScore[] ps = new PositionScore[al.size()];
        al.toArray(ps);
        final SliceInfo sliceInfo = new SliceInfo(currentChromosome, ".", 0, 0, 0, null);
        final PositionScoreData data = new PositionScoreData(ps, sliceInfo);
        PositionScoreData.updateSliceInfo(ps, sliceInfo);
        data.sliceWritePositionScoreData(this.rowChunkSize, this.saveDirectory, this.files2Zip);
        al = null;
    }
    
    public void parseVariableStepWigFile() throws Exception {
        String[] tokens = null;
        String chromosome = null;
        ArrayList<PositionScore> ps = new ArrayList<PositionScore>();
        final BufferedReader in = USeqUtilities.fetchBufferedReader(this.workingWigFile);
        String line;
        while ((line = in.readLine()) != null) {
            line = line.trim();
            if (line.length() == 0) {
                continue;
            }
            final Matcher mat = this.number.matcher(line);
            if (mat.find()) {
                tokens = this.space.split(line);
                if (tokens.length != 2) {
                    throw new Exception("Problem with parsing position:value from " + this.workingWigFile + " line -> " + line);
                }
                final float value = Float.parseFloat(tokens[1]);
                if (value == Float.MIN_VALUE) {
                    continue;
                }
                ps.add(new PositionScore(Integer.parseInt(tokens[0]) - 1, value));
            }
            else {
                if (!line.startsWith("variableStep")) {
                    continue;
                }
                tokens = this.space.split(line);
                tokens = this.equal.split(tokens[1]);
                if (tokens.length != 2) {
                    throw new Exception("Problem parsing chromosome from" + this.workingWigFile + " line -> " + line);
                }
                if (chromosome != null) {
                    System.out.println("\t\t" + chromosome + "\t" + ps.size());
                    final PositionScore[] psArray = new PositionScore[ps.size()];
                    ps.toArray(psArray);
                    ps.clear();
                    final SliceInfo sliceInfo = new SliceInfo(chromosome, ".", 0, 0, 0, null);
                    final PositionScoreData data = new PositionScoreData(psArray, sliceInfo);
                    PositionScoreData.updateSliceInfo(psArray, sliceInfo);
                    data.sliceWritePositionScoreData(this.rowChunkSize, this.saveDirectory, this.files2Zip);
                }
                chromosome = tokens[1];
            }
        }
        if (chromosome == null) {
            throw new Exception("No 'variableStep chrom=...' line found in " + this.workingWigFile);
        }
        System.out.println("\t\t" + chromosome + "\t" + ps.size());
        PositionScore[] psArray = new PositionScore[ps.size()];
        ps.toArray(psArray);
        final SliceInfo sliceInfo = new SliceInfo(chromosome, ".", 0, 0, 0, null);
        final PositionScoreData data = new PositionScoreData(psArray, sliceInfo);
        PositionScoreData.updateSliceInfo(psArray, sliceInfo);
        data.sliceWritePositionScoreData(this.rowChunkSize, this.saveDirectory, this.files2Zip);
        ps = null;
        psArray = null;
        in.close();
    }
    
    public void parseFixedStepWigFile() throws Exception {
        ArrayList<PositionScore> ps = new ArrayList<PositionScore>();
        String[] tokens = null;
        String chromosome = null;
        final BufferedReader in = USeqUtilities.fetchBufferedReader(this.workingWigFile);
        final HashSet<String> chroms = new HashSet<String>();
        int startPosition = 0;
        int stepSize = 0;
        String line;
        while ((line = in.readLine()) != null) {
            line = line.trim();
            if (line.length() == 0) {
                continue;
            }
            final Matcher mat = this.number.matcher(line);
            if (mat.find()) {
                float value = Float.parseFloat(line);
                if (value != this.skipValue) {
                    if (value == 0.0f) {
                        value = 1.0E-10f;
                    }
                    ps.add(new PositionScore(startPosition, value));
                }
                startPosition += stepSize;
            }
            else {
                if (!line.startsWith("fixedStep")) {
                    continue;
                }
                final int sizePS = ps.size();
                if (sizePS != 0) {
                    final int lastPosition = ps.get(sizePS - 1).getPosition();
                    ps.add(new PositionScore(lastPosition + 1, 0.0f));
                }
                tokens = this.space.split(line);
                if (tokens.length != 4) {
                    throw new Exception("Problem with parsing fixedStep line from " + this.workingWigFile + " line -> " + line);
                }
                final String[] chromTokens = this.equal.split(tokens[1]);
                if (chromTokens.length != 2) {
                    throw new Exception("Problem parsing chromosome from" + this.workingWigFile + " line -> " + line);
                }
                if (chromosome == null) {
                    chromosome = chromTokens[1];
                }
                if (chromosome != null && !chromosome.equals(chromTokens[1])) {
                    System.out.println("\t\t" + chromosome + "\t" + ps.size());
                    PositionScore[] psArray = new PositionScore[ps.size()];
                    ps.toArray(psArray);
                    ps.clear();
                    psArray = stripDuplicateValues(psArray);
                    final SliceInfo sliceInfo = new SliceInfo(chromosome, ".", 0, 0, 0, null);
                    final PositionScoreData data = new PositionScoreData(psArray, sliceInfo);
                    PositionScoreData.updateSliceInfo(psArray, sliceInfo);
                    data.sliceWritePositionScoreData(this.rowChunkSize, this.saveDirectory, this.files2Zip);
                    if (!chroms.contains(chromosome)) {
                        chroms.add(chromosome);
                    }
                    else {
                        USeqUtilities.printExit("\nWig file is not sorted by chromosome! Aborting.\n");
                    }
                    chromosome = chromTokens[1];
                }
                final String[] startTokens = this.equal.split(tokens[2]);
                if (startTokens.length != 2) {
                    throw new Exception("Problem parsing start position from" + this.workingWigFile + " line -> " + line);
                }
                startPosition = Integer.parseInt(startTokens[1]) - 1;
                final String[] stepTokens = this.equal.split(tokens[3]);
                if (stepTokens.length != 2) {
                    throw new Exception("Problem parsing start position from" + this.workingWigFile + " line -> " + line);
                }
                stepSize = Integer.parseInt(stepTokens[1]);
                final int pos = startPosition - 1;
                if (pos <= 0) {
                    continue;
                }
                ps.add(new PositionScore(pos, 0.0f));
            }
        }
        if (chromosome == null) {
            throw new Exception("No 'fixedStep chrom=...' line found in " + this.workingWigFile);
        }
        final int sizePS = ps.size();
        if (sizePS != 0) {
            final int lastPosition = ps.get(sizePS - 1).getPosition();
            ps.add(new PositionScore(lastPosition + 1, 0.0f));
        }
        System.out.println("\t\t" + chromosome + "\t" + ps.size());
        PositionScore[] psArray2 = new PositionScore[ps.size()];
        ps.toArray(psArray2);
        ps.clear();
        psArray2 = stripDuplicateValues(psArray2);
        final SliceInfo sliceInfo2 = new SliceInfo(chromosome, ".", 0, 0, 0, null);
        PositionScoreData data2 = new PositionScoreData(psArray2, sliceInfo2);
        PositionScoreData.updateSliceInfo(psArray2, sliceInfo2);
        data2.sliceWritePositionScoreData(this.rowChunkSize, this.saveDirectory, this.files2Zip);
        ps = null;
        data2 = null;
        psArray2 = null;
        in.close();
    }
    
    public static PositionScore[] stripDuplicateValues(PositionScore[] ps) {
        final ArrayList<PositionScore> al = new ArrayList<PositionScore>();
        float value = Float.MIN_VALUE;
        int lastSetIndex = -1;
        for (int i = 0; i < ps.length; ++i) {
            final float testValue = ps[i].getScore();
            if (testValue != value) {
                al.add(ps[i]);
                value = testValue;
                lastSetIndex = i;
            }
            else {
                for (int j = i + 1; j < ps.length; ++j) {
                    final float nextValue = ps[j].getScore();
                    if (nextValue != testValue) {
                        al.add(ps[i]);
                        lastSetIndex = i;
                        break;
                    }
                    ++i;
                }
            }
        }
        if (lastSetIndex != ps.length - 1) {
            al.add(ps[ps.length - 1]);
        }
        ps = new PositionScore[al.size()];
        al.toArray(ps);
        return ps;
    }
    
    public static void main(final String[] args) {
        if (args.length == 0) {
            printDocs();
            System.exit(0);
        }
        new Wig2USeq(args);
    }
    
    public void processArgs(final String[] args) {
        final Pattern pat = Pattern.compile("-[a-z]");
        File file = null;
        for (int i = 0; i < args.length; ++i) {
            final String lcArg = args[i].toLowerCase();
            final Matcher mat = pat.matcher(lcArg);
            if (mat.matches()) {
                final char test = args[i].charAt(1);
                try {
                    switch (test) {
                        case 'f': {
                            file = new File(args[i + 1]);
                            ++i;
                            break;
                        }
                        case 's': {
                            this.skipValue = Float.parseFloat(args[++i]);
                            break;
                        }
                        case 'i': {
                            this.rowChunkSize = Integer.parseInt(args[++i]);
                            break;
                        }
                        case 'v': {
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
                            USeqUtilities.printExit("\nError: unknown option! " + mat.group());
                            break;
                        }
                    }
                }
                catch (Exception e) {
                    USeqUtilities.printExit("\nSorry, something doesn't look right with this parameter: -" + test + "\n");
                }
            }
        }
        if (file == null) {
            USeqUtilities.printExit("\nError: cannot find your xxx.wig file(s)?");
        }
        final File[][] tot = { USeqUtilities.extractFiles(file, ".wig"), USeqUtilities.extractFiles(file, ".wig.zip"), USeqUtilities.extractFiles(file, ".wig.gz"), USeqUtilities.extractFiles(file, ".bedGraph4"), USeqUtilities.extractFiles(file, ".bedGraph4.zip"), USeqUtilities.extractFiles(file, ".bedGraph4.gz") };
        this.files = USeqUtilities.collapseFileArray(tot);
        if (this.files == null || this.files.length == 0) {
            USeqUtilities.printExit("\nError: cannot find your xxx.wig/bedGraph4 file(s)?");
        }
        if (this.versionedGenome == null) {
            USeqUtilities.printExit("\nError: you must supply a genome version. Goto http://genome.ucsc.edu/cgi-bin/hgGateway load your organism to find the associated genome version.\n");
        }
        if (this.skipValue != Float.MIN_VALUE) {
            this.negativeSkipValue = this.skipValue * -1.0f;
        }
    }
    
    public static void printDocs() {
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < Text2USeq.GRAPH_STYLES.length; ++i) {
            sb.append("      " + i + "\t" + Text2USeq.GRAPH_STYLES[i] + "\n");
        }
        System.out.println("\n**************************************************************************************\n**                                Wig 2 USeq: Dec 2009                              **\n**************************************************************************************\nConverts variable step, fixed step, and bedGraph xxx.wig/bedGraph4(.zip/.gz OK) files\ninto stair step/ heat map useq archives. Span parameters are not supported.\n\n-f The full path directory/file text for your xxx.wig(.gz/.zip OK) file(s).\n-v Genome version (e.g. H_sapiens_Mar_2006), get from UCSC Browser,\n      http://genome.ucsc.edu/FAQ/FAQreleases\n-s Skip wig lines with designated value/score.\n-i Index size for slicing split chromosome data (e.g. # rows per file), defaults to\n      100000.\n-r Initial graph style, defaults to 1\n" + (Object)sb + "-h Initial graph color, hexadecimal (e.g. #6633FF), enclose in quotations!\n" + "-d Description, enclose in quotations! \n" + "\nExample: java -Xmx1G -jar path2/Apps/Wig2USeq -f /WigFiles/ -v H_sapiens_Feb_2009\n\n" + "**************************************************************************************\n");
    }
}
