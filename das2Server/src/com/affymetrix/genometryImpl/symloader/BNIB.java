// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.symloader;

import java.io.OutputStream;
import java.io.ByteArrayOutputStream;
import com.affymetrix.genometryImpl.SeqSpan;
import net.sf.samtools.util.SeekableStream;
import com.affymetrix.genometryImpl.util.GeneralUtils;
import java.io.InputStream;
import com.affymetrix.genometryImpl.parsers.NibbleResiduesParser;
import com.affymetrix.genometryImpl.util.LocalUrlCacher;
import java.util.ArrayList;
import com.affymetrix.genometryImpl.AnnotatedSeqGroup;
import java.net.URI;
import com.affymetrix.genometryImpl.util.LoadUtils;
import com.affymetrix.genometryImpl.BioSeq;
import java.util.List;

public class BNIB extends SymLoader
{
    private static final List<String> pref_list;
    private List<BioSeq> chrList;
    private static final List<LoadUtils.LoadStrategy> strategyList;
    
    public BNIB(final URI uri, final String featureName, final AnnotatedSeqGroup group) {
        super(uri, "", group);
        this.chrList = null;
        this.isResidueLoader = true;
    }
    
    public void init() throws Exception {
        if (this.isInitialized) {
            return;
        }
        super.init();
    }
    
    @Override
    public List<LoadUtils.LoadStrategy> getLoadChoices() {
        return BNIB.strategyList;
    }
    
    @Override
    public List<BioSeq> getChromosomeList() throws Exception {
        if (this.chrList != null) {
            return this.chrList;
        }
        this.init();
        this.chrList = new ArrayList<BioSeq>(1);
        SeekableStream sis = null;
        try {
            sis = LocalUrlCacher.getSeekableStream(this.uri);
            final BioSeq seq = NibbleResiduesParser.determineChromosome((InputStream)sis, this.group);
            if (seq != null) {
                this.chrList.add(seq);
            }
            return this.chrList;
        }
        catch (Exception ex) {
            throw ex;
        }
        finally {
            GeneralUtils.safeClose(sis);
        }
    }
    
    @Override
    public String getRegionResidues(final SeqSpan span) throws Exception {
        this.init();
        SeekableStream sis = null;
        ByteArrayOutputStream outStream = null;
        try {
            outStream = new ByteArrayOutputStream();
            sis = LocalUrlCacher.getSeekableStream(this.uri);
            NibbleResiduesParser.parse((InputStream)sis, span.getStart(), span.getEnd(), outStream);
            final byte[] bytes = outStream.toByteArray();
            return new String(bytes);
        }
        catch (Exception ex) {
            throw ex;
        }
        finally {
            GeneralUtils.safeClose(outStream);
            GeneralUtils.safeClose(sis);
        }
    }
    
    @Override
    public List<String> getFormatPrefList() {
        return BNIB.pref_list;
    }
    
    static {
        (pref_list = new ArrayList<String>()).add("raw");
        BNIB.pref_list.add("bnib");
        (strategyList = new ArrayList<LoadUtils.LoadStrategy>()).add(LoadUtils.LoadStrategy.NO_LOAD);
        BNIB.strategyList.add(LoadUtils.LoadStrategy.VISIBLE);
    }
}
