package us.awardspace.tekkno.xtrimlogy;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class XtrimlogyPcBooksApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(XtrimlogyPcBooksApplication.class, args);
	}

	private final CatalogService catalogService;

	public XtrimlogyPcBooksApplication(CatalogService catalogService) {
		this.catalogService = catalogService;
	}

	@Override
	public void run(String... args) throws Exception {
	//	CatalogService service = new CatalogService();
		List<Book> books = catalogService.findByTitle("Kompedium");
		books.forEach(System.out::println);

	}
}
