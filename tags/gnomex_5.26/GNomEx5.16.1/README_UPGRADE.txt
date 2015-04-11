Upgrading to GNomEx_$VERSION
------------------------------------------

1. Download and uncompress the the GNomEx open source distribution (GNomEx_$VERSION.zip) from SourceForge 
   (http://sourceforge.net/projects/gnomex).
 
2. Login into MySQL as gnomex user and run the upgrade SQL scripts. You must run all SQL scripts in order.  
   For example, if you last installed GNomEx 5.0.4 and have upgraded to GNomEx 5.0.8, you must run
   all upgrade scripts from 5.0.4 to 5.0.8.

     >mysql -u gnomex -p
     [enter gnomex password]
     
     SOURCE ~/GNomEx_$VERSION/gnomex/sql/gnomex_db_upgrade_to_$VERSION.sql

