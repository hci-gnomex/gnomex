package hci.gnomex.daemon.auto_import;

import java.util.HashMap;
import java.util.List;

public class FileMover {

	public static void main(String[] args) {
		
		
		//USAGE:                           inFile, root, skip?
		String root = "";
		String inFileName = "";
		String currentDownloadLocation = "";
		String flaggedIDFileName= "";
		boolean skipFirstLine = false;
		DirectoryBuilder db = null;
		
		db = new DirectoryBuilder(args);
		
		 // "C:\\Users\\u0566434\\Desktop\\ORIEN\\Scripts\\");
		List<String> filesToMove = db.preparePath();
		db.moveTheFiles(filesToMove);

	}

}
