use gnomex;

-- uuid column on Request
alter table Request add uuid varchar(36) NULL;

-- Add properties for seq setup tab
Insert Into PropertyDictionary(propertyName, propertyValue, propertyDescription, forServerOnly)
  values('hiseq_run_type_label_standard', 'HiSeq High Output Run Mode', 'Label for sequencing cycles on seq setup screen for numberSequencingCyclesAllowed with isCustom=N', 'N');
Insert Into PropertyDictionary(propertyName, propertyValue, propertyDescription, forServerOnly)
  values('hiseq_run_type_label_custom', 'HiSeq Rapid Run Mode', 'Label for sequencing cycles on seq setup screen for numberSequencingCyclesAllowed with isCustom=Y', 'N');
Insert Into PropertyDictionary(propertyName, propertyValue, propertyDescription, forServerOnly)
  values('hiseq_run_type_custom_warning', 'Warning: These options are only available if you fill all lanes of the flow cells.', 'warning text for sequencing cycles on seq setup screen for numberSequencingCyclesAllowed with isCustom=Y', 'N');

