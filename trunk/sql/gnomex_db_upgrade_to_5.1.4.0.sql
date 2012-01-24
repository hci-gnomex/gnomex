-- Add columns to NumberSequencingCyclesAllowed
ALTER TABLE gnomex.NumberSequencingCyclesAllowed add column idSeqRunType INT(10) null;
ALTER TABLE gnomex.NumberSequencingCyclesAllowed add column name varchar(100)  null;
ALTER TABLE gnomex.NumberSequencingCyclesAllowed add column notes varchar(500) null;
ALTER TABLE NumberSequencingCyclesAllowed  add CONSTRAINT FK_NumberSequencingCyclesAllowed_SeqRunType FOREIGN KEY FK_NumberSequencingCyclesAllowed_SeqRunType (idSeqRunType)
    REFERENCES gnomex.SeqRunType (idSeqRunType)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION;
    
