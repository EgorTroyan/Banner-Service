package com.troian.bannerservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.troian.bannerservice.exception.ErrorMessage;
import com.troian.bannerservice.model.entity.Banner;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.Assert;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@TestPropertySource(locations = "/application.yml")
@AutoConfigureMockMvc
@Sql({"/schemas.sql", "/import-data.sql"})
class BannerControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;


    @Test
    void getBannerWithoutBearerToken() throws Exception {
        mockMvc.perform(get("/banner"))
                .andExpect(status().is(405));
    }

    @Test
    void getBannerWithBadBearerToken() throws Exception {
        mockMvc.perform(get("/banner")
                        .header("Authorization", "Bearer BadPassword"))
                .andExpect(status().is(405));
    }

    @Test
    void getAllBannerWithBearerToken() throws Exception {
        mockMvc.perform(get("/banner")
                        .header("Authorization", "Bearer TestPassword"))
                .andExpect(status().isOk());
    }

    @Test
    void getBannerWithBearerToken() throws Exception {
        mockMvc.perform(get("/banner/1")
                        .header("Authorization", "Bearer TestPassword"))
                .andExpect(status().isOk());
    }

    @Test
    void getFilteredBannerWithBearerToken() throws Exception {
        MvcResult result = mockMvc.perform(get("/banner/filter/3")
                        .header("Authorization", "Bearer TestPassword"))
                .andExpect(status().isOk())
                .andReturn();
        String object = result.getResponse().getContentAsString();
        List bannerList = objectMapper.readValue(object, List.class);
        for (Object o : bannerList) {
            if (o instanceof Banner) {
                Assert.isTrue(((Banner) o).getName().contains("Banner3"),"");
            }
        }
    }

    @Test
    void deleteBannerWithBearerToken() throws Exception {
        MvcResult result = mockMvc.perform(delete("/banner/2")
                        .header("Authorization", "Bearer TestPassword"))
                .andExpect(status().isOk())
                .andReturn();
        String object = result.getResponse().getContentAsString();
        Banner banner = objectMapper.readValue(object, Banner.class);
        Assert.isTrue(!banner.isActive(), "not deactivated");
    }

    @Test
    void addBannerWithBearerToken() throws Exception {
        Banner bannerForAdd = new Banner("New banner", "text new banner",1.1f, false);
        MvcResult result = mockMvc.perform(post("/banner?cat=1&cat=2")
                        .header("Authorization", "Bearer TestPassword")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(bannerForAdd)))
                .andExpect(status().isOk())
                .andReturn();
        String object = result.getResponse().getContentAsString();
        Banner banner = objectMapper.readValue(object, Banner.class);
        Assert.isTrue(banner.getId() > 0 && banner.isActive(), "");
    }

    @Test
    void addBannerWithEmptyNameWithBearerToken() throws Exception {
        Banner bannerForAdd = new Banner("", "b_t",0.5f, false);
        MvcResult result = mockMvc.perform(post("/banner?cat=1&cat=2")
                        .header("Authorization", "Bearer TestPassword")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(bannerForAdd)))
                .andExpect(status().is(400))
                .andReturn();
        String object = result.getResponse().getContentAsString();
        ErrorMessage message = objectMapper.readValue(object, ErrorMessage.class);
        Assert.isTrue(message.getMessage().contains("must not be blank"), "");
    }

    @Test
    void addBannerWithEmptyTextWithBearerToken() throws Exception {
        Banner bannerForAdd = new Banner("New banner", "", 0.5f,false);
        MvcResult result = mockMvc.perform(post("/banner?cat=1&cat=2")
                        .header("Authorization", "Bearer TestPassword")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(bannerForAdd)))
                .andExpect(status().is(400))
                .andReturn();
        String object = result.getResponse().getContentAsString();
        ErrorMessage message = objectMapper.readValue(object, ErrorMessage.class);
        Assert.isTrue(message.getMessage().contains("must not be blank"), "");
    }

    @Test
    void addBannerWithExistingNameWithBearerToken() throws Exception {
        Banner bannerForAdd = new Banner("Banner1", "c",0.5f, false);
        MvcResult result = mockMvc.perform(post("/banner?cat=1&cat=2")
                        .header("Authorization", "Bearer TestPassword")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(bannerForAdd)))
                .andExpect(status().is(400))
                .andReturn();
        String object = result.getResponse().getContentAsString();
        ErrorMessage message = objectMapper.readValue(object, ErrorMessage.class);
        Assert.isTrue(message.getMessage().contains("already exist"), "");
    }

    @Test
    void addBannerWithoutCategoryInParamNameWithBearerToken() throws Exception {
        Banner bannerForAdd = new Banner("Banner1", "c",0.5f, false);
        MvcResult result = mockMvc.perform(post("/banner")
                        .header("Authorization", "Bearer TestPassword")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(bannerForAdd)))
                .andExpect(status().is(400))
                .andReturn();
        String object = result.getResponse().getContentAsString();
        ErrorMessage message = objectMapper.readValue(object, ErrorMessage.class);
        Assert.isTrue(message.getMessage().contains("Required request parameter 'cat'"), "");
    }

    @Test
    void editBannerWithBearerToken() throws Exception {
        Banner updatedBanner = new Banner("Upd banner 3", "ban_3_upd",0.7f, true);
        MvcResult result = mockMvc.perform(put("/banner/3")
                        .header("Authorization", "Bearer TestPassword")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(updatedBanner)))
                .andExpect(status().isOk())
                .andReturn();
        String object = result.getResponse().getContentAsString();
        Banner banner = objectMapper.readValue(object, Banner.class);
        Assert.isTrue(banner.getId() == 3 && banner.getText().equals("ban_3_upd"), "");
    }

}