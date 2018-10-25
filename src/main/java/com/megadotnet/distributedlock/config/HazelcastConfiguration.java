package com.megadotnet.distributedlock.config;

/**
 * Created by dev on 2018/7/9.
 */
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.spring.cache.HazelcastCacheManager;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import javax.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
@ConditionalOnProperty(
        prefix = "hazelcast",
        name = {"enabled"},
        havingValue = "true"
)
@EnableConfigurationProperties({HazelcastProperties.class})
public class HazelcastConfiguration {
    private HazelcastInstance hazelcastInstance;
    private CacheManager cacheManager;
    @Autowired
    private HazelcastProperties hazelcastProperties;

    public HazelcastConfiguration() {
    }

    @PreDestroy
    public void destroy() {
        Hazelcast.shutdownAll();
    }

    @Bean
    public CacheManager cacheManager() {
        return new HazelcastCacheManager(this.hazelcastInstance());
    }

    @Bean(
            name = {"keyGenerator"}
    )
    public KeyGenerator keyGenerator() {
        return (target, method, params) -> {
            StringBuilder sb = new StringBuilder();
            sb.append(method.getName());
            sb.append(":");
            if(null != params) {
                Object[] var4 = params;
                int var5 = params.length;

                for(int var6 = 0; var6 < var5; ++var6) {
                    Object obj = var4[var6];
                    sb.append(obj.toString());
                }
            }

            return sb.toString();
        };
    }

    @Bean
    HazelcastInstance hazelcastInstance() {
        List<String> servers = this.hazelcastProperties.getServers();
        if(null != servers && 0 != servers.size()) {
            ClientConfig config = new ClientConfig();
            Iterator var3 = servers.iterator();

            while(true) {
                do {
                    if(!var3.hasNext()) {
                        this.hazelcastInstance = HazelcastClient.newHazelcastClient(config);
                        return this.hazelcastInstance;
                    }

                    String server = (String)var3.next();
                    config.getNetworkConfig().addAddress(new String[]{server});
                    config.getNetworkConfig().setConnectionAttemptLimit(this.hazelcastProperties.getConnectionAttemptLimits().intValue());
                    config.getNetworkConfig().setConnectionTimeout(this.hazelcastProperties.getTimeout().intValue());
                    config.getNetworkConfig().setConnectionAttemptPeriod(this.hazelcastProperties.getConnectionAttemptPeriod().intValue());
                    config.setExecutorPoolSize(this.hazelcastProperties.getPoolSize().intValue());
                    config.setProperty("hazelcast.socket.keep.alive", "true");
                } while(null == this.hazelcastProperties.getProperties());

                Iterator var5 = this.hazelcastProperties.getProperties().entrySet().iterator();

                while(var5.hasNext()) {
                    Entry<String, String> entry = (Entry)var5.next();
                    config.setProperty((String)entry.getKey(), (String)entry.getValue());
                }
            }
        } else {
            this.hazelcastInstance = Hazelcast.newHazelcastInstance();
            return this.hazelcastInstance;
        }
    }
}