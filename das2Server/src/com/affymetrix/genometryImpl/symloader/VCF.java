//
// Decompiled by Procyon v0.5.30
//

package com.affymetrix.genometryImpl.symloader;

import cern.colt.list.FloatArrayList;
import cern.colt.list.IntArrayList;
import java.util.Set;
import java.util.HashSet;
import com.affymetrix.genometryImpl.GenometryModel;
import net.sf.samtools.CigarElement;
import net.sf.samtools.CigarOperator;
import net.sf.samtools.Cigar;
import com.affymetrix.genometryImpl.symmetry.BAMSym;
import com.affymetrix.genometryImpl.util.ErrorHandler;
import java.util.regex.Matcher;
import java.util.Collection;
import com.affymetrix.genometryImpl.style.GraphState;
import java.util.Iterator;
import com.affymetrix.genometryImpl.SeqSpan;
import com.affymetrix.genometryImpl.style.ITrackStyleExtended;
import com.affymetrix.genometryImpl.style.DefaultTrackStyle;
import com.affymetrix.genometryImpl.symmetry.GraphIntervalSym;
import java.util.Arrays;
import com.affymetrix.genometryImpl.style.ITrackStyle;
import com.affymetrix.genometryImpl.span.SimpleSeqSpan;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.affymetrix.genometryImpl.symmetry.SimpleSymWithProps;
import com.affymetrix.genometryImpl.symmetry.SeqSymmetry;
import org.broad.tribble.readers.LineReader;
import com.affymetrix.genometryImpl.BioSeq;
import java.util.ArrayList;
import java.util.HashMap;
import com.affymetrix.genometryImpl.AnnotatedSeqGroup;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class VCF extends UnindexedSymLoader implements LineProcessor
{
    private static final String[] EXTENSIONS;
    private static final String NO_DATA = ".";
    private static final Pattern line_regex;
    private static final Pattern info_regex;
    private double version;
    private String[] samples;
    private Map<String, String> metaMap;
    private Map<String, INFO> infoMap;
    private Map<String, FILTER> filterMap;
    private Map<String, FORMAT> formatMap;
    private boolean combineGenotype;
    private List<String> selectedFields;
    private static final Pattern idPattern;
    private static final Pattern numberPattern;
    private static final Pattern typePattern;
    private static final Pattern descriptionPattern;

    public VCF(final URI uri, final String featureName, final AnnotatedSeqGroup group) {
        super(uri, featureName, group);
        this.version = -1.0;
        this.samples = new String[0];
        this.metaMap = new HashMap<String, String>();
        this.infoMap = new HashMap<String, INFO>();
        this.filterMap = new HashMap<String, FILTER>();
        this.formatMap = new HashMap<String, FORMAT>();
        this.selectedFields = new ArrayList<String>();
    }

    @Override
    public List<? extends SeqSymmetry> processLines(final BioSeq seq, final LineReader lineReader, final LineTrackerI lineTracker) {
        final SimpleSymWithProps mainSym = new SimpleSymWithProps();
        mainSym.setProperty("seq", seq);
        mainSym.setProperty("type", this.featureName);
        mainSym.setProperty("container sym", Boolean.TRUE);
        int line_count = 0;
        final Map<String, SimpleSymWithProps> dataMap = new HashMap<String, SimpleSymWithProps>();
        final Map<String, GraphData> graphDataMap = new HashMap<String, GraphData>();
        final Map<String, SimpleSymWithProps> genotypeDataMap = new HashMap<String, SimpleSymWithProps>();
        String line = null;
        try {
            while ((line = lineReader.readLine()) != null && !Thread.currentThread().isInterrupted()) {
                if (lineTracker != null) {
                    lineTracker.notifyReadLine(line.length());
                }
                if (line.startsWith("#")) {
                    ++line_count;
                }
                else {
                    if (line.length() <= 0) {
                        continue;
                    }
                    this.processDataLine(mainSym, seq, 0, Integer.MAX_VALUE, this.featureName, dataMap, graphDataMap, genotypeDataMap, line, line_count, this.combineGenotype);
                    ++line_count;
                }
            }
        }
        catch (Exception x) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "failed to parse vcf file ", x);
        }
        final SeqSpan span = new SimpleSeqSpan(seq.getMin(), seq.getMax(), seq);
        final List<SeqSymmetry> symlist = new ArrayList<SeqSymmetry>();
        if (mainSym.getChildCount() > 0) {
            mainSym.addSpan(span);
        }
        symlist.add(mainSym);
        for (final String key : dataMap.keySet()) {
            final SimpleSymWithProps container = dataMap.get(key);
            container.addSpan(span);
            symlist.add(container);
        }
        for (final String key : genotypeDataMap.keySet()) {
            final SimpleSymWithProps container = genotypeDataMap.get(key);
            container.addSpan(span);
            symlist.add(container);
        }
        final Map<String, ITrackStyle> styleMap = new HashMap<String, ITrackStyle>();
        for (final String key2 : graphDataMap.keySet()) {
            final GraphData graphData = graphDataMap.get(key2);
            final int dataSize = graphData.xData.size();
            final int[] xList = Arrays.copyOf(graphData.xData.elements(), dataSize);
            final float[] yList = Arrays.copyOf(graphData.yData.elements(), dataSize);
            final int[] wList = Arrays.copyOf(graphData.wData.elements(), dataSize);
            final GraphIntervalSym graphIntervalSym = new GraphIntervalSym(xList, wList, yList, key2, seq);
            final String comboKey = key2.substring(0, key2.lastIndexOf(47));
            final GraphState gstate = graphIntervalSym.getGraphState();
            if (this.combineGenotype && key2.indexOf(47) != key2.lastIndexOf(47)) {
                ITrackStyle combo_style = styleMap.get(comboKey);
                if (combo_style == null) {
                    combo_style = new DefaultTrackStyle(comboKey, true);
                    combo_style.setTrackName(comboKey);
                    combo_style.setExpandable(true);
                    combo_style.setCollapsed(true);
                    styleMap.put(comboKey, combo_style);
                }
                if (combo_style instanceof ITrackStyleExtended) {
                    gstate.setComboStyle((ITrackStyleExtended)combo_style, 0);
                }
                else {
                    gstate.setComboStyle(null, 0);
                }
                gstate.getTierStyle().setFloatTier(false);
            }
            else {
                gstate.setComboStyle(null, 0);
                gstate.getTierStyle().setFloatTier(false);
            }
            symlist.add(graphIntervalSym);
        }
        return symlist;
    }

    @Override
    public void init(final URI uri) {
    }

    public void setCombineGenotype(final boolean combineGenotype) {
        this.combineGenotype = combineGenotype;
    }

    public void select(final String name, final boolean separateTracks, final Map<String, List<String>> selections) {
        this.setCombineGenotype(!separateTracks);
        for (final String dataField : new ArrayList<String>(this.selectedFields)) {
            if (dataField.indexOf(47) > -1) {
                this.selectedFields.remove(dataField);
            }
        }
        for (final String type : selections.keySet()) {
            for (final String sample : selections.get(type)) {
                this.selectedFields.add(name + "/" + type + "/" + sample);
            }
        }
    }

    public List<String> getSelectedFields() {
        return this.selectedFields;
    }

    @Override
    public List<String> getFormatPrefList() {
        return Arrays.asList(VCF.EXTENSIONS);
    }

    public List<String> getAllFields() {
        return new ArrayList<String>(this.infoMap.keySet());
    }

    public List<String> getSamples() {
        return Arrays.asList(this.samples);
    }

    public List<String> getGenotypes() {
        return new ArrayList<String>(this.formatMap.keySet());
    }

    private String getID(final String line) {
        final Matcher matcher = VCF.idPattern.matcher(line);
        if (matcher.find()) {
            final String group = matcher.group();
            return group.substring(",ID=".length(), group.length() - 1);
        }
        return null;
    }

    private int getNumber(final String line) {
        int number = -1;
        final String numberString = this.getNumberString(line);
        if (numberString != null) {
            try {
                number = Integer.parseInt(numberString);
            }
            catch (NumberFormatException ex) {}
        }
        return number;
    }

    private String getNumberString(final String line) {
        final Matcher matcher = VCF.numberPattern.matcher(line);
        if (matcher.find()) {
            final String group = matcher.group();
            return group.substring(",Number=".length(), group.length() - 1);
        }
        return null;
    }

    private Type getType(final String line) {
        final Matcher matcher = VCF.typePattern.matcher(line);
        if (matcher.find()) {
            final String group = matcher.group();
            return Type.valueOf(group.substring(",Type=".length(), group.length() - 1));
        }
        return null;
    }

    private String getDescription(final String line) {
        final Matcher matcher = VCF.descriptionPattern.matcher(line);
        if (matcher.find()) {
            final String group = matcher.group();
            return group.substring(",Description=\"".length(), group.length() - 2);
        }
        return null;
    }

    private INFO getInfo(final String line) {
        final String dataline = "," + line.substring(1, line.length() - 1) + ",";
        int number = -1;
        boolean onePerAllele = false;
        boolean onePerGenotype = false;
        final String numberString = this.getNumberString(line);
        if (numberString != null) {
            if ("A".equals(numberString)) {
                onePerAllele = true;
            }
            else if ("G".equals(numberString)) {
                onePerGenotype = true;
            }
            else if (!".".equals(numberString)) {
                number = Integer.parseInt(numberString);
            }
        }
        return new INFO(this.getID(dataline), number, this.getType(dataline), this.getDescription(dataline), onePerAllele, onePerGenotype);
    }

    private FILTER getFilter(final String line) {
        final String dataline = "," + line.substring(1, line.length() - 1) + ",";
        return new FILTER(this.getID(dataline), this.getDescription(dataline));
    }

    private FORMAT getFormat(final String line) {
        final String dataline = "," + line.substring(1, line.length() - 1) + ",";
        return new FORMAT(this.getID(dataline), this.getNumber(dataline), this.getType(dataline), this.getDescription(dataline));
    }

    private void processMetaInformationLine(final String line) {
        if (line.startsWith("fileformat=")) {
            final String format = line.substring("fileformat=".length());
            if (format.equals("VCFv4.0")) {
                this.version = 4.0;
            }
            else {
                if (!format.equals("VCFv4.1")) {
                    ErrorHandler.errorPanel("file version not supported " + format);
                    throw new UnsupportedOperationException("file version not supported " + format);
                }
                this.version = 4.1;
            }
            Logger.getLogger("com.affymetrix.genometryImpl.symloader").log(Level.INFO, "vcf file version " + this.version);
        }
        else {
            if (line.startsWith("format=")) {
                final String format = line.substring("format=".length());
                ErrorHandler.errorPanel("file version not supported " + format);
                throw new UnsupportedOperationException("file version not supported " + format);
            }
            if (line.startsWith("INFO=")) {
                final INFO info = this.getInfo(line.substring("INFO=".length()));
                this.infoMap.put(info.getID(), info);
            }
            else if (line.startsWith("FILTER=")) {
                final FILTER filter = this.getFilter(line.substring("FILTER=".length()));
                this.filterMap.put(filter.getID(), filter);
            }
            else if (line.startsWith("FORMAT=")) {
                final FORMAT format2 = this.getFormat(line.substring("FORMAT=".length()));
                this.formatMap.put(format2.getID(), format2);
            }
            else {
                final int pos = line.indexOf(61);
                this.metaMap.put(line.substring(0, pos), line.substring(pos + 1));
            }
        }
    }

    private void processHeaderLine(final String line) {
        final String[] fields = VCF.line_regex.split(line);
        if (fields.length > 8) {
            this.samples = Arrays.copyOfRange(fields, 9, fields.length);
        }
        else {
            this.samples = new String[0];
        }
    }

    private SimpleSymWithProps getContainerSymFromMap(final Map<String, SimpleSymWithProps> symMap, final String key, final BioSeq seq) {
        SimpleSymWithProps container = symMap.get(key);
        if (container == null) {
            container = new SimpleSymWithProps();
            container.setProperty("seq", seq);
            container.setProperty("type", key);
            container.setProperty("id", key);
            container.setProperty("container sym", Boolean.TRUE);
            symMap.put(key, container);
        }
        return container;
    }

    private String getMultiple(final char c, final int count) {
        final StringBuffer sb = new StringBuffer();
        for (int i = 0; i < count; ++i) {
            sb.append(c);
        }
        return sb.toString();
    }

	private BAMSym getBAMSym(String nameType, BioSeq seq, String id, int start, int end, int width, String qualString, String filter, String ref, String alt) {
		Cigar cigar = null;
// Cigar cigar = TextCigarCodec.getSingleton().decode(cigarString);
		boolean equal = false;
		boolean equalLength = false;
		boolean insertion = false;
		boolean deletion = false;
		if (ref.equals(alt)) {
			equal = true;
		}
		else if (ref.length() == alt.length()) {
			cigar = new Cigar();
			CigarElement cigarElement = new CigarElement(width, CigarOperator.M);
			cigar.add(cigarElement);
			equalLength = true;
		}
		else if (ref.length() == 1) {
			cigar = new Cigar();
			CigarElement cigarElement = new CigarElement(1, CigarOperator.M);
			cigar.add(cigarElement);
			cigarElement = new CigarElement(alt.length() - 1, CigarOperator.I);
			cigar.add(cigarElement);
			insertion = true;
		}
		else if (alt.length() == 1) {
			cigar = new Cigar();
			CigarElement cigarElement = new CigarElement(1, CigarOperator.M);
			cigar.add(cigarElement);
			cigarElement = new CigarElement(ref.length() - 1, CigarOperator.D);
			cigar.add(cigarElement);
			deletion = true;
		}
		int[] iblockMins = insertion ? new int[]{start + 1} : new int[]{};
		int[] iblockMaxs = insertion ? new int[]{start + alt.length()} : new int[]{};
		String residuesStr = "";
		if (equal || equalLength) {
			residuesStr = alt;
		}
		else if (insertion) {
			residuesStr = ref;
		}
		else if (deletion) {
			String repeated = getMultiple('_', ref.length() - 1);
			residuesStr = alt + repeated;
		}
		BAMSym residueSym = new BAMSym(nameType, seq, start, end, id, 0.0f,true, 0, 0,new int[]{start}, new int[]{end}, iblockMins, iblockMaxs, cigar, residuesStr);
		if (cigar != null ) {
			residueSym.setProperty(BAM.CIGARPROP, cigar);
		}
		residueSym.setInsResidues(insertion ? alt.substring(1) : "");
		residueSym.setProperty(BAM.SHOWMASK, true);
		residueSym.setProperty("type", nameType);
		residueSym.setProperty("seq", seq.getID());
		residueSym.setProperty("pos", start);
		residueSym.setProperty("id", id);
		residueSym.setProperty("ref", ref);
		residueSym.setProperty("alt", alt);
		if (!NO_DATA.equals(qualString)) {
			residueSym.setProperty("qual", Float.parseFloat(qualString));
		}
		if (!"PASS".equals(filter) && filterMap.get(filter) != null) {
			filter += " - " + filterMap.get(filter).getDescription();
		}
		residueSym.setProperty("filter", filter);
		return residueSym;
	}

