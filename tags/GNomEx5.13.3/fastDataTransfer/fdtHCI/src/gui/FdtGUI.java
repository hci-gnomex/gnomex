/*
 * This is the main GUI class with declaration of all gui components.
 *
 *
 */

/*
 * FdtGUI.java
 *
 * Created on Nov 15, 2010, 3:56:25 PM
 */

package gui;

import java.io.File;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import lia.util.net.common.Config;
import lia.util.net.copy.FDT;


public class FdtGUI extends javax.swing.JFrame implements Runnable {

  private static FdtGUI ref;

  protected DefaultTableModel model;

  private String serverName;

  private long portNum;

  private boolean isUpload;

  protected ArrayList<File> files;

  private boolean isDirectory;

  private String destinationDir;

  private String initWaitMsg = "All files read. Waiting for things to finish up on the server...";

  public static Config config;

  private static Thread t;

  private WaitThread waitThread;
  private boolean waitThreadActive = false;

  private static Properties localProps = new Properties();

  private static GUILogger guiLogger = GUILogger.getGUILoggerInstance();

  private static LogPanel logPanelInstance = LogPanel.getLogPanelInstance();

  public static final String FDT_FULL_VERSION = "0.9.17-201008311824";

  public boolean isIsUpload() {
    return isUpload;
  }

  public void setIsDirectory(boolean isDirectory) {
    this.isDirectory = isDirectory;
  }

  public void setDestinationDir(String destinationDir) {
    this.destinationDir = destinationDir;
  }

  public void run() {
    try {
      createArguments();
      if(isUpload) {
        cancelButton.setEnabled(true);
        waitMessage.setVisible(false);
        exitWarning.setVisible(false);
        waitThreadActive = false;
      }
      return;

    } catch (Exception ex) {
      Logger.getLogger(FdtGUI.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  public void setWaitingForRemote() {
    waitMessage.setVisible(true);
    waitThread = new WaitThread();
    waitThreadActive = true;
    waitThread.start();
  }

  class WaitThread extends Thread {
    public void run() {
      while(waitThreadActive) {
        try {
          sleep(1000);
          String waitTxt = waitMessage.getText();
          waitTxt = waitTxt + ".";
          if(waitTxt.length() > initWaitMsg.length() + 20) {
            waitMessage.setText(initWaitMsg);
          } else {
            waitMessage.setText(waitTxt);
          }  
        } catch (InterruptedException e) {
        }
      }
    }
  }    


  public void setIsUpload(boolean isUpload) {
    this.isUpload = isUpload;
    if(isUpload) {
      setTitle("Upload files using FDT");
    } else {
      setTitle("Download files using FDT");          
    }
  }

  public void setPortNum(long portNum) {
    this.portNum = portNum;
  }

  public void setServerName(String serverName) {
    this.serverName = serverName;
  }


  /** Creates new form FdtGUI */
  private FdtGUI() {
    try
    {
      setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
    catch(Exception e)
    {
      guiLogger.logError(e);
    }

    serverName = "";

    t = null;

    portNum = 54321;

    isUpload = false;

    ref=null;

    destinationDir = null;

    isDirectory = false;

    String columns[] = {
        "From", "To", "% Completed", "ETA", "Current Status"
    };


    Object data[][]= new Object [0][5];

    model = new DefaultTableModel(data,columns){
      @Override
      public boolean isCellEditable(int row, int column) {
        return false;}
    };

    initComponents();

    TableColumn column = statusTable.getColumnModel().getColumn(0);
    column.setPreferredWidth(160);
    column.setMaxWidth(160);
    column.setCellRenderer(new MyTableCellRenderer());

    column = statusTable.getColumnModel().getColumn(1);
    column.setCellRenderer(new MyTableCellRenderer());

    column = statusTable.getColumnModel().getColumn(2);
    column.setPreferredWidth(50);
    column.setMaxWidth(150);
    column.setCellRenderer(new MyTableCellRenderer());

    column = statusTable.getColumnModel().getColumn(3);
    column.setPreferredWidth(100);
    column.setMaxWidth(150);
    column.setCellRenderer(new MyTableCellRenderer());


    column = statusTable.getColumnModel().getColumn(4);
    column.setCellRenderer(new MyTableCellRenderer());


    menuItemShowLogs.setState(false);

    files = new ArrayList<File>();

  }

  public static synchronized FdtGUI getFdtGUIInstance()
  {
    if (ref == null)

      ref = new FdtGUI();
    return ref;
  }

  /** This method is called from within the constructor to
   * initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is
   * always regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    jMenuBar2 = new javax.swing.JMenuBar();
    jMenu3 = new javax.swing.JMenu();
    jMenu4 = new javax.swing.JMenu();
    jFrame1 = new javax.swing.JFrame();
    jFrame2 = new javax.swing.JFrame();
    jFrame3 = new javax.swing.JFrame();
    startButton = new javax.swing.JButton();
    waitMessage = new javax.swing.JLabel();
    exitWarning = new javax.swing.JLabel();
    cancelButton = new javax.swing.JButton();
    selectFilesButton = new javax.swing.JButton();
    jScrollPane1 = new javax.swing.JScrollPane();
    statusTable = new javax.swing.JTable();
    menuBar = new javax.swing.JMenuBar();
    jMenu1 = new javax.swing.JMenu();
    menuItemShowLogs = new javax.swing.JCheckBoxMenuItem();
    menuItemExit = new javax.swing.JMenuItem();
    jMenu6 = new javax.swing.JMenu();
    menuItemHelp = new javax.swing.JMenuItem();
    menuItemAbout = new javax.swing.JMenuItem();

    jMenu3.setText("File");
    jMenuBar2.add(jMenu3);

    jMenu4.setText("Edit");
    jMenuBar2.add(jMenu4);

    org.jdesktop.layout.GroupLayout jFrame1Layout = new org.jdesktop.layout.GroupLayout(jFrame1.getContentPane());
    jFrame1.getContentPane().setLayout(jFrame1Layout);
    jFrame1Layout.setHorizontalGroup(
        jFrame1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(0, 400, Short.MAX_VALUE)
    );
    jFrame1Layout.setVerticalGroup(
        jFrame1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(0, 300, Short.MAX_VALUE)
    );

    org.jdesktop.layout.GroupLayout jFrame2Layout = new org.jdesktop.layout.GroupLayout(jFrame2.getContentPane());
    jFrame2.getContentPane().setLayout(jFrame2Layout);
    jFrame2Layout.setHorizontalGroup(
        jFrame2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(0, 400, Short.MAX_VALUE)
    );
    jFrame2Layout.setVerticalGroup(
        jFrame2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(0, 300, Short.MAX_VALUE)
    );

    org.jdesktop.layout.GroupLayout jFrame3Layout = new org.jdesktop.layout.GroupLayout(jFrame3.getContentPane());
    jFrame3.getContentPane().setLayout(jFrame3Layout);
    jFrame3Layout.setHorizontalGroup(
        jFrame3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(0, 400, Short.MAX_VALUE)
    );
    jFrame3Layout.setVerticalGroup(
        jFrame3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(0, 300, Short.MAX_VALUE)
    );

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    setMinimumSize(new java.awt.Dimension(300, 200));

    startButton.setText("Start Upload");
    startButton.setEnabled(false);
    startButton.setFocusable(false);
    startButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
    startButton.setMaximumSize(new java.awt.Dimension(121, 49));
    startButton.setMinimumSize(new java.awt.Dimension(90, 20));
    startButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    startButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        startButtonActionPerformed(evt);
      }
    });

    waitMessage.setVisible(false);
    waitMessage.setText(initWaitMsg);
    exitWarning.setVisible(false);
    exitWarning.setText("WARNING: Closing this window before the Exit button is enabled will abort the upload.");


    cancelButton.setText("Exit");
    cancelButton.setFocusable(false);
    cancelButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
    cancelButton.setMaximumSize(new java.awt.Dimension(75, 49));
    cancelButton.setMinimumSize(new java.awt.Dimension(60, 20));
    cancelButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    cancelButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        cancelButtonActionPerformed(evt);
      }
    });

    selectFilesButton.setText("Select Files");
    selectFilesButton.setFocusable(false);
    selectFilesButton.setMaximumSize(new java.awt.Dimension(114, 49));
    selectFilesButton.setMinimumSize(new java.awt.Dimension(90, 20));
    selectFilesButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    selectFilesButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        selectFilesButtonActionPerformed(evt);
      }
    });

    statusTable.setModel(model);
    statusTable.setRowHeight(20);
    statusTable.setSize(new java.awt.Dimension(450, 64));
    jScrollPane1.setViewportView(statusTable);

    jMenu1.setText("Options");

    menuItemShowLogs.setText("Show Transfer Logs");
    menuItemShowLogs.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        menuItemShowLogsActionPerformed(evt);
      }
    });
    jMenu1.add(menuItemShowLogs);

    menuItemExit.setText("Exit");
    menuItemExit.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        menuItemExitActionPerformed(evt);
      }
    });
    jMenu1.add(menuItemExit);

    menuBar.add(jMenu1);

    jMenu6.setText("Help");

    menuItemHelp.setText("Help");
    menuItemHelp.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        menuItemHelpActionPerformed(evt);
      }
    });
    jMenu6.add(menuItemHelp);

    menuItemAbout.setText("About");
    menuItemAbout.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        menuItemAboutActionPerformed(evt);
      }
    });
    jMenu6.add(menuItemAbout);

    menuBar.add(jMenu6);

    setJMenuBar(menuBar);

    org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);

    layout.setHorizontalGroup(
        layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(layout.createSequentialGroup()
            .addContainerGap()
            .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 700, Short.MAX_VALUE)
                .add(layout.createSequentialGroup()
                    .add(selectFilesButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 111, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(83, 83, 83)
                    .add(startButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 117, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 113, Short.MAX_VALUE)
                    .add(cancelButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 72, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(waitMessage)
                    .add(exitWarning))
                    .addContainerGap())
    );
    layout.setVerticalGroup(
        layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
            .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
            .add(8, 8, 8)
            .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(selectFilesButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(cancelButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(startButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(12, 12, 12)
                .add(waitMessage)
                .add(exitWarning)
                .addContainerGap())
    );          

    layout.linkSize(new java.awt.Component[] {cancelButton, selectFilesButton, startButton}, org.jdesktop.layout.GroupLayout.VERTICAL);

    pack();
  }// </editor-fold>//GEN-END:initComponents

  public void showFileChooser()
  {

    if(isUpload)
    {
      Transfer tr = new Upload();
      tr.showFileChooser();
    }
    else
    {
      Transfer tr = new Download();
      tr.showFileChooser();
    }

    if(!files.isEmpty() && !isUpload)
    {
      selectFilesButton.setEnabled(false);
      try {
        t= new Thread(ref);
        t.start();
      } catch (Exception ex) {
        guiLogger.logError(Level.SEVERE, null, ex);
      }
    }
  }

  private void createArguments() throws Exception
  {
    String arguments[];

    arguments = new String[7 + files.size()];
    if(isUpload)
    {
      arguments[0]="-r";
      arguments[1]="-c";
      arguments[2]=serverName;
      arguments[3]="-d";
      arguments[4]=destinationDir;
      arguments[5]="-p";
      arguments[6]=String.valueOf(portNum);
      for(int i=0;i<files.size();i++) {
        File f = files.get(i);
        arguments[7+i]=f.getAbsolutePath();
      }
    }
    else
    {
      String fullFileName = files.get(0).getAbsolutePath();
      if(isDirectory)
        arguments = new String[]{"-r","-pull","-c",serverName, "-d",fullFileName,destinationDir,"-p",String.valueOf(portNum)};
      else
        arguments = new String[]{"-pull","-c",serverName, "-d",fullFileName,destinationDir,"-p",String.valueOf(portNum)};
    }

    files.clear();

    FDT.main(arguments);
  }


  private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
    System.exit(0);
    // TODO add your handling code here:
  }//GEN-LAST:event_cancelButtonActionPerformed

  private void startButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startButtonActionPerformed
    try {
      if(serverName.equals("")||serverName == null)
      {
        String message = "ServerName should not be Null. Please try again.";
        JOptionPane.showMessageDialog(new JFrame(), message, "Dialog",JOptionPane.ERROR_MESSAGE);
      }
      else if(files.isEmpty())
      {
        String message = "Please select a fileName and press start.";
        JOptionPane.showMessageDialog(new JFrame(), message, "Dialog",JOptionPane.ERROR_MESSAGE);
        showFileChooser();
      }
      else
      {
        t=null;
        selectFilesButton.setEnabled(false);
        startButton.setEnabled(false);
        if(isUpload) {
          cancelButton.setEnabled(false);
          exitWarning.setVisible(true);
        }
        t= new Thread(ref);
        t.start();
      }

    } catch (Exception ex) {
      guiLogger.logError(Level.SEVERE, "", ex);
    }

    // TODO add your handling code here:
  }//GEN-LAST:event_startButtonActionPerformed

  private void menuItemExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemExitActionPerformed
    System.exit(0);
    // TODO add your handling code here:
  }//GEN-LAST:event_menuItemExitActionPerformed

  private void menuItemAboutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemAboutActionPerformed

    AboutDialog dlg= new AboutDialog(this);
    dlg.show();
    // TODO add your handling code here:
  }//GEN-LAST:event_menuItemAboutActionPerformed

  private void menuItemHelpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemHelpActionPerformed

    try {
      //Set your page url in this string. For eg, I m using URL for Google Search engine
      String url = "http://bioserver.hci.utah.edu/BioInfo/index.php/Software:FDT";
      java.awt.Desktop.getDesktop().browse(java.net.URI.create(url));
    }
    catch (java.io.IOException e) {
      guiLogger.logError(e);
    }
    // TODO add your handling code here:
  }//GEN-LAST:event_menuItemHelpActionPerformed

  private void selectFilesButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectFilesButtonActionPerformed
    // TODO add your handling code here:
    showFileChooser();
  }//GEN-LAST:event_selectFilesButtonActionPerformed

  private void menuItemShowLogsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemShowLogsActionPerformed
    // TODO add your handling code here:
    if(menuItemShowLogs.getState() == true)
      logPanelInstance.setVisible(true);
    else
      logPanelInstance.setVisible(false);
  }//GEN-LAST:event_menuItemShowLogsActionPerformed


  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JButton cancelButton;
  private javax.swing.JFrame jFrame1;
  private javax.swing.JFrame jFrame2;
  private javax.swing.JFrame jFrame3;
  private javax.swing.JMenu jMenu1;
  private javax.swing.JMenu jMenu3;
  private javax.swing.JMenu jMenu4;
  private javax.swing.JMenu jMenu6;
  private javax.swing.JMenuBar jMenuBar2;
  private javax.swing.JScrollPane jScrollPane1;
  private javax.swing.JMenuBar menuBar;
  private javax.swing.JMenuItem menuItemAbout;
  private javax.swing.JMenuItem menuItemExit;
  private javax.swing.JMenuItem menuItemHelp;
  private javax.swing.JCheckBoxMenuItem menuItemShowLogs;
  protected javax.swing.JButton selectFilesButton;
  protected javax.swing.JButton startButton;
  protected javax.swing.JTable statusTable;
  // ***(R. Cundick 3/2/12): The following were added manually as I don't have 
  // ** access to the form builder that was originally used to generate this class
  protected javax.swing.JLabel exitWarning;   
  protected javax.swing.JLabel waitMessage;   
  // End of variables declaration//GEN-END:variables

}
