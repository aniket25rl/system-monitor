package com.example.systemmonitor.service;

import com.example.systemmonitor.model.BandwidthUsage;
import com.example.systemmonitor.model.CpuUsage;
import com.example.systemmonitor.model.DiskUsage;
import com.example.systemmonitor.model.MemoryUsage;
import org.springframework.stereotype.Service;
import oshi.SystemInfo;
import oshi.hardware.NetworkIF;
import oshi.hardware.GlobalMemory;
import oshi.hardware.CentralProcessor;
import oshi.software.os.FileSystem;
import oshi.software.os.OSFileStore;
import oshi.software.os.OperatingSystem;

import java.util.List;

@Service
public class SystemMonitorService {

    private final SystemInfo systemInfo;
    private final CentralProcessor processor;
    private final GlobalMemory memory;
    private final OperatingSystem os;
    private long[] prevTicks;
    private long prevBytesSent = 0;
    private long prevBytesReceived = 0;
    private boolean firstRun = true;

    public SystemMonitorService() {
        this.systemInfo = new SystemInfo();
        this.processor = systemInfo.getHardware().getProcessor();
        this.memory = systemInfo.getHardware().getMemory();
        this.os = systemInfo.getOperatingSystem();
    }

    public CpuUsage getCpuUsage() {
        double cpuLoad = processor.getSystemCpuLoad(1000) * 100;
        return new CpuUsage(Math.round(cpuLoad * 10.0) / 10.0);
    }

    public MemoryUsage getMemoryUsage() {
        double memUsedPercent = ((double)(memory.getTotal() - memory.getAvailable()) / memory.getTotal()) * 100;
        memUsedPercent = Math.round(memUsedPercent * 10.0) / 10.0;
        return new MemoryUsage(memUsedPercent);
    }

    public DiskUsage getDiskUsage() {
        FileSystem fileSystem = os.getFileSystem();
        List<OSFileStore> fsArray = fileSystem.getFileStores();
        long total = 0;
        long used = 0;
        for (OSFileStore fs : fsArray) {
            total += fs.getTotalSpace();
            used += (fs.getTotalSpace() - fs.getUsableSpace());
        }
        double diskUsedPercent = ((double) used / total) * 100;
        diskUsedPercent = Math.round(diskUsedPercent * 10.0) / 10.0;
        return new DiskUsage(diskUsedPercent);
    }

    public BandwidthUsage getBandwidthUsage() {
        List<NetworkIF> networks = systemInfo.getHardware().getNetworkIFs();
        long bytesSent = 0;
        long bytesReceived = 0;
        for (NetworkIF net : networks) {
            net.updateAttributes();
            bytesSent += net.getBytesSent();
            bytesReceived += net.getBytesRecv();
        }

        // Calculate bandwidth since last check
        if (firstRun) {
            prevBytesSent = bytesSent;
            prevBytesReceived = bytesReceived;
            firstRun = false;
            return new BandwidthUsage(0, 0);
        }

        long deltaSent = bytesSent - prevBytesSent;
        long deltaReceived = bytesReceived - prevBytesReceived;
        prevBytesSent = bytesSent;
        prevBytesReceived = bytesReceived;

        return new BandwidthUsage(deltaSent, deltaReceived);
    }
}
