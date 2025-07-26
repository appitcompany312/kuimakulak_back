package org.appitcompany.kuimakulak.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.context.annotation.Bean;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.stereotype.Component;

@Component
@EnableElasticsearchRepositories
public class ElasticsearchConfig {


    @Bean
    public RestClient restClient() {
        return RestClient.builder(
                new HttpHost("localhost", 9200, "http")

        ).build();
    }

    @Bean

    public ElasticsearchTransport elasticsearchTransport(RestClient restClient) {
        return new RestClientTransport(restClient, new JacksonJsonpMapper());
    }

    @Bean
    public ElasticsearchClient elasticsearchClient(ElasticsearchTransport elasticsearchTransport) {
        return new ElasticsearchClient(elasticsearchTransport);
    }

    @Bean
    public ElasticsearchOperations elasticsearchTemplate(ElasticsearchClient elasticsearchClient) {
        return new ElasticsearchTemplate(elasticsearchClient);
    }
}