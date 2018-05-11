package hci.gnomex.daemon.auto_import;

public class DownloadMain {

	public static void main(String[] args) {
		
		try {
			String tempDataPath = "";
			String downloadPath = "";
			if (args.length == 2) {
				tempDataPath = args[0];
				downloadPath = args[1];
			}else {
				System.out.println("Please provide path to prep data, and then the download files path");
				System.exit(1);
			}
			
			Downloader d = new Downloader(tempDataPath,downloadPath);
			d.loadFileNames();
			d.executeAvatarDownload();
			//System.out.println(d.getFileNameList().toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		

	}

}
