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


INSERT INTO ContextSensitiveHelp(context1, context2, context3, helpText, toolTipText)
VALUES ('SampleDetailsOrganism', '1', '', '<TEXTFORMAT LEADING="2"><P ALIGN="LEFT"><FONT FACE="Open Sans" SIZE="12" COLOR="#2E2D2C" LETTERSPACING="0" KERNING="0">If you wish to add a new organism</FONT></P></TEXTFORMAT><TEXTFORMAT LEADING="2"><P ALIGN="LEFT"><FONT FACE="Open Sans" SIZE="12" COLOR="#2E2D2C" LETTERSPACING="0" KERNING="0">which does not exist in the list, simply type</FONT></P></TEXTFORMAT><TEXTFORMAT LEADING="2"><P ALIGN="LEFT"><FONT FACE="Open Sans" SIZE="12" COLOR="#2E2D2C" LETTERSPACING="0" KERNING="0">it&apos;s name in the box and hit enter. </</FONT></P></TEXTFORMAT><TEXTFORMAT LEADING="2"><P ALIGN="LEFT"><FONT FACE="Open Sans" SIZE="12" COLOR="#2E2D2C" LETTERSPACING="0" KERNING="0">That will bring up the new  organism</</FONT></P></TEXTFORMAT><TEXTFORMAT LEADING="2"><P ALIGN="LEFT"><FONT FACE="Open Sans" SIZE="12" COLOR="#2E2D2C" LETTERSPACING="0" KERNING="0">dialog.  </</FONT></P></TEXTFORMAT>','<TEXTFORMAT LEADING="2"><P ALIGN="LEFT"><FONT FACE="Open Sans" SIZE="12" COLOR="#2E2D2C" LETTERSPACING="0" KERNING="0">Organism Dialog</FONT></P></TEXTFORMAT>');

INSERT INTO ContextSensitiveHelp(context1, context2, context3, helpText, toolTipText)
VALUES ('SampleDetailsOrganism', '2', '', '<TEXTFORMAT LEADING="2"><P ALIGN="LEFT"><FONT FACE="Open Sans" SIZE="12" COLOR="#2E2D2C" LETTERSPACING="0" KERNING="0">If you wish to add a new organism</FONT></P></TEXTFORMAT><TEXTFORMAT LEADING="2"><P ALIGN="LEFT"><FONT FACE="Open Sans" SIZE="12" COLOR="#2E2D2C" LETTERSPACING="0" KERNING="0">which does not exist in the list, simply type</FONT></P></TEXTFORMAT><TEXTFORMAT LEADING="2"><P ALIGN="LEFT"><FONT FACE="Open Sans" SIZE="12" COLOR="#2E2D2C" LETTERSPACING="0" KERNING="0">it&apos;s name in the box and hit enter. </</FONT></P></TEXTFORMAT><TEXTFORMAT LEADING="2"><P ALIGN="LEFT"><FONT FACE="Open Sans" SIZE="12" COLOR="#2E2D2C" LETTERSPACING="0" KERNING="0">That will bring up the new  organism</</FONT></P></TEXTFORMAT><TEXTFORMAT LEADING="2"><P ALIGN="LEFT"><FONT FACE="Open Sans" SIZE="12" COLOR="#2E2D2C" LETTERSPACING="0" KERNING="0">dialog.  </</FONT></P></TEXTFORMAT>','<TEXTFORMAT LEADING="2"><P ALIGN="LEFT"><FONT FACE="Open Sans" SIZE="12" COLOR="#2E2D2C" LETTERSPACING="0" KERNING="0">Organism Dialog</FONT></P></TEXTFORMAT>');

