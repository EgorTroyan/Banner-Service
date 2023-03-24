package com.troian.bannerservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.troian.bannerservice.exception.ErrorMessage;
import com.troian.bannerservice.model.entity.Category;
import org.junit.jupiter.api.Assertions;
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
class CategoryControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;


    @Test
    void getCategoryWithoutBearerToken() throws Exception {
        mockMvc.perform(get("/category"))
                .andExpect(status().is(405));
    }

    @Test
    void getCategoryWithBadBearerToken() throws Exception {
        mockMvc.perform(get("/category")
                        .header("Authorization", "Bearer BadPassword"))
                .andExpect(status().is(405));
    }

    @Test
    void getAllCategoryWithBearerToken() throws Exception {
        mockMvc.perform(get("/category")
                        .header("Authorization", "Bearer TestPassword"))
                .andExpect(status().isOk());
    }

    @Test
    void getCategoryWithBearerToken() throws Exception {
        mockMvc.perform(get("/category/1")
                        .header("Authorization", "Bearer TestPassword"))
                .andExpect(status().isOk());
    }

    @Test
    void getFilteredCategoryWithBearerToken() throws Exception {
        MvcResult result = mockMvc.perform(get("/category/filter/3")
                        .header("Authorization", "Bearer TestPassword"))
                .andExpect(status().isOk())
                .andReturn();
        String object = result.getResponse().getContentAsString();
        List categoryList = objectMapper.readValue(object, List.class);
        for (Object o : categoryList) {
            if (o instanceof Category) {
                Assertions.assertEquals("Test_category2", ((Category) o).getName());
            }
        }
    }

    @Test
    void deleteNoLinksCategoryWithBearerToken() throws Exception {
        MvcResult result = mockMvc.perform(delete("/category/5")
                        .header("Authorization", "Bearer TestPassword"))
                .andExpect(status().isOk())
                .andReturn();
        String object = result.getResponse().getContentAsString();
        Category category = objectMapper.readValue(object, Category.class);
        Assert.isTrue(!category.isActive(), "deactivated");
    }

    @Test
    void deleteLinkedCategoryWithBearerToken() throws Exception {
        MvcResult result = mockMvc.perform(delete("/category/2")
                        .header("Authorization", "Bearer TestPassword"))
                .andExpect(status().is(400))
                .andReturn();
        String object = result.getResponse().getContentAsString();
        ErrorMessage message = objectMapper.readValue(object, ErrorMessage.class);
        Assert.isTrue(message.getMessage().contains("Exist linked banners"), "");
    }

    @Test
    void addCategoryWithBearerToken() throws Exception {
        Category categoryForAdd = new Category("New category", "cat_new", false);
        MvcResult result = mockMvc.perform(post("/category")
                        .header("Authorization", "Bearer TestPassword")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(categoryForAdd)))
                .andExpect(status().isOk())
                .andReturn();
        String object = result.getResponse().getContentAsString();
        Category category = objectMapper.readValue(object, Category.class);
        Assert.isTrue(category.getId() > 0 && category.isActive(), "");
    }

    @Test
    void addCategoryWithEmptyNameWithBearerToken() throws Exception {
        Category categoryForAdd = new Category("", "cat_new", false);
        MvcResult result = mockMvc.perform(post("/category")
                        .header("Authorization", "Bearer TestPassword")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(categoryForAdd)))
                .andExpect(status().is(400))
                .andReturn();
        String object = result.getResponse().getContentAsString();
        ErrorMessage message = objectMapper.readValue(object, ErrorMessage.class);
        Assert.isTrue(message.getMessage().contains("name is required"), "");
    }

    @Test
    void addCategoryWithEmptyNameIdWithBearerToken() throws Exception {
        Category categoryForAdd = new Category("New category", "", false);
        MvcResult result = mockMvc.perform(post("/category")
                        .header("Authorization", "Bearer TestPassword")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(categoryForAdd)))
                .andExpect(status().is(400))
                .andReturn();
        String object = result.getResponse().getContentAsString();
        ErrorMessage message = objectMapper.readValue(object, ErrorMessage.class);
        Assert.isTrue(message.getMessage().contains("name id is required"), "");
    }

    @Test
    void addCategoryWithExistingNameWithBearerToken() throws Exception {
        Category categoryForAdd = new Category("Test_category1", "c", false);
        MvcResult result = mockMvc.perform(post("/category")
                        .header("Authorization", "Bearer TestPassword")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(categoryForAdd)))
                .andExpect(status().is(400))
                .andReturn();
        String object = result.getResponse().getContentAsString();
        ErrorMessage message = objectMapper.readValue(object, ErrorMessage.class);
        Assert.isTrue(message.getMessage().contains("already exist"), "");
    }

    @Test
    void addCategoryWithExistingNameIdWithBearerToken() throws Exception {
        Category categoryForAdd = new Category("Tes", "cat1", false);
        MvcResult result = mockMvc.perform(post("/category")
                        .header("Authorization", "Bearer TestPassword")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(categoryForAdd)))
                .andExpect(status().is(400))
                .andReturn();
        String object = result.getResponse().getContentAsString();
        ErrorMessage message = objectMapper.readValue(object, ErrorMessage.class);
        Assert.isTrue(message.getMessage().contains("already exist"), "");
    }

    @Test
    void editCategoryWithBearerToken() throws Exception {
        Category updatedCategory = new Category("Upd category 3", "cat_3_upd", true);
        MvcResult result = mockMvc.perform(put("/category/3")
                        .header("Authorization", "Bearer TestPassword")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(updatedCategory)))
                .andExpect(status().isOk())
                .andReturn();
        String object = result.getResponse().getContentAsString();
        Category category = objectMapper.readValue(object, Category.class);
        Assert.isTrue(category.getId() == 3 && category.getNameId().equals("cat_3_upd"), "");
    }

}