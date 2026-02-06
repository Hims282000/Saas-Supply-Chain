package com.supplychain.tenant.components.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

public class TestUtils {
    
    private static final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());
    
    public static String asJsonString(final Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public static MockHttpServletRequestBuilder jsonPost(String url, Object body) {
        return post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(body));
    }
    
    public static MockHttpServletRequestBuilder jsonPut(String url, Object body) {
        return put(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(body));
    }
    
    public static MockHttpServletRequestBuilder jsonGet(String url) {
        return get(url)
                .contentType(MediaType.APPLICATION_JSON);
    }
    
    public static MockHttpServletRequestBuilder jsonDelete(String url) {
        return delete(url)
                .contentType(MediaType.APPLICATION_JSON);
    }
    
    public static MockHttpServletRequestBuilder authenticatedRequest(MockHttpServletRequestBuilder requestBuilder) {
        return requestBuilder.header("Authorization", "Bearer " + TestConstants.TEST_JWT_TOKEN);
    }
}