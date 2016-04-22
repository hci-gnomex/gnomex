//
// Decompiled by Procyon v0.5.30
//

package com.affymetrix.genometryImpl.symloader;

import java.net.URISyntaxException;
import com.affymetrix.genometryImpl.util.LocalUrlCacher;
import java.io.File;
import java.net.URLDecoder;
import org.broad.tribble.util.BlockCompressedInputStream;
import java.net.URL;
import java.lang.reflect.Field;
import com.affymetrix.genometryImpl.util.BlockCompressedStreamPosition;
import org.broad.tribble.readers.LineReader;
import com.affymetrix.genometryImpl.thread.ProgressUpdater;
import com.affymetrix.genometryImpl.thread.CThreadHolder;
import com.affymetrix.genometryImpl.SeqSpan;
import com.affymetrix.genometryImpl.span.SimpleSeqSpan;
import com.affymetrix.genometryImpl.symmetry.SeqSymmetry;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.HashMap;
import com.affymetrix.genometryImpl.AnnotatedSeqGroup;
import java.net.URI;
import com.affymetrix.genometryImpl.util.LoadUtils;
import java.util.List;
import org.broad.tribble.source.tabix.TabixLineReader;
import com.affymetrix.genometryImpl.BioSeq;
import java.util.Map;

public class SymLoaderTabix extends SymLoader
{
    protected final Map<BioSeq, String> seqs;
    private TabixLineReader tabixLineReader;
    private final LineProcessor lineProcessor;
    private static final List<LoadUtils.LoadStrategy> strategyList;

    public SymLoaderTabix(final URI uri, final String featureName, final AnnotatedSeqGroup group, final LineProcessor lineProcessor) {
        super(uri, featureName, group);
        this.seqs = new HashMap<BioSeq, String>();
        this.lineProcessor = lineProcessor;
    }

    @Override
    public List<LoadUtils.LoadStrategy> getLoadChoices() {
        return SymLoaderTabix.strategyList;
    }

    public boolean isValid() {
        return this.tabixLineReader != null;
    }

    public void init() throws Exception {
        if (this.isInitialized) {
            return;
        }
        try {
            String uriString = this.uri.toString();
            if (uriString.startsWith("file:")) {
                uriString = this.uri.getPath();
            }
            this.tabixLineReader = new TabixLineReader(uriString);
        }
        catch (Exception x) {
            this.tabixLineReader = null;
            Logger.getLogger(SymLoaderTabix.class.getName()).log(Level.SEVERE, "Could not initialize tabix line reader for {0}.", new Object[] { this.featureName });
            return;
        }
        if (!this.isValid()) {
            throw new IllegalStateException("tabix file does not exist or was not read");
        }
        this.lineProcessor.init(this.uri);
        for (final String seqID : this.tabixLineReader.getSequenceNames()) {
            BioSeq seq = this.group.getSeq(seqID);
            if (seq == null) {
                final int length = 1000000000;
                seq = this.group.addSeq(seqID, length);
                Logger.getLogger(SymLoaderTabix.class.getName()).log(Level.INFO, "Sequence not found. Adding {0} with default length {1}", new Object[] { seqID, length });
            }
            this.seqs.put(seq, seqID);
        }
        this.isInitialized = true;
    }

    public LineProcessor getLineProcessor() {
        return this.lineProcessor;
    }

    @Override
    public List<String> getFormatPrefList() {
        return this.lineProcessor.getFormatPrefList();
    }

    @Override
    public List<BioSeq> getChromosomeList() throws Exception {
        this.init();
        return new ArrayList<BioSeq>(this.seqs.keySet());
    }

    @Override
    public List<? extends SeqSymmetry> getGenome() throws Exception {
        this.init();
        final List<BioSeq> allSeq = this.getChromosomeList();
        final List<SeqSymmetry> retList = new ArrayList<SeqSymmetry>();
        for (final BioSeq seq : allSeq) {
            retList.addAll(this.getChromosome(seq));
        }
        return retList;
    }

    @Override
    public List<? extends SeqSymmetry> getChromosome(final BioSeq seq) throws Exception {
        return this.getRegion(new SimpleSeqSpan(0, 1073741823, seq));
    }

