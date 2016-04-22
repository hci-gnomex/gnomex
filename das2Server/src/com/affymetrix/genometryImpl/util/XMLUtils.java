// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.util;

import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.IOException;
import org.xml.sax.SAXException;
import java.net.MalformedURLException;
import java.net.URLConnection;
import java.net.URL;
import org.w3c.dom.Document;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.DocumentBuilderFactory;

public class XMLUtils
{
    public static DocumentBuilderFactory nonValidatingFactory() {
        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(false);
        setFeature(factory, "http://xml.org/sax/features/validation");
        setFeature(factory, "http://apache.org/xml/features/validation/dynamic");
        setFeature(factory, "http://apache.org/xml/features/nonvalidating/load-external-dtd");
        return factory;
    }
    
    private static void setFeature(final DocumentBuilderFactory factory, final String feature) {
        try {
            factory.setFeature(feature, false);
        }
        catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }
    
    public static Document getDocument(final String url) throws ParserConfigurationException, MalformedURLException, SAXException, IOException {
        Document doc = null;
        final URL request_url = new URL(url);
        final URLConnection request_con = request_url.openConnection();
        request_con.setConnectTimeout(20000);
        request_con.setReadTimeout(60000);
        doc = getDocument(request_con);
        return doc;
    }
    
    public static Document getDocument(final URLConnection request_con) throws ParserConfigurationException, SAXException, IOException {
        InputStream result_stream = null;
        Document doc = null;
        try {
            result_stream = new BufferedInputStream(request_con.getInputStream());
            doc = getDocument(result_stream);
        }
        finally {
            GeneralUtils.safeClose(result_stream);
        }
        return doc;
    }
    
    public static Document getDocument(final InputStream str) throws ParserConfigurationException, SAXException, IOException {
        return nonValidatingFactory().newDocumentBuilder().parse(str);
    }
}
