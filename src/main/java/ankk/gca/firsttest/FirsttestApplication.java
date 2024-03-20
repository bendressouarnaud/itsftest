package ankk.gca.firsttest;

import ankk.gca.firsttest.services.Sales;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class FirsttestApplication {

	public static void main(String[] args) {
		SpringApplication.run(FirsttestApplication.class, args);

		Sales sales = new Sales();
		sales.process();
	}
}
