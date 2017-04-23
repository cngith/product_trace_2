-- --------------------------------------------------------
-- 主机:                           127.0.0.1
-- 服务器版本:                        10.1.9-MariaDB - mariadb.org binary distribution
-- 服务器操作系统:                      Win64
-- HeidiSQL 版本:                  9.1.0.4867
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

-- CentOS下导入脚本命令 source /root/mywork/project/pt/product_trace_empty.sql;

-- Windows下导入脚本命令 source E:/Program/source/product_trace_2/pt2_SQL/product_trace_empty.sql;
-- 开发期数据库

-- PRINT "删除开发期数据库 product_trace_2_dev";
 DROP DATABASE IF EXISTS `product_trace_2_dev`; 
-- PRINT "新建开发期数据库 product_trace_2_dev";
 CREATE DATABASE IF NOT EXISTS `product_trace_2_dev` /*!40100 DEFAULT CHARACTER SET utf8 */;
 USE `product_trace_2_dev`;

-- 生产期数据库
-- PRINT "删除生产期数据库 product_trace_a";
-- DROP DATABASE IF EXISTS `product_trace_a`; 
-- PRINT "新建生产期数据库 product_trace_2a";
-- CREATE DATABASE IF NOT EXISTS `product_trace_a` /*!40100 DEFAULT CHARACTER SET utf8 */;
-- USE `product_trace_a`;

