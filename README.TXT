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
 - Flex SDK 3.0+ (http://www.adobe.com/products/flex/)


Install Instructions
--------------------
1. Download the gnomex open source distribution (gnomex_opensource.zip) on SourceForge.
   Extract the files to a local directory (/path/to/gnomexinstall)
 
2. Install Java.  Download the SDK (Version 1.6). Make sure JAVA_HOME is set.

3. Install Orion application server.
   a. Download Orion application server and unzip
	 b. Modify Orion configuration
	   - Copy tools.jar file located in the Java SDK lib directory  to /path/to/orion and /path/to/orion/lib
	   - Copy all jar files in /path/to/gnomexinstall/orion/lib to /path/to/orion/lib
	 c. Login as root
	 d. Start Orion application server
	    >cd /path/to/orion
	    >java -Xms256m -Xms900m -jar orion.jar [-out log/orion_out.log]
	 		Try to access orion from browser (http://myserver) to make sure 
	 		Orion Application Server page appears.  You may need to open
	 		up port 80 to gain access.
	 
4. Install MySQL database server
   - Create a database user called gnomex and gnomexGuest. Grant all
     privileges on gnomex database to gnomexUser, but only read access 
     on gnomex database to gnomexGuest.
     
     >mysql -u root -p
     [enter root password]     
     
      mysql> CREATE USER 'gnomex'@'localhost' IDENTIFIED BY 'password_for_gnomex_account';
      mysql> CREATE USER 'gnomex'@'%' IDENTIFIED BY 'password_for_gnomex_account';
      mysql> GRANT ALL PRIVILEGES ON gnomex.* TO 'gnomex'@'localhost' WITH GRANT OPTION;
      mysql> GRANT ALL PRIVILEGES ON gnomex.* TO 'gnomex'@'%' WITH GRANT OPTION;
      
      mysql> CREATE USER 'gnomexGuest'@'localhost' IDENTIFIED BY 'password_for_gnomexGuest_account';
      mysql> CREATE USER 'gnomexGuest'@'%' IDENTIFIED BY 'password_for_gnomexGuest_account';
      mysql> GRANT SELECT ON gnomex.* TO 'gnomexGuest'@'localhost' IDENTIFIED BY 'password_for_gnomexGuest_account';
      mysql> GRANT SELECT ON gnomex.* TO 'gnomexGuest'@'%' IDENTIFIED BY 'password_for_gnomexGuest_account';
      
      mysql> FLUSH PRIVILEGES;  
     
   - Login into MySQL as gnomex user and run the SQL script 
     /path/to/gnomexinstall/gnomex/'gnomex_db_ddl.sql' to create the gnomex database.
     
   - Login into MySQL as gnomex user and run the SQL script 
     /path/to/gnomexinstall/gnomex/'gnomex_db_populate.sql' to load the dictionaries.
   
5. Copy /path/to/gnomexinstall/gnomex/dist/gnomex.ear into /path/to/orion/applications.

6. Modify the orion configuration by revising the following
   files in /path/to/orion/config.  (Use the files in /path/to/gnomexinstall/orion/config
   copy  the necessary entries into each file.) 
   a. datasources.xml 
      - Change the password to match your MySQL gnomex user and 
        gnomexGuest user. 
   b. server.xml 
      - Change the <mail-session> smtp host to your mail server.
      - Add the <web-site path="./secure-web-site.xml" /> if you will be running 
        gnomex over secure socket layer (SSL).
      - Add the <application> entries for hciEnv and gnomex
   c. default-web-site.xml 
      - Add the <web-app> entry for gnomex
      
   d. secure-web-site.xml
      - Add the <web-app> entry from gnomex.  
      - Add the  <ssl-config> entry.  Once you have a keystore, change the -storepass
        parameter to match the keystore password.  See detailed instructions
        for getting SSL configured to run under Orion.)

        NOTE: By default, gnomex must run from a secure website (https:), so this <web-app>
        entry must be present in secure-web-site.xml.  However; gnomex can
        be configured to run from a non-secure website (http:).  In this case,
        leave out the <web-app> entry and <ssl-config> entry and
        build gnomex to allow for non-secure hosting.  (See build instructions
        below.)
         
7. Start Orion

8. Run GNomEx Flex application from browser window.
   - From browser window, type in URL:  https://myserver/gnomex/gnomexFlex.jsp
   - The flex application should load and then a login popup window should appear.
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
      
      
Configuring Orion application server for SSL
--------------------------------------------
1. Create a keystore.  Use the JDK or JRE installed on your server.   
   >keytool -genkey -keyalg "RSA" -keystore /path/to/orion/keystore -storepass 123456 -validity 5060

2. Modify the <ssl-config> entry in security-web-site.xml, setting the storepass to your keystore's
   password.
    
3. Generate a certificate request, specifying your password (-storepass), the file
   and to store the certificate request (-file) specifying your full 
   web-server domain name (-alias).
	 >keytool -certreq -keyalg "RSA"  -file myserver.csr -keystore keystore -storepass 123456 -alias myserver.someplace.somewhere.edu
	
4. Purchase a certificate (VeriSign, Thawte, etc) or obtain one from your 
   institution.  (Use the certificate request (.csr file) file generated in step 3.)
   
5. Put the certificate returned (.cer file) into the keystore, specifying your
   certificate file (-file) and your full web-server domain name (-alias). 
	 >keytool -keystore keystore -keyalg "RSA" -import -trustcacerts -file myserver.cer -alias myserver.someplace.somewhere.edu

6. Make sure the signing authority (example: VeriSign) that you obtained the 
   certificate from has it's root certificate installed in the cacerts keystore.
   To list the root certificates:  
   >keytool -list -keystore $JAVA_HOME/jre/lib/security/cacerts
      
7. If the root certificate for the signing authority is not here,
   add it.  When you obtained the certificate, there should have been
   a way to download the institutions's root certificate (.cer) file.
   To allow the system to "trust" this signing authority, add the 
   root certificate into cacerts for the jdk you are using. Default cacerts 
   password is 'changeit', but recommend change to something else.
	 >keytool -keystore $JAVA_HOME/jre/lib/security/cacerts -keyalg "RSA" -import -file myserver.cer -alias myserver.someplace.somewhere.edu
   
8. TODO:  Add note about making root certificate available for download
   from website. 
   
- See detailed instructions and troubleshooting guide at http://www.orionserver.com/docs/ssl.html.




GNomEx Batch Jobs
-------------------------
GNomEx builds a searchable text index using Apache's Lucene 
(http://lucene.apache.org/java/docs/).  In order to refresh
this index with the latest data, a batch job (nightly) must be scheduled.
The command to build the index is in the GNomEx ear. To run the
command manually do the following
  >cd /path/to/orion/applications/gnomex/gnomex
  
  For Windows environments:
  >index_gnomex.cmd
  
  For Unix environments:
  >sh index_gnomex.sh
   
   
Build Instructions
------------------
1. You must have the Adobe Flex Builder's SDK to compile GNomEx.  If
   you do not want to purchase this, you can use the open source version
   of the Flex SDK (Mozilla Public License) in combination with
   the Flex Data Visualization Components.  NOTE:  The Data Visualizations
   Components are licensed under the Flex SDK license, not the Mozilla
   Public License, so please please read each license to understand how
   the libraries can be used.
   
   - Download and uncompress the Flex SDK 3+ MPL 
     (http://opensource.adobe.com/wiki/display/flexsdk/downloads)
    
   - Download and uncompress (into the same directory as above)
     the Adobe Flex 3.4 Data Visualization Components for Flex Builder.
     (http://www.adobe.com/support/documentation/en/flex/3/releasenotes_flex3_sdk.html#Datavisualization)
     

2. Modify build.properties
   - Set FLEX_HOME to the directory containing the Flex SDK.
   - Set orion.dir to the directory of orion
   
3. If you want to run gnomex from a non-secure website (http:) rather
   than over SSL (https:), you will need to modify the following
   constant before building:
   - Edit the source file gnomex/src/hci/gnomex/constants/Constants.java
   - Set the following constant to false
     public static final boolean            REQUIRE_SECURE_REMOTE           = false; 
   
3. Run Ant build on build.xml with target=ALL
   >cd path/to/gnomex_source
   >ant all

4. A new gnomex.ear file will be placed in path/to/orion/applications.  
   Restart orion and gnomex will be deployed.
   



   