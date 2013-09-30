INSERT INTO `gnomex`.`Property` (`idProperty`,`propertyName`,`propertyValue`,`propertyDescription`, `forServerOnly`) VALUES
 (48, 'fdt_supported', 'N', 'Flag indicating if FDT is supported.', 'N'),
 (49, 'http_port', '80', 'The port number that non-secure (http) runs on.', 'Y'),
 (50, 'fdt_directory', '', 'The fdt staging directory (file path) that is accessible from the fdt server.', 'N'),
 (51, 'fdt_directory_gnomex', '', 'The fdt staging directory (file path) that is accessible from gnomex application server.', 'N'),
 (52, 'fdt_client_codebase', '', 'The URL that the fdtClient.jar is served from.', 'N'),
 (53, 'fdt_server_name', '', 'The server name that the fdt server is running on', 'N');



INSERT INTO `gnomex`.`Property` (`propertyName`,`propertyValue`,`propertyDescription`, `forServerOnly`) 
 select 'experiment_read_directory', prop.propertyValue, prop.propertyDescription, 'Y'
 from Property prop
 where prop.propertyName = 'experiment_directory';

INSERT INTO `gnomex`.`Property` (`propertyName`,`propertyValue`,`propertyDescription`, `forServerOnly`) 
 select 'experiment_write_directory', prop.propertyValue, prop.propertyDescription, 'Y'
 from Property prop
 where prop.propertyName = 'experiment_directory';


INSERT INTO `gnomex`.`Property` (`propertyName`,`propertyValue`,`propertyDescription`, `forServerOnly`) 
 select 'analysis_read_directory', prop.propertyValue, prop.propertyDescription, 'Y'
 from Property prop
 where prop.propertyName = 'analysis_directory';

INSERT INTO `gnomex`.`Property` (`propertyName`,`propertyValue`,`propertyDescription`, `forServerOnly`) 
 select 'analysis_write_directory', prop.propertyValue, prop.propertyDescription, 'Y'
 from Property prop
 where prop.propertyName = 'analysis_directory';
