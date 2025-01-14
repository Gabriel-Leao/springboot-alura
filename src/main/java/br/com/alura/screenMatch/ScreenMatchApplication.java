package br.com.alura.screenMatch;

import br.com.alura.screenMatch.main.Main;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class ScreenMatchApplication implements CommandLineRunner {
	private final Main main;

	@Autowired
	public ScreenMatchApplication(Main main) {
		this.main = main;
	}

	public static void main(String[] args) {
		SpringApplication.run(ScreenMatchApplication.class, args);
	}

	@Override
	public void run(String... args) {
		main.applicationMenu();
	}
 }
