package com.daniel.bookshop.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Data
@Component
public class QianfanConfig {
    
    /**
     * API Key (Bearer Token格式)
     */
    @Value("${qianfan.apiKey:}")
    private String apiKey;
    
    /**
     * 基础URL (v2版本)
     */
    @Value("${qianfan.baseUrl:}")
    private String baseUrl;
    
    /**
     * 使用的模型
     */
    @Value("${qianfan.model:ernie-3.5-8k}")
    private String model;
    
    @PostConstruct
    public void init() {
        System.out.println("=== QianfanConfig 配置加载检查 ===");
        System.out.println("apiKey: " + (apiKey != null ? "已加载(长度:" + apiKey.length() + ")" : "null"));
        System.out.println("baseUrl: " + baseUrl);
        System.out.println("model: " + model);
        
        // 如果配置文件读取失败，使用硬编码值
        if (baseUrl == null || baseUrl.trim().isEmpty()) {
            System.err.println("❌ 警告：从配置文件读取baseUrl失败，使用硬编码值");
            baseUrl = "https://qianfan.baidubce.com/v2/chat/completions";
        }
        if (apiKey == null || apiKey.trim().isEmpty()) {
            System.err.println("❌ 警告：从配置文件读取apiKey失败，使用硬编码值");
            apiKey = "bce-v3/ALTAK-gxQmLfd6LZOYRPnBLpxHA/6b656539eb2ac3ccdd66020482d64a3190a17b51";
        }
        
        System.out.println("最终配置:");
        System.out.println("apiKey: " + (apiKey != null ? "已配置(长度:" + apiKey.length() + ")" : "null"));
        System.out.println("baseUrl: " + baseUrl);
        System.out.println("model: " + model);
        System.out.println("==============================");
    }
    
    // 添加getter方法确保配置能正确加载
    public String getApiKey() {
        return apiKey;
    }
    
    public String getBaseUrl() {
        return baseUrl;
    }
    
    public String getModel() {
        return model;
    }
} 