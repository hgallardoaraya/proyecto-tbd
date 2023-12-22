package cl.vol.app_voluntario;

import cl.vol.app_voluntario.repository.EmergenciaRepository;
import cl.vol.app_voluntario.repository.EmergenciaRepositoryImp;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AppVoluntarioApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(AppVoluntarioApplication.class, args);
	}

}
