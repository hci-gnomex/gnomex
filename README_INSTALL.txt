Installing GNomEx_$VERSION
------------------------------------------

System Requirements
--------------------
- Java 6 (http://java.sun.com/)
- Orion application server 2.0.2+ (http://www.orionserver.com/)
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

3. Install Orion application server 
   a. Download Orion application server (http://www.orionserver.com/) and unzip
	 b. Modify Orion configuration
	   - Copy the tools.jar file located in the Java SDK lib directory (/usr/java/latest/lib/tools.jar) to /path/to/orion 
	   - Copy all jar files in /path/to/GNomEx_$VERSION/orion/lib to /path/to/orion/lib
	 c. Login as root
	 d. Start Orion application server
	    >cd /path/to/orion
	    >java -Xms256m -Xms900m -jar orion.jar [-out log/orion_out.log]
	 		Try to access orion from browser (http://myserver) to make sure 
	 		Orion Application Server page appears.  You may need to open
	 		up port 80 to gain access and punch holes through your firewalls.
	 e. Shut down the server
	 f. Copy the /path/to/GNomEx_$VERSION/gnomex/dist/gnomex.ear into /path/to/orion/applications/
	 
4. Install the MySQL database server 5.XX (http://mysql.com)
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

5. Copy the following files in /path/to/GNomEx_$VERSION/orion/config to 
   /path/to/orion/config:
   a. data-sources.xml 
      - Change the passwords to match your MySQL gnomex user and 
        gnomexGuest user. 
   b. server.xml 
      - Change the <mail-session> smtp host to your mail server.
   c. default-web-site.xml 
   d. secure-web-site.xml 
             
6. Start Orion

7. Run GNomEx Flex application from browser window.
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
      -	experiment_directory, experiment_read_directory, experiment_write_directory      
      -	analysis_directory, analysis_read_directory, analysis_write_directory        
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
  >cd /path/to/orion/applications/gnomex
  
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
   >nohup java -jar fdtServer.jar -rdt fdt_staging_dir &

6. Copy fdtClient.jar to your default web directory.  (For example, create a directory called 
   fdt under var/www/html/ if your system is running the standard apache server.)
   
7. Make a directory for the fdt file monitor daemon and copy the necessary files:
   >mkdir /path/to/fdtfilemonitor
   >mkdir /path/to/fdtfilemonitor/tasks
   >cp scripts/fdtFileDaemon.jar /path/to/fdtfilemonitor/
   >cp scripts/fdtfiledaemon.sh /path/to/fdtfilemonitor/
   
8. Modify /path/to/fdtfilemonitor/fdtfiledaemon.sh.  Edit /path/to/fdtfilemonitor/ and /path/to/fdt_staging_dir to point
   to your filepath locations.
   
9. Start the fdt file monitor daemon.
   >cd /path/to/fdtfilemonitor
   >nohup sh fdtfiledaemon.sh 
   
10. In GNomEx, click on 'Manage Dictionaries'.  Create/edit these properties:
     property                          value
     ----------------------            ---------------------------------------
     fdt_supported                     Y
     fdt_directory                     The fdt staging dir
     fdt_directory_gnomex              The fdt staging dir.  Only differs from fdt_directory when fdt server running on different machine than Orion (gnomex)
     fdt_client_codebase               The URL to download fdtClient.jar.  (See step 6).
     fdt_server_name                   The machine (domain name) that the fdt server is running on.
     fdt_filedaemon_task_dir           Set to /path/to/fdtfilemonitor/tasks
     fdt_user                          Set to the unix user account that has read/write access to the fdt_staging dir
                                       and gnomex data area
     fdt_group                         Set to the unix group that has read/write access to the fdt_staging directory and
                                       and gnomex data area
     
11. In GNomEx, go to Experiment downloads or Analysis Downloads.  A button for 'FDT Download' should appear.  Also,
    there should be an 'FDT Upload' link for uploading Analysis and Experiment data files.
    
12.  When using the FDT Upload or Download, webstart will lauch the FDT GUI.  To troubleshoot transfer problems, 
     click on the menu Options -> Show Transfer Logs. 
  
        
      
Configuring GNomEx and Orion for SSL (https)
--------------------------------------------
By default, GNomEx is configured to run from a non-secure (http:) web-site.
To run GNomEx from a secure web-site (https:), deploy the secure gnomex.ear, modify the orion 
configuration, and set up a server certificate. 
   

1. Copy the /path/to/GNomEx_$VERSION/gnomex/dist/secure/gnomex.ear into /path/to/orion/applications/

2. Modify the Orion configuration:

   a. Modify /path/to/orion/config/server.xml
      - Uncomment <web-site path="./secure-web-site.xml" />  

   b. Modify /path/to/orion/config/secure-web-site.xml  
      - Uncomment the <web-app> entry from gnomex
      - Uncomment the <ssl-config> entry
   
   
3. Configure Orion for SSL.  (See detailed instructions and troubleshooting guide 
   at http://www.orionserver.com/docs/ssl.html.)
   
   a. Create a keystore.  Use the JDK or JRE installed on your server.   
      >keytool -genkey -keyalg "RSA" -keystore /path/to/orion/keystore -storepass 123456 -validity 5060

   b. Modify the <ssl-config> entry in security-web-site.xml, setting the storepass to your keystore's
      password.
    
   c. Generate a certificate request, specifying your password (-storepass), the file
      and to store the certificate request (-file) specifying your full 
      web-server domain name (-alias).
	    >keytool -certreq -keyalg "RSA"  -file myserver.csr -keystore keystore -storepass 123456 -alias myserver.someplace.somewhere.edu
	
   d. Purchase a certificate (VeriSign, Thawte, etc) or obtain one from your 
      institution.  (Use the certificate request (.csr file) file generated in step c.)
   
   e. Put the certificate returned (.cer file) into the keystore, specifying your
      certificate file (-file) and your full web-server domain name (-alias). 
	    >keytool -keystore keystore -keyalg "RSA" -import -trustcacerts -file myserver.cer -alias myserver.someplace.somewhere.edu

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
	     >keytool -keystore $JAVA_HOME/jre/lib/security/cacerts -keyalg "RSA" -import -file myserver.cer -alias myserver.someplace.somewhere.edu
   
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
   - Set orion.dir to the directory of orion

   
3. Run Ant build on build.xml with target=ALL
   >cd path/to/gnomex
   >ant all

4. A new gnomex.ear file will be placed in path/to/orion/applications.  
   Restart orion and gnomex will be deployed.
   
   


      
