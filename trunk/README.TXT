Configuring GNomEx
------------------------------------------

System Requirements
--------------------
- Java 6 (http://java.sun.com/)
- Orion application server 2.0.5+ (http://www.orionserver.com/)
- MySQL database server (http://mysql.com)
- MySQL JDBC driver (http://mysql.com, download mysql-connector-java-5.1.7.zip)
- Adobe Flash Player 9+ (http://get.adobe.com/flashplayer/)

To compile GNomEx:
 - Ant (http://ant.apache.org/)
 - Flex Builder 3.0+ (http://www.adobe.com/products/flex/)


Install Instructions
--------------------
1. Install Java.  Make sure JAVA_HOME is set.

2. Install Orion application server.
   a. Download Orion application server and unzip
	 b. Modify Orion configuration
	   - Copy tool.jar file in gnomex/orion/ to /path/to/orion
	   - Copy all jar files in gnomex/orion/lib to /path/to/orion
	 c. Start Orion application server
	    >cd /path/to/orion
	    >java -Xms256m -Xms900m -jar orion.jar
	 		Try to access orion from browser (http://myserver) to make sure 
	 		Orion Application Server page appears.
	 
3. Install MySQL database server
   - Create a database user called gnomex and gnomexGuest.  
     >mysql -u root -p
     [enter root password]
     
      mysql> CREATE USER 'gnomex'@'localhost' IDENTIFIED BY 'password_for_gnomex_account';
      mysql> CREATE USER 'gnomex'@'%' IDENTIFIED BY 'password_for_gnomex_account';
      mysql> GRANT ALL PRIVILEGES ON gnomex.* TO 'gnomex'@'localhost' WITH GRANT OPTION;
      mysql> GRANT ALL PRIVILEGES ON gnomex.* TO 'gnomex'@'%' WITH GRANT OPTION;
      
      mysql> CREATE USER 'gnomexGuest'@'localhost' IDENTIFIED BY 'password_for_gnomexGuest_account';
      mysql> CREATE USER 'gnomexGuest'@'%' IDENTIFIED BY 'password_for_gnomexGuest_account';
      mysql> GRANT SELECT PRIVILEGES ON gnomex.* TO 'gnomexGuest'@'localhost';
      mysql> GRANT SELECT PRIVILEGES ON gnomex.* TO 'gnomexGuest'@'%';
      
      mysql> FLUSH PRIVILEGES;  
     
   - Login into MySQL as gnomex user and run the SQL script 
     'gnomex_db_ddl.sql' to create the gnomex database.
     
   - Login into MySQL as gnomex user and run the SQL script 
     'gnomex_db_populate.sql' to load the dictionaries.
   
3. Copy gnomex/dist/gnomex.ear into /path/to/orion/applications.

4. Copy gnomex/dist/hciEnv.ear to /path/to/orion/applications.

5. Modify the orion configuration by revising the following
   files in /path/to/orion/config.  (Use the files in gnomex/orion/config
   copy copy the necessary entries into each file.) 
   - datasources.xml - Change the password to match your MySQL gnomex user and 
     gnomexGuest user. 
   - application.xml
   - server.xml - Change the <mail-session> smtp host to your mail server.
   - default-web-site.xml
   
   - secure-web-site.xml
   - Create a keystore
      >keytool -genkey -keyalg "RSA" -keystore /path/to/orion/keystore -storepass 123456 -validity 360
   - Modify the security-web-site.xml, setting the storepass to your keystore's
     password. 
      <ssl-config keystore="../keystore" keystore-password="your_keystore_password_here" needs-client-auth="true" />
   - TODO:  Add info about certificates
      
   
5. Start Orion

6. Run GNomEx Flex application from browser window.
   - From browser window, type in URL:  https://myserver/gnomex/gnomexFlex.jsp
   - The flex application should load and then a login dialog should appear.
   - Enter the user name 'admin' and the password 'admin'.
   - The first thing to do is get rid the the 'admin' user account and add
     yourself as an admin.  Click and the 'Users and Groups' link.  Click
     'Add new user' link on bottom left.  Enter your user information,
     including a login and password.  Save the entry.  Now select the 
     'admin' user and remove it.
   - Try logging in again, using your new login and password.
   - There are a number of configurable properties that you will want to 
     set for your installation.  Click on the upper-right link 'Dictionaries'.
     Scroll in the left-hand tree to find the node called 'Property'.  Review
     the entries and make sure you modify the following properties to 
     match your environment:
      Required properties - Make sure all of these directories exist.
      -	experiment_directory      
      -	experiment_test_directory 
      -	analyis_directory        
      -	analysis_test_directory   
      -	flowcell_directory				
      -	flowcell_test_directory   
      -	lucene_index_directory
      -	lucene_experiment_index_directory
      -	lucene_analysis_index_directory
      -	lucene_protocol_index_directory
	
      
      Optional properties:
      -  contact_name_software_bugs
      -  contact_email_software_bugs
      -  contact_name_core_facility
      -  contact_email_core_facility
     	-  contact_phone_core_facility
     	-  contact_email_software_tester
      -	 production_server
      -  qc_instructions
      -  workauth_instructions


GNomEx Batch Jobs
-------------------------
GNomEx builds a searchable text index using Apache's Lucene 
(http://lucene.apache.org/java/docs/).  In order to refresh
this index with the latest data, a batch job (nightly) must be scheduled.
The command to build the index is in the GNomEx ear. To run the
command manually do the following
  >cd /path/to/orion/applications/gnomex/gnomex
  >index_gnomex.cmd (TODO:  Create a shell script for unix enviornments)
   
   
Build Instructions
------------------
1. You must have the Adobe Flex Builder's SDK to compile GNomEx.  If
   you do not want to purchase this, you can use the opensource version
   of the Flex SDK (Mozilla Public License).
   - AdvancedDataGrid is not available in this version of the SDK, so you will
     have to comment out any references to it.  In GNomEx, you will need
     to remove the views for Billing.  (TODO:  put detailed instructions
     here for disabling billing.)
   - Download and install the Flex SDK 3+ MPL 
     (http://opensource.adobe.com/wiki/display/flexsdk/downloads)

2. Modify build.properties
   - Set FLEX_HOME to the directory contained the Flex SDK.
   - Set orion.dir to the directory of orion

3. Run Ant build on build.xml with target=ALL
   >cd path/to/gnomex_source
   >ant all

4. A new gnomex.ear file will be placed in path/to/orion/applications.  
   Restart orion and gnomex will be deployed.
   



   