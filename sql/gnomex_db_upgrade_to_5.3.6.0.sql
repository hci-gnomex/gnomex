use gnomex;

--Rename TotalPrice to InvoicePrice.
alter table BillingItem change totalPrice invoicePrice decimal(8,2) not null;