INSERT INTO ContextSensitiveHelp(context1, context2, context3, helpText, toolTipText)
VALUES ('SampleDetailsOrganism', '3', '', '<TEXTFORMAT LEADING="2"><P ALIGN="LEFT"><FONT FACE="Open Sans" SIZE="12" COLOR="#2E2D2C" LETTERSPACING="0" KERNING="0">If you wish to add a new organism</FONT></P></TEXTFORMAT><TEXTFORMAT LEADING="2"><P ALIGN="LEFT"><FONT FACE="Open Sans" SIZE="12" COLOR="#2E2D2C" LETTERSPACING="0" KERNING="0">which does not exist in the list, simply type</FONT></P></TEXTFORMAT><TEXTFORMAT LEADING="2"><P ALIGN="LEFT"><FONT FACE="Open Sans" SIZE="12" COLOR="#2E2D2C" LETTERSPACING="0" KERNING="0">it&apos;s name in the box and hit enter. </</FONT></P></TEXTFORMAT><TEXTFORMAT LEADING="2"><P ALIGN="LEFT"><FONT FACE="Open Sans" SIZE="12" COLOR="#2E2D2C" LETTERSPACING="0" KERNING="0">That will bring up the new  organism</</FONT></P></TEXTFORMAT><TEXTFORMAT LEADING="2"><P ALIGN="LEFT"><FONT FACE="Open Sans" SIZE="12" COLOR="#2E2D2C" LETTERSPACING="0" KERNING="0">dialog.  </</FONT></P></TEXTFORMAT>','<TEXTFORMAT LEADING="2"><P ALIGN="LEFT"><FONT FACE="Open Sans" SIZE="12" COLOR="#2E2D2C" LETTERSPACING="0" KERNING="0">Organism Dialog</FONT></P></TEXTFORMAT>');

INSERT INTO ContextSensitiveHelp(context1, context2, context3, helpText, toolTipText)
VALUES ('SampleDetailsOrganism', '4', '', '<TEXTFORMAT LEADING="2"><P ALIGN="LEFT"><FONT FACE="Open Sans" SIZE="12" COLOR="#2E2D2C" LETTERSPACING="0" KERNING="0">If you wish to add a new organism</FONT></P></TEXTFORMAT><TEXTFORMAT LEADING="2"><P ALIGN="LEFT"><FONT FACE="Open Sans" SIZE="12" COLOR="#2E2D2C" LETTERSPACING="0" KERNING="0">which does not exist in the list, simply type</FONT></P></TEXTFORMAT><TEXTFORMAT LEADING="2"><P ALIGN="LEFT"><FONT FACE="Open Sans" SIZE="12" COLOR="#2E2D2C" LETTERSPACING="0" KERNING="0">it&apos;s name in the box and hit enter. </</FONT></P></TEXTFORMAT><TEXTFORMAT LEADING="2"><P ALIGN="LEFT"><FONT FACE="Open Sans" SIZE="12" COLOR="#2E2D2C" LETTERSPACING="0" KERNING="0">That will bring up the new  organism</</FONT></P></TEXTFORMAT><TEXTFORMAT LEADING="2"><P ALIGN="LEFT"><FONT FACE="Open Sans" SIZE="12" COLOR="#2E2D2C" LETTERSPACING="0" KERNING="0">dialog.  </</FONT></P></TEXTFORMAT>','<TEXTFORMAT LEADING="2"><P ALIGN="LEFT"><FONT FACE="Open Sans" SIZE="12" COLOR="#2E2D2C" LETTERSPACING="0" KERNING="0">Organism Dialog</FONT></P></TEXTFORMAT>');

INSERT INTO gnomex.PropertyDictionary (propertyName,propertyValue,propertyDescription, forServerOnly)
VALUES ('billing_account_exp_email','N','Reminder email sent to out to labs for billing accounts to expire within the month','Y');

ALTER TABLE BillingAccount
ADD activeAccount char(1) default 'Y';

INSERT INTO PropertyDictionary(propertyName, propertyValue, propertyDescription, forServerOnly, idCoreFacility)
VALUES ('exclude_new_requests', 'N', 'Excludes requests from billing that are in the NEW category', 'N', NULL);

ALTER TABLE Request
modify reagent VARCHAR(100);

ALTER TABLE Request
modify elutionBuffer VARCHAR(100);

ALTER TABLE Request_Audit
modify reagent VARCHAR(100);

ALTER TABLE Request_Audit
modify elutionBuffer VARCHAR(100);



