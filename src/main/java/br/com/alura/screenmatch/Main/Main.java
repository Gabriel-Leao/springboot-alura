package br.com.alura.screenmatch.Main;

import br.com.alura.screenmatch.Utils.EnvUtil;
import br.com.alura.screenmatch.model.Episode;
import br.com.alura.screenmatch.model.EpisodeData;
import br.com.alura.screenmatch.model.SeasonData;
import br.com.alura.screenmatch.model.SerieData;
import br.com.alura.screenmatch.service.ApiConsumer;
import br.com.alura.screenmatch.service.DataConverter;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    private final Scanner scanner = new Scanner(System.in);
    private final ApiConsumer api = new ApiConsumer();
    private final String url = "https://www.omdbapi.com/?t=";
    private final String apiKey = "&apikey=" + EnvUtil.getEnv("API_KEY");
    private String productionName;
    private final DataConverter converter = new DataConverter();

    public String searchProduction() {
        System.out.print("Digite o nome da série ou filme: ");
        productionName = scanner.nextLine();
        return api.getData(url + productionName.replace(" ", "+") + apiKey);
    }

    public List<SeasonData> getSeasons(@NotNull SerieData serie) {
        List<SeasonData> seasons = new ArrayList<>();
		for (int i = 1; i <= serie.totalSeasons(); i++) {
			String data = api.getData(url + productionName.replace(" ", "+") + "&Season=" + i + apiKey);
			seasons.add(converter.getData(data, SeasonData.class));
		}

        return seasons;
    }

    public void printSeasonEpisodes(@NotNull List<SeasonData> seasons) {
        List<EpisodeData> episodesData = seasons.stream()
                .flatMap(season -> season.episodes().stream())
                .toList();

        System.out.println("Episódios os episódios mais bem avaliados:");
        episodesData.stream()
                .filter(episode -> !episode.rating().equals("N/A"))
                .sorted(Comparator.comparing(EpisodeData::rating).reversed())
                .limit(10)
                .forEach(System.out::println);
    }

    public List<Episode> filterByYear(int year, List<Episode> episodes) {
        LocalDate startDate = LocalDate.of(year, 1, 1);

        return episodes.stream()
                .filter(episode -> episode.getReleaseDate() != null && episode.getReleaseDate().isAfter(startDate)).toList();
    }

    public List<Episode> convertSeasonsToEpisodes(List<SeasonData> seasons) {
        return seasons.stream()
                .flatMap(season -> season.episodes().stream()
                        .map(episode -> new Episode(season.seasonNumber(), episode)))
                .collect(Collectors.toList());
    }

    public Optional<Episode> searchEpisode (List<Episode> episodeList, String episodeName) {
        return episodeList.stream()
                .filter(e -> e.getTitle().toUpperCase().contains(episodeName.toUpperCase()))
                .findFirst();
    }

    public Map<Integer, Double> ratingForSeason(List<Episode> episodes) {
        return episodes.stream()
                .filter(episode -> episode.getRating() != null)
                .collect(Collectors.groupingBy(Episode::getSeason,
                        Collectors.averagingDouble(Episode::getRating)));
    }
}

