Warning: Using a password on the command line interface can be insecure.
-- MySQL dump 10.13  Distrib 5.6.16, for Win32 (x86)
--
-- Host: localhost    Database: mot
-- ------------------------------------------------------
-- Server version	5.6.22-log

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
-- Table structure for table `avgcalc`
--

DROP TABLE IF EXISTS `avgcalc`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `avgcalc` (
  `STOCK` varchar(50) COLLATE utf16_bin NOT NULL,
  `VALUE1` int(11) NOT NULL,
  `VALUE2` int(11) NOT NULL,
  `TIMERANGE` varchar(20) COLLATE utf16_bin NOT NULL,
  `QUANTITY` int(11) DEFAULT NULL,
  `TRADECOUNT` int(11) DEFAULT NULL,
  `RESULT` double NOT NULL,
  `TXNCOST` double DEFAULT NULL,
  `PNL` double DEFAULT NULL,
  `CLASSNAME` varchar(100) COLLATE utf16_bin NOT NULL,
  `TIMESTAMP` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `STARTTIME` varchar(25) COLLATE utf16_bin DEFAULT NULL,
  `ENDTIME` varchar(25) COLLATE utf16_bin DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf16 COLLATE=utf16_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `configuration`
--

DROP TABLE IF EXISTS `configuration`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `configuration` (
  `property` varchar(70) COLLATE utf16_bin NOT NULL,
  `value` varchar(50) COLLATE utf16_bin NOT NULL,
  PRIMARY KEY (`property`)
) ENGINE=MyISAM DEFAULT CHARSET=utf16 COLLATE=utf16_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `exchange`
--

DROP TABLE IF EXISTS `exchange`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `exchange` (
  `ID` varchar(20) COLLATE utf16_bin NOT NULL,
  `EXCHANGE` varchar(20) COLLATE utf16_bin NOT NULL,
  `NAME` varchar(20) COLLATE utf16_bin DEFAULT NULL,
  `OPEN` varchar(20) COLLATE utf16_bin DEFAULT NULL,
  `CLOSE` varchar(20) COLLATE utf16_bin DEFAULT NULL,
  `TIMEZONE` varchar(20) COLLATE utf16_bin DEFAULT NULL,
  UNIQUE KEY `ID` (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf16 COLLATE=utf16_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `orders` (
  `ORDERID` varchar(50) COLLATE utf16_bin NOT NULL,
  `BUYSELL` varchar(10) COLLATE utf16_bin NOT NULL,
  `QUANTITY` int(11) NOT NULL,
  `STATUS` varchar(30) COLLATE utf16_bin DEFAULT NULL,
  `PRICE` double NOT NULL,
  `AVGPRICE` double DEFAULT NULL,
  `BARRIER` double NOT NULL,
  `SYMBOL` varchar(20) COLLATE utf16_bin NOT NULL,
  `TIMESTAMP` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `CLOSED` varchar(50) COLLATE utf16_bin NOT NULL,
  `SIMULATED` tinyint(1) NOT NULL,
  `STRATEGY` varchar(50) COLLATE utf16_bin NOT NULL,
  KEY `ORDERID` (`ORDERID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf16 COLLATE=utf16_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `portfolio`
--

DROP TABLE IF EXISTS `portfolio`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `portfolio` (
  `TIMESTAMP` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `SYMBOL` varchar(30) COLLATE utf16_bin NOT NULL,
  `TYPE` varchar(30) COLLATE utf16_bin NOT NULL,
  `POSITION` int(11) NOT NULL,
  `PRICE` double NOT NULL,
  `AVERAGEPRICE` double DEFAULT NULL,
  `MARKETVALUE` double DEFAULT NULL,
  `UPNL` double DEFAULT NULL,
  `PNL` double DEFAULT NULL,
  PRIMARY KEY (`SYMBOL`)
) ENGINE=MyISAM DEFAULT CHARSET=utf16 COLLATE=utf16_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `property`
--

DROP TABLE IF EXISTS `property`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `property` (
  `ID` varchar(55) COLLATE utf16_bin NOT NULL,
  `PROPERTY` varchar(70) COLLATE utf16_bin NOT NULL,
  `VALUE` varchar(50) COLLATE utf16_bin NOT NULL,
  `TIMESTAMP` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `ENABLED` tinyint(1) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf16 COLLATE=utf16_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `simulation`
--

DROP TABLE IF EXISTS `simulation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `simulation` (
  `SYMBOL` varchar(50) COLLATE utf16_bin NOT NULL,
  `LOADVALUES` varchar(100) COLLATE utf16_bin NOT NULL,
  `FREQUENCY` varchar(50) COLLATE utf16_bin NOT NULL,
  `QUANTITY` int(11) DEFAULT NULL,
  `TRADECOUNT` int(11) DEFAULT NULL,
  `RESULT` double NOT NULL,
  `TXNCOST` double DEFAULT NULL,
  `PNL` double DEFAULT NULL,
  `CLASSNAME` varchar(100) COLLATE utf16_bin NOT NULL,
  `TIMESTAMP` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `STARTTIME` varchar(25) COLLATE utf16_bin DEFAULT NULL,
  `ENDTIME` varchar(25) COLLATE utf16_bin DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf16 COLLATE=utf16_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `staticdata`
--

DROP TABLE IF EXISTS `staticdata`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `staticdata` (
  `TIMESTAMP` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `SYMBOL` varchar(20) COLLATE utf16_bin NOT NULL,
  `RANGE` varchar(20) COLLATE utf16_bin DEFAULT NULL,
  `YEARRANGE` varchar(20) COLLATE utf16_bin DEFAULT NULL,
  `OPEN` varchar(20) COLLATE utf16_bin DEFAULT NULL,
  `VOLAVG` varchar(100) COLLATE utf16_bin DEFAULT NULL,
  `MKTCAP` varchar(20) COLLATE utf16_bin DEFAULT NULL,
  `PNE` varchar(20) COLLATE utf16_bin DEFAULT NULL,
  `DIVYIELD` varchar(20) COLLATE utf16_bin DEFAULT NULL,
  `EPS` varchar(20) COLLATE utf16_bin DEFAULT NULL,
  `SHARES` varchar(20) COLLATE utf16_bin DEFAULT NULL,
  `BETA` varchar(20) COLLATE utf16_bin DEFAULT NULL,
  PRIMARY KEY (`TIMESTAMP`)
) ENGINE=MyISAM DEFAULT CHARSET=utf16 COLLATE=utf16_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `strategies`
--

DROP TABLE IF EXISTS `strategies`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `strategies` (
  `ID` varchar(30) COLLATE utf16_bin NOT NULL,
  `NAME` varchar(20) COLLATE utf16_bin NOT NULL,
  `TYPE` varchar(255) COLLATE utf16_bin NOT NULL,
  `SYMBOL` varchar(30) COLLATE utf16_bin NOT NULL,
  `LOADVALUES` varchar(200) COLLATE utf16_bin NOT NULL,
  `ENABLED` tinyint(1) NOT NULL,
  `AMOUNT` varchar(20) COLLATE utf16_bin NOT NULL,
  `SIMULATED` tinyint(1) NOT NULL,
  `TIMESTAMP` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`NAME`),
  UNIQUE KEY `ID` (`ID`),
  KEY `SYMBOL` (`SYMBOL`,`ENABLED`)
) ENGINE=MyISAM DEFAULT CHARSET=utf16 COLLATE=utf16_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `strategyanalysis`
--

DROP TABLE IF EXISTS `strategyanalysis`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `strategyanalysis` (
  `TIMESTAMP` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `NAME` varchar(20) COLLATE utf16_bin NOT NULL,
  `PNL` double NOT NULL,
  `TRADECOUNT` int(11) NOT NULL,
  `QUANTITY` int(11) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf16 COLLATE=utf16_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tickhistory`
--

DROP TABLE IF EXISTS `tickhistory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tickhistory` (
  `ID` varchar(50) COLLATE utf16_bin NOT NULL,
  `FREQUENCY` varchar(10) COLLATE utf16_bin NOT NULL,
  `STOCK` varchar(50) COLLATE utf16_bin NOT NULL,
  `DATE` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `OPEN` double NOT NULL,
  `HIGH` double NOT NULL,
  `LOW` double NOT NULL,
  `CLOSE` int(11) NOT NULL,
  `VOLUME` int(11) NOT NULL,
  `COUNT` int(11) NOT NULL,
  UNIQUE KEY `ID` (`ID`),
  KEY `STOCK` (`STOCK`),
  KEY `FREQUENCY` (`FREQUENCY`)
) ENGINE=MyISAM DEFAULT CHARSET=utf16 COLLATE=utf16_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tickhistoryrequests`
--

DROP TABLE IF EXISTS `tickhistoryrequests`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tickhistoryrequests` (
  `TIMESTAMP` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `REQID` varchar(50) COLLATE utf16_bin NOT NULL,
  `STOCK` varchar(20) COLLATE utf16_bin NOT NULL,
  `FREQUENCY` varchar(50) COLLATE utf16_bin NOT NULL,
  UNIQUE KEY `REQID` (`REQID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf16 COLLATE=utf16_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tickprices`
--

DROP TABLE IF EXISTS `tickprices`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tickprices` (
  `TIMESTAMP` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `STOCK` varchar(50) COLLATE utf16_bin NOT NULL,
  `FIELD` varchar(20) COLLATE utf16_bin NOT NULL,
  `PRICE` double NOT NULL,
  KEY `STOCK` (`STOCK`),
  KEY `FIELD` (`FIELD`),
  KEY `TIMESTAMP` (`TIMESTAMP`),
  KEY `PRICE` (`PRICE`),
  KEY `TIMESTAMP_2` (`TIMESTAMP`)
) ENGINE=MyISAM DEFAULT CHARSET=utf16 COLLATE=utf16_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tickprices_archive`
--

DROP TABLE IF EXISTS `tickprices_archive`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tickprices_archive` (
  `TIMESTAMP` datetime NOT NULL,
  `STOCK` varchar(50) COLLATE utf16_bin NOT NULL,
  `FIELD` varchar(20) COLLATE utf16_bin NOT NULL,
  `PRICE` double NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf16 COLLATE=utf16_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ticksize`
--

DROP TABLE IF EXISTS `ticksize`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ticksize` (
  `TIMESTAMP` datetime NOT NULL,
  `SYMBOL` varchar(50) COLLATE utf16_bin NOT NULL,
  `FIELD` varchar(20) COLLATE utf16_bin NOT NULL,
  `SIZE` int(11) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf16 COLLATE=utf16_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `watchlist`
--

DROP TABLE IF EXISTS `watchlist`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `watchlist` (
  `ID` varchar(50) COLLATE utf16_bin NOT NULL,
  `SYMBOL` varchar(50) COLLATE utf16_bin DEFAULT NULL,
  `CURRENCY` varchar(20) COLLATE utf16_bin DEFAULT NULL,
  `EXCHANGE` varchar(20) COLLATE utf16_bin NOT NULL,
  `TYPE` varchar(10) COLLATE utf16_bin NOT NULL,
  `NAME` varchar(40) COLLATE utf16_bin NOT NULL,
  `EXECUTOR` varchar(30) COLLATE utf16_bin DEFAULT NULL,
  `ENABLED` tinyint(1) DEFAULT NULL,
  UNIQUE KEY `ID` (`ID`),
  KEY `ENABLED` (`ENABLED`,`SYMBOL`,`TYPE`,`NAME`)
) ENGINE=MyISAM DEFAULT CHARSET=utf16 COLLATE=utf16_bin;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2015-03-01 12:45:24
