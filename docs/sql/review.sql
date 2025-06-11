-- 商品评价表
CREATE TABLE `review` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `order_id` int(11) NOT NULL COMMENT '订单ID',
  `order_item_id` int(11) NOT NULL COMMENT '订单项ID',
  `book_id` int(11) NOT NULL COMMENT '书籍ID',
  `user_id` int(11) NOT NULL COMMENT '用户ID',
  `rating` int(1) NOT NULL COMMENT '评分：1-5星',
  `content` text COMMENT '评价内容',
  `images` varchar(500) DEFAULT NULL COMMENT '评价图片（JSON格式）',
  `is_anonymous` tinyint(1) DEFAULT 0 COMMENT '是否匿名评价：0-否，1-是',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_order_item` (`order_item_id`), -- 每个订单项只能评价一次
  KEY `idx_order_id` (`order_id`),
  KEY `idx_book_id` (`book_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_rating` (`rating`),
  KEY `idx_create_time` (`create_time`),
  CONSTRAINT `fk_review_order` FOREIGN KEY (`order_id`) REFERENCES `order` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_review_order_item` FOREIGN KEY (`order_item_id`) REFERENCES `order_item` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_review_book` FOREIGN KEY (`book_id`) REFERENCES `book` (`id`) ON DELETE RESTRICT,
  CONSTRAINT `fk_review_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='商品评价表'; 