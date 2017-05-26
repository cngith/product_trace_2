-- --------------------------------------------------------
-- 主机:                           127.0.0.1
-- 服务器版本:                        10.0.21-MariaDB - mariadb.org binary distribution
-- 服务器操作系统:                      Win32
-- HeidiSQL 版本:                  9.1.0.4867
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/* */;

-- CentOS下导入脚本命令 source /root/mywork/project/pt/SQLScript/product_trace_basic_insert.sql;

-- Windows下导入脚本命令 source E:/Program/source/pt2/product_trace_basic_insert.sql;
-- 导入 product_trace_x 的数据
-- USE `product_trace_2a`;
 USE `product_trace_2017`;

--SELECT "添加操作员账号";
INSERT INTO `user` (`userid`, `username`, `password`) VALUES	('', 'zd', '0424');

LOCK TABLES `workshop` WRITE;
INSERT INTO `workshop` VALUES (001,'办公室'),(002,'皮料仓'),(003,'配皮房'),(004,'毛工部'),(005,'裁缝部');
UNLOCK TABLES;
