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
	 - Place MySQL JDBC driver in /path/to/orion/lib
	 - Try out Orion server.  Start it and then attempt to access from a browser. 
	   A page showing orion info should appear.
	 
3. Install MySQL database server
   - Create a database user called gnomex
   - Login into MySQL as gnomex user and run the SQL script 
     'gnomex_db_ddl.sql' to create the gnomex database.
   - Login into MySQL as gnomex user and run the SQL script 
     'gnomex_db_populate.sql' to load the dictionaries.
   
3. Copy gnomex.ear into path/to/orion/applications

4. Copy the following files to path/to/orion/config. (If these files are 
   configured for other web applications, don't overlay these files but instead 
   modify them to include necessary entries.) 
   - datasources.xml - Change the password to match your MySQL gnomex user. 
   - application.xml
   - server.xml - Change the <mail-session> smtp host to your mail server.
   - secure-web-site.xml
   
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
      Required properties:
      -	experiment_directory
      -	experiment_test_directory
      -	analyis_directory
      -	analysis_test_directory
      -	flowcell_directory
      -	flowcell_test_directory
      -	production_server
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
   



   