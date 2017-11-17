-- MySQL dump 10.15  Distrib 10.0.17-MariaDB, for Linux (x86_64)
--
-- Host: localhost    Database: product_trace_2
-- ------------------------------------------------------
-- Server version	10.0.17-MariaDB-log

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

-- 直接在命名行下运行以下命名从现有数据库product_trace_2中导出user employee workshop三个表的内容到product_trace_3t_data.sql文件
-- mysqldump -uroot -pxxxxxx product_trace_2 user employee workshop > /root/mywork/project/pt/product_trace_3t_data.sql;
--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `userid` int(5) unsigned NOT NULL AUTO_INCREMENT,
  `username` varchar(20) NOT NULL,
  `password` varchar(20) NOT NULL DEFAULT '',
  PRIMARY KEY (`userid`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COMMENT='操作员';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'adm','0989'),(2,'cf','eng31'),(3,'hehuangxin','0931'),(4,'jiaoyan','1036'),(5,'zhuyafang','1517');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `employee`
--

DROP TABLE IF EXISTS `employee`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `employee` (
  `epId` int(4) unsigned zerofill NOT NULL AUTO_INCREMENT,
  `wsId` int(3) unsigned zerofill NOT NULL COMMENT '所属部门(车间)',
  `epName` varchar(10) NOT NULL,
  PRIMARY KEY (`epId`),
  KEY `FK_ws_id` (`wsId`),
  CONSTRAINT `FK_ws_id` FOREIGN KEY (`wsId`) REFERENCES `workshop` (`wsId`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8 COMMENT='在各车间工作的员工';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employee`
--

LOCK TABLES `employee` WRITE;
/*!40000 ALTER TABLE `employee` DISABLE KEYS */;
INSERT INTO `employee` VALUES (0001,001,'焦燕'),(0002,002,'袁成军'),(0003,004,'李组'),(0004,004,'谭组'),(0005,004,'何组'),(0006,004,'林组'),(0007,003,'朱贤义'),(0008,003,'陈光'),(0009,004,'孙组'),(0010,004,'熊组'),(0011,004,'张组'),(0012,004,'邓组'),(0013,004,'巧组'),(0014,004,'王组'),(0015,004,'海组'),(0016,004,'朱组'),(0017,004,'利组'),(0018,004,'方组'),(0019,005,'李永俞'),(0020,005,'黄聪聪'),(0021,005,'张良斌'),(0022,005,'张性举'),(0023,003,'曾庆新'),(0024,003,'许利武'),(0025,003,'温增标'),(0026,003,'余雪松'),(0027,003,'韩剑锋');
/*!40000 ALTER TABLE `employee` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `workshop`
--

DROP TABLE IF EXISTS `workshop`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `workshop` (
  `wsId` int(3) unsigned zerofill NOT NULL AUTO_INCREMENT,
  `wsName` varchar(30) NOT NULL,
  PRIMARY KEY (`wsId`),
  UNIQUE KEY `wsName` (`wsName`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COMMENT='生产流程上的各个车间';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `workshop`
--

LOCK TABLES `workshop` WRITE;
/*!40000 ALTER TABLE `workshop` DISABLE KEYS */;
INSERT INTO `workshop` VALUES (001,'办公室'),(004,'毛工部'),(002,'皮料仓'),(005,'裁缝部'),(003,'配皮房');
/*!40000 ALTER TABLE `workshop` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-04-22 10:57:09
