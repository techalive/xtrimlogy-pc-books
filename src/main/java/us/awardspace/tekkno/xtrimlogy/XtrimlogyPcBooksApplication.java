package us.awardspace.tekkno.xtrimlogy;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class XtrimlogyPcBooksApplication {

	public static void main(String[] args) {
		SpringApplication.run(XtrimlogyPcBooksApplication.class, args);
	}

}
