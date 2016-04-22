// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.parsers.das;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.stream.events.Attribute;
import javax.xml.namespace.QName;
import java.util.Iterator;
import com.affymetrix.genometryImpl.symmetry.SeqSymmetry;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.Characters;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import java.util.Map;
import java.util.Deque;
import javax.xml.stream.XMLEventReader;
import java.util.Collection;
import java.util.ArrayList;
import java.util.HashMap;
import javax.xml.stream.events.StartElement;
import java.util.ArrayDeque;
import javax.xml.stream.XMLInputFactory;
import java.util.List;
import com.affymetrix.genometryImpl.AnnotatedSeqGroup;
import java.io.InputStream;
import com.affymetrix.genometryImpl.BioSeq;
import com.affymetrix.genometryImpl.parsers.Parser;

public final class DASFeatureParser implements Parser
{
    private BioSeq sequence;
    private String note;
    private boolean annotateSeq;
    
    public DASFeatureParser() {
        this.annotateSeq = true;
    }
    
    public void setAnnotateSeq(final boolean annotateSeq) {
        this.annotateSeq = annotateSeq;
    }
    
    public List<DASSymmetry> parse(final InputStream s, final AnnotatedSeqGroup seqGroup) throws XMLStreamException {
        final XMLInputFactory factory = XMLInputFactory.newInstance();
        final XMLEventReader reader = factory.createXMLEventReader(s);
        final Deque<StartElement> stack = new ArrayDeque<StartElement>();
        final FeatureBean feature = new FeatureBean();
        final LinkBean link = new LinkBean();
        final GroupBean group = new GroupBean();
        final TargetBean target = new TargetBean();
        final Map<String, DASSymmetry> groupMap = new HashMap<String, DASSymmetry>();
        while (reader.hasNext() && !Thread.currentThread().isInterrupted()) {
            final XMLEvent current = reader.nextEvent();
            switch (current.getEventType()) {
                case 1: {
                    this.startElement(current.asStartElement(), feature, link, group, target, seqGroup);
                    stack.push(current.asStartElement());
                    continue;
                }
                case 4: {
                    this.characters(current.asCharacters(), stack.peek(), feature, link, target);
                    continue;
                }
                case 2: {
                    stack.pop();
                    this.endElement(current.asEndElement(), stack.peek(), groupMap, feature, link, group, target, seqGroup);
                    continue;
                }
            }
        }
        return new ArrayList<DASSymmetry>(groupMap.values());
    }
    
    private void startElement(final StartElement current, final FeatureBean feature, final LinkBean link, final GroupBean group, final TargetBean target, final AnnotatedSeqGroup seqGroup) {
        switch (Elements.valueOf(current.getName().getLocalPart())) {
            case SEGMENT: {
                this.sequence = seqGroup.addSeq(getAttribute(current, Attr.id), Integer.valueOf(getAttribute(current, Attr.stop)));
                break;
            }
            case FEATURE: {
                feature.clear();
                feature.setID(getAttribute(current, Attr.id));
                feature.setLabel(getAttribute(current, Attr.label));
                break;
            }
            case TYPE: {
                feature.setTypeID(getAttribute(current, Attr.id));
                feature.setTypeCategory(getAttribute(current, Attr.category));
                feature.setTypeReference(getAttribute(current, Attr.reference));
                break;
            }
            case METHOD: {
                feature.setMethodID(getAttribute(current, Attr.id));
                break;
            }
            case LINK: {
                link.clear();
                link.setURL(getAttribute(current, Attr.href));
                break;
            }
            case TARGET: {
                target.clear();
                target.setID(getAttribute(current, Attr.id));
                target.setStart(getAttribute(current, Attr.start));
                target.setStop(getAttribute(current, Attr.stop));
                break;
            }
            case GROUP: {
                group.clear();
                group.setID(getAttribute(current, Attr.id));
                group.setLabel(getAttribute(current, Attr.label));
                group.setType(getAttribute(current, Attr.type));
                break;
            }
        }
    }
    
    private void characters(final Characters current, final StartElement parent, final FeatureBean feature, final LinkBean link, final TargetBean target) {
        switch (Elements.valueOf(parent.getName().getLocalPart())) {
            case TYPE: {
                feature.setTypeLabel(current.getData());
                break;
            }
            case METHOD: {
                feature.setMethodLabel(current.getData());
                break;
            }
            case START: {
                feature.setStart(current.getData());
                break;
            }
            case END: {
                feature.setEnd(current.getData());
                break;
            }
            case SCORE: {
                feature.setScore(current.getData());
                break;
            }
            case ORIENTATION: {
                feature.setOrientation(current.getData());
                break;
            }
            case PHASE: {
                feature.setPhase(current.getData());
                break;
            }
            case NOTE: {
                this.note = current.getData();
                break;
            }
            case LINK: {
                link.setTitle(current.getData());
                break;
            }
            case TARGET: {
                target.setName(current.getData());
                break;
            }
        }
    }
    
