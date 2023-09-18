package com.cameinw.cameinwbackend;

import com.cameinw.cameinwbackend.image.utils.ImageHandler;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableJpaAuditing
@EnableTransactionManagement
public class CameinwBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(CameinwBackendApplication.class, args);
	}

	@PostConstruct
	public void init() {

		String baseDirectory = System.getProperty("user.dir"); // current working directory
		String imageDirectory = baseDirectory + "/image_storage/"; // subdirectory for storing images
		ImageHandler.setMainPath(imageDirectory); // main path to the image directory

//		ImageHandler.setMainPath("./cameinw_image_storage/");
	}

}
