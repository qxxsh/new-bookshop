package com.daniel.bookshop.service.impl;

import com.daniel.bookshop.config.QianfanConfig;
import com.daniel.bookshop.dto.BookRecommendationDto;
import com.daniel.bookshop.entity.Book;
import com.daniel.bookshop.entity.Cart;
import com.daniel.bookshop.service.BookRecommendationService;
import com.daniel.bookshop.service.BookService;
import com.daniel.bookshop.service.CartService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BookRecommendationServiceImpl implements BookRecommendationService {

    @Autowired
    private QianfanConfig qianfanConfig;

    @Autowired
    private CartService cartService;

    @Autowired
    private BookService bookService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public BookRecommendationDto recommendBooksBasedOnCart(Integer userId) {
        System.out.println("=== Service层: AI推荐逻辑开始 ===");
        System.out.println("步骤4: 进入BookRecommendationServiceImpl.recommendBooksBasedOnCart()");
        System.out.println("  - 接收到的用户ID: " + userId);
        
        try {
            // 1. 获取用户购物车中的书籍
            System.out.println("步骤5: 调用cartService.getCartList(" + userId + ")获取购物车数据");
            List<Cart> cartList = cartService.getCartList(userId);
            
            System.out.println("步骤6: 购物车查询结果");
            System.out.println("  - 购物车记录数量: " + (cartList != null ? cartList.size() : "null"));
            
            if (CollectionUtils.isEmpty(cartList)) {
                System.out.println("❌ 步骤6失败: 购物车为空或null");
                log.warn("用户{}的购物车为空", userId);
                return createEmptyRecommendation();
            }

            // 2. 构建购物车书籍信息
            System.out.println("步骤7: 提取购物车中的书籍信息");
            List<String> cartBookInfo = new ArrayList<>();
            List<String> cartBookTitles = new ArrayList<>();
            
            for (int i = 0; i < cartList.size(); i++) {
                Cart cart = cartList.get(i);
                System.out.println("  处理购物车项[" + i + "]:");
                System.out.println("    - 购物车ID: " + cart.getId());
                System.out.println("    - 书籍ID: " + cart.getBookId());
                System.out.println("    - 数量: " + cart.getQuantity());
                System.out.println("    - 书籍对象是否为null: " + (cart.getBook() == null));
                
                if (cart.getBook() != null) {
                    Book book = cart.getBook();
                    System.out.println("    - 书名: " + book.getName());
                    System.out.println("    - 作者: " + book.getAuthor());
                    System.out.println("    - 描述: " + (book.getDescription() != null ? book.getDescription().substring(0, Math.min(50, book.getDescription().length())) + "..." : "null"));
                    
                    String bookInfo = String.format("《%s》 - %s (%s)", 
                        book.getName(), book.getAuthor(), book.getDescription());
                    cartBookInfo.add(bookInfo);
                    cartBookTitles.add(book.getName());
                } else {
                    System.out.println("    ❌ 书籍对象为null！");
                }
            }

            System.out.println("步骤8: 书籍信息提取完成");
            System.out.println("  - 有效书籍数量: " + cartBookInfo.size());
            System.out.println("  - 书籍标题列表: " + cartBookTitles);

            if (cartBookInfo.isEmpty()) {
                System.out.println("❌ 步骤8失败: 没有有效的书籍信息");
                log.warn("用户{}的购物车中没有有效的书籍信息", userId);
                return createEmptyRecommendation();
            }

            // 3. 调用千帆AI v2获取推荐
            System.out.println("步骤9: 开始调用千帆AI API");
            String aiResponse = callQianfanAIv2(cartBookInfo);
            
            System.out.println("步骤10: 千帆AI调用完成，开始解析响应");
            System.out.println("  - AI响应长度: " + (aiResponse != null ? aiResponse.length() : "null"));
            if (aiResponse != null && aiResponse.length() > 100) {
                System.out.println("  - AI响应前100字符: " + aiResponse.substring(0, 100) + "...");
            }
            
            // 4. 解析AI响应并匹配数据库中的书籍
            BookRecommendationDto recommendation = parseAIResponseAndMatchBooks(aiResponse, cartBookTitles);
            
            System.out.println("步骤11: AI响应解析完成");
            if (recommendation != null) {
                System.out.println("  - 推荐理由: " + recommendation.getRecommendationReason());
                System.out.println("  - 推荐书籍数量: " + (recommendation.getRecommendedBooks() != null ? recommendation.getRecommendedBooks().size() : "null"));
            }
            
            System.out.println("✅ Service层: AI推荐逻辑成功完成");
            return recommendation;

        } catch (Exception e) {
            System.out.println("❌ Service层: AI推荐逻辑发生异常");
            System.out.println("  - 异常类型: " + e.getClass().getSimpleName());
            System.out.println("  - 异常消息: " + e.getMessage());
            e.printStackTrace();
            log.error("为用户{}推荐书籍时发生错误", userId, e);
            return createEmptyRecommendation();
        }
    }

    private String callQianfanAIv2(List<String> cartBookInfo) throws IOException {
        System.out.println("  === 千帆AI调用开始 ===");
        
        // 验证配置
        System.out.println("  步骤9.1: 检查千帆配置");
        System.out.println("    - qianfanConfig对象: " + (qianfanConfig != null ? "存在" : "null"));
        if (qianfanConfig != null) {
            System.out.println("    - baseUrl: " + qianfanConfig.getBaseUrl());
            System.out.println("    - apiKey: " + (qianfanConfig.getApiKey() != null ? "已配置(长度:" + qianfanConfig.getApiKey().length() + ")" : "null"));
            System.out.println("    - model: " + qianfanConfig.getModel());
        }
        
        // 配置验证
        if (qianfanConfig == null) {
            throw new IllegalStateException("❌ qianfanConfig配置对象为null，请检查Spring配置");
        }
        if (qianfanConfig.getBaseUrl() == null || qianfanConfig.getBaseUrl().trim().isEmpty()) {
            throw new IllegalStateException("❌ qianfan.base-url配置为空，当前值: " + qianfanConfig.getBaseUrl());
        }
        if (qianfanConfig.getApiKey() == null || qianfanConfig.getApiKey().trim().isEmpty()) {
            throw new IllegalStateException("❌ qianfan.api-key配置为空");
        }
        
        // 构建prompt
        System.out.println("  步骤9.2: 构建AI提示词");
        String prompt = buildRecommendationPrompt(cartBookInfo);
        System.out.println("    - 提示词长度: " + prompt.length());
        System.out.println("    - 提示词前200字符: " + prompt.substring(0, Math.min(200, prompt.length())) + "...");

        // 构建请求体 (使用OpenAI兼容格式)
        System.out.println("  步骤9.3: 构建请求体");
        Map<String, Object> requestBody = new HashMap<>();
        
        List<Map<String, String>> messages = new ArrayList<>();
        
        // 系统消息
        Map<String, String> systemMessage = new HashMap<>();
        systemMessage.put("role", "system");
        systemMessage.put("content", "你是一个专业的图书推荐专家，能够根据用户的阅读偏好推荐相关书籍。");
        messages.add(systemMessage);
        
        // 用户消息
        Map<String, String> userMessage = new HashMap<>();
        userMessage.put("role", "user");
        userMessage.put("content", prompt);
        messages.add(userMessage);
        
        requestBody.put("model", qianfanConfig.getModel());
        requestBody.put("messages", messages);
        requestBody.put("temperature", 0.7);
        requestBody.put("max_tokens", 2000);

        System.out.println("    - 请求模型: " + requestBody.get("model"));
        System.out.println("    - 消息数量: " + messages.size());

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            System.out.println("  步骤9.4: 创建HTTP请求");
            HttpPost httpPost = new HttpPost(qianfanConfig.getBaseUrl());
            httpPost.setHeader("Content-Type", "application/json");
            httpPost.setHeader("Authorization", "Bearer " + qianfanConfig.getApiKey());

            String jsonString = objectMapper.writeValueAsString(requestBody);
            StringEntity entity = new StringEntity(jsonString, StandardCharsets.UTF_8);
            httpPost.setEntity(entity);

            System.out.println("    - 请求URL: " + qianfanConfig.getBaseUrl());
            System.out.println("    - 请求体大小: " + jsonString.length() + " 字符");
            log.info("调用千帆AI v2，请求体: {}", jsonString);

            System.out.println("  步骤9.5: 发送HTTP请求到千帆AI");
            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                String responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
                
                System.out.println("  步骤9.6: 收到千帆AI响应");
                System.out.println("    - 响应状态码: " + response.getStatusLine().getStatusCode());
                System.out.println("    - 响应体大小: " + responseBody.length() + " 字符");
                log.info("千帆AI v2响应: {}", responseBody);
                
                JsonNode jsonNode = objectMapper.readTree(responseBody);
                if (jsonNode.has("choices") && jsonNode.get("choices").isArray() && 
                    jsonNode.get("choices").size() > 0) {
                    JsonNode firstChoice = jsonNode.get("choices").get(0);
                    if (firstChoice.has("message") && firstChoice.get("message").has("content")) {
                        String content = firstChoice.get("message").get("content").asText();
                        System.out.println("  ✅ 千帆AI调用成功，提取到内容");
                        System.out.println("    - 内容长度: " + content.length());
                        return content;
                    }
                }
                
                System.out.println("  ❌ 千帆AI响应格式异常");
                log.error("千帆AI v2调用失败: {}", responseBody);
                throw new RuntimeException("千帆AI调用失败: " + responseBody);
            }
        } catch (Exception e) {
            System.out.println("  ❌ 千帆AI调用发生异常: " + e.getClass().getSimpleName() + " - " + e.getMessage());
            throw e;
        }
    }

    private String buildRecommendationPrompt(List<String> cartBookInfo) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("我是一个书店的客户，我的购物车中有以下书籍：\n");
        
        for (int i = 0; i < cartBookInfo.size(); i++) {
            prompt.append(i + 1).append(". ").append(cartBookInfo.get(i)).append("\n");
        }
        
        prompt.append("\n请根据我购物车中的书籍，为我推荐5本我可能喜欢的【其他】书籍，并给出推荐理由。");
        prompt.append("重要要求：\n");
        prompt.append("1. 请不要推荐我购物车中已有的书籍\n");
        prompt.append("2. 推荐的书籍应该与购物车中的书籍有相关性，但必须是不同的书\n");
        prompt.append("3. 推荐书籍可以是同一作者的其他作品、同一类型的其他书籍、或相关主题的书籍\n");
        prompt.append("\n请按照以下JSON格式返回推荐结果：\n");
        prompt.append("{\n");
        prompt.append("  \"recommendationReason\": \"整体推荐理由\",\n");
        prompt.append("  \"books\": [\n");
        prompt.append("    {\n");
        prompt.append("      \"title\": \"书名\",\n");
        prompt.append("      \"author\": \"作者\",\n");
        prompt.append("      \"reason\": \"推荐理由\"\n");
        prompt.append("    }\n");
        prompt.append("  ]\n");
        prompt.append("}\n");
        prompt.append("请确保推荐的书籍质量高，与购物车中的书籍有相关性，但绝对不要重复购物车中的书籍。");
        
        return prompt.toString();
    }

    private BookRecommendationDto parseAIResponseAndMatchBooks(String aiResponse, List<String> cartBookTitles) {
        BookRecommendationDto recommendation = new BookRecommendationDto();
        recommendation.setCartBookTitles(cartBookTitles);
        
        try {
            // 尝试解析JSON格式的响应
            JsonNode jsonNode = objectMapper.readTree(aiResponse);
            
            if (jsonNode.has("recommendationReason")) {
                recommendation.setRecommendationReason(jsonNode.get("recommendationReason").asText());
            }
            
            List<BookRecommendationDto.RecommendedBook> recommendedBooks = new ArrayList<>();
            
            if (jsonNode.has("books")) {
                JsonNode booksNode = jsonNode.get("books");
                for (JsonNode bookNode : booksNode) {
                    BookRecommendationDto.RecommendedBook recommendedBook = 
                        new BookRecommendationDto.RecommendedBook();
                    
                    String title = bookNode.get("title").asText();
                    String author = bookNode.get("author").asText();
                    String reason = bookNode.has("reason") ? bookNode.get("reason").asText() : "";
                    
                    // 检查是否与购物车中的书籍重复
                    boolean isDuplicate = false;
                    for (String cartTitle : cartBookTitles) {
                        if (title.equals(cartTitle) || 
                            title.contains(cartTitle) || 
                            cartTitle.contains(title)) {
                            isDuplicate = true;
                            log.warn("跳过推荐书籍，与购物车重复: {} (购物车中有: {})", title, cartTitle);
                            break;
                        }
                    }
                    
                    // 如果重复，跳过这本书
                    if (isDuplicate) {
                        continue;
                    }
                    
                    recommendedBook.setTitle(title);
                    recommendedBook.setAuthor(author);
                    recommendedBook.setReason(reason);
                    
                    // 尝试在数据库中查找匹配的书籍
                    Book matchedBook = findBookInDatabase(title, author);
                    if (matchedBook != null) {
                        recommendedBook.setBookId(matchedBook.getId());
                        recommendedBook.setExistsInDatabase(true);
                        recommendedBook.setPrice(matchedBook.getPrice());
                        recommendedBook.setDescription(matchedBook.getDescription());
                    } else {
                        recommendedBook.setExistsInDatabase(false);
                    }
                    
                    recommendedBooks.add(recommendedBook);
                }
            }
            
            recommendation.setRecommendedBooks(recommendedBooks);
            
        } catch (Exception e) {
            log.error("解析AI响应失败，使用文本解析方式", e);
            // 如果JSON解析失败，使用简单的文本解析
            recommendation = parseTextResponse(aiResponse, cartBookTitles);
        }
        
        return recommendation;
    }

    private BookRecommendationDto parseTextResponse(String aiResponse, List<String> cartBookTitles) {
        BookRecommendationDto recommendation = new BookRecommendationDto();
        recommendation.setCartBookTitles(cartBookTitles);
        recommendation.setRecommendationReason("基于您购物车中的书籍，AI为您精心挑选了以下推荐：");
        
        List<BookRecommendationDto.RecommendedBook> recommendedBooks = new ArrayList<>();
        
        // 简单文本解析逻辑，可以根据实际需要改进
        String[] lines = aiResponse.split("\n");
        for (String line : lines) {
            if (line.contains("《") && line.contains("》")) {
                BookRecommendationDto.RecommendedBook recommendedBook = 
                    new BookRecommendationDto.RecommendedBook();
                
                // 提取书名
                int start = line.indexOf("《");
                int end = line.indexOf("》");
                if (start >= 0 && end > start) {
                    String title = line.substring(start + 1, end);
                    
                    // 检查是否与购物车中的书籍重复
                    boolean isDuplicate = false;
                    for (String cartTitle : cartBookTitles) {
                        if (title.equals(cartTitle) || 
                            title.contains(cartTitle) || 
                            cartTitle.contains(title)) {
                            isDuplicate = true;
                            log.warn("跳过推荐书籍，与购物车重复: {} (购物车中有: {})", title, cartTitle);
                            break;
                        }
                    }
                    
                    // 如果重复，跳过这本书
                    if (isDuplicate) {
                        continue;
                    }
                    
                    recommendedBook.setTitle(title);
                    recommendedBook.setReason("AI智能推荐");
                    
                    // 尝试在数据库中查找
                    Book matchedBook = findBookInDatabase(title, null);
                    if (matchedBook != null) {
                        recommendedBook.setBookId(matchedBook.getId());
                        recommendedBook.setAuthor(matchedBook.getAuthor());
                        recommendedBook.setExistsInDatabase(true);
                        recommendedBook.setPrice(matchedBook.getPrice());
                        recommendedBook.setDescription(matchedBook.getDescription());
                    } else {
                        recommendedBook.setExistsInDatabase(false);
                    }
                    
                    recommendedBooks.add(recommendedBook);
                }
            }
        }
        
        recommendation.setRecommendedBooks(recommendedBooks);
        return recommendation;
    }

    /**
     * 在数据库中查找匹配的书籍
     */
    private Book findBookInDatabase(String title, String author) {
        // 获取所有书籍进行模糊匹配
        List<Book> allBooks = bookService.list();
        
        for (Book book : allBooks) {
            // 书名模糊匹配
            if (book.getName() != null && 
                (book.getName().contains(title) || title.contains(book.getName()))) {
                return book;
            }
            
            // 如果提供了作者信息，也进行作者匹配
            if (author != null && book.getAuthor() != null &&
                (book.getAuthor().contains(author) || author.contains(book.getAuthor()))) {
                return book;
            }
        }
        
        return null;
    }

    private BookRecommendationDto createEmptyRecommendation() {
        BookRecommendationDto recommendation = new BookRecommendationDto();
        recommendation.setRecommendationReason("暂无推荐，请先添加一些书籍到购物车");
        recommendation.setRecommendedBooks(new ArrayList<>());
        recommendation.setCartBookTitles(new ArrayList<>());
        return recommendation;
    }
} 