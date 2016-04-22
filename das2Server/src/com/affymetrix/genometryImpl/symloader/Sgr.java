//
// Decompiled by Procyon v0.5.30
//

package com.affymetrix.genometryImpl.symloader;

import com.affymetrix.genometryImpl.symmetry.SeqSymmetry;
import java.io.BufferedWriter;
import java.util.HashMap;
import java.util.Map;
import com.affymetrix.genometryImpl.parsers.graph.GrParser;
import java.util.Arrays;
import java.io.DataOutputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import com.affymetrix.genometryImpl.util.GeneralUtils;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.File;
import cern.colt.list.FloatArrayList;
import cern.colt.list.IntArrayList;
import com.affymetrix.genometryImpl.SeqSpan;
import java.util.Iterator;
import com.affymetrix.genometryImpl.symmetry.GraphSym;
import java.util.Comparator;
import java.util.Collections;
import com.affymetrix.genometryImpl.comparator.BioSeqComparator;
import java.util.Collection;
import java.util.ArrayList;
import com.affymetrix.genometryImpl.BioSeq;
import com.affymetrix.genometryImpl.AnnotatedSeqGroup;
import java.net.URI;
import com.affymetrix.genometryImpl.util.LoadUtils;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import com.affymetrix.genometryImpl.parsers.AnnotationWriter;

public final class Sgr extends SymLoader implements AnnotationWriter
{
    private static final Pattern line_regex;
    private static final List<LoadUtils.LoadStrategy> strategyList;

    public Sgr(final URI uri, final String featureName, final AnnotatedSeqGroup seq_group) {
        super(uri, featureName, seq_group);
    }

    public void init() throws Exception {
        if (this.isInitialized) {
            return;
        }
        if (this.buildIndex()) {
            super.init();
        }
    }

    @Override
    public List<LoadUtils.LoadStrategy> getLoadChoices() {
        return Sgr.strategyList;
    }

    @Override
    public List<BioSeq> getChromosomeList() throws Exception {
        this.init();
        final List<BioSeq> chromosomeList = new ArrayList<BioSeq>(this.chrList.keySet());
        Collections.sort(chromosomeList, new BioSeqComparator());
        return chromosomeList;
    }

    @Override
    public List<GraphSym> getGenome() throws Exception {
        this.init();
        final List<BioSeq> allSeq = this.getChromosomeList();
        final List<GraphSym> retList = new ArrayList<GraphSym>();
        for (final BioSeq seq : allSeq) {
            retList.addAll(this.getChromosome(seq));
        }
        return retList;
    }

    @Override
    public List<GraphSym> getChromosome(final BioSeq seq) throws Exception {
        this.init();
        return this.parse(seq, seq.getMin(), seq.getMax() + 1);
    }

    @Override
    public List<GraphSym> getRegion(final SeqSpan span) throws Exception {
        this.init();
        return this.parse(span.getBioSeq(), span.getMin(), span.getMax() + 1);
    }

    @Override
    public String getMimeType() {
        return "text/sgr";
    }

    private List<GraphSym> parse(final BioSeq seq, final int min, final int max) throws Exception {
        final List<GraphSym> results = new ArrayList<GraphSym>();
        final IntArrayList xlist = new IntArrayList();
        final FloatArrayList ylist = new FloatArrayList();
        BufferedReader br = null;
        FileOutputStream fos = null;
        try {
            final File file = this.chrList.get(seq);
            if (file == null) {
                Logger.getLogger(Sgr.class.getName()).log(Level.FINE, "Could not find chromosome " + seq.getID());
                return Collections.emptyList();
            }
            br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            final boolean sort = parseLines(br, xlist, ylist, min, max, !file.canWrite());
            final GraphSym sym = createResults(xlist, seq, ylist, this.uri.toString(), sort);
            results.add(sym);
            if (sort) {
                fos = new FileOutputStream(file);
                writeSgrFormat(sym, fos);
            }
            file.setReadOnly();
            return results;
        }
        catch (Exception ex) {
            throw ex;
        }
        finally {
            GeneralUtils.safeClose(br);
            GeneralUtils.safeClose(fos);
        }
    }

    private static boolean parseLines(final BufferedReader br, final IntArrayList xlist, final FloatArrayList ylist, final int min, final int max, final boolean sorted) throws IOException, NumberFormatException {
        int x = 0;
        float y = 0.0f;
        int prevx = 0;
        boolean sort = false;
        String line;
        while ((line = br.readLine()) != null) {
            if (line.length() != 0 && line.charAt(0) != '#') {
                if (line.charAt(0) == '%') {
                    continue;
                }
                final String[] fields = Sgr.line_regex.split(line);
                if (fields == null) {
                    continue;
                }
                if (fields.length == 0) {
                    continue;
                }
                x = Integer.parseInt(fields[1]);
                if (x >= max) {
                    if (sorted) {
                        break;
                    }
                    continue;
                }
                else {
                    if (x < min) {
                        continue;
                    }
                    y = Float.parseFloat(fields[2]);
                    xlist.add(x);
                    ylist.add(y);
                    if (sorted) {
                        continue;
                    }
                    if (prevx > x) {
                        sort = true;
                    }
                    else {
                        prevx = x;
                    }
                }
            }
        }
        return sort;
    }

    public static boolean writeSgrFormat(final GraphSym graf, final OutputStream ostr) throws IOException {
        final BioSeq seq = graf.getGraphSeq();
        if (seq == null) {
            throw new IOException("You cannot use the '.sgr' format when the sequence is unknown. Use '.gr' instead.");
        }
        final String seq_id = seq.getID();
        BufferedOutputStream bos = null;
        DataOutputStream dos = null;
        try {
            bos = new BufferedOutputStream(ostr);
            dos = new DataOutputStream(bos);
            writeGraphPoints(graf, dos, seq_id);
        }
        finally {
            GeneralUtils.safeClose(bos);
            GeneralUtils.safeClose(dos);
        }
        return true;
    }

    private static void writeGraphPoints(final GraphSym graf, final DataOutputStream dos, final String seq_id) throws IOException {
        for (int total_points = graf.getPointCount(), i = 0; i < total_points; ++i) {
            dos.writeBytes(seq_id + "\t" + graf.getGraphXCoord(i) + "\t" + graf.getGraphYCoordString(i) + "\n");
        }
    }

    private static GraphSym createResults(IntArrayList xlist, final BioSeq aseq, FloatArrayList ylist, final String gid, final boolean sort) {
        final int[] xcoords = Arrays.copyOf(xlist.elements(), xlist.size());
        xlist = null;
        final float[] ycoords = Arrays.copyOf(ylist.elements(), ylist.size());
        ylist = null;
        if (sort) {
            GrParser.sortXYDataOnX(xcoords, ycoords);
        }
        return new GraphSym(xcoords, ycoords, gid, aseq);
    }

    @Override
    protected boolean parseLines(final InputStream istr, final Map<String, Integer> chrLength, final Map<String, File> chrFiles) throws Exception {
        final Map<String, BufferedWriter> chrs = new HashMap<String, BufferedWriter>();
        BufferedReader br = null;
        BufferedWriter bw = null;
        String[] fields = null;
        try {
            br = new BufferedReader(new InputStreamReader(istr));
            final Thread thread = Thread.currentThread();
            String line;
            while ((line = br.readLine()) != null && !thread.isInterrupted()) {
                if (line.length() != 0 && line.charAt(0) != '#') {
                    if (line.charAt(0) == '%') {
                        continue;
                    }
                    fields = Sgr.line_regex.split(line);
                    final String seqid = fields[0];
                    final int x = Integer.parseInt(fields[1]);
                    if (!chrs.containsKey(seqid)) {
                        this.addToLists(chrs, seqid, chrFiles, chrLength, ".sgr");
                    }
                    if (x > chrLength.get(seqid)) {
                        chrLength.put(seqid, x);
                    }
                    bw = chrs.get(seqid);
                    bw.write(line + "\n");
                }
            }
            return !thread.isInterrupted();
        }
        catch (Exception ex) {
            throw ex;
        }
        finally {
            for (final BufferedWriter b : chrs.values()) {
                try {
                    b.flush();
                }
                catch (IOException ex2) {
                    Logger.getLogger(Sgr.class.getName()).log(Level.SEVERE, null, ex2);
                }
                GeneralUtils.safeClose(b);
            }
            GeneralUtils.safeClose(bw);
            GeneralUtils.safeClose(br);
        }
    }

    @Override
    public boolean writeAnnotations(final Collection<? extends SeqSymmetry> syms, final BioSeq seq, final String type, final OutputStream ostr) throws IOException {
        BufferedOutputStream bos = null;
        DataOutputStream dos = null;
        try {
            bos = new BufferedOutputStream(ostr);
            dos = new DataOutputStream(bos);
            for (final GraphSym graf : (Set<GraphSym>)syms) {
                writeGraphPoints(graf, dos, graf.getGraphSeq().getID());
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
        line_regex = Pattern.compile("\\s+");
        (strategyList = new ArrayList<LoadUtils.LoadStrategy>()).add(LoadUtils.LoadStrategy.NO_LOAD);
        Sgr.strategyList.add(LoadUtils.LoadStrategy.VISIBLE);
        Sgr.strategyList.add(LoadUtils.LoadStrategy.GENOME);
    }
}
