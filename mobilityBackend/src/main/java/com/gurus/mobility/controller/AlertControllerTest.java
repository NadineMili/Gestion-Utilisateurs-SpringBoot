package com.gurus.mobility.controller;
import com.gurus.mobility.controller.AlertController;
import com.gurus.mobility.entity.alert.Alert;
import com.gurus.mobility.entity.user.User;
import com.gurus.mobility.service.AlertServices.IAlertService;
import com.gurus.mobility.repository.User.UserRepository;
import com.gurus.mobility.security.jwt.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class AlertControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private AlertController alertController;

    @Mock
    private IAlertService alertService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private HttpServletRequest request;

    @Mock
    private JwtUtils jwtUtils;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(alertController).build();
    }

    @Test
    public void testAjouterAlert() throws Exception {
        Alert alert = new Alert();
        alert.setIdAlrt(1L);
        User user = new User();
        user.setId(1L);

        when(request.getHeader("Authorization")).thenReturn("Bearer jwt_token");
        when(jwtUtils.getUserNameFromJwtToken("jwt_token")).thenReturn("username");
        when(userRepository.findByUserName("username")).thenReturn(Optional.of(user));
        Mockito.doNothing().when(alertService).createAlert(alert, user.getId());

        mockMvc.perform(post("/api/alert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk());
    }


    @Test
    public void testGetUserAlerts() throws Exception {
        User user = new User();
        user.setId(1L);
        List<Alert> alerts = new ArrayList<>();
        when(request.getHeader("Authorization")).thenReturn("Bearer jwt_token");
        when(jwtUtils.getUserNameFromJwtToken("jwt_token")).thenReturn("username");
        when(userRepository.findByUserName("username")).thenReturn(Optional.of(user));
        when(alertService.getAllAlerts()).thenReturn(alerts);

        mockMvc.perform(get("/api/alert/alerts"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetAllAlerts() throws Exception {
        User user = new User();
        user.setId(1L);
        List<Alert> alerts = new ArrayList<>();
        when(request.getHeader("Authorization")).thenReturn("Bearer jwt_token");
        when(jwtUtils.getUserNameFromJwtToken("jwt_token")).thenReturn("username");
        when(userRepository.findByUserName("username")).thenReturn(Optional.of(user));
        when(alertService.getAlertsByUser(user.getId())).thenReturn(alerts);

        mockMvc.perform(get("/api/alert/myalerts"))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteAlert() throws Exception {
        mockMvc.perform(delete("/api/alert/1/1"))
                .andExpect(status().isOk());
    }
}
