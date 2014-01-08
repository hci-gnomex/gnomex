use gnomex;

// Make billing account nullable on DiskUsageByMonth
alter table DiskUsageByMonth change idBillingAccount idBillingAccount INT(10) NULL;

-- Procedure to modify columns in audit tables if they exist.
drop procedure if exists ExecuteIfTableExists;
delimiter '//'

create procedure ExecuteIfTableExists(
  IN dbName tinytext,
  IN tableName tinytext,
  IN statement text)
begin
  IF EXISTS (SELECT * FROM information_schema.TABLES WHERE table_name=tableName and table_schema=dbName)
  THEN
    set @ddl=statement;
    prepare stmt from @ddl;
    execute stmt;
  END IF;
end;
//

delimiter ';'

alter table gnomex.FlowCell add idCoreFacility int(10) NULL;
call ExecuteIfTableExists('gnomex','FlowCell_Audit','alter table gnomex.FlowCell_Audit add idCoreFacility int(10) NULL');
alter table gnomex.FlowCell add CONSTRAINT FK_FlowCell_CoreFacility 
  FOREIGN KEY FK_FlowCell_CoreFacility (idCoreFacility)
    REFERENCES gnomex.CoreFacility (idCoreFacility)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION;

    use gnomex;

-- Add owner only flag to request category
alter table RequestCategory add isOwnerOnly CHAR(1) null;
call ExecuteIfTableExists('gnomex','RequestCategory_Audit','alter table gnomex.RequestCategory_Audit add isOwnerOnly CHAR(1) null');
