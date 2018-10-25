package com.megadotnet.distributedlock.config;

/**
 * Created by dev on 2018/7/9.
 */
import java.util.List;
import java.util.Map;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(
        prefix = "hazelcast"
)
public class HazelcastProperties {
    private boolean cluster = true;
    private boolean enabled = true;
    private Integer poolSize = Integer.valueOf(10);
    private Integer timeout = Integer.valueOf(1000);
    private Integer connectionAttemptPeriod = Integer.valueOf(1440);
    private Integer connectionAttemptLimits = Integer.valueOf(86400);
    private String hazelcastType = "client";
    private List<String> servers;
    private Map<String, String> properties;

    public HazelcastProperties() {
    }

    public boolean isCluster() {
        return this.cluster;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public Integer getPoolSize() {
        return this.poolSize;
    }

    public Integer getTimeout() {
        return this.timeout;
    }

    public Integer getConnectionAttemptPeriod() {
        return this.connectionAttemptPeriod;
    }

    public Integer getConnectionAttemptLimits() {
        return this.connectionAttemptLimits;
    }

    public String getHazelcastType() {
        return this.hazelcastType;
    }

    public List<String> getServers() {
        return this.servers;
    }

    public Map<String, String> getProperties() {
        return this.properties;
    }

    public void setCluster(boolean cluster) {
        this.cluster = cluster;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setPoolSize(Integer poolSize) {
        this.poolSize = poolSize;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    public void setConnectionAttemptPeriod(Integer connectionAttemptPeriod) {
        this.connectionAttemptPeriod = connectionAttemptPeriod;
    }

    public void setConnectionAttemptLimits(Integer connectionAttemptLimits) {
        this.connectionAttemptLimits = connectionAttemptLimits;
    }

    public void setHazelcastType(String hazelcastType) {
        this.hazelcastType = hazelcastType;
    }

    public void setServers(List<String> servers) {
        this.servers = servers;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }
}