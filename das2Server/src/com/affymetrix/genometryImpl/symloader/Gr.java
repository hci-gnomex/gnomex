//
// Decompiled by Procyon v0.5.30
//

package com.affymetrix.genometryImpl.symloader;

import java.util.Iterator;
import com.affymetrix.genometryImpl.symmetry.SeqSymmetry;
import java.util.Collection;
import cern.colt.GenericSorting;
import cern.colt.function.IntComparator;
import cern.colt.Swapper;
import java.util.Arrays;
import java.util.logging.Logger;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.FileInputStream;
import cern.colt.list.FloatArrayList;
import cern.colt.list.IntArrayList;
import java.io.IOException;
import java.io.DataOutputStream;
import java.io.BufferedOutputStream;
import com.affymetrix.genometryImpl.SeqSpan;
import java.util.ArrayList;
import com.affymetrix.genometryImpl.symmetry.GraphSym;
import com.affymetrix.genometryImpl.util.GeneralUtils;
import java.io.OutputStream;
import java.io.FileOutputStream;
import com.affymetrix.genometryImpl.util.LocalUrlCacher;
import com.affymetrix.genometryImpl.AnnotatedSeqGroup;
import java.net.URI;
import com.affymetrix.genometryImpl.util.LoadUtils;
import java.util.List;
import java.util.Set;
import com.affymetrix.genometryImpl.BioSeq;
import java.io.File;
import com.affymetrix.genometryImpl.parsers.AnnotationWriter;

public final class Gr extends SymLoader implements AnnotationWriter
{
    private File f;
    private boolean isSorted;
    private File tempFile;
    private static final String UNNAMED = "unnamed";
    private BioSeq unnamed;
    private static final List<LoadUtils.LoadStrategy> strategyList;

    public Gr(final URI uri, final String featureName, final AnnotatedSeqGroup seq_group) {
        super(uri, featureName, seq_group);
        this.isSorted = false;
        this.tempFile = null;
    }

    public void init() throws Exception {
        if (this.isInitialized) {
            return;
        }
        super.init();
        this.f = LocalUrlCacher.convertURIToFile(this.uri);
        this.sort();
    }

    private void sort() throws Exception {
        this.unnamed = this.group.getSeq("unnamed");
        if (this.unnamed == null) {
            this.unnamed = new BioSeq("unnamed", null, 0);
        }
        final GraphSym sym = this.parse(this.unnamed, Integer.MIN_VALUE, Integer.MAX_VALUE);
        if (!this.isSorted) {
            FileOutputStream fos = null;
            try {
                (this.tempFile = File.createTempFile(this.f.getName(), ".gr")).deleteOnExit();
                fos = new FileOutputStream(this.tempFile);
                writeGrFormat(sym, fos);
            }
            catch (Exception ex) {
                throw ex;
            }
            finally {
                GeneralUtils.safeClose(fos);
            }
        }
        this.isSorted = true;
        if (this.unnamed.getLength() < sym.getMaxXCoord()) {
            this.unnamed.setLength(sym.getMaxXCoord());
        }
    }

    @Override
    public List<BioSeq> getChromosomeList() throws Exception {
        this.init();
        List<BioSeq> seqs = this.group.getSeqList();
        if (!seqs.isEmpty()) {
            return seqs;
        }
        seqs = new ArrayList<BioSeq>();
        seqs.add(this.unnamed);
        return seqs;
    }

    @Override
    public List<LoadUtils.LoadStrategy> getLoadChoices() {
        return Gr.strategyList;
    }

    @Override
    public List<GraphSym> getGenome() throws Exception {
        this.init();
        final BioSeq seq = this.group.addSeq(this.featureName, 2147483646, this.uri.toString());
        return this.getChromosome(seq);
    }

    @Override
    public List<GraphSym> getChromosome(final BioSeq seq) throws Exception {
        this.init();
        final List<GraphSym> results = new ArrayList<GraphSym>();
        results.add(this.parse(seq, seq.getMin(), seq.getMax() + 1));
        return results;
    }

    @Override
    public List<GraphSym> getRegion(final SeqSpan span) throws Exception {
        this.init();
        final List<GraphSym> results = new ArrayList<GraphSym>();
        results.add(this.parse(span.getBioSeq(), span.getMin(), span.getMax() + 1));
        return results;
    }

    @Override
    public String getMimeType() {
        return "text/gr";
    }

    public static boolean writeGrFormat(final GraphSym graf, final OutputStream ostr) throws IOException {
        BufferedOutputStream bos = null;
        DataOutputStream dos = null;
        try {
            bos = new BufferedOutputStream(ostr);
            dos = new DataOutputStream(bos);
            writeGraphPoints(graf, dos);
        }
        finally {
            GeneralUtils.safeClose(dos);
        }
        return true;
    }

    private static void writeGraphPoints(final GraphSym graf, final DataOutputStream dos) throws IOException {
        for (int total_points = graf.getPointCount(), i = 0; i < total_points; ++i) {
            dos.writeBytes("" + graf.getGraphXCoord(i) + "\t" + graf.getGraphYCoordString(i) + "\n");
        }
    }

