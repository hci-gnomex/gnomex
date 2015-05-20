---
--- Add isExternal, isInternal flags to RequestCategory
---
ALTER TABLE gnomex.RequestCategory add column isInternal CHAR(1);
ALTER TABLE gnomex.RequestCategory add column isExternal CHAR(1);

update gnomex.RequestCategory set isInternal = 'Y', isExternal = 'Y';

---
--- Move default protocols from SampleTypeApplication RequestCategoryApplication
---
ALTER TABLE gnomex.RequestCategoryApplication add column idLabelingProtocolDefault INT(10);
ALTER TABLE gnomex.RequestCategoryApplication add column idHybProtocolDefault INT(10);
ALTER TABLE gnomex.RequestCategoryApplication add column idScanProtocolDefault INT(10);
ALTER TABLE gnomex.RequestCategoryApplication add column idFeatureExtractionProtocolDefault INT(10);


ALTER TABLE gnomex.RequestCategoryApplication  add
  CONSTRAINT FK_RequestCategoryApplication_HybProtocol FOREIGN KEY FK_RequestCategoryApplication_HybProtocol (idHybProtocolDefault)
    REFERENCES gnomex.HybProtocol (idHybProtocol)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION;
    
ALTER TABLE gnomex.RequestCategoryApplication  add
  CONSTRAINT FK_RequestCategoryApplication_LabelingProtocol FOREIGN KEY FK_RequestCategoryApplication_LabelingProtocol (idLabelingProtocolDefault)
    REFERENCES gnomex.LabelingProtocol (idLabelingProtocol)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION;

ALTER TABLE gnomex.RequestCategoryApplication  add
  CONSTRAINT FK_RequestCategoryApplication_ScanProtocol FOREIGN KEY FK_RequestCategoryApplication_ScanProtocol (idScanProtocolDefault)
    REFERENCES gnomex.ScanProtocol (idScanProtocol)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION;

ALTER TABLE gnomex.RequestCategoryApplication  add
  CONSTRAINT FK_RequestCategoryApplication_FeatureExtractionProtocol FOREIGN KEY FK_RequestCategoryApplication_FeatureExtractionProtocol (idFeatureExtractionProtocolDefault)
    REFERENCES gnomex.FeatureExtractionProtocol (idFeatureExtractionProtocol)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION;
    
update gnomex.RequestCategoryApplication rx, gnomex.SampleTypeApplication sx
set rx.idLabelingProtocolDefault = sx.idLabelingProtocolDefault,
    rx.idHybProtocolDefault = sx.idHybProtocolDefault,
    rx.idScanProtocolDefault = sx.idScanProtocolDefault,
    rx.idFeatureExtractionProtocolDefault = sx.idFeatureExtractionProtocolDefault
where sx.codeApplication = rx.codeApplication



ALTER TABLE gnomex.SampleTypeApplication drop column idLabelingProtocolDefault;
ALTER TABLE gnomex.SampleTypeApplication drop column idHybProtocolDefault;
ALTER TABLE gnomex.SampleTypeApplication drop column idScanProtocolDefault;
ALTER TABLE gnomex.SampleTypeApplication drop column idFeatureExtractionProtocolDefault;