    @Override
    public List<? extends SeqSymmetry> getRegion(final SeqSpan overlapSpan) throws Exception {
        this.init();
        final String seqID = this.seqs.get(overlapSpan.getBioSeq());
        final LineReader lineReader = this.tabixLineReader.query(seqID, overlapSpan.getStart() + 1, overlapSpan.getEnd());
        if (lineReader == null) {
            return new ArrayList<SeqSymmetry>();
        }
        final long[] startEnd = this.getStartEnd(lineReader);
        if (startEnd == null) {
            return new ArrayList<SeqSymmetry>();
        }
        System.out.println("***** " + startEnd[0] + ":" + startEnd[1]);
        this.parseLinesProgressUpdater = new ParseLinesProgressUpdater("Tabix process lines " + this.uri, startEnd[0], startEnd[1]);
        CThreadHolder.getInstance().getCurrentCThreadWorker().setProgressUpdater(this.parseLinesProgressUpdater);
        return this.lineProcessor.processLines(overlapSpan.getBioSeq(), lineReader, this);
    }

    private long[] getStartEnd(final LineReader lineReader) {
        final long[] startEnd = new long[2];
        try {
            Field field = lineReader.getClass().getDeclaredField("it");
            field.setAccessible(true);
            final Object it = field.get(lineReader);
            if (it == null) {
                return null;
            }
            field = it.getClass().getDeclaredField("off");
            field.setAccessible(true);
            final Object[] off = (Object[])field.get(it);
            field = off[0].getClass().getDeclaredField("u");
            field.setAccessible(true);
            final long startPos = (Long)field.get(off[0]);
            startEnd[0] = new BlockCompressedStreamPosition(startPos).getApproximatePosition();
            field = off[off.length - 1].getClass().getDeclaredField("v");
            field.setAccessible(true);
            final long endPos = (Long)field.get(off[0]);
            startEnd[1] = new BlockCompressedStreamPosition(endPos).getApproximatePosition();
        }
        catch (IllegalAccessException x) {
            Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "unable to display progress for " + this.uri, x);
        }
        catch (NoSuchFieldException x2) {
            Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "unable to display progress for " + this.uri, x2);
        }
        return startEnd;
    }

    public static boolean isTabix(final String path) {
        if (!path.endsWith("gz")) {
            return false;
        }
        BlockCompressedInputStream is = null;
        try {
            if (path.startsWith("ftp:")) {
                return false;
            }
            if (path.startsWith("http:") || path.startsWith("https:")) {
                is = new BlockCompressedInputStream(new URL(path + ".tbi"));
            }
            else {
                is = new BlockCompressedInputStream(new File(URLDecoder.decode(path, "UTF-8") + ".tbi"));
            }
            final byte[] bytes = new byte[4];
            is.read(bytes);
            return (char)bytes[0] == 'T' && (char)bytes[1] == 'B';
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        finally {
            if (is != null) {
                try {
                    is.close();
                }
                catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        }
    }

    public static SymLoader getSymLoader(final SymLoader sym) {
        try {
            final URI uri = new URI(sym.uri.toString() + ".tbi");
            if (LocalUrlCacher.isValidURI(uri)) {
                String uriString = sym.uri.toString();
                if (uriString.startsWith("file:")) {
                    uriString = sym.uri.getPath();
                }
                if (isTabix(uriString) && sym instanceof LineProcessor) {
                    return new SymLoaderTabix(sym.uri, sym.featureName, sym.group, (LineProcessor)sym);
                }
            }
        }
        catch (URISyntaxException ex) {
            Logger.getLogger(SymLoaderTabix.class.getName()).log(Level.SEVERE, null, ex);
        }
        return sym;
    }

    static {
        (strategyList = new ArrayList<LoadUtils.LoadStrategy>()).add(LoadUtils.LoadStrategy.NO_LOAD);
        SymLoaderTabix.strategyList.add(LoadUtils.LoadStrategy.AUTOLOAD);
        SymLoaderTabix.strategyList.add(LoadUtils.LoadStrategy.VISIBLE);
        SymLoaderTabix.strategyList.add(LoadUtils.LoadStrategy.GENOME);
    }
}
