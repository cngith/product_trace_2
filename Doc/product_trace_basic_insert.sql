-- --------------------------------------------------------
-- 主机:                           127.0.0.1
-- 服务器版本:                        10.0.21-MariaDB - mariadb.org binary distribution
-- 服务器操作系统:                      Win32
-- HeidiSQL 版本:                  9.1.0.4867
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

-- CentOS下导入脚本命令 source /root/mywork/project/pt/product_trace_data.sql;

-- Windows下导入脚本命令 source E:/Program/source/pt2/product_trace_user_data.sql;
-- 导入 product_trace_x 的数据
-- USE `product_trace_2a`;
 USE `product_trace_2_dev`;

--SELECT "添加操作员账号adm";
INSERT INTO `user` (`userid`, `username`, `password`) VALUES	(1, 'adm', '');

LOCK TABLES `workshop` WRITE;
INSERT INTO `workshop` VALUES (001,'办公室'),(002,'皮料仓'),(003,'配皮房'),(004,'毛工部'),(005,'裁缝部');
UNLOCK TABLES;
