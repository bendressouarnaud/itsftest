package ankk.gca.firsttest;

import ankk.gca.firsttest.services.Sales;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FirsttestApplication {

	public static void main(String[] args) {
		SpringApplication.run(FirsttestApplication.class, args);

		Sales sales = new Sales();
		sales.process();
	}
}
