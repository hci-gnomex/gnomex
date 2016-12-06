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
  isDefault VARCHAR(1) NOT NULL DEFAULT ('N'),
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


---------------------------------------------------------
-- Add initial rows to Pipeline Protocol table for HTG --
---------------------------------------------------------

INSERT INTO PipelineProtocol (description, idCoreFacility, protocol, isDefault) VALUES
  ('Single base mismatch', 1, 'Single base mismatch', 'Y'),
  ('Zero base mismatch', 1, 'Zero base mismatch', 'N'),
  ('No demultiplexing', 1, 'No demultiplexing', 'N'),
  ('Second barcode random N-mer', 1, 'Second barcode random N-mer', 'N');
