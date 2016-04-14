package hci.gnomex.utility;


import hci.gnomex.constants.Constants;
import hci.gnomex.model.AppUser;
import hci.gnomex.model.BillingAccount;
import hci.gnomex.model.Lab;
import hci.gnomex.model.Product;
import hci.gnomex.model.ProductLineItem;
import hci.gnomex.model.ProductOrder;
import hci.gnomex.model.PropertyDictionary;

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
import java.util.Iterator;
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


  public static File saveReqFileFromURL(ProductOrder po, Session sess, String serverName) throws MalformedURLException, IOException, SAXException, InterruptedException {

    // Make sure correct directories are in place or create them.
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
    String createYear = formatter.format(po.getSubmitDate());

    String baseDir = PropertyDictionaryHelper.getInstance(sess).getProductOrderDirectory(serverName, po.getIdCoreFacility());
    baseDir +=  "/" + createYear;
    if (!new File(baseDir).exists()) {
      boolean success = (new File(baseDir)).mkdir();
      if (!success) {
        System.out.println("RequisitionFormUtil: Unable to create base directory " + baseDir);
      }
    }

    String directoryName = baseDir + "/" + po.getIdProductOrder();
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
    if ( fileList != null && fileList.length > 0 ) {
      String fileName = directoryName + File.separator + fileList[0];
      return new File(fileName);
    }

    // New requisition file:
    File reqFile = null;

    // Connect to the finance web site
    String reqURL  = PropertyDictionaryHelper.getInstance(sess).getCoreFacilityProperty(po.getIdCoreFacility(), PropertyDictionary.REQUISITION_FORM_URL);

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


    return reqFile;

  }

  // TODO: make the form fields properties && Finish all form fields
  public static File populateRequisitionForm(ProductOrder po, File reqFile, Session sess) throws IOException, DocumentException{

    if ( po == null || reqFile == null || reqFile.length() == 0 ) {
      return null;
    }

    AppUser user = (AppUser)sess.load(AppUser.class, po.getIdAppUser());
    Lab lab = (Lab)sess.load(Lab.class, po.getIdLab());
    BillingAccount ba = (BillingAccount)sess.load(BillingAccount.class, po.getAcceptingBalanceAccountId(sess));

    // Load the PDF file, get the form
    PdfReader reader = new PdfReader( reqFile.getCanonicalPath() );
    File temp = new File( FileUtils.getTempDirectoryPath(), reqFile.getName() );
    PdfStamper stamper = new PdfStamper( reader, new FileOutputStream( temp ) );
    AcroFields form = stamper.getAcroFields();

    // TODO Enter these as properties in the dictionary
    // Set form field values
    setField(form, "Req Date", new SimpleDateFormat("MM-dd-yyyy").format(System.currentTimeMillis()));
    setField(form, "Requesting Department", "HSC Core Research Facilities 'Genomics'");
    setField(form, "Deliver to Attention of", "Mike Klein");
    setField(form, "Delivery Phone Num", "585-2977");
    setField(form, "Bldg", "521");
    setField(form, "Room", "4A430");
    setField(form, "Ship to Code", "052104A430");
    setField(form, "Questions directed to", user != null ? user.getFirstLastDisplayName() : "");
    // Account information
    if ( ba != null ) {
      setField(form, "BU", ba.getAccountNumberBus() != null ? ba.getAccountNumberBus() : "");
      setField(form, "Org", ba.getAccountNumberOrg() != null ? ba.getAccountNumberOrg() : "");
      setField(form, "Fund", ba.getAccountNumberFund() != null ? ba.getAccountNumberFund() : "");
      setField(form, "Activity", ba.getAccountNumberActivity() != null ? ba.getAccountNumberActivity() : "");
      setField(form, "Project", ba.getAccountNumberProject() != null ? ba.getAccountNumberProject() : "");
      setField(form, "Account", ba.getAccountNumberAccount() != null ? ba.getAccountNumberAccount() : "");
      setField(form, "A/L", ba.getAccountNumberAu() != null ? ba.getAccountNumberAu() : "");
      setField(form, "Year", ba.getAccountNumberYear() != null ? ba.getAccountNumberYear() : "");
      setField(form, "Funding End Date", ba.getExpirationDate() != null ? new SimpleDateFormat("MM-dd-yyyy").format(ba.getExpirationDate()) : "" );
    }
    // TODO Add address to vendor object and use that instead of hard coding it here
    // Vendor information
    setField(form, "Vendor Name and Address", "Illumina");
    setField(form, "Vendor Name and Address 2", "5200 Illumina Way");
    setField(form, "Vendor Name and Address 3", "San Diego, CA 92122 USA");

    // Product line item information
    Integer totalQuantity = 0;
    BigDecimal grandTotal = new BigDecimal(BigInteger.ZERO, 2);
    Integer count = 0;
    for(Iterator i = po.getProductLineItems().iterator(); i.hasNext();) {
      ProductLineItem lineItem = (ProductLineItem) i.next();
      Product product = (Product)sess.load(Product.class, lineItem.getIdProduct());

      if( product != null ) {
        setField(form, "Quantity["+count+"]", lineItem.getQty().toString() );
        totalQuantity += lineItem.getQty();
        setField(form, "Description["+count+"]", product.getDisplay() + ", Illumina Catalog #: " + product.getCatalogNumber() );
        BigDecimal estimatedCost = new BigDecimal( BigInteger.ZERO, 2 ) ;
        estimatedCost = lineItem.getUnitPrice().multiply(new BigDecimal(lineItem.getQty()));
        setField(form, "Estimated Unit Price["+count+"]", lineItem.getUnitPrice().toString());
        grandTotal = grandTotal.add(estimatedCost);
      }
      count++;
    }

    setField(form, "Description[8]", "Please Reference \"" + lab.getLastName() + " Project\"");

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
