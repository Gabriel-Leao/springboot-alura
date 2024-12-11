package br.com.alura.screenmatch;

import br.com.alura.screenmatch.Main.Main;
import br.com.alura.screenmatch.model.Episode;
import br.com.alura.screenmatch.model.SerieData;
import br.com.alura.screenmatch.service.DataConverter;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;


@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Scanner scanner = new Scanner(System.in);
		Main main = new Main();
		String data = main.searchProduction();

		DataConverter converter = new DataConverter();
		var serie = converter.getData(data, SerieData.class);
//		System.out.println(serie);
		var seasons = main.getSeasons(serie);
		var episodes = main.convertSeasonsToEpisodes(seasons);

		main.printSeasonEpisodes(seasons);

//		System.out.print("Digite o ano para filtrar os episódios: ");
//		int year = scanner.nextInt();
//		scanner.nextLine();
//
//		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//
//		List<Episode> filteredEpisodes = main.filterByYear(year, episodes);
//		filteredEpisodes.forEach(episode -> System.out.println(
//				"Temporada: " + episode.getSeason() +
//						", Episódio: " + episode.getTitle() +
//						", Data de lançamento: " + episode.getReleaseDate().format(dateFormatter)));

		System.out.print("Digite o nome do episódio que deseja buscar: ");
		String episodeName = scanner.nextLine();
		Optional<Episode> searchedEpisode = main.searchEpisode(episodes, episodeName);
		searchedEpisode.ifPresentOrElse(
				episode -> System.out.println("Episódio encontrado: " + episode),
				() -> System.out.println("Episódio não encontrado"));

//		Map<Integer, Double> ratingForSeason = main.ratingForSeason(episodes);
//
//		System.out.println(ratingForSeason);

//		DoubleSummaryStatistics statistics = episodes.stream()
//				.filter(episode -> episode.getRating() != null)
//						.collect(Collectors.summarizingDouble(Episode::getRating));
//
//		System.out.println("A média das notas dá serie é: " + statistics.getAverage());
//		System.out.println("O melhor episódio teve a nota: " + statistics.getMax());
//		System.out.println("O pior episódio teve a nota:  " + statistics.getMin());
		scanner.close();
	}
 }
