use gnomex;

--Rename TotalPrice to InvoicePrice.
alter table BillingItem change totalPrice invoicePrice decimal(8,2) not null;

-- Add splitType
alter table BillingItem add column  `splitType` char(1) NULL;

-- Initialize split type
update BillingItem set splitType='%' where splitType is null

