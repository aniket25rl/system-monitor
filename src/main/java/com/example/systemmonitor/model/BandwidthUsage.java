package com.example.systemmonitor.model;

public class BandwidthUsage {
    private long bytesSent;
    private long bytesReceived;

    public BandwidthUsage(long bytesSent, long bytesReceived) {
        this.bytesSent = bytesSent;
        this.bytesReceived = bytesReceived;
    }

    public long getBytesSent() {
        return bytesSent;
    }

    public void setBytesSent(long bytesSent) {
        this.bytesSent = bytesSent;
    }

    public long getBytesReceived() {
        return bytesReceived;
    }

    public void setBytesReceived(long bytesReceived) {
        this.bytesReceived = bytesReceived;
    }
}
