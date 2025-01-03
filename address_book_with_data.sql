/*

 Source Server         : Mysql
 Source Server Type    : MySQL
 Source Server Version : 80036
 Source Host           : localhost:3306
 Source Schema         : address_book

 Target Server Type    : MySQL
 Target Server Version : 80036
 File Encoding         : 65001

*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for contact
-- ----------------------------
DROP TABLE IF EXISTS `contact`;
CREATE TABLE `contact`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `userid` int NULL DEFAULT NULL,
  `groupid` int NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `sex` tinyint NULL DEFAULT NULL,
  `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `workunit` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `notes` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `uid`(`userid` ASC) USING BTREE,
  INDEX `gid`(`groupid` ASC) USING BTREE,
  CONSTRAINT `gid` FOREIGN KEY (`groupid`) REFERENCES `groups` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `uid` FOREIGN KEY (`userid`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB  CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of contact
-- ----------------------------
INSERT INTO `contact` VALUES (1, 1, 2, '李明', 1, '13800138000', 'liming@example.com', '阿里巴巴集团', '北京市海淀区中关村大街1号院', '同事，负责技术开发');
INSERT INTO `contact` VALUES (2, 1, 2, '王丽', 0, '13900139000', 'wangli@example.com', '腾讯公司', '深圳市南山区科技园', '朋友，曾一起合作过项目');
INSERT INTO `contact` VALUES (3, 1, 2, '张伟', 1, '13700137000', 'zhangwei@example.com', '华为技术有限公司', '上海市浦东新区张江高科技园区', '同学，毕业于清华大学');
INSERT INTO `contact` VALUES (4, 1, 2, '陈静', 0, '13600136000', 'chenjing@example.com', '百度在线网络技术有限公司', '广州市天河区珠江新城', '合作伙伴，长期业务往来');
INSERT INTO `contact` VALUES (5, 1, 2, '刘洋', 1, '13500135000', 'liuyang@example.com', '京东商城', '杭州市滨江区网商路699号', '客户，经常沟通物流事宜');
INSERT INTO `contact` VALUES (6, 1, 2, '赵敏', 0, '13400134000', 'zhaomin@example.com', '网易公司', '南京市鼓楼区软件大道', '朋友，共同兴趣是摄影');
INSERT INTO `contact` VALUES (7, 1, 2, '孙强', 1, '13300133000', 'sunqiang@example.com', '小米科技有限责任公司', '成都市高新区天府大道', '校友，毕业于北京大学');
INSERT INTO `contact` VALUES (8, 1, 2, '周莉', 0, '13200132000', 'zhouli@example.com', '美团点评', '武汉市洪山区光谷广场', '同事，负责市场推广');
INSERT INTO `contact` VALUES (9, 1, 2, '吴刚', 1, '13100131000', 'wugang@example.com', '滴滴出行', '重庆市渝北区互联网产业园', '朋友，喜欢户外运动');
INSERT INTO `contact` VALUES (10, 1, 2, '郑洁', 0, '13000130000', 'zhengjie@example.com', '字节跳动', '天津市滨海新区开发区', '同事，负责内容审核');

-- ----------------------------
-- Table structure for groups
-- ----------------------------
DROP TABLE IF EXISTS `groups`;
CREATE TABLE `groups`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '组别ID',
  `userid` int NULL DEFAULT NULL COMMENT '外键，用户ID',
  `groupname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '组名',
  `isdefault` blob NULL COMMENT '是否为默认组',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `group_userid`(`userid` ASC) USING BTREE,
  CONSTRAINT `group_userid` FOREIGN KEY (`userid`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB  CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of groups
-- ----------------------------
INSERT INTO `groups` VALUES (1, 1, '默认分组', 0x31);
INSERT INTO `groups` VALUES (2, 1, '测试分组', 0x30);

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户密码',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户邮箱',
  `createtime` datetime NOT NULL COMMENT '用户注册时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `email`(`email` ASC) USING BTREE
) ENGINE = InnoDB  CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'Admin', 'admin123', '123456@qq.com', '2025-01-03 17:29:10');

-- ----------------------------
-- Triggers structure for table user
-- ----------------------------
DROP TRIGGER IF EXISTS `user_register`;
delimiter ;;
CREATE TRIGGER `user_register` AFTER INSERT ON `user` FOR EACH ROW INSERT INTO `groups` VALUES(NULL,NEW.id,"默认分组",TRUE)
;;
delimiter ;

SET FOREIGN_KEY_CHECKS = 1;
