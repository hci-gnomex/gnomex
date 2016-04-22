//
// Decompiled by Procyon v0.5.30
//

package com.affymetrix.genometryImpl.symloader;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Iterator;
import java.io.BufferedInputStream;
import com.affymetrix.genometryImpl.util.GeneralUtils;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import com.affymetrix.genometryImpl.util.LocalUrlCacher;
import java.io.File;
import java.io.InputStream;
import com.affymetrix.genometryImpl.thread.ProgressUpdater;
import com.affymetrix.genometryImpl.thread.CThreadHolder;
import com.affymetrix.genometryImpl.SeqSpan;
import com.affymetrix.genometryImpl.symmetry.GenbankSym;
import java.util.ArrayList;
import com.affymetrix.genometryImpl.AnnotatedSeqGroup;
import java.net.URI;
import com.affymetrix.genometryImpl.util.LoadUtils;
import com.affymetrix.genometryImpl.BioSeq;
import java.util.List;
import java.util.Map;

public final class Genbank extends SymLoader
{
    private static final String NONAME = "no name";
    private static final Map<String, String> genbank_hash;
    private static final List<String> transcript_id_tags;
    private static final List<String> annot_id_tags;
    private static final List<String> annot_name_tags;
    private static final Map<String, String> featureTag_hash;
    private static final int BEGINNING_OF_ENTRY = 0;
    private static final String BEGINNING_OF_ENTRY_STRING = "ID";
    private static final int END_OF_ENTRY = 1;
    private static final String END_OF_ENTRY_STRING = "//";
    private static final int SEQUENCE = 2;
    private static final String SEQUENCE_STRING = "SQ";
    private static final int FEATURE = 3;
    private static final String FEATURE_STRING = "FT";
    private static final int FEATURE_HEADER = 4;
    private static final String FEATURE_HEADER_STRING = "FH";
    private static final int DEFINITION = 5;
    private static final String DEFINITION_STRING = "DE";
    private static final int ACCESSION = 6;
    private static final String ACCESSION_STRING = "AC";
    private static final int ORGANISM = 7;
    private static final String ORGANISM_STRING = "OS";
    private static final int REF_KEY = 8;
    private static final int MISC = 9;
    private static final int EMBL_CONTENT_OFFSET = 5;
    private static final int GENBANK_CONTENT_OFFSET = 12;
    private int content_offset;
    private int line_number;
    private String current_line;
    private String current_content;
    private String current_locus;
    private int current_line_type;
    private BioSeq currentSeq;
    protected final List<BioSeq> seqs;
    private static final List<LoadUtils.LoadStrategy> strategyList;

    public Genbank(final URI uri, final String featureName, final AnnotatedSeqGroup seq_group) {
        super(uri, featureName, seq_group);
        this.line_number = 0;
        this.current_locus = "";
        this.currentSeq = null;
        this.seqs = new ArrayList<BioSeq>();
    }

