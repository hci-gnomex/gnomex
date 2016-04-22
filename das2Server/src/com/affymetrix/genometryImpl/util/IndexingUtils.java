//
// Decompiled by Procyon v0.5.30
//

package com.affymetrix.genometryImpl.util;

import com.affymetrix.genometryImpl.SeqSpan;
import com.affymetrix.genometryImpl.span.SimpleSeqSpan;
import java.util.BitSet;
import java.util.Arrays;
import com.affymetrix.genometryImpl.parsers.AbstractPSLParser;
import java.io.ByteArrayInputStream;
import java.nio.MappedByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.nio.channels.FileChannel;
import java.io.FileInputStream;
import java.util.Collections;
import com.affymetrix.genometryImpl.symmetry.UcscPslSym;
import java.util.ArrayList;
import java.io.FileNotFoundException;
import java.util.Collection;
import com.affymetrix.genometryImpl.parsers.ProbeSetDisplayPlugin;
import com.affymetrix.genometryImpl.symloader.PSL;
import com.affymetrix.genometryImpl.parsers.PSLParser;
import java.io.DataOutputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.ByteArrayOutputStream;
import com.affymetrix.genometryImpl.symmetry.SimpleSymWithProps;
import java.util.regex.Matcher;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;
import java.io.IOException;
import java.util.Iterator;
import java.util.Comparator;
import com.affymetrix.genometryImpl.parsers.IndexWriter;
import com.affymetrix.genometryImpl.symmetry.SeqSymmetry;
import java.util.List;
import com.affymetrix.genometryImpl.BioSeq;
import com.affymetrix.genometryImpl.AnnotatedSeqGroup;
import java.io.File;

public final class IndexingUtils
{
    private static final boolean DEBUG = false;

    static String indexedFileName(final String dataRoot, final File file, final String annot_name, final AnnotatedSeqGroup genome, final BioSeq seq) {
        final String retVal = indexedDirName(dataRoot, genome, seq) + "/";
        final String fullPath = file.getPath().replace("\\", "/");
        String fullDirName = dataRoot + genomeDirName(genome);
        if (!fullDirName.endsWith("/")) {
            fullDirName += "/";
        }
        if (fullPath.indexOf(fullDirName) >= 0) {
            final String shortenedPath = fullPath.replace(fullDirName, "");
            return retVal + shortenedPath;
        }
        return retVal + annot_name + "_indexed";
    }

    static String indexedDirName(final String dataRoot, final AnnotatedSeqGroup genome, final BioSeq seq) {
        return indexedGenomeDirName(dataRoot, genome) + "/" + seq.getID();
    }

    static String genomeDirName(final AnnotatedSeqGroup genome) {
        return genome.getOrganism() + "/" + genome.getID();
    }

    static String indexedGenomeDirName(final String dataRoot, final AnnotatedSeqGroup genome) {
        final String optimizedDirectory = dataRoot + ".indexed";
        return optimizedDirectory + "/" + genomeDirName(genome);
    }

    public static void determineIndexes(final AnnotatedSeqGroup originalGenome, final AnnotatedSeqGroup tempGenome, final String dataRoot, final File file, final List<? extends SeqSymmetry> loadedSyms, final IndexWriter iWriter, final String typeName, final String returnTypeName, final String ext) throws IOException {
        for (final BioSeq originalSeq : originalGenome.getSeqList()) {
            final BioSeq tempSeq = tempGenome.getSeq(originalSeq.getID());
            if (tempSeq == null) {
                continue;
            }
            final List<SeqSymmetry> sortedSyms = getSortedAnnotationsForChrom(loadedSyms, tempSeq, iWriter.getComparator(tempSeq));
            final String indexedAnnotationsFileName = indexedFileName(dataRoot, file, typeName, tempGenome, tempSeq);
            final String dirName = indexedAnnotationsFileName.substring(0, indexedAnnotationsFileName.lastIndexOf("/"));
            ServerUtils.createDirIfNecessary(dirName);
            final File indexedAnnotationsFile = new File(indexedAnnotationsFileName);
            indexedAnnotationsFile.deleteOnExit();
            final IndexedSyms iSyms = new IndexedSyms(sortedSyms.size(), indexedAnnotationsFile, typeName, ext, iWriter);
            originalSeq.addIndexedSyms(returnTypeName, iSyms);
            writeIndexedAnnotations(sortedSyms, tempSeq, tempGenome, iSyms);
        }
    }

