use gnomex;

insert into RequestCategoryType values('GENERIC','Generic','assets/flask.png','N','N');


-- New AppUser columns for security
ALTER TABLE AppUser ADD salt VARCHAR(300) NULL;
ALTER TABLE AppUser ADD guid VARCHAR(100) NULL;
ALTER TABLE AppUser ADD guidExpiration DATETIME NULL;
ALTER TABLE AppUser ADD passwordExpired CHAR(1) NULL;