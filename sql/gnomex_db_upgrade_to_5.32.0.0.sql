use gnomex;


-- Add PriceCategory to Property
ALTER TABLE Property
ADD COLUMN idPriceCategory int default NULL;

ALTER TABLE Property ADD CONSTRAINT FK_Property_PriceCategory FOREIGN KEY FK_Property_PriceCategory (idPriceCategory)
    REFERENCES gnomex.PriceCategory (idPriceCategory)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION   


call ExecuteIfTableExists('gnomex','Property_Audit','alter table Property_Audit ADD COLUMN idPriceCategory int NULL');


-- script for billThroughGnomex MYSQL
ALTER TABLE Product ADD COLUMN billThroughGnomex CHAR(1) NULL;
CALL ExecuteIfTableExists('gnomex', 'Product_Audit', 'ALTER TABLE Product_Audit ADD COLUMN billThroughGnomex CHAR(1) NULL');


-- new property that allows users to just save their request before actually submitting.  Submitting the experiment will prevent the user from making changes to their samples.
  insert into PropertyDictionary (propertyName, propertyValue, propertyDescription, forServerOnly, idCoreFacility, codeRequestCategory)
  VALUES('new_request_save_before_submit', 'N', 'Allow users to save a new request and still make changes to the request until they mark the request as submitted.', 'N', NULL, NULL);
  
  -- iobio viewer URL's
  INSERT INTO gnomex.PropertyDictionary (propertyName,propertyValue,propertyDescription, forServerOnly) VALUES
   ('bam_iobio_viewer_url','http://bam.iobio.io/?bam=','','N'),
   ('vcf_iobio_viewer_url','http://vcf.iobio.io/?vcf=','','N'),
   ('gene_iobio_viewer_url','http://gene.iobio.io','','N');
  
  
  -- update directory names
update PropertyDictionary set propertyName = 'directory_experiment' where propertyName = 'experiment_directory';
update PropertyDictionary set propertyName = 'directory_analysis' where propertyName = 'analysis_directory';
update PropertyDictionary set propertyName = 'directory_datatrack' where propertyName = 'datatrack_directory'; 
update PropertyDictionary set propertyName = 'directory_flowcell' where propertyName = 'flowcell_directory'; 
update PropertyDictionary set propertyName = 'directory_instrument_run' where propertyName = 'instrument_run_directory'; 
update PropertyDictionary set propertyName = 'directory_product_order' where propertyName = 'product_order_directory'; 


