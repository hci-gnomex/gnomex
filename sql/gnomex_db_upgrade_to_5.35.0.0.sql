use gnomex;

-- Add description to Product
ALTER TABLE Product ADD COLUMN description VARCHAR(500) NULL;
CALL ExecuteIfTableExists('gnomex', 'Product_Audit', 'ALTER TABLE Product_Audit ADD COLUMN description VARCHAR(500) NULL');

ALTER TABLE ProductType
DROP COLUMN utilizePurchasingSystem;
CALL ExecuteIfTableExists('gnomex', 'ProductType_Audit', 'ALTER TABLE ProductType_Audit DROP COLUMN utilizePurchasingSystem');

ALTER TABLE ProductOrder
DROP COLUMN uuid;
CALL ExecuteIfTableExists('gnomex', 'ProductOrder_Audit', 'ALTER TABLE ProductOrder_Audit DROP COLUMN uuid');

ALTER TABLE BillingItem
DROP COLUMN tag;
CALL ExecuteIfTableExists('gnomex', 'BillingItem_Audit', 'ALTER TABLE BillingItem_Audit DROP COLUMN tag');

-- Change displayName to fileName on Chromatogram
ALTER TABLE Chromatogram change column displayName fileName VARCHAR(200) NULL;
call ExecuteIfTableExists('gnomex','Chromatogram_Audit','alter table Chromatogram_Audit change column displayName fileName VARCHAR(200) NULL;');