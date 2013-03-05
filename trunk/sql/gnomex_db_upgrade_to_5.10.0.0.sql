use gnomex;

-- Add sortOrder to workflow step table
alter table RequestCategory add isClinicalResearch CHAR(1) null;
