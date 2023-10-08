package com.dsk.kongapiclientsdk;

import com.dsk.kongapiclientsdk.client.KongApiClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "kongapi")
@ComponentScan
@Data
public class KongApiClientConfig {

    private String accessKey;

    private String secretKey;

    @Bean
    public KongApiClient getKongApiClient(){
        return new KongApiClient(accessKey,secretKey);
    }

}
