use gnomex;

-- Create missing PropertyEntryOption entries for sample annotations of type 'OPTION'
insert into PropertyEntryOption
(idPropertyEntry, idPropertyOption)
select pe.idPropertyEntry, pe.valueString as idPropertyOption
from PropertyEntry pe
  left join Property p
  on p.idProperty = pe.idProperty
  join PropertyOption po
  on po.idPropertyOption = pe.valueString
where idSample is not null
and codePropertyType = 'OPTION';

-- Set help url to GNomEx Wiki
update PropertyDictionary set propertyValue = 'http://hci-bio-wiki/biowiki' where propertyName = 'help_url';


-- Change the foreign key constraint rules for the following tables so that user can delete institutions
-- and not receive FK constraint violations.
ALTER TABLE DataTrack DROP CONSTRAINT FK_DataTrack_Institution;
alter table DataTrack add constraint FK_DataTrack_Institution 
    foreign key (idInstitution) references Institution(idInstitution) on delete set null;
    
ALTER TABLE Analysis DROP CONSTRAINT FK_Analysis_Institution;
alter table Analysis add constraint FK_Analysis_Institution 
    foreign key (idInstitution) references Institution(idInstitution) on delete set null;
    
ALTER TABLE Topic DROP CONSTRAINT FK_Topic_Institution;
alter table Topic add constraint FK_Topic_Institution 
    foreign key (idInstitution) references Institution(idInstitution) on delete set null;  
    
ALTER TABLE Request DROP CONSTRAINT FK_Request_Institution;
alter table Request add constraint FK_Request_Institution 
    foreign key (idInstitution) references Institution(idInstitution) on delete set null;  


-- Create PropertyPlatformApplication from PlatformApplication table
-- and then delete PlatformApplication   

INSERT INTO PropertyPlatformApplication SELECT * FROM PlatformApplication;

DROP TABLE PlatformApplication;

-- Add a property for das2_url (should have been added way back when datatracks introduced....
INSERT INTO PropertyDictionary (propertyName,propertyValue,propertyDescription, forServerOnly) VALUES
('das2_url', 'Y', 'http://localhost:8080/das2gnomex/genome', 'N');


ALTER TABLE Analysis DROP CONSTRAINT FK_Analysis_AnalysisProtocol;
alter table Analysis add constraint FK_Analysis_AnalysisProtocol 
    foreign key (idAnalysisProtocol) references AnalysisProtocol(idAnalysisProtocol) on delete set null;   
        
ALTER TABLE Sample DROP CONSTRAINT FK_Sample_SeqLibProtocol;
alter table Sample add constraint FK_Sample_SeqLibProtocol 
    foreign key (idSeqLibProtocol) references SeqLibProtocol(idSeqLibProtocol) on delete set null;
    

alter table SeqLibProtocolApplication add constraint FK_SeqLibProtocolApplication_SeqLibProtocol 
    foreign key (idSeqLibProtocol) references SeqLibProtocol(idSeqLibProtocol) on delete cascade;          
     
ALTER TABLE Hybridization DROP CONSTRAINT FK_Hybridization_ScanProtocol;
alter table Hybridization add constraint FK_Hybridization_ScanProtocol 
    foreign key (idScanProtocol) references ScanProtocol(idScanProtocol) on delete set null; 
    
ALTER TABLE RequestCategoryApplication DROP foreign key FK_RequestCategoryApplication_ScanProtocol;
alter table RequestCategoryApplication add constraint FK_RequestCategoryApplication_ScanProtocol 
    foreign key (idScanProtocolDefault) references ScanProtocol(idScanProtocol) on delete set null;


alter table SampleTypeApplication add constraint FK_SampleTypeMicroarrayCategory_ScanProtocol 
    foreign key (idScanProtocolDefault) references ScanProtocol(idScanProtocol) on delete set null;
    
ALTER TABLE LabeledSample DROP foreign key FK_LabeledSample_LabelingProtocol;
alter table LabeledSample add constraint FK_LabeledSample_LabelingProtocol 
    foreign key (idLabelingProtocol) references LabelingProtocol(idLabelingProtocol) on delete set null;
    
ALTER TABLE RequestCategoryApplication DROP foreign key FK_RequestCategoryApplication_LabelingProtocol;
alter table RequestCategoryApplication add constraint FK_RequestCategoryApplication_LabelingProtocol 
    foreign key (idLabelingProtocolDefault) references LabelingProtocol(idLabelingProtocol) on delete set null;

alter table SampleTypeApplication add constraint FK_SampleTypeMicroarrayCategory_LabelingProtocol 
    foreign key (idLabelingProtocolDefault) references LabelingProtocol(idLabelingProtocol) on delete set null;
    

alter table SampleTypeApplication add constraint FK_SampleTypeMicroarrayCategory_HybProtocol 
    foreign key (idHybProtocolDefault) references HybProtocol(idHybProtocol) on delete set null;
    
ALTER TABLE RequestCategoryApplication DROP foreign key FK_RequestCategoryApplication_HybProtocol;
alter table RequestCategoryApplication add constraint FK_RequestCategoryApplication_HybProtocol 
    foreign key (idHybProtocolDefault) references HybProtocol(idHybProtocol) on delete set null;
      
ALTER TABLE Hybridization DROP foreign key FK_Hybridization_HybProtocol;
alter table Hybridization add constraint FK_Hybridization_HybProtocol 
    foreign key (idHybProtocol) references HybProtocol(idHybProtocol) on delete set null;
    
ALTER TABLE Hybridization DROP foreign key FK_Hybridization_FeatureExtractionProtocol;
alter table Hybridization add constraint FK_Hybridization_FeatureExtractionProtocol 
    foreign key (idFeatureExtractionProtocol) references FeatureExtractionProtocol(idFeatureExtractionProtocol) on delete set null;                                      
    
ALTER TABLE RequestCategoryApplication DROP foreign key FK_RequestCategoryApplication_FeatureExtractionProtocol;
alter table RequestCategoryApplication add constraint FK_RequestCategoryApplication_FeatureExtractionProtocol 
    foreign key (idFeatureExtractionProtocolDefault) references FeatureExtractionProtocol(idFeatureExtractionProtocol) on delete set null;    
    

alter table SampleTypeApplication add constraint FK_SampleTypeMicroarrayCategory_FeatureExtractionProtocol 
    foreign key (idFeatureExtractionProtocolDefault) references FeatureExtractionProtocol(idFeatureExtractionProtocol) on delete set null;



-- New property for lucene global search
insert into PropertyDictionary(propertyName, propertyValue, propertyDescription, forServerOnly)
  values('lucene_global_index_directory','/home/gnomex/luceneIndex/Global/','The file directory for storing lucene index files on combined data for all object types.','Y');