    @Override
    public List<LoadUtils.LoadStrategy> getLoadChoices() {
        return Genbank.strategyList;
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
    public List<BioSeq> getChromosomeList() throws Exception {
        this.init();
        return this.seqs;
    }

    @Override
    public List<GenbankSym> getGenome() throws Exception {
        return this.parse(null, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    @Override
    public List<GenbankSym> getChromosome(final BioSeq seq) throws Exception {
        return this.parse(seq, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    @Override
    public List<GenbankSym> getRegion(final SeqSpan span) throws Exception {
        this.symLoaderProgressUpdater = new SymLoaderProgressUpdater("Genbank SymLoaderProgressUpdater getRegion for " + this.uri + " - " + span, span);
        CThreadHolder.getInstance().getCurrentCThreadWorker().setProgressUpdater(this.symLoaderProgressUpdater);
        return this.parse(span.getBioSeq(), span.getMin(), span.getMax());
    }

    @Override
    protected boolean parseLines(final InputStream istr, final Map<String, Integer> chrLength, final Map<String, File> chrFiles) throws Exception {
        this.parseLinesProgressUpdater = new ParseLinesProgressUpdater(this, "Genbank parse lines " + this.uri);
        CThreadHolder.getInstance().getCurrentCThreadWorker().setProgressUpdater(this.parseLinesProgressUpdater);
        BufferedInputStream bis = null;
        BufferedReader br = null;
        try {
            bis = LocalUrlCacher.convertURIToBufferedUnzippedStream(this.uri);
            br = new BufferedReader(new InputStreamReader(bis));
            final String first_line = this.getCurrentData(br);
            if (first_line == null) {
                return false;
            }
            final String[] vals = first_line.split("\\s+");
            this.readFeature(br, vals[1], vals[2], chrLength);
            return true;
        }
        catch (Exception ex) {
            throw ex;
        }
        finally {
            GeneralUtils.safeClose(bis);
            GeneralUtils.safeClose(br);
        }
    }

    @Override
    protected void createResults(final Map<String, Integer> chrLength, final Map<String, File> chrFiles) {
        for (final Map.Entry<String, Integer> bioseq : chrLength.entrySet()) {
            final String seq_name = bioseq.getKey();
            BioSeq seq = this.group.getSeq(seq_name);
            if (seq == null) {
                seq = this.group.addSeq(seq_name, bioseq.getValue(), this.uri.toString());
            }
            this.seqs.add(seq);
        }
    }

    private void readFeature(final BufferedReader input, final String seqName, final String lengthStr, final Map<String, Integer> chrLength) {
        final boolean done = false;
        int length;
        try {
            length = Integer.valueOf(lengthStr);
        }
        catch (NumberFormatException ex) {
            length = -1;
        }
        try {
            this.lastSleepTime = System.nanoTime();
            while (this.current_line != null && !done) {
                this.checkSleep();
                this.notifyReadLine(this.current_line.length());
                this.getCurrentInput(input);
                switch (this.current_line_type) {
                    case 3:
                    case 4: {
                        this.readSingleFeature(input, seqName, length, chrLength);
                        continue;
                    }
                }
            }
        }
        catch (InterruptedException ex2) {}
    }

    private void readSingleFeature(final BufferedReader input, final String seqName, int length, final Map<String, Integer> chrLength) {
        BioSeq seq = null;
        while (this.current_line != null && this.current_line_type != 3) {
            this.getCurrentInput(input);
        }
        final boolean findLength = length < 0;
        if (findLength) {
            length = 0;
        }
        while (this.current_line != null && this.current_line_type == 3) {
            final GenbankFeature current_feature = new GenbankFeature();
            final String key = current_feature.getFeatureType(this.current_line);
            this.getCurrentInput(input);
            while (this.current_line != null && this.current_line_type == 3 && current_feature.addToFeature(this.current_line)) {
                this.getCurrentInput(input);
            }
            if (key == null) {
                Logger.getLogger(Genbank.class.getName()).log(Level.SEVERE, "GenBank read error: no key in line {0}", this.current_line);
            }
            else if (key.equals("source")) {
                final Map<String, List<String>> tagValues = current_feature.getTagValues();
                boolean chrFound = false;
                for (final String tag : tagValues.keySet()) {
                    final String value = current_feature.getValue(tag);
                    if (value != null && !value.equals("")) {
                        if (tag.equals("chromosome")) {
                            seq = this.group.getSeq(value);
                            if (seq == null) {
                                seq = new BioSeq(value, "", length);
                            }
                            chrLength.put(value, length);
                            chrFound = true;
                        }
                        else if (tag.equals("organism")) {}
                    }
                }
                if (chrFound) {
                    continue;
                }
                Logger.getLogger(Genbank.class.getName()).log(Level.WARNING, "No chromosome name found in sources");
                seq = new BioSeq(seqName, "", length);
                chrLength.put(seqName, length);
            }
            else {
                if (!findLength || (!key.equals("gene") && !key.equals("locus_tag"))) {
                    continue;
                }
                final List<int[]> locs = current_feature.initLocations();
                if (locs == null) {
                    continue;
                }
                if (locs.isEmpty()) {
                    continue;
                }
                final int loc1 = locs.get(0)[1];
                if (loc1 <= seq.getLength()) {
                    continue;
                }
                seq.setLength(loc1);
                chrLength.put(seq.getID(), seq.getLength());
            }
        }
    }

    public List<GenbankSym> parse(final BioSeq seq, final int min, final int max) throws Exception {
        BufferedInputStream bis = null;
        BufferedReader br = null;
        try {
            bis = LocalUrlCacher.convertURIToBufferedUnzippedStream(this.uri);
            br = new BufferedReader(new InputStreamReader(bis));
            return this.parse(br, seq, min, max);
        }
        catch (Exception ex) {
            throw ex;
        }
        finally {
            GeneralUtils.safeClose(bis);
            GeneralUtils.safeClose(br);
        }
    }

    public List<GenbankSym> parse(final BufferedReader input, final BioSeq seq, final int min, final int max) {
        final Map<String, GenbankSym> id2sym = new HashMap<String, GenbankSym>(1000);
        if (this.getCurrentData(input) == null) {
            return Collections.emptyList();
        }
        this.readFeature(input, id2sym, seq, min, max);
        return new ArrayList<GenbankSym>(id2sym.values());
    }

    private String getCurrentData(final BufferedReader input) {
        this.line_number = 0;
        this.current_line_type = -1;
        String first_line = null;
        this.getCurrentInput(input);
        while (this.current_line != null && first_line == null) {
            int index = this.current_line.indexOf("LOCUS       ");
            if (index < 0) {
                index = this.current_line.indexOf("ID   ");
            }
            if (index >= 0) {
                first_line = this.current_line.substring(index);
                this.current_line = first_line;
                this.current_line_type = this.getLineType(this.current_line);
                this.current_content = getRestOfLine(this.current_line, this.content_offset);
                this.current_locus = this.current_content.trim();
            }
            else {
                this.getCurrentInput(input);
            }
        }
        if (first_line == null) {
            Logger.getLogger(Genbank.class.getName()).log(Level.SEVERE, "GenBank read failed");
            return null;
        }
        return first_line;
    }

    private void readFeature(final BufferedReader input, final Map<String, GenbankSym> id2sym, final BioSeq seq, final int min, final int max) {
        boolean done = false;
        this.getCurrentInput(input);
        while (this.current_line != null && !done) {
            switch (this.current_line_type) {
                case 1: {
                    done = true;
                    continue;
                }
                case 2: {
                    this.ignoreSequence(input);
                    continue;
                }
                case 3:
                case 4: {
                    this.readSingleFeature(input, id2sym, seq, min, max);
                    continue;
                }
                case 5: {
                    this.getCurrentInput(input);
                    continue;
                }
                case 6: {
                    this.getCurrentInput(input);
                    continue;
                }
                case 7: {
                    this.getCurrentInput(input);
                    continue;
                }
                case 8: {
                    this.getCurrentInput(input);
                    continue;
                }
                case 9: {
                    this.getCurrentInput(input);
                    continue;
                }
                default: {
                    Logger.getLogger(Genbank.class.getName()).log(Level.WARNING, "not handling line type \"{0}\" (current content = {1})", new Object[] { this.current_line_type, this.current_content });
                    this.getCurrentInput(input);
                    continue;
                }
            }
        }
        if (this.current_line_type != 1) {
            Logger.getLogger(Genbank.class.getName()).log(Level.WARNING, "didn't find end of record (//) but saving annotations anyway.");
        }
    }

    private void getCurrentInput(final BufferedReader input) {
        ++this.line_number;
        this.current_line = getCurrentLine(input, this.line_number);
        if (this.current_line == null) {
            return;
        }
        this.current_line_type = this.getLineType(this.current_line);
        this.current_content = getRestOfLine(this.current_line, this.content_offset);
    }

    private static String getCurrentLine(final BufferedReader input, final int line_number) {
        String current_line = readLineFromInput(input, line_number);
        if (current_line == null) {
            return null;
        }
        boolean within_html = true;
        while (within_html) {
            final int html1 = current_line.indexOf("<");
            final int html2 = current_line.indexOf(">") + 1;
            within_html = (html1 >= 0 && html2 > 0 && html2 > html1);
            within_html = (within_html && current_line.indexOf("   gene   ") < 0 && current_line.indexOf("   mRNA   ") < 0);
            if (within_html) {
                final String prefix = current_line.substring(0, html1);
                final String suffix = (html2 < current_line.length()) ? current_line.substring(html2) : "";
                current_line = prefix + suffix;
            }
        }
        current_line = replaceInString(current_line, "&gt;", ">");
        current_line = replaceInString(current_line, "&lt;", "<");
        return current_line;
    }

    private static String readLineFromInput(final BufferedReader input, final int line_number) {
        try {
            return input.readLine();
        }
        catch (Exception e) {
            Logger.getLogger(Genbank.class.getName()).log(Level.SEVERE, "Unable to read line " + line_number, e);
            return null;
        }
    }

    private static String replaceInString(String current_line, final String get_out, final String put_in) {
        boolean seeking = true;
        while (seeking) {
            final int sgml = current_line.indexOf(get_out);
            seeking = (sgml >= 0);
            if (seeking) {
                final String prefix = current_line.substring(0, sgml);
                final int index = sgml + get_out.length();
                final String suffix = (index < current_line.length()) ? current_line.substring(index) : "";
                current_line = prefix + put_in + suffix;
            }
        }
        return current_line;
    }

    public int getLineType(final String line) {
        String line_key = null;
        if (line.length() >= 2 && (line.charAt(0) == '/' || Character.isLetter(line.charAt(0))) && (line.charAt(1) == '/' || Character.isLetter(line.charAt(1))) && (line.length() == 2 || (line.length() == 3 && line.endsWith(" ")) || (line.length() == 4 && line.endsWith("  ")) || (line.length() >= 5 && line.substring(2, 5).equals("   ")))) {
            line_key = line.substring(0, 2);
            this.content_offset = 5;
        }
        else if (line.length() > 0) {
            if (Character.isLetter(line.charAt(0))) {
                final int first_space = line.indexOf(32);
                if (first_space == -1) {
                    line_key = Genbank.genbank_hash.get(line);
                }
                else {
                    final String first_word = line.substring(0, first_space);
                    line_key = Genbank.genbank_hash.get(first_word);
                }
                if (line_key != null) {
                    this.content_offset = 12;
                }
            }
            else if (12 < line.length()) {
                final String sub_key = line.substring(0, 12).trim();
                line_key = Genbank.genbank_hash.get(sub_key);
            }
        }
        if (line_key == null) {
            return this.current_line_type;
        }
        if (line_key.startsWith("ID")) {
            return 0;
        }
        if (line_key.startsWith("OS")) {
            return 7;
        }
        if (line_key.startsWith("//")) {
            return 1;
        }
        if (line_key.startsWith("SQ")) {
            return 2;
        }
        if (line_key.startsWith("FH")) {
            return 4;
        }
        if (line_key.startsWith("FT")) {
            return 3;
        }
        if (line_key.startsWith("DE")) {
            return 5;
        }
        if (line_key.startsWith("AC")) {
            return 6;
        }
        return 9;
    }

    private static String getRestOfLine(final String line, final int content_offset) {
        if (line.length() > content_offset) {
            return line.substring(content_offset).trim();
        }
        return "";
    }

    private void readSingleFeature(final BufferedReader input, final Map<String, GenbankSym> id2sym, final BioSeq seq, final int min, final int max) {
        while (this.current_line != null && this.current_line_type != 3) {
            this.getCurrentInput(input);
        }
        GenbankSym annotation = null;
        while (this.current_line != null && this.current_line_type == 3) {
            final GenbankFeature current_feature = new GenbankFeature();
            final String key = current_feature.getFeatureType(this.current_line);
            this.getCurrentInput(input);
            while (this.current_line != null && this.current_line_type == 3 && current_feature.addToFeature(this.current_line)) {
                this.getCurrentInput(input);
            }
            if (key == null) {
                Logger.getLogger(Genbank.class.getName()).log(Level.SEVERE, "GenBank read error: no key in line {0}", this.current_line);
            }
            else if (key.equals("source")) {
                final Map<String, List<String>> tagValues = current_feature.getTagValues();
                for (final String tag : tagValues.keySet()) {
                    final String value = current_feature.getValue(tag);
                    if (value != null && !value.equals("")) {
                        if (tag.equals("chromosome")) {
                            this.currentSeq = this.group.getSeq(value);
                        }
                        else if (tag.equals("organism")) {}
                    }
                    if (this.currentSeq == null && this.seqs.size() == 1) {
                        this.currentSeq = this.seqs.get(0);
                    }
                }
            }
            else {
                if (seq != null && this.currentSeq != seq) {
                    continue;
                }
                if (key.equals("gene") || key.equals("locus_tag")) {
                    annotation = buildAnnotation(this.currentSeq, this.current_locus, current_feature, id2sym, min, max);
                }
                else {
                    if (annotation == null) {
                        continue;
                    }
                    if (key.equals("mRNA") || key.equals("rRNA") || key.equals("tRNA") || key.equals("scRNA") || key.equals("snRNA") || key.equals("snoRNA")) {
                        String value2 = current_feature.getValue("gene");
                        if (value2 != null && !value2.equals("")) {
                            annotation.setProperty("name", value2);
                        }
                        value2 = current_feature.getValue("locus_tag");
                        if (value2 != null && !value2.equals("")) {
                            annotation.setID(value2);
                        }
                        final List<int[]> locs = current_feature.getLocation();
                        if (locs == null || locs.isEmpty()) {
                            Logger.getLogger(Genbank.class.getName()).log(Level.WARNING, "no location for key {0} for {1}", new Object[] { key, current_feature.toString() });
                        }
                        else {
                            for (final int[] loc : locs) {
                                annotation.addBlock(loc[0], loc[1]);
                            }
                        }
                    }
                    else if (key.equals("CDS")) {
                        final List<int[]> locs2 = current_feature.getLocation();
                        if (locs2 == null || locs2.isEmpty()) {
                            Logger.getLogger(Genbank.class.getName()).log(Level.WARNING, "no location for key {0} for {1}", new Object[] { key, current_feature.toString() });
                        }
                        else {
                            for (final int[] loc2 : locs2) {
                                annotation.addCDSBlock(loc2[0], loc2[1]);
                            }
                        }
                    }
                    else {
                        Logger.getLogger(Genbank.class.getName()).log(Level.WARNING, "ignoring {0} features; next line is {1}", new Object[] { key, this.line_number });
                    }
                }
            }
        }
    }

    private void ignoreSequence(final BufferedReader input) {
        this.getCurrentInput(input);
        while (this.current_line != null && this.current_line.length() > 0 && this.current_line_type == 2) {
            this.getCurrentInput(input);
        }
    }

    private static GenbankSym buildAnnotation(final BioSeq seq, final String type, final GenbankFeature pub_feat, final Map<String, GenbankSym> id2sym, final int min, final int max) {
        String id = getAnnotationId(pub_feat);
        if (id == null || id.equals("")) {
            id = pub_feat.getValue("protein_id");
            if (id == null || id.equals("")) {
                Logger.getLogger(Genbank.class.getName()).log(Level.WARNING, "no id for {0}", pub_feat.toString());
                id = "no name";
            }
        }
        String name = getAnnotationName(pub_feat);
        if (name == null || name.equals("")) {
            name = id;
        }
        GenbankSym annotation = id2sym.get(id);
        if (annotation == null) {
            final List<int[]> locs = pub_feat.getLocation();
            if (locs == null || locs.isEmpty()) {
                Logger.getLogger(Genbank.class.getName()).log(Level.WARNING, "no location for {0}", pub_feat.toString());
                annotation = new GenbankSym(type, seq, 0, 0, name);
            }
            final int loc0 = locs.get(0)[0];
            final int loc2 = locs.get(0)[1];
            if (loc0 >= loc2 && (min >= loc2 || max <= loc0)) {
                return null;
            }
            if (loc2 < loc0 && (min >= loc0 || max <= loc2)) {
                return null;
            }
            annotation = new GenbankSym(type, seq, locs.get(0)[0], locs.get(0)[1], name);
            id2sym.put(id, annotation);
        }
        setDescription(annotation, pub_feat);
        if (!pub_feat.getValue("pseudo").equals("")) {
            annotation.setProperty("pseudogene", "true");
        }
        return annotation;
    }

    private static String getFeatureId(final GenbankFeature pub_feat, final List<String> tags) {
        String id = "";
        String tag;
        for (int i = 0; i < tags.size() && (id == null || id.equals("")); id = pub_feat.getValue(tag), ++i) {
            tag = tags.get(i);
        }
        return id;
    }

    private static String getAnnotationId(final GenbankFeature pub_feat) {
        String id = getFeatureId(pub_feat, Genbank.annot_id_tags);
        if (id == null || id.equals("")) {
            id = getFeatureId(pub_feat, Genbank.annot_name_tags);
        }
        return id;
    }

    private static String getAnnotationName(final GenbankFeature pub_feat) {
        return getFeatureId(pub_feat, Genbank.annot_name_tags);
    }

    private static void setDescription(final GenbankSym seq, final GenbankFeature pub_feat) {
        final Map<String, List<String>> tagValues = pub_feat.getTagValues();
        for (final String tag : tagValues.keySet()) {
            final String value = pub_feat.getValue(tag);
            if (value != null && !value.equals("")) {
                if (tag.equals("chromosome")) {
                    continue;
                }
                if (tag.equals("organism")) {
                    continue;
                }
                seq.setProperty(tag, value);
            }
        }
    }

    static {
        (genbank_hash = new HashMap<String, String>()).put("LOCUS", "ID");
        Genbank.genbank_hash.put("DEFINITION", "DE");
        Genbank.genbank_hash.put("ACCESSION", "AC");
        Genbank.genbank_hash.put("NID", "NID");
        Genbank.genbank_hash.put("VERSION", "DT");
        Genbank.genbank_hash.put("KEYWORDS", "KW");
        Genbank.genbank_hash.put("SOURCE", "OS");
        Genbank.genbank_hash.put("ORGANISM", "OC");
        Genbank.genbank_hash.put("REFERENCE", "RP");
        Genbank.genbank_hash.put("AUTHORS", "RA");
        Genbank.genbank_hash.put("TITLE", "RT");
        Genbank.genbank_hash.put("JOURNAL", "RL");
        Genbank.genbank_hash.put("PUBMED", "RK");
        Genbank.genbank_hash.put("MEDLINE", "RK");
        Genbank.genbank_hash.put("COMMENT", "CC");
        Genbank.genbank_hash.put("FEATURES", "FH");
        Genbank.genbank_hash.put("source", "FT");
        Genbank.genbank_hash.put("BASE", "SQ");
        Genbank.genbank_hash.put("ORIGIN", "SQ");
        transcript_id_tags = new ArrayList<String>();
        annot_id_tags = new ArrayList<String>();
        annot_name_tags = new ArrayList<String>();
        Genbank.transcript_id_tags.add("product");
        Genbank.transcript_id_tags.add("transcript_id");
        Genbank.transcript_id_tags.add("protein_id");
        Genbank.transcript_id_tags.add("codon_start");
        Genbank.annot_id_tags.add("locus_tag");
        Genbank.annot_id_tags.add("transposon");
        Genbank.annot_name_tags.add("gene");
        Genbank.annot_name_tags.add("standard_name");
        Genbank.annot_name_tags.add("allele");
        Genbank.annot_name_tags.add("label");
        Genbank.annot_name_tags.add("note");
        (featureTag_hash = new HashMap<String, String>()).put("map", "cyto_range");
        Genbank.featureTag_hash.put("RBS", "ribosomal_binding_site");
        Genbank.featureTag_hash.put("transposon", "transposable_element");
        Genbank.featureTag_hash.put("misc_RNA", "ncRNA");
        (strategyList = new ArrayList<LoadUtils.LoadStrategy>()).add(LoadUtils.LoadStrategy.NO_LOAD);
        Genbank.strategyList.add(LoadUtils.LoadStrategy.VISIBLE);
        Genbank.strategyList.add(LoadUtils.LoadStrategy.GENOME);
    }
}
