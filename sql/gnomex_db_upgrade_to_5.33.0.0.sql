use gnomex;


-- Add constraint for uniqueness on corefacility / description
ALTER TABLE ProductType ADD CONSTRAINT UNQ_ProductType_idCoreFacility_description
    UNIQUE (idCoreFacility, description)