    private GraphSym parse(final BioSeq aseq, final int min, final int max) throws Exception {
        GraphSym graf = null;
        String line = null;
        String headerstr = null;
        final String name = this.uri.toString();
        boolean hasHeader = false;
        int count = 0;
        final IntArrayList xlist = new IntArrayList();
        final FloatArrayList ylist = new FloatArrayList();
        FileInputStream fis = null;
        InputStream is = null;
        BufferedReader br = null;
        try {
            if (this.tempFile != null) {
                fis = new FileInputStream(this.tempFile);
            }
            else {
                fis = new FileInputStream(this.f);
            }
            is = GeneralUtils.unzipStream(fis, this.f.getName(), new StringBuffer());
            br = new BufferedReader(new InputStreamReader(is));
            line = br.readLine();
            if (line == null) {
                System.out.println("can't find any data in file!");
                return null;
            }
            try {
                int firstx;
                float firsty;
                if (line.indexOf(32) > 0) {
                    firstx = Integer.parseInt(line.substring(0, line.indexOf(32)));
                    firsty = Float.parseFloat(line.substring(line.indexOf(32) + 1));
                }
                else {
                    if (line.indexOf(9) <= 0) {
                        System.out.println("format not recognized");
                        return null;
                    }
                    firstx = Integer.parseInt(line.substring(0, line.indexOf(9)));
                    firsty = Float.parseFloat(line.substring(line.indexOf(9) + 1));
                }
                if (firstx >= min && firstx < max) {
                    xlist.add(firstx);
                    ylist.add(firsty);
                    ++count;
                }
            }
            catch (Exception ex) {
                headerstr = line;
                System.out.println("Found header on graph file: " + line);
                hasHeader = true;
            }
            int x = 0;
            float y = 0.0f;
            int xprev = Integer.MIN_VALUE;
            boolean sorted = true;
            while ((line = br.readLine()) != null) {
                int indexOfDelimiter = line.indexOf(32);
                if (indexOfDelimiter > 0) {
                    x = Integer.parseInt(line.substring(0, indexOfDelimiter));
                    y = Float.parseFloat(line.substring(indexOfDelimiter + 1));
                }
                else {
                    indexOfDelimiter = line.indexOf(9);
                    if (indexOfDelimiter <= 0) {
                        Logger.getLogger(Gr.class.getName()).warning("Line " + line + " doesn't match... ignoring");
                        continue;
                    }
                    x = Integer.parseInt(line.substring(0, indexOfDelimiter));
                    y = Float.parseFloat(line.substring(indexOfDelimiter + 1));
                }
                if (x >= max) {
                    if (this.isSorted) {
                        break;
                    }
                    continue;
                }
                else {
                    if (x < min) {
                        continue;
                    }
                    xlist.add(x);
                    ylist.add(y);
                    ++count;
                    if (this.isSorted) {
                        continue;
                    }
                    if (xprev > x) {
                        sorted = false;
                    }
                    else {
                        xprev = x;
                    }
                }
            }
            this.isSorted = sorted;
            graf = this.createResults(name, hasHeader, headerstr, xlist, ylist, sorted, aseq);
            System.out.println("loaded graph data, total points = " + count);
            return graf;
        }
        catch (Exception ex) {
            throw ex;
        }
        finally {
            GeneralUtils.safeClose(br);
        }
    }

    private GraphSym createResults(String name, final boolean hasHeader, final String headerstr, IntArrayList xlist, FloatArrayList ylist, final boolean sorted, final BioSeq aseq) {
        if (name == null && hasHeader) {
            name = headerstr;
        }
        final int[] xcoords = Arrays.copyOf(xlist.elements(), xlist.size());
        xlist = null;
        final float[] ycoords = Arrays.copyOf(ylist.elements(), ylist.size());
        ylist = null;
        if (!sorted) {
            System.err.println("input graph not sorted, sorting by base coord");
            sortXYDataOnX(xcoords, ycoords);
        }
        final GraphSym graf = new GraphSym(xcoords, ycoords, name, aseq);
        return graf;
    }

    private static void sortXYDataOnX(final int[] xList, final float[] yList) {
        final Swapper swapper = (Swapper)new Swapper() {
            public void swap(final int a, final int b) {
                final int swapInt = xList[a];
                xList[a] = xList[b];
                xList[b] = swapInt;
                final float swapFloat = yList[a];
                yList[a] = yList[b];
                yList[b] = swapFloat;
            }
        };
        final IntComparator comp = (IntComparator)new IntComparator() {
            public int compare(final int a, final int b) {
                return Integer.valueOf(xList[a]).compareTo(Integer.valueOf(xList[b]));
            }
        };
        GenericSorting.quickSort(0, xList.length, comp, swapper);
    }

    @Override
    public boolean writeAnnotations(final Collection<? extends SeqSymmetry> syms, final BioSeq seq, final String type, final OutputStream ostr) throws IOException {
        BufferedOutputStream bos = null;
        DataOutputStream dos = null;
        try {
            bos = new BufferedOutputStream(ostr);
            dos = new DataOutputStream(bos);
            for (final GraphSym graf : (Set<GraphSym>)syms) {
                writeGraphPoints(graf, dos);
            }
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            GeneralUtils.safeClose(dos);
        }
        return false;
    }

    static {
        (strategyList = new ArrayList<LoadUtils.LoadStrategy>()).add(LoadUtils.LoadStrategy.NO_LOAD);
        Gr.strategyList.add(LoadUtils.LoadStrategy.VISIBLE);
    }
}
