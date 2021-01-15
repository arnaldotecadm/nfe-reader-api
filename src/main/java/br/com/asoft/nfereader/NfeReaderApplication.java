package br.com.asoft.nfereader;

import javax.annotation.Resource;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.asoft.nfereader.service.FilesStorageService;

@SpringBootApplication
@RestController
public class NfeReaderApplication implements CommandLineRunner {

	@Resource
	FilesStorageService storageService;

	public static void main(String[] args) {
		SpringApplication.run(NfeReaderApplication.class, args);
	}

	@Override
	public void run(String... arg) {
		storageService.deleteAll();
		storageService.init();
	}

	@GetMapping({ "", "ping" })
	public String ping() {
		return "ok";
	}

}
