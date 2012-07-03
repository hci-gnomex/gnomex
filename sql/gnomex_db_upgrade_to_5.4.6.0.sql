use gnomex;

--Add new fields for commercial pricing
alter table Lab add column `isExternalPricingCommercial` CHAR(1) null;
alter table Price add column `unitPriceExternalCommercial` DECIMAL(6, 2) NULL;

