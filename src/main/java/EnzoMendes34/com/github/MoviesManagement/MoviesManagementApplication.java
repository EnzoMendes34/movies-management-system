package EnzoMendes34.com.github.MoviesManagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MoviesManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(MoviesManagementApplication.class, args);
	}

}
