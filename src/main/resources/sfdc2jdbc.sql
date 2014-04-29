CREATE DATABASE  IF NOT EXISTS `sfdc2jdbc`;
USE `sfdc2jdbc`;

--
-- Table structure for table `Account`
--

DROP TABLE IF EXISTS `Account`;

CREATE TABLE `Account` (
  `Id` varchar(255) NOT NULL,
  `AccountNumber` varchar(255) DEFAULT NULL,
  `Description` text,
  `Industry` varchar(255) DEFAULT NULL,
  `Name` varchar(255) NOT NULL,
  `NumberOfEmployees` int(11) DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
