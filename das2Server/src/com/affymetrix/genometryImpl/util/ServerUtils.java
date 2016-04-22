// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.util;

import com.affymetrix.genometryImpl.parsers.FileTypeHandler;
import com.affymetrix.genometryImpl.symloader.SymLoaderInstNC;
import com.affymetrix.genometryImpl.parsers.FileTypeHolder;
import com.affymetrix.common.ExtensionPointHandler;
import java.net.URISyntaxException;
import java.net.URI;
import com.affymetrix.genometryImpl.symloader.BAM;
import com.affymetrix.genometryImpl.das2.SimpleDas2Type;
import com.affymetrix.genometryImpl.AnnotSecurity;
import java.io.DataInputStream;
import java.io.ByteArrayInputStream;
import com.affymetrix.genometryImpl.symloader.PSL;
import com.affymetrix.genometryImpl.parsers.PSLParser;
import com.affymetrix.genometryImpl.symmetry.UcscPslSym;
import com.affymetrix.genometryImpl.MutableSeqSpan;
import com.affymetrix.genometryImpl.span.SimpleMutableSeqSpan;
import com.affymetrix.genometryImpl.symmetry.SearchableSeqSymmetry;
import com.affymetrix.genometryImpl.span.SimpleSeqSpan;
import com.affymetrix.genometryImpl.SeqSpan;
import com.affymetrix.genometryImpl.parsers.IndexWriter;
import com.affymetrix.genometryImpl.symmetry.SymWithProps;
import com.affymetrix.genometryImpl.symmetry.SeqSymmetry;
import com.affymetrix.genometryImpl.parsers.useq.USeqUtilities;
import com.affymetrix.genometryImpl.symloader.SymLoader;
import com.affymetrix.genometryImpl.BioSeq;
import java.util.Arrays;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import com.affymetrix.genometryImpl.parsers.AnnotsXmlParser;
import java.util.Iterator;
import com.affymetrix.genometryImpl.comparator.GenomeVersionDateComparator;
import java.util.LinkedHashMap;
import java.util.Comparator;
import java.util.Collections;
import java.util.Collection;
import java.util.ArrayList;
import com.affymetrix.genometryImpl.comparator.MatchToListComparator;
import com.affymetrix.genometryImpl.AnnotatedSeqGroup;
import java.util.Map;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.io.IOException;
import java.io.InputStream;
import com.affymetrix.genometryImpl.parsers.LiftParser;
import com.affymetrix.genometryImpl.parsers.ChromInfoParser;
import com.affymetrix.genometryImpl.GenometryModel;
import java.io.FileInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.File;
import java.util.List;
import java.util.regex.Pattern;

public abstract class ServerUtils
{
    private static final String annots_filename = "annots.xml";
    private static final String graph_dir_suffix = ".graphs.seqs";
    private static final boolean SORT_SOURCES_BY_ORGANISM = true;
    private static final boolean SORT_VERSIONS_BY_DATE_CONVENTION = true;
    private static final Pattern interval_splitter;
    private static final String modChromInfo = "mod_chromInfo.txt";
    private static final String liftAll = "liftAll.lft";
    public static final List<String> BAR_FORMATS;
    private static final List<ServerTypeI> DEFAULT_SERVER_TYPES;
    
    public static void parseChromosomeData(final File genome_directory, final String genome_version) throws IOException {
        final File chrom_info_file = new File(genome_directory, "mod_chromInfo.txt");
        if (chrom_info_file.exists()) {
            Logger.getLogger(ServerUtils.class.getName()).log(Level.INFO, "parsing {0} for: {1}", new Object[] { "mod_chromInfo.txt", genome_version });
            final InputStream chromstream = new FileInputStream(chrom_info_file);
            try {
                ChromInfoParser.parse(chromstream, GenometryModel.getGenometryModel(), genome_version);
            }
            finally {
                GeneralUtils.safeClose(chromstream);
            }
        }
        else {
            Logger.getLogger(ServerUtils.class.getName()).log(Level.INFO, "couldn't find {0} for: {1}", new Object[] { "mod_chromInfo.txt", genome_version });
            Logger.getLogger(ServerUtils.class.getName()).log(Level.INFO, "looking for {0} instead", "liftAll.lft");
            final File lift_file = new File(genome_directory, "liftAll.lft");
            if (lift_file.exists()) {
                Logger.getLogger(ServerUtils.class.getName()).log(Level.INFO, "parsing {0} for: {1}", new Object[] { "liftAll.lft", genome_version });
                final InputStream liftstream = new FileInputStream(lift_file);
                try {
                    LiftParser.parse(liftstream, GenometryModel.getGenometryModel(), genome_version);
                }
                finally {
                    GeneralUtils.safeClose(liftstream);
                }
            }
            else {
                Logger.getLogger(ServerUtils.class.getName()).log(Level.SEVERE, "couldn't find {0} or {1} for genome!!! {2}", new Object[] { "mod_chromInfo.txt", "liftAll.lft", genome_version });
            }
        }
    }
    
