-- Fix error in ddl script, moving name from OligoBarcodeScheme to OligoBarcode
ALTER TABLE gnomex.OligoBarcodeScheme drop column name;
ALTER TABLE gnomex.OligoBarcode add column name VARCHAR(50) NULL;

UPDATE gnomex.OligoBarcode set name = 'GAII Tag 1', sortOrder = 1 where idOligoBarcode = 1;
UPDATE gnomex.OligoBarcode set name = 'GAII Tag 2', sortOrder = 2 where idOligoBarcode = 2;
UPDATE gnomex.OligoBarcode set name = 'GAII Tag 3', sortOrder = 3 where idOligoBarcode = 3;
UPDATE gnomex.OligoBarcode set name = 'GAII Tag 4', sortOrder = 4 where idOligoBarcode = 4;
UPDATE gnomex.OligoBarcode set name = 'HiSeq Tag 1', sortOrder = 1 where idOligoBarcode = 5;
UPDATE gnomex.OligoBarcode set name = 'HiSeq Tag 2', sortOrder = 2 where idOligoBarcode = 6;
UPDATE gnomex.OligoBarcode set name = 'HiSeq Tag 3', sortOrder = 3 where idOligoBarcode = 7;
UPDATE gnomex.OligoBarcode set name = 'HiSeq Tag 4', sortOrder = 4 where idOligoBarcode = 8;
UPDATE gnomex.OligoBarcode set name = 'HiSeq Tag 5', sortOrder = 5 where idOligoBarcode = 9;
UPDATE gnomex.OligoBarcode set name = 'HiSeq Tag 6', sortOrder = 6 where idOligoBarcode = 10;
UPDATE gnomex.OligoBarcode set name = 'HiSeq Tag 7', sortOrder = 7 where idOligoBarcode = 11;
UPDATE gnomex.OligoBarcode set name = 'HiSeq Tag 8', sortOrder = 8 where idOligoBarcode = 12;
UPDATE gnomex.OligoBarcode set name = 'HiSeq Tag 9', sortOrder = 9 where idOligoBarcode = 13;
UPDATE gnomex.OligoBarcode set name = 'HiSeq Tag 10', sortOrder = 10 where idOligoBarcode = 14;
UPDATE gnomex.OligoBarcode set name = 'HiSeq Tag 11', sortOrder = 11 where idOligoBarcode = 15;
UPDATE gnomex.OligoBarcode set name = 'HiSeq Tag 12', sortOrder = 12 where idOligoBarcode = 16;


-- Set the type and icon on RequestCategory
ALTER TABLE gnomex.RequestCategory add column icon varchar(200);
ALTER TABLE gnomex.RequestCategory add column type varchar(10);
UPDATE gnomex.RequestCategory set type = 'MICROARRAY' WHERE codeRequestCategory = 'AFFY';
UPDATE gnomex.RequestCategory set icon = 'assets/microarray_chip.png' WHERE codeRequestCategory = 'AFFY';

UPDATE gnomex.RequestCategory set type = 'MICROARRAY' WHERE codeRequestCategory = 'AGIL1';
UPDATE gnomex.RequestCategory set icon = 'assets/microarray_small_single_color.png' WHERE codeRequestCategory = 'AGIL1';

UPDATE gnomex.RequestCategory set type = 'MICROARRAY' WHERE codeRequestCategory = 'AGIL2';
UPDATE gnomex.RequestCategory set icon = 'assets/microarray_small.png' WHERE codeRequestCategory = 'AGIL2';

UPDATE gnomex.RequestCategory set type = 'MICROARRAY' WHERE codeRequestCategory = 'INHOUSE';
UPDATE gnomex.RequestCategory set type = 'MICROARRAY' WHERE codeRequestCategory = 'NIMBLE';
UPDATE gnomex.RequestCategory set type = 'MICROARRAY' WHERE codeRequestCategory = 'OTHER';

UPDATE gnomex.RequestCategory set type = 'QC' WHERE codeRequestCategory = 'QC';
UPDATE gnomex.RequestCategory set icon = 'assets/chart_line.png' WHERE codeRequestCategory = 'QC';

UPDATE gnomex.RequestCategory set type = 'ILLUMINA' WHERE codeRequestCategory = 'SOLEXA';
UPDATE gnomex.RequestCategory set icon = 'assets/DNA_diag.png' WHERE codeRequestCategory = 'SOLEXA';

UPDATE gnomex.RequestCategory set type = 'ILLUMINA' WHERE codeRequestCategory = 'HISEQ';
UPDATE gnomex.RequestCategory set icon = 'assets/DNA_diag_lightening.png' WHERE codeRequestCategory = 'HISEQ';
