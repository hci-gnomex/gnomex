use gnomex;

-- Add description to Product
ALTER TABLE Product ADD COLUMN description VARCHAR(500) NULL;
CALL ExecuteIfTableExists('gnomex', 'Product_Audit', 'ALTER TABLE Product_Audit ADD COLUMN description VARCHAR(500) NULL');