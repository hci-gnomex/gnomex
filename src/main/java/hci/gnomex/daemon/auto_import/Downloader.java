package hci.gnomex.daemon.auto_import;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.sql.Timestamp;



public class Downloader {
	
	
	private Map<String,String>fileNameList;
	private String dependentDataPath;
	private String downloadPath;
	private final String rootAvatar = "HCI_Molecular_Data:/";
	private List<String> flaggedFileList;
	
	Downloader(String dependentDataPath , String downloadPath ){
		this.dependentDataPath = dependentDataPath;
		this.downloadPath = downloadPath;
		this.fileNameList = new TreeMap<String,String>();
		this.flaggedFileList = new ArrayList<String>();
		
	}
	
	private void executeCommands(List<String> commands) {

		File tempScript = null;
		
		try {
			System.out.println("started executing command");
			tempScript = createTempScript(commands);
			ProcessBuilder pb = new ProcessBuilder("bash", tempScript.toString());
			pb.inheritIO();
			Process process;
			process = pb.start();
			process.waitFor();
			System.out.println("finished executing command");
		}

		catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		finally

		{
			tempScript.delete();
		}
	}
	
	
	

	private File createTempScript(List<String> commands) throws IOException {
		File tempScript = File.createTempFile("script", null);

		Writer streamWriter = new OutputStreamWriter(new FileOutputStream(tempScript));
		PrintWriter printWriter = new PrintWriter(streamWriter);
		
		
		for(int i =0; i < commands.size(); i++) {
			printWriter.println(commands.get(i));
		}
		
		printWriter.close();

		return tempScript;
	}
	
	public void executeAvatarDownload() {
		
		List<String> commands = new ArrayList<String>();
		
		String parsedDownloadList = prepDownloadString(this.rootAvatar);
		
		if(parsedDownloadList.equals("")) { // downloading is in progress, no need to continue
			System.out.println("The process has been aborted because files are already downloading" );
			System.exit(3);
		}
		
		
		
		//["Downloading in Progress..."]
		
		List<String> status = Arrays.asList("Downloading in progress...");
		writeToFile(this.dependentDataPath + "download.log",status); // /home/u0566434/parser_data/download.log
		
		
		// Execute download actually
		String downloadCommand = "dx download " + parsedDownloadList;
		//dx download "project-xxxx:/my_folder/example.bam"
		System.out.println("These command for files to be downloaded: " + downloadCommand);
		
		commands.add("#!/bin/bash");
		commands.add("cd " + this.downloadPath);
		commands.add("mv -vn -t ./ " + this.downloadPath+ "/Flagged/*" );
		commands.add(downloadCommand);

		//commands.add("sleep 40s");
		//commands.add("wget /dev/null http://speedtest.dal01.softlayer.com/downloads/test100.zip");
	
		executeCommands(commands);
		
		List<String> reDownloadList = Arrays.asList("SL8834234");
		//boolean dSuccess = downloadSuccesful(reDownloadList);
		
		// determine if download successful && make log
		if(!true) {
			System.out.println("One or more downloads failed, and will be requeued for downloading.");
			writeToFile(this.dependentDataPath + "uniqueFilesToDownload.txt",reDownloadList); ///home/u0566434/parser_data/uniqueFilesToDownload.txt
			// need to limit  fileNameList
		}else {
			
			System.out.println("The dowload was a success!!!!!!");
			ArrayList<String> downloadedList = new ArrayList<String>();
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
	    
			
			downloadedList.add("Dowloaded successfully, " + timestamp );
			downloadedList.add(this.createFormattedPath("~/", true, true).replace("\"", ""));
			
			writeToFile(this.dependentDataPath + "download.log", downloadedList);
			
			
		}
			
	}
	
	private boolean downloadSuccesful(List<String> requeueList ) {
		
		String sumNewFiles = this.dependentDataPath + "tempNewFilesSum.out";
		String sumRemoteFiles = this.dependentDataPath + "FakeSum.out";
		
		
		List<String> commands = new ArrayList<String>();
		commands.add("md5sum " + this.downloadPath + "* > " + sumNewFiles);
		executeCommands(commands);
		
		Differ d = new Differ("remote","tempNewFilesSum.out");
		d.findDifference();
		
		
		
		//call a subprocess(perform checksum on downloaded files and compare to )
		//
		return false;
	}
	

	
	private void executeNewFilesList(List<String> requeueList ) {
		List<String> commands = new ArrayList<String>();
		
		if(requeueList.size() > 0 ) {
			
		}
		else {
		
			
		}
		
		executeCommands(commands);
		
	}

	public void loadFileNames(){
		
		
		FileReader reader = null;
		try {
			reader = new FileReader(new File(this.dependentDataPath + "uniqueFilesToDownload.out"));
			BufferedReader buffReader = new BufferedReader(reader);
			
			String line = "";
			while((line = buffReader.readLine()) != null) {
				if(!line.trim().equals("")){
					String[] fullPath = line.split("/");
					String fileName = fullPath[fullPath.length - 1];
					fileNameList.put(fileName, line);

				}

			}
			if(fileNameList.size() < 1) {
				throw new Exception("Appears to be no new files to download");
			}

			File flaggedFolder = new File(this.downloadPath + "/Flagged/");
			for(File file: flaggedFolder.listFiles()){
				if(!file.isDirectory()){
					String fileName = this.fileNameList.remove(file.getName());
					if(fileName != null){
						System.out.println("Filtering out: " + fileName);
						this.flaggedFileList.add(fileName);
					}else{
						System.out.println("[downloader] This flagged file wasn't found: "  +  file.getName());
					}

				}
			}


			writeToFile(this.dependentDataPath + "uniqueFilesToDownload.out", new ArrayList<String>());
		
		
		}
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (Exception e) {
			System.out.println(e.getMessage());
			try{ reader.close(); } catch(IOException ioExcept){
				System.out.print("Couldn't close the file reader");
			}
			System.exit(2);
		}
		finally{
			try{ reader.close(); }catch(IOException ioExcept){
				System.out.print("Couldn't close the file reader");
			}
		}
		
	}
	
	
	
	public String prepDownloadString(String root) {
		String strPaths = "";
		boolean hasNewLines = false;
		boolean safe = downloadSafe();
		
		if (safe) {
			strPaths = createFormattedPath(root, hasNewLines, false);
		}

		// String fileNameArray = fileNameList.toString().replaceAll("[\\[,\\]]+", "");
		return strPaths;

	}
	
	private boolean downloadSafe() {
		boolean safe = false;
		PrintWriter writer = null;
		
		try {
			BufferedReader bf = new BufferedReader(new FileReader(this.dependentDataPath + "download.log")); /// home/u0566434/parser_data/download.log
			String line = bf.readLine();

			if (!line.equals("Downloading in progress...")) { // If downloading in progress it is not 'safe' to continue
				safe = true;
			}
			
		} catch (FileNotFoundException e) { // File hasn't been created so lets make it
			// TODO Auto-generated catch block
			try {
				writer = new PrintWriter(this.dependentDataPath +"download.log"); // /home/u0566434/parser_data/download.log
				writer.println("Initalized");
				safe = true;

			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if(writer != null) {
				writer.close();
			}
			
		}
		return safe;
		
	}

	public Map<String,String> getFileNameList() {
		return fileNameList;
	}
	
	public String createFormattedPath(String root, boolean hasNewLine, boolean afterDownload) {
		StringBuilder strBuild = new StringBuilder();
		int count = 0;
		
		for (Map.Entry<String, String> entry : fileNameList.entrySet()) {
			String pathWithFileName = entry.getValue();
			strBuild.append("\"");
			strBuild.append(root);
			String safeFileName = pathWithFileName.replaceAll(" ", "\\\\ ");
			strBuild.append(safeFileName);
			strBuild.append("\"");
			if(hasNewLine) {
				if(count < fileNameList.size() - 1) {
					strBuild.append("\n");
				}
			}
			else {
				if(count < fileNameList.size() - 1) {
					strBuild.append(" ");
				}
			}
		
			count++;
		}

		// flagged files added now, even though they weren't downloaded this attempt. Since they were in the past
		// The flagged files need to be check in db everytime to see if person data was updated  and can now be imported
		if(afterDownload){
			for( int i = 0; i <  this.flaggedFileList.size(); i++){
				String flaggedFileName =  this.flaggedFileList.get(i);
				strBuild.append("\"");
				String safeFileName = flaggedFileName.replaceAll(" ", "\\\\ ");

				strBuild.append(safeFileName);
				strBuild.append("\"");
				if(hasNewLine) {
					if(i < flaggedFileList.size() - 1) {
						strBuild.append("\n");
					}
				}
				else {
					if(i < flaggedFileList.size() - 1) {
						strBuild.append(" ");
					}
				}

			}

		}


		return strBuild.toString();
	}
	
	
	
	public List<String> readFile(String fileName) throws IOException{
		BufferedReader bf = null;
		List<String> dataFromFileList = new ArrayList(); 
		
		try {
			bf = new BufferedReader(new FileReader(fileName));
			String line = "";
			while((line= bf.readLine()) != null) {
				dataFromFileList.add(line);
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		finally {
			bf.close();
		}
		return dataFromFileList;
	}
	
	public void writeToFile(String fileName, List<String> dataToWrite) {
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(fileName);
			for (int i = 0; i < dataToWrite.size(); i++) {
				writer.println(dataToWrite.get(i));
			}
			
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}finally {
			writer.close();
		}
		
	}
	
	
	
	
}
