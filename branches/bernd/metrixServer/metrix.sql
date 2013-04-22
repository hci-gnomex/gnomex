-- Illumina Metrix Server 
-- Netherlands Cancer Institute
-- Bernd van der Veen - 2012 / 2013
-- For license details please see LICENSE.TXT
-- SQL Script for Metrix Server object table --

DROP DATABASE IF EXISTS `metrix`;
CREATE DATABASE `metrix`;
USE `metrix`;
DROP TABLE IF EXISTS `metrix_objects`;
CREATE TABLE `metrix`.`metrix_objects` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `run_id` varchar(512) DEFAULT NULL,
  `object_value` blob,
  `state` enum('1','2','3','4','5') DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=UTF8;

