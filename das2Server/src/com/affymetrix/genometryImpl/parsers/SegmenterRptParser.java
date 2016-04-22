// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.parsers;

import java.util.Arrays;
import com.affymetrix.genometryImpl.symmetry.SymWithProps;
import com.affymetrix.genometryImpl.BioSeq;
import com.affymetrix.genometryImpl.symmetry.SeqSymmetry;
import com.affymetrix.genometryImpl.symmetry.SingletonSymWithProps;
import java.util.ArrayList;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import com.affymetrix.genometryImpl.AnnotatedSeqGroup;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public final class SegmenterRptParser implements Parser
{
    public static final String CN_REGION_FILE_EXT = "cn_segments";
    public static final String LOH_REGION_FILE_EXT = "loh_segments";
    private static final String HEADER_PROP_KEY_NAME = "SegmenterFileHeader";
    private int chromosome_col;
    private int start_col;
    private int end_col;
    private int length_col;
    private int strand_col;
    private int cn_change_col;
    private int file_col;
    private final boolean make_props;
    private final boolean use_length;
    private final boolean use_strand;
    private final boolean has_column_names_header_line;
    private static final Pattern line_splitter;
    private static final Pattern headerPattern;
    private final Map<String, String> headerMap;
    private static final List<String> integerColumnNames;
    
    public static boolean isCnRegionsFilename(final String s) {
        return s != null && s.toLowerCase().endsWith("." + "cn_segments".toLowerCase());
    }
    
    public static boolean isLohRegionsFilename(final String s) {
        return s != null && s.toLowerCase().endsWith("." + "loh_segments".toLowerCase());
    }
    
    public SegmenterRptParser() {
        this(true, true);
    }
    
    public SegmenterRptParser(final boolean props, final boolean addToIndex) {
        this.headerMap = new HashMap<String, String>();
        this.file_col = 0;
        this.chromosome_col = 3;
        this.start_col = 10;
        this.end_col = 11;
        this.cn_change_col = 2;
        this.length_col = -1;
        this.strand_col = -1;
        this.has_column_names_header_line = true;
        this.use_length = (this.length_col >= 0);
        this.use_strand = (this.strand_col >= 0);
        this.make_props = props;
    }
    
    void parseHeaderLine(final String line) {
        final Matcher m = SegmenterRptParser.headerPattern.matcher(line);
        if (m.matches()) {
            final String key = m.group(1);
            final String val = m.group(2);
            this.headerMap.put(key, val);
        }
    }
    
    public void parse(final InputStream istr, final String default_type, final AnnotatedSeqGroup seq_group) {
        List<String> col_names = null;
        try {
            final InputStreamReader asr = new InputStreamReader(istr);
            BufferedReader br;
            String line;
            for (br = new BufferedReader(asr), line = br.readLine(); line != null && (line.startsWith("#") || line.startsWith("[")) && !Thread.currentThread().isInterrupted(); line = br.readLine()) {
                this.parseHeaderLine(line);
            }
            if (line == null) {
                return;
            }
            if (this.has_column_names_header_line) {
                final String[] cols = SegmenterRptParser.line_splitter.split(line);
                col_names = new ArrayList<String>(cols.length);
                for (int i = 0; i < cols.length && !Thread.currentThread().isInterrupted(); ++i) {
                    col_names.add(cols[i]);
                }
            }
            for (line = br.readLine(); line != null && (line.startsWith("#") || line.startsWith("[")) && Thread.currentThread().isInterrupted(); line = br.readLine()) {}
            while (line != null && !Thread.currentThread().isInterrupted()) {
                final String[] cols = SegmenterRptParser.line_splitter.split(line);
                if (cols.length <= 0) {
                    continue;
                }
                final int start = Integer.parseInt(cols[this.start_col]);
                int end;
                if (this.use_length) {
                    final int length = Integer.parseInt(cols[this.length_col]);
                    if (this.use_strand) {
                        final String strand = cols[this.strand_col];
                        if (strand.equals("-")) {
                            end = start - length;
                        }
                        else {
                            end = start + length;
                        }
                    }
                    else {
                        end = start + length;
                    }
                }
                else {
                    end = Integer.parseInt(cols[this.end_col]);
                }
                String chromName = cols[this.chromosome_col];
                if (!chromName.startsWith("chr")) {
                    chromName = "chr" + chromName;
                }
                BioSeq seq = seq_group.getSeq(chromName);
                if (seq == null) {
                    seq = seq_group.addSeq(chromName, 0);
                }
                if (seq.getLength() < end) {
                    seq.setLength(end);
                }
                if (seq.getLength() < start) {
                    seq.setLength(start);
                }
                final String type = cols[this.file_col];
                if (type == null || type.trim().length() == 0) {}
                final String change_type = cols[this.cn_change_col];
                final SingletonSymWithProps sym = new SingletonSymWithProps(start, end, seq);
                sym.setProperty("method", default_type);
                final String id = change_type + " " + seq.getID() + ":" + start + "-" + end;
                sym.setProperty("id", id);
                if (this.make_props) {
                    for (int j = 0; j < cols.length && j < col_names.size() && !Thread.currentThread().isInterrupted(); ++j) {
                        final String name = col_names.get(j);
                        final String stringVal = cols[j];
                        if (SegmenterRptParser.integerColumnNames.contains(name)) {
                            try {
                                final Long intVal = Long.parseLong(stringVal);
                                sym.setProperty(name, intVal);
                            }
                            catch (Exception e) {
                                sym.setProperty(name, stringVal);
                            }
                        }
                        else {
                            sym.setProperty(name, stringVal);
                        }
                    }
                }
                sym.setProperty("SegmenterFileHeader", this.headerMap);
                seq.addAnnotation(sym);
                seq_group.addToIndex(id, sym);
                line = br.readLine();
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public static String getHeaderValue(final String key, final SymWithProps sym) {
        final Map<String, String> map = (Map<String, String>)sym.getProperty("SegmenterFileHeader");
        if (map != null) {
            return map.get(key);
        }
        return null;
    }
    
    @Override
    public List<? extends SeqSymmetry> parse(final InputStream is, final AnnotatedSeqGroup group, final String nameType, final String uri, final boolean annotate_seq) throws Exception {
        this.parse(is, uri, group);
        return null;
    }
    
    static {
        line_splitter = Pattern.compile("\t");
        headerPattern = Pattern.compile("^#(.*)=(.*)$");
        integerColumnNames = Arrays.asList("Copy Number", "Size(kb)", "#Markers", "Avg_DistBetweenMarkers(kb)", "Start_Linear_Pos", "End_Linear_Position");
    }
}
