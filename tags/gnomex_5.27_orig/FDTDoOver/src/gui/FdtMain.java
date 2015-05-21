package gui;

import javax.swing.UIManager;

import com.alee.laf.WebLookAndFeel;

public class FdtMain {

	/**Args are coming from a custom jnlp file with three arguments: serverIP, upload or download, serverDirectory*/
	public static void main(String[] args) {
		
		Request uploadRequest = new Request("bioserver.hci.utah.edu", "/scraXXXXtch/fdtswap/fdt_sandbox_gnomex/ab8f8e63-d0eb-4dad-901c-397d1797a851/A1664", true);
		
		
		try {
		    UIManager.setLookAndFeel ( WebLookAndFeel.class.getCanonicalName () );
		}
		catch ( Throwable e ){
		    System.err.println("Something went wrong attempting to set the WebLookAndFeel! Aborting!\n");
		    e.printStackTrace();
		    System.exit(1);
		}
		
		new FDTGui (uploadRequest);
		
		/*
		
		if (args.length != 3){
			System.err.println("\nError: must provide three arguments: serverIPAddress, upload or download, and the serverDirectory.\n");
			System.exit(1);
		}
		String serverIPAddress = args[0].toLowerCase();
		String type = args[1].toLowerCase();
		String serverDirectory = args[2];
		if (type.equals("upload")) new UploadRequest(serverIPAddress, serverDirectory);
		else if (type.equals("download")) new DownloadRequest(serverIPAddress, serverDirectory);
		else {
			System.err.println("\nError: please indicate if you want to 'upload' or 'download' data.\n");
			System.exit(1);
		}
		*/
	}

}
