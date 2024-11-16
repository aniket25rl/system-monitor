package com.example.systemmonitor.service;

import com.example.systemmonitor.model.BandwidthUsage;
import com.example.systemmonitor.model.CpuUsage;
import com.example.systemmonitor.model.DiskUsage;
import com.example.systemmonitor.model.MemoryUsage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import oshi.SystemInfo;
import oshi.hardware.*;
import oshi.software.os.FileSystem;
import oshi.software.os.OSFileStore;
import oshi.software.os.OperatingSystem;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SystemMonitorServiceTest {

    @Mock
    private SystemInfo mockSystemInfo;

    @Mock
    private CentralProcessor mockProcessor;

    @Mock
    private GlobalMemory mockMemory;

    @Mock
    private OperatingSystem mockOperatingSystem;

    @Mock
    private FileSystem mockFileSystem;

    @Mock
    private NetworkIF mockNetworkIF;

    private SystemMonitorService systemMonitorService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        when(mockSystemInfo.getHardware()).thenReturn(mock(HardwareAbstractionLayer.class));
        when(mockSystemInfo.getHardware().getProcessor()).thenReturn(mockProcessor);
        when(mockSystemInfo.getHardware().getMemory()).thenReturn(mockMemory);
        when(mockSystemInfo.getOperatingSystem()).thenReturn(mockOperatingSystem);
        when(mockOperatingSystem.getFileSystem()).thenReturn(mockFileSystem);

        systemMonitorService = new SystemMonitorService(mockSystemInfo);
    }

    @Test
    void testGetCpuUsage() {
        when(mockProcessor.getSystemCpuLoad(1000)).thenReturn(0.65); // 65% CPU load
        CpuUsage cpuUsage = systemMonitorService.getCpuUsage();
        assertNotNull(cpuUsage);
        assertEquals(65.0, cpuUsage.getUsage());
    }

    @Test
    void testGetMemoryUsage() {
        when(mockMemory.getTotal()).thenReturn(16000000000L); // 16 GB
        when(mockMemory.getAvailable()).thenReturn(8000000000L); // 8 GB available

        MemoryUsage memoryUsage = systemMonitorService.getMemoryUsage();
        assertNotNull(memoryUsage);
        assertEquals(50.0, memoryUsage.getUsage());
    }

    @Test
    void testGetDiskUsage() {
        List<OSFileStore> mockFileStores = new ArrayList<>();
        OSFileStore mockFileStore = mock(OSFileStore.class);
        when(mockFileStore.getTotalSpace()).thenReturn(1000000000L); // 1 GB
        when(mockFileStore.getUsableSpace()).thenReturn(500000000L); // 0.5 GB usable
        mockFileStores.add(mockFileStore);

        when(mockFileSystem.getFileStores()).thenReturn(mockFileStores);

        DiskUsage diskUsage = systemMonitorService.getDiskUsage();
        assertNotNull(diskUsage);
        assertEquals(50.0, diskUsage.getUsage());
    }

    @Test
    void testGetBandwidthUsage() {
        List<NetworkIF> mockNetworkIFs = new ArrayList<>();
        when(mockNetworkIF.getBytesSent()).thenReturn(1000L);
        when(mockNetworkIF.getBytesRecv()).thenReturn(2000L);
        mockNetworkIFs.add(mockNetworkIF);

        when(mockSystemInfo.getHardware().getNetworkIFs()).thenReturn(mockNetworkIFs);

        BandwidthUsage initialUsage = systemMonitorService.getBandwidthUsage();
        assertNotNull(initialUsage);
        assertEquals(0, initialUsage.getBytesSent());
        assertEquals(0, initialUsage.getBytesReceived());

        // Simulate bandwidth increase
        when(mockNetworkIF.getBytesSent()).thenReturn(2000L);
        when(mockNetworkIF.getBytesRecv()).thenReturn(4000L);

        BandwidthUsage updatedUsage = systemMonitorService.getBandwidthUsage();
        assertNotNull(updatedUsage);
        assertEquals(1000, updatedUsage.getBytesSent());
        assertEquals(2000, updatedUsage.getBytesReceived());
    }
}
