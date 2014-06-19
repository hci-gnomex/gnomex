use gnomex;

insert into RequestCategoryType values('GENERIC','Generic','assets/flask.png','N','N');


-- New AppUser columns for security
ALTER TABLE AppUser ADD salt VARCHAR(300) NULL;
call ExecuteIfTableExists('gnomex','AppUser_Audit','alter table AppUser_Audit add salt VARCHAR(300) null');
ALTER TABLE AppUser ADD guid VARCHAR(100) NULL;
call ExecuteIfTableExists('gnomex','AppUser_Audit','alter table AppUser_Audit add guid VARCHAR(100) null');
ALTER TABLE AppUser ADD guidExpiration DATETIME NULL;
call ExecuteIfTableExists('gnomex','AppUser_Audit','alter table AppUser_Audit add guidExpiration DATETIME null');
ALTER TABLE AppUser ADD passwordExpired CHAR(1) NULL;
call ExecuteIfTableExists('gnomex','AppUser_Audit','alter table AppUser_Audit add passwordExpired CHAR(1) null');

-- Sort order for annotations
ALTER TABLE Property ADD sortOrder INT(10) NULL;
call ExecuteIfTableExists('gnomex','Property_Audit','alter table Property_Audit add sortOrder INT(10) null');
