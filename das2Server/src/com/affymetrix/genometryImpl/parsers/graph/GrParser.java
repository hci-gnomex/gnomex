// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.parsers.graph;

import java.io.FileOutputStream;
import com.affymetrix.genometryImpl.GenometryModel;
import com.affymetrix.genometryImpl.symmetry.SeqSymmetry;
import java.util.List;
import cern.colt.GenericSorting;
import cern.colt.function.IntComparator;
import cern.colt.Swapper;
import com.affymetrix.genometryImpl.AnnotatedSeqGroup;
import java.util.Arrays;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import cern.colt.list.FloatArrayList;
import cern.colt.list.IntArrayList;
import com.affymetrix.genometryImpl.BioSeq;
import java.io.InputStream;
import java.io.IOException;
import com.affymetrix.genometryImpl.util.GeneralUtils;
import java.io.DataOutputStream;
import java.io.BufferedOutputStream;
import java.io.OutputStream;
import com.affymetrix.genometryImpl.symmetry.GraphSym;

public final class GrParser implements GraphParser
{
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
    
    public static GraphSym parse(final InputStream istr, final BioSeq aseq, final String name) throws IOException {
        return parse(istr, aseq, name, true);
    }
    
    public static GraphSym parse(final InputStream istr, final BioSeq aseq, String name, final boolean ensure_unique_id) throws IOException {
        GraphSym graf = null;
        String line = null;
        String headerstr = null;
        boolean hasHeader = false;
        int count = 0;
        IntArrayList xlist = new IntArrayList();
        FloatArrayList ylist = new FloatArrayList();
        final InputStreamReader isr = new InputStreamReader(istr);
        final BufferedReader br = new BufferedReader(isr);
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
            xlist.add(firstx);
            ylist.add(firsty);
            ++count;
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
            if (line.indexOf(32) > 0) {
                x = Integer.parseInt(line.substring(0, line.indexOf(32)));
                y = Float.parseFloat(line.substring(line.indexOf(32) + 1));
            }
            else if (line.indexOf(9) > 0) {
                x = Integer.parseInt(line.substring(0, line.indexOf(9)));
                y = Float.parseFloat(line.substring(line.indexOf(9) + 1));
            }
            xlist.add(x);
            ylist.add(y);
            ++count;
            if (xprev > x) {
                sorted = false;
            }
            xprev = x;
        }
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
        if (ensure_unique_id) {
            name = AnnotatedSeqGroup.getUniqueGraphID(name, aseq);
        }
        graf = new GraphSym(xcoords, ycoords, name, aseq);
        System.out.println("loaded graph data, total points = " + count);
        return graf;
    }
    
    public static void sortXYDataOnX(final int[] xList, final float[] yList) {
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
    public List<? extends SeqSymmetry> parse(final InputStream is, final AnnotatedSeqGroup group, final String nameType, final String uri, final boolean annotate_seq) throws Exception {
        throw new IllegalStateException("Gr should not be processed here");
    }
    
    @Override
    public List<GraphSym> readGraphs(final InputStream istr, final String stream_name, final AnnotatedSeqGroup seq_group, BioSeq seq) throws IOException {
        final StringBuffer stripped_name = new StringBuffer();
        final InputStream newstr = GeneralUtils.unzipStream(istr, stream_name, stripped_name);
        if (seq == null) {
            seq = GenometryModel.getGenometryModel().getSelectedSeq();
        }
        if (seq_group.getSeqCount() == 0) {
            seq = seq_group.addSeq("unnamed", 1000);
        }
        if (seq == null) {
            throw new IOException("Must select a sequence before loading a graph of type 'gr'");
        }
        final GraphSym graph = parse(newstr, seq, stream_name);
        final int max_x = graph.getMaxXCoord();
        final BioSeq gseq = graph.getGraphSeq();
        seq_group.addSeq(gseq.getID(), max_x);
        return GraphParserUtil.getInstance().wrapInList(graph);
    }
    
    @Override
    public void writeGraphFile(final GraphSym gsym, final AnnotatedSeqGroup seq_group, final String file_name) throws IOException {
        BufferedOutputStream bos = null;
        try {
            bos = new BufferedOutputStream(new FileOutputStream(file_name));
            writeGrFormat(gsym, bos);
        }
        finally {
            GeneralUtils.safeClose(bos);
        }
    }
}
