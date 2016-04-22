// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.parsers.useq.apps;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.IOException;
import java.util.Iterator;
import java.util.HashMap;
import java.io.Writer;
import java.io.PrintWriter;
import java.io.FileWriter;
import com.affymetrix.genometryImpl.parsers.useq.USeqUtilities;
import java.util.ArrayList;
import com.affymetrix.genometryImpl.parsers.useq.USeqArchive;
import java.io.File;

public class USeq2UCSCBig extends Thread
{
    private File[] useqArchives;
    private File workingUSeqArchiveFile;
    private USeqArchive workingUSeqArchive;
    private File ucscWig2BigWig;
    private File ucscBed2BigBed;
    private boolean verbose;
    private int lengthExtender;
    private File chromLengths;
    private File convertedFile;
    private File tempFile;
    private File tempFileSorted;
    
    public USeq2UCSCBig(final String[] args) {
        this.verbose = false;
        this.lengthExtender = 10000;
        try {
            this.verbose = true;
            this.processArgs(args);
            for (final File u : this.useqArchives) {
                this.workingUSeqArchiveFile = u;
                if (this.verbose) {
                    System.out.println("Processing: " + this.workingUSeqArchiveFile.getName());
                }
                this.convert();
            }
            if (this.verbose) {
                System.out.println("\nDone!\n");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public USeq2UCSCBig(final File ucscWig2BigWig, final File ucscBed2BigBed, final File useq) {
        this.verbose = false;
        this.lengthExtender = 10000;
        this.ucscWig2BigWig = ucscWig2BigWig;
        this.ucscBed2BigBed = ucscBed2BigBed;
        this.workingUSeqArchiveFile = useq;
    }
    
    public ArrayList<File> convert() throws Exception {
        try {
            this.workingUSeqArchive = new USeqArchive(this.workingUSeqArchiveFile);
            this.writeChromLengths();
            if (this.workingUSeqArchive.getArchiveInfo().isGraphData()) {
                return this.convertGraphData();
            }
            return this.convertRegionData();
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    @Override
    public void run() {
        try {
            this.convert();
        }
        catch (Exception e) {
            System.err.println("\nERROR: failed to convert useq file to big file!\n");
            e.printStackTrace();
        }
    }
    
    public ArrayList<File> fetchConvertedFileNames() throws Exception {
        this.workingUSeqArchive = new USeqArchive(this.workingUSeqArchiveFile);
        if (this.workingUSeqArchive.getArchiveInfo().isGraphData()) {
            return this.fetchConvertedGraphNames();
        }
        return this.fetchConvertRegionNames();
    }
    
    private ArrayList<File> fetchConvertRegionNames() {
        final String name = this.workingUSeqArchiveFile.getName().replace(".useq", "");
        final File convertedFile = new File(this.workingUSeqArchiveFile.getParentFile(), name + ".bb");
        final ArrayList<File> al = new ArrayList<File>();
        al.add(convertedFile);
        return al;
    }
    
    private ArrayList<File> convertRegionData() throws Exception {
        final String name = this.workingUSeqArchiveFile.getName().replace(".useq", "");
        (this.tempFile = new File(this.workingUSeqArchiveFile.getCanonicalPath() + ".bed")).deleteOnExit();
        USeq2Text.print2TextFile(this.workingUSeqArchiveFile, this.tempFile, true, true);
        (this.tempFileSorted = new File(this.workingUSeqArchiveFile.getCanonicalPath() + ".sorted.bed")).deleteOnExit();
        final String shell = "sort -k1,1 -k2,2n " + this.tempFile.getCanonicalPath() + " > " + this.tempFileSorted.getCanonicalPath();
        USeqUtilities.executeShellScript(shell, this.workingUSeqArchiveFile.getParentFile());
        this.convertedFile = new File(this.workingUSeqArchiveFile.getParentFile(), name + ".bb");
        final String[] command = { this.ucscBed2BigBed.getCanonicalPath(), this.tempFileSorted.getCanonicalPath(), this.chromLengths.getCanonicalPath(), this.convertedFile.getCanonicalPath() };
        this.executeUCSCCommand(command);
        this.deleteTempFiles();
        final ArrayList<File> al = new ArrayList<File>();
        al.add(this.convertedFile);
        return al;
    }
    
    private ArrayList<File> fetchConvertedGraphNames() {
        final String name = this.workingUSeqArchiveFile.getName().replace(".useq", "");
        final ArrayList<File> convertedFiles = new ArrayList<File>();
        final boolean stranded = this.workingUSeqArchive.isStranded();
        if (stranded) {
            File convertedFile = new File(this.workingUSeqArchiveFile.getParentFile(), name + "_Plus.bw");
            convertedFiles.add(convertedFile);
            convertedFile = new File(this.workingUSeqArchiveFile.getParentFile(), name + "_Minus.bw");
            convertedFiles.add(convertedFile);
        }
        else {
            final File convertedFile = new File(this.workingUSeqArchiveFile.getParentFile(), name + ".bw");
            convertedFiles.add(convertedFile);
        }
        return convertedFiles;
    }
    
    private ArrayList<File> convertGraphData() throws Exception {
        final USeq2Text useq2Text = new USeq2Text();
        useq2Text.setPrintWigFormat(true);
        final String name = this.workingUSeqArchiveFile.getName().replace(".useq", "");
        final ArrayList<File> convertedFiles = new ArrayList<File>();
        (this.tempFile = new File(this.workingUSeqArchiveFile.getCanonicalPath() + ".wig")).deleteOnExit();
        final boolean stranded = this.workingUSeqArchive.isStranded();
        if (stranded) {
            useq2Text.print2WigFile(this.workingUSeqArchiveFile, this.tempFile, "+");
            this.convertedFile = new File(this.workingUSeqArchiveFile.getParentFile(), name + "_Plus.bw");
            final String[] command = { this.ucscWig2BigWig.getCanonicalPath(), this.tempFile.getCanonicalPath(), this.chromLengths.getCanonicalPath(), this.convertedFile.getCanonicalPath() };
            this.executeUCSCCommand(command);
            convertedFiles.add(this.convertedFile);
            useq2Text.print2WigFile(this.workingUSeqArchiveFile, this.tempFile, "-");
            this.convertedFile = new File(this.workingUSeqArchiveFile.getParentFile(), name + "_Minus.bw");
            command[3] = this.convertedFile.getCanonicalPath();
            this.executeUCSCCommand(command);
            convertedFiles.add(this.convertedFile);
        }
        else {
            useq2Text.print2WigFile(this.workingUSeqArchiveFile, this.tempFile, null);
            this.convertedFile = new File(this.workingUSeqArchiveFile.getParentFile(), name + ".bw");
            final String[] command = { this.ucscWig2BigWig.getCanonicalPath(), this.tempFile.getCanonicalPath(), this.chromLengths.getCanonicalPath(), this.convertedFile.getCanonicalPath() };
            this.executeUCSCCommand(command);
            convertedFiles.add(this.convertedFile);
        }
        this.deleteTempFiles();
        return convertedFiles;
    }
    
    private void executeUCSCCommand(final String[] command) throws Exception {
        if (this.verbose) {
            System.out.println("\nUnix Command:");
            for (final String c : command) {
                System.out.println(c);
            }
            System.out.println();
        }
        final String[] results = USeqUtilities.executeCommandLineReturnAll(command);
        if (results.length != 0) {
            boolean ok = true;
            final StringBuilder sb = new StringBuilder("Error message:");
            for (final String c2 : results) {
                sb.append("\n");
                sb.append(c2);
                if (!c2.contains("millis")) {
                    ok = false;
                }
            }
            if (!ok) {
                this.deleteAllFiles();
                throw new Exception(sb.toString());
            }
        }
    }
    
    private void writeChromLengths() throws IOException {
        final HashMap<String, Integer> nameBase = this.workingUSeqArchive.fetchChromosomesAndLastBase();
        (this.chromLengths = new File(this.workingUSeqArchive.getZipFile() + ".chromLengths")).deleteOnExit();
        final PrintWriter out = new PrintWriter(new FileWriter(this.chromLengths));
        for (final String name : nameBase.keySet()) {
            final int length = nameBase.get(name) + this.lengthExtender;
            out.print(name);
            out.print("\t");
            out.println(length);
        }
        out.close();
    }
    
    public void deleteAllFiles() {
        this.deleteTempFiles();
        if (this.convertedFile != null) {
            this.convertedFile.delete();
        }
    }
    
    public void deleteTempFiles() {
        if (this.tempFile != null) {
            this.tempFile.delete();
        }
        if (this.chromLengths != null) {
            this.chromLengths.delete();
        }
        if (this.tempFileSorted != null) {
            this.tempFileSorted.delete();
        }
    }
    
    public static void main(final String[] args) {
        if (args.length == 0) {
            printDocs();
            System.exit(0);
        }
        new USeq2UCSCBig(args);
    }
    
    public void processArgs(final String[] args) {
        final Pattern pat = Pattern.compile("-[a-z]");
        if (this.verbose) {
            System.out.println("\nArguments: " + USeqUtilities.stringArrayToString(args, " ") + "\n");
        }
        File ucscDir = null;
        for (int i = 0; i < args.length; ++i) {
            final String lcArg = args[i].toLowerCase();
            final Matcher mat = pat.matcher(lcArg);
            if (mat.matches()) {
                final char test = args[i].charAt(1);
                try {
                    switch (test) {
                        case 'u': {
                            this.useqArchives = USeqUtilities.fetchFilesRecursively(new File(args[++i]), ".useq");
                            break;
                        }
                        case 'd': {
                            ucscDir = new File(args[++i]);
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
        if (ucscDir == null || !ucscDir.isDirectory()) {
            USeqUtilities.printExit("\nCannot find your directory containing the UCSC wig2BigWig and bed2BigBed apps -> " + ucscDir);
        }
        this.ucscWig2BigWig = new File(ucscDir, "wigToBigWig");
        this.ucscBed2BigBed = new File(ucscDir, "bedToBigBed");
        if (this.useqArchives == null || this.useqArchives.length == 0) {
            USeqUtilities.printExit("\nCannot find any xxx.useq USeq archives?\n");
        }
        if (!this.ucscWig2BigWig.canExecute()) {
            USeqUtilities.printExit("\nCannot find or execute -> " + this.ucscWig2BigWig + "\n");
        }
        if (!this.ucscBed2BigBed.canExecute()) {
            USeqUtilities.printExit("\nCannot find or execute -> " + this.ucscBed2BigBed + "\n");
        }
    }
    
    public static void printDocs() {
        System.out.println("\n**************************************************************************************\n**                              USeq 2 UCSC Big: Aug 2011                           **\n**************************************************************************************\nConverts USeq archives to UCSC bigWig (xxx.bw) or bigBed (xxx.bb) archives based on\nthe data type. WARNING: bigBed format conversion will clip any associated scores to\nbetween 0-1000. \n\nOptions:\n-u Full path file/directory containing xxx.useq files. Recurses through sub \n       if a directory is given.\n-d Full path directory containing the UCSC wigToBigWig and bedToBigBed apps, download\n       from http://hgdownload.cse.ucsc.edu/admin/exe/\n\nExample: java -Xmx4G -jar pathTo/USeq/Apps/USeq2UCSCBig -u\n      /AnalysisResults/USeqDataArchives/ -d /Apps/UCSC/\n\n**************************************************************************************\n");
    }
}
