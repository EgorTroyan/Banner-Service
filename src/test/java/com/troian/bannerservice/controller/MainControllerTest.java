package com.troian.bannerservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.troian.bannerservice.exception.ErrorMessage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.Assert;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@TestPropertySource(locations = "/application.yml")
@AutoConfigureMockMvc
class MainControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;


    @Test
    @Sql({"/schemas.sql", "/import-data.sql"})
    void getBannerWithOneExistCategory() throws Exception {
        MvcResult result1 = mockMvc.perform(get("/bid?cat=cat1")
                        .header("User-Agent", "Test Agent"))
                .andExpect(status().isOk())
                .andReturn();
        String text1 = result1.getResponse().getContentAsString();
        Assert.isTrue(text1.equals("third banner"), "");
    }

    @Test
    @Sql({"/schemas.sql", "/import-data.sql"})
    void getBannerWithTwoNotMostExpensiveExistCategory() throws Exception {
        MvcResult result1 = mockMvc.perform(get("/bid?cat=cat2&cat=cat3")
                        .header("User-Agent", "Test Agent"))
                .andExpect(status().isOk())
                .andReturn();
        String text1 = result1.getResponse().getContentAsString();
        Assert.isTrue(text1.equals("second banner"), "");
    }

    @Test
    @Sql({"/schemas.sql", "/import-data.sql"})
    void getBannerWithoutCategory() throws Exception {
        MvcResult result1 = mockMvc.perform(get("/bid")
                        .header("User-Agent", "Test Agent"))
                .andExpect(status().is(400))
                .andReturn();
        String text1 = result1.getResponse().getContentAsString();
        ErrorMessage message = objectMapper.readValue(text1, ErrorMessage.class);
        Assert.isTrue(message.getMessage().contains("Required request parameter 'cat'"), "");
    }

    @Test
    @Sql({"/schemas.sql", "/import-data.sql"})
    void getBannerWithExistAndNotExistCategory() throws Exception {
        MvcResult result1 = mockMvc.perform(get("/bid?cat=cat2&cat=cat33")
                        .header("User-Agent", "Test Agent"))
                .andExpect(status().is(200))
                .andReturn();
        String text1 = result1.getResponse().getContentAsString();
        Assert.isTrue(text1.contains("second banner"), "");
    }

    @Test
    @Sql({"/schemas.sql", "/import-data.sql"})
    void getBannerManyTimesWithOneIpAndCheckBestPricesAndDayDuplicates() throws Exception {
        MvcResult result1 = mockMvc.perform(get("/bid?cat=cat1")
                        .header("User-Agent", "Test Agent"))
                .andExpect(status().isOk())
                .andReturn();
        String text1 = result1.getResponse().getContentAsString();
        Assert.isTrue(text1.equals("third banner"), "");
        MvcResult result2 = mockMvc.perform(get("/bid?cat=cat1")
                .header("User-Agent", "Test Agent"))
                .andExpect(status().isOk())
                .andReturn();
        String text2 = result2.getResponse().getContentAsString();
        Assert.isTrue(text2.equals("second banner"), "");
        MvcResult result3 = mockMvc.perform(get("/bid?cat=cat1")
                .header("User-Agent", "Test Agent"))
                .andExpect(status().isOk())
                .andReturn();
        String text3 = result3.getResponse().getContentAsString();
        Assert.isTrue(text3.equals("first banner"), "");
        mockMvc.perform(get("/bid?cat=cat1")
                .header("User-Agent", "Test Agent"))
                .andExpect(status().is(204));
    }

    @Test
    @Sql({"/schemas.sql", "/import-data.sql"})
    void getBannerTwoTimesWithDifferentIpAndCheckBestPrices() throws Exception {
        MvcResult result1 = mockMvc.perform(get("/bid?cat=cat1")
                        .with(request->{request.setRemoteHost("192.168.0.2");return request;})
                        .header("User-Agent", "Test Agent"))
                .andExpect(status().isOk())
                .andReturn();
        String text1 = result1.getResponse().getContentAsString();
        Assert.isTrue(text1.equals("third banner"), "");
        MvcResult result2 = mockMvc.perform(get("/bid?cat=cat1")
                        .with(request->{request.setRemoteHost("192.168.0.1");return request;})
                        .header("User-Agent", "Test Agent"))
                .andExpect(status().isOk())
                .andReturn();
        String text2 = result2.getResponse().getContentAsString();
        Assert.isTrue(text2.equals("third banner"), "");
    }
}