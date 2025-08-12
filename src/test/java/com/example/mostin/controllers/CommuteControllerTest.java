package com.example.mostin.controllers;

import com.example.mostin.models.Commute;
import com.example.mostin.repositories.CommuteRepository;
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
import java.time.LocalTime;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CommuteController.class)
class CommuteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommuteRepository commuteRepository;

    private ObjectMapper objectMapper;

    private Commute testCommute;
    private Map<String, String> clockInPayload;
    private Map<String, String> clockOutPayload;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        testCommute = new Commute();
        testCommute.setEmployeeId("EMP001");
        testCommute.setEmployeeName("John Doe");
        testCommute.setWorkPlaceName("Main Office");
        testCommute.setCommuteDay(LocalDate.of(2024, 1, 15));
        testCommute.setStartTime(LocalTime.of(9, 0));
        testCommute.setEndTime(LocalTime.of(18, 0));

        clockInPayload = new HashMap<>();
        clockInPayload.put("employeeId", "EMP001");
        clockInPayload.put("employeeName", "John Doe");
        clockInPayload.put("workPlaceName", "Main Office");
        clockInPayload.put("commuteDay", "2024-01-15");
        clockInPayload.put("startTime", "09:00");

        clockOutPayload = new HashMap<>();
        clockOutPayload.put("employeeId", "EMP001");
        clockOutPayload.put("endTime", "18:00");
    }

    @Test
    void should_createCommuteRecord_when_clockInWithValidData() throws Exception {
        // Given
        Commute savedCommute = new Commute();
        savedCommute.setEmployeeId("EMP001");
        savedCommute.setEmployeeName("John Doe");
        savedCommute.setWorkPlaceName("Main Office");
        savedCommute.setCommuteDay(LocalDate.of(2024, 1, 15));
        savedCommute.setStartTime(LocalTime.of(9, 0));

        when(commuteRepository.save(any(Commute.class))).thenReturn(savedCommute);

        // When & Then
        mockMvc.perform(post("/api/commute/clock-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clockInPayload)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.employeeId").value("EMP001"))
                .andExpect(jsonPath("$.employeeName").value("John Doe"))
                .andExpect(jsonPath("$.workPlaceName").value("Main Office"))
                .andExpect(jsonPath("$.commuteDay").value("2024-01-15"))
                .andExpect(jsonPath("$.startTime").value("09:00:00"));

        verify(commuteRepository).save(any(Commute.class));
    }

    @Test
    void should_updateEndTime_when_clockOutWithExistingRecord() throws Exception {
        // Given
        Commute existingCommute = new Commute();
        existingCommute.setEmployeeId("EMP001");
        existingCommute.setCommuteDay(LocalDate.now());
        existingCommute.setStartTime(LocalTime.of(9, 0));

        Commute updatedCommute = new Commute();
        updatedCommute.setEmployeeId("EMP001");
        updatedCommute.setCommuteDay(LocalDate.now());
        updatedCommute.setStartTime(LocalTime.of(9, 0));
        updatedCommute.setEndTime(LocalTime.of(18, 0));

        when(commuteRepository.findByEmployeeIdAndCommuteDay("EMP001", LocalDate.now()))
                .thenReturn(List.of(existingCommute));
        when(commuteRepository.save(any(Commute.class))).thenReturn(updatedCommute);

        // When & Then
        mockMvc.perform(put("/api/commute/clock-out")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clockOutPayload)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.employeeId").value("EMP001"))
                .andExpect(jsonPath("$.endTime").value("18:00:00"));

        verify(commuteRepository).findByEmployeeIdAndCommuteDay("EMP001", LocalDate.now());
        verify(commuteRepository).save(existingCommute);
    }

    @Test
    void should_return404_when_clockOutWithNoExistingRecord() throws Exception {
        // Given
        when(commuteRepository.findByEmployeeIdAndCommuteDay("EMP001", LocalDate.now()))
                .thenReturn(Collections.emptyList());

        // When & Then
        mockMvc.perform(put("/api/commute/clock-out")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clockOutPayload)))
                .andExpect(status().isNotFound());

        verify(commuteRepository).findByEmployeeIdAndCommuteDay("EMP001", LocalDate.now());
        verify(commuteRepository, never()).save(any(Commute.class));
    }

    @Test
    void should_returnTodayCommute_when_recordExistsForToday() throws Exception {
        // Given
        LocalDate today = LocalDate.now();
        when(commuteRepository.findByEmployeeIdAndCommuteDay("EMP001", today))
                .thenReturn(List.of(testCommute));

        // When & Then
        mockMvc.perform(get("/api/commute/today")
                        .param("employeeId", "EMP001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.employeeId").value("EMP001"))
                .andExpect(jsonPath("$.employeeName").value("John Doe"))
                .andExpect(jsonPath("$.startTime").value("09:00:00"));

        verify(commuteRepository).findByEmployeeIdAndCommuteDay("EMP001", today);
    }

    @Test
    void should_return404_when_noCommuteRecordForToday() throws Exception {
        // Given
        LocalDate today = LocalDate.now();
        when(commuteRepository.findByEmployeeIdAndCommuteDay("EMP001", today))
                .thenReturn(Collections.emptyList());

        // When & Then
        mockMvc.perform(get("/api/commute/today")
                        .param("employeeId", "EMP001"))
                .andExpect(status().isNotFound());

        verify(commuteRepository).findByEmployeeIdAndCommuteDay("EMP001", today);
    }

    @Test
    void should_returnMonthlyCommutes_when_validDateRangeProvided() throws Exception {
        // Given
        Commute commute1 = new Commute();
        commute1.setEmployeeId("EMP001");
        commute1.setCommuteDay(LocalDate.of(2024, 1, 15));
        commute1.setStartTime(LocalTime.of(9, 0));

        Commute commute2 = new Commute();
        commute2.setEmployeeId("EMP001");
        commute2.setCommuteDay(LocalDate.of(2024, 1, 16));
        commute2.setStartTime(LocalTime.of(8, 30));

        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 1, 31);

        when(commuteRepository.findByEmployeeIdAndCommuteDayBetween("EMP001", startDate, endDate))
                .thenReturn(Arrays.asList(commute1, commute2));

        // When & Then
        mockMvc.perform(get("/api/commute/monthly")
                        .param("employeeId", "EMP001")
                        .param("year", "2024")
                        .param("month", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].commuteDay").value("2024-01-15"))
                .andExpect(jsonPath("$[1].commuteDay").value("2024-01-16"));

        verify(commuteRepository).findByEmployeeIdAndCommuteDayBetween("EMP001", startDate, endDate);
    }

    @Test
    void should_returnEmptyList_when_noMonthlyCommutesFound() throws Exception {
        // Given
        LocalDate startDate = LocalDate.of(2024, 2, 1);
        LocalDate endDate = LocalDate.of(2024, 2, 29);

        when(commuteRepository.findByEmployeeIdAndCommuteDayBetween("EMP001", startDate, endDate))
                .thenReturn(Collections.emptyList());

        // When & Then
        mockMvc.perform(get("/api/commute/monthly")
                        .param("employeeId", "EMP001")
                        .param("year", "2024")
                        .param("month", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));

        verify(commuteRepository).findByEmployeeIdAndCommuteDayBetween("EMP001", startDate, endDate);
    }

    @Test
    void should_returnRecentCommute_when_recordExists() throws Exception {
        // Given
        when(commuteRepository.findTopByEmployeeIdAndEmployeeNameOrderByCommuteDayDescStartTimeDesc("EMP001", "John Doe"))
                .thenReturn(Optional.of(testCommute));

        // When & Then
        mockMvc.perform(get("/api/commute/recent")
                        .param("employeeId", "EMP001")
                        .param("employeeName", "John Doe"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.employeeId").value("EMP001"))
                .andExpect(jsonPath("$.employeeName").value("John Doe"))
                .andExpect(jsonPath("$.commuteDay").value("2024-01-15"));

        verify(commuteRepository).findTopByEmployeeIdAndEmployeeNameOrderByCommuteDayDescStartTimeDesc("EMP001", "John Doe");
    }

    @Test
    void should_return404_when_noRecentCommuteFound() throws Exception {
        // Given
        when(commuteRepository.findTopByEmployeeIdAndEmployeeNameOrderByCommuteDayDescStartTimeDesc("EMP001", "John Doe"))
                .thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/api/commute/recent")
                        .param("employeeId", "EMP001")
                        .param("employeeName", "John Doe"))
                .andExpect(status().isNotFound());

        verify(commuteRepository).findTopByEmployeeIdAndEmployeeNameOrderByCommuteDayDescStartTimeDesc("EMP001", "John Doe");
    }

    @Test
    void should_handleDifferentTimeFormats_when_clockIn() throws Exception {
        // Given
        clockInPayload.put("startTime", "08:30");

        Commute savedCommute = new Commute();
        savedCommute.setEmployeeId("EMP001");
        savedCommute.setStartTime(LocalTime.of(8, 30));

        when(commuteRepository.save(any(Commute.class))).thenReturn(savedCommute);

        // When & Then
        mockMvc.perform(post("/api/commute/clock-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clockInPayload)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.startTime").value("08:30:00"));

        verify(commuteRepository).save(any(Commute.class));
    }
}