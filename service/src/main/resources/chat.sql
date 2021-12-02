/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80026
 Source Host           : localhost:3306
 Source Schema         : chat

 Target Server Type    : MySQL
 Target Server Version : 80026
 File Encoding         : 65001

 Date: 02/12/2021 22:14:59
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for gang
-- ----------------------------
DROP TABLE IF EXISTS `gang`;
CREATE TABLE `gang`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `create_time` timestamp(0) NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of gang
-- ----------------------------
INSERT INTO `gang` VALUES ('1', 'Java交流群', NULL);
INSERT INTO `gang` VALUES ('2', 'Python交流群', NULL);
INSERT INTO `gang` VALUES ('3', '程序员交流群', NULL);
INSERT INTO `gang` VALUES ('4', 'Web前端交流群', NULL);
INSERT INTO `gang` VALUES ('5', '游戏交流群', NULL);
INSERT INTO `gang` VALUES ('6', '相亲交流群', NULL);

-- ----------------------------
-- Table structure for gang_member
-- ----------------------------
DROP TABLE IF EXISTS `gang_member`;
CREATE TABLE `gang_member`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `gang_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `member_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `create_time` timestamp(0) NULL DEFAULT NULL,
  UNIQUE INDEX `uid`(`gang_id`, `member_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of gang_member
-- ----------------------------
INSERT INTO `gang_member` VALUES ('1', '1', '1', NULL);
INSERT INTO `gang_member` VALUES ('2', '1', '2', NULL);
INSERT INTO `gang_member` VALUES ('3', '2', '1', NULL);
INSERT INTO `gang_member` VALUES ('4', '2', '2', NULL);
INSERT INTO `gang_member` VALUES ('61a8cf2517439a410960300e', '3', '2', '2021-12-02 13:50:29');
INSERT INTO `gang_member` VALUES ('61a8cf2617439a410960300f', '4', '2', '2021-12-02 13:50:31');
INSERT INTO `gang_member` VALUES ('61a8d15b17439a4109603061', '5', '2', '2021-12-02 13:59:56');
INSERT INTO `gang_member` VALUES ('61a8d15d17439a4109603062', '6', '2', '2021-12-02 13:59:58');

-- ----------------------------
-- Table structure for member
-- ----------------------------
DROP TABLE IF EXISTS `member`;
CREATE TABLE `member`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `username` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `create_time` timestamp(0) NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of member
-- ----------------------------
INSERT INTO `member` VALUES ('1', 'user1', '$2a$10$WiuebtAwq/sg5ivzD9Z4k.eHdYonJpJ6C4K6/6SypWRHBEPfWB1hq', NULL);
INSERT INTO `member` VALUES ('2', 'user2', '$2a$10$WiuebtAwq/sg5ivzD9Z4k.eHdYonJpJ6C4K6/6SypWRHBEPfWB1hq', NULL);

-- ----------------------------
-- Table structure for message
-- ----------------------------
DROP TABLE IF EXISTS `message`;
CREATE TABLE `message`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `from` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `to` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `text` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `type` int(0) NULL DEFAULT NULL,
  `create_time` timestamp(0) NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of message
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
