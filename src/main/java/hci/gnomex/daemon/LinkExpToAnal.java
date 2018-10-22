package hci.gnomex.daemon;

// 03/15/2018	tim


import hci.gnomex.utility.BatchDataSource;
import hci.gnomex.utility.PropertyDictionaryHelper;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.AnalysisExperimentItem;
import hci.gnomex.model.Hybridization;
import hci.gnomex.model.Request;
import hci.gnomex.model.Sample;
import hci.gnomex.model.SequenceLane;
import hci.gnomex.utility.GNomExRollbackException;
import hci.gnomex.utility.HibernateSession;
import hci.gnomex.utility.Util;

import java.io.*;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.hibernate.Transaction;
import org.hibernate.internal.SessionImpl;
import org.hibernate.query.Query;
import org.hibernate.Session;


public class LinkExpToAnal extends TimerTask {

    private static long fONCE_PER_DAY = 1000 * 60 * 60 * 24; // A day in
    // milliseconds
    private static int fONE_DAY = 1;
    private static int wakeupHour = 2; // Default wakupHour is 2 am
    private static int fZERO_MINUTES = 0;

    private BatchDataSource dataSource;
    private Session sess;

    private static boolean all = false;
    private static Integer daysSince = null;
    private static String serverName = "";
    private static LinkExpToAnal app = null;

    private boolean runAsDaemon = false;

    private String baseExperimentDir;
    private String baseAnalysisDir;
    private Calendar asOfDate;
    private Calendar runDate; // Date program is being run.

    private Boolean debug = false;
    private Boolean testConnection = false;

    private String idRequest = null;
    private String idAnalysis = null;
    private boolean add = false;
    private boolean delete = false;

    private String errorMessageString = "Error in LinkExpToAnal";


    public LinkExpToAnal(String[] args) {
        int i = -1;
        while (i < args.length) {
            i++;
            if (i >= args.length) {
                break;
            }

            if (args[i].equals("-request")) {                // i.e., 148
                i++;
                if (i >= args.length) {
                    System.out.println("-request must be followed by the idRequest");
                    System.exit(1);
                }
                idRequest = args[i];
            } else if (args[i].equals("-debug")) {
                debug = true;
            } else if (args[i].equals("-analysis")) {
                i++;
                if (i >= args.length) {
                    System.out.println("-analysis must be followed by the idAnalysis");
                    System.exit(1);
                }
                idAnalysis = args[i];
            } else if (args[i].equals("-add")) {
                add = true;
            } else if (args[i].equals("-delete")) {
                delete = true;
            }
        }
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        app = new LinkExpToAnal(args);
        app.run();
    }

    @Override
    public void run() {
        runDate = Calendar.getInstance();
        errorMessageString += " on " + new SimpleDateFormat("MM-dd-yyyy_HH:mm:ss").format(runDate.getTime()) + "\n";

        try {
            Logger LOG = Logger.getLogger("org.hibernate");
            LOG.setLevel(Level.ERROR);

            dataSource = new BatchDataSource();
            app.connect();

            app.initialize();

            if (idRequest == null) {
                System.out.println("-request is required");
                System.out.println("Usage: sh ./LinkExpToAnal.sh -request idRequest (e.g., 126) -analysis idAnalysis [-add or -delete] {-debug is optional}");
                System.exit(1);
            }

            if (idAnalysis == null) {
                System.out.println("-analysis is required");
                System.out.println("Usage: sh ./LinkExpToAnal.sh -request idRequest (e.g., 126) -analysis idAnalysis [-add or -delete] {-debug is optional}");
                System.exit(1);
            }

            // do the work
            app.LinkExpToAnal();

            app.disconnect();
            System.out.println("Exiting...");
            System.exit(0);

        } catch (Exception e) {

            String msg = "The following error occured: " + e.toString() + "\n";
            System.out.println(msg);
            e.printStackTrace();
/*
            StackTraceElement[] stack = e.getStackTrace();
            for (StackTraceElement s : stack) {
                msg = msg + s.toString() + "\n\t\t";
            }

            System.out.println(msg);

            if (!errorMessageString.equals("")) {
                errorMessageString += "\n";
            }
            errorMessageString += msg;

            System.err.println(errorMessageString);
*/
        }

        System.out.println("Exiting(2)...");
        System.exit(0);

    }

