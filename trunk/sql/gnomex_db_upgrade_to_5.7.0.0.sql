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

