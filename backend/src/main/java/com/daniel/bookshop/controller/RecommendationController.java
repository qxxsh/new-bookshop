package com.daniel.bookshop.controller;

import com.daniel.bookshop.common.Constants;
import com.daniel.bookshop.common.Response;
import com.daniel.bookshop.dto.BookRecommendationDto;
import com.daniel.bookshop.entity.User;
import com.daniel.bookshop.service.BookRecommendationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Slf4j
@RestController
@RequestMapping("/recommendation")
@Tag(name = "书籍推荐", description = "基于AI的书籍推荐功能")
public class RecommendationController {

    @Autowired
    private BookRecommendationService bookRecommendationService;

    /**
     * 根据购物车内容推荐书籍
     */
    @GetMapping("/cart-based")
    @Operation(summary = "基于购物车推荐书籍", description = "根据用户购物车中的书籍，使用千帆AI推荐相似或相关的书籍")
    public Response<BookRecommendationDto> recommendBooksBasedOnCart(HttpSession session) {
        System.out.println("=== AI推荐流程开始 ===");
        System.out.println("步骤1: 接收到AI推荐请求 - GET /api/recommendation/cart-based");
        
        try {
            // 检查用户是否登录
            System.out.println("步骤2: 检查Session中的用户信息");
            User user = (User) session.getAttribute(Constants.SESSION_USER);
            if (user == null) {
                System.out.println("❌ 步骤2失败: Session中没有用户信息");
                return Response.error("请先登录");
            }
            
            System.out.println("✅ 步骤2成功: 从Session获取到用户信息");
            System.out.println("  - 用户ID: " + user.getId());
            System.out.println("  - 用户名: " + user.getName());

            log.info("为用户{}生成基于购物车的书籍推荐", user.getId());

            System.out.println("步骤3: 调用推荐服务 - bookRecommendationService.recommendBooksBasedOnCart(" + user.getId() + ")");
            
            // 调用推荐服务
            BookRecommendationDto recommendation = bookRecommendationService.recommendBooksBasedOnCart(user.getId());

            System.out.println("步骤X: 推荐服务调用完成，检查返回结果");
            if (recommendation == null) {
                System.out.println("❌ 推荐服务返回null");
                return Response.error("推荐服务异常，请稍后再试");
            }
            
            if (recommendation.getRecommendedBooks() == null) {
                System.out.println("❌ 推荐书籍列表为null");
                return Response.error("暂无推荐结果，请先添加一些书籍到购物车");
            }
            
            if (recommendation.getRecommendedBooks().isEmpty()) {
                System.out.println("❌ 推荐书籍列表为空");
                System.out.println("  - 推荐理由: " + recommendation.getRecommendationReason());
                System.out.println("  - 购物车书籍: " + recommendation.getCartBookTitles());
                return Response.error("暂无推荐结果，请先添加一些书籍到购物车");
            }

            System.out.println("✅ AI推荐流程成功完成!");
            System.out.println("  - 推荐书籍数量: " + recommendation.getRecommendedBooks().size());
            System.out.println("  - 推荐理由: " + recommendation.getRecommendationReason());
            System.out.println("=== AI推荐流程结束 ===");
            
            log.info("成功为用户{}生成{}本推荐书籍", user.getId(), recommendation.getRecommendedBooks().size());
            return Response.success(recommendation);

        } catch (Exception e) {
            System.out.println("❌ AI推荐流程发生异常: " + e.getClass().getSimpleName() + " - " + e.getMessage());
            e.printStackTrace();
            log.error("生成书籍推荐时发生错误", e);
            return Response.error("推荐服务暂时不可用，请稍后再试");
        }
    }

    /**
     * 测试千帆AI连接
     */
    @GetMapping("/test-ai")
    @Operation(summary = "测试AI连接", description = "测试千帆AI v2服务是否可用")
    public Response<String> testAIConnection(HttpSession session) {
        try {
            // 检查用户是否登录
            User user = (User) session.getAttribute(Constants.SESSION_USER);
            if (user == null) {
                return Response.error("请先登录");
            }

            log.info("测试千帆AI v2连接，用户ID: {}", user.getId());
            return Response.success("千帆AI v2配置正常，可以进行推荐测试");

        } catch (Exception e) {
            log.error("测试AI连接时发生错误", e);
            return Response.error("AI服务连接异常：" + e.getMessage());
        }
    }
} 