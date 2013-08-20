use gnomex;

--New property which is a note that is appended to the "sequencing complete" email that notifies users who to contact if they have
--any further questions about the processing of their request. 
insert into PropertyDictionary(propertyName, propertyValue, propertyDescription, forServerOnly, forSample, forDataTrack, forAnnotation) 
  values('analysis_assistance_note', 'If you would like data analysis assistance from the Bioinformatics Shared Resource, please contact them via email at bioinformaticshelp@bio.hci.utah.edu . Please include the sequencing or microarray request number, the aims of the experiment, and any specific analysis questions you would like to have addressed.', 'Note that is sent when sequencing of an experiment is complete notifiying user who to contact if they have further questions', 'N', 'N', 'N', 'N');
