-- 项目插件关联
CREATE TABLE `project_plugin` (
  `id`  int NOT NULL AUTO_INCREMENT ,
  `projectId`  char(12) CHARACTER SET utf8 NOT NULL ,
  `pluginId`  varchar(32) CHARACTER SET utf8 NOT NULL ,
  `createTime`  datetime NOT NULL ,
  PRIMARY KEY (`id`)
)
  DEFAULT CHARACTER SET=utf8
  COMMENT='项目插件关联表'
;


-- 全局环境变量表
-- 全局参数表
-- 全局字典表
-- 附件表
-- 历史记录表
-- 插件属性表
-- 登录插件表