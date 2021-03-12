package us.awardspace.tekkno.xtrimlogy;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;
import us.awardspace.tekkno.xtrimlogy.order.application.OrdersProperties;

@EnableScheduling
@EnableJpaAuditing
@SpringBootApplication
@EnableConfigurationProperties(OrdersProperties.class)
public class XtrimlogyPcBooksApplication {

	public static void main(String[] args) {
		SpringApplication.run(XtrimlogyPcBooksApplication.class, args);
	}

	@Bean
	RestTemplate restTemplate() { return new RestTemplateBuilder().build();	}
}
