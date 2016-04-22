//
// Decompiled by Procyon v0.5.30
//

package com.affymetrix.genometryImpl.parsers;

import java.util.Iterator;
import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.HashMap;
import java.util.Map;
import org.jdom.Document;
import org.jdom.input.SAXBuilder;
import java.util.List;
import java.io.InputStream;
import org.jdom.Element;
import org.jdom.*;

public abstract class AnnotsXmlParser
{
    static Element root;
    static final String folder = "folder";
    static final String file = "file";

    public static final void parseAnnotsXml(final InputStream istr, final List<AnnotMapElt> annotList) {
        try {
            final SAXBuilder docBuilder = new SAXBuilder();
            final Document doc = docBuilder.build(istr);
            AnnotsXmlParser.root = doc.getRootElement();
            final List children = AnnotsXmlParser.root.getChildren();
            if (AnnotsXmlParser.root.getChild("folder") != null) {
                iterateAllNodesNew(children, annotList);
            }
            else {
                iterateAllNodesOld(children, annotList);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void iterateAllNodesNew(final List list, final List<AnnotMapElt> annotList) {
        for (int i = 0; i < list.size(); ++i) {
            final Element e = (Element) list.get(i);
            if (e.getName().equalsIgnoreCase("folder")) {
                final List children = e.getChildren();
                if (children != null) {
                    iterateAllNodesNew(children, annotList);
                }
            }
            else if (e.getName().equalsIgnoreCase("file")) {
                final String path = getPath(e);
                final String title = path + e.getAttributeValue("title");
                addDataToList(annotList, e, title);
            }
        }
    }

    static void iterateAllNodesOld(final List list, final List<AnnotMapElt> annotList) {
        for (int i = 0; i < list.size(); ++i) {
            final Element e = (Element) list.get(i);
            final String title = e.getAttributeValue("title");
            addDataToList(annotList, e, title);
        }
    }

    static void addDataToList(final List<AnnotMapElt> annotList, final Element e, final String title) {
        final String filename = e.getAttributeValue("name");
        final String desc = e.getAttributeValue("description");
        final String friendlyURL = e.getAttributeValue("url");
        final String serverURL = e.getAttributeValue("serverURL");
        final String load_hint = e.getAttributeValue("load_hint");
        final String auto_select = e.getAttributeValue("auto_select");
        final String label_field = e.getAttributeValue("label_field");
        final String foreground = e.getAttributeValue("foreground");
        final String background = e.getAttributeValue("background");
        final String max_depth = e.getAttributeValue("max_depth");
        final String name_size = e.getAttributeValue("name_size");
        final String connected = e.getAttributeValue("connected");
        final String collapsed = e.getAttributeValue("collapsed");
        final String show2tracks = e.getAttributeValue("show2tracks");
        final String direction_type = e.getAttributeValue("direction_type");
        final String positive_strand_color = e.getAttributeValue("positive_strand_color");
        final String negative_strand_color = e.getAttributeValue("negative_strand_color");
        final String view_mode = e.getAttributeValue("view_mode");
        if (filename != null) {
            final AnnotMapElt annotMapElt = new AnnotMapElt(filename, title, desc, friendlyURL, serverURL, load_hint, auto_select, label_field, foreground, background, max_depth, name_size, connected, collapsed, show2tracks, direction_type, positive_strand_color, negative_strand_color, view_mode);
            annotList.add(annotMapElt);
        }
    }

    static String getPath(Element e) {
        String path="";
		while (e.getParentElement() != root) {
			e = e.getParentElement();
			path = e.getAttributeValue("name") + "/" + path;
		}

//        for (path = ""; e.getParentElement() != AnnotsXmlParser.root; e = e.getParentElement(), path = e.getAttributeValue("name") + "/" + path) {}
        return path;
    }

    public static class AnnotMapElt
    {
        public String fileName;
        public String title;
        public String serverURL;
        public Map<String, String> props;

        public AnnotMapElt(final String fileName, final String title) {
            this(fileName, title, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
        }

        public AnnotMapElt(final String fileName, final String title, final String description, final String URL, final String serverURL, final String load_hint, final String auto_select, final String label_field, final String foreground, final String background, final String max_depth, final String name_size, final String connected, final String collapsed, final String show2tracks, final String direction_type, final String positive_strand_color, final String negative_strand_color, final String view_mode) {
            this.props = new HashMap<String, String>();
            this.fileName = fileName;
            this.title = ((title == null) ? "" : title);
            this.serverURL = ((serverURL == null) ? "" : serverURL);
            if (description != null && description.trim().length() > 0) {
                this.props.put("description", description);
            }
            if (URL != null && URL.trim().length() > 0) {
                this.props.put("url", URL);
            }
            if (load_hint != null && load_hint.trim().length() > 0) {
                this.props.put("load_hint", load_hint);
            }
            if (auto_select != null && auto_select.trim().length() > 0) {
                this.props.put("auto_select", auto_select);
            }
            if (label_field != null && label_field.trim().length() > 0) {
                this.props.put("label_field", label_field);
            }
            if (foreground != null && foreground.trim().length() > 0) {
                this.props.put("foreground", foreground);
            }
            if (background != null && background.trim().length() > 0) {
                this.props.put("background", background);
            }
            if (max_depth != null && max_depth.trim().length() > 0) {
                this.props.put("max_depth", max_depth);
            }
            if (name_size != null && name_size.trim().length() > 0) {
                this.props.put("name_size", name_size);
            }
            if (connected != null && connected.trim().length() > 0) {
                this.props.put("connected", connected);
            }
            if (collapsed != null && collapsed.trim().length() > 0) {
                this.props.put("collapsed", collapsed);
            }
            if (show2tracks != null && show2tracks.trim().length() > 0) {
                this.props.put("show2tracks", show2tracks);
            }
            if (direction_type != null && direction_type.trim().length() > 0) {
                this.props.put("direction_type", direction_type);
            }
            if (positive_strand_color != null && positive_strand_color.trim().length() > 0) {
                this.props.put("positive_strand_color", positive_strand_color);
            }
            if (negative_strand_color != null && negative_strand_color.trim().length() > 0) {
                this.props.put("negative_strand_color", negative_strand_color);
            }
            if (view_mode != null && view_mode.trim().length() > 0) {
                this.props.put("view_mode", view_mode);
            }
        }

        public static AnnotMapElt findFileNameElt(final String fileName, final List<AnnotMapElt> annotList) {
            for (final AnnotMapElt annotMapElt : new CopyOnWriteArrayList<AnnotMapElt>(annotList)) {
                if (annotMapElt.fileName.equalsIgnoreCase(fileName)) {
                    return annotMapElt;
                }
            }
            return null;
        }

        public static AnnotMapElt findTitleElt(final String title, final List<AnnotMapElt> annotList) {
            for (final AnnotMapElt annotMapElt : new CopyOnWriteArrayList<AnnotMapElt>(annotList)) {
                if (annotMapElt.title.equalsIgnoreCase(title)) {
                    return annotMapElt;
                }
            }
            return null;
        }
    }
}