    public static Set<SeqSymmetry> findSymsByName(final AnnotatedSeqGroup genome, final Pattern regex) {
        final Matcher matcher = regex.matcher("");
        final Set<SeqSymmetry> results = new HashSet<SeqSymmetry>(100000);
        final int resultCount = 0;
    Label_0129:
        for (final BioSeq seq : genome.getSeqList()) {
            for (final String type : seq.getIndexedTypeList()) {
                final IndexedSyms iSyms = seq.getIndexedSym(type);
                if (iSyms == null) {
                    continue;
                }
                if (findSymByName(iSyms, matcher, seq, type, results, resultCount)) {
                    break Label_0129;
                }
            }
        }
        return results;
    }

    private static boolean findSymByName(final IndexedSyms iSyms, final Matcher matcher, final BioSeq seq, final String type, final Set<SeqSymmetry> results, int resultCount) {
        for (int symSize = iSyms.min.length, i = 0; i < symSize; ++i) {
            final byte[][] ids = iSyms.id[i];
            boolean foundID = false;
            for (int idLength = (ids == null) ? 0 : ids.length, j = 0; j < idLength; ++j) {
                final String id = new String(ids[j]);
                matcher.reset(id);
                if (matcher.matches()) {
                    foundID = true;
                    break;
                }
            }
            if (foundID) {
                final SimpleSymWithProps sym = iSyms.convertToSymWithProps(i, seq, type);
                results.add(sym);
                ++resultCount;
            }
        }
        return false;
    }

    public static void writeIndexedAnnotations(final List<SeqSymmetry> syms, final BioSeq seq, final AnnotatedSeqGroup group, final IndexedSyms iSyms) throws IOException {
        createIndexArray(iSyms, syms, seq, group);
        writeIndex(iSyms, syms, seq);
    }

