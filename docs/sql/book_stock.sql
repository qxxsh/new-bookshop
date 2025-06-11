-- 为book表添加库存字段
ALTER TABLE `book` ADD COLUMN `stock` int(11) NOT NULL DEFAULT 0 COMMENT '库存数量' AFTER `price`;

-- 为现有书籍设置初始库存（可根据实际情况调整）
UPDATE `book` SET `stock` = 50 WHERE `stock` = 0;

-- 添加库存索引
ALTER TABLE `book` ADD INDEX `idx_stock` (`stock`); 