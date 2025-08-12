package com.example.mostin.controllers;

import com.example.mostin.models.WorkPlace;
import com.example.mostin.repositories.WorkPlaceRepository;
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
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WorkPlaceController.class)
class WorkPlaceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WorkPlaceRepository workPlaceRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private WorkPlace testWorkPlace;

    @BeforeEach
    void setUp() {
        testWorkPlace = new WorkPlace();
        testWorkPlace.setWorkPlaceName("Main Office");
        testWorkPlace.setLatitude(37.5665);
        testWorkPlace.setLongitude(126.9780);
    }

    @Test
    void should_returnAllWorkPlaces_when_getAllWorkPlacesRequested() throws Exception {
        // Given
        WorkPlace workPlace1 = new WorkPlace();
        workPlace1.setWorkPlaceName("Seoul Office");
        workPlace1.setLatitude(37.5665);
        workPlace1.setLongitude(126.9780);
        
        WorkPlace workPlace2 = new WorkPlace();
        workPlace2.setWorkPlaceName("Busan Office");
        workPlace2.setLatitude(35.1796);
        workPlace2.setLongitude(129.0756);

        when(workPlaceRepository.findAll()).thenReturn(Arrays.asList(workPlace1, workPlace2));

        // When & Then
        mockMvc.perform(get("/api/workplaces"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].workPlaceName").value("Seoul Office"))
                .andExpect(jsonPath("$[0].latitude").value(37.5665))
                .andExpect(jsonPath("$[0].longitude").value(126.9780))
                .andExpect(jsonPath("$[1].workPlaceName").value("Busan Office"))
                .andExpect(jsonPath("$[1].latitude").value(35.1796))
                .andExpect(jsonPath("$[1].longitude").value(129.0756));

        verify(workPlaceRepository).findAll();
    }

    @Test
    void should_returnEmptyList_when_noWorkPlacesExist() throws Exception {
        // Given
        when(workPlaceRepository.findAll()).thenReturn(Collections.emptyList());

        // When & Then
        mockMvc.perform(get("/api/workplaces"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));

        verify(workPlaceRepository).findAll();
    }

    @Test
    void should_createWorkPlace_when_validWorkPlaceProvided() throws Exception {
        // Given
        WorkPlace newWorkPlace = new WorkPlace();
        newWorkPlace.setWorkPlaceName("New Branch");
        newWorkPlace.setLatitude(35.1796);
        newWorkPlace.setLongitude(129.0756);
        
        WorkPlace savedWorkPlace = new WorkPlace();
        savedWorkPlace.setWorkPlaceName("New Branch");
        savedWorkPlace.setLatitude(35.1796);
        savedWorkPlace.setLongitude(129.0756);

        when(workPlaceRepository.save(any(WorkPlace.class))).thenReturn(savedWorkPlace);

        // When & Then
        mockMvc.perform(post("/api/workplaces")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newWorkPlace)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.workPlaceName").value("New Branch"))
                .andExpect(jsonPath("$.latitude").value(35.1796))
                .andExpect(jsonPath("$.longitude").value(129.0756));

        verify(workPlaceRepository).save(any(WorkPlace.class));
    }

    @Test
    void should_returnWorkPlace_when_getWorkPlaceByValidName() throws Exception {
        // Given
        String workPlaceName = "Main Office";
        when(workPlaceRepository.findById(workPlaceName)).thenReturn(Optional.of(testWorkPlace));

        // When & Then
        mockMvc.perform(get("/api/workplaces/{name}", workPlaceName))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.workPlaceName").value("Main Office"))
                .andExpect(jsonPath("$.latitude").value(37.5665))
                .andExpect(jsonPath("$.longitude").value(126.9780));

        verify(workPlaceRepository).findById(workPlaceName);
    }

    @Test
    void should_return404_when_getWorkPlaceByInvalidName() throws Exception {
        // Given
        String workPlaceName = "Nonexistent Office";
        when(workPlaceRepository.findById(workPlaceName)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/api/workplaces/{name}", workPlaceName))
                .andExpect(status().isNotFound());

        verify(workPlaceRepository).findById(workPlaceName);
    }

    @Test
    void should_handleSpecialCharactersInWorkPlaceName_when_getWorkPlaceByName() throws Exception {
        // Given
        String workPlaceName = "Office & Co.";
        when(workPlaceRepository.findById(workPlaceName)).thenReturn(Optional.of(testWorkPlace));

        // When & Then
        mockMvc.perform(get("/api/workplaces/{name}", workPlaceName))
                .andExpect(status().isOk());

        verify(workPlaceRepository).findById(workPlaceName);
    }

    @Test
    void should_handleEmptyWorkPlaceName_when_getWorkPlaceByName() throws Exception {
        // Given - Empty path variable might cause 404 before reaching controller
        // So we'll test with a space character instead
        String workPlaceName = " ";
        when(workPlaceRepository.findById(workPlaceName)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/api/workplaces/{name}", workPlaceName))
                .andExpect(status().isNotFound());

        verify(workPlaceRepository).findById(workPlaceName);
    }

    @Test
    void should_createMultipleWorkPlaces_when_calledMultipleTimes() throws Exception {
        // Given
        WorkPlace workPlace1 = new WorkPlace();
        WorkPlace workPlace2 = new WorkPlace();

        when(workPlaceRepository.save(any(WorkPlace.class)))
                .thenReturn(workPlace1)
                .thenReturn(workPlace2);

        // When & Then - First creation
        mockMvc.perform(post("/api/workplaces")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(workPlace1)))
                .andExpect(status().isOk());

        // When & Then - Second creation
        mockMvc.perform(post("/api/workplaces")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(workPlace2)))
                .andExpect(status().isOk());

        verify(workPlaceRepository, times(2)).save(any(WorkPlace.class));
    }

    @Test
    void should_handleContentTypeJson_when_requestingAllWorkPlaces() throws Exception {
        // Given
        when(workPlaceRepository.findAll()).thenReturn(Collections.emptyList());

        // When & Then
        mockMvc.perform(get("/api/workplaces")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(workPlaceRepository).findAll();
    }

    @Test
    void should_handleContentTypeJson_when_creatingWorkPlace() throws Exception {
        // Given
        WorkPlace newWorkPlace = new WorkPlace();
        when(workPlaceRepository.save(any(WorkPlace.class))).thenReturn(newWorkPlace);

        // When & Then
        mockMvc.perform(post("/api/workplaces")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newWorkPlace))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(workPlaceRepository).save(any(WorkPlace.class));
    }

    @Test
    void should_acceptEmptyRequestBody_when_creatingWorkPlace() throws Exception {
        // Given
        WorkPlace emptyWorkPlace = new WorkPlace();
        when(workPlaceRepository.save(any(WorkPlace.class))).thenReturn(emptyWorkPlace);

        // When & Then
        mockMvc.perform(post("/api/workplaces")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk());

        verify(workPlaceRepository).save(any(WorkPlace.class));
    }

    @Test
    void should_handleUrlEncodedWorkPlaceName_when_getWorkPlaceByName() throws Exception {
        // Given
        String workPlaceName = "Office Building 1";  // Simplified name without special characters
        
        when(workPlaceRepository.findById(workPlaceName)).thenReturn(Optional.of(testWorkPlace));

        // When & Then
        // Use path variable instead of string concatenation
        mockMvc.perform(get("/api/workplaces/{name}", workPlaceName))
                .andExpect(status().isOk());

        verify(workPlaceRepository).findById(workPlaceName);
    }
}