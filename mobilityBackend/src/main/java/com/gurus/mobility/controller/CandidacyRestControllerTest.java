package com.gurus.mobility.controller;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.gurus.mobility.entity.Candidacy.Candidacy;
import com.gurus.mobility.service.CandidacyServices.ICandidacyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class CandidacyRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ICandidacyService candidacyService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }
    @Test
    public void testGetAllCandidacy() throws Exception {
        List<Candidacy> mockCandidacyList = new ArrayList<>();

        Candidacy candidacy1 = new Candidacy();
        candidacy1.setIdCandidacy(1);
        candidacy1.setFirstName("Nadine");
        candidacy1.setLastName("Mili");

        Mockito.when(candidacyService.getAllCandidacy()).thenReturn(mockCandidacyList);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/candidacy/getCandidacy")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        String jsonResponse = result.getResponse().getContentAsString();
        JavaType type = objectMapper.getTypeFactory().constructCollectionType(List.class, Candidacy.class);
        List<Candidacy> responseCandidacyList = objectMapper.readValue(jsonResponse, type);
        assertEquals(mockCandidacyList, responseCandidacyList);
    }

    @Test
    public void testGetCandidacyById() throws Exception {
        Candidacy simulatedCandidacy = new Candidacy();
        simulatedCandidacy.setIdCandidacy(1);
        Mockito.when(candidacyService.getCandidacyById(Mockito.anyInt())).thenReturn(simulatedCandidacy);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/candidacy/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.idCandidacy", is(1)))
                .andExpect(jsonPath("$.cv", is(nullValue())))
                .andExpect(jsonPath("$.coverLetter", is(nullValue())))
                .andExpect(jsonPath("$.certificate", is(nullValue())))
                .andReturn();
    }

    @Test
    public void testCreateCandidacy() throws Exception {
        Candidacy testCandidacy = new Candidacy();
        testCandidacy.setFirstName("Nadine");
        testCandidacy.setLastName("Mili");
        when(candidacyService.createCandidacy(Mockito.any(Candidacy.class))).thenReturn(testCandidacy);
        String json = objectMapper.writeValueAsString(testCandidacy);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/candidacy/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.firstName", is("Nadine")))
                .andExpect(jsonPath("$.lastName", is("Mili")));
        verify(candidacyService).createCandidacy(Mockito.any(Candidacy.class));
    }
}

