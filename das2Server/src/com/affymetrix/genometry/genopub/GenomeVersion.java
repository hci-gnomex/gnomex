//
// Decompiled by Procyon v0.5.30
//

package com.affymetrix.genometry.genopub;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.IOException;
import org.dom4j.Element;
import java.text.NumberFormat;
import java.io.File;
import org.dom4j.DocumentHelper;
import org.dom4j.Document;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.sql.Date;
import java.io.Serializable;

public class GenomeVersion implements Serializable
{
    private Integer idGenomeVersion;
    private String name;
    private String ucscName;
    private Date buildDate;
    private String coordURI;
    private String coordAuthority;
    private String coordVersion;
    private String coordSource;
    private String coordTestRange;
    private Set segments;
    private Set aliases;
    private Integer idOrganism;
    private String dataPath;
    private Set annotationGroupings;
    private Set annotations;

    public Integer getIdGenomeVersion() {
        return this.idGenomeVersion;
    }

    public void setIdGenomeVersion(final Integer idGenomeVersion) {
        this.idGenomeVersion = idGenomeVersion;
    }

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Date getBuildDate() {
        return this.buildDate;
    }

    public void setBuildDate(final Date buildDate) {
        this.buildDate = buildDate;
    }

    public String getCoordURI() {
        return this.coordURI;
    }

    public void setCoordURI(final String coordURI) {
        this.coordURI = coordURI;
    }

    public String getCoordAuthority() {
        return this.coordAuthority;
    }

    public void setCoordAuthority(final String coordAuthority) {
        this.coordAuthority = coordAuthority;
    }

    public String getCoordVersion() {
        return this.coordVersion;
    }

    public void setCoordVersion(final String coordVersion) {
        this.coordVersion = coordVersion;
    }

    public String getCoordSource() {
        return this.coordSource;
    }

    public void setCoordSource(final String coordSource) {
        this.coordSource = coordSource;
    }

    public String getCoordTestRange() {
        return this.coordTestRange;
    }

    public void setCoordTestRange(final String coordTestRange) {
        this.coordTestRange = coordTestRange;
    }

    public Set getSegments() {
        return this.segments;
    }

    public void setSegments(final Set segments) {
        this.segments = segments;
    }

    public Set getAliases() {
        return this.aliases;
    }

    public void setAliases(final Set aliases) {
        this.aliases = aliases;
    }

    public Set getAnnotationGroupings() {
        return this.annotationGroupings;
    }

    public void setAnnotationGroupings(final Set annotationGroupings) {
        this.annotationGroupings = annotationGroupings;
    }

    private List getRootAnnotationGroupings() {
        final ArrayList rootGroupings = new ArrayList();
        for (final AnnotationGrouping annotationGrouping : (Set<AnnotationGrouping>) this.getAnnotationGroupings()) {
            if (annotationGrouping.getIdParentAnnotationGrouping() == null) {
                rootGroupings.add(annotationGrouping);
            }
        }
        return rootGroupings;
    }

    public AnnotationGrouping getRootAnnotationGrouping() {
        final List rootGroupings = (List) this.getRootAnnotationGroupings();
        if (rootGroupings.size() > 0) {
            return AnnotationGrouping.class.cast(rootGroupings.get(0));
        }
        return null;
    }

    public Integer getIdOrganism() {
        return this.idOrganism;
    }

    public void setIdOrganism(final Integer idOrganism) {
        this.idOrganism = idOrganism;
    }

    public String getDataPath() {
        return this.dataPath;
    }

    public void setDataPath(final String dataPath) {
        this.dataPath = dataPath;
    }

    public Set getAnnotations() {
        return this.annotations;
    }

    public void setAnnotations(final Set annotations) {
        this.annotations = annotations;
    }

