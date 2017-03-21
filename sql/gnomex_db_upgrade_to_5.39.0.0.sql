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

INSERT INTO gnomex.PropertyDictionary (propertyName,propertyValue,propertyDescription, forServerOnly)
VALUES ('billing_account_exp_email','N','Reminder email sent to out to labs for billing accounts to expire within the month','Y');

ALTER TABLE BillingAccount
ADD activeAccount char(1) default 'Y';

