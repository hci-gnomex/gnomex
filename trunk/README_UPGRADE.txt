Upgrading to GNomEx_$VERSION
------------------------------------------

1. Download and uncompress the the GNomEx open source distribution (GNomEx_$VERSION.zip) from SourceForge 
   (http://sourceforge.net/projects/gnomex).
 
2. Login into MySQL as gnomex user and run the upgrad SQL scripts

     >mysql -u gnomex -p
     [enter gnomex password]
     
     SOURCE ~/GNomEx_$VERSION/gnomex/sql/gnomex_db_upgrade_to_$VERSION.sql

