package hci.gnomex.daemon.auto_import;

public class XMLParserMain {

	public static void main(String[] args)  {
		// USAGE slID.out request.xml projectRequestList.xml experiment_importer.sh tempRequestXML.xml
		//Query q = new Query();
		//q.initalizeConnection();
		XMLParser parse = new XMLParser(args);
		try {
			parse.parseXML();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