    private void initialize() throws Exception {
        PropertyDictionaryHelper ph = PropertyDictionaryHelper.getInstance(sess);
        baseExperimentDir = PropertyDictionaryHelper.getInstance(sess).getDirectory(serverName, null,
                PropertyDictionaryHelper.PROPERTY_EXPERIMENT_DIRECTORY);
        baseAnalysisDir = PropertyDictionaryHelper.getInstance(sess).getDirectory(serverName, null,
                PropertyDictionaryHelper.PROPERTY_ANALYSIS_DIRECTORY);

    }

    private String getCurrentDateString() {
        runDate = Calendar.getInstance();
        return new SimpleDateFormat("MM-dd-yyyy_HH:mm:ss").format(runDate.getTime());

    }

    private void LinkExpToAnal() {       //throws Exception {

        // we are only doing add right now, need to add code for delete
        try {
            // make sure the request and the analysis exist
            int idRequestint = Integer.parseInt(idRequest);
            int idAnalysisint = Integer.parseInt(idAnalysis);
            Transaction tx = sess.beginTransaction();

            // first check to see if there is already a link between this request and analysis
            String theQuery = "Select aei from AnalysisExperimentItem aei where aei.idRequest = " + idRequest + " and aei.idAnalysis= " + idAnalysis;
            if (debug) System.out.println ("theQuery: " + theQuery);
            Query q = sess.createQuery(theQuery);
//            q.setParameter(0, idRequestint);
//            q.setParameter(1, idAnalysisint);
            if (q.list().size() != 0) {

//            }

//            List<AnalysisExperimentItem> aeiList = q.list();
//            if (aeiList != null && aeiList.size() != 0) {
                System.out.println("WARNING: A link already exists between this request (" + idRequest + ") and analysis (" + idAnalysis + ").");
                System.exit(2);
            }

            Request r = sess.load(Request.class, idRequestint);
            if (debug) {
                System.out.println ("idAnalysisint: " + idAnalysisint + " idRequestint: " + idRequestint);
            }

            if (r.getSequenceLanes().size() > 0) {
                for (Iterator i = r.getSequenceLanes().iterator(); i.hasNext(); ) {
                    SequenceLane sl = (SequenceLane) i.next();
                    AnalysisExperimentItem aei = new AnalysisExperimentItem();
                    aei.setIdRequest(idRequestint);
                    aei.setIdAnalysis(idAnalysisint);
                    aei.setIdSequenceLane(sl.getIdSequenceLane());

                    sess.save(aei);
                }

            } else if (r.getHybridizations().size() > 0) {
                for (Iterator i = r.getHybridizations().iterator(); i.hasNext(); ) {
                    Hybridization h = (Hybridization) i.next();
                    AnalysisExperimentItem aei = new AnalysisExperimentItem();
                    aei.setIdRequest(idRequestint);
                    aei.setIdAnalysis(idAnalysisint);
                    aei.setIdSequenceLane(h.getIdHybridization());
                    sess.save(aei);
                }

            } else {
                for (Iterator i = r.getSamples().iterator(); i.hasNext(); ) {
                    Sample s = (Sample) i.next();
                    AnalysisExperimentItem aei = new AnalysisExperimentItem();
                    aei.setIdRequest(idRequestint);
                    aei.setIdAnalysis(idAnalysisint);
                    aei.setIdSample(s.getIdSample());
                    sess.save(aei);
                }
            }

            sess.flush();
            tx.commit();

            sess.clear();

        } catch (Exception e) {

            System.out.println("ERROR: An exception has occurred in LinkExpToAnal " + e);
            e.printStackTrace();
        }
        System.out.println("normal exit -- no problems!");
        System.exit(0);
    }

    private static Date getWakeupTime() {
        Calendar tomorrow = new GregorianCalendar();
        tomorrow.add(Calendar.DATE, fONE_DAY);
        Calendar result = new GregorianCalendar(tomorrow.get(Calendar.YEAR), tomorrow.get(Calendar.MONTH),
                tomorrow.get(Calendar.DATE), wakeupHour, fZERO_MINUTES);
        return result.getTime();
    }

    private void connect() throws Exception {
        sess = dataSource.connect();
        if (sess == null) {
            System.out.println("[LinkExpToAnal] ERROR: Unable to acquire session. Exiting...");
            System.exit(1);
        }
    }

    private void disconnect() throws Exception {
        if (sess == null) {
            return;
        }

        sess.close();
    }
}
