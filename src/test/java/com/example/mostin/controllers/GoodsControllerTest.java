package com.example.mostin.controllers;

import com.example.mostin.models.Goods;
import com.example.mostin.repositories.GoodsRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GoodsController.class)
class GoodsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GoodsRepository goodsRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Goods testGoods;

    @BeforeEach
    void setUp() {
        testGoods = new Goods();
        testGoods.setBarcode("1234567890");
        testGoods.setGoodsName("Sample Product");
    }

    @Test
    void should_returnAllGoods_when_getAllGoodsRequested() throws Exception {
        // Given
        Goods goods1 = new Goods();
        goods1.setBarcode("1111111111");
        goods1.setGoodsName("Product A");
        
        Goods goods2 = new Goods();
        goods2.setBarcode("2222222222");
        goods2.setGoodsName("Product B");

        when(goodsRepository.findAll()).thenReturn(Arrays.asList(goods1, goods2));

        // When & Then
        mockMvc.perform(get("/api/goods"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].barcode").value("1111111111"))
                .andExpect(jsonPath("$[0].goodsName").value("Product A"))
                .andExpect(jsonPath("$[1].barcode").value("2222222222"))
                .andExpect(jsonPath("$[1].goodsName").value("Product B"));

        verify(goodsRepository).findAll();
    }

    @Test
    void should_returnEmptyList_when_noGoodsExist() throws Exception {
        // Given
        when(goodsRepository.findAll()).thenReturn(Collections.emptyList());

        // When & Then
        mockMvc.perform(get("/api/goods"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));

        verify(goodsRepository).findAll();
    }

    @Test
    void should_createGoods_when_validGoodsProvided() throws Exception {
        // Given
        Goods newGoods = new Goods();
        newGoods.setBarcode("9999999999");
        newGoods.setGoodsName("New Product");
        
        Goods savedGoods = new Goods();
        savedGoods.setBarcode("9999999999");
        savedGoods.setGoodsName("New Product");

        when(goodsRepository.save(any(Goods.class))).thenReturn(savedGoods);

        // When & Then
        mockMvc.perform(post("/api/goods")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newGoods)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.barcode").value("9999999999"))
                .andExpect(jsonPath("$.goodsName").value("New Product"));

        verify(goodsRepository).save(any(Goods.class));
    }

    @Test
    void should_handleMultipleGoodsCreation_when_calledMultipleTimes() throws Exception {
        // Given
        Goods goods1 = new Goods();
        goods1.setBarcode("1111111111");
        goods1.setGoodsName("First Product");
        
        Goods goods2 = new Goods();
        goods2.setBarcode("2222222222");
        goods2.setGoodsName("Second Product");

        when(goodsRepository.save(any(Goods.class)))
                .thenReturn(goods1)
                .thenReturn(goods2);

        // When & Then - First creation
        mockMvc.perform(post("/api/goods")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(goods1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.barcode").value("1111111111"))
                .andExpect(jsonPath("$.goodsName").value("First Product"));

        // When & Then - Second creation
        mockMvc.perform(post("/api/goods")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(goods2)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.barcode").value("2222222222"))
                .andExpect(jsonPath("$.goodsName").value("Second Product"));

        verify(goodsRepository, times(2)).save(any(Goods.class));
    }

    @Test
    void should_acceptEmptyRequestBody_when_creatingGoods() throws Exception {
        // Given
        Goods emptyGoods = new Goods();
        when(goodsRepository.save(any(Goods.class))).thenReturn(emptyGoods);

        // When & Then
        mockMvc.perform(post("/api/goods")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk());

        verify(goodsRepository).save(any(Goods.class));
    }

    @Test
    void should_handleContentTypeJson_when_requestingAllGoods() throws Exception {
        // Given
        when(goodsRepository.findAll()).thenReturn(Collections.emptyList());

        // When & Then
        mockMvc.perform(get("/api/goods")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(goodsRepository).findAll();
    }

    @Test
    void should_handleContentTypeJson_when_creatingGoods() throws Exception {
        // Given
        Goods newGoods = new Goods();
        when(goodsRepository.save(any(Goods.class))).thenReturn(newGoods);

        // When & Then
        mockMvc.perform(post("/api/goods")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newGoods))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(goodsRepository).save(any(Goods.class));
    }
}