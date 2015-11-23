use gnomex;


-- Add PriceCategory to Property
ALTER TABLE Property
ADD COLUMN idPriceCategory int default NULL;

ALTER TABLE Property ADD CONSTRAINT FK_Property_PriceCategory FOREIGN KEY FK_Property_PriceCategory (idPriceCategory)
    REFERENCES gnomex.PriceCategory (idPriceCategory)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION   


call ExecuteIfTableExists('gnomex','Property_Audit','alter table Property_Audit ADD COLUMN idPriceCategory int NULL');

