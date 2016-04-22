//
// Decompiled by Procyon v0.5.30
//

package com.affymetrix.genometryImpl.parsers;

import java.io.InputStream;
import com.affymetrix.genometryImpl.span.SimpleSeqSpan;
import com.affymetrix.genometryImpl.MutableSeqSpan;
import com.affymetrix.genometryImpl.span.SimpleMutableSeqSpan;
import java.io.PrintWriter;
import java.io.Writer;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.OutputStream;
import java.util.Collection;
import com.affymetrix.genometryImpl.symmetry.SymWithProps;
import java.util.Iterator;
import com.affymetrix.genometryImpl.BioSeq;
import com.affymetrix.genometryImpl.das2.SimpleDas2Feature;
import com.affymetrix.genometryImpl.util.GeneralUtils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import java.io.IOException;
import org.xml.sax.XMLReader;
import org.xml.sax.ErrorHandler;
import org.xml.sax.ContentHandler;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.InputSource;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import com.affymetrix.genometryImpl.symmetry.MutableSeqSymmetry;
import com.affymetrix.genometryImpl.symmetry.SeqSymmetry;
import java.util.Map;
import com.affymetrix.genometryImpl.SeqSpan;
import java.util.List;
import java.net.URI;
import java.util.Stack;
import com.affymetrix.genometryImpl.AnnotatedSeqGroup;
import java.util.regex.Pattern;
import org.xml.sax.helpers.DefaultHandler;

public final class Das2FeatureSaxParser extends DefaultHandler implements AnnotationWriter, Parser
{
    public static final boolean DO_SEQID_HACK = true;
    private static final boolean DEBUG = false;
    private static final boolean REPORT_RESULTS = false;
    private static final boolean REPORT_MULTI_LOC = true;
    private static final boolean REQUIRE_DAS2_NAMESPACE = false;
    private static final boolean ADD_NEW_SEQS_TO_GROUP = false;
    public static final String FEATURES_CONTENT_TYPE = "application/x-das-features+xml";
    public static final String FEATURES_CONTENT_SUBTYPE = "x-das-features+xml";
    public static final String DAS2_NAMESPACE = "http://biodas.org/documents/das2";
    private static final String FEATURELIST = "FEATURELIST";
    private static final String FEATURES = "FEATURES";
    private static final String FEATURE = "FEATURE";
    private static final String LOC = "LOC";
    private static final String XID = "XID";
    private static final String PART = "PART";
    private static final String PARENT = "PARENT";
    private static final String PROP = "PROP";
    private static final String XMLBASE = "xml:base";
    public static final String ID = "id";
    public static final String URID = "uri";
    private static final String TYPE = "type";
    private static final String TYPEID = "type_id";
    public static final String TYPEURI = "type_uri";
    public static final String NAME = "name";
    public static final String TITLE = "title";
    private static final String CREATED = "created";
    private static final String MODIFIED = "modified";
    private static final String DOC_HREF = "doc_href";
    private static final String RANGE = "range";
    private static final String CIGAR = "gap";
    private static final String KEY = "key";
    private static final String VALUE = "value";
    public static final String SEGMENT = "segment";
    static final Pattern range_splitter;
    static final Pattern interval_splitter;
    private AnnotatedSeqGroup seqgroup;
    private boolean add_annots_to_seq;
    private static final boolean add_to_sym_hash = true;
    private String current_elem;
    private final Stack<String> elemstack;
    private final Stack<URI> base_uri_stack;
    private URI current_base_uri;
    private String feat_id;
    private String feat_type;
    private String feat_name;
    private String feat_parent_id;
    private String feat_created;
    private String feat_modified;
    private String feat_doc_href;
    private String feat_prop_key;
    private String feat_prop_val;
    private final List<SeqSpan> feat_locs;
    private Map<String, Object> feat_parts;
    private Map<String, Object> feat_props;
    private List<SeqSymmetry> result_syms;
    private final Map<String, MutableSeqSymmetry> id2sym;
    private final Map<SeqSymmetry, Map<String, Object>> parent2parts;
    private int dup_count;
    private int feature_constructor_calls;

    public Das2FeatureSaxParser() {
        this.seqgroup = null;
        this.add_annots_to_seq = false;
        this.current_elem = null;
        this.elemstack = new Stack<String>();
        this.base_uri_stack = new Stack<URI>();
        this.current_base_uri = null;
        this.feat_id = null;
        this.feat_type = null;
        this.feat_name = null;
        this.feat_parent_id = null;
        this.feat_created = null;
        this.feat_modified = null;
        this.feat_doc_href = null;
        this.feat_prop_key = null;
        this.feat_prop_val = null;
        this.feat_locs = new ArrayList<SeqSpan>();
        this.feat_parts = new LinkedHashMap<String, Object>();
        this.feat_props = null;
        this.result_syms = null;
        this.id2sym = new HashMap<String, MutableSeqSymmetry>();
        this.parent2parts = new HashMap<SeqSymmetry, Map<String, Object>>();
        this.dup_count = 0;
        this.feature_constructor_calls = 0;
    }

