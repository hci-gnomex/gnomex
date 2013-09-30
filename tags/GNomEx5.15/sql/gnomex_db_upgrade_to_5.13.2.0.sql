use gnomex;

-- barcodeSequenceB column on Sample
alter table Sample add barcodeSequenceB varchar(20) NULL;

-- Remove meanLibSizeActual column from Request
alter table Request drop column meanLibSizeActual;