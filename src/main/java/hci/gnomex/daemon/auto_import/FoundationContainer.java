package hci.gnomex.daemon.auto_import;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FoundationContainer {

	private List<String> smallFilesList;
	private Map<String,String> largeFilesMap;

	public FoundationContainer() {
		smallFilesList = new ArrayList<String>();
		largeFilesMap = new HashMap<String,String>();
	}


	public List<String> getSmallFilesList() {
		return smallFilesList;
	}

	public String getLargeFileValue(String key) {
		return largeFilesMap.get(key);
	}





	public void addSmallFile(String smallFile){
		this.smallFilesList.add(smallFile);
	}
	public void addLargeFile(String key, String value) {	
			largeFilesMap.put(key, value);
			
	}



	public String loadCheckSum(String sumFile) throws Exception{


		FileReader reader = null;
		String line ="";

		try {
			reader = new FileReader(new File(sumFile));
			BufferedReader buffReader = new BufferedReader(reader);
			line = buffReader.readLine();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		} 
		finally{
			reader.close();
		}
		return line;

	}

	public void  writeCheckSumList(String fileName, String fileContent) {
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(new FileOutputStream(new File(fileName), true));
			pw.println(fileContent);
			pw.close();
		
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			pw.close();
		}

	}
	
	public void  writeSmallFilesList(String fileName, List<String> fileContent) {
		PrintWriter pw = null;
		
			try {
				pw = new PrintWriter(fileName);
				for(String content : fileContent) {
					pw.println(content);
				}
				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally {
				pw.close();
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
		finally{
			tempScript.delete();
		}
	}
	
	public void makeLocalCheckSums(String localChecksum,String localPath) {
		List<String>  commands = new ArrayList<String>();
		StringBuilder strBuild = new StringBuilder();
		int count = 0;
		
		
		for (Map.Entry<String, String> entry : largeFilesMap.entrySet()) {
		    String file = entry.getKey();
		    String fileChecksum = entry.getValue();
		    if(file != null && fileChecksum != null && !fileChecksum.equals("")) {
		    	System.out.println(file);
		    	strBuild.append("md5sum ");
		    	strBuild.append(localPath);
		    	strBuild.append(file);
		    	strBuild.append(" >> ");
		    	strBuild.append(localChecksum);
		    	strBuild.append(";");
		    	//strBuild.append(" rm ");
		    	//strBuild.append(fileChecksum);
		    	//strBuild.append(";");
		    	commands.add(strBuild.toString());
		    	strBuild = new StringBuilder();
		    	
		    }
		   
		}
		this.executeCommands(commands);
		
	}
	
	

}



