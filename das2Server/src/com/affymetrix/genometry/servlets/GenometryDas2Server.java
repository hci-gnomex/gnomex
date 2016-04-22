// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometry.servlets;

import org.mortbay.jetty.servlet.ServletHolder;
import java.awt.Container;
import org.mortbay.http.HttpHandler;
import org.mortbay.jetty.servlet.ServletHandler;
import org.mortbay.http.HttpContext;
import org.mortbay.http.HttpListener;
import org.mortbay.http.SocketListener;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import java.awt.LayoutManager;
import java.awt.BorderLayout;
import javax.swing.JFrame;
import org.mortbay.http.HttpServer;

public final class GenometryDas2Server
{
    static boolean SHOW_GUI;
    
    public static void main(final String[] args) throws Exception {
        final HttpServer server = new HttpServer();
        if (args.length < 3) {
            System.out.println("Usage: ... GenometryDas2Server port data_path admin_email xml_base");
            System.exit(0);
        }
        final int server_port = Integer.parseInt(args[0]);
        final String data_path = args[1];
        final String admin_email = args[2];
        final String xml_base = args[3];
        System.setProperty("das2_genometry_server_dir", data_path);
        System.setProperty("das2_maintainer_email", admin_email);
        System.setProperty("das2_xml_base", xml_base);
        if (GenometryDas2Server.SHOW_GUI) {
            final JFrame frm = new JFrame("Genometry Server");
            final Container cpane = frm.getContentPane();
            cpane.setLayout(new BorderLayout());
            final JButton exitB = new JButton("Quit Server");
            exitB.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(final ActionEvent evt) {
                    try {
                        server.stop();
                    }
                    catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    frm.setVisible(false);
                    System.exit(0);
                }
            });
            cpane.add("Center", exitB);
            frm.setSize(300, 80);
            frm.setVisible(true);
        }
        final SocketListener listener = new SocketListener();
        listener.setPort(server_port);
        server.addListener((HttpListener)listener);
        final HttpContext context = new HttpContext();
        context.setContextPath("/");
        final ServletHandler servlets = new ServletHandler();
        context.addHandler((HttpHandler)servlets);
        final ServletHolder das_holder = servlets.addServlet("GenometryDas2Servlet", "/das2/*", "com.affymetrix.genometry.servlets.GenometryDas2Servlet");
        das_holder.setInitOrder(1);
        server.addContext(context);
        server.start();
        final GenometryDas2Servlet das_servlet = (GenometryDas2Servlet)das_holder.getServlet();
        GenometryDas2Servlet.setXmlBase(xml_base);
    }
    
    static {
        GenometryDas2Server.SHOW_GUI = false;
    }
}
