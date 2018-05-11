package hci.gnomex.daemon.auto_import;



import hci.gnomex.utility.MailUtilHelper;

import java.io.FileNotFoundException;
import java.util.List;



public class DiffParser {

	public static void main(String[] args) throws FileNotFoundException {

		MailUtilHelper mailUtilHelper;
		// TODO Auto-generated method stub
		String local = "";
		String remote = "";
		String pathForOutput = "";
		Differ d = null;
		
		
		if (args.length == 2) { // Default location of output is the folder you are currently in
			local = args[0];
			remote = args[1];
			d = new Differ(remote,local);
		}
		else if(args.length == 3) { 
			local = args[0];
			remote = args[1];
			pathForOutput = args[2]; 
			d = new Differ(remote,local,pathForOutput);
		}
		else {
			System.out.println("Please provide the local textfile then the remote textfile");
			System.out.println("This parser accepts a file of filePaths, or a file generated from md5sum");
			System.out.println("Don't worry about telling the input type, it will figure that out at run time");
			
			System.exit(1);
		}
		
		d.findDifference();
		//d.writeDiffToFile(dList);	
		
	}
	

}
