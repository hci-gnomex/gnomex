package hci.gnomex.daemon.auto_import;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.filter.Filters;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;

import java.io.*;

public class DeIdentifier {
    public  static void removePHI(String fileName){

        Document jdomDocument =  readXML(fileName);

        XPathFactory xFactory = XPathFactory.instance();
        Element root = jdomDocument.getRootElement();
        Element custInfo = root.getChild("CustomerInformation", Namespace.getNamespace("rr", "http://integration.foundationmedicine.com/reporting"));
        if(custInfo != null) {
            System.out.println(custInfo.removeChild("MRN", Namespace.getNamespace("rr", "http://integration.foundationmedicine.com/reporting")));


            Namespace defaultNs = Namespace.getNamespace("rr", "http://integration.foundationmedicine.com/reporting");
            XPathExpression<Element> expr = xFactory.compile("//rr:ResultsReport/rr:ResultsPayload//PMI", Filters.element(),null,defaultNs);
            Element parentPMI = expr.evaluateFirst(jdomDocument);


            System.out.println(parentPMI.removeChild("FirstName"));
            parentPMI.removeChild("LastName");
            System.out.println(parentPMI.removeChild("FullName"));
            parentPMI.removeChild("MRN");
            parentPMI.removeChild("DOB");
            parentPMI.removeChild("CollDate");
            parentPMI.removeChild("ReceivedDate");



            expr = xFactory.compile("//rr:ResultsReport/rr:ResultsPayload//Sample", Filters.element(),null,defaultNs);
            Element parentSample = expr.evaluateFirst(jdomDocument);
            System.out.println(parentSample.removeChild("ReceivedDate"));


            expr = xFactory.compile("//rr:ResultsReport/rr:ResultsPayload", Filters.element(),null,defaultNs);
            Element parentResultPayload = expr.evaluateFirst(jdomDocument);
            parentResultPayload.removeChild("ReportPDF");



            for(Element node : parentPMI.getChildren()) {
                System.out.println(node.getName());
            }
        }else {
            custInfo = root.getChild("CustomerInformation");
            System.out.println(custInfo.removeChild("MRN"));


            XPathExpression<Element> expr = xFactory.compile("//ResultsReport//PMI", Filters.element());
            Element parentPMI = expr.evaluateFirst(jdomDocument);


            System.out.println(parentPMI.removeChild("FirstName"));
            parentPMI.removeChild("LastName");
            System.out.println(parentPMI.removeChild("FullName"));
            parentPMI.removeChild("MRN");
            parentPMI.removeChild("DOB");
            parentPMI.removeChild("CollDate");
            parentPMI.removeChild("ReceivedDate");

            root.removeChild("ReportPDF");


            expr = xFactory.compile("//ResultsReport//Sample", Filters.element(),null);
            Element parentSample = expr.evaluateFirst(jdomDocument);
            System.out.println(parentSample.removeChild("ReceivedDate"));


            for(Element node : parentPMI.getChildren()) {
                System.out.println(node.getName());
            }

        }

        // has full path with file name
        String[] splitExtension = fileName.split("\\.");
        String fileNameNoExtension = splitExtension[0];
        writeXML(jdomDocument,fileNameNoExtension + ".deident.xml");


    }


    public static Document readXML(String inXMLFile){

        File inputFile = new File(inXMLFile);
        BufferedReader reader = null;
        Document doc = null;

        SAXBuilder saxBuilder = new SAXBuilder();
        try {

            reader = new BufferedReader( new FileReader(inputFile));

            saxBuilder = new SAXBuilder();
            doc = saxBuilder.build(reader);
            Element rootElement = doc.getRootElement();
            return doc;

        }
        catch(JDOMException e) {
            e.printStackTrace();
            try{
                reader.close();
            }catch(IOException ioExcept){
                System.out.print("Couldn't close the file reader");
            }

            System.exit(1);
        }
        catch(IOException e){
            e.printStackTrace();
            try{
                reader.close();
            }catch(IOException ioExcept){
                System.out.print("Couldn't close the file reader");
            }
            System.exit(1);
        }

        return doc;

    }


    private static void writeXML(Document doc, String fileName){
        PrintWriter writer = null;

        XMLOutputter outputter = new XMLOutputter();


        try {
            writer = new PrintWriter(fileName);

            outputter.setFormat(Format.getPrettyFormat());
            outputter.output(doc, writer);
        }catch (FileNotFoundException e) {
            e.printStackTrace();
            writer.close();
            System.exit(1);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            writer.close();
            System.exit(1);
        }
        finally {
            writer.close();
        }


    }
}
