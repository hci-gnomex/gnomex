use gnomex;

-- Add approved billing status for credit cards.
insert into BillingStatus (codeBillingStatus, billingStatus, isActive) 
  values ('APPROVEDCC', 'Approved (Credit Card)', 'Y');