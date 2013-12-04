// Make billing account nullable on DiskUsageByMonth
alter table DiskUsageByMonth MODIFY idBillingAccount INT(10) NULL;
