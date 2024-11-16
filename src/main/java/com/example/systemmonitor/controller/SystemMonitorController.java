package com.example.systemmonitor.controller;

import com.example.systemmonitor.model.BandwidthUsage;
import com.example.systemmonitor.model.CpuUsage;
import com.example.systemmonitor.model.DiskUsage;
import com.example.systemmonitor.model.MemoryUsage;
import com.example.systemmonitor.service.SystemMonitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api")
public class SystemMonitorController {

    private static final Logger logger = LoggerFactory.getLogger(SystemMonitorController.class);

    @Autowired
    private SystemMonitorService systemMonitorService;

    @GetMapping("/cpu")
    public ResponseEntity<CpuUsage> getCpuUsage() {
        try {
            CpuUsage cpuUsage = systemMonitorService.getCpuUsage();
            logger.info("CPU usage requested: {}%", cpuUsage.getUsage());
            return ResponseEntity.ok(cpuUsage);
        } catch (Exception e) {
            logger.error("Error fetching CPU usage", e);
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/memory")
    public ResponseEntity<MemoryUsage> getMemoryUsage() {
        try {
            MemoryUsage memoryUsage = systemMonitorService.getMemoryUsage();
            logger.info("Memory usage requested: {}%", memoryUsage.getUsage());
            return ResponseEntity.ok(memoryUsage);
        } catch (Exception e) {
            logger.error("Error fetching Memory usage", e);
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/disk")
    public ResponseEntity<DiskUsage> getDiskUsage() {
        try {
            DiskUsage diskUsage = systemMonitorService.getDiskUsage();
            logger.info("Disk usage requested: {}%", diskUsage.getUsage());
            return ResponseEntity.ok(diskUsage);
        } catch (Exception e) {
            logger.error("Error fetching Disk usage", e);
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/bandwidth")
    public ResponseEntity<BandwidthUsage> getBandwidthUsage() {
        try {
            BandwidthUsage bandwidthUsage = systemMonitorService.getBandwidthUsage();
            logger.info("Bandwidth usage requested: Sent={} bytes, Received={} bytes",
                    bandwidthUsage.getBytesSent(), bandwidthUsage.getBytesReceived());
            return ResponseEntity.ok(bandwidthUsage);
        } catch (Exception e) {
            logger.error("Error fetching Bandwidth usage", e);
            return ResponseEntity.status(500).build();
        }
    }

    // Handle 404
    @ExceptionHandler({org.springframework.web.servlet.NoHandlerFoundException.class})
    public ResponseEntity<String> handleNotFound(Exception ex) {
        logger.warn("404 error: {}", ex.getMessage());
        return ResponseEntity.status(404).body("{\"error\": \"Endpoint not found\"}");
    }

    // Handle generic exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleInternalError(Exception ex) {
        logger.error("500 error: {}", ex.getMessage());
        return ResponseEntity.status(500).body("{\"error\": \"Internal server error\"}");
    }
}
