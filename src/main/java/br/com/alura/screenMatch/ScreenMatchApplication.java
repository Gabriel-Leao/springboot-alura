package br.com.alura.screenMatch;

import br.com.alura.screenMatch.main.Main;
import br.com.alura.screenMatch.utils.EnvUtil;
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
		System.setProperty("DB_HOST", EnvUtil.getEnv("DB_HOST"));
		System.setProperty("DB_NAME", EnvUtil.getEnv("DB_NAME"));
		System.setProperty("DB_USER", EnvUtil.getEnv("DB_USER"));
		System.setProperty("DB_PASSWORD", EnvUtil.getEnv("DB_PASSWORD"));

		SpringApplication.run(ScreenMatchApplication.class, args);
	}

	@Override
	public void run(String... args) {
		main.applicationMenu();
	}
 }
