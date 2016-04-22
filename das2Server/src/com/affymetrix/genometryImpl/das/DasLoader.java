// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.das;

import java.util.regex.Matcher;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import org.w3c.dom.Document;
import org.w3c.dom.Text;
import com.affymetrix.genometryImpl.util.XMLUtils;
import org.xml.sax.InputSource;
import java.util.Set;
import com.affymetrix.genometryImpl.util.GeneralUtils;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.InputStream;
import java.io.BufferedInputStream;
import com.affymetrix.genometryImpl.util.LocalUrlCacher;
import com.affymetrix.genometryImpl.util.QueryBuilder;
import java.net.URL;
import java.net.URI;
import java.util.Collection;
import com.affymetrix.genometryImpl.util.SynonymLookup;
import com.affymetrix.genometryImpl.general.GenericVersion;
import java.util.regex.Pattern;

public abstract class DasLoader
{
    private static final Pattern white_space;
    
    public static String getDasResidues(final GenericVersion version, final String seqid, final int min, final int max) {
        final Set<String> segments = ((DasSource)version.versionSourceObj).getEntryPoints();
        final String segment = SynonymLookup.getDefaultLookup().findMatchingSynonym(segments, seqid);
        InputStream result_stream = null;
        String residues = null;
        try {
            URI request = URI.create(version.gServer.URL);
            final URL url = new URL(request.toURL(), version.versionID + "/dna?");
            final QueryBuilder builder = new QueryBuilder(url.toExternalForm());
            builder.add("segment", segment + ":" + (min + 1) + "," + max);
            request = builder.build();
            result_stream = LocalUrlCacher.getInputStream(request.toString());
            residues = parseDasResidues(new BufferedInputStream(result_stream));
        }
        catch (MalformedURLException ex) {
            Logger.getLogger(DasLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IOException ex2) {
            Logger.getLogger(DasLoader.class.getName()).log(Level.SEVERE, null, ex2);
        }
        catch (ParserConfigurationException ex3) {
            Logger.getLogger(DasLoader.class.getName()).log(Level.SEVERE, null, ex3);
        }
        catch (SAXException ex4) {
            Logger.getLogger(DasLoader.class.getName()).log(Level.SEVERE, null, ex4);
        }
        finally {
            GeneralUtils.safeClose(result_stream);
        }
        return residues;
    }
    
    private static String parseDasResidues(final InputStream das_dna_result) throws IOException, SAXException, ParserConfigurationException {
        final InputSource isrc = new InputSource(das_dna_result);
        final Document doc = XMLUtils.nonValidatingFactory().newDocumentBuilder().parse(isrc);
        final Element top_element = doc.getDocumentElement();
        final NodeList top_children = top_element.getChildNodes();
        for (int i = 0; i < top_children.getLength(); ++i) {
            final Node top_child = top_children.item(i);
            final String cname = top_child.getNodeName();
            if (cname != null) {
                if (cname.equalsIgnoreCase("sequence")) {
                    final NodeList seq_children = top_child.getChildNodes();
                    for (int k = 0; k < seq_children.getLength(); ++k) {
                        final Node seq_child = seq_children.item(k);
                        if (seq_child != null) {
                            if (seq_child.getNodeName().equalsIgnoreCase("DNA")) {
                                final NodeList dna_children = seq_child.getChildNodes();
                                for (int m = 0; m < dna_children.getLength(); ++m) {
                                    final Node dna_child = dna_children.item(m);
                                    if (dna_child instanceof Text) {
                                        String residues = ((Text)dna_child).getData();
                                        final Matcher matcher = DasLoader.white_space.matcher("");
                                        residues = matcher.reset(residues).replaceAll("");
                                        return residues;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return null;
    }
    
    static {
        white_space = Pattern.compile("\\s+");
    }
}