    private void endElement(final EndElement current, final StartElement parent, final Map<String, DASSymmetry> groupMap, final FeatureBean feature, final LinkBean link, final GroupBean group, final TargetBean target, final AnnotatedSeqGroup seqGroup) {
        Elements p = null;
        if (parent != null) {
            p = Elements.valueOf(parent.getName().getLocalPart());
        }
        switch (Elements.valueOf(current.getName().getLocalPart())) {
            case FEATURE: {
                final DASSymmetry featureSymmetry = new DASSymmetry(feature, this.sequence);
                if (feature.getGroups().isEmpty()) {
                    if (this.annotateSeq) {
                        this.sequence.addAnnotation(featureSymmetry);
                    }
                    groupMap.put(featureSymmetry.getID(), featureSymmetry);
                    break;
                }
                for (final GroupBean groupBean : feature.getGroups()) {
                    final DASSymmetry groupSymmetry = this.getGroupSymmetry(groupMap, feature, groupBean, seqGroup);
                    groupSymmetry.addChild(featureSymmetry);
                }
                break;
            }
            case NOTE: {
                if (p == Elements.FEATURE) {
                    feature.addNote(this.note);
                    break;
                }
                if (p == Elements.GROUP) {
                    group.addNote(this.note);
                    break;
                }
                break;
            }
            case LINK: {
                if (p == Elements.FEATURE) {
                    feature.addLink(link);
                    break;
                }
                if (p == Elements.GROUP) {
                    group.addLink(link);
                    break;
                }
                break;
            }
            case TARGET: {
                if (p == Elements.FEATURE) {
                    feature.addTarget(target);
                    break;
                }
                if (p == Elements.GROUP) {
                    group.addTarget(target);
                    break;
                }
                break;
            }
            case GROUP: {
                feature.addGroup(group);
                break;
            }
        }
    }
    
    private static String getAttribute(final StartElement current, final Attr attr) {
        final QName qName = new QName(current.getName().getNamespaceURI(), attr.toString());
        final Attribute attribute = current.getAttributeByName(qName);
        return (attribute == null) ? "" : attribute.getValue();
    }
    
    private DASSymmetry getGroupSymmetry(final Map<String, DASSymmetry> groupMap, final FeatureBean feature, final GroupBean group, final AnnotatedSeqGroup seqGroup) {
        if (groupMap.containsKey(group.getID())) {
            return groupMap.get(group.getID());
        }
        for (final SeqSymmetry sym : seqGroup.findSyms(group.getID())) {
            if (sym instanceof DASSymmetry && sym.getSpan(this.sequence) != null) {
                groupMap.put(sym.getID(), (DASSymmetry)sym);
                return (DASSymmetry)sym;
            }
        }
        final DASSymmetry groupSymmetry = new DASSymmetry(group, feature, this.sequence);
        if (this.annotateSeq) {
            this.sequence.addAnnotation(groupSymmetry);
        }
        seqGroup.addToIndex(groupSymmetry.getID(), groupSymmetry);
        groupMap.put(groupSymmetry.getID(), groupSymmetry);
        return groupSymmetry;
    }
    
    @Override
    public List<? extends SeqSymmetry> parse(final InputStream is, final AnnotatedSeqGroup group, final String nameType, final String uri, final boolean annotate_seq) throws Exception {
        this.setAnnotateSeq(annotate_seq);
        try {
            return this.parse(is, group);
        }
        catch (XMLStreamException ex) {
            Logger.getLogger(DASFeatureParser.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    enum Orientation
    {
        UNKNOWN, 
        FORWARD, 
        REVERSE;
    }
    
    private enum Elements
    {
        DASGFF, 
        GFF, 
        SEGMENT, 
        FEATURE, 
        TYPE, 
        METHOD, 
        START, 
        END, 
        SCORE, 
        ORIENTATION, 
        PHASE, 
        NOTE, 
        LINK, 
        TARGET, 
        GROUP;
    }
    
    private enum Attr
    {
        version, 
        href, 
        id, 
        start, 
        stop, 
        type, 
        label, 
        category, 
        reference;
    }
}
