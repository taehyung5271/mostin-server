package com.example.mostin.controllers;

import com.example.mostin.models.Ordering;
import com.example.mostin.repositories.OrderingRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderingController.class)
class OrderingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderingRepository orderingRepository;

    private ObjectMapper objectMapper;

    private Ordering testOrdering;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        testOrdering = new Ordering();
        testOrdering.setOrderingDay(LocalDate.of(2024, 1, 15));
        testOrdering.setEmployeeId("EMP001");
        testOrdering.setBarcode("1234567890");
        testOrdering.setEmployeeName("John Doe");
        testOrdering.setBoxNum(5);
        testOrdering.setGoodsName("Product A");
    }

    @Test
    void should_createOrderWithCurrentDate_when_validOrderProvided() throws Exception {
        // Given
        Ordering newOrder = new Ordering();
        newOrder.setEmployeeId("EMP001");
        newOrder.setBarcode("1234567890");
        newOrder.setEmployeeName("John Doe");
        newOrder.setBoxNum(3);
        newOrder.setGoodsName("Product B");

        Ordering savedOrder = new Ordering();
        savedOrder.setOrderingDay(LocalDate.now());
        savedOrder.setEmployeeId("EMP001");
        savedOrder.setBarcode("1234567890");
        savedOrder.setEmployeeName("John Doe");
        savedOrder.setBoxNum(3);
        savedOrder.setGoodsName("Product B");

        when(orderingRepository.save(any(Ordering.class))).thenReturn(savedOrder);

        // When & Then
        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newOrder)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.employeeId").value("EMP001"))
                .andExpect(jsonPath("$.barcode").value("1234567890"))
                .andExpect(jsonPath("$.employeeName").value("John Doe"))
                .andExpect(jsonPath("$.boxNum").value(3))
                .andExpect(jsonPath("$.goodsName").value("Product B"))
                .andExpect(jsonPath("$.orderingDay").exists());

        verify(orderingRepository).save(any(Ordering.class));
    }

    @Test
    void should_returnOrdersDescendingByDate_when_getOrdersByEmployee() throws Exception {
        // Given
        Ordering order1 = new Ordering();
        order1.setEmployeeId("EMP001");
        order1.setOrderingDay(LocalDate.of(2024, 1, 16));
        order1.setBarcode("1111111111");
        order1.setGoodsName("Product A");

        Ordering order2 = new Ordering();
        order2.setEmployeeId("EMP001");
        order2.setOrderingDay(LocalDate.of(2024, 1, 15));
        order2.setBarcode("2222222222");
        order2.setGoodsName("Product B");

        when(orderingRepository.findByEmployeeIdOrderByOrderingDayDesc("EMP001"))
                .thenReturn(Arrays.asList(order1, order2));

        // When & Then
        mockMvc.perform(get("/api/orders")
                        .param("employeeId", "EMP001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].orderingDay").value("2024-01-16"))
                .andExpect(jsonPath("$[1].orderingDay").value("2024-01-15"))
                .andExpect(jsonPath("$[0].goodsName").value("Product A"))
                .andExpect(jsonPath("$[1].goodsName").value("Product B"));

        verify(orderingRepository).findByEmployeeIdOrderByOrderingDayDesc("EMP001");
    }

    @Test
    void should_returnEmptyList_when_noOrdersFoundForEmployee() throws Exception {
        // Given
        when(orderingRepository.findByEmployeeIdOrderByOrderingDayDesc("EMP999"))
                .thenReturn(Collections.emptyList());

        // When & Then
        mockMvc.perform(get("/api/orders")
                        .param("employeeId", "EMP999"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));

        verify(orderingRepository).findByEmployeeIdOrderByOrderingDayDesc("EMP999");
    }

    @Test
    void should_returnOrderDetailsForSpecificDate_when_validDateProvided() throws Exception {
        // Given
        LocalDate targetDate = LocalDate.of(2024, 1, 15);
        
        Ordering order1 = new Ordering();
        order1.setEmployeeId("EMP001");
        order1.setOrderingDay(targetDate);
        order1.setBarcode("1111111111");
        order1.setGoodsName("Product A");

        Ordering order2 = new Ordering();
        order2.setEmployeeId("EMP001");
        order2.setOrderingDay(targetDate);
        order2.setBarcode("2222222222");
        order2.setGoodsName("Product B");

        when(orderingRepository.findByEmployeeIdAndOrderingDay("EMP001", targetDate))
                .thenReturn(Arrays.asList(order1, order2));

        // When & Then
        mockMvc.perform(get("/api/orders/details")
                        .param("employeeId", "EMP001")
                        .param("date", "2024-01-15"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].orderingDay").value("2024-01-15"))
                .andExpect(jsonPath("$[1].orderingDay").value("2024-01-15"));

        verify(orderingRepository).findByEmployeeIdAndOrderingDay("EMP001", targetDate);
    }

    @Test
    void should_returnOrderHistory_when_getOrderHistoryEndpoint() throws Exception {
        // Given  
        when(orderingRepository.findByEmployeeIdOrderByOrderingDayDesc("EMP001"))
                .thenReturn(Arrays.asList(testOrdering));

        // When & Then
        mockMvc.perform(get("/api/orders/history")
                        .param("employeeId", "EMP001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].employeeId").value("EMP001"));

        verify(orderingRepository).findByEmployeeIdOrderByOrderingDayDesc("EMP001");
    }

    @Test
    void should_deleteOrdersTransactionally_when_validEmployeeIdAndDateProvided() throws Exception {
        // Given
        LocalDate today = LocalDate.now();
        
        // Use doNothing() for void method
        doNothing().when(orderingRepository).deleteByEmployeeIdAndOrderingDay("EMP001", today);

        // When & Then
        mockMvc.perform(delete("/api/orders")
                        .param("employeeId", "EMP001")
                        .param("date", "2024-01-15")) // Note: The controller uses today's date, not the provided date
                .andExpect(status().isOk());

        // Verify the transactional delete was called with today's date (not the provided date)
        verify(orderingRepository).deleteByEmployeeIdAndOrderingDay("EMP001", today);
    }

    @Test
    void should_deleteOrdersForToday_when_deleteEndpointCalled() throws Exception {
        // Given
        LocalDate today = LocalDate.now();
        
        doNothing().when(orderingRepository).deleteByEmployeeIdAndOrderingDay("EMP001", today);

        // When & Then
        mockMvc.perform(delete("/api/orders")
                        .param("employeeId", "EMP001")
                        .param("date", "some-irrelevant-date"))
                .andExpect(status().isOk());

        // Verify it always uses today's date regardless of the date parameter
        verify(orderingRepository).deleteByEmployeeIdAndOrderingDay("EMP001", today);
    }

    @Test
    void should_handleMultipleOrdersForSameDay_when_creatingOrders() throws Exception {
        // Given
        Ordering order1 = new Ordering();
        order1.setEmployeeId("EMP001");
        order1.setBarcode("1111111111");
        order1.setGoodsName("Product A");
        order1.setBoxNum(2);

        Ordering order2 = new Ordering();
        order2.setEmployeeId("EMP001");
        order2.setBarcode("2222222222");
        order2.setGoodsName("Product B");
        order2.setBoxNum(3);

        when(orderingRepository.save(any(Ordering.class)))
                .thenReturn(order1)
                .thenReturn(order2);

        // When & Then - First order
        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(order1)))
                .andExpect(status().isOk());

        // When & Then - Second order
        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(order2)))
                .andExpect(status().isOk());

        verify(orderingRepository, times(2)).save(any(Ordering.class));
    }

    @Test
    void should_handleEmptyOrderDetailsResponse_when_noOrdersForDate() throws Exception {
        // Given
        LocalDate targetDate = LocalDate.of(2024, 1, 15);
        when(orderingRepository.findByEmployeeIdAndOrderingDay("EMP001", targetDate))
                .thenReturn(Collections.emptyList());

        // When & Then
        mockMvc.perform(get("/api/orders/details")
                        .param("employeeId", "EMP001")
                        .param("date", "2024-01-15"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));

        verify(orderingRepository).findByEmployeeIdAndOrderingDay("EMP001", targetDate);
    }

    @Test 
    void should_setOrderingDayToCurrentDate_when_creatingOrder() throws Exception {
        // Given
        Ordering newOrder = new Ordering();
        newOrder.setEmployeeId("EMP001");
        newOrder.setBarcode("9999999999");
        newOrder.setEmployeeName("Test User");
        newOrder.setBoxNum(1);
        newOrder.setGoodsName("Test Product");
        // Note: orderingDay is not set

        LocalDate today = LocalDate.now();
        
        when(orderingRepository.save(any(Ordering.class))).thenAnswer(invocation -> {
            Ordering order = invocation.getArgument(0);
            // Verify that the orderingDay is set to today when the controller processes the order
            assert order.getOrderingDay().equals(today);
            return order;
        });

        // When & Then
        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newOrder)))
                .andExpect(status().isOk());

        verify(orderingRepository).save(any(Ordering.class));
    }
}