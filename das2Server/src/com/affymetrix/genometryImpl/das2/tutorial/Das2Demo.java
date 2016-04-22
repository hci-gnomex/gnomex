// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.das2.tutorial;

import java.net.PasswordAuthentication;
import java.io.IOException;
import java.util.zip.ZipInputStream;
import java.io.Reader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;
import java.io.BufferedReader;
import java.net.URL;
import com.affymetrix.genometryImpl.das2.Das2Region;
import java.util.Iterator;
import java.util.Map;
import com.affymetrix.genometryImpl.das2.Das2Type;
import com.affymetrix.genometryImpl.das2.Das2VersionedSource;
import com.affymetrix.genometryImpl.das2.Das2Source;
import com.affymetrix.genometryImpl.das2.Das2ServerInfo;
import java.net.Authenticator;

public class Das2Demo
{
    private String genoPubDas2UrlString;
    private String name;
    private String userName;
    private String password;
    
    public Das2Demo() {
        this.genoPubDas2UrlString = "http://bioserver.hci.utah.edu:8080/DAS2DB/genome";
        this.name = "UofUBioInfoCore";
        this.userName = "guest";
        this.password = "guest";
        try {
            final String das2UrlString = this.genoPubDas2UrlString;
            Authenticator.setDefault(new MyAuthenticator(this.userName, this.password));
            final Das2ServerInfo dsi = new Das2ServerInfo(das2UrlString, this.name, true);
            final Map<String, Das2Source> das2Sources = dsi.getSources();
            System.out.println("\nSpecies:");
            for (final String speciesName : das2Sources.keySet()) {
                System.out.println("\t" + speciesName);
            }
            System.out.println("\nSpecies and genome builds:");
            for (final String speciesName : das2Sources.keySet()) {
                System.out.println("\t" + speciesName);
                final Das2Source ds = das2Sources.get(speciesName);
                final Map<String, Das2VersionedSource> das2VersionedSources = ds.getVersions();
                for (final String genomeBuild : das2VersionedSources.keySet()) {
                    System.out.println("\t\t" + genomeBuild);
                }
            }
            final Das2Source sourceHSapiens = das2Sources.get(das2UrlString + "/H_sapiens");
            final Das2VersionedSource versionedSourceHSapiensMar2006 = sourceHSapiens.getVersions().get(das2UrlString + "/H_sapiens_Mar_2006");
            final Map<String, Das2Region> segmentsHSapiensMar2006 = versionedSourceHSapiensMar2006.getSegments();
            System.out.println("\nChromosomes for H_sapiens_Mar_2006:");
            for (final String chromosome : segmentsHSapiensMar2006.keySet()) {
                System.out.println("\t" + chromosome);
            }
            final Map<String, Das2Type> typesHSapiensMar2006 = versionedSourceHSapiensMar2006.getTypes();
            System.out.println("\nTypes/ datasets for H_sapiens_Mar_2006:");
            for (final String typeName : typesHSapiensMar2006.keySet()) {
                System.out.println("\t" + typeName);
            }
            final Das2Type dataset = typesHSapiensMar2006.get("http://bioserver.hci.utah.edu:8080/DAS2DB/genome/H_sapiens_Mar_2006/ENCODE/USeq/CTCF+Broad/Peak+Calls/All+Peaks+Summed+Scores");
            final Map<String, String> properties = dataset.getProps();
            System.out.println("\nProperties for example dataset:");
            for (final String key : properties.keySet()) {
                System.out.println("\t" + key + " = " + properties.get(key));
            }
            final Map<String, String> formats = dataset.getFormats();
            System.out.println("\nFormats for example dataset:");
            for (final String key2 : formats.keySet()) {
                System.out.println("\t" + key2 + " = " + properties.get(key2));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void main(final String[] args) {
        new Das2Demo();
    }
    
    public static BufferedReader fetchBufferedReader(final URL url) throws IOException {
        BufferedReader in = null;
        final InputStream is = url.openStream();
        final String name = url.toString();
        if (name.endsWith(".gz")) {
            in = new BufferedReader(new InputStreamReader(new GZIPInputStream(is)));
        }
        else if (name.endsWith(".zip")) {
            final ZipInputStream zis = new ZipInputStream(is);
            zis.getNextEntry();
            in = new BufferedReader(new InputStreamReader(zis));
        }
        else {
            in = new BufferedReader(new InputStreamReader(is));
        }
        return in;
    }
    
    private class MyAuthenticator extends Authenticator
    {
        String userName;
        String password;
        
        public MyAuthenticator(final String userName, final String password) {
            this.userName = userName;
            this.password = password;
        }
        
        public PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(this.userName, this.password.toCharArray());
        }
    }
}