-- 导出  表 product_trace_x.complete_detail 结构
CREATE TABLE IF NOT EXISTS `complete_detail` (
  `cdId` int(9) unsigned zerofill NOT NULL AUTO_INCREMENT,
  `fromWs` int(3) unsigned zerofill NOT NULL,
  `fromEp` int(4) unsigned zerofill DEFAULT NULL,
  `toWs` int(3) unsigned zerofill DEFAULT NULL,
  `toEp` int(4) unsigned zerofill DEFAULT NULL,
  `barCode` varchar(9) NOT NULL,
  `fromTime` datetime NOT NULL,
  `toTime` datetime DEFAULT NULL,
  `addTime` datetime NOT NULL,
  `fromUserId` int(5) unsigned NOT NULL COMMENT '发送操作员',
  `toUserId` int(5) unsigned DEFAULT NULL COMMENT '接收操作员',
  PRIMARY KEY (`cdId`),
  KEY `FK_complete_detail_user` (`fromUserId`),
  KEY `FK_complete_detail_user_2` (`toUserId`),
  CONSTRAINT `FK_complete_detail_user` FOREIGN KEY (`fromUserId`) REFERENCES `user` (`userid`),
  CONSTRAINT `FK_complete_detail_user_2` FOREIGN KEY (`toUserId`) REFERENCES `user` (`userid`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='已完成生产的商品调拨[完工]明细表';

-- 正在导出表  product_trace_x.complete_detail 的数据：~0 rows (大约)
-- DELETE FROM `complete_detail`;
/*!40000 ALTER TABLE `complete_detail` DISABLE KEYS */;
/*!40000 ALTER TABLE `complete_detail` ENABLE KEYS */;


-- 导出  表 product_trace_x.customer 结构
CREATE TABLE IF NOT EXISTS `customer` (
  `cusCode` varchar(3) NOT NULL COMMENT '客户代码',
  `cusName` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`cusCode`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='客户表';

-- 正在导出表  product_trace_x.customer 的数据：~0 rows (大约)
--DELETE FROM `customer`;
/*!40000 ALTER TABLE `customer` DISABLE KEYS */;
/*!40000 ALTER TABLE `customer` ENABLE KEYS */;


-- 导出  表 product_trace_x.employee 结构
CREATE TABLE IF NOT EXISTS `employee` (
  `epId` int(4) unsigned zerofill NOT NULL AUTO_INCREMENT,
  `wsId` int(3) unsigned zerofill NOT NULL COMMENT '所属部门(车间)',
  `epName` varchar(10) NOT NULL,
  PRIMARY KEY (`epId`),
  KEY `FK_ws_id` (`wsId`),
  CONSTRAINT `FK_ws_id` FOREIGN KEY (`wsId`) REFERENCES `workshop` (`wsId`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='在各车间工作的员工';

CREATE TABLE `mink_type` (
	`minkTypeId` INT(5) UNSIGNED NOT NULL AUTO_INCREMENT,
	`minkType` VARCHAR(10) NOT NULL,
	PRIMARY KEY (`minkTypeId`),
	UNIQUE INDEX `minkType` (`minkType`)
)
COMMENT='皮号类型[F/M]'
COLLATE='utf8_general_ci'
ENGINE=InnoDB
;

CREATE TABLE `mink_size` (
	`msId` INT(5) UNSIGNED NOT NULL AUTO_INCREMENT,
	`msSize` VARCHAR(5) NOT NULL DEFAULT '0',
	PRIMARY KEY (`msId`),
	UNIQUE INDEX `msSize` (`msSize`)
)
COMMENT='皮号(与“皮号类型”组合成F0,F1,M0,M1,...)'
COLLATE='utf8_general_ci'
ENGINE=InnoDB
;


CREATE TABLE `material` (
	`mtId` INT(5) UNSIGNED NOT NULL AUTO_INCREMENT,
	`mtName` VARCHAR(20) NOT NULL,
	PRIMARY KEY (`mtId`),
	UNIQUE INDEX `mtName` (`mtName`)
)
COMMENT='原料(原皮类型：珍珠，本白，米黄……)'
COLLATE='utf8_general_ci'
ENGINE=InnoDB
AUTO_INCREMENT=1
;

-- 导出  表 product_trace_x.product 结构
CREATE TABLE IF NOT EXISTS `product` (
  `pdId` int(8) unsigned zerofill NOT NULL AUTO_INCREMENT,
  `barCode` varchar(9) NOT NULL COMMENT '条码',
  `styleCode` varchar(20) NOT NULL COMMENT '款号',
  `pdName` varchar(30) NOT NULL COMMENT '颜色',
  `length` int(2) NOT NULL COMMENT '长度',
  `size` varchar(4) NOT NULL COMMENT '尺码',
  `remark` varchar(50) DEFAULT NULL COMMENT '备注',
  `cusCode` varchar(3) NOT NULL COMMENT '客户代码',
  `billNo` varchar(9) NOT NULL COMMENT '单号',
  `status` int(1) unsigned DEFAULT NULL COMMENT '商品状态:0-取消,1-生产中,5-完工',
  PRIMARY KEY (`pdId`),
  UNIQUE KEY `bar_code` (`barCode`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='商品';

-- 正在导出表  product_trace_x.product 的数据：~0 rows (大约)
--DELETE FROM `product`;
/*!40000 ALTER TABLE `product` DISABLE KEYS */;
/*!40000 ALTER TABLE `product` ENABLE KEYS */;


-- 导出  表 product_trace_x.transfer_order_detail 结构
CREATE TABLE IF NOT EXISTS `transfer_order_detail` (
  `tdId` int(9) unsigned zerofill NOT NULL AUTO_INCREMENT,
  `fromWs` int(3) unsigned zerofill NOT NULL,
  `fromEp` int(4) unsigned zerofill DEFAULT NULL,
  `toWs` int(3) unsigned zerofill NOT NULL,
  `toEp` int(4) unsigned zerofill DEFAULT NULL,
  `barCode` varchar(9) NOT NULL,
  `fromTime` datetime NOT NULL,
  `toTime` datetime DEFAULT NULL,
  `addTime` datetime NOT NULL,
  `fromUserId` int(5) unsigned NOT NULL COMMENT '发送操作员',
  `toUserId` int(5) unsigned DEFAULT NULL COMMENT '接收操作员',
  PRIMARY KEY (`tdId`),
  KEY `FK_tdfromws` (`fromWs`),
  KEY `FK_tdfromep` (`fromEp`),
  KEY `FK_tdtows` (`toWs`),
  KEY `FK_tdtoep` (`toEp`),
  KEY `FK_tdbarcode` (`barCode`),
  KEY `FK_transfer_order_detail_user` (`fromUserId`),
  KEY `FK_transfer_order_detail_user_2` (`toUserId`),
  CONSTRAINT `FK_tdbarcode` FOREIGN KEY (`barCode`) REFERENCES `product` (`barCode`),
  CONSTRAINT `FK_tdfromep` FOREIGN KEY (`fromEp`) REFERENCES `employee` (`epId`),
  CONSTRAINT `FK_tdfromws` FOREIGN KEY (`fromWs`) REFERENCES `workshop` (`wsId`),
  CONSTRAINT `FK_tdtoep` FOREIGN KEY (`toEp`) REFERENCES `employee` (`epId`),
  CONSTRAINT `FK_tdtows` FOREIGN KEY (`toWs`) REFERENCES `workshop` (`wsId`),
  CONSTRAINT `FK_transfer_order_detail_user` FOREIGN KEY (`fromUserId`) REFERENCES `user` (`userid`),
  CONSTRAINT `FK_transfer_order_detail_user_2` FOREIGN KEY (`toUserId`) REFERENCES `user` (`userid`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='调拨明细表';

-- 正在导出表  product_trace_x.transfer_order_detail 的数据：~0 rows (大约)
--DELETE FROM `transfer_order_detail`;
/*!40000 ALTER TABLE `transfer_order_detail` DISABLE KEYS */;
/*!40000 ALTER TABLE `transfer_order_detail` ENABLE KEYS */;


-- 导出  表 product_trace_x.user 结构
CREATE TABLE IF NOT EXISTS `user` (
  `userid` int(5) unsigned NOT NULL AUTO_INCREMENT,
  `username` varchar(20) NOT NULL,
  `password` varchar(20) NOT NULL DEFAULT '',
  PRIMARY KEY (`userid`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='操作员';

-- 正在导出表  product_trace_x.user 的数据：~0 rows (大约)
-- DELETE FROM `user`;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
--SELECT "添加默认管理员账号adm";
INSERT INTO `user` (`userid`, `username`, `password`) VALUES	('1', 'adm', '');

-- 导出  表 product_trace_x.workshop 结构
CREATE TABLE IF NOT EXISTS `workshop` (
  `wsId` int(3) unsigned zerofill NOT NULL AUTO_INCREMENT,
  `wsName` varchar(30) NOT NULL,
  PRIMARY KEY (`wsId`),
  UNIQUE KEY `wsName` (`wsName`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='生产流程上的各个车间';

-- 正在导出表  product_trace_x.workshop 的数据：~0 rows (大约)
--DELETE FROM `workshop`;
/*!40000 ALTER TABLE `workshop` DISABLE KEYS */;
/*!40000 ALTER TABLE `workshop` ENABLE KEYS */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
