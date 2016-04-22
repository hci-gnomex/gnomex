//
// Decompiled by Procyon v0.5.30
//

package com.affymetrix.genometryImpl.parsers;

import java.util.Arrays;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.TreeMap;
import com.affymetrix.genometryImpl.util.GeneralUtils;
import com.affymetrix.genometryImpl.util.ServerUtils;
import java.lang.reflect.Constructor;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.affymetrix.genometryImpl.symloader.VCF;
import com.affymetrix.genometryImpl.symloader.Wiggle;
import com.affymetrix.genometryImpl.parsers.graph.WiggleParser;
import com.affymetrix.genometryImpl.symloader.USeq;
import com.affymetrix.genometryImpl.parsers.useq.USeqRegionParser;
import com.affymetrix.genometryImpl.symloader.TwoBit;
import com.affymetrix.genometryImpl.symloader.Sgr;
import com.affymetrix.genometryImpl.parsers.graph.SgrParser;
import com.affymetrix.genometryImpl.parsers.graph.ScoredMapParser;
import com.affymetrix.genometryImpl.parsers.graph.ScoredIntervalParser;
import com.affymetrix.genometryImpl.symloader.BNIB;
import com.affymetrix.genometryImpl.symloader.PSL;
import com.affymetrix.genometryImpl.symloader.Gr;
import com.affymetrix.genometryImpl.parsers.graph.GrParser;
import com.affymetrix.genometryImpl.symloader.Genbank;
import com.affymetrix.genometryImpl.symloader.Fasta;
import com.affymetrix.genometryImpl.symloader.FastaIdx;
import com.affymetrix.genometryImpl.parsers.das.DASFeatureParser;
import com.affymetrix.genometryImpl.parsers.graph.CntParser;
import com.affymetrix.genometryImpl.parsers.graph.BgrParser;
import com.affymetrix.genometryImpl.symloader.SymLoaderInst;
import com.affymetrix.genometryImpl.symloader.GFF;
import com.affymetrix.genometryImpl.symloader.GFF3;
import com.affymetrix.genometryImpl.symloader.BED;
import com.affymetrix.genometryImpl.parsers.graph.BarParser;
import com.affymetrix.genometryImpl.symloader.SymLoaderTabix;
import com.affymetrix.genometryImpl.symloader.SAM;
import com.affymetrix.genometryImpl.AnnotatedSeqGroup;
import java.net.URI;
import com.affymetrix.genometryImpl.symloader.BAM;
import com.affymetrix.genometryImpl.symloader.SymLoader;
import com.affymetrix.genometryImpl.symloader.SymLoaderInstNC;
import com.affymetrix.genometryImpl.parsers.gchp.AffyCnChpParser;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileTypeHolder
{
    private static final FileTypeHolder instance;
    private final Map<String, FileTypeHandler> fileTypeHandlerMap;
    private static final List<String> TABIX_FILE_TYPES;

    public static FileTypeHolder getInstance() {
        return FileTypeHolder.instance;
    }

    private FileTypeHolder() {
        this.fileTypeHandlerMap = new HashMap<String, FileTypeHandler>();
        this.addFileTypeHandler("Copy Number CHP", new String[] { "cnchp", "lohchp" }, FileTypeCategory.Annotation, AffyCnChpParser.class, SymLoaderInstNC.class);
        this.addFileTypeHandler("BAM", new String[] { "bam" }, FileTypeCategory.Alignment, null, BAM.class);
        this.addFileTypeHandler(new FileTypeHandler() {
            String[] extensions = { "sam" };

            @Override
            public String getName() {
                return "SAM";
            }

            @Override
            public String[] getExtensions() {
                return this.extensions;
            }

            @Override
            public SymLoader createSymLoader(final URI uri, final String featureName, final AnnotatedSeqGroup group) {
                return SymLoaderTabix.getSymLoader(new SAM(uri, featureName, group));
            }

            @Override
            public Parser getParser() {
                return null;
            }

            @Override
            public IndexWriter getIndexWriter(final String stream_name) {
                return null;
            }

            @Override
            public FileTypeCategory getFileTypeCategory() {
                return FileTypeCategory.Alignment;
            }
        });
        this.addFileTypeHandler("Graph", new String[] { "bar" }, FileTypeCategory.Graph, BarParser.class, SymLoaderInstNC.class);
        this.addFileTypeHandler(new FileTypeHandler() {
            String[] extensions = { "bed" };

            @Override
            public String getName() {
                return "BED";
            }

            @Override
            public String[] getExtensions() {
                return this.extensions;
            }

            @Override
            public SymLoader createSymLoader(final URI uri, final String featureName, final AnnotatedSeqGroup group) {
                return SymLoaderTabix.getSymLoader(new BED(uri, featureName, group));
            }

            @Override
            public Parser getParser() {
                return new BedParser();
            }

            @Override
            public IndexWriter getIndexWriter(final String stream_name) {
                return (IndexWriter)this.getParser();
            }

            @Override
            public FileTypeCategory getFileTypeCategory() {
                return FileTypeCategory.Annotation;
            }
        });
        this.addFileTypeHandler(new FileTypeHandler() {
            String[] extensions = { "gff3" };

            @Override
            public String getName() {
                return "GFF";
            }

            @Override
            public String[] getExtensions() {
                return this.extensions;
            }

            @Override
            public SymLoader createSymLoader(final URI uri, final String featureName, final AnnotatedSeqGroup group) {
                return SymLoaderTabix.getSymLoader(new GFF3(uri, featureName, group));
            }

            @Override
            public GFF3Parser getParser() {
                return new GFF3Parser();
            }

            @Override
            public IndexWriter getIndexWriter(final String stream_name) {
                return null;
            }

            @Override
            public FileTypeCategory getFileTypeCategory() {
                return FileTypeCategory.Annotation;
            }
        });
        this.addFileTypeHandler(new FileTypeHandler() {
            String[] extensions = { "gff", "gtf" };

            @Override
            public String getName() {
                return "GFF";
            }

            @Override
            public String[] getExtensions() {
                return this.extensions;
            }

            @Override
            public SymLoader createSymLoader(final URI uri, final String featureName, final AnnotatedSeqGroup group) {
                if (GFF3.isGFF3(uri)) {
                    return SymLoaderTabix.getSymLoader(new GFF3(uri, featureName, group));
                }
                return SymLoaderTabix.getSymLoader(new GFF(uri, featureName, group));
            }

            @Override
            public Parser getParser() {
                return new GFFParser();
            }

            @Override
            public IndexWriter getIndexWriter(final String stream_name) {
                return null;
            }

            @Override
            public FileTypeCategory getFileTypeCategory() {
                return FileTypeCategory.Annotation;
            }
        });
        this.addFileTypeHandler("Binary", new String[] { "bgn" }, FileTypeCategory.Annotation, BgnParser.class, SymLoaderInst.class);
        this.addFileTypeHandler("Graph", new String[] { "bgr" }, FileTypeCategory.Graph, BgrParser.class, SymLoaderInstNC.class);
        this.addFileTypeHandler("Binary", new String[] { "bp1", "bp2" }, FileTypeCategory.Annotation, Bprobe1Parser.class, SymLoaderInst.class);
        this.addFileTypeHandler("Binary", new String[] { "bps" }, FileTypeCategory.Annotation, BpsParser.class, SymLoaderInst.class);
        this.addFileTypeHandler("Binary", new String[] { "brpt" }, FileTypeCategory.Annotation, BrptParser.class, SymLoaderInstNC.class);
        this.addFileTypeHandler("Binary", new String[] { "brs" }, FileTypeCategory.Annotation, BrsParser.class, SymLoaderInst.class);
        this.addFileTypeHandler("Binary", new String[] { "bsnp" }, FileTypeCategory.Annotation, BsnpParser.class, SymLoaderInstNC.class);
        this.addFileTypeHandler("Graph", new String[] { "chp" }, FileTypeCategory.ScoredContainer, null, SymLoaderInstNC.class);
        this.addFileTypeHandler("Copy Number", new String[] { "cnt" }, FileTypeCategory.Graph, CntParser.class, SymLoaderInst.class);
        this.addFileTypeHandler("Cytobands", new String[] { "cyt" }, null, CytobandParser.class, SymLoaderInst.class);
        this.addFileTypeHandler("DAS", new String[] { "x-das-features+xml", "das2feature", "das2xml", "x-das-feature" }, FileTypeCategory.Annotation, Das2FeatureSaxParser.class, SymLoaderInstNC.class);
        this.addFileTypeHandler("DAS", new String[] { "das", "dasxml" }, FileTypeCategory.Annotation, DASFeatureParser.class, SymLoaderInstNC.class);
        this.addFileTypeHandler("Binary", new String[] { "ead" }, FileTypeCategory.Annotation, ExonArrayDesignParser.class, SymLoaderInstNC.class);
        this.addFileTypeHandler(new FileTypeHandler() {
            String[] extensions = { "fa", "fas", "fasta" };

            @Override
            public String getName() {
                return "FASTA";
            }

            @Override
            public String[] getExtensions() {
                return this.extensions;
            }

            @Override
            public SymLoader createSymLoader(final URI uri, final String featureName, final AnnotatedSeqGroup group) {
                SymLoader symLoader = new FastaIdx(uri, featureName, group);
                if (!((FastaIdx)symLoader).isValid()) {
                    symLoader = new Fasta(uri, featureName, group);
                }
                return symLoader;
            }

            @Override
            public Parser getParser() {
                return new FastaParser();
            }

            @Override
            public IndexWriter getIndexWriter(final String stream_name) {
                return null;
            }

            @Override
            public FileTypeCategory getFileTypeCategory() {
                return FileTypeCategory.Sequence;
            }
        });
        this.addFileTypeHandler("FishClones", new String[] { "fsh" }, FileTypeCategory.Annotation, FishClonesParser.class, SymLoaderInstNC.class);
        this.addFileTypeHandler("Genbank", new String[] { "gb", "gen" }, FileTypeCategory.Annotation, null, Genbank.class);
        this.addFileTypeHandler("Graph", new String[] { "gr" }, FileTypeCategory.Graph, GrParser.class, Gr.class);
        this.addFileTypeHandler(new FileTypeHandler() {
            String[] extensions = { "link.psl" };

            @Override
            public String getName() {
                return "PSL";
            }

            @Override
            public String[] getExtensions() {
                return this.extensions;
            }

            @Override
            public SymLoader createSymLoader(final URI uri, final String featureName, final AnnotatedSeqGroup group) {
                final PSL psl = new PSL(uri, featureName, group);
                psl.setIsLinkPsl(true);
                psl.enableSharedQueryTarget(true);
                return psl;
            }

            @Override
            public Parser getParser() {
                return new LinkPSLParser();
            }

            @Override
            public IndexWriter getIndexWriter(final String stream_name) {
                final int sindex = stream_name.lastIndexOf("/");
                final String type_prefix = (sindex < 0) ? null : stream_name.substring(0, sindex + 1);
                final PSLParser parser = new PSLParser();
                if (type_prefix != null) {
                    parser.setTrackNamePrefix(type_prefix);
                }
                parser.setIsLinkPsl(true);
                parser.enableSharedQueryTarget(true);
                parser.setCreateContainerAnnot(true);
                return parser;
            }

            @Override
            public FileTypeCategory getFileTypeCategory() {
                return FileTypeCategory.ProbeSet;
            }
        });
        this.addFileTypeHandler("Binary", new String[] { "bnib" }, FileTypeCategory.Sequence, NibbleResiduesParser.class, BNIB.class);
        this.addFileTypeHandler(new FileTypeHandler() {
            String[] extensions = { "psl", "psl3", "pslx" };

            @Override
            public String getName() {
                return "PSL";
            }

            @Override
            public String[] getExtensions() {
                return this.extensions;
            }

            @Override
            public SymLoader createSymLoader(final URI uri, final String featureName, final AnnotatedSeqGroup group) {
                final PSL psl = new PSL(uri, featureName, group);
                psl.enableSharedQueryTarget(true);
                return SymLoaderTabix.getSymLoader(psl);
            }

            @Override
            public Parser getParser() {
                return new PSLParser();
            }

            @Override
            public IndexWriter getIndexWriter(final String stream_name) {
                final int sindex = stream_name.lastIndexOf("/");
                final String type_prefix = (sindex < 0) ? null : stream_name.substring(0, sindex + 1);
                final PSLParser iWriter = new PSLParser();
                if (type_prefix != null) {
                    iWriter.setTrackNamePrefix(type_prefix);
                }
                return iWriter;
            }

            @Override
            public FileTypeCategory getFileTypeCategory() {
                return FileTypeCategory.Annotation;
            }
        });
        this.addFileTypeHandler("Scored Interval", new String[] { "sin", "egr", "egr.txt" }, FileTypeCategory.ScoredContainer, ScoredIntervalParser.class, SymLoaderInstNC.class);
        this.addFileTypeHandler("Scored Map", new String[] { "map" }, FileTypeCategory.ScoredContainer, ScoredMapParser.class, SymLoaderInstNC.class);
        this.addFileTypeHandler("Regions", new String[] { "cn_segments", "loh_segments" }, FileTypeCategory.Annotation, SegmenterRptParser.class, SymLoaderInstNC.class);
        this.addFileTypeHandler("Graph", new String[] { "sgr" }, FileTypeCategory.Graph, SgrParser.class, Sgr.class);
        this.addFileTypeHandler(".2bit", new String[] { "2bit" }, FileTypeCategory.Sequence, TwoBitParser.class, TwoBit.class);
        this.addFileTypeHandler("Binary", new String[] { "useq" }, FileTypeCategory.Annotation, USeqRegionParser.class, USeq.class);
        this.addFileTypeHandler("Genomic Variation", new String[] { "var" }, FileTypeCategory.Annotation, VarParser.class, SymLoaderInstNC.class);
        this.addFileTypeHandler("Graph", new String[] { "wig" }, FileTypeCategory.Graph, WiggleParser.class, Wiggle.class);
        this.addFileTypeHandler(new FileTypeHandler() {
            String[] extensions = { "bedgraph" };

            @Override
            public String getName() {
                return "Graph";
            }

            @Override
            public String[] getExtensions() {
                return this.extensions;
            }

            @Override
            public SymLoader createSymLoader(final URI uri, final String featureName, final AnnotatedSeqGroup group) {
                return SymLoaderTabix.getSymLoader(new Wiggle(uri, featureName, group));
            }

            @Override
            public Parser getParser() {
                return new WiggleParser();
            }

            @Override
            public IndexWriter getIndexWriter(final String stream_name) {
                return (IndexWriter)this.getParser();
            }

            @Override
            public FileTypeCategory getFileTypeCategory() {
                return FileTypeCategory.Graph;
            }
        });
        this.addFileTypeHandler(new FileTypeHandler() {
            String[] extensions = { "vcf" };

            @Override
            public String getName() {
                return "VCF";
            }

            @Override
            public String[] getExtensions() {
                return this.extensions;
            }

            @Override
            public SymLoader createSymLoader(final URI uri, final String featureName, final AnnotatedSeqGroup group) {
                return SymLoaderTabix.getSymLoader(new VCF(uri, featureName, group));
            }

            @Override
            public Parser getParser() {
                return null;
            }

            @Override
            public IndexWriter getIndexWriter(final String stream_name) {
                return null;
            }

            @Override
            public FileTypeCategory getFileTypeCategory() {
                return FileTypeCategory.Alignment;
            }
        });
    }

    private void addFileTypeHandler(final String name, final String[] extensions, final FileTypeCategory category, final Class<? extends Parser> parserClass, final Class<? extends SymLoader> symLoaderClass) {
        this.addFileTypeHandler(new FileTypeHandler() {
            @Override
            public Parser getParser() {
                try {
                    if (parserClass == null) {
                        return null;
                    }
                    return parserClass.getConstructor((Class<?>[])new Class[0]).newInstance(new Object[0]);
                }
                catch (Exception x) {
                    Logger.getLogger(FileTypeHolder.class.getName()).log(Level.SEVERE, "Failed to create Parser " + parserClass.getName() + " reason = " + ((x.getCause() == null) ? x.getMessage() : x.getCause().getMessage()));
                    return null;
                }
            }

            @Override
            public String getName() {
                return name;
            }

            @Override
            public String[] getExtensions() {
                return extensions;
            }

            @Override
            public SymLoader createSymLoader(final URI uri, final String featureName, final AnnotatedSeqGroup group) {
                try {
                    final Constructor<?> con = symLoaderClass.getConstructor(URI.class, String.class, AnnotatedSeqGroup.class);
                    return (SymLoader)con.newInstance(uri, featureName, group);
                }
                catch (Exception x) {
                    Logger.getLogger(FileTypeHolder.class.getName()).log(Level.SEVERE, "Failed to create SymLoader " + symLoaderClass.getName() + " reason = " + ((x.getCause() == null) ? x.getMessage() : x.getCause().getMessage()));
                    return null;
                }
            }

            @Override
            public IndexWriter getIndexWriter(final String stream_name) {
                final Parser parser = this.getParser();
                if (parser instanceof IndexWriter) {
                    return (IndexWriter)parser;
                }
                return null;
            }

            @Override
            public FileTypeCategory getFileTypeCategory() {
                return category;
            }
        });
    }

    public void addFileTypeHandler(final FileTypeHandler fileTypeHandler) {
        final String[] arr$;
        final String[] extensions = arr$ = fileTypeHandler.getExtensions();
        for (final String extension : arr$) {
            if (this.fileTypeHandlerMap.get(extension) != null) {
                Logger.getLogger(ServerUtils.class.getName()).log(Level.SEVERE, "duplicate SymLoaderFactory for extension {0}!!!", new Object[] { extension });
            }
            this.fileTypeHandlerMap.put(extension, fileTypeHandler);
        }
    }

    public void removeFileTypeHandler(final FileTypeHandler fileTypeHandler) {
        final String[] arr$;
        final String[] extensions = arr$ = fileTypeHandler.getExtensions();
        for (final String extension : arr$) {
            if (this.fileTypeHandlerMap.get(extension) == null) {
                Logger.getLogger(ServerUtils.class.getName()).log(Level.SEVERE, "missing removed SymLoaderFactory for extension {0}!!!", new Object[] { extension });
            }
            this.fileTypeHandlerMap.remove(extension);
        }
    }

    public FileTypeHandler getFileTypeHandler(final String extension) {
        if (extension == null) {
            return null;
        }
        if (extension.startsWith("x-das-feature")) {
            return this.fileTypeHandlerMap.get("das2xml");
        }
        return this.fileTypeHandlerMap.get(extension);
    }

    public String getExtensionForURI(final String uri) {
        String extension = null;
        final String lc = extension = GeneralUtils.stripEndings(uri).toLowerCase();
        int position = lc.lastIndexOf(46);
        if (position > -1) {
            extension = lc.substring(position + 1);
            final String prefix = lc.substring(0, Math.max(0, position - 1));
            position = prefix.lastIndexOf(46);
            if (position > -1) {
                final String tryExtension = lc.substring(position + 1);
                if (this.getFileTypeHandler(tryExtension) != null) {
                    extension = tryExtension;
                }
            }
        }
        return extension;
    }

    public FileTypeHandler getFileTypeHandlerForURI(final String uri) {
        final String lc = GeneralUtils.stripEndings(uri).toLowerCase();
        FileTypeHandler fileTypeHandler = null;
        String extension = lc;
        int position = lc.lastIndexOf(46);
        if (position == -1) {
            fileTypeHandler = this.getFileTypeHandler(lc);
        }
        else {
            extension = lc.substring(position + 1);
            fileTypeHandler = this.getFileTypeHandler(extension);
            final String prefix = lc.substring(0, Math.max(0, position - 1));
            position = prefix.lastIndexOf(46);
            if (position > -1) {
                extension = lc.substring(position + 1);
                if (this.getFileTypeHandler(extension) != null) {
                    fileTypeHandler = this.getFileTypeHandler(extension);
                }
            }
        }
        if (fileTypeHandler == null) {
            Logger.getAnonymousLogger(FileTypeHolder.class.getName()).log(Level.SEVERE, "No file handler found for type {0} of uri {1}", new Object[] { extension, uri });
        }
        return fileTypeHandler;
    }

    public Map<String, List<String>> getNameToExtensionMap() {
        final Map<String, List<String>> nameToExtensionMap = new TreeMap<String, List<String>>();
        for (final FileTypeHandler fileTypeHandler : new HashSet<FileTypeHandler>(this.fileTypeHandlerMap.values())) {
            final String name = fileTypeHandler.getName();
            List<String> extensions = nameToExtensionMap.get(name);
            if (extensions == null) {
                extensions = new ArrayList<String>();
                nameToExtensionMap.put(name, extensions);
            }
            for (final String ext : fileTypeHandler.getExtensions()) {
                extensions.add(ext);
            }
        }
        return nameToExtensionMap;
    }

    public boolean isSequence(final String extension) {
        for (final FileTypeHandler fileTypeHandler : new HashSet<FileTypeHandler>(this.fileTypeHandlerMap.values())) {
            for (final String ext : fileTypeHandler.getExtensions()) {
                if (ext.equalsIgnoreCase(extension)) {
                    return fileTypeHandler.getFileTypeCategory() == FileTypeCategory.Sequence;
                }
            }
        }
        return false;
    }

    public Map<String, String[]> getSequenceToExtensionMap() {
        final Map<String, String[]> map = new TreeMap<String, String[]>();
        map.put("Fasta", new String[] { "fa", "fas", "fasta" });
        map.put(".2bit", new String[] { "2bit" });
        map.put("Binary", new String[] { "bnib" });
        return map;
    }

    public List<String> getTabixFileTypes() {
        return FileTypeHolder.TABIX_FILE_TYPES;
    }

    static {
        instance = new FileTypeHolder();
        TABIX_FILE_TYPES = new ArrayList<String>(Arrays.asList("sam", "bed", "bedgraph", "gff", "gff3", "gtf", "psl", "psl3", "pslx", "vcf"));
    }
}