    public Document getXML(final GenoPubSecurity genoPubSecurity, final String data_root) {
        final Document doc = DocumentHelper.createDocument();
        final Element root = doc.addElement("GenomeVersion");
        root.addAttribute("label", this.getName());
        root.addAttribute("idGenomeVersion", this.getIdGenomeVersion().toString());
        root.addAttribute("name", this.getName());
        root.addAttribute("ucscName", this.getUcscName());
        root.addAttribute("buildDate", (this.getBuildDate() != null) ? Util.formatDate(this.getBuildDate()) : "");
        root.addAttribute("idOrganism", this.getIdOrganism().toString());
        root.addAttribute("coordURI", (this.getCoordURI() != null) ? this.getCoordURI().toString() : "");
        root.addAttribute("coordVersion", (this.getCoordVersion() != null) ? this.getCoordVersion().toString() : "");
        root.addAttribute("coordSource", (this.getCoordSource() != null) ? this.getCoordSource().toString() : "");
        root.addAttribute("coordTestRange", (this.getCoordTestRange() != null) ? this.getCoordTestRange().toString() : "");
        root.addAttribute("coordAuthority", (this.getCoordAuthority() != null) ? this.getCoordAuthority().toString() : "");
        if (data_root != null) {
            final Element filesNode = root.addElement("SequenceFiles");
            final String filePath = this.getSequenceDirectory(data_root);
            final File fd = new File(filePath);
            if (fd.exists()) {
                final Element fileNode = filesNode.addElement("Dir");
                fileNode.addAttribute("name", this.getSequenceFileName());
                fileNode.addAttribute("url", filePath);
                appendSequenceFileXML(filePath, fileNode, null);
            }
            final Element segmentsNode = root.addElement("Segments");
            for (final Segment segment : (Set<Segment>) this.getSegments()) {
                final Element sNode = segmentsNode.addElement("Segment");
                sNode.addAttribute("idSegment", segment.getIdSegment().toString());
                sNode.addAttribute("name", segment.getName());
                sNode.addAttribute("length", (segment.getLength() != null) ? NumberFormat.getInstance().format(segment.getLength()) : "");
                sNode.addAttribute("sortOrder", (segment.getSortOrder() != null) ? segment.getSortOrder().toString() : "");
            }
        }
        root.addAttribute("canRead", genoPubSecurity.canRead(this) ? "Y" : "N");
        root.addAttribute("canWrite", genoPubSecurity.canWrite(this) ? "Y" : "N");
        return doc;
    }

    public String getSequenceDirectory(final String data_root) {
        String dataPath = null;
        if (this.getDataPath() != null && !this.getDataPath().equals("")) {
            dataPath = this.getDataPath();
        }
        else {
            dataPath = data_root;
        }
        return dataPath + this.getSequenceFileName();
    }

    public String getSequenceFileName() {
        return "SEQ" + this.getIdGenomeVersion();
    }

    public boolean hasSequence(final String data_root) throws IOException {
        boolean hasSequence = false;
        final String filePath = this.getSequenceDirectory(data_root);
        final File dir = new File(filePath);
        if (dir.exists()) {
            final String[] childFileNames = dir.list();
            if (childFileNames != null && childFileNames.length > 0) {
                hasSequence = true;
            }
        }
        return hasSequence;
    }

    public void removeSequenceFiles(final String data_root) throws IOException {
        final String filePath = this.getSequenceDirectory(data_root);
        final File dir = new File(filePath);
        if (dir.exists()) {
            final String[] childFileNames = dir.list();
            if (childFileNames != null) {
                for (int x = 0; x < childFileNames.length; ++x) {
                    final String fileName = filePath + "/" + childFileNames[x];
                    final File f = new File(fileName);
                    final boolean success = f.delete();
                    if (!success) {
                        Logger.getLogger(GenomeVersion.class.getName()).log(Level.WARNING, "Unable to delete file " + fileName);
                    }
                }
            }
            final boolean success2 = dir.delete();
            if (!success2) {
                Logger.getLogger(GenomeVersion.class.getName()).log(Level.WARNING, "Unable to delete directory " + filePath);
            }
        }
    }

    public static void appendSequenceFileXML(final String filePath, final Element parentNode, final String subDirName) {
        final File fd = new File(filePath);
        if (fd.isDirectory()) {
            final String[] fileList = fd.list();
            for (int x = 0; x < fileList.length; ++x) {
                final String fileName = filePath + "/" + fileList[x];
                final File f1 = new File(fileName);
                String displayName = "";
                if (subDirName != null) {
                    displayName = subDirName + "/" + fileList[x];
                }
                else {
                    displayName = f1.getName();
                }
                if (f1.isDirectory()) {
                    final Element fileNode = parentNode.addElement("Dir");
                    fileNode.addAttribute("name", displayName);
                    fileNode.addAttribute("url", fileName);
                    appendSequenceFileXML(fileName, fileNode, (subDirName != null) ? (subDirName + "/" + f1.getName()) : f1.getName());
                }
                else {
                    final Element fileNode = parentNode.addElement("File");
                    final long kb = Util.getKilobytes(f1.length());
                    final String kilobytes = kb + " kb";
                    fileNode.addAttribute("name", displayName);
                    fileNode.addAttribute("url", fileName);
                    fileNode.addAttribute("size", kilobytes);
                    fileNode.addAttribute("lastModified", Util.formatDate(new Date(f1.lastModified())));
                }
            }
        }
    }

    public String getUcscName() {
        return this.ucscName;
    }

    public void setUcscName(final String ucscName) {
        this.ucscName = ucscName;
    }
}
