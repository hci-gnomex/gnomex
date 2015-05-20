package hci.gnomex.utility;


import hci.gnomex.constants.Constants;
import hci.gnomex.model.IScanChip;
import hci.gnomex.model.PropertyDictionary;
import hci.gnomex.model.Request;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.hibernate.Session;
import org.xml.sax.SAXException;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

/**
 * @author MBowler
 *
 */
public class RequisitionFormUtil {
  
  
  public static File saveReqFileFromURL(Request request, Session sess, String serverName) throws MalformedURLException, IOException, SAXException, InterruptedException {
    
    // Make sure correct directories are in place or create them.
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
    String createYear = formatter.format(request.getCreateDate());

    String baseDir = PropertyDictionaryHelper.getInstance(sess).getExperimentDirectory(serverName, request.getIdCoreFacility());
    baseDir +=  "/" + createYear;
    if (!new File(baseDir).exists()) {
      boolean success = (new File(baseDir)).mkdir();
      if (!success) {
        System.out.println("RequisitionFormUtil: Unable to create base directory " + baseDir);      
      }      
    }

    String directoryName = baseDir + "/" + Request.getBaseRequestNumber(request.getNumber());
    if (!new File(directoryName).exists()) {
      boolean success = (new File(directoryName)).mkdir();
      if (!success) {
        System.out.println("RequisitionFormUtil: Unable to create directory " + directoryName);      
      }      
    }

    directoryName += "/" + Constants.REQUISITION_DIR;

    File directory = new File(directoryName);
    if (!directory.exists()) {
      boolean success = directory.mkdir();
      if (!success) {
        System.out.println("RequisitionFormUtil: Unable to create directory " + directoryName);      
      }      
    }
    
    // Check to see if a requisition form is already in place for this request.
    String[] fileList = directory.list();
    if ( fileList.length > 0 ) {
      String fileName = directoryName + File.separator + fileList[0];
      return new File(fileName);
    }
    
    // New requisition file:
    File reqFile = null;
    
    // Connect to the finance web site
    String reqURL  = PropertyDictionaryHelper.getInstance(sess).getCoreFacilityProperty(request.getIdCoreFacility(), PropertyDictionary.REQUISITION_FORM_URL);
    
    if ( reqURL == null || reqURL.equals( "" ) ) {
      return reqFile;
    }
    
    URL url = new URL( reqURL );
    URLConnection conn = url.openConnection();
    BufferedReader in = new BufferedReader( new InputStreamReader( conn.getInputStream() ));
    // Find the redirect address
    String inputLine;
    while ((inputLine = in.readLine()) != null) {
      Pattern pattern = Pattern.compile("http(.+?)pdf");
      Matcher matcher = pattern.matcher(inputLine);
      if ( matcher.find() ) {
        url = new URL(matcher.group(0));
        // Get the name of the file
        Pattern numberPattern = Pattern.compile( "data/(.+?)pdf" );
        Matcher numberMatcher = numberPattern.matcher( inputLine );
        if ( numberMatcher.find() ) {
          reqFile = new File(directoryName, numberMatcher.group(1) + "pdf");
        } else {
          reqFile = new File(directoryName, "requisition.pdf");
        }
        // Copy the pdf from the url
        FileUtils.copyURLToFile( url, reqFile );
        break;
      }
    }
    in.close();
    
    FileUtils.copyURLToFile( url, reqFile );
    
//    return new File(directoryName, "114948.pdf"); // For testing purposes - comment out url call and just use this
    
    return reqFile;
    
  }
  
  // TODO: make the form fields properties && Finish all form fields
  public static File populateRequisitionForm(Request request, File reqFile, Session sess) throws IOException, DocumentException{

    if ( request == null || reqFile == null || reqFile.length() == 0 ) {
      return null;
    }

    IScanChip chip = (IScanChip) sess.get( IScanChip.class, request.getIdIScanChip() );

    // Load the PDF file, get the form
    PdfReader reader = new PdfReader( reqFile.getCanonicalPath() );
    File temp = new File( FileUtils.getTempDirectoryPath(), reqFile.getName() );
    PdfStamper stamper = new PdfStamper( reader, new FileOutputStream( temp ) );
    AcroFields form = stamper.getAcroFields(); 
    
    // Set form field values
    setField(form, "Req Date", new SimpleDateFormat("MM-dd-yyyy").format(System.currentTimeMillis()));
    setField(form, "Requesting Department", "HSC Core Research Facilities 'Genomics'");
    setField(form, "Deliver to Attention of", "Mike Klein");
    setField(form, "Delivery Phone Num", "585-2977");
    setField(form, "Bldg", "521");
    setField(form, "Room", "4A430");
    setField(form, "Ship to Code", "052104A430");
    setField(form, "Questions directed to", request.getAppUser() != null ? request.getAppUser().getFirstLastDisplayName() : "");
    // Account information
    if ( request.getBillingAccount() != null ) {
      setField(form, "BU", request.getBillingAccount().getAccountNumberBus() != null ? request.getBillingAccount().getAccountNumberBus() : "");
      setField(form, "Org", request.getBillingAccount().getAccountNumberOrg() != null ? request.getBillingAccount().getAccountNumberOrg() : "");
      setField(form, "Fund", request.getBillingAccount().getAccountNumberFund() != null ? request.getBillingAccount().getAccountNumberFund() : "");
      setField(form, "Activity", request.getBillingAccount().getAccountNumberActivity() != null ? request.getBillingAccount().getAccountNumberActivity() : "");
      setField(form, "Project", request.getBillingAccount().getAccountNumberProject() != null ? request.getBillingAccount().getAccountNumberProject() : "");
      setField(form, "Account", request.getBillingAccount().getAccountNumberAccount() != null ? request.getBillingAccount().getAccountNumberAccount() : "");
      setField(form, "A/L", request.getBillingAccount().getAccountNumberAu() != null ? request.getBillingAccount().getAccountNumberAu() : "");
      setField(form, "Year", request.getBillingAccount().getAccountNumberYear() != null ? request.getBillingAccount().getAccountNumberYear() : "");
      setField(form, "Funding End Date", request.getBillingAccount().getExpirationDate() != null ? new SimpleDateFormat("MM-dd-yyyy").format(request.getBillingAccount().getExpirationDate()) : "" );
    }
    // Vendor information
    setField(form, "Vendor Name and Address", "Illumina");
    setField(form, "Vendor Name and Address 2", "5200 Illumina Way");
    setField(form, "Vendor Name and Address 3", "San Diego, CA 92122 USA");
    // Chip information
    if( chip != null ) {
      setField(form, "Quantity[0]", request.getNumberIScanChips().toString() );
      setField(form, "Description[0]", chip.getDisplay() + " BeadChip, Illumina Catalog #: " + chip.getCatalogNumber() );
      BigDecimal estimatedCost = new BigDecimal( BigInteger.ZERO, 2 ) ;
      estimatedCost = chip.getCostPerSample().multiply( new BigDecimal(chip.getSamplesPerChip()) );
      setField(form, "Estimated Unit Price[0]", estimatedCost.toString());
    }
    setField(form, "Description[5]", "Please Reference \"" + request.getLab().getLastName() + " Project\"");
    
    // Save and close the PDF
    stamper.close();
    reader.close();

    FileUtils.copyFile( temp, reqFile );
    FileUtils.deleteQuietly( temp );
    
    return reqFile;
  }
  
  private static void setField(AcroFields form, String fieldName, String value) throws IOException, DocumentException {
    if ( form == null || fieldName == null || value == null ) {
      return;
    }
    form.setField( fieldName, value );
  }
  
 
  
}
