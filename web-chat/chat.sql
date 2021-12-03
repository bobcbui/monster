/*
SQLyog Ultimate v10.00 Beta1
MySQL - 8.0.27 : Database - chat
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`chat` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `chat`;

/*Table structure for table `gang` */

DROP TABLE IF EXISTS `gang`;

CREATE TABLE `gang` (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;

/*Data for the table `gang` */

insert  into `gang`(`id`,`name`,`create_time`) values ('1','Java交流群',NULL),('2','Python交流群',NULL),('3','程序员交流群',NULL),('4','Web前端交流群',NULL),('5','游戏交流群',NULL),('6','相亲交流群',NULL);

/*Table structure for table `gang_member` */

DROP TABLE IF EXISTS `gang_member`;

CREATE TABLE `gang_member` (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `gang_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `member_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT NULL,
  UNIQUE KEY `uid` (`gang_id`,`member_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;

/*Data for the table `gang_member` */

insert  into `gang_member`(`id`,`gang_id`,`member_id`,`create_time`) values ('61a8cf2517439a410960300e','3','2','2021-12-02 13:50:29'),('61a8cf2617439a410960300f','4','2','2021-12-02 13:50:31'),('61a8d15b17439a4109603061','5','2','2021-12-02 13:59:56'),('61a8d15d17439a4109603062','6','2','2021-12-02 13:59:58'),('61a8e2a77be9d79d22e43828','6','1','2021-12-02 15:13:44'),('61a8e3027be9d79d22e43831','5','1','2021-12-02 15:15:15'),('61a8e32d7be9d79d22e43834','3','1','2021-12-02 15:15:58'),('61a8e3bd7be9d79d22e4383d','4','1','2021-12-02 15:18:22');

/*Table structure for table `member` */

DROP TABLE IF EXISTS `member`;

CREATE TABLE `member` (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `username` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT NULL,
  `gender` int DEFAULT NULL COMMENT '0是女  1是男',
  `usernametwo` varchar(32) DEFAULT NULL COMMENT '里面的名字',
  `letters` tinyint(1) DEFAULT NULL COMMENT '是否可以私信',
  `introduction` text COMMENT '简介',
  `avatar` varchar(100) DEFAULT NULL COMMENT '头像url'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;

/*Data for the table `member` */

insert  into `member`(`id`,`username`,`password`,`create_time`,`gender`,`usernametwo`,`letters`,`introduction`,`avatar`) values ('1','user1','$2a$10$WiuebtAwq/sg5ivzD9Z4k.eHdYonJpJ6C4K6/6SypWRHBEPfWB1hq',NULL,NULL,NULL,NULL,NULL,NULL),('2','user2','$2a$10$WiuebtAwq/sg5ivzD9Z4k.eHdYonJpJ6C4K6/6SypWRHBEPfWB1hq',NULL,NULL,NULL,NULL,NULL,NULL),('61a963877be98403e19eda60','980507010@qq.com','$2a$10$mgFnomKjeUBakQf1kMePoOzMV5DQh3NrcFZ126lNIBoH8xnA3H.Eu','2021-12-03 00:23:36',NULL,NULL,NULL,NULL,NULL),('61a963d07be98403e19eda63','456@qq.com','$2a$10$2bhdq.zOM4DYAx6UuEnS3.DUkPaHBjK/vcYC4JAL8RiI/GIhEidLS','2021-12-03 00:24:48',NULL,NULL,NULL,NULL,NULL),('61a965427be98403e19eda6a','qqq@qq.com','$2a$10$E9rdvfA6cnhSZ.AFnytDveUl9P2.daI8pXI.uE/4LJY8hBK.4Wvz6','2021-12-03 00:30:59',NULL,NULL,NULL,NULL,NULL);

/*Table structure for table `message` */

DROP TABLE IF EXISTS `message`;

CREATE TABLE `message` (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `from` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `to` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `text` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `type` int DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;

/*Data for the table `message` */

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
