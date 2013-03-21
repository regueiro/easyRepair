-- MySQL dump 10.13  Distrib 5.1.67, for redhat-linux-gnu (x86_64)
--
-- Host: localhost    Database: easyRepair
-- ------------------------------------------------------
-- Server version	5.1.67

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `CLIENT`
--

DROP TABLE IF EXISTS `CLIENT`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `CLIENT` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` int(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  `surname` varchar(100) DEFAULT NULL,
  `nif` varchar(9) DEFAULT NULL,
  `clientid` varchar(11) DEFAULT NULL,
  `notes` longtext,
  `enabled` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `clientid` (`clientid`),
  UNIQUE KEY `nif` (`nif`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CLIENT`
--

LOCK TABLES `CLIENT` WRITE;
/*!40000 ALTER TABLE `CLIENT` DISABLE KEYS */;
/*!40000 ALTER TABLE `CLIENT` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CLIENT_ADDRESS`
--

DROP TABLE IF EXISTS `CLIENT_ADDRESS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `CLIENT_ADDRESS` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `label` varchar(100) NOT NULL,
  `street` varchar(200) DEFAULT NULL,
  `city` varchar(100) DEFAULT NULL,
  `province` varchar(100) DEFAULT NULL,
  `country` varchar(100) DEFAULT NULL,
  `postalcode` varchar(10) DEFAULT NULL,
  `notes` longtext,
  `CLIENT_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKC705BB6028525785` (`CLIENT_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CLIENT_ADDRESS`
--

LOCK TABLES `CLIENT_ADDRESS` WRITE;
/*!40000 ALTER TABLE `CLIENT_ADDRESS` DISABLE KEYS */;
/*!40000 ALTER TABLE `CLIENT_ADDRESS` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CLIENT_EMAIL`
--

DROP TABLE IF EXISTS `CLIENT_EMAIL`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `CLIENT_EMAIL` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `address` varchar(150) NOT NULL,
  `label` varchar(100) NOT NULL,
  `notes` longtext,
  `CLIENT_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK7EFA5A0828525785` (`CLIENT_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CLIENT_EMAIL`
--

LOCK TABLES `CLIENT_EMAIL` WRITE;
/*!40000 ALTER TABLE `CLIENT_EMAIL` DISABLE KEYS */;
/*!40000 ALTER TABLE `CLIENT_EMAIL` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CLIENT_PHONE`
--

DROP TABLE IF EXISTS `CLIENT_PHONE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `CLIENT_PHONE` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `number` varchar(20) NOT NULL,
  `label` varchar(100) NOT NULL,
  `notes` longtext,
  `CLIENT_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK7F934BDA28525785` (`CLIENT_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CLIENT_PHONE`
--

LOCK TABLES `CLIENT_PHONE` WRITE;
/*!40000 ALTER TABLE `CLIENT_PHONE` DISABLE KEYS */;
/*!40000 ALTER TABLE `CLIENT_PHONE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `EMPLOYEE`
--

DROP TABLE IF EXISTS `EMPLOYEE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `EMPLOYEE` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` int(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  `surname` varchar(100) DEFAULT NULL,
  `nif` varchar(9) DEFAULT NULL,
  `nss` varchar(12) DEFAULT NULL,
  `employeeid` varchar(11) DEFAULT NULL,
  `occupation` varchar(100) DEFAULT NULL,
  `notes` longtext,
  `enabled` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `employeeid` (`employeeid`),
  UNIQUE KEY `nif` (`nif`),
  UNIQUE KEY `nss` (`nss`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `EMPLOYEE`
--

LOCK TABLES `EMPLOYEE` WRITE;
/*!40000 ALTER TABLE `EMPLOYEE` DISABLE KEYS */;
/*!40000 ALTER TABLE `EMPLOYEE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `EMPLOYEE_ADDRESS`
--

DROP TABLE IF EXISTS `EMPLOYEE_ADDRESS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `EMPLOYEE_ADDRESS` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `label` varchar(100) NOT NULL,
  `street` varchar(200) DEFAULT NULL,
  `city` varchar(100) DEFAULT NULL,
  `province` varchar(100) DEFAULT NULL,
  `country` varchar(100) DEFAULT NULL,
  `postalcode` varchar(10) DEFAULT NULL,
  `notes` longtext,
  `EMPLOYEE_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK9C09174376214462` (`EMPLOYEE_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `EMPLOYEE_ADDRESS`
--

LOCK TABLES `EMPLOYEE_ADDRESS` WRITE;
/*!40000 ALTER TABLE `EMPLOYEE_ADDRESS` DISABLE KEYS */;
/*!40000 ALTER TABLE `EMPLOYEE_ADDRESS` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `EMPLOYEE_EMAIL`
--

DROP TABLE IF EXISTS `EMPLOYEE_EMAIL`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `EMPLOYEE_EMAIL` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `address` varchar(150) NOT NULL,
  `label` varchar(100) NOT NULL,
  `notes` longtext,
  `EMPLOYEE_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKFD952AB76214462` (`EMPLOYEE_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `EMPLOYEE_EMAIL`
--

LOCK TABLES `EMPLOYEE_EMAIL` WRITE;
/*!40000 ALTER TABLE `EMPLOYEE_EMAIL` DISABLE KEYS */;
/*!40000 ALTER TABLE `EMPLOYEE_EMAIL` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `EMPLOYEE_PHONE`
--

DROP TABLE IF EXISTS `EMPLOYEE_PHONE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `EMPLOYEE_PHONE` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `number` varchar(20) NOT NULL,
  `label` varchar(100) NOT NULL,
  `notes` longtext,
  `EMPLOYEE_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK1072447D76214462` (`EMPLOYEE_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `EMPLOYEE_PHONE`
--

LOCK TABLES `EMPLOYEE_PHONE` WRITE;
/*!40000 ALTER TABLE `EMPLOYEE_PHONE` DISABLE KEYS */;
/*!40000 ALTER TABLE `EMPLOYEE_PHONE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `INSURANCE_COMPANY`
--

DROP TABLE IF EXISTS `INSURANCE_COMPANY`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `INSURANCE_COMPANY` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` int(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  `nif` varchar(9) DEFAULT NULL,
  `web` varchar(100) DEFAULT NULL,
  `notes` longtext,
  `enabled` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `nif` (`nif`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `INSURANCE_COMPANY`
--

LOCK TABLES `INSURANCE_COMPANY` WRITE;
/*!40000 ALTER TABLE `INSURANCE_COMPANY` DISABLE KEYS */;
/*!40000 ALTER TABLE `INSURANCE_COMPANY` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `INSURANCE_COMPANY_ADDRESS`
--

DROP TABLE IF EXISTS `INSURANCE_COMPANY_ADDRESS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `INSURANCE_COMPANY_ADDRESS` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `label` varchar(100) NOT NULL,
  `street` varchar(200) DEFAULT NULL,
  `city` varchar(100) DEFAULT NULL,
  `province` varchar(100) DEFAULT NULL,
  `country` varchar(100) DEFAULT NULL,
  `postalcode` varchar(10) DEFAULT NULL,
  `notes` longtext,
  `INSURANCE_COMPANY_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK32CD426D45E792D0` (`INSURANCE_COMPANY_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `INSURANCE_COMPANY_ADDRESS`
--

LOCK TABLES `INSURANCE_COMPANY_ADDRESS` WRITE;
/*!40000 ALTER TABLE `INSURANCE_COMPANY_ADDRESS` DISABLE KEYS */;
/*!40000 ALTER TABLE `INSURANCE_COMPANY_ADDRESS` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `INSURANCE_COMPANY_EMAIL`
--

DROP TABLE IF EXISTS `INSURANCE_COMPANY_EMAIL`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `INSURANCE_COMPANY_EMAIL` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `address` varchar(150) NOT NULL,
  `label` varchar(100) NOT NULL,
  `notes` longtext,
  `INSURANCE_COMPANY_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKD522405545E792D0` (`INSURANCE_COMPANY_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `INSURANCE_COMPANY_EMAIL`
--

LOCK TABLES `INSURANCE_COMPANY_EMAIL` WRITE;
/*!40000 ALTER TABLE `INSURANCE_COMPANY_EMAIL` DISABLE KEYS */;
/*!40000 ALTER TABLE `INSURANCE_COMPANY_EMAIL` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `INSURANCE_COMPANY_PHONE`
--

DROP TABLE IF EXISTS `INSURANCE_COMPANY_PHONE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `INSURANCE_COMPANY_PHONE` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `number` varchar(20) NOT NULL,
  `label` varchar(100) NOT NULL,
  `notes` longtext,
  `INSURANCE_COMPANY_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKD5BB322745E792D0` (`INSURANCE_COMPANY_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `INSURANCE_COMPANY_PHONE`
--

LOCK TABLES `INSURANCE_COMPANY_PHONE` WRITE;
/*!40000 ALTER TABLE `INSURANCE_COMPANY_PHONE` DISABLE KEYS */;
/*!40000 ALTER TABLE `INSURANCE_COMPANY_PHONE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `LABOUR`
--

DROP TABLE IF EXISTS `LABOUR`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `LABOUR` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` int(11) NOT NULL,
  `enabled` tinyint(1) NOT NULL DEFAULT '1',
  `name` varchar(100) NOT NULL,
  `description` longtext,
  `price` decimal(10,2) NOT NULL,
  `notes` longtext,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `LABOUR`
--

LOCK TABLES `LABOUR` WRITE;
/*!40000 ALTER TABLE `LABOUR` DISABLE KEYS */;
/*!40000 ALTER TABLE `LABOUR` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `PART`
--

DROP TABLE IF EXISTS `PART`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `PART` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` int(11) NOT NULL,
  `enabled` tinyint(1) NOT NULL DEFAULT '1',
  `make` varchar(100) DEFAULT NULL,
  `model` varchar(100) NOT NULL,
  `category` varchar(100) DEFAULT NULL,
  `price` decimal(10,2) DEFAULT NULL,
  `notes` longtext,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `PART`
--

LOCK TABLES `PART` WRITE;
/*!40000 ALTER TABLE `PART` DISABLE KEYS */;
/*!40000 ALTER TABLE `PART` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `PARTORDER`
--

DROP TABLE IF EXISTS `PARTORDER`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `PARTORDER` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` int(11) NOT NULL,
  `enabled` tinyint(1) NOT NULL DEFAULT '1',
  `ordernumber` varchar(11) DEFAULT NULL,
  `status` varchar(100) DEFAULT NULL,
  `discount` decimal(5,2) DEFAULT NULL,
  `shippingcosts` decimal(10,2) DEFAULT NULL,
  `othercosts` decimal(10,2) DEFAULT NULL,
  `notes` longtext,
  `orderdate` date DEFAULT NULL,
  `estimateddate` date DEFAULT NULL,
  `receiptdate` date DEFAULT NULL,
  `SUPPLIER_Id` bigint(20) NOT NULL,
  `RESPONSIBLE_Id` bigint(20) DEFAULT NULL,
  `WAREHOUSE_Id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ordernumber` (`ordernumber`),
  KEY `FK3B24B31B6A8925EA` (`WAREHOUSE_Id`),
  KEY `FK3B24B31B11901D2A` (`SUPPLIER_Id`),
  KEY `FK3B24B31B32279F1A` (`RESPONSIBLE_Id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `PARTORDER`
--

LOCK TABLES `PARTORDER` WRITE;
/*!40000 ALTER TABLE `PARTORDER` DISABLE KEYS */;
/*!40000 ALTER TABLE `PARTORDER` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `PARTORDER_INVOICE`
--

DROP TABLE IF EXISTS `PARTORDER_INVOICE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `PARTORDER_INVOICE` (
  `ORDER_ID` bigint(20) NOT NULL,
  `invoicenumber` varchar(11) DEFAULT NULL,
  `status` varchar(100) DEFAULT NULL,
  `paymentMethod` varchar(100) DEFAULT NULL,
  `notes` longtext,
  `invoicedate` date DEFAULT NULL,
  `accepteddate` date DEFAULT NULL,
  `estimatedpaymentdate` date DEFAULT NULL,
  `paymentdate` date DEFAULT NULL,
  `RESPONSIBLE_Id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ORDER_ID`),
  UNIQUE KEY `invoicenumber` (`invoicenumber`),
  KEY `FK802ADDE9870D01D7` (`ORDER_ID`),
  KEY `FK802ADDE932279F1A` (`RESPONSIBLE_Id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `PARTORDER_INVOICE`
--

LOCK TABLES `PARTORDER_INVOICE` WRITE;
/*!40000 ALTER TABLE `PARTORDER_INVOICE` DISABLE KEYS */;
/*!40000 ALTER TABLE `PARTORDER_INVOICE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `PARTORDER_PARTLINE`
--

DROP TABLE IF EXISTS `PARTORDER_PARTLINE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `PARTORDER_PARTLINE` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `PART_id` bigint(20) NOT NULL,
  `quantity` int(11) DEFAULT NULL,
  `discount` decimal(5,2) DEFAULT NULL,
  `PARTORDER_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKA61C9F0B4723C2CA` (`PARTORDER_id`),
  KEY `FKA61C9F0BABCE8FCA` (`PART_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `PARTORDER_PARTLINE`
--

LOCK TABLES `PARTORDER_PARTLINE` WRITE;
/*!40000 ALTER TABLE `PARTORDER_PARTLINE` DISABLE KEYS */;
/*!40000 ALTER TABLE `PARTORDER_PARTLINE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `REPAIRORDER`
--

DROP TABLE IF EXISTS `REPAIRORDER`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `REPAIRORDER` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` int(11) NOT NULL,
  `enabled` tinyint(1) NOT NULL DEFAULT '1',
  `ordernumber` varchar(11) DEFAULT NULL,
  `status` varchar(100) DEFAULT NULL,
  `gastanklevel` varchar(100) DEFAULT NULL,
  `description` longtext,
  `kilometres` varchar(10) DEFAULT NULL,
  `notes` longtext,
  `orderdate` date DEFAULT NULL,
  `estimateddate` date DEFAULT NULL,
  `finishdate` date DEFAULT NULL,
  `deliverydate` date DEFAULT NULL,
  `RESPONSIBLE_Id` bigint(20) DEFAULT NULL,
  `VEHICLE_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ordernumber` (`ordernumber`),
  KEY `FKE4AFABC132279F1A` (`RESPONSIBLE_Id`),
  KEY `FKE4AFABC12F2949EF` (`VEHICLE_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `REPAIRORDER`
--

LOCK TABLES `REPAIRORDER` WRITE;
/*!40000 ALTER TABLE `REPAIRORDER` DISABLE KEYS */;
/*!40000 ALTER TABLE `REPAIRORDER` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `REPAIRORDER_ESTIMATE`
--

DROP TABLE IF EXISTS `REPAIRORDER_ESTIMATE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `REPAIRORDER_ESTIMATE` (
  `id` bigint(20) NOT NULL,
  `version` int(11) NOT NULL,
  `estimatenumber` varchar(11) DEFAULT NULL,
  `status` varchar(100) DEFAULT NULL,
  `discount` decimal(5,2) DEFAULT NULL,
  `estimatedate` date DEFAULT NULL,
  `accepteddate` date DEFAULT NULL,
  `notes` longtext,
  `RESPONSIBLE_Id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `estimatenumber` (`estimatenumber`),
  KEY `FKE95A43E6E4AA22D3` (`id`),
  KEY `FKE95A43E632279F1A` (`RESPONSIBLE_Id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `REPAIRORDER_ESTIMATE`
--

LOCK TABLES `REPAIRORDER_ESTIMATE` WRITE;
/*!40000 ALTER TABLE `REPAIRORDER_ESTIMATE` DISABLE KEYS */;
/*!40000 ALTER TABLE `REPAIRORDER_ESTIMATE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `REPAIRORDER_INVOICE`
--

DROP TABLE IF EXISTS `REPAIRORDER_INVOICE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `REPAIRORDER_INVOICE` (
  `id` bigint(20) NOT NULL,
  `version` int(11) NOT NULL,
  `invoicenumber` varchar(11) DEFAULT NULL,
  `status` varchar(100) DEFAULT NULL,
  `paymentMethod` varchar(100) DEFAULT NULL,
  `paymentResponsible` varchar(30) DEFAULT NULL,
  `notes` longtext,
  `invoicedate` date DEFAULT NULL,
  `accepteddate` date DEFAULT NULL,
  `estimatedpaymentdate` date DEFAULT NULL,
  `paymentdate` date DEFAULT NULL,
  `RESPONSIBLE_Id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `invoicenumber` (`invoicenumber`),
  KEY `FK6F9DD08FE4AA22D3` (`id`),
  KEY `FK6F9DD08F32279F1A` (`RESPONSIBLE_Id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `REPAIRORDER_INVOICE`
--

LOCK TABLES `REPAIRORDER_INVOICE` WRITE;
/*!40000 ALTER TABLE `REPAIRORDER_INVOICE` DISABLE KEYS */;
/*!40000 ALTER TABLE `REPAIRORDER_INVOICE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `REPAIRORDER_LABOURLINE`
--

DROP TABLE IF EXISTS `REPAIRORDER_LABOURLINE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `REPAIRORDER_LABOURLINE` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` int(11) NOT NULL,
  `LABOUR_id` bigint(20) NOT NULL,
  `hours` decimal(6,2) DEFAULT NULL,
  `discount` decimal(5,2) DEFAULT NULL,
  `REPAIRORDER_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK3B2E3251DDAA57F1` (`REPAIRORDER_id`),
  KEY `FK3B2E3251E5C5ED03` (`LABOUR_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `REPAIRORDER_LABOURLINE`
--

LOCK TABLES `REPAIRORDER_LABOURLINE` WRITE;
/*!40000 ALTER TABLE `REPAIRORDER_LABOURLINE` DISABLE KEYS */;
/*!40000 ALTER TABLE `REPAIRORDER_LABOURLINE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `REPAIRORDER_PARTLINE`
--

DROP TABLE IF EXISTS `REPAIRORDER_PARTLINE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `REPAIRORDER_PARTLINE` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `PART_id` bigint(20) NOT NULL,
  `quantity` int(11) DEFAULT NULL,
  `discount` decimal(5,2) DEFAULT NULL,
  `REPAIRORDER_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKA5080125DDAA57F1` (`REPAIRORDER_id`),
  KEY `FKA5080125ABCE8FCA` (`PART_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `REPAIRORDER_PARTLINE`
--

LOCK TABLES `REPAIRORDER_PARTLINE` WRITE;
/*!40000 ALTER TABLE `REPAIRORDER_PARTLINE` DISABLE KEYS */;
/*!40000 ALTER TABLE `REPAIRORDER_PARTLINE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ROLE`
--

DROP TABLE IF EXISTS `ROLE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ROLE` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` int(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  `description` longtext,
  PRIMARY KEY (`id`),
  UNIQUE KEY `displayname` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ROLE`
--

LOCK TABLES `ROLE` WRITE;
/*!40000 ALTER TABLE `ROLE` DISABLE KEYS */;
INSERT INTO `ROLE` VALUES (1,3,'admin','Default Admin');
/*!40000 ALTER TABLE `ROLE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ROLE_PRIVILEGE`
--

DROP TABLE IF EXISTS `ROLE_PRIVILEGE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ROLE_PRIVILEGE` (
  `ROLE_id` bigint(20) NOT NULL,
  `privilege` varchar(255) DEFAULT NULL,
  KEY `FK4D2EDE0858E86C65` (`ROLE_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ROLE_PRIVILEGE`
--

LOCK TABLES `ROLE_PRIVILEGE` WRITE;
/*!40000 ALTER TABLE `ROLE_PRIVILEGE` DISABLE KEYS */;
INSERT INTO `ROLE_PRIVILEGE` VALUES (1,'ADMIN');
/*!40000 ALTER TABLE `ROLE_PRIVILEGE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `STOCK`
--

DROP TABLE IF EXISTS `STOCK`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `STOCK` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `WAREHOUSE_id` bigint(20) NOT NULL,
  `units` int(11) DEFAULT NULL,
  `PART_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK4B8FEF66A8925EA` (`WAREHOUSE_id`),
  KEY `FK4B8FEF6ABCE8FCA` (`PART_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `STOCK`
--

LOCK TABLES `STOCK` WRITE;
/*!40000 ALTER TABLE `STOCK` DISABLE KEYS */;
/*!40000 ALTER TABLE `STOCK` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SUPPLIER`
--

DROP TABLE IF EXISTS `SUPPLIER`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SUPPLIER` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` int(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  `nif` varchar(9) DEFAULT NULL,
  `notes` longtext,
  `web` varchar(100) DEFAULT NULL,
  `category` varchar(100) DEFAULT NULL,
  `paymentmethod` varchar(100) DEFAULT NULL,
  `shippingmethod` varchar(100) DEFAULT NULL,
  `enabled` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `nif` (`nif`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SUPPLIER`
--

LOCK TABLES `SUPPLIER` WRITE;
/*!40000 ALTER TABLE `SUPPLIER` DISABLE KEYS */;
/*!40000 ALTER TABLE `SUPPLIER` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SUPPLIER_ADDRESS`
--

DROP TABLE IF EXISTS `SUPPLIER_ADDRESS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SUPPLIER_ADDRESS` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `label` varchar(100) NOT NULL,
  `street` varchar(200) DEFAULT NULL,
  `city` varchar(100) DEFAULT NULL,
  `province` varchar(100) DEFAULT NULL,
  `country` varchar(100) DEFAULT NULL,
  `postalcode` varchar(10) DEFAULT NULL,
  `notes` longtext,
  `SUPPLIER_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK9D042C6111901D2A` (`SUPPLIER_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SUPPLIER_ADDRESS`
--

LOCK TABLES `SUPPLIER_ADDRESS` WRITE;
/*!40000 ALTER TABLE `SUPPLIER_ADDRESS` DISABLE KEYS */;
/*!40000 ALTER TABLE `SUPPLIER_ADDRESS` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SUPPLIER_EMAIL`
--

DROP TABLE IF EXISTS `SUPPLIER_EMAIL`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SUPPLIER_EMAIL` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `address` varchar(150) NOT NULL,
  `label` varchar(100) NOT NULL,
  `notes` longtext,
  `SUPPLIER_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKC033174911901D2A` (`SUPPLIER_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SUPPLIER_EMAIL`
--

LOCK TABLES `SUPPLIER_EMAIL` WRITE;
/*!40000 ALTER TABLE `SUPPLIER_EMAIL` DISABLE KEYS */;
/*!40000 ALTER TABLE `SUPPLIER_EMAIL` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SUPPLIER_PHONE`
--

DROP TABLE IF EXISTS `SUPPLIER_PHONE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SUPPLIER_PHONE` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `number` varchar(20) NOT NULL,
  `label` varchar(100) NOT NULL,
  `notes` longtext,
  `SUPPLIER_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKC0CC091B11901D2A` (`SUPPLIER_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SUPPLIER_PHONE`
--

LOCK TABLES `SUPPLIER_PHONE` WRITE;
/*!40000 ALTER TABLE `SUPPLIER_PHONE` DISABLE KEYS */;
/*!40000 ALTER TABLE `SUPPLIER_PHONE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `USER`
--

DROP TABLE IF EXISTS `USER`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `USER` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` int(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  `password` varchar(100) NOT NULL,
  `ROLE_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`),
  KEY `FK27E3CB58E86C65` (`ROLE_id`)
) ENGINE=MyISAM AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `USER`
--

LOCK TABLES `USER` WRITE;
/*!40000 ALTER TABLE `USER` DISABLE KEYS */;
INSERT INTO `USER` VALUES (1,2,'admin','$2a$10$csyloGgSR/XBH/6/YcLNI.PsyLOqfy0NpXPehE9fZDAAGEvm/f09i',1);
/*!40000 ALTER TABLE `USER` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `VEHICLE`
--

DROP TABLE IF EXISTS `VEHICLE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `VEHICLE` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `registration` varchar(20) NOT NULL,
  `vin` varchar(17) DEFAULT NULL,
  `make` varchar(50) DEFAULT NULL,
  `model` varchar(50) DEFAULT NULL,
  `year` varchar(4) DEFAULT NULL,
  `colour` varchar(20) DEFAULT NULL,
  `type` varchar(20) DEFAULT NULL,
  `fuel` varchar(20) DEFAULT NULL,
  `insurancenumber` varchar(20) DEFAULT NULL,
  `notes` longtext,
  `enabled` tinyint(1) NOT NULL DEFAULT '1',
  `CLIENT_Id` bigint(20) NOT NULL,
  `INSURANCE_COMPANY_Id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK3F2F1B0C45E792D0` (`INSURANCE_COMPANY_Id`),
  KEY `FK3F2F1B0C28525785` (`CLIENT_Id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `VEHICLE`
--

LOCK TABLES `VEHICLE` WRITE;
/*!40000 ALTER TABLE `VEHICLE` DISABLE KEYS */;
/*!40000 ALTER TABLE `VEHICLE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `WAREHOUSE`
--

DROP TABLE IF EXISTS `WAREHOUSE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `WAREHOUSE` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` int(11) NOT NULL,
  `enabled` tinyint(1) NOT NULL DEFAULT '1',
  `name` varchar(100) NOT NULL,
  `notes` longtext,
  `address_street` varchar(200) DEFAULT NULL,
  `address_city` varchar(100) DEFAULT NULL,
  `address_province` varchar(100) DEFAULT NULL,
  `address_country` varchar(100) DEFAULT NULL,
  `address_postalcode` varchar(10) DEFAULT NULL,
  `address_notes` longtext,
  `address_label` varchar(100) DEFAULT NULL,
  `email_address` varchar(150) DEFAULT NULL,
  `email_notes` longtext,
  `email_label` varchar(100) DEFAULT NULL,
  `phone_number` varchar(20) DEFAULT NULL,
  `phone_notes` longtext,
  `phone_label` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `WAREHOUSE`
--

LOCK TABLES `WAREHOUSE` WRITE;
/*!40000 ALTER TABLE `WAREHOUSE` DISABLE KEYS */;
/*!40000 ALTER TABLE `WAREHOUSE` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2013-03-21 14:34:44
