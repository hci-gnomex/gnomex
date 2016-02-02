use gnomex;


-- Add constraint for uniqueness on corefacility / description
ALTER TABLE ProductType ADD CONSTRAINT UNQ_ProductType_idCoreFacility_description
    UNIQUE (idCoreFacility, description);
	

-- Note that this might fail if there are duplicates in the DB already
-- We will have to null out values where email is not unique.
ALTER TABLE AppUser ADD CONSTRAINT UNQ_AppUser_email UNIQUE (email);
