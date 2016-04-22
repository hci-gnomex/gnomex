//
// Decompiled by Procyon v0.5.30
//

package com.affymetrix.genometryImpl.symloader;

import java.util.HashSet;
import java.util.Set;
import java.util.AbstractMap;
import java.util.HashMap;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.AbstractList;
import java.util.List;
import java.util.Map;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.affymetrix.genometryImpl.parsers.FileTypeHolder;
import com.affymetrix.genometryImpl.util.GeneralUtils;
import com.affymetrix.genometryImpl.AnnotatedSeqGroup;
import java.net.URI;
import org.broad.tribble.source.tabix.TabixReader;

public class TbiZoomSymLoader extends IndexZoomSymLoader
{
    private TabixReader tabixReader;

    public TbiZoomSymLoader(final URI uri, final String featureName, final AnnotatedSeqGroup group) {
        super(uri, featureName, group);
    }

    private URI getFileURI(final URI tbiUri) throws Exception {
        String baseUriString = tbiUri.toString().substring(0, tbiUri.toString().length() - ".tbi".length());
        if (!baseUriString.startsWith("file:") && !baseUriString.startsWith("http:") && !baseUriString.startsWith("https:") && !baseUriString.startsWith("ftp:")) {
            baseUriString = GeneralUtils.getFileScheme() + baseUriString;
        }
        return new URI(baseUriString);
    }

    @Override
    protected SymLoader getDataFileSymLoader() throws Exception {
        final URI baseUri = this.getFileURI(this.uri);
        return FileTypeHolder.getInstance().getFileTypeHandlerForURI(baseUri.toString()).createSymLoader(baseUri, this.featureName, this.group);
    }

    public void init() throws Exception {
        if (this.isInitialized) {
            return;
        }
        try {
            String uriString;
            if (this.uri.toString().startsWith("http:") || this.uri.toString().startsWith("https:")) {
                uriString = this.uri.toString();
            }
            else {
                uriString = GeneralUtils.fixFileName(this.uri.toString());
            }
            uriString = uriString.substring(0, uriString.toString().length() - ".tbi".length());
            (this.tabixReader = new TabixReader(uriString)).readIndex();
        }
        catch (Exception x) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Could not read tabix for {0}.", new Object[] { this.featureName });
            return;
        }
        this.isInitialized = true;
    }

    @Override
    protected Iterator<Map<Integer, List<List<Long>>>> getBinIter(final String seq) {
        return this.getBinIter(this.getSynonymMap(), seq);
    }

    private List<List<Long>> getChunkList(final Object[] chunks) {
        return new AbstractList<List<Long>>() {
            @Override
            public List<Long> get(final int index) {
                final Object chunk = chunks[index];
                final List<Long> chunkList = new ArrayList<Long>();
                try {
                    Field privateReaderField = chunk.getClass().getDeclaredField("u");
                    privateReaderField.setAccessible(true);
                    final Long u = (Long)privateReaderField.get(chunk);
                    chunkList.add(u);
                    privateReaderField = chunk.getClass().getDeclaredField("v");
                    privateReaderField.setAccessible(true);
                    final Long v = (Long)privateReaderField.get(chunk);
                    chunkList.add(v);
                }
                catch (NoSuchFieldException x) {
                    Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "cannot read tbi index for " + TbiZoomSymLoader.this.uri, x);
                }
                catch (IllegalAccessException x2) {
                    Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "cannot read tbi index for " + TbiZoomSymLoader.this.uri, x2);
                }
                return chunkList;
            }

            @Override
            public int size() {
                return chunks.length;
            }
        };
    }

    private Iterator<Map<Integer, List<List<Long>>>> getBinIterator(final HashMap<Integer, Object[]> bins) {
        return new Iterator<Map<Integer, List<List<Long>>>>() {
            Iterator<Integer> binIter = bins.keySet().iterator();

            @Override
            public boolean hasNext() {
                return this.binIter.hasNext();
            }

            @Override
            public Map<Integer, List<List<Long>>> next() {
                final Integer binNo = this.binIter.next();
                return new AbstractMap<Integer, List<List<Long>>>() {
                    @Override
                    public Set<Map.Entry<Integer, List<List<Long>>>> entrySet() {
                        final Set<Map.Entry<Integer, List<List<Long>>>> entrySet = new HashSet<Map.Entry<Integer, List<List<Long>>>>();
                        final Object[] chunks = bins.get(binNo);
//                        entrySet.add((Map.Entry<Integer, List>)new SimpleEntry<Integer, List>(binNo, TbiZoomSymLoader.this.getChunkList(chunks)));
                        entrySet.add(new SimpleEntry<Integer, List<List<Long>>>(binNo, getChunkList(chunks)));
                        return entrySet;
                    }
                };
            }

            @Override
            public void remove() {
            }
        };
    }

    private int getRefNo(final Map<String, String> synonymMap, final String igbSeq) {
        try {
            final Field privateReaderField = this.tabixReader.getClass().getDeclaredField("mChr2tid");
            privateReaderField.setAccessible(true);
            final HashMap<String, Integer> mChr2tid = (HashMap<String, Integer>)privateReaderField.get(this.tabixReader);
            for (final String chr : mChr2tid.keySet()) {
                final String bamSeq = synonymMap.get(chr);
                if (igbSeq.equals(bamSeq)) {
                    return mChr2tid.get(chr);
                }
            }
        }
        catch (NoSuchFieldException x) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "cannot read tbi index for " + this.uri, x);
        }
        catch (IllegalAccessException x2) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "cannot read tbi index for " + this.uri, x2);
        }
        return -1;
    }

    private Iterator<Map<Integer, List<List<Long>>>> getBinIter(final Map<String, String> synonymMap, final String seq) {
        final int refno = this.getRefNo(synonymMap, seq.toString());
        if (refno != -1) {
            try {
                Field privateReaderField = this.tabixReader.getClass().getDeclaredField("mIndex");
                privateReaderField.setAccessible(true);
                final Object[] mIndexArrayValue = (Object[])privateReaderField.get(this.tabixReader);
                final Object mIndexItemValue = mIndexArrayValue[refno];
                privateReaderField = mIndexItemValue.getClass().getDeclaredField("b");
                privateReaderField.setAccessible(true);
                final HashMap<Integer, Object[]> bins = (HashMap<Integer, Object[]>)privateReaderField.get(mIndexItemValue);
                return this.getBinIterator(bins);
            }
            catch (NoSuchFieldException x) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "cannot access tbi index for " + this.uri, x);
            }
            catch (IllegalAccessException x2) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "cannot access tbi index for " + this.uri, x2);
            }
        }
        return null;
    }
}
