package com.craft;

import com.craft.controller.request.AdminLoginRequest;
import com.craft.controller.response.JwtResponse;
import com.craft.service.AdminServiceImp;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdminServiceImp adminService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testAdminLogin() throws Exception {
        AdminLoginRequest request = new AdminLoginRequest();
        request.setEmail("admin");
        request.setPassword("password");

        JwtResponse jwtResponse = new JwtResponse("success", true, "mock-jwt-token");

        when(adminService.adminLogin(request)).thenReturn(ResponseEntity.ok(jwtResponse));

        mockMvc.perform(post("/admin/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mock-jwt-token"))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("success"));
    }
}
