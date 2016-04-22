// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.symloader;

import java.util.ArrayList;
import com.affymetrix.genometryImpl.SeqSpan;
import com.affymetrix.genometryImpl.util.GeneralUtils;
import com.affymetrix.genometryImpl.BioSeq;
import com.affymetrix.genometryImpl.parsers.graph.BarParser;
import com.affymetrix.genometryImpl.GenometryModel;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import com.affymetrix.genometryImpl.symmetry.GraphSym;
import com.affymetrix.genometryImpl.util.LocalUrlCacher;
import com.affymetrix.genometryImpl.AnnotatedSeqGroup;
import java.net.URI;
import com.affymetrix.genometryImpl.util.LoadUtils;
import java.util.List;
import java.io.File;

public final class Bar extends SymLoader
{
    private File f;
    private static final List<LoadUtils.LoadStrategy> strategyList;
    
    public Bar(final URI uri, final String featureName, final AnnotatedSeqGroup group) {
        super(uri, featureName, group);
        this.f = null;
    }
    
    @Override
    public List<LoadUtils.LoadStrategy> getLoadChoices() {
        return Bar.strategyList;
    }
    
    public void init() throws Exception {
        if (this.isInitialized) {
            return;
        }
        super.init();
        this.f = LocalUrlCacher.convertURIToFile(this.uri);
    }
    
    @Override
    public List<GraphSym> getGenome() throws Exception {
        BufferedInputStream bis = null;
        try {
            this.init();
            bis = new BufferedInputStream(new FileInputStream(this.f));
            return BarParser.parse(this.uri.toString(), bis, GenometryModel.getGenometryModel(), this.group, null, 0, Integer.MAX_VALUE, this.uri.toString(), true);
        }
        catch (Exception ex) {
            throw ex;
        }
        finally {
            GeneralUtils.safeClose(bis);
        }
    }
    
    @Override
    public List<GraphSym> getChromosome(final BioSeq seq) throws Exception {
        BufferedInputStream bis = null;
        try {
            this.init();
            bis = new BufferedInputStream(new FileInputStream(this.f));
            return BarParser.parse(this.uri.toString(), bis, GenometryModel.getGenometryModel(), this.group, seq, 0, seq.getMax() + 1, this.uri.toString(), true);
        }
        catch (Exception ex) {
            throw ex;
        }
        finally {
            GeneralUtils.safeClose(bis);
        }
    }
    
    @Override
    public List<GraphSym> getRegion(final SeqSpan span) throws Exception {
        BufferedInputStream bis = null;
        try {
            this.init();
            bis = new BufferedInputStream(new FileInputStream(this.f));
            return BarParser.parse(this.uri.toString(), bis, GenometryModel.getGenometryModel(), this.group, span.getBioSeq(), span.getMin(), span.getMax(), this.uri.toString(), true);
        }
        catch (Exception ex) {
            throw ex;
        }
        finally {
            GeneralUtils.safeClose(bis);
        }
    }
    
    static {
        (strategyList = new ArrayList<LoadUtils.LoadStrategy>()).add(LoadUtils.LoadStrategy.NO_LOAD);
        Bar.strategyList.add(LoadUtils.LoadStrategy.VISIBLE);
        Bar.strategyList.add(LoadUtils.LoadStrategy.GENOME);
    }
}
