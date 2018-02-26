package hci.gnomex.daemon;

import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.InternalAccountFieldsConfiguration;
import hci.gnomex.utility.*;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.internal.SessionImpl;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.*;

import hci.gnomex.constants.Constants;
import org.hibernate.query.Query;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.naming.NamingException;
import javax.persistence.criteria.CriteriaBuilder;

/**
 * Created by u0566434 on 3/15/2017.
 */
public class ExpireBillingAccount {


    private static long fONCE_PER_DAY = 1000 * 60 * 60 * 24; // A day in
    // milliseconds

    private static int fONE_DAY = 1;
    private static int wakeupHour = 2; // Default wakupHour is 2 am
    private static int fZERO_MINUTES = 0;

    private BatchDataSource dataSource;
    private Session sess;

    private String serverName;
    public static final Integer daysInFuture = 30;

    private ArrayList<String> waList;

    private PropertyDictionaryHelper propertyHelper;

    private static ExpireBillingAccount app;

    private Properties mailProps;

    private boolean runAsDaemon = false;

    private boolean testNoMailServer = false;

    private int numSent = 0;
    private int numSkipped = 0;

    private String orionPath = "";
    private String schemaPath = "";

    private String testEmailTo = "";

    public ExpireBillingAccount(String[] args){
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-server")) {
                serverName = args[++i];
            } else if (args[i].equals("-wakeupHour")) {
                wakeupHour = Integer.valueOf(args[++i]);
            } else if (args[i].equals("-runAsDaemon")) {
                runAsDaemon = true;
            } else if (args[i].equals("-testNoMailServer")) {
                testNoMailServer = true;
            } else if (args[i].equals("-orionPath")) {
                orionPath = args[++i];
            } else if (args[i].equals("-testEmailTo")) {
                testEmailTo = args[++i];
            } else if (args[i].equals("-schemaPath")) {
                schemaPath = args[++i];
            }
        }

        if (testNoMailServer) {
            mailProps = null;
        } else {
            try {
                mailProps = new BatchMailer(orionPath).getMailProperties();
            } catch (Exception e) {
                System.err.println("Cannot initialize mail properties");
                System.exit(0);
            }
        }
    }

    public static void main(String[] args) {
        app = new ExpireBillingAccount(args);
        // cron on linux machine will run this once at 2am
        app.run();
    }

    public void run(){



        try {
            Logger log = Logger.getLogger("org.hibernate");
            log.setLevel(Level.ERROR);

            dataSource = new BatchDataSource(orionPath, schemaPath);
            connect();

            InternalAccountFieldsConfiguration.reloadConfigurations(sess);
            propertyHelper = PropertyDictionaryHelper.getInstance(sess);

            Connection myConn = null;
            try {
                SessionImpl sessionImpl = (SessionImpl) sess;
                String strQuery = "";

                Map<Integer,String> cardType = new HashMap<Integer,String>();
                cardType.put(1,"American Express"); // order in db its stored
                cardType.put(2,"Visa");
                cardType.put(3,"Master Card");

                myConn = sessionImpl.connection(); // using this connection to determine what db is being used
                strQuery = "SELECT pd.idCoreFacility" +
                        " FROM PropertyDictionary as pd" +
                        " WHERE pd.propertyValue = 'Y' AND pd.propertyName = 'billing_account_exp_email'";

                Query query = sess.createQuery(strQuery);
                List coreFacilitiesList = query.list();

                strQuery = "SELECT l.idLab, l.contactEmail, l.billingContactEmail, ba.accountName," +
                        " ba.isCreditCard,ba.idCreditCardCompany, ba.isPO, ba.accountNumberBus," +
                        " ba.accountNumberOrg, ba.accountNumberFund, ba.accountNumberActivity," +
                        " ba.accountNumberProject, ba.accountNumberAccount, ba.expirationDate" +
                        " FROM Lab as l JOIN l.billingAccounts as ba" +
                        " WHERE ba.idBillingAccount  IN";
                String sqlServerQuery = " ( SELECT ba.idBillingAccount FROM BillingAccount as ba" +
                        " WHERE ba.idCoreFacility = :idCoreFacility" +
                        " AND ba.activeAccount = 'Y'" +
                        " AND Convert(date,DATEADD(day,:daysInFuture,CURRENT_TIMESTAMP)) = Convert(date,ba.expirationDate) )";

                String mySQLQuery = " ( SELECT ba.idBillingAccount FROM BillingAccount as ba" +
                        " WHERE ba.idCoreFacility = :idCoreFacility" +
                        " AND ba.activeAccount = 'Y'" +
                        " AND DATE_ADD(CURDATE(), INTERVAL :daysInFuture DAY) = DATE(ba.expirationDate) )";

                for (Iterator i = coreFacilitiesList.iterator(); i.hasNext(); ) {
                    Integer idCoreFacility = (Integer) i.next();

                    /**/

                    if (myConn.getMetaData().getDatabaseProductName().toUpperCase().indexOf(Constants.SQL_SERVER) >= 0) { // microsoft sql
                        System.out.println("sql Server");
                        sendExpiredAccountEmails(strQuery,sqlServerQuery,idCoreFacility,cardType);

                    } else {// Oracle
                        System.out.println("sql Server");
                        sendExpiredAccountEmails(strQuery,mySQLQuery,idCoreFacility,cardType);

                    }


                }
            }
            catch(Exception ex){
                System.out.println(ex.toString());
                ex.printStackTrace();
                throw new RollBackCommandException();
            }finally {
                if (myConn != null) {
                    try {
                        myConn.close();
                    } catch (SQLException e) {
                        System.out.println(e.toString());
                    }
                }
            }

            disconnect();
        }
        catch(Exception ex){
            System.out.println(ex.toString());
        }

        System.out.println ("Exiting...");
        System.exit(0);
    }

    private void sendExpiredAccountEmails(String strQuery, String sqlServerQuery, Integer idCoreFacility,Map<Integer,String> cardType) throws NamingException, MessagingException, IOException {
        List joinedExpireBillingAccounts = getExpiredBillingAccounts(strQuery, sqlServerQuery, idCoreFacility);
        for (int indx = 0; indx < joinedExpireBillingAccounts.size(); indx++) {

            Object[] row = (Object[]) joinedExpireBillingAccounts.get(indx);
            String contactEmail = (String)row[1];
            String billingEmail = (String)row[2];

            String allEmailAddresses = mergeEmailAddress(contactEmail,billingEmail);
            if(allEmailAddresses == null){
                continue;
            }

            String body = formatBillingAccountBody(row,cardType);

            if ( !testNoMailServer) {
                boolean testEmail = false;
                if (testEmailTo.length() > 0) {
                    testEmail = true;
                }
                MailUtilHelper helper = new MailUtilHelper(mailProps, allEmailAddresses, null, null, "DoNotReply@hci.utah.edu", "Expiring Billing Accounts", body, null, true, testEmail, testEmailTo);
                MailUtil.validateAndSendEmail(helper);
            }

        }
    }

    private String mergeEmailAddress(String PIemail, String billingEmail) {
        StringBuilder b = new StringBuilder();
        boolean validPI = false;
        boolean validBilling = false;

        if(PIemail != null){
            validPI = true;
        }
        if(billingEmail != null){
            validBilling = true;
        }


        if(validPI && validBilling){
            if(!(PIemail.equals(billingEmail))){
                b.append(PIemail);
                b.append(", ");
                b.append(billingEmail);
                return b.toString();
            }
            else{
                return PIemail; //could also return billingEmail since there the same
            }

        }

        if(validBilling){
            return billingEmail;
        }
        else if(validPI){
            return PIemail;
        }
        return null; // error don't email

    }

    public List getExpiredBillingAccounts(String query, String subQuery, Integer idCoreFacility){

        String completeQueryStr = query + subQuery;
        Query q = sess.createQuery(completeQueryStr)
                .setParameter("idCoreFacility", idCoreFacility)
                .setParameter("daysInFuture",daysInFuture);

        List mixTable = q.list();
        return mixTable;
    }
    public String formatBillingAccountBody(Object[] row, Map<Integer,String> cardType){
        StringBuilder body = new StringBuilder();
        StringBuilder table = new StringBuilder();

        String billingAccountType = "";
        String accountName = (String)row[3];
        String isCreditCard = (String)row[4];
        String isPO = (String)row[6];
        String expDate = row[13].toString();

        body.append("<html><head><title>Billing Account Status</title><meta http-equiv='content-style-type' content='text/css'></head>");
        body.append("<body leftmargin='0' marginwidth='0' topmargin='0' marginheight='0' offset='0' bgcolor='#FFFFFF'>");
        body.append("<style>.fontClass{font-size:11px;color:#000000;font-family:verdana;text-decoration:none;}");
        body.append(" .fontClassBold{font-size:11px;font-weight:bold;color:#000000;font-family:verdana;text-decoration:none;}");
        body.append(" .fontClassLgeBold{font-size:12px;line-height:22px;font-weight:bold;color:#000000;font-family:verdana;text-decoration:none;}</style>");
        body.append("<style>.fontClass{font-size:11px;color:#000000;font-family:verdana;text-decoration:none;}");
        body.append(" .fontClassBold{font-size:11px;font-weight:bold;color:#000000;font-family:verdana;text-decoration:none;}");
        body.append(" .fontClassLgeBold{font-size:12px;line-height:22px;font-weight:bold;color:#000000;font-family:verdana;text-decoration:none;}</style>");


        if(isCreditCard.equalsIgnoreCase("Y")){
            Integer idCardType = (Integer)row[5];
            String strCardType = cardType.get(idCardType) != null ? cardType.get(idCardType):"";
            billingAccountType = "Credit Card Account";

            table.append("<tr><td width='150'><span class='fontClassBold'>Card Type</span></td><td width='150'><span class='fontClassBold'>Expiration Date</span></td></tr>"); // header for table of the body
            table.append("<tr><td width='150'><span class='fontClass'>" + strCardType + "</span></td><td width='150'><span class='fontClass'>" + expDate + "</span></td></tr>");

           //cardType.get(idCardType) != null ? cardType.get(idCardType):""
        }
        else if (isPO.equalsIgnoreCase("Y")){
            table.append("<td width='150'><span class='fontClassBold'>Expiration Date</span></td></tr>");
            table.append("<td width='150'><span class='fontClass'>" + expDate  + "</span></td></tr>");
            billingAccountType = "PO Account";

        }
        else{ // CharterField
            String bus = (String)row[7];
            String org = (String)row[8];
            String fund = (String)row[9];
            String activity = (String)row[10];
            String project = (String)row[11];
            String acct = (String)row[12];
            billingAccountType = "Charter Account";

            table.append("<tr><td width='150'><span class='fontClassBold'>Bus</span></td>");
            table.append("<td width='150'><span class='fontClassBold'>Org</span></td>");
            table.append("<td width='150'><span class='fontClassBold'>Fund</span></td>");
            table.append("<td width='150'><span class='fontClassBold'>Activity</span></td>");
            table.append("<td width='150'><span class='fontClassBold'>Project</span></td>");
            table.append("<td width='150'><span class='fontClassBold'>Account</span></td>");
            table.append("<td width='150'><span class='fontClassBold'>Expiration Date</span></td></tr>");

            table.append("<tr><td width='150'><span class='fontClass'>" + bus + "</span></td>");
            table.append("<td width='150'><span class='fontClass'>" + org + "</span></td>");
            table.append("<td width='150'><span class='fontClass'>" + fund + "</span></td>");
            table.append("<td width='150'><span class='fontClass'>" + activity + "</span></td>");
            table.append("<td width='150'><span class='fontClass'>" + project + "</span></td>");
            table.append("<td width='150'><span class='fontClass'>" + acct + "</span></td>");
            table.append("<td width='150'><span class='fontClass'>" + expDate  + "</span></td></tr>");


        }

        body.append("<h2>"+billingAccountType+"</h2>");
        body.append("<p> \"" +accountName + "\"") ;
        body.append(" Billing Account is due to expire in 30 days </p>");
        body.append("<table cellpadding='5' cellspacing='0' border='1' bgcolor='#EBF2FC'>");
        body.append(table.toString());
        body.append("</table></body></html>");

        return body.toString();
    }



    private void connect()throws Exception{
        sess = dataSource.connect();
    }
    private void disconnect() throws Exception {
        dataSource.close();
    }




}
