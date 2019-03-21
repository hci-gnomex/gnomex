package hci.gnomex.daemon;

// 02/07/2018	tim
// 01/30/2019   tim     fix hardwired 2018 year


import hci.gnomex.utility.BatchDataSource;
import hci.gnomex.utility.PropertyDictionaryHelper;

import java.io.*;
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

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.internal.SessionImpl;

public class LinkData extends TimerTask {

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
    private static LinkData app = null;

    private boolean runAsDaemon = false;

    private String baseExperimentDir;
    private String baseAnalysisDir;
    private Calendar asOfDate;
    private Calendar runDate; // Date program is being run.

    private Boolean debug = false;
    private Boolean testConnection = false;

    private String dataType = "";
    private int MAXREQUESTS = 5000;
    private String[] requestList = new String[MAXREQUESTS];
    private int nxtRequest = 0;

    private String errorMessageString = "Error in LinkData";


    // NOTE: -requests must be the last argument
    public LinkData(String[] args) {
        nxtRequest = 0;
        int i = -1;
        while (i < args.length) {
            i++;
            if (i >= args.length) {
                break;
            }

            if (args[i].equals("-dataSource")) {                // i.e., 2R (Foundation), 4R (Avatar), 10R (Tempus)
                i++;
                if (i >= args.length) {
                    System.out.println ("-dataSource must be followed by 2R 4R or 10R.");
                    System.exit(1);
                }
                dataType = args[i];
            } else if (args[i].equals("-debug")) {
                debug = true;
            } else if (args[i].equals("-requests")) {
                // get them all
                while (true) {
                    i++;
                    if (i >= args.length || i >= MAXREQUESTS) {
                        break;
                    }
                    requestList[nxtRequest] = args[i];
                    System.out.println("[LinkData] i: " + i + " request: " + requestList[nxtRequest]);
                    nxtRequest++;
                }

                break;
            }
        }
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        app = new LinkData(args);
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

            if (dataType == null) {
                System.out.println("-dataSource is required");
                System.out.println("Usage: sh ./LinkData.sh -dataSource (either 2R 4R or 10R) -requests idRequest [list as many as you want]");
                System.exit(1);
            }

            if (nxtRequest == 0) {
                System.out.println("-requests is required");
                System.out.println("Usage: sh ./LinkData.sh -dataSource (either 2R 4R or 10R) -requests idRequest [list as many as you want]");
                System.exit(1);
            }

            // do the work
            app.linkData();

            app.disconnect();
            System.out.println("Exiting...");
            System.exit(0);

        } catch (Exception e) {

            String msg = "The following error occured: " + e.toString() + "\n";
            System.out.println(msg);

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

    private void linkData() throws Exception {

        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        String startPath = "/Repository/PersonData/" + currentYear + "/";
        String startAvatarPath = "/Repository/PersonData/2017";

        StringBuilder buf = new StringBuilder();

        // deal with each request
        int nxtOne = 0;
        while (nxtOne < nxtRequest) {
            System.out.println("[linkData] processing request: " + requestList[nxtOne]);

            Statement stmt = null;
            ResultSet rs = null;

            SessionImpl sessionImpl = (SessionImpl) sess;
            Connection con = sessionImpl.connection();

            stmt = con.createStatement();

            buf = new StringBuilder("select name from Sample where idRequest = ");
            buf.append(requestList[nxtOne] + ";");
            if (debug) System.out.println("ExperimentFile query: " + buf.toString());

            List<String> names = new ArrayList<String>();


            rs = stmt.executeQuery(buf.toString());
            while (rs.next()) {
                names.add(rs.getString(1));
            }
            rs.close();
            stmt.close();

            if (debug) System.out.println("size of names: " + names.size());

            // now using the list of sample 'names' see if we can find any matching experiment files
            Iterator it = names.iterator();

            while (it.hasNext()) {
                String theName = (String) it.next();

                stmt = con.createStatement();

                StringBuilder buf1 = new StringBuilder("select filename from ExperimentFile where filename like '");
                buf1.append(dataType + "%" + theName + "%';");
                if (debug) System.out.println("ExperimentFile query: " + buf1.toString());

                List<String> pathnames = new ArrayList<String>();

                rs = stmt.executeQuery(buf1.toString());
                while (rs.next()) {
                    pathnames.add(rs.getString(1));
                }
                rs.close();
                stmt.close();

                if (debug) System.out.println("size of pathnames: " + pathnames.size());
                // now using the list of sample 'names' see if we can find any matching experiment files
                Iterator itp = pathnames.iterator();

                //  create the directory
                String dirPath = startPath + requestList[nxtOne] + "R";
                if (debug) System.out.println("dirPath: " + dirPath);
                File f = new File(dirPath);
                f.mkdir();

                String myStartPath = dirPath;
                while (itp.hasNext()) {
                    String thePath = (String) itp.next();        // for example: 4R/Whole_Exome/FASTq/SL278299_2.fastq.gz

                    int ipos = thePath.indexOf("/");
                    if (ipos == -1) {
                        // bad path
                        // complain....
                        continue;
                    }
                    int epos = thePath.lastIndexOf("/");
                    if (epos <= ipos) {
                        // that's weird
                        continue;
                    }

                    String middleOfPath = thePath.substring(ipos + 1, epos);
                    if (debug) System.out.println("middleOfPath: " + middleOfPath);

                    String filename = thePath.substring(epos + 1);
                    if (debug) System.out.println("filename: " + filename);

                    String myPath = dirPath + "/" + middleOfPath;
                    if (debug) System.out.println("myPath: " + myPath);

                    f = new File(myPath);
                    f.mkdirs();

                    //  make the soft link
                    myPath = myPath + "/" + filename;
                    String pathToRealData = startAvatarPath + "/" + thePath;
                    File target = new File(pathToRealData);
                    File linkName = new File(myPath);
                    if (debug) System.out.println("[LinkData] right before makeSoftLinks, target: " + pathToRealData + "\n\t\t\t\t linkName: " + linkName);

                    boolean ok = makeSoftLinks(target, linkName);
                    if (!ok) {
                        System.out.println("makeSoftLinks failed!");
                    }
                } // end of inner while

            } // end of outer while

            nxtOne++;

        } // end of requestList while
        System.out.println("normal exit -- no problems!");
        System.exit(0);
    }

    /**
     * Makes a soft link between the realFile and the linked File using the linux 'ln -s' command.
     */
    public static boolean makeSoftLinks(File realFile, File link) {
        try {
//			String[] cmd1 = { "rm", "-f", link.toString() };
//			Runtime.getRuntime().exec(cmd1);
            String[] cmd = {"ln", "-s", realFile.getAbsolutePath(), link.toString()};
            Runtime.getRuntime().exec(cmd);
            return true;
        } catch (IOException e) {

        }
        return false;
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
            System.out.println("[LinkData] ERROR: Unable to acquire session. Exiting...");
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
