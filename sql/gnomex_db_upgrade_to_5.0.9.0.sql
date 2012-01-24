-- Add new Table Institution
CREATE TABLE gnomex.Institution ( 
    idInstitution	INT(10) NOT NULL AUTO_INCREMENT,
    institution 	varchar(200) NOT NULL,
    description  	varchar(500) NULL,
    isActive     	char(1) NULL,
    PRIMARY KEY (idInstitution)
    ) ENGINE = INNODB;;
    
    
-- Add new Table to link lab to multiple institutions
 CREATE TABLE gnomex.InstitutionLab ( 
     idInstitution	INT(10),
     idLab           INT(10),
     PRIMARY KEY (idInstitution, idLab),
    CONSTRAINT FK_InstitutionLab_Institution FOREIGN KEY FK_InstitutionLab_Institution (idInstitution)
    REFERENCES gnomex.Institution (idInstitution)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
    CONSTRAINT FK_InstitutionLab_Lab FOREIGN KEY FK_InstitutionLab_Lab (idLab)
    REFERENCES gnomex.Lab (idLab)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
    ) ENGINE = INNODB;;
    
-- Add new Institution visibility level    
-- INSERT INTO gnomex.Visibility (codeVisibility, visibility) values ('INST', 'Institution');

-- Add idInstitution to Experiment
ALTER TABLE gnomex.Request add column idInstitution INT(10);
ALTER TABLE gnomex.Request add CONSTRAINT FK_Request_Institution FOREIGN KEY FK_Request_Institution (idInstitution)
    REFERENCES gnomex.Institution (idInstitution)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION;
    
    
-- Add idInstitution to Analysis
ALTER TABLE gnomex.Analysis add column idInstitution INT(10);
ALTER TABLe gnomex.Analysis add CONSTRAINT FK_Analysis_Institution FOREIGN KEY FK_Analysis_Institution (idInstitution)
    REFERENCES gnomex.Institution (idInstitution)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION;



    