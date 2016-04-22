// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.util;

import java.io.ByteArrayInputStream;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.HashSet;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.FileOutputStream;
import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingWorker;
import java.util.concurrent.Executors;
import com.affymetrix.genometryImpl.general.GenericServer;
import java.util.Set;

public class CacheScript extends Thread
{
    private static final String temp = "temp";
    private final String path;
    private final Set<GenericServer> server_list;
    private static final String defaultList = " <servers>  <server type='das' name='UCSC Das' url='http://genome.cse.ucsc.edu/cgi-bin/das/dsn' /> <server type='das' name='Ensembl' url='http://www.ensembl.org/das/dsn' enabled='false' /> </servers> ";
    
    public CacheScript(final String path, final Set<GenericServer> server_list) {
        this.path = path;
        this.server_list = server_list;
    }
    
    @Override
    public void run() {
        for (final GenericServer gServer : this.server_list) {
            final Timer ser_tim = new Timer();
            final ExecutorService vexec = Executors.newSingleThreadExecutor();
            final SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                @Override
                protected Void doInBackground() {
                    ser_tim.start();
                    if (processServer(gServer, CacheScript.this.path)) {
                        copyDirectoryFor(CacheScript.this.path, gServer.serverName);
                    }
                    return null;
                }
                
                public void done() {
                    Logger.getLogger(CacheScript.class.getName()).log(Level.INFO, "Time required to cache " + gServer.serverName + " :" + ser_tim.read() / 1000.0f, ser_tim);
                }
            };
            vexec.execute(worker);
            vexec.shutdown();
        }
    }
    
    public void writeServerMapping() {
        FileOutputStream fos = null;
        PrintStream out = null;
        try {
            final File mapping = new File(this.path + "/" + "serverMapping.txt");
            mapping.createNewFile();
            fos = new FileOutputStream(mapping);
            out = new PrintStream(fos);
            for (final GenericServer gServer : this.server_list) {
                out.println(gServer.URL + "\t" + gServer.serverName);
            }
        }
        catch (IOException ex) {
            Logger.getLogger(CacheScript.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally {
            GeneralUtils.safeClose(fos);
            GeneralUtils.safeClose(out);
        }
    }
    
    private static boolean processServer(final GenericServer gServer, final String path) {
        Logger.getLogger(CacheScript.class.getName()).log(Level.FINE, "Caching {0} at path {1}", new Object[] { gServer.serverName, path });
        final String serverCachePath = path + gServer.serverName + "temp";
        GeneralUtils.makeDir(serverCachePath);
        return gServer.serverType.processServer(gServer, path);
    }
    
    public static boolean getFileAvailability(final String fileName) {
        return fileName.equals("annots.txt") || fileName.equals("annots.xml") || fileName.equals("liftAll.lft");
    }
    
    private static Set<GenericServer> parseServerList(final InputStream istr) throws Exception {
        final Set<GenericServer> serverList = new HashSet<GenericServer>();
        final Document list = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(istr);
        final Element top_element = list.getDocumentElement();
        final String topname = top_element.getTagName();
        if (!topname.equalsIgnoreCase("servers")) {
            System.err.println("not a server list file -- can't parse!");
        }
        final NodeList children = top_element.getChildNodes();
        Element el = null;
        for (int i = 0; i < children.getLength(); ++i) {
            final Node child = children.item(i);
            final String name = child.getNodeName();
            if (child instanceof Element) {
                el = (Element)child;
                if (name.equalsIgnoreCase("server")) {
                    final ServerTypeI server_type = getServerType(el.getAttribute("type"));
                    final String server_name = el.getAttribute("name").replaceAll("\\W", "");
                    final String server_url = el.getAttribute("url");
                    final String en = el.getAttribute("enabled");
                    final Boolean enabled = en == null || en.isEmpty() || Boolean.valueOf(en);
                    final String d = el.getAttribute("default");
                    final Boolean isDefault = d == null || d.isEmpty() || Boolean.valueOf(d);
                    final String serverURL = ServerUtils.formatURL(server_url, server_type);
                    final Object serverInfo = server_type.getServerInfo(serverURL, server_name);
                    final GenericServer server = new GenericServer(server_name, serverURL, server_type, enabled, serverInfo, isDefault);
                    serverList.add(server);
                }
            }
        }
        return serverList;
    }
    
    private static ServerTypeI getServerType(final String type) {
        for (final ServerTypeI t : ServerUtils.getServerTypes()) {
            if (type.equalsIgnoreCase(t.toString())) {
                return t;
            }
        }
        return null;
    }
    
    private static void copyRecursively(final File source, final File dest) {
        for (final File file : source.listFiles()) {
            if (file.isDirectory()) {
                copyRecursively(file, GeneralUtils.makeDir(dest.getPath() + "/" + file.getName()));
            }
            else {
                GeneralUtils.moveFileTo(file, file.getName(), dest.getPath());
            }
        }
    }
    
    private static void copyDirectoryFor(final String path, final String servername) {
        final File temp_dir = new File(path + servername + "temp");
        final String perm_path = path + servername;
        GeneralUtils.makeDir(perm_path);
        final File perm_dir = new File(perm_path);
        copyRecursively(temp_dir, perm_dir);
    }
    
    public static void main(final String[] args) {
        InputStream istr = null;
        try {
            istr = new ByteArrayInputStream(" <servers>  <server type='das' name='UCSC Das' url='http://genome.cse.ucsc.edu/cgi-bin/das/dsn' /> <server type='das' name='Ensembl' url='http://www.ensembl.org/das/dsn' enabled='false' /> </servers> ".getBytes());
            String path = "/";
            if (args.length >= 1) {
                path = args[0];
            }
            final Set<GenericServer> server_list = parseServerList(istr);
            final CacheScript script = new CacheScript(path, server_list);
            script.start();
            script.writeServerMapping();
        }
        catch (Exception ex) {
            Logger.getLogger(CacheScript.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally {
            GeneralUtils.safeClose(istr);
        }
    }
}
