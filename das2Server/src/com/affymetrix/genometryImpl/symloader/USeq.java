//
// Decompiled by Procyon v0.5.30
//

package com.affymetrix.genometryImpl.symloader;

import com.affymetrix.genometryImpl.parsers.useq.data.USeqData;
import com.affymetrix.genometryImpl.SeqSpan;
import com.affymetrix.genometryImpl.parsers.useq.USeqRegionParser;
import com.affymetrix.genometryImpl.GenometryModel;
import com.affymetrix.genometryImpl.parsers.useq.USeqGraphParser;
import com.affymetrix.genometryImpl.symmetry.SeqSymmetry;
import java.util.Iterator;
import com.affymetrix.genometryImpl.util.GeneralUtils;
import java.util.Comparator;
import java.util.Collections;
import com.affymetrix.genometryImpl.comparator.BioSeqComparator;
import java.util.Collection;
import java.util.ArrayList;
import java.io.InputStream;
import com.affymetrix.genometryImpl.util.LocalUrlCacher;
import java.util.HashMap;
import com.affymetrix.genometryImpl.AnnotatedSeqGroup;
import java.net.URI;
import com.affymetrix.genometryImpl.util.LoadUtils;
import java.util.List;
import com.affymetrix.genometryImpl.parsers.useq.USeqArchive;
import com.affymetrix.genometryImpl.BioSeq;
import java.util.Map;
import java.io.BufferedInputStream;
import java.util.zip.ZipInputStream;
import com.affymetrix.genometryImpl.parsers.useq.ArchiveInfo;

public class USeq extends SymLoader
{
    private ArchiveInfo archiveInfo;
    private ZipInputStream zis;
    private BufferedInputStream bis;
    private final Map<BioSeq, String> chromosomeList;
    private USeqArchive useqArchive;
    private static final List<LoadUtils.LoadStrategy> strategyList;

    public USeq(final URI uri, final String featureName, final AnnotatedSeqGroup group) {
        super(uri, featureName, group);
        this.archiveInfo = null;
        this.zis = null;
        this.bis = null;
        this.chromosomeList = new HashMap<BioSeq, String>();
        this.useqArchive = null;
    }

    @Override
    public List<LoadUtils.LoadStrategy> getLoadChoices() {
        return USeq.strategyList;
    }

    public void init() throws Exception {
        if (this.isInitialized) {
            return;
        }
        try {
            this.useqArchive = new USeqArchive(LocalUrlCacher.convertURIToFile(this.uri));
            this.bis = LocalUrlCacher.convertURIToBufferedUnzippedStream(this.uri);
            (this.zis = new ZipInputStream(this.bis)).getNextEntry();
            this.archiveInfo = new ArchiveInfo(this.zis, false);
            final HashMap<String, Integer> chromBase = this.useqArchive.fetchChromosomesAndLastBase();
            for (final String chrom : chromBase.keySet()) {
                this.chromosomeList.put(this.group.addSeq(chrom, chromBase.get(chrom), this.uri.toString()), chrom);
            }
//            Collections.sort(new ArrayList<Object>(this.chromosomeList.keySet()), (Comparator<? super Object>)new BioSeqComparator());
            Collections.sort(new ArrayList<BioSeq>(chromosomeList.keySet()),new BioSeqComparator());
        }
        catch (Exception ex) {
            throw ex;
        }
        finally {
            GeneralUtils.safeClose(this.bis);
            GeneralUtils.safeClose(this.zis);
        }
        super.init();
    }

    @Override
    public List<? extends SeqSymmetry> getGenome() throws Exception {
        this.init();
        try {
            if (this.archiveInfo.getDataType().equals("graph")) {
                final USeqGraphParser gp = new USeqGraphParser();
                return gp.parseGraphSyms(this.zis, GenometryModel.getGenometryModel(), this.uri.toString(), this.archiveInfo);
            }
            final USeqRegionParser rp = new USeqRegionParser();
            return rp.parse(this.zis, this.group, this.uri.toString(), false, this.archiveInfo);
        }
        catch (Exception ex) {
            throw ex;
        }
        finally {
            GeneralUtils.safeClose(this.bis);
            GeneralUtils.safeClose(this.zis);
        }
    }

    @Override
    public List<? extends SeqSymmetry> getRegion(final SeqSpan span) throws Exception {
        try {
            this.init();
            if (!this.chromosomeList.containsKey(span.getBioSeq())) {
                return null;
            }
            final int start = span.getStart();
            final int stop = span.getEnd();
            final String chrom = this.chromosomeList.get(span.getBioSeq());
            final USeqData[] useqData = this.useqArchive.fetch(chrom, start, stop);
            if (useqData == null) {
                return null;
            }
            if (this.useqArchive.getArchiveInfo().getDataType().equals("graph")) {
                final USeqGraphParser gp = new USeqGraphParser();
                return gp.parseGraphSyms(this.useqArchive, useqData, GenometryModel.getGenometryModel(), this.uri.toString());
            }
            final USeqRegionParser rp = new USeqRegionParser();
            return rp.parse(this.useqArchive, useqData, this.group, this.uri.toString());
        }
        catch (Exception ex) {
            throw ex;
        }
        finally {
            GeneralUtils.safeClose(this.bis);
            GeneralUtils.safeClose(this.zis);
        }
    }

    @Override
    public List<BioSeq> getChromosomeList() throws Exception {
        this.init();
        return new ArrayList<BioSeq>(this.chromosomeList.keySet());
    }

    static {
        (strategyList = new ArrayList<LoadUtils.LoadStrategy>()).add(LoadUtils.LoadStrategy.NO_LOAD);
        USeq.strategyList.add(LoadUtils.LoadStrategy.VISIBLE);
        USeq.strategyList.add(LoadUtils.LoadStrategy.AUTOLOAD);
        USeq.strategyList.add(LoadUtils.LoadStrategy.GENOME);
    }
}
