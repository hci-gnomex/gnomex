use gnomex;


-----------------------------------
-- Create table PipelineProtocol --
-----------------------------------

DROP TABLE IF EXISTS PipelineProtocol;
CREATE TABLE PipelineProtocol (
  idPipelineProtocol INT(10) NOT NULL AUTO_INCREMENT,
  description LONGTEXT NULL,
  idCoreFacility INT(10) NOT NULL,
  protocol VARCHAR(50) NOT NULL,
  isDefault VARCHAR(1) NOT NULL DEFAULT 'N',
  PRIMARY KEY (idPipelineProtocol),
  CONSTRAINT FK_PipelineProtocol_CoreFacility FOREIGN KEY FK_PipelineProtocol_CoreFacility (idCoreFacility)
    REFERENCES gnomex.CoreFacility (idCoreFacility)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;


------------------------------------------------------
-- Add FK column FlowCellChannel.idPipelineProtocol --
------------------------------------------------------

ALTER TABLE FlowCellChannel
ADD COLUMN idPipelineProtocol INT(10) NULL;

ALTER TABLE FlowCellChannel ADD
  CONSTRAINT FK_FlowCellChannel_PipelineProtocol FOREIGN KEY FK_FlowCellChannel_PipelineProtocol (idPipelineProtocol)
    REFERENCES gnomex.PipelineProtocol (idPipelineProtocol)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION;

CALL ExecuteIfTableExists('gnomex','FlowCellChannel_Audit','ALTER TABLE FlowCellChannel_Audit ADD COLUMN idPipelineProtocol INT(10) NULL');



-----------------------------------------------
-- Drop column BillingTemplateItem.sortOrder --
-----------------------------------------------

ALTER TABLE BillingTemplateItem
DROP COLUMN sortOrder;

CALL ExecuteIfTableExists('gnomex', 'BillingTemplateItem_Audit', 'ALTER TABLE BillingTemplateItem_Audit DROP COLUMN sortOrder');

ALTER TABLE BillingTemplate ADD COLUMN isActive char(1) null;
CALL ExecuteIfTableExists('gnomex', 'BillingTemplate_Audit', 'ALTER TABLE BillingTemplate_Audit ADD COLUMN isActive char(1) null');