    private static void createIndexArray(final IndexedSyms iSyms, final List<SeqSymmetry> syms, final BioSeq seq, final AnnotatedSeqGroup group) throws IOException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int index = 0;
        long currentFilePos = 0L;
        final IndexWriter iWriter = iSyms.iWriter;
        iSyms.filePos[0] = 0L;
        for (final SeqSymmetry sym : syms) {
            iWriter.writeSymmetry(sym, seq, baos);
            baos.flush();
            final byte[] buf = baos.toByteArray();
            baos.reset();
            iSyms.setIDs(group, sym.getID(), index);
            iSyms.min[index] = iWriter.getMin(sym, seq);
            iSyms.max[index] = iWriter.getMax(sym, seq);
            iSyms.forward.set(index, sym.getSpan(seq).isForward());
            currentFilePos += buf.length;
            ++index;
            iSyms.filePos[index] = currentFilePos;
        }
        GeneralUtils.safeClose(baos);
    }

    private static void writeIndex(final IndexedSyms iSyms, final List<SeqSymmetry> syms, final BioSeq seq) throws IOException {
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        DataOutputStream dos = null;
        try {
            fos = new FileOutputStream(iSyms.file.getAbsoluteFile());
            bos = new BufferedOutputStream(fos);
            dos = new DataOutputStream(bos);
            final IndexWriter iSymWriter = iSyms.iWriter;
            for (final SeqSymmetry sym : syms) {
                iSymWriter.writeSymmetry(sym, seq, dos);
            }
            if ((iSymWriter instanceof PSLParser || iSymWriter instanceof PSL) && iSyms.ext.equalsIgnoreCase("link.psl")) {
                writeAdditionalLinkPSLIndex(iSyms.file.getAbsolutePath(), syms, seq, iSyms.typeName);
            }
        }
        finally {
            GeneralUtils.safeClose(dos);
            GeneralUtils.safeClose(bos);
            GeneralUtils.safeClose(fos);
        }
    }

    private static void writeAdditionalLinkPSLIndex(final String indexesFileName, final List<SeqSymmetry> syms, final BioSeq seq, final String typeName) throws FileNotFoundException {
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        DataOutputStream dos = null;
        final String secondIndexesFileName = indexesFileName + ".link2.psl";
        try {
            fos = new FileOutputStream(secondIndexesFileName);
            bos = new BufferedOutputStream(fos);
            dos = new DataOutputStream(bos);
            ProbeSetDisplayPlugin.collectAndWriteAnnotations(syms, false, seq, typeName, dos);
        }
        finally {
            GeneralUtils.safeClose(fos);
            GeneralUtils.safeClose(dos);
            GeneralUtils.safeClose(bos);
        }
    }

    public static List<SeqSymmetry> getSortedAnnotationsForChrom(final List syms, final BioSeq seq, final Comparator comp) {
        final List<SeqSymmetry> results = new ArrayList<SeqSymmetry>(10000);
        for (int symSize = syms.size(), i = 0; i < symSize; ++i) {
            final SeqSymmetry sym = (SeqSymmetry) syms.get(i);
            if (sym instanceof UcscPslSym) {
                if (((UcscPslSym)sym).getTargetSeq() == seq) {
                    results.add(sym);
                }
            }
            else if (sym.getSpan(seq) != null) {
                results.add(sym);
            }
        }
        Collections.sort(results, comp);
        return results;
    }

    public static byte[] readBytesFromFile(final File file, final long filePosStart, int length) {
        byte[] contentsOnly = null;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            if (file.length() < length) {
                System.out.println("WARNING: filesize " + file.length() + " was less than argument " + length);
                length = (int)file.length();
            }
            final FileChannel fc = fis.getChannel();
            final MappedByteBuffer mbb = fc.map(FileChannel.MapMode.READ_ONLY, filePosStart, length);
            contentsOnly = new byte[length];
            mbb.get(contentsOnly);
        }
        catch (IOException ex) {
            Logger.getLogger(IndexingUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally {
            GeneralUtils.safeClose(fis);
        }
        return contentsOnly;
    }

    static ByteArrayInputStream readAdditionalLinkPSLIndex(final String indexesFileName, final String annot_type, byte[] bytes1) throws IOException {
        final String secondIndexesFileName = indexesFileName + ".link2.psl";
        final File secondIndexesFile = new File(secondIndexesFileName);
        final int bytes2Len = (int)secondIndexesFile.length();
        byte[] bytes2 = AbstractPSLParser.trackLine(annot_type, "Consensus Sequences").getBytes();
        final int bytes0Len = bytes2.length;
        final int bytes1Len = bytes1.length;
        final byte[] combinedByteArr = new byte[bytes0Len + bytes1Len + bytes2Len];
        System.arraycopy(bytes2, 0, combinedByteArr, 0, bytes0Len);
        bytes2 = null;
        System.arraycopy(bytes1, 0, combinedByteArr, bytes0Len, bytes1Len);
        bytes1 = null;
        byte[] bytes3 = readBytesFromFile(secondIndexesFile, 0L, bytes2Len);
        System.arraycopy(bytes3, 0, combinedByteArr, bytes0Len + bytes1Len, bytes2Len);
        bytes3 = null;
        return new ByteArrayInputStream(combinedByteArr);
    }

    public static void findMaxOverlap(final int[] overlapRange, final int[] outputRange, final int[] min, final int[] max) {
        final int minStart = findMinimaGreaterOrEqual(min, overlapRange[0]);
        final int correctedMinStart = backtrackForHalfInIntervals(minStart, max, overlapRange[0]);
        outputRange[0] = correctedMinStart;
        final int maxEnd = findMaximaLessOrEqual(min, overlapRange[1]);
        outputRange[1] = maxEnd;
    }

    private static int findMinimaGreaterOrEqual(final int[] min, final int elt) {
        int tempPos = Arrays.binarySearch(min, elt);
        if (tempPos >= 0) {
            tempPos = backTrack(min, tempPos);
        }
        else {
            tempPos = -(tempPos + 1);
            tempPos = Math.min(min.length - 1, tempPos);
        }
        return tempPos;
    }

    private static int findMaximaLessOrEqual(final int[] min, final int elt) {
        int tempPos = Arrays.binarySearch(min, elt);
        if (tempPos >= 0) {
            tempPos = forwardtrack(min, tempPos);
        }
        else {
            tempPos = -(tempPos + 1);
            if (tempPos > 0) {
                --tempPos;
            }
            tempPos = Math.min(min.length - 1, tempPos);
        }
        return tempPos;
    }

    public static int backTrack(final int[] arr, int pos) {
        while (pos > 0 && arr[pos - 1] == arr[pos]) {
            --pos;
        }
        return pos;
    }

    public static int forwardtrack(final int[] arr, int pos) {
        while (pos < arr.length - 1 && arr[pos + 1] == arr[pos]) {
            ++pos;
        }
        return pos;
    }

    private static int backtrackForHalfInIntervals(final int minStart, final int[] max, final int overlapStart) {
        int minVal = minStart;
        for (int i = minStart - 1; i >= 0; --i) {
            if (max[i] >= overlapStart) {
                minVal = i;
            }
        }
        return minVal;
    }

    public static File createIndexedFile(final int pointCount, final int[] x, final float[] y, final int[] w) {
        File bufVal = null;
        DataOutputStream dos = null;
        try {
            bufVal = File.createTempFile((Math.random() + "").substring(2), "idx");
            bufVal.deleteOnExit();
            dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(bufVal)));
            for (int i = 0; i < pointCount; ++i) {
                dos.writeInt(x[i]);
                dos.writeFloat(y[i]);
                dos.writeInt((w == null) ? 1 : w[i]);
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        finally {
            GeneralUtils.safeClose(dos);
        }
        return bufVal;
    }

    public static final class IndexedSyms
    {
        final File file;
        public final int[] min;
        public final int[] max;
        public final String ext;
        private final BitSet forward;
        public final long[] filePos;
        private final String typeName;
        final IndexWriter iWriter;
        private final byte[][][] id;

        public IndexedSyms(final int resultSize, final File file, final String typeName, final String ext, final IndexWriter iWriter) {
            this.min = new int[resultSize];
            this.max = new int[resultSize];
            this.forward = new BitSet(resultSize);
            this.id = new byte[resultSize][][];
            this.filePos = new long[resultSize + 1];
            this.file = file;
            this.typeName = typeName;
            this.iWriter = iWriter;
            this.ext = ext;
        }

        private void setIDs(final AnnotatedSeqGroup group, final String symID, final int i) {
            if (symID == null) {
                this.id[i] = null;
                return;
            }
            final Set<String> extraNames = group.getSymmetryIDs(symID.toLowerCase());
            final List<String> ids = new ArrayList<String>(1 + ((extraNames == null) ? 0 : extraNames.size()));
            ids.add(symID);
            if (extraNames != null) {
                ids.addAll(extraNames);
            }
            final int idSize = ids.size();
            this.id[i] = new byte[idSize][];
            for (int j = 0; j < idSize; ++j) {
                this.id[i][j] = ids.get(j).getBytes();
            }
        }

        private SimpleSymWithProps convertToSymWithProps(final int i, final BioSeq seq, final String type) {
            final SimpleSymWithProps sym = new SimpleSymWithProps();
            final String id = (this.id[i] == null) ? "" : new String(this.id[i][0]);
            sym.setID(id);
            sym.setProperty("name", id);
            sym.setProperty("method", type);
            if (this.forward.get(i)) {
                sym.addSpan(new SimpleSeqSpan(this.min[i], this.max[i], seq));
            }
            else {
                sym.addSpan(new SimpleSeqSpan(this.max[i], this.min[i], seq));
            }
            return sym;
        }
    }
}