    public void setBaseURI(final URI base) {
        this.current_base_uri = base;
    }

    public URI getBaseURI() {
        return this.current_base_uri;
    }

    public List<SeqSymmetry> parse(final InputSource isrc, final String uri, final AnnotatedSeqGroup group, final boolean annot_seq) throws IOException, SAXException {
        this.clearAll();
        try {
            final URI source_uri = new URI(uri);
            System.out.println("parsing XML doc, original URI = " + source_uri);
            this.current_base_uri = source_uri.resolve("");
            System.out.println("  initial base uri: " + this.current_base_uri);
            this.base_uri_stack.push(this.current_base_uri);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        this.add_annots_to_seq = annot_seq;
        this.result_syms = new ArrayList<SeqSymmetry>();
        this.seqgroup = group;
        XMLReader reader = null;
        try {
            final SAXParserFactory f = SAXParserFactory.newInstance();
            f.setNamespaceAware(true);
            reader = f.newSAXParser().getXMLReader();
            reader.setFeature("http://xml.org/sax/features/validation", false);
            reader.setFeature("http://apache.org/xml/features/validation/dynamic", false);
            reader.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        try {
            reader.setContentHandler(this);
            reader.setErrorHandler(this);
            reader.parse(isrc);
        }
        catch (Exception ex2) {
            ex2.printStackTrace();
        }
        System.out.println("finished parsing das2xml feature doc, number of top-level features: " + this.result_syms.size());
        if (this.dup_count > 0) {
            System.out.println("Warning: found " + this.dup_count + " duplicate feature ID" + ((this.dup_count > 1) ? "s" : ""));
        }
        System.out.println("feature constructor calls: " + this.feature_constructor_calls);
        return this.result_syms;
    }

    @Override
    public void startDocument() {
        System.out.println("Das2FeaturesSaxParser.startDocument() called");
    }

    @Override
    public void endDocument() {
    }

    @Override
    public void startElement(final String uri, final String localName, final String qname, final Attributes atts) throws SAXException {
        this.elemstack.push(this.current_elem);
        this.current_elem = localName.intern();
        final String xml_base = atts.getValue("xml:base");
        if (xml_base != null) {
            this.current_base_uri = this.current_base_uri.resolve(xml_base);
            System.out.println("resolved new base uri: " + this.current_base_uri);
        }
        this.base_uri_stack.push(this.current_base_uri);
        if (this.current_elem.equals("FEATURELIST") || this.current_elem.equals("FEATURES") || this.current_elem.equals("XID")) {
            return;
        }
        if (this.current_elem.equals("FEATURE")) {
            this.parseFeature(atts);
        }
        else if (this.current_elem.equals("LOC")) {
            this.parseLoc(atts);
        }
        else if (this.current_elem.equals("PARENT")) {
            this.parseParent(atts);
        }
        else if (this.current_elem.equals("PART")) {
            this.parsePart(atts);
        }
        else if (this.current_elem.equals("PROP")) {
            this.feat_prop_key = atts.getValue("key");
            this.feat_prop_val = atts.getValue("value");
        }
        else {
            System.out.println("element not recognized, but within DAS2 namespace: " + this.current_elem);
        }
    }

    private void parseFeature(final Attributes atts) throws SAXException {
        String feat_id_att = atts.getValue("uri");
        if (feat_id_att == null) {
            feat_id_att = atts.getValue("id");
        }
        try {
            this.feat_id = GeneralUtils.URLDecode(this.current_base_uri.resolve(feat_id_att).toString());
        }
        catch (IllegalArgumentException ioe) {
            throw new SAXException("Feature id uses illegal characters: '" + feat_id_att + "'");
        }
        String feat_type_att = atts.getValue("type");
        if (feat_type_att == null) {
            feat_type_att = atts.getValue("type_id");
        }
        if (feat_type_att == null) {
            feat_type_att = atts.getValue("type_uri");
        }
        try {
            this.feat_type = GeneralUtils.URLDecode(this.current_base_uri.resolve(feat_type_att).toString());
        }
        catch (IllegalArgumentException ioe2) {
            throw new SAXException("Feature type uses illegal characters: '" + feat_type_att + "'");
        }
        this.feat_name = atts.getValue("title");
        if (this.feat_name == null) {
            this.feat_name = atts.getValue("name");
        }
        this.feat_created = atts.getValue("created");
        this.feat_modified = atts.getValue("modified");
        this.feat_doc_href = atts.getValue("doc_href");
    }

    private void parseLoc(final Attributes atts) throws SAXException {
        String seqid_att = atts.getValue("segment");
        if (seqid_att == null) {
            seqid_att = atts.getValue("uri");
        }
        if (seqid_att == null) {
            seqid_att = atts.getValue("id");
        }
        String seqid;
        try {
            seqid = GeneralUtils.URLDecode(this.current_base_uri.resolve(seqid_att).toString());
        }
        catch (IllegalArgumentException ioe) {
            throw new SAXException("Segment id uses illegal characters: '" + seqid_att + "'");
        }
        final String range = atts.getValue("range");
        atts.getValue("gap");
        seqid = doSeqIdHack(seqid);
        final SeqSpan span = getLocationSpan(seqid, range, this.seqgroup);
        this.feat_locs.add(span);
    }

    private void parseParent(final Attributes atts) throws SAXException {
        if (this.feat_parent_id == null) {
            this.feat_parent_id = atts.getValue("uri");
            if (this.feat_parent_id == null) {
                this.feat_parent_id = atts.getValue("id");
            }
            try {
                this.feat_parent_id = GeneralUtils.URLDecode(this.current_base_uri.resolve(this.feat_parent_id).toString());
                return;
            }
            catch (IllegalArgumentException ioe) {
                throw new SAXException("Parent id uses illegal characters: '" + this.feat_parent_id + "'");
            }
        }
        System.out.println("WARNING:  multiple parents for feature, just using first one");
    }

    private void parsePart(final Attributes atts) throws SAXException {
        String part_id = atts.getValue("uri");
        if (part_id == null) {
            part_id = atts.getValue("id");
        }
        try {
            part_id = GeneralUtils.URLDecode(this.current_base_uri.resolve(part_id).toString());
        }
        catch (IllegalArgumentException ioe) {
            throw new SAXException("Part id uses illegal characters: '" + part_id + "'");
        }
        final SeqSymmetry child_sym = this.id2sym.get(part_id);
        if (child_sym == null) {
            this.feat_parts.put(part_id, part_id);
        }
        else {
            this.feat_parts.put(part_id, child_sym);
        }
    }

    public void clearAll() {
        this.feature_constructor_calls = 0;
        this.result_syms = null;
        this.id2sym.clear();
        this.base_uri_stack.clear();
        this.current_base_uri = null;
        this.clearFeature();
    }

    public void clearFeature() {
        this.feat_id = null;
        this.feat_type = null;
        this.feat_name = null;
        this.feat_parent_id = null;
        this.feat_created = null;
        this.feat_modified = null;
        this.feat_doc_href = null;
        this.feat_locs.clear();
        this.feat_parts = new LinkedHashMap<String, Object>();
        this.feat_props = null;
        this.feat_prop_key = null;
        this.feat_prop_val = null;
    }

    @Override
    public void endElement(final String uri, final String name, final String qname) {
        if (name.equals("FEATURE")) {
            this.addFeature();
            this.clearFeature();
        }
        else if (name.equals("PROP")) {
            if (this.feat_props == null) {
                this.feat_props = new HashMap<String, Object>();
            }
            final Object prev = this.feat_props.get(this.feat_prop_key);
            if (prev == null) {
                this.feat_props.put(this.feat_prop_key, this.feat_prop_val);
            }
            else if (prev instanceof List) {
                ((List)prev).add(this.feat_prop_val);
            }
            else {
                final List multivals = new ArrayList();
                multivals.add(prev);
                multivals.add(this.feat_prop_val);
                this.feat_props.put(this.feat_prop_key, multivals);
            }
            this.feat_prop_key = null;
            this.feat_prop_val = null;
            this.current_elem = this.elemstack.pop();
        }
        this.current_base_uri = this.base_uri_stack.pop();
    }

    @Override
    public void characters(final char[] ch, final int start, final int length) {
    }

    public void addFeature() {
        if (this.id2sym.get(this.feat_id) != null) {
            ++this.dup_count;
            return;
        }
        final SimpleDas2Feature featsym = new SimpleDas2Feature(this.feat_id, this.feat_type, this.feat_name, this.feat_parent_id, this.feat_created, this.feat_modified, this.feat_doc_href, this.feat_props);
        ++this.feature_constructor_calls;
        this.id2sym.put(this.feat_id, featsym);
        this.parent2parts.put(featsym, this.feat_parts);
        final int loc_count = this.feat_locs.size();
        for (int i = 0; i < loc_count; ++i) {
            final SeqSpan span = this.feat_locs.get(i);
            featsym.addSpan(span);
        }
        if (this.feat_parts.size() > 0 && this.childrenReady(featsym)) {
            this.addChildren(featsym);
        }
        if (this.feat_parent_id == null) {
            if (loc_count > 2) {
                System.out.println("loc count: " + loc_count);
            }
            for (int i = 0; i < loc_count; ++i) {
                final SeqSpan span = this.feat_locs.get(i);
                final BioSeq seq = span.getBioSeq();
                final BioSeq aseq = this.seqgroup.getSeq(seq.getID());
                if (aseq != null && seq == aseq) {
                    this.result_syms.add(featsym);
                    this.seqgroup.addToIndex(featsym.getID(), featsym);
                    if (featsym.getName() != null) {
                        this.seqgroup.addToIndex(featsym.getName(), featsym);
                    }
                    if (this.add_annots_to_seq) {
                        aseq.addAnnotation(featsym);
                    }
                }
            }
        }
        else {
            final MutableSeqSymmetry parent = this.id2sym.get(this.feat_parent_id);
            if (parent != null) {
                final Map<String, Object> parent_parts = this.parent2parts.get(parent);
                if (parent_parts == null) {
                    System.out.println("WARNING: no parent_parts found for parent, id=" + this.feat_parent_id);
                }
                else {
                    parent_parts.put(this.feat_id, featsym);
                    if (this.childrenReady(parent)) {
                        this.addChildren(parent);
                    }
                }
            }
        }
    }

    private boolean childrenReady(final SeqSymmetry parent_sym) {
        final Map<String, Object> parts = this.parent2parts.get(parent_sym);
        final Iterator<Object> citer = parts.values().iterator();
        boolean all_child_syms = true;
        while (citer.hasNext()) {
            final Object val = citer.next();
            if (!(val instanceof SeqSymmetry)) {
                all_child_syms = false;
                break;
            }
        }
        return all_child_syms;
    }

    private void addChildren(final MutableSeqSymmetry parent_sym) {
        final Map<String, Object> parts = this.parent2parts.get(parent_sym);
        for (final Map.Entry<String, Object> keyval : parts.entrySet()) {
            keyval.getKey();
            final SeqSymmetry child_sym = (SeqSymmetry) keyval.getValue();
            if (child_sym instanceof SymWithProps) {
                final String child_type = (String)((SymWithProps)child_sym).getProperty("type");
                if (child_type != null && child_type.endsWith("SO:intron")) {
                    continue;
                }
            }
            parent_sym.addChild(child_sym);
        }
        this.parent2parts.remove(parent_sym);
    }

    @Override
    public String getMimeType() {
        return "application/x-das-features+xml";
    }

    @Override
    public boolean writeAnnotations(final Collection<? extends SeqSymmetry> syms, final BioSeq seq, final String type, final OutputStream outstream) {
        boolean success = true;
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(outstream)));
            pw.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            pw.println("<FEATURES ");
            pw.println("    xmlns=\"http://biodas.org/documents/das2\"");
            pw.println("    xmlns:xlink=\"http://www.w3.org/1999/xlink\" ");
            if (this.getBaseURI() != null) {
                String genome_id = "";
                if (seq != null) {
                    genome_id = seq.getSeqGroup().getID();
                }
                final String xbase = this.getBaseURI().toString() + genome_id + "/";
                pw.println("   xml:base=\"" + xbase + "\" >");
            }
            else {
                pw.println(" >");
            }
            final MutableSeqSpan mspan = new SimpleMutableSeqSpan();
            for (final SeqSymmetry annot : syms) {
                this.writeDasFeature(annot, null, 0, type, pw, mspan);
            }
            pw.println("</FEATURES>");
            pw.flush();
        }
        catch (Exception ex) {
            ex.printStackTrace();
            success = false;
        }
        finally {
            GeneralUtils.safeClose(pw);
        }
        return success;
    }

    public void writeDasFeature(final SeqSymmetry annot, final String parent_id, final int parent_index, String feat_type, final PrintWriter pw, final MutableSeqSpan mspan) {
        if (feat_type == null) {
            feat_type = BioSeq.determineMethod(annot);
        }
        final String feat_id = getChildID(annot, parent_id, parent_index);
        String feat_title = null;
        if (annot instanceof SymWithProps) {
            final SymWithProps swp = (SymWithProps)annot;
            feat_title = (String)swp.getProperty("name");
            if (feat_title == null) {
                feat_title = (String)swp.getProperty("title");
            }
            if (feat_title == null) {
                feat_title = (String)swp.getProperty("gene_name");
            }
        }
        if (feat_title == null && annot.getChildCount() > 0) {
            feat_title = feat_id;
        }
        pw.print("  <FEATURE uri=\"");
        pw.print(GeneralUtils.URLEncode(feat_id));
        if (feat_title != null) {
            pw.print("\" title=\"");
            pw.print(feat_title);
        }
        pw.print("\" type=\"");
        pw.print(GeneralUtils.URLEncode(feat_type));
        pw.print("\" >");
        pw.println();
        for (int scount = annot.getSpanCount(), i = 0; i < scount; ++i) {
            SeqSpan span = null;
            if (mspan == null) {
                span = annot.getSpan(i);
            }
            else {
                annot.getSpan(i, mspan);
                span = mspan;
            }
            pw.print("     <LOC segment=\"");
            pw.print(GeneralUtils.URLEncode(span.getBioSeq().getID()));
            pw.print("\" range=\"");
            final String range = getRangeString(span);
            pw.print(range);
            pw.print("\" />");
            pw.println();
        }
        if (parent_id != null) {
            pw.print("     <PARENT ");
            pw.print("uri");
            pw.print("=\"");
            pw.print(GeneralUtils.URLEncode(parent_id));
            pw.print("\" />");
            pw.println();
        }
        final int child_count = annot.getChildCount();
        if (child_count > 0) {
            for (int j = 0; j < child_count; ++j) {
                final SeqSymmetry child = annot.getChild(j);
                final String child_id = getChildID(child, feat_id, j);
                pw.print("     <PART ");
                pw.print("uri");
                pw.print("=\"");
                pw.print(GeneralUtils.URLEncode(child_id));
                pw.print("\" />");
                pw.println();
            }
        }
        pw.println("  </FEATURE>");
        if (child_count > 0) {
            for (int j = 0; j < child_count; ++j) {
                final SeqSymmetry child = annot.getChild(j);
                this.writeDasFeature(child, feat_id, j, feat_type, pw, mspan);
            }
        }
    }

    protected static String getChildID(final SeqSymmetry child, final String parent_id, final int child_index) {
        String feat_id = child.getID();
        if (feat_id == null) {
            if (child instanceof SymWithProps) {
                feat_id = (String)((SymWithProps)child).getProperty("id");
            }
            if (feat_id == null) {
                if (parent_id != null) {
                    feat_id = parent_id;
                }
                if (feat_id == null) {
                    return "unknown";
                }
            }
        }
        if (parent_id != null) {
            feat_id = feat_id + "." + Integer.toString(child_index);
        }
        return feat_id;
    }

    public static String doSeqIdHack(final String seqid) {
        String new_seqid = seqid;
        final int slash_index = new_seqid.lastIndexOf("/");
        if (slash_index >= 0) {
            new_seqid = new_seqid.substring(slash_index + 1);
        }
        return new_seqid;
    }

    private static SeqSpan getLocationSpan(final String seqid, final String rng, final AnnotatedSeqGroup group) {
        if (seqid == null || rng == null) {
            return null;
        }
        final String[] subfields = Das2FeatureSaxParser.interval_splitter.split(rng);
        final int min = Integer.parseInt(subfields[0]);
        final int max = Integer.parseInt(subfields[1]);
        boolean forward = true;
        if (subfields.length >= 3 && subfields[2].equals("-1")) {
            forward = false;
        }
        BioSeq seq;
        if (group == null) {
            seq = new BioSeq(seqid, "", max);
        }
        else {
            seq = group.getSeq(seqid);
            if (seq == null) {
                seq = new BioSeq(seqid, "", max);
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

    public static String getRangeString(final SeqSpan span) {
        return getRangeString(span, true);
    }

    public static String getRangeString(final SeqSpan span, final boolean indicate_strand) {
        if (span == null) {
            return null;
        }
        final StringBuilder buf = new StringBuilder(100);
        buf.append(Integer.toString(span.getMin()));
        buf.append(":");
        buf.append(Integer.toString(span.getMax()));
        if (indicate_strand) {
            if (span.isForward()) {
                buf.append(":1");
            }
            else {
                buf.append(":-1");
            }
        }
        return buf.toString();
    }

    @Override
    public List<? extends SeqSymmetry> parse(final InputStream is, final AnnotatedSeqGroup group, final String nameType, final String uri, final boolean annotate_seq) throws Exception {
        return this.parse(new InputSource(is), uri, group, false);
    }

    static {
        range_splitter = Pattern.compile("/");
        interval_splitter = Pattern.compile(":");
    }
}