/*
    private BAMSym getBAMSym(final String nameType, final BioSeq seq, final String id, final int start, final int end, final int width, final String qualString, String filter, final String ref, final String alt) {
        Cigar cigar = null;
        boolean equal = false;
        boolean equalLength = false;
        boolean insertion = false;
        boolean deletion = false;
        if (ref.equals(alt)) {
            equal = true;
        }
        else if (ref.length() == alt.length()) {
            cigar = new Cigar();
            final CigarElement cigarElement = new CigarElement(width, CigarOperator.M);
            cigar.add(cigarElement);
            equalLength = true;
        }
        else if (ref.length() == 1) {
            cigar = new Cigar();
            CigarElement cigarElement = new CigarElement(1, CigarOperator.M);
            cigar.add(cigarElement);
            cigarElement = new CigarElement(alt.length() - 1, CigarOperator.I);
            cigar.add(cigarElement);
            insertion = true;
        }
        else if (alt.length() == 1) {
            cigar = new Cigar();
            CigarElement cigarElement = new CigarElement(1, CigarOperator.M);
            cigar.add(cigarElement);
            cigarElement = new CigarElement(ref.length() - 1, CigarOperator.D);
            cigar.add(cigarElement);
            deletion = true;
        }
        final int[] array;
        if (insertion) {
            array = new int[] { start + 1 };
        }
        else {
            final int[] array2 = new int[0];
        }
        final int[] iblockMins = array;
        final int[] array3;
        if (insertion) {
            array3 = new int[] { start + alt.length() };
        }
        else {
            final int[] array4 = new int[0];
        }
        final int[] iblockMaxs = array3;
        String residuesStr = "";
        if (equal || equalLength) {
            residuesStr = alt;
        }
        else if (insertion) {
            residuesStr = ref;
        }
        else if (deletion) {
            final String repeated = this.getMultiple('_', ref.length() - 1);
            residuesStr = alt + repeated;
        }
        final BAMSym residueSym = new BAMSym(nameType, seq, start, end, id, 0.0f, true, 0, 0, new int[] { start }, new int[] { end }, iblockMins, iblockMaxs, cigar, residuesStr);
        if (cigar != null) {
            residueSym.setProperty("cigar", cigar);
        }
        residueSym.setInsResidues(insertion ? alt.substring(1) : "");
        residueSym.setProperty("showMask", true);
        residueSym.setProperty("type", nameType);
        residueSym.setProperty("seq", seq.getID());
        residueSym.setProperty("pos", start);
        residueSym.setProperty("id", id);
        residueSym.setProperty("ref", ref);
        residueSym.setProperty("alt", alt);
        if (!".".equals(qualString)) {
            residueSym.setProperty("qual", Float.parseFloat(qualString));
        }
        if (!"PASS".equals(filter) && this.filterMap.get(filter) != null) {
            filter = filter + " - " + this.filterMap.get(filter).getDescription();
        }
        residueSym.setProperty("filter", filter);
        return residueSym;
    }
*/
    private void processInfo(final String key, final String valuesString, final BioSeq seq, final String nameType, final int start, final int end, final int width, final Map<String, SimpleSymWithProps> dataMap, final Map<String, GraphData> graphDataMap) {
        final String[] values = valuesString.split(",");
        if (this.infoMap.get(key).getType() == Type.Integer || this.infoMap.get(key).getType() == Type.Float) {
            for (final String value : values) {
                this.addGraphData(graphDataMap, nameType + "/" + key, seq, start, width, Float.parseFloat(value));
            }
        }
        else {
            final SimpleSymWithProps container = this.getContainerSymFromMap(dataMap, nameType + "/" + key, seq);
            for (final String value2 : values) {
                final SimpleSymWithProps sym = new SimpleSymWithProps();
                sym.addSpan(new SimpleSeqSpan(start, end, (BioSeq)container.getProperty("seq")));
                sym.setProperty(key, value2);
                container.addChild(sym);
            }
        }
    }

    private void processSamples(final BioSeq seq, final String nameType, final int start, final int end, final int width, final String[] fields, final Map<String, SimpleSymWithProps> genotypeDataMap, final Map<String, GraphData> graphDataMap, final boolean combineGenotype, final BAMSym refSym, final BAMSym[] altSyms, final int line_count) {
        if (this.samples.length > 0) {
            if (fields.length < this.samples.length + 9) {
                Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "vcf line " + line_count + " has " + (fields.length - 9) + " genotype records, but header has " + this.samples.length);
            }
            else if (fields.length > this.samples.length + 9) {
                throw new IllegalStateException("vcf format error, line " + line_count + " has " + (fields.length - 9) + " genotype records, but header has " + this.samples.length);
            }
        }
        if (fields.length > 8) {
            final String[] format = fields[8].split(":");
            if (!"GT".equals(format[0])) {
                throw new IllegalStateException("vcf format error, line " + line_count + " first genotype field must be \"GT\"");
            }
            for (int j = 9; j < fields.length; ++j) {
                String sample;
                if (j - 9 >= this.samples.length || this.samples[j - 9].trim().length() == 0) {
                    sample = "sample #" + (j - 8);
                }
                else {
                    sample = this.samples[j - 9];
                }
                final String[] data = fields[j].split(":");
                if (format.length < data.length) {
                    throw new IllegalStateException("vcf format error, line " + line_count + " has " + data.length + "genotype fields, but definition has " + format.length);
                }
                for (int k = 0; k < format.length; ++k) {
                    final String type = format[k];
                    final String fullKey = nameType + "/" + type + "/" + sample;
                    final String dataKey = nameType + "/" + type + (combineGenotype ? "" : ("/" + sample));
                    if (k < data.length && this.selectedFields.contains(fullKey) && !".".equals(data[k])) {
                        if (this.formatMap.get(format[k]) != null && (this.formatMap.get(format[k]).getType() == Type.Integer || this.formatMap.get(format[k]).getType() == Type.Float)) {
                            for (final String datum : data[k].split(",")) {
                                if (!".".equals(datum)) {
                                    this.addGraphData(graphDataMap, fullKey, seq, start, width, Float.parseFloat(datum));
                                }
                            }
                        }
                        else if ("GT".equals(format[k])) {
                            final String[] genotypes = data[k].split("[|/]");
                            for (int l = 0; l < genotypes.length; ++l) {
                                if (!".".equals(genotypes[l])) {
                                    final SimpleSymWithProps container = this.getContainerSymFromMap(genotypeDataMap, dataKey, seq);
                                    final int index = Integer.parseInt(genotypes[l]);
                                    if (index == 0) {
                                        container.addChild(refSym);
                                    }
                                    else {
                                        container.addChild(altSyms[index - 1]);
                                    }
                                }
                            }
                        }
                        else {
                            final SimpleSymWithProps container2 = this.getContainerSymFromMap(genotypeDataMap, dataKey, seq);
                            for (final String datum2 : data[k].split(",")) {
                                if (!".".equals(datum2)) {
                                    final SimpleSymWithProps sym = new SimpleSymWithProps();
                                    sym.addSpan(new SimpleSeqSpan(start, end, (BioSeq)container2.getProperty("seq")));
                                    container2.addChild(sym);
                                    sym.setProperty(format[k], datum2);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void processDataLine(final SimpleSymWithProps mainSym, final BioSeq seq, final int min, final int max, final String nameType, final Map<String, SimpleSymWithProps> dataMap, final Map<String, GraphData> graphDataMap, final Map<String, SimpleSymWithProps> genotypeDataMap, final String line, final int line_count, final boolean combineGenotype) {
        final String[] fields = VCF.line_regex.split(line);
        final int start = Integer.parseInt(fields[1]) - 1;
        if (max <= start) {
            return;
        }
        final String ref = fields[3];
        final int width = ref.length();
        final int end = start + width;
        if (min >= end) {
            return;
        }
        final String id = fields[2];
        final String[] alts = fields[4].split(",");
        final String qualString = fields[5];
        final String filter = fields[6];
        if (!".".equals(qualString) && this.selectedFields.contains("qual")) {
            this.addGraphData(graphDataMap, nameType + "/qual", seq, start, width, Float.parseFloat(qualString));
        }
        final BAMSym[] altSyms = new BAMSym[alts.length];
        for (int i = 0; i < alts.length; ++i) {
            final String alt = alts[i];
            mainSym.addChild(altSyms[i] = this.getBAMSym(nameType, seq, id, start, end, width, qualString, filter, ref, alt));
            final String[] arr$;
            final String[] info_fields = arr$ = VCF.info_regex.split(fields[7]);
            for (final String info_field : arr$) {
                final String[] prop_fields = info_field.split("=");
                final String key = prop_fields[0];
                final String valuesString = (prop_fields.length == 1) ? "true" : prop_fields[1];
                String fullKey = key;
                if (this.infoMap.get(key) != null && this.infoMap.get(key).getDescription() != null) {
                    fullKey = fullKey + " - " + this.infoMap.get(key).getDescription();
                }
                altSyms[i].setProperty(fullKey, valuesString);
                if (this.selectedFields.contains(key)) {
                    this.processInfo(key, valuesString, seq, nameType, start, end, width, dataMap, graphDataMap);
                }
            }
        }
        final BAMSym refSym = this.getBAMSym(nameType, seq, id, start, end, width, qualString, filter, ref, ref);
        this.processSamples(seq, nameType, start, end, width, fields, genotypeDataMap, graphDataMap, combineGenotype, refSym, altSyms, line_count);
    }

    private void addGraphData(final Map<String, GraphData> graphDataMap, final String key, final BioSeq seq, final int pos, final int width, final float value) {
        GraphData graphData = graphDataMap.get(key);
        if (graphData == null) {
            graphData = new GraphData();
            graphDataMap.put(key, graphData);
        }
        graphData.xData.add(pos);
        graphData.yData.add(value);
        graphData.wData.add(width);
    }

    @Override
    public SeqSpan getSpan(final String line) {
        final String[] fields = VCF.line_regex.split(line);
        final String seq_name = fields[0];
        final int start = Integer.parseInt(fields[1]) - 1;
        final String ref = fields[3];
        final int width = ref.length();
        final int end = start + width;
        BioSeq seq = GenometryModel.getGenometryModel().getSelectedSeqGroup().getSeq(seq_name);
        if (seq == null) {
            seq = new BioSeq(seq_name, "", 0);
        }
        return new SimpleSeqSpan(start, end, seq);
    }

    @Override
    public boolean processInfoLine(final String line, final List<String> infoLines) {
        if (line.startsWith("##")) {
            this.processMetaInformationLine(line.substring(2));
            return true;
        }
        if (!line.startsWith("#")) {
            return false;
        }
        if (this.version < 0.0) {
            ErrorHandler.errorPanel("version error", "file version not supported or not found for " + this.uri, Level.SEVERE);
            throw new UnsupportedOperationException("file version not supported or not found");
        }
        this.processHeaderLine(line.substring(1));
        return true;
    }

    @Override
    protected LineProcessor createLineProcessor(final String featureName) {
        return this;
    }

    static {
        EXTENSIONS = new String[] { "vcf" };
        line_regex = Pattern.compile("\\s+");
        info_regex = Pattern.compile(";");
        final Set<String> types = new HashSet<String>();
        types.add("protein");
        idPattern = Pattern.compile(",ID=\\w+,");
        numberPattern = Pattern.compile(",Number=\\w+,");
        typePattern = Pattern.compile(",Type=\\w+,");
        descriptionPattern = Pattern.compile(",Description=\\\"[^\\\"]+\\\",");
    }

    private enum Type
    {
        Integer,
        String,
        Float,
        Flag;
    }

    private class INFO
    {
        private final String ID;
        private final Type type;
        private final String description;

        public INFO(final String ID, final int number, final Type type, final String description, final boolean onePerAllele, final boolean onePerGenotype) {
            this.ID = ID;
            this.type = type;
            this.description = description;
        }

        public String getID() {
            return this.ID;
        }

        public Type getType() {
            return this.type;
        }

        public String getDescription() {
            return this.description;
        }
    }

    private class FILTER
    {
        private final String ID;
        private final String description;

        public FILTER(final String ID, final String description) {
            this.ID = ID;
            this.description = description;
        }

        public String getID() {
            return this.ID;
        }

        public String getDescription() {
            return this.description;
        }
    }

    private class FORMAT
    {
        private final String ID;
        private final Type type;

        public FORMAT(final String ID, final int number, final Type type, final String description) {
            this.ID = ID;
            this.type = type;
        }

        public String getID() {
            return this.ID;
        }

        public Type getType() {
            return this.type;
        }
    }

    private class GraphData
    {
        IntArrayList xData;
        FloatArrayList yData;
        IntArrayList wData;

        private GraphData() {
            this.xData = new IntArrayList();
            this.yData = new FloatArrayList();
            this.wData = new IntArrayList();
        }
    }
}
