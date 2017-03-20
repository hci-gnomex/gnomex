use gnomex;

-----------------------------------
-- Create table WorkflowProperty --
-----------------------------------

DROP TABLE IF EXISTS WorkflowProperty;
CREATE TABLE WorkflowProperty (
  idWorkflowProperty INT(10) NOT NULL AUTO_INCREMENT Primary Key,
  workflowPropertyName VARCHAR(50) NOT NULL,
  workflowPropertyValue VARCHAR(50) NOT NULL,
  codeRequestCategory VARCHAR(50)
)
ENGINE = INNODB;

INSERT INTO WorkflowProperty(workflowPropertyName, workflowPropertyValue, codeRequestCategory)
VALUES ('SUBMITTED', 'Submitted', '');
INSERT INTO WorkflowProperty(workflowPropertyName, workflowPropertyValue, codeRequestCategory)
VALUES ('SAMPLEQC', 'Sample QC', '');
INSERT INTO WorkflowProperty(workflowPropertyName, workflowPropertyValue, codeRequestCategory)
VALUES ('LIBRARYPREP', 'Library Prep', '');
INSERT INTO WorkflowProperty(workflowPropertyName, workflowPropertyValue, codeRequestCategory)
VALUES ('LIBRARYPREPQC', 'Library Prep QC', '');
INSERT INTO WorkflowProperty(workflowPropertyName, workflowPropertyValue, codeRequestCategory)
VALUES ('READYTOSEQUENCE', 'Ready to Sequence', '');
INSERT INTO WorkflowProperty(workflowPropertyName, workflowPropertyValue, codeRequestCategory)
VALUES ('SEQUENCEINPROGRESS', 'Sequence in Progress', '');
INSERT INTO WorkflowProperty(workflowPropertyName, workflowPropertyValue, codeRequestCategory)
VALUES ('DATAAVAILABLE', 'Data Available', '');
INSERT INTO WorkflowProperty(workflowPropertyName, workflowPropertyValue, codeRequestCategory)
VALUES ('LABELING', 'Labeling', '');
INSERT INTO WorkflowProperty(workflowPropertyName, workflowPropertyValue, codeRequestCategory)
VALUES ('HYBRIDIZATION', 'Hybridization', '');
INSERT INTO WorkflowProperty(workflowPropertyName, workflowPropertyValue, codeRequestCategory)
VALUES ('EXTRACTION', 'Extraction', '');

ALTER TABLE BillingAccount
ADD activeAccount char(1) default 'Y';

INSERT INTO PropertyDictionary(propertyName, propertyValue, propertyDescription, forServerOnly, idCoreFacility)
VALUES ('exclude_new_requests', 'Y', 'Excludes requests from billing that are in the NEW category', 'N', 2);

ALTER TABLE Request
modify reagent VARCHAR(100);

ALTER TABLE Request
modify elutionBuffer VARCHAR(100);

ALTER TABLE Request_Audit
modify reagent VARCHAR(100);

ALTER TABLE Request_Audit
modify elutionBuffer VARCHAR(100);

