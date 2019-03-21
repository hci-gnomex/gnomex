package hci.gnomex.daemon.auto_import;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Differ {
	
	
	private String remote;
	private String local;
	private String outputPath;
	private Map<String,String> fileMap;
	private List<String> uniqueByName;
	private List<String> uniqueByChecksum;
	
	

	
	public Differ(String remote, String local){
		this.remote = remote;
		this.local = local;
		this.outputPath = "";
		this.fileMap = new TreeMap<String,String>();
		this.uniqueByName = new ArrayList<String>();
		this.uniqueByChecksum = new ArrayList<String>();
	}
	
	public Differ(String remote, String local,String outputPath){
		this.remote = remote;
		this.local = local;
		this.outputPath = outputPath;
		this.fileMap = new TreeMap<String,String>();
		this.uniqueByName = new ArrayList<String>();
		this.uniqueByChecksum = new ArrayList<String>();
	}
	
	
	public void findDifference(){
		Map<String,String> fileMap = new TreeMap<String,String>();
		List<String> uniqueFiles = new ArrayList<String>();
		BufferedReader buffReader = null;
		boolean simpleDiff = true;

		try {
			FileReader reader = new FileReader(new File(local));
			buffReader = new BufferedReader(reader);
			int BUFFER_SIZE = 1000;

			buffReader.mark(BUFFER_SIZE);
			String sampleLine = buffReader.readLine();
			buffReader.reset();

			simpleDiff = deterimineFileType(sampleLine);
			diffFile(buffReader,simpleDiff);
			
			if(!simpleDiff) {
				List<String> inclusionList = getInclusionList();
				writeDiffToFile(inclusionList,"inclusion.out" );
				writeDiffToFile(this.uniqueByChecksum,"uniqueByChecksum.out");
			}else {
				writeDiffToFile(this.uniqueByName);
			}



		} catch (IOException e) {
			e.printStackTrace();
		}catch (Exception e1) {
			System.out.print("The file name in the file list may not be different in the other file list. the file lists should not differ.\n "
					+ "The only exception is the checksums can differ " );
			System.out.println(e1.getMessage());
			System.exit(1);
		}
		finally {
			try {
				buffReader.close();
			} catch (IOException e) {
				System.out.println("Error: Unable to close file reading");
				e.printStackTrace();
			}
		}
		
	
		

	}
	
	private boolean deterimineFileType(String sampleLine) throws Exception{
		
		int len = (sampleLine.split("  ")).length;
		if(len == 1) {
			return true;
		}else if(len == 2) {
			return false;
		}else {
			throw new Exception("Can't determine file type in diffParser " + sampleLine );
		}
		
		
		
		
	}
	
	
	private void diffFile(BufferedReader buff, boolean simpleFileType) throws IOException {
		String[]  filePath = null;
		String[] fileAndChecksum = null;


		String line = "";
		String line1 = "";

		//
		while((line = buff.readLine()) != null) { // 1st file local 'put' into map

			if(!simpleFileType) {
				fileAndChecksum = line.split("  ");
				String checkSum = fileAndChecksum[0];

				filePath = fileAndChecksum[1].split("/");
				String fileName = filePath[filePath.length - 1];
				fileMap.put(fileName, checkSum);
			}else {
				String[] pathWithFile = line.split("/");
				String fileName = pathWithFile[pathWithFile.length -1];
				fileMap.put(fileName, line);

			}	

		}
		buff.close();


		buff = new BufferedReader(new FileReader(remote));


		while((line1 = buff.readLine()) != null) { // second file 'get' remote file
			String fileName = "";
			String remoteCheckSum = "";

			if(!simpleFileType) {
				fileAndChecksum = line1.split("  ");
				remoteCheckSum = fileAndChecksum[0];

				filePath = fileAndChecksum[1].split("/");
				fileName = filePath[filePath.length - 1];

				String localChecksum = fileMap.get(fileName);
				if(localChecksum != null){
					// not recording unique by name only by checksum.
					// I don't care about extra files in remoteList if there are any
					if(!localChecksum.equals(remoteCheckSum)) {
						uniqueByChecksum.add(fileName);
					}
				}

				
				
			}else {
				
				String[] pathWithFile = line1.split("/");
				fileName = pathWithFile[pathWithFile.length -1];
				
				if(fileMap.get(fileName) == null) { // diff
					uniqueByName.add(line1);
				}
			}
			

		}
	}
	
	public List<String> getInclusionList(){
		
		List<String>excludeByNameOrchecksumList = new ArrayList<String>();
		excludeByNameOrchecksumList.addAll(this.uniqueByName);
		excludeByNameOrchecksumList.addAll(this.uniqueByChecksum);
		List<String>inclusionList = new ArrayList<String>();
		
		
		for(int i=0; i < excludeByNameOrchecksumList.size(); i++) {
			this.fileMap.remove(excludeByNameOrchecksumList.get(i));
		}
		
		
		for(String key: this.fileMap.keySet()) {
			inclusionList.add(key);
		}
		return inclusionList;
		
		
	}

	public void writeDiffToFile(List<String> uniqueFiles){ // use if you want to do std.out
		PrintWriter writer = null;
		writer = new PrintWriter(System.out);

		for(String file: uniqueFiles) {
				System.out.println(file);
			}

		writer.close();
	}
	
	public void writeDiffToFile(List<String> uniqueFiles,String fileName){
		PrintWriter writer = null;

		try {
			writer = new PrintWriter(this.outputPath + fileName);
			for(String file: uniqueFiles) {
				writer.println(file);
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			writer.close();
		}
		
	}
	

	
	

}
