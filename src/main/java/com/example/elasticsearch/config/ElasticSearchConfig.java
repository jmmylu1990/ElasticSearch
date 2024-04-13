package com.example.elasticsearch.config;

import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories("com.example.elasticsearch.repository")
@ComponentScan(basePackages = {"com.example.elasticsearch"})
public class ElasticSearchConfig extends ElasticsearchConfiguration {

    @Value("${elasticsearch.url}")
    private String elasticsearchUrl;

    @Override
    public ClientConfiguration clientConfiguration() {
        final ClientConfiguration config = ClientConfiguration.builder().connectedTo(elasticsearchUrl).build();
        return config;
    }
}
