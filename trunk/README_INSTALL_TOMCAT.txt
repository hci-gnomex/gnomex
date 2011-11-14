Installing GNomEx_$VERSION
------------------------------------------

System Requirements
--------------------
- Java 6 (http://java.sun.com/)
- Apache Tomcat server 5.5 (http://tomcat.apache.org/download-55.cgi)
- OpenEJB 3.1.4 (http://www.apache.org/dist/openejb/3.1.4/openejb.war)
- MySQL database server (http://mysql.com)
- MySQL JDBC driver (http://mysql.com, download mysql-connector-java-5.1.7.zip)
- Adobe Flash Player 10+ (http://get.adobe.com/flashplayer/)

To compile GNomEx:
 - Ant (http://ant.apache.org/)
 - Adobe Flex 3.5 Open source (http://opensource.adobe.com/wiki/display/flexsdk/download?build=3.5.0.12683&pkgtype=2&release=3)
 - Adobe Flex 3.5 Data Visualization Components for Flex Builder (http://www.adobe.com/cfusion/entitlement/index.cfm?e=flex3sdk)


Install Instructions
--------------------
1. Download and uncompress the the GNomEx open source distribution (GNomEx_$VERSION.zip) from SourceForge 
   (http://sourceforge.net/projects/gnomex).
 
2. Install Java, 1.6+. Make sure to set the JAVA_HOME environment variable.

3. Install Apache Tomcat server application server 
     a. Download Apache Tomcat 5.5 server (http://tomcat.apache.org/download-55.cgi) and follow the install instructions
	 b. Modify Tomcat configuration
	   - Copy all jar files in /path/to/GNomEx_$VERSION/tomcat/common/lib to /path/to/tomcat/common/lib
	 c. Login as root
	 d. Start Tomcat
	    >cd /path/to/tomcat/bin
	    >sh startup.sh
	 		Try to access Tomcat from browser (http://myserver:8080) to make sure 
	 		Apache Tomcat Server page appears.  You may need to open
	 		up port 8080 to gain access and punch holes through your firewalls.
	 e. Shut down the server
	 f. Copy the /path/to/GNomEx_$VERSION/gnomex/dist/gnomex.war into /path/to/tomcat/webapps
	 g. Copy the /path/to/GNomEx_$VERSION/gnomex/dist/gnomex_realm.jar into /path/to/tomcat/server/lib
	 
4. Install Apache OpenEJB 3.1.4
     a. Download and install the openejb.war (http://www.apache.org/dist/openejb/3.1.4/openejb.war by copying 
        it to /path/to/tomcat/webapps.  (NOTE:  file must be called openejb.war.)
     b. Restart Tomcat
     c. Run the installer servlet at http://127.0.0.1:8080/openejb/installer (and press the install button). 
        Note: this will modify your catalina.sh or catalina.bat files. 
     d. Restart Tomcat
	 
5. Install the MySQL database server 5.XX (http://mysql.com)
   - Login to mysql.

     >mysql -u root -p
     [enter root password]     

   - Create a database user called gnomex and gnomexGuest. Grant all
     privileges on gnomex database to gnomexUser, but only read access 
     on gnomex database to gnomexGuest.
     
     CREATE DATABASE gnomex;  
     CREATE USER 'gnomex'@'localhost' IDENTIFIED BY 'password_for_gnomex_account';
     CREATE USER 'gnomex'@'%' IDENTIFIED BY 'password_for_gnomex_account';
     GRANT ALL PRIVILEGES ON gnomex.* TO 'gnomex'@'localhost' WITH GRANT OPTION;
     GRANT ALL PRIVILEGES ON gnomex.* TO 'gnomex'@'%' WITH GRANT OPTION;
     CREATE USER 'gnomexGuest'@'localhost' IDENTIFIED BY 'password_for_gnomexGuest_account';
     CREATE USER 'gnomexGuest'@'%' IDENTIFIED BY 'password_for_gnomexGuest_account';
     GRANT SELECT ON gnomex.* TO 'gnomexGuest'@'localhost' IDENTIFIED BY 'password_for_gnomexGuest_account';
     GRANT SELECT ON gnomex.* TO 'gnomexGuest'@'%' IDENTIFIED BY 'password_for_gnomexGuest_account';
     FLUSH PRIVILEGES;  
     
   - Login into MySQL as gnomex user and run the SQL scripts
     /path/to/GNomEx_$VERSION/gnomex/sql/gnomex_db_ddl.sql to create the gnomex database and  
     /path/to/GNomEx_$VERSION/gnomex/sql/gnomex_db_populate.sql to load the dictionaries.
     
     >mysql -u gnomex -p
     [enter gnomex password]
     
     SOURCE ~/GNomEx_$VERSION/gnomex/sql/gnomex_db_ddl.sql
     SOURCE ~/GNomEx_$VERSION/gnomex/sql/gnomex_db_populate.sql

6. Set up the datasources.  Edit /path/to/tomcat/openejb.xml.  Add the datasources 
   in  path/to/GNomEx_$VERSION/gnomex/tomcat/conf/openejb.xml.  
   - Change the passwords to match your MySQL gnomex user and 
     gnomexGuest user. 
             
7. Start Tomcat

8. Run GNomEx Flex application from browser window.
   - From browser window, type in URL:  http://myserver/gnomex/gnomexFlex.jsp
   - The flex application should load and then a login popup window should appear.
   - Enter the user name 'admin' and the password 'admin'.
   - The first thing to do is get rid the the 'admin' user account and add
     yourself as an admin.  Click and the 'Users and Groups' link.  Click
     'New user' link on bottom left.  Enter your user information,
     including a login and password.  Set the Permission level to Admin. Save 
     the entry.  
   - Try logging in again, using your new login and password. Then go back to the 
     Users And Groups and remove the user named 'admin'.
   - There are a number of configurable properties that you need to 
     set for your installation.  Click on the 'Manage Dictionaries' under the 'Administration'
     heading of the Welcome pane.  Scroll in the left-hand tree to find the node called 
     'Admin - Property'.  Review the entries and make sure you modify the following properties 
     to match your environment:
      Required properties - Make sure all of these directories exist.
      -	experiment_read_directory, experiment_write_directory      
      -	analysis_read_directory, analysis_write_directory        
      -	flowcell_directory				
      -	lucene_index_directory
      -	lucene_experiment_index_directory
      -	lucene_analysis_index_directory
      -	lucene_protocol_index_directory	
      - temp_directory
      


GNomEx Batch Jobs
-------------------------
GNomEx builds a searchable text index using Apache's Lucene 
(http://lucene.apache.org/java/docs/).  In order to refresh
this index with the latest data, a batch chron job (nightly) must be scheduled.
The command to build the index is in the GNomEx ear. To run the
command manually do the following
  >cd /path/to/tomcat/webapps/gnomex/scripts
  
  For Windows environments:
  >index_gnomex.cmd
  
  For Unix environments:
  >sh index_gnomex.sh
  
  
 
Configuring GNomEx for FDT Uploads and Downloads
------------------------------------------------
Fast Data Transfer is a utility developed by CalTech and CERN. See http://monalisa.cern.ch/FDT/.
GNomEx can be configured with FDT, which is equipped to deal with the transfer of large
data files over the internet.  NOTE:  At this time, FDT in GNomEx is set up to work with the Orion application
server running on Unix and Linux based systems.  If you are running GNomex on a Windows or Mac server, GNomEx is
is not yet configured to work.

1. Download and unzip the fdt distribution (http://sourceforge.net/projects/gnomex/files/fdt/fdt_hci_1.0.zip/download).
   
2. Copy fdtServer.jar to /path/to/fdt/server.  

3. Make sure port 54321 is not blocked by the firewall.

4. Create an fdt staging dir.  This will be the directory that stages the files for the fdt uploads and downloads.   
   VERY IMPORTANT: the fdt stagin directory should be a sandbox directory that ONLY contains temporary fdt directories and
   files.  Make sure that this directory does not contain any files or subdirectories that should be restricted
   from FDT transfers.
   
5. Start the fdt server in the background.  
   >java -jar fdtServer.jar -rdt fdt_staging_dir &

6. Copy fdtClient.jar to your default web directory.  (For example, create a directory called 
   fdt under var/www/html/ if your system is running the standard apache server.)
   
7. Make a directory for the fdt file monitor daemon and copy the files located in the fdt distribution:
   >mkdir /path/to/fdtfiledaemon
   >mkdir /path/to/fdtfieldaemon/tasks
   >cp fdtFileDaemon.jar /path/to/fdtfiledaemon/
   >cp scripts/fdtfiledaemon.sh /path/to/fdtfiledaemon/
   
8. Modify /path/to/fdtfiledaemon/fdtfiledaemon.sh.  Edit /path/to/fdtfiledaemon/ and /path/to/fdt_staging_dir to point
   to your filepath locations.
   
9. Start the fdt file monitor daemon.
   >cd /path/to/fdtfiledaemon
   >nohup sh fdtfiledaemon.sh 
   
10. In GNomEx, click on 'Manage Dictionaries'.  Create/edit these properties:
     property                          value
     ----------------------            ---------------------------------------
     fdt_supported                     Y
     fdt_directory                     The fdt staging dir
     fdt_directory_gnomex              The fdt staging dir.  Only differs from fdt_directory when fdt server running on different machine than Orion (gnomex)
     fdt_client_codebase               The URL to download fdtClient.jar.  example:  http:bioserver.hci.utah.edu/fdt (See step 6).
     fdt_server_name                   The machine (domain name) that the fdt server is running on.
     fdt_file_daemon_task_dir          Set to /path/to/fdtfilemonitor/tasks
     fdt_user                          Set to the unix user account that has read/write access to the fdt_staging dir
                                       and gnomex data area
     fdt_group                         Set to the unix group that has read/write access to the fdt_staging directory and
                                       and gnomex data area
     
11. In GNomEx, go to Experiment downloads or Analysis Downloads.  A button for 'FDT Download' should appear.  Also,
    there should be an 'FDT Upload' link for uploading Analysis and Experiment data files.
    
12.  When using the FDT Upload or Download, webstart will lauch the FDT GUI.  To troubleshoot transfer problems, 
     click on the menu Options -> Show Transfer Logs. 
     
13.  (Optional) To configure GNomEx to log FDT upload and download activity (for GNomEx upload/download usage charts), 
     set up a shell script which will be executed when FDT performs a transfer:
     
     a.  Create a directory /path/to/fdtapplogger
     b.  Copy /path/to/GNomEx_$VERSION/gnomex/scripts/transfer_logger.sh to 
              /path/to/fdtapplogger/
     c.  Modify transfer_logger.sh to match your filepath and server name (-server argument)
     d.  Copy /path/to/GNomEx_$VERSION/gnomex/dist/gnomex_client.jar to     
              /path/to/fdtapplogger/  
     e.  Make the fdt user account to owner and the fdt group the group.  Give read and execute permissions 
         to the script and jar.
         >chown -R fdt_user_here:fdt_group_here /path/to/fdtapplogger/ 
         >chmod -R 550 /path/to/fdtapplogger/ 
     f.  Restart the fdt server with the -appLogger argument
         >java -jar fdtServer.jar -rdt fdt_staging_dir -appLogger /path/to/fdtapplogger/transfer_logger.sh &
  
        
      
Configuring GNomEx and Orion for SSL (https)
--------------------------------------------
By default, GNomEx is configured to run from a non-secure (http:) web-site.
To run GNomEx from a secure web-site (https:), deploy the secure gnomex.ear, modify the orion 
configuration, and set up a server certificate. 
   

1. Copy the /path/to/GNomEx_$VERSION/gnomex/dist/secure/gnomex.war into /path/to/tomcat/webapps

2. Modify the Tomcat configuration. Edit the /path/to/tomcat/conf/server.xml, uncommenting
   the <Connector port="8443> element:
    
      <Connector port="8443" maxThreads="150" 
       scheme="https" secure="true" SSLEnabled="true" 
       keystoreFile="path/to/tomcat/keystore" 
       keystorePass="YourKeystorePassword" 
       clientAuth="false" keyAlias="yourAlias" sslProtocol="TLS"/>
   
   
3. Create a Keystore for SSL.  
   
   a. Create a keystore.  Use the JDK or JRE installed on your server.   
      >keytool -genkey -keyalg "RSA" -keystore /path/to/cert/keystore -storepass the_password_goes_here -validity 5060

   b. Modify the <Connector port="8443"> entry in /path/to/tomcat/conf/server.xml, setting the keystorePass to your keystore's
      password and keystoreFile to your keystore file location.
    
   c. Generate a certificate request, specifying your password (-storepass), the file
      and to store the certificate request (-file) specifying your full 
      web-server domain name (-alias).
	    >keytool -certreq -keyalg "RSA"  -file /path/to/cert/myserver.csr -keystore /path/to/cert/keystore -storepass the_password_goes_here -alias myserver.someplace.somewhere.edu
	
   d. Purchase a certificate (VeriSign, Thawte, etc) or obtain one from your 
      institution.  (Use the certificate request (.csr file) file generated in step c.)
   
   e. Put the certificate returned (.cer file) into the keystore, specifying your
      certificate file (-file) and your full web-server domain name (-alias). 
	    >keytool -keystore /path/to/cert/keystore -keyalg "RSA" -import -trustcacerts -file myserver.cer -alias myserver.someplace.somewhere.edu

   f.  Make sure the signing authority (example: VeriSign) that you obtained the 
       certificate from has it's root certificate installed in the cacerts keystore.
       To list the root certificates:  
       >keytool -list -keystore $JAVA_HOME/jre/lib/security/cacerts
      
   g.  If the root certificate for the signing authority is not here,
       add it.  When you obtained the certificate, there should have been
       a way to download the institutions's root certificate (.cer) file.
       To allow the system to "trust" this signing authority, add the 
       root certificate into cacerts for the jdk you are using. Default cacerts 
       password is 'changeit', but recommend change to something else.
	     >keytool -keystore $JAVA_HOME/jre/lib/security/cacerts -keyalg "RSA" -import -file /path/to/cert/myserver.cer -alias myserver.someplace.somewhere.edu
   
   h. Making root certificate available for download from website. 
   
   
Build Instructions
------------------
1. You must have the Adobe Flex Builder's SDK to compile GNomEx.  If
   you do not want to purchase this, you can use the open source version
   of the Flex SDK (Mozilla Public License) in combination with
   the Flex Data Visualization Components.  NOTE:  The Data Visualizations
   Components are licensed under the Flex SDK license, not the Mozilla
   Public License, so please please read each license to understand how
   the libraries can be used.
   
   - Download and uncompress the Flex SDK 3.5 MPL 
     (http://opensource.adobe.com/wiki/display/flexsdk/download?build=3.5.0.12683&pkgtype=2&release=3)
    
   - Download and uncompress (into the same directory as above)
     the Adobe Flex 3.5 Data Visualization Components for Flex Builder.
     (http://www.adobe.com/cfusion/entitlement/index.cfm?e=flex3sdk)
     

2. Modify /path/to/gnomex/build.properties
   - Set FLEX_HOME to the directory containing the Flex SDK.
   - Set tomcat.dir to the directory of tomcat

   
3. Run Ant build on build.xml with target=all-tomcat
   >cd path/to/gnomex
   >ant all-tomcat

4. A new gnomex.war file will be placed in path/to/tomcat/webapps  
   Restart Tomcat and gnomex will be deployed.
   
   


      