    public static HashMap<String, String> loadFileIntoHashMap(final File file) {
        BufferedReader in = null;
        final HashMap<String, String> names = new HashMap<String, String>();
        try {
            in = new BufferedReader(new FileReader(file));
            String line;
            while ((line = in.readLine()) != null) {
                line = line.trim();
                if (line.length() != 0) {
                    if (line.startsWith("#")) {
                        continue;
                    }
                    final String[] keyValue = line.split("\\s+");
                    if (keyValue.length < 2) {
                        continue;
                    }
                    names.put(keyValue[0], keyValue[1]);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            GeneralUtils.safeClose(in);
        }
        return names;
    }
    
    public static void loadSynonyms(final File synfile, final SynonymLookup lookup) {
        if (synfile.exists()) {
            Logger.getLogger(ServerUtils.class.getName()).log(Level.INFO, "Synonym file {0} found, loading synonyms", synfile.getName());
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(synfile);
                lookup.loadSynonyms(fis);
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
            finally {
                GeneralUtils.safeClose(fis);
            }
        }
        else {
            Logger.getLogger(ServerUtils.class.getName()).log(Level.INFO, "Synonym file {0} not found, therefore not using synonyms", synfile.getName());
        }
    }
    
    public static void sortGenomes(Map<String, List<AnnotatedSeqGroup>> organisms, final String org_order_filename) {
        final File org_order_file = new File(org_order_filename);
        if (org_order_file.exists()) {
            final Comparator<String> org_comp = new MatchToListComparator(org_order_filename);
            final List<String> orglist = new ArrayList<String>(organisms.keySet());
            Collections.sort(orglist, org_comp);
            final Map<String, List<AnnotatedSeqGroup>> sorted_organisms = new LinkedHashMap<String, List<AnnotatedSeqGroup>>();
            for (final String org : orglist) {
                sorted_organisms.put(org, organisms.get(org));
            }
            organisms = sorted_organisms;
        }
        final Comparator<AnnotatedSeqGroup> date_comp = new GenomeVersionDateComparator();
        for (final List<AnnotatedSeqGroup> versions : organisms.values()) {
            Collections.sort(versions, date_comp);
        }
    }
    
    public static void loadAnnots(final File genomeDir, final AnnotatedSeqGroup genome, final Map<AnnotatedSeqGroup, List<AnnotsXmlParser.AnnotMapElt>> annots_map, final Map<String, String> graph_name2dir, final Map<String, String> graph_name2file, final String dataRoot) throws IOException {
        if (genomeDir.isDirectory()) {
            loadAnnotsFromDir(genomeDir.getName(), genome, genomeDir, "", annots_map, graph_name2dir, graph_name2file, dataRoot);
        }
        else {
            Logger.getLogger(ServerUtils.class.getName()).log(Level.WARNING, "{0} is not a directory.  Skipping.", genomeDir.getAbsolutePath());
        }
    }
    
    private static void loadAnnotsFromDir(final String type_name, final AnnotatedSeqGroup genome, final File current_file, final String new_type_prefix, final Map<AnnotatedSeqGroup, List<AnnotsXmlParser.AnnotMapElt>> annots_map, final Map<String, String> graph_name2dir, final Map<String, String> graph_name2file, final String dataRoot) throws IOException {
        final File annot = new File(current_file, "annots.xml");
        if (annot.exists()) {
            FileInputStream istr = null;
            try {
                istr = new FileInputStream(annot);
                List<AnnotsXmlParser.AnnotMapElt> annotList = annots_map.get(genome);
                if (annotList == null) {
                    annotList = new ArrayList<AnnotsXmlParser.AnnotMapElt>();
                    annots_map.put(genome, annotList);
                }
                AnnotsXmlParser.parseAnnotsXml(istr, annotList);
            }
            catch (FileNotFoundException ex) {
                Logger.getLogger(ServerUtils.class.getName()).log(Level.SEVERE, null, ex);
            }
            finally {
                GeneralUtils.safeClose(istr);
            }
        }
        if (type_name.endsWith(".graphs.seqs")) {
            final String graph_name = type_name.substring(0, type_name.length() - ".graphs.seqs".length());
            Logger.getLogger(ServerUtils.class.getName()).log(Level.FINE, "@@@ adding graph directory to types: {0}, path: {1}", new Object[] { graph_name, current_file.getPath() });
            graph_name2dir.put(graph_name, current_file.getPath());
            genome.addType(graph_name, null);
        }
        else {
            final File[] child_files = current_file.listFiles(new HiddenFileFilter());
            Arrays.sort(child_files);
            for (final File child_file : child_files) {
                loadAnnotsFromFile(child_file, genome, new_type_prefix, annots_map, graph_name2dir, graph_name2file, dataRoot);
            }
        }
    }
    
    private static void loadAnnotsFromFile(final File current_file, final AnnotatedSeqGroup genome, final String type_prefix, final Map<AnnotatedSeqGroup, List<AnnotsXmlParser.AnnotMapElt>> annots_map, final Map<String, String> graph_name2dir, final Map<String, String> graph_name2file, final String dataRoot) throws IOException {
        String file_name = current_file.getName();
        String extension = ParserController.getExtension(GeneralUtils.getUnzippedName(current_file.getName()));
        if (extension != null && extension.length() > 0) {
            file_name = file_name.substring(0, file_name.lastIndexOf(extension));
            extension = extension.substring(extension.indexOf(46) + 1);
        }
        final String type_name = type_prefix + file_name;
        if (current_file.isDirectory()) {
            final String new_type_prefix = type_name + "/";
            loadAnnotsFromDir(type_name, genome, current_file, new_type_prefix, annots_map, graph_name2dir, graph_name2file, dataRoot);
            return;
        }
        if (isSequenceFile(current_file) || isGraph(current_file, type_name, graph_name2file, genome) || isAnnotsFile(current_file)) {
            return;
        }
        if (isSymLoader(extension)) {
            final List<AnnotsXmlParser.AnnotMapElt> annotList = annots_map.get(genome);
            final String annotTypeName = ParserController.getAnnotType(annotList, current_file.getName(), extension, type_name);
            genome.addType(annotTypeName, null);
            for (final BioSeq originalSeq : genome.getSeqList()) {
                final SymLoader symloader = determineLoader(extension, current_file.toURI(), type_name, genome);
                originalSeq.addSymLoader(annotTypeName, symloader);
            }
            return;
        }
        if (!annots_map.isEmpty() && annots_map.containsKey(genome) && AnnotsXmlParser.AnnotMapElt.findFileNameElt(file_name, annots_map.get(genome)) == null) {
            Logger.getLogger(ServerUtils.class.getName()).log(Level.INFO, "Ignoring file {0} which was not found in annots.xml", file_name);
            return;
        }
        indexOrLoadFile(dataRoot, current_file, type_name, extension, annots_map, genome, null);
    }
    
    private static boolean isSequenceFile(final File current_file) {
        return current_file.getName().equals("mod_chromInfo.txt") || current_file.getName().equals("liftAll.lft");
    }
    
    public static boolean isResidueFile(final String format) {
        return format.equalsIgnoreCase("bnib") || format.equalsIgnoreCase("fa") || format.equalsIgnoreCase("2bit");
    }
    
    private static boolean isAnnotsFile(final File current_file) {
        return current_file.getName().equals("annots.xml");
    }
    
    private static boolean isGraph(final File current_file, final String type_name, final Map<String, String> graph_name2file, final AnnotatedSeqGroup genome) {
        final String file_name = current_file.getName();
        if (file_name.endsWith(".bar") || USeqUtilities.USEQ_ARCHIVE.matcher(file_name).matches()) {
            final String file_path = current_file.getPath();
            Logger.getLogger(ServerUtils.class.getName()).log(Level.FINE, "@@@ adding graph file to types: {0}, path: {1}", new Object[] { type_name, file_path });
            graph_name2file.put(type_name, file_path);
            genome.addType(type_name, null);
            return true;
        }
        return false;
    }
    
    private static boolean isSymLoader(final String extension) {
        return extension.endsWith("bam") || isResidueFile(extension);
    }
    
    public static void loadGenoPubAnnotsFromFile(final String dataroot, final File current_file, final AnnotatedSeqGroup genome, final Map<AnnotatedSeqGroup, List<AnnotsXmlParser.AnnotMapElt>> annots_map, final String type_prefix, final Integer annot_id, final Map<String, String> graph_name2file) throws FileNotFoundException, IOException {
        if (isGenoPubSequenceFile(current_file) || isGenoPubGraph(current_file, type_prefix, graph_name2file, genome, annot_id)) {
            return;
        }
        final String currentFileName = current_file.getName();
        if (currentFileName.endsWith("bam")) {
            final List<AnnotsXmlParser.AnnotMapElt> annotList = annots_map.get(genome);
            final String annotTypeName = ParserController.getAnnotType(annotList, currentFileName, "bam", type_prefix);
            genome.addType(annotTypeName, annot_id);
            for (final BioSeq originalSeq : genome.getSeqList()) {
                final SymLoader symloader = determineLoader("bam", current_file.toURI(), type_prefix, genome);
                originalSeq.addSymLoader(annotTypeName, symloader);
            }
            return;
        }
        final String extension = ParserController.getExtension(GeneralUtils.getUnzippedName(current_file.getName()));
        indexOrLoadFile(dataroot, current_file, type_prefix, extension, annots_map, genome, annot_id);
    }
    
    public static void loadGenoPubAnnotFromDir(final String type_name, final String file_path, final AnnotatedSeqGroup genome, final File current_file, final Integer annot_id, final Map<String, String> graph_name2dir) {
        Logger.getLogger(ServerUtils.class.getName()).log(Level.FINE, "@@@ adding graph directory to types: {0}, path: {1}", new Object[] { type_name, file_path });
        graph_name2dir.put(type_name, file_path);
        genome.addType(type_name, annot_id);
    }
    
    public static void unloadGenoPubAnnot(final String type_name, final AnnotatedSeqGroup genome, final Map<String, String> graph_name2dir) {
        if (graph_name2dir != null && graph_name2dir.containsKey(type_name)) {
            Logger.getLogger(ServerUtils.class.getName()).log(Level.FINE, "@@@ removing graph directory to types: {0}", type_name);
            graph_name2dir.remove(type_name);
        }
        else {
            Logger.getLogger(ServerUtils.class.getName()).log(Level.FINE, "@@@ removing annotation {0}", type_name);
            final List<BioSeq> seqList = genome.getSeqList();
            for (final BioSeq aseq : seqList) {
                SymWithProps tannot = aseq.getAnnotation(type_name);
                if (tannot != null) {
                    aseq.unloadAnnotation(tannot);
                    tannot = null;
                }
                else {
                    IndexingUtils.IndexedSyms iSyms = aseq.getIndexedSym(type_name);
                    if (iSyms == null) {
                        continue;
                    }
                    if (!aseq.removeIndexedSym(type_name)) {
                        Logger.getLogger(ServerUtils.class.getName()).log(Level.WARNING, "Unable to remove indexed annotation {0}", type_name);
                    }
                    iSyms = null;
                }
            }
        }
        genome.removeType(type_name);
    }
    
    private static boolean isGenoPubSequenceFile(final File current_file) {
        return current_file.getName().equals("mod_chromInfo.txt") || current_file.getName().equals("liftAll.lft");
    }
    
    private static boolean isGenoPubGraph(final File current_file, final String type_prefix, final Map<String, String> graph_name2file, final AnnotatedSeqGroup genome, final Integer annot_id) {
        final String file_name = current_file.getName();
        if (file_name.endsWith(".bar") || USeqUtilities.USEQ_ARCHIVE.matcher(file_name).matches()) {
            final String file_path = current_file.getPath();
            Logger.getLogger(ServerUtils.class.getName()).log(Level.FINE, "@@@ adding graph file to types: {0}, path: {1}", new Object[] { type_prefix, file_path });
            graph_name2file.put(type_prefix, file_path);
            genome.addType(type_prefix, annot_id);
            return true;
        }
        return false;
    }
    
    private static void indexOrLoadFile(final String dataRoot, final File file, final String annot_name, final String extension, final Map<AnnotatedSeqGroup, List<AnnotsXmlParser.AnnotMapElt>> annots_map, final AnnotatedSeqGroup genome, final Integer annot_id) throws FileNotFoundException, IOException {
        final String stream_name = GeneralUtils.getUnzippedName(file.getName());
        final IndexWriter iWriter = ParserController.getIndexWriter(stream_name);
        final List<AnnotsXmlParser.AnnotMapElt> annotList = annots_map.get(genome);
        final String annotTypeName = ParserController.getAnnotType(annotList, file.getName(), extension, annot_name);
        final AnnotatedSeqGroup tempGenome = AnnotatedSeqGroup.tempGenome(genome);
        if (iWriter == null) {
            loadAnnotFile(file, annotTypeName, annotList, genome, false);
            getAddedChroms(genome, tempGenome, false);
            getAlteredChroms(genome, tempGenome, false);
            return;
        }
        final List<? extends SeqSymmetry> loadedSyms = loadAnnotFile(file, annotTypeName, annotList, tempGenome, true);
        getAddedChroms(tempGenome, genome, true);
        getAlteredChroms(tempGenome, genome, true);
        String returnTypeName = annotTypeName;
        if (stream_name.endsWith(".link.psl")) {
            returnTypeName = annotTypeName + " " + "netaffx consensus";
        }
        genome.addType(returnTypeName, annot_id);
        createDirIfNecessary(IndexingUtils.indexedGenomeDirName(dataRoot, genome));
        IndexingUtils.determineIndexes(genome, tempGenome, dataRoot, file, loadedSyms, iWriter, annotTypeName, returnTypeName, extension);
    }
    
    public static boolean createDirIfNecessary(final String dirName) {
        final File newFile = new File(dirName);
        if (!newFile.exists()) {
            if (!new File(dirName).mkdirs()) {
                Logger.getLogger(ServerUtils.class.getName()).log(Level.SEVERE, "Couldn''t create directory: {0}", dirName);
                return false;
            }
            Logger.getLogger(ServerUtils.class.getName()).log(Level.FINE, "Created new directory: {0}", dirName);
        }
        return true;
    }
    
    public static List<? extends SeqSymmetry> loadAnnotFile(final File current_file, final String type_name, final List<AnnotsXmlParser.AnnotMapElt> annotList, final AnnotatedSeqGroup genome, final boolean isIndexed) throws FileNotFoundException, IOException {
        final String stream_name = GeneralUtils.getUnzippedName(current_file.getName());
        InputStream istr = null;
        try {
            istr = GeneralUtils.getInputStream(current_file, new StringBuffer());
            if (!isIndexed) {
                return ParserController.parse(istr, annotList, stream_name, genome, type_name);
            }
            return ParserController.parseIndexed(istr, annotList, stream_name, genome, type_name);
        }
        finally {
            GeneralUtils.safeClose(istr);
        }
    }
    
    public static SeqSpan getLocationSpan(final String seqid, final String rng, final AnnotatedSeqGroup group) {
        if (seqid == null || group == null) {
            return null;
        }
        final BioSeq seq = group.getSeq(seqid);
        if (seq == null) {
            return null;
        }
        boolean forward = true;
        int min;
        int max;
        if (rng == null) {
            min = 0;
            max = seq.getLength();
        }
        else {
            try {
                final String[] subfields = ServerUtils.interval_splitter.split(rng);
                min = Integer.parseInt(subfields[0]);
                max = Integer.parseInt(subfields[1]);
                if (subfields.length >= 3 && subfields[2].equals("-1")) {
                    forward = false;
                }
            }
            catch (Exception ex) {
                Logger.getLogger(ServerUtils.class.getName()).log(Level.INFO, "Problem parsing a query parameter range filter: {0}", rng);
                return null;
            }
        }
        SeqSpan span;
        if (forward) {
            span = new SimpleSeqSpan(min, max, seq);
        }
        else {
            span = new SimpleSeqSpan(max, min, seq);
        }
        return span;
    }
    
    public static List<SeqSymmetry> getIntersectedSymmetries(final SeqSpan overlap_span, final String query_type, final SeqSpan inside_span) {
        List<SeqSymmetry> result = getOverlappedSymmetries(overlap_span, query_type);
        if (result == null) {
            result = Collections.emptyList();
        }
        if (inside_span != null) {
            result = specifiedInsideSpan(inside_span, result);
        }
        return result;
    }
    
    public static List<SeqSymmetry> getOverlappedSymmetries(final SeqSpan query_span, final String annot_type) {
        final BioSeq seq = query_span.getBioSeq();
        final SymWithProps container = seq.getAnnotation(annot_type);
        if (container != null) {
            Logger.getLogger(ServerUtils.class.getName()).log(Level.FINE, "non-indexed request for {0}", annot_type);
            for (int annot_count = container.getChildCount(), i = 0; i < annot_count; ++i) {
                final SeqSymmetry sym = container.getChild(i);
                if (sym instanceof SearchableSeqSymmetry) {
                    final SearchableSeqSymmetry target_sym = (SearchableSeqSymmetry)sym;
                    return target_sym.getOverlappingChildren(query_span);
                }
            }
        }
        else {
            Logger.getLogger(ServerUtils.class.getName()).log(Level.FINE, "indexed request for {0}", annot_type);
            final IndexingUtils.IndexedSyms iSyms = seq.getIndexedSym(annot_type);
            if (iSyms != null) {
                return getIndexedOverlappedSymmetries(query_span, iSyms, annot_type, seq.getSeqGroup());
            }
        }
        return Collections.emptyList();
    }
    
    public static <SymExtended extends SeqSymmetry> List<SymExtended> specifiedInsideSpan(final SeqSpan inside_span, List<SymExtended> result) {
        final int inside_min = inside_span.getMin();
        final int inside_max = inside_span.getMax();
        final BioSeq iseq = inside_span.getBioSeq();
        final MutableSeqSpan testspan = new SimpleMutableSeqSpan();
        final List<SymExtended> orig_result = result;
        result = new ArrayList<SymExtended>(orig_result.size());
        for (final SymExtended sym : orig_result) {
            sym.getSpan(iseq, testspan);
            if (testspan.getMin() >= inside_min && testspan.getMax() <= inside_max) {
                result.add(sym);
            }
        }
        Logger.getLogger(ServerUtils.class.getName()).log(Level.FINE, "  overlapping annotations that passed inside_span constraints: {0}", result.size());
        return result;
    }
    
    public static List<SeqSymmetry> getIndexedOverlappedSymmetries(final SeqSpan overlap_span, final IndexingUtils.IndexedSyms iSyms, final String annot_type, final AnnotatedSeqGroup group) {
        final List<? extends SeqSymmetry> symList = getIndexedSymmetries(overlap_span, iSyms, annot_type, group);
        return filterForOverlappingSymmetries(overlap_span, symList);
    }
    
    public static List<SeqSymmetry> filterForOverlappingSymmetries(final SeqSpan overlapSpan, final List<? extends SeqSymmetry> symList) {
        final List<SeqSymmetry> newList = new ArrayList<SeqSymmetry>(symList.size());
        for (final SeqSymmetry sym : symList) {
            if (sym instanceof UcscPslSym) {
                final UcscPslSym uSym = (UcscPslSym)sym;
                final SeqSpan span = uSym.getSpan(uSym.getTargetSeq());
                if (!SeqUtils.overlap(span, overlapSpan)) {
                    continue;
                }
                newList.add(sym);
            }
            else {
                if (!isOverlapping(sym, overlapSpan)) {
                    continue;
                }
                newList.add(sym);
            }
        }
        return newList;
    }
    
    private static boolean isOverlapping(final SeqSymmetry sym, final SeqSpan overlapSpan) {
        for (int spanCount = sym.getSpanCount(), i = 0; i < spanCount; ++i) {
            final SeqSpan span = sym.getSpan(i);
            if (span != null && SeqUtils.overlap(span, overlapSpan)) {
                return true;
            }
        }
        return false;
    }
    
    private static List<? extends SeqSymmetry> getIndexedSymmetries(final SeqSpan overlap_span, final IndexingUtils.IndexedSyms iSyms, final String annot_type, final AnnotatedSeqGroup group) {
        InputStream newIstr = null;
        DataInputStream dis = null;
        try {
            final int[] overlapRange = new int[2];
            final int[] outputRange = new int[2];
            overlapRange[0] = overlap_span.getMin();
            overlapRange[1] = overlap_span.getMax();
            IndexingUtils.findMaxOverlap(overlapRange, outputRange, iSyms.min, iSyms.max);
            final int minPos = outputRange[0];
            final int maxPos = outputRange[1] + 1;
            if (minPos >= maxPos) {
                return Collections.emptyList();
            }
            final byte[] bytes = IndexingUtils.readBytesFromFile(iSyms.file, iSyms.filePos[minPos], (int)(iSyms.filePos[maxPos] - iSyms.filePos[minPos]));
            if ((iSyms.iWriter instanceof PSLParser || iSyms.iWriter instanceof PSL) && iSyms.ext.equalsIgnoreCase("link.psl")) {
                final String indexesFileName = iSyms.file.getAbsolutePath();
                newIstr = IndexingUtils.readAdditionalLinkPSLIndex(indexesFileName, annot_type, bytes);
            }
            else {
                newIstr = new ByteArrayInputStream(bytes);
            }
            dis = new DataInputStream(newIstr);
            return iSyms.iWriter.parse(dis, annot_type, group);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
        finally {
            GeneralUtils.safeClose(dis);
            GeneralUtils.safeClose(newIstr);
        }
    }
    
    public static void printGenomes(final Map<String, List<AnnotatedSeqGroup>> organisms) {
        for (final Map.Entry<String, List<AnnotatedSeqGroup>> ent : organisms.entrySet()) {
            final String org = ent.getKey();
            Logger.getLogger(ServerUtils.class.getName()).log(Level.INFO, "Organism: {0}", org);
            for (final AnnotatedSeqGroup version : ent.getValue()) {
                Logger.getLogger(ServerUtils.class.getName()).log(Level.INFO, "    Genome version: {0}, organism: {1}, seq count: {2}", new Object[] { version.getID(), version.getOrganism(), version.getSeqCount() });
            }
        }
    }
    
    public static Map<String, SimpleDas2Type> getAnnotationTypes(final String data_root, final AnnotatedSeqGroup genome, final AnnotSecurity annotSecurity) {
        final List<BioSeq> seqList = genome.getSeqList();
        final Map<String, SimpleDas2Type> genome_types = new LinkedHashMap<String, SimpleDas2Type>();
        for (final BioSeq aseq : seqList) {
            for (final String type : aseq.getTypeList()) {
                if (genome_types.get(type) != null) {
                    continue;
                }
                List<String> flist = Collections.emptyList();
                final SymWithProps tannot = aseq.getAnnotation(type);
                final SymWithProps first_child = (SymWithProps)tannot.getChild(0);
                if (first_child != null) {
                    final List formats = (List)first_child.getProperty("preferred_formats");
                    if (formats != null) {
                        flist = new ArrayList<String>(formats.size());
                        for (final Object o : formats) {
                            flist.add((String)o);
                        }
                    }
                }
                if (annotSecurity != null && !isAuthorized(genome, annotSecurity, type)) {
                    continue;
                }
                genome_types.put(type, new SimpleDas2Type(type, flist, getProperties(genome, annotSecurity, type)));
            }
            for (final String type : aseq.getIndexedTypeList()) {
                if (genome_types.get(type) != null) {
                    continue;
                }
                final IndexingUtils.IndexedSyms iSyms = aseq.getIndexedSym(type);
                final List<String> flist2 = new ArrayList<String>();
                flist2.addAll(iSyms.iWriter.getFormatPrefList());
                if (annotSecurity != null && !isAuthorized(genome, annotSecurity, type)) {
                    continue;
                }
                genome_types.put(type, new SimpleDas2Type(type, flist2, getProperties(genome, annotSecurity, type)));
            }
        }
        return genome_types;
    }
    
    public static void getSymloaderTypes(final AnnotatedSeqGroup genome, final AnnotSecurity annotSecurity, final Map<String, SimpleDas2Type> genome_types) {
        for (final BioSeq aseq : genome.getSeqList()) {
            for (final String type : aseq.getSymloaderList()) {
                final SymLoader sym = aseq.getSymLoader(type);
                if (genome_types.containsKey(type)) {
                    return;
                }
                if (annotSecurity != null && !isAuthorized(genome, annotSecurity, type)) {
                    continue;
                }
                genome_types.put(type, new SimpleDas2Type(type, sym.getFormatPrefList(), getProperties(genome, annotSecurity, type)));
            }
        }
    }
    
    public static void getGraphTypes(final String data_root, final AnnotatedSeqGroup genome, final AnnotSecurity annotSecurity, final Map<String, SimpleDas2Type> genome_types) {
        for (final String type : genome.getTypeList()) {
            if (!genome_types.containsKey(type)) {
                if (!isAuthorized(genome, annotSecurity, type)) {
                    continue;
                }
                if (annotSecurity == null) {
                    if (USeqUtilities.USEQ_ARCHIVE.matcher(type).matches()) {
                        genome_types.put(type, new SimpleDas2Type(genome.getID(), USeqUtilities.USEQ_FORMATS, getProperties(genome, annotSecurity, type)));
                    }
                    else {
                        genome_types.put(type, new SimpleDas2Type(genome.getID(), ServerUtils.BAR_FORMATS, getProperties(genome, annotSecurity, type)));
                    }
                }
                else if (annotSecurity.isBarGraphData(data_root, genome.getID(), type, genome.getAnnotationId(type))) {
                    genome_types.put(type, new SimpleDas2Type(genome.getID(), ServerUtils.BAR_FORMATS, getProperties(genome, annotSecurity, type)));
                }
                else if (annotSecurity.isUseqGraphData(data_root, genome.getID(), type, genome.getAnnotationId(type))) {
                    genome_types.put(type, new SimpleDas2Type(genome.getID(), USeqUtilities.USEQ_FORMATS, getProperties(genome, annotSecurity, type)));
                }
                else if (annotSecurity.isBamData(data_root, genome.getID(), type, genome.getAnnotationId(type))) {
                    genome_types.put(type, new SimpleDas2Type(genome.getID(), BAM.pref_list, getProperties(genome, annotSecurity, type)));
                }
                else {
                    Logger.getLogger(ServerUtils.class.getName()).log(Level.WARNING, "Non-graph annotation {0} encountered, but does not match known entry.  This annotation will not show in the types request.", type);
                }
            }
        }
    }
    
    private static boolean isAuthorized(final AnnotatedSeqGroup group, final AnnotSecurity annotSecurity, final String type) {
        final boolean isAuthorized = annotSecurity == null || annotSecurity.isAuthorized(group.getID(), type, group.getAnnotationId(type));
        Logger.getLogger(AnnotatedSeqGroup.class.getName()).log(Level.FINE, "{0} Annotation {1} ID={2}", new Object[] { isAuthorized ? "Showing  " : "Blocking ", type, group.getAnnotationId(type) });
        return isAuthorized;
    }
    
    private static Map<String, Object> getProperties(final AnnotatedSeqGroup group, final AnnotSecurity annotSecurity, final String type) {
        return (annotSecurity == null) ? null : annotSecurity.getProperties(group.getID(), type, group.getAnnotationId(type));
    }
    
    private static void getAddedChroms(final AnnotatedSeqGroup newGenome, final AnnotatedSeqGroup oldGenome, final boolean isIgnored) {
        if (oldGenome.getSeqCount() == newGenome.getSeqCount()) {
            return;
        }
        Logger.getLogger(ServerUtils.class.getName()).log(Level.WARNING, "found {0} chromosomes instead of {1}", new Object[] { newGenome.getSeqCount(), oldGenome.getSeqCount() });
        if (isIgnored) {
            Logger.getLogger(ServerUtils.class.getName()).log(Level.WARNING, "Due to indexing, this was ignored.");
        }
        else {
            Logger.getLogger(ServerUtils.class.getName()).log(Level.WARNING, "The genome has been altered.");
        }
        Logger.getLogger(ServerUtils.class.getName()).log(Level.WARNING, "Extra chromosomes : ");
        for (final BioSeq seq : newGenome.getSeqList()) {
            final BioSeq genomeSeq = oldGenome.getSeq(seq.getID());
            if (genomeSeq == null) {
                Logger.getLogger(ServerUtils.class.getName()).log(Level.WARNING, seq.getID());
            }
        }
    }
    
    private static void getAlteredChroms(final AnnotatedSeqGroup newGenome, final AnnotatedSeqGroup oldGenome, final boolean isIgnored) {
        final List<String> alteredChromStrings = new ArrayList<String>();
        for (final BioSeq seq : newGenome.getSeqList()) {
            final BioSeq genomeSeq = oldGenome.getSeq(seq.getID());
            if (genomeSeq != null && genomeSeq.getLength() != seq.getLength()) {
                alteredChromStrings.add(seq.getID() + ":" + seq.getLength() + "(was " + genomeSeq.getLength() + ") ");
            }
        }
        if (alteredChromStrings.size() > 0) {
            Logger.getLogger(ServerUtils.class.getName()).log(Level.WARNING, "altered chromosomes found for genome {0}. ", oldGenome.getID());
            if (isIgnored) {
                Logger.getLogger(ServerUtils.class.getName()).log(Level.WARNING, "Indexing; this may cause problems.");
            }
            else {
                Logger.getLogger(ServerUtils.class.getName()).log(Level.WARNING, "The genome has been altered.");
            }
            Logger.getLogger(ServerUtils.class.getName()).log(Level.WARNING, "Altered chromosomes : ");
            for (final String alteredChromString : alteredChromStrings) {
                Logger.getLogger(ServerUtils.class.getName()).log(Level.WARNING, alteredChromString);
            }
        }
    }
    
    public static String formatURL(String url, final ServerTypeI type) {
        try {
            url = url.replace(" ", "");
            url = new URI(url).normalize().toASCIIString();
        }
        catch (URISyntaxException ex) {
            final String message = "Unable to parse URL: '" + url + "'";
            Logger.getLogger(ServerUtils.class.getName()).log(Level.SEVERE, message, ex);
            throw new IllegalArgumentException(message, ex);
        }
        if (type == null) {
            return url;
        }
        return type.formatURL(url);
    }
    
    public static List<ServerTypeI> getServerTypes() {
        final List<ServerTypeI> serverTypes = (ExtensionPointHandler.getExtensionPoint((Class)ServerTypeI.class) == null) ? ServerUtils.DEFAULT_SERVER_TYPES : ExtensionPointHandler.getExtensionPoint((Class)ServerTypeI.class).getExtensionPointImpls();
        Collections.sort(serverTypes);
        return serverTypes;
    }
    
    public static SymLoader determineLoader(final String extension, final URI uri, final String featureName, final AnnotatedSeqGroup group) {
        final FileTypeHandler fileTypeHandler = FileTypeHolder.getInstance().getFileTypeHandler(extension);
        SymLoader symLoader;
        if (fileTypeHandler == null) {
            Logger.getLogger(ServerUtils.class.getName()).log(Level.WARNING, "Couldn't find any Symloader for {0} format. Opening whole file.", new Object[] { extension });
            symLoader = new SymLoaderInstNC(uri, featureName, group);
        }
        else {
            symLoader = fileTypeHandler.createSymLoader(uri, featureName, group);
        }
        return symLoader;
    }
    
    static {
        interval_splitter = Pattern.compile(":");
        (BAR_FORMATS = new ArrayList<String>()).add("bar");
        (DEFAULT_SERVER_TYPES = new ArrayList<ServerTypeI>()).add(ServerTypeI.DAS2);
        ServerUtils.DEFAULT_SERVER_TYPES.add(ServerTypeI.DAS);
        ServerUtils.DEFAULT_SERVER_TYPES.add(ServerTypeI.QuickLoad);
        ServerUtils.DEFAULT_SERVER_TYPES.add(ServerTypeI.LocalFiles);
    }
}
