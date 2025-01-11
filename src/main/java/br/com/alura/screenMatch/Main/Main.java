package br.com.alura.screenMatch.Main;

import br.com.alura.screenMatch.Utils.EnvUtil;
import br.com.alura.screenMatch.model.*;
import br.com.alura.screenMatch.service.ApiConsumer;
import br.com.alura.screenMatch.service.DataConverter;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    private final Scanner scanner = new Scanner(System.in);
    private final ApiConsumer api = new ApiConsumer();
    private final String url = "https://www.omdbapi.com/?t=";
    private final String apiKey = "&apikey=" + EnvUtil.getEnv("API_KEY");
    private String productionName;
    private final DataConverter converter = new DataConverter();
    ArrayList<Serie> searchedSeries = new ArrayList<>();

    public void applicationMenu () {
        int option = -1;

        while (option != 0) {
            System.out.println("Bem vindo ao ScreenMatch!");
            System.out.println("1 - Buscar série");
            System.out.println("2 - Buscar filme");
            System.out.println("3 - Listar séries buscadas");
            System.out.println("0 - Sair");
            System.out.print("Digite a opção desejada: ");
            String stringOpt = scanner.nextLine();

            try {
                option = Integer.parseInt(stringOpt);
            } catch (NumberFormatException e) {
                System.out.println("Opção inválida!");
                continue;
            }

            switch (option) {
                case 1:
                    searchSerie();
                    break;
                case 2:
                    searchMovie();
                    break;
                case 3:
                    showSearchedSeries();
                    break;
                case 0:
                    System.out.println("Até mais!");
                    break;
                default:
                    System.out.println("Opção inválida!");
                    break;
            }
        }
    }

    private String searchProduction(boolean isSeries) {
        System.out.print("Digite o nome da " + (isSeries ? "série" : "filme") + ": ");
        productionName = scanner.nextLine();
        return api.getData(url + productionName.replace(" ", "+") + apiKey);
    }

    private void searchSerie() {
        SerieData serieData = converter.getData(searchProduction(true), SerieData.class);
        Serie serie = new Serie(serieData);
        System.out.println("Série encontrada: " + serie.getTitle());
        searchedSeries.add(serie);
    }

    private void searchMovie() {
        MovieData movie = converter.getData(searchProduction(false), MovieData.class);
        System.out.println("Filme encontrado: " + movie.title() + " - (" + movie.releaseDate() + ")" + " - Diretor: " + movie.director());
    }

    private List<SeasonData> getSeasons(@NotNull SerieData serie) {
        List<SeasonData> seasons = new ArrayList<>();
		for (int i = 1; i <= serie.totalSeasons(); i++) {
			String data = api.getData(url + productionName.replace(" ", "+") + "&Season=" + i + apiKey);
			seasons.add(converter.getData(data, SeasonData.class));
		}

        return seasons;
    }

    private void printSeasonEpisodes(@NotNull List<SeasonData> seasons) {
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

    private List<Episode> filterByYear(int year, List<Episode> episodes) {
        LocalDate startDate = LocalDate.of(year, 1, 1);

        return episodes.stream()
                .filter(episode -> episode.getReleaseDate() != null && episode.getReleaseDate().isAfter(startDate)).toList();
    }

    private List<Episode> convertSeasonsToEpisodes(List<SeasonData> seasons) {
        return seasons.stream()
                .flatMap(season -> season.episodes().stream()
                        .map(episode -> new Episode(season.seasonNumber(), episode)))
                .collect(Collectors.toList());
    }

    private Optional<Episode> searchEpisode (List<Episode> episodeList, String episodeName) {
        return episodeList.stream()
                .filter(e -> e.getTitle().toUpperCase().contains(episodeName.toUpperCase()))
                .findFirst();
    }

    private Map<Integer, Double> ratingForSeason(List<Episode> episodes) {
        return episodes.stream()
                .filter(episode -> episode.getRating() != null)
                .collect(Collectors.groupingBy(Episode::getSeason,
                        Collectors.averagingDouble(Episode::getRating)));
    }

    private void showSearchedSeries() {
        searchedSeries.stream()
                .sorted(Comparator.comparing(Serie::getGenre))
                .forEach(System.out::println);
    }
}

