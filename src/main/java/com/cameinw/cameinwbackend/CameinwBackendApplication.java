package com.cameinw.cameinwbackend;

import com.cameinw.cameinwbackend.image.utils.ImageHandler;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class CameinwBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(CameinwBackendApplication.class, args);
	}

	@PostConstruct
	public void init() {
		ImageHandler.setMainPath("/main_image_storage/");
	}

}
