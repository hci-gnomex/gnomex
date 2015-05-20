-- Add idOrganism to RequestCategory
ALTER TABLE gnomex.RequestCategory add column idOrganism INT(10) null;
ALTER TABLE gnomex.RequestCategory  add CONSTRAINT FK_RequestCategory_Organism FOREIGN KEY FK_RequestCategory_Organism (idOrganism)
    REFERENCES gnomex.Organism (idOrganism)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION;
    
    
-- Add otherOrganism to Sample
ALTER TABLE gnomex.Sample add column otherOrganism VARCHAR(100) null;


-- Add idSamplePrepMethod to RequestCategory
ALTER TABLE gnomex.RequestCategory add column idSamplePrepMethod INT(10) null;
ALTER TABLE gnomex.RequestCategory  add CONSTRAINT FK_RequestCategory_SamplePrepMethod FOREIGN KEY FK_RequestCategory_SamplePrepMethod (idSamplePrepMethod)
    REFERENCES gnomex.SamplePrepMethod (idSamplePrepMethod)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION;
    
-- Add isSampleBarcodingOptional
ALTER TABLE gnomex.RequestCategory add column isSampleBarcodingOptional CHAR(1) null;
