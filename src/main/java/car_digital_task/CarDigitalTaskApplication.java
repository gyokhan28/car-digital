package car_digital_task;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(
		info = @Info(
				title = "Car Digital Project",
				description = "Backend API"
		))
@SpringBootApplication
public class CarDigitalTaskApplication {

	public static void main(String[] args) {
		SpringApplication.run(CarDigitalTaskApplication.class, args);
	}

}
