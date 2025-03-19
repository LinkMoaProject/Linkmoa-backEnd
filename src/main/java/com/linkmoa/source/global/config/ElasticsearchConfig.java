package com.linkmoa.source.global.config;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;

@Configuration
public class ElasticsearchConfig {

	@Value("${spring.data.elasticsearch.host}")
	private String host;
	@Value("${spring.data.elasticsearch.port}")
	private int port;
	@Value("${spring.data.elasticsearch.encodedApiKey}")
	private String encodedApiKey;

	@Value("${spring.data.elasticsearch.user-name}")
	private String userName;
	@Value("${spring.data.elasticsearch.password}")
	private String password;

	@Bean
	public ElasticsearchClient elasticsearchClient() {
		try {
			RestClient restClient = RestClient
				.builder(new HttpHost(host, port, "http"))
				.setDefaultHeaders(new Header[] {
					new BasicHeader("Authorization", "ApiKey " + encodedApiKey)  // API 키만 사용
				})
				.build();

			ElasticsearchTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());

			return new ElasticsearchClient(transport);

		} catch (Exception e) {
			throw new RuntimeException("Failed to create Elasticsearch client", e);
		}
	}
    /*@Bean
    public ElasticsearchClient elasticsearchClient() {

        BasicCredentialsProvider credsProv = new BasicCredentialsProvider();
        credsProv.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(userName, password));


        try {
            // Create the low-level client
            RestClient restClient = RestClient
                    .builder(new HttpHost(host, port, "http"))
                    .setDefaultHeaders(new Header[]{
                            new BasicHeader("Authorization", "ApiKey " + encodedApiKey)
                    })
                    .setHttpClientConfigCallback(httpClientBuilder -> httpClientBuilder
                            .setDefaultCredentialsProvider(credsProv)
                    )
                    .build();


            // Create the transport with a Jackson mapper
            ElasticsearchTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());

            // Create and return the Elasticsearch client
            return new ElasticsearchClient(transport);

        } catch (Exception e) {
            throw new RuntimeException("Failed to create Elasticsearch client", e);
        }
    }*/

}
