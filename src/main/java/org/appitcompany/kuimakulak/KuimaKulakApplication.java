package org.appitcompany.kuimakulak;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "org.appitcompany.kuimakulak.jpaRepository")
@EnableElasticsearchRepositories(basePackages = "org.appitcompany.kuimakulak.elasticRepository")

public class KuimaKulakApplication {
	public static void main(String[] args) {
		SpringApplication.run(KuimaKulakApplication.class, args);
	}

}
