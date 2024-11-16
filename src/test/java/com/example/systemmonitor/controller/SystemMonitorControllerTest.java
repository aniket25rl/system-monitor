package com.example.systemmonitor.controller;

import com.example.systemmonitor.model.BandwidthUsage;
import com.example.systemmonitor.model.CpuUsage;
import com.example.systemmonitor.model.DiskUsage;
import com.example.systemmonitor.model.MemoryUsage;
import com.example.systemmonitor.service.SystemMonitorService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SystemMonitorController.class)
class SystemMonitorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SystemMonitorService systemMonitorService;

    @Test
    void testGetCpuUsage_Success() throws Exception {
        // Mock the service response
        CpuUsage mockCpuUsage = new CpuUsage(50.0);
        when(systemMonitorService.getCpuUsage()).thenReturn(mockCpuUsage);

        // Perform GET request and validate response
        mockMvc.perform(get("/api/cpu")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.usage").value(50.0));
    }

    @Test
    void testGetCpuUsage_Error() throws Exception {
        // Simulate service throwing an exception
        when(systemMonitorService.getCpuUsage()).thenThrow(new RuntimeException("Service error"));

        // Perform GET request and validate 500 response
        mockMvc.perform(get("/api/cpu")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void testGetMemoryUsage_Success() throws Exception {
        // Mock the service response
        MemoryUsage mockMemoryUsage = new MemoryUsage(75.0);
        when(systemMonitorService.getMemoryUsage()).thenReturn(mockMemoryUsage);

        // Perform GET request and validate response
        mockMvc.perform(get("/api/memory")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.usage").value(75.0));
    }

    @Test
    void testGetDiskUsage_Success() throws Exception {
        // Mock the service response
        DiskUsage mockDiskUsage = new DiskUsage(60.0);
        when(systemMonitorService.getDiskUsage()).thenReturn(mockDiskUsage);

        // Perform GET request and validate response
        mockMvc.perform(get("/api/disk")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.usage").value(60.0));
    }

    @Test
    void testGetBandwidthUsage_Success() throws Exception {
        // Mock the service response
        BandwidthUsage mockBandwidthUsage = new BandwidthUsage(1024L, 2048L);
        when(systemMonitorService.getBandwidthUsage()).thenReturn(mockBandwidthUsage);

        // Perform GET request and validate response
        mockMvc.perform(get("/api/bandwidth")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bytesSent").value(1024))
                .andExpect(jsonPath("$.bytesReceived").value(2048));
    }

}
