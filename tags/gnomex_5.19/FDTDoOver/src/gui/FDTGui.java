package gui;

import gui.FileDrop.Listener;

import javax.swing.*;

import com.alee.extended.filechooser.FilesSelectionListener;
import com.alee.extended.filechooser.WebFileDrop;
import com.alee.extended.panel.BorderPanel;
import com.alee.extended.panel.GroupPanel;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.rootpane.WebFrame;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.splitpane.WebSplitPane;
import com.alee.laf.text.WebTextArea;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.LinkedHashSet;
import java.util.regex.Pattern;
import java.util.List;

public class FDTGui  {
	
	//fields
	private static final long serialVersionUID = 1L;
	private Request request;
	private String transferDirection = "upload";
	private File fdtJar;
	private FDTExecutor fdtExecutor = null;
	private static final Pattern tab = Pattern.compile("\\t");
	private int margin = 5;
	
	//File transfer area
	private String fileTransferText = "Drop files & folders here";
	private WebFileDrop webFileDropArea;
	//Action button
	private JButton button;
	private String buttonStart = "Start";
	private String buttonAbort = "Abort";
	private String buttonExit = "Exit";
	//Status label
	private JLabel statusLabel;
	//Log area
	private JTextArea logArea;
	private WebSplitPane splitPane;

	
	//constructors	
	public FDTGui(Request request) {
		this.request = request;
		if (request.isUpload()) transferDirection = "upload";
		else transferDirection = "download";
		
		 // Split
        splitPane = new WebSplitPane ( WebSplitPane.VERTICAL_SPLIT, createFileDropArea(), createLog() );
        splitPane.setOneTouchExpandable ( true );
        splitPane.setPreferredSize ( new Dimension ( 400, 200 ) );
        splitPane.setDividerLocation ( 100 );
        splitPane.setContinuousLayout ( false );
        splitPane.setResizeWeight(0.5);
        
		
		//Schedule a job for the event-dispatching thread:
		//creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				//Turn off metal's use of bold fonts
				UIManager.put("swing.boldMetal", Boolean.FALSE);
				createAndShowGUI();
			}
		});
		
		//check to see if another FDT session is running
		checkPort();
		
		//look for fdt.jar app
		checkFDTJar();

		//good to go!
		webFileDropArea.setFilesDropEnabled(true);
		
		
		
	}
	/**Looks for fdt.jar in ~/.FDTGui directory, if not found downloads it.*/
	private void checkFDTJar() {
		appendLog("Checking fdt.jar...");
		File home = new File(System.getProperty("user.home"));
		File fdtAppDir = new File (home,".FDTGui");
		if (fdtAppDir.exists() == false) fdtAppDir.mkdir();
		fdtJar = new File (fdtAppDir, "fdt.jar");
		if (fdtJar.exists() == false){
			try {
				appendLog("   Downloading jar file");
				URL website = new URL("http://monalisa.cern.ch/FDT/lib/fdt.jar");
				ReadableByteChannel rbc = Channels.newChannel(website.openStream());
			    FileOutputStream fos = new FileOutputStream(fdtJar);
			    fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
			    appendLog("   Ready");
			} catch (Exception e) {
				this.appendLog("Error: could not download fdt.jar from http://monalisa.cern.ch ?!\n\nAboring\n");
				this.appendLog(e.getMessage());
				sleepExit(2000);
			}
		}
	}

	private void checkPort(){
		appendLog("Checking port...");
		ServerSocket serverSocket = null;
		boolean sleep = false;
		try {
		    serverSocket = new ServerSocket(request.getPort());
		} catch (IOException e) {
		    appendLog("\nError: port "+ request.getPort() +" is being used?!\nAre you running another FDT transfer?\n\nAborting!");
		    sleep = true;
		} finally {
			if (serverSocket != null){
				try {
					serverSocket.close();
				} catch (Exception e) {}
				if (sleep) sleepExit(2000);
			}
		}
	}
	
	private void sleepExit(int milSec){
		try {
			Thread.sleep(milSec);
			System.exit(1);
		} catch (Exception e) {}
	}

	private WebScrollPane createLog() {
		
		//create the text area
		logArea = new WebTextArea();
		logArea.setEditable(true);
		logArea.setToolTipText("Logging info");
		logArea.setLineWrap(false);
		
		//create a scroll pane and panel
		WebScrollPane scrollPane = new WebScrollPane(logArea);
		return scrollPane;
	}
	
	/**Appends the text plus a line return and sets the view to the last line.*/
	public void appendLog(String x){
		logArea.append(x);
		logArea.append("\n");
		logArea.setCaretPosition(logArea.getDocument().getLength());
	}

	private WebScrollPane createFileDropArea(){
		 // Simple file drop area
        webFileDropArea = new WebFileDrop();
        webFileDropArea.setDropText(fileTransferText);
        webFileDropArea.setShowFileExtensions(true);
        webFileDropArea.setFilesDropEnabled(false);
        
        //add listener to drop area
        FilesSelectionListener fdl = new FilesSelectionListener() {
			public void selectionChanged(List<File> files ){
				//button.setEnabled(true);
				System.out.println("Dropped file!");
			} 
		};
        webFileDropArea.addFileSelectionListener(fdl);

        // File drop area scroll
        WebScrollPane webFilesDropScroll = new WebScrollPane ( webFileDropArea );
        //webFilesDropScroll.setPreferredSize ( new Dimension ( 400, 100 ) );
      
        return webFilesDropScroll;
        
        /*
		//make inactive button
		button = new JButton(buttonStart);
		button.addActionListener(this);
		button.setEnabled(false);
		
		//make jlabel for status info
		statusLabel = new JLabel("");
		JPanel dropPanel = new JPanel();
		dropPanel.add(statusLabel,BorderLayout.LINE_START);
		dropPanel.add(button,BorderLayout.LINE_END);
        
        

        
		//creating master panel adding the scroll pane on top and the JCombo on bottom
        WebPanel masterPanel = new WebPanel(new BorderLayout());
		masterPanel.add(webFilesDropScroll, BorderLayout.CENTER);
		masterPanel.add(dropPanel, BorderLayout.SOUTH);
		//masterPanel.setBorder(BorderFactory.createTitledBorder("Files to "+transferDirection));
		masterPanel.setDrawLeft(true);
		return masterPanel;
		*/
	}
	
	/*
	private JPanel createFileTransferArea() {
		
		//create the text area
		fileTransferArea = new JTextArea();
		fileTransferArea.setEditable(true);
		fileTransferArea.setToolTipText("Drag & drop here");
		fileTransferArea.setLineWrap(true);
		
		//create a scroll pane and panel
		JScrollPane scrollPane = new JScrollPane(fileTransferArea);
		scrollPane.setPreferredSize(new Dimension(400,100));
		
		//create FileDrop with listener
		Listener fdl = new FileDrop.Listener() {
			public void filesDropped(File[] files ){   
				// handle file drop
				for (File f: files){
					try {
						//first drop? clear contents
						if (firstDrop) {
							fileTransferArea.setText("");
							firstDrop = false;
							button.setEnabled(true);
						}
						fileTransferArea.append(f.getCanonicalPath());
						fileTransferArea.append("\n");
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}   // end filesDropped
		};
		FileDrop fd =  new FileDrop( scrollPane, fdl); 

		//make inactive button
		button = new JButton(buttonStart);
		button.addActionListener(this);
		button.setEnabled(false);
		
		//make jlabel for status info
		statusLabel = new JLabel("");
		JPanel dropPanel = new JPanel();

		dropPanel.add(statusLabel,BorderLayout.LINE_START);
		dropPanel.add(button,BorderLayout.LINE_END);

		//creating master panel adding the scroll pane on top and the JCombo on bottom
		JPanel masterPanel = new JPanel(new BorderLayout());
		masterPanel.add(scrollPane, BorderLayout.CENTER);
		masterPanel.add(dropPanel, BorderLayout.SOUTH);
		masterPanel.setBorder(BorderFactory.createTitledBorder("Files to "+transferDirection));
		return masterPanel;
	}*/
	
	public LinkedHashSet<String> parseFilesToUpload(){
		LinkedHashSet<String> filesToTransfer = new LinkedHashSet<String>();
		try {
			List<File> files = webFileDropArea.getSelectedFiles();
			for (File f: files){
				String pathName = f.getCanonicalPath();
				if (f.canRead()) {
					//don't add duplicates
					if (filesToTransfer.contains(pathName) == false){
						filesToTransfer.add(pathName);
						appendLog("   OK : "+ pathName);
					}
				}
				else appendLog("Can't read or find : "+ pathName);
			}
		} catch (Exception e){
			e.printStackTrace();
		}
		return filesToTransfer;
	}

	public void actionPerformed(ActionEvent ae) {
		String buttonLabel = button.getText();
		//start transfer
		if (buttonLabel.equals(buttonStart)){
			button.setEnabled(false);
			appendLog("Checking files...");
			
			//parse file list
			LinkedHashSet<String> files = parseFilesToUpload();
			if (files.size() == 0){
				button.setEnabled(true);
				appendLog("\nERROR: No files or folders found to upload?! Correct and try again.");
			}
			
			//build fdt cmd
			appendLog("Executing "+transferDirection+"...");
			String[] cmd = fetchUploadCmd(files);
			StringBuilder sb = new StringBuilder();
			for (String c :cmd){
				sb.append(c);
				sb.append(" ");
			}
			appendLog("   "+sb.toString()+"\n");
			
			//set for aborting
			button.setText(buttonAbort);
			button.setEnabled(true);
			
			//execute fdt thread, this completes itself 
			statusLabel.setText("Transferring data...  ");
			fdtExecutor = new FDTExecutor(cmd);
			fdtExecutor.start();
		}
		//exit after completing transfer or aborting
		else if (buttonLabel.equals(buttonExit)){
			System.exit(0);
		}
		//must have hit abort!
		else {
			appendLog(buttonLabel+"ing fdt transfer...");		
			fdtExecutor.interrupt();
		}
	}
	
	private class FDTExecutor extends Thread {

		String[] command;
		
		public FDTExecutor (String[] command){
			this.command = command;
		}
		
		public void run () {	
			//0 fail, 1 OK, 2 aborted
			int transferStatus = 0; 
			try {
				Runtime rt = Runtime.getRuntime();
				rt.traceInstructions(true); //for debugging
				rt.traceMethodCalls(true); //for debugging
				Process p = rt.exec(command);
				BufferedReader data = new BufferedReader(new InputStreamReader(p.getInputStream()));
				BufferedReader error = new BufferedReader(new InputStreamReader(p.getErrorStream())); //for debugging
				String exitStatus = null;
				String line;
				//logging
				appendLog("FDT Logging:");
				while ((line = data.readLine()) != null){
					//interrupted?
					if (interrupted()) {
						transferStatus = 2;
						p.destroy();
						break;
					}					
					line = line.trim();
					if (line.length() != 0) {
						appendLog(line);
						if (line.contains("Avg: ")) statusLabel.setText("Transferring "+parseUploadLineForAve(line));
						else if (line.startsWith("Exit Status:")) exitStatus = line;
					}
				}
				//check exit status, if not good then print error
				if (exitStatus.contains("Not OK")){
					transferStatus = 0;
					appendLog("\nERROR in executing FDT:");
					while ((line = error.readLine()) != null){
						//interrupted?
						if (interrupted()) {
							transferStatus = 2;
							p.destroy();
							break;
						}
						line = line.trim();
						if (line.length() != 0) appendLog(line);
					}
				}
				else transferStatus = 1;
				
				data.close();
				error.close();

			} catch (Exception e) {
				appendLog(e.getMessage());
				return;
			} finally {
				//failed
				if (transferStatus == 0){
					statusLabel.setText("Transfer failed, see log! ");
					statusLabel.setForeground(Color.red);
					appendLog("Correct issue(s) and restart.");
				}
				//ok
				else if (transferStatus == 1){
					button.setText(buttonExit);
					statusLabel.setText("Transfer complete, safe to -> ");
					statusLabel.setForeground(new Color(34, 139, 34));
				}
				//interrupted
				else {
					button.setText(buttonExit);
					statusLabel.setText("Transfer interrupted, safe to -> ");
					statusLabel.setForeground(Color.blue);
				}
			}
		}
	}
	
	public String parseUploadLineForAve(String line){
		String[] tokens = tab.split(line);
		return tokens[2].substring(4);
	}
	
	/*java -jar path2YourLocalCopyOfFDT/fdt.jar \
	-noupdates \
	-c bioserver.hci.utah.edu \
	-d /scratch/fdtswap/fdt_sandbox_gnomex/2d446fc7-0f81-4294-bbd6-5d6d80bed2d7/A1463 \
	-r \
	list of full path files and directories  (will recurse through)
	 */
	private String[] fetchUploadCmd(LinkedHashSet<String> files) {
		String[] uploadCmd = new String[files.size() + 9];
		uploadCmd[0]="java";
		uploadCmd[1]="-jar";
		uploadCmd[2]=fdtJar.toString();
		uploadCmd[3]="-noupdates";
		uploadCmd[4]="-c";
		uploadCmd[5]=request.getServerIPAddress();
		uploadCmd[6]="-d";
		uploadCmd[7]=request.getServerDirectory();
		uploadCmd[8]="-r";
		int index = 9;
		for (String path : files) uploadCmd[index++] = path;
		return uploadCmd;
	}
	
	/**
	 * Create the GUI and show it.  For thread safety,
	 * this method should be invoked from the
	 * event-dispatching thread.
	 */
	private void createAndShowGUI() {
		//Create and set up the window.
		WebFrame frame = new WebFrame("FDT Uploader");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.setResizable(true);
		frame.getContentPane ().add ( new BorderPanel ( splitPane, margin ) );
		
		//Display the window
		frame.pack();
		frame.center();
		frame.setVisible(true);
	}

}
