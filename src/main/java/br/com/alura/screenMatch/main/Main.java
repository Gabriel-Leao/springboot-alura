package br.com.alura.screenMatch.main;

import br.com.alura.screenMatch.repository.SerieRepository;
import br.com.alura.screenMatch.utils.EnvUtil;
import br.com.alura.screenMatch.model.*;
import br.com.alura.screenMatch.service.ApiConsumer;
import br.com.alura.screenMatch.service.DataConverter;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class Main {
    private final Scanner scanner = new Scanner(System.in);
    private final ApiConsumer api = new ApiConsumer();
    private final String url = "https://www.omdbapi.com/?t=";
    private final String apiKey = "&apikey=" + EnvUtil.getEnv("API_KEY");
    private final DataConverter converter = new DataConverter();
    private final SerieRepository serieRepository;

    @Autowired
    public Main(SerieRepository serieRepository) { this.serieRepository = serieRepository; }

    public void applicationMenu () {
        int option = -1;

        while (option != 0) {
            System.out.println("Bem vindo ao ScreenMatch!");
            System.out.println("1 - Buscar série");
            System.out.println("2 - Buscar filme");
            System.out.println("3 - Buscar Episódio");
            System.out.println("4 - Listar séries buscadas");
            System.out.println("5 - Buscar série por titulo");
            System.out.println("6 - Buscar séries por ator");
            System.out.println("7 - Listar séries mais bem avaliadas");
            System.out.println("8 - Buscar séries por gênero");
            System.out.println("9 - Filtrar séries para maratonar");
            System.out.println("0 - Sair");
            System.out.print("Digite a opção desejada => ");
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
                    searchEpisodes();
                    break;
                case 4:
                    showSearchedSeries();
                    break;
                case 5:
                    Optional<Serie> serie = searchSerieByTitle();
                    serie.ifPresentOrElse(System.out::println, () -> System.out.println("Série não encontrada!"));
                    break;
                case 6:
                    List<Serie> actorSeries = searchSerieByActor();
                    System.out.println("Séries encontradas: ");
                    actorSeries.forEach(System.out::println);
                    break;
                case 7:
                    List<Serie> bestSeries = bestSeries();
                    System.out.println("Séries mais bem avaliadas: ");
                    bestSeries.forEach( s-> System.out.println(s.getTitle() + " - " + s.getRating()));
                    break;
                case 8:
                    List<Serie> genreSeries = searchSerieByGenre();
                    System.out.println("Séries encontradas: ");
                    genreSeries.forEach(System.out::println);
                    break;
                case 9:
                    System.out.println("Filtrar séries para maratonar");
                    System.out.print("Digite o número de temporadas: ");
                    int totalSeasons = Integer.parseInt(scanner.nextLine());
                    System.out.print("Digite a nota mínima: ");
                    double rating = Double.parseDouble(scanner.nextLine());
                    List<Serie> highRatingAndFewSeasons = filterSeries(totalSeasons, rating);
                    System.out.println("Séries encontradas: ");
                    highRatingAndFewSeasons.forEach(System.out::println);
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

    private String searchProduction(boolean serie) {
        System.out.print("Digite o nome da " + (serie ? "série" : "filme") + ": ");
        String productionName = scanner.nextLine();
        return api.getData(url + productionName.replace(" ", "+") + apiKey);
    }

    private void searchSerie() {
        SerieData serieData = converter.getData(searchProduction(true), SerieData.class);
        Serie serie = new Serie(serieData);
        System.out.println("Série encontrada: " + serie.getTitle());
        serieRepository.save(serie);
    }

    private void searchMovie() {
        MovieData movie = converter.getData(searchProduction(false), MovieData.class);
        System.out.println("Filme encontrado: " + movie.title() + " - (" + movie.releaseDate() + ")" + " - Diretor: " + movie.director());
    }

    private List<SeasonData> getSeasons(@NotNull Serie serie) {
        List<SeasonData> seasons = new ArrayList<>();
		for (int i = 1; i <= serie.getTotalSeasons(); i++) {
			String data = api.getData(url + serie.getTitle().replace(" ", "+") + "&Season=" + i + apiKey);
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

    private Optional<List<Episode>> findEpisodesBySerieName(String serieName) {
        Optional<Serie> serie = serieRepository.findByTitleContainingIgnoreCase(serieName);

        if (serie.isEmpty()) {
            System.out.println("Série não encontrada!");
            return Optional.empty();
        }

        Serie foundSerie = serie.get();
        List<SeasonData> seasons = getSeasons(foundSerie);
        List<Episode> episodes = convertSeasonsToEpisodes(seasons);

        foundSerie.setEpisodes(episodes);
        serieRepository.save(foundSerie);
        return Optional.of(episodes);
    }

    private Map<Integer, Double> ratingForSeason(List<Episode> episodes) {
        return episodes.stream()
                .filter(episode -> episode.getRating() != null)
                .collect(Collectors.groupingBy(Episode::getSeason,
                        Collectors.averagingDouble(Episode::getRating)));
    }

    private void showSearchedSeries() {
        List<Serie> series = serieRepository.findAll();
        series.stream()
                .sorted(Comparator.comparing(Serie::getGenre))
                .forEach(System.out::println);
    }

    private void showSerieEpisodes(Optional<List<Episode>> episodes) {
        if (episodes.isEmpty()) {
            System.out.println("Episódios dá série não encontrados!");
            return;
        }

        episodes.get().forEach(System.out::println);
    }

    private void searchEpisodes() {
        System.out.println("Estas são as séries buscadas: ");
        showSearchedSeries();
        System.out.print("De qual delas quer buscar os episódios: ");
        String serieName = scanner.nextLine();
        Optional<List<Episode>> episodes = findEpisodesBySerieName(serieName);
        showSerieEpisodes(episodes);
    }

    private Optional<Serie> searchSerieByTitle() {
        System.out.print("Digite o titulo da série: ");
        String serieTitle = scanner.nextLine();

        return serieRepository.findByTitleContainingIgnoreCase(serieTitle);
    }

    private List<Serie> searchSerieByActor() {
        System.out.print("Digite o nome do ator: ");
        String actorName = scanner.nextLine();

         return serieRepository.findByActorsContainingIgnoreCaseAndRatingGreaterThanEqual(actorName, 8.0);
    }

    private List<Serie> bestSeries() {
         return serieRepository.findTop5ByOrderByRatingDesc();
    }

    private List<Serie> searchSerieByGenre() {
        System.out.print("Digite o gênero desejado: ");
        Genre genre = Genre.fromPortuguese(scanner.nextLine());
        return serieRepository.findByGenre(genre);
    }

    private List<Serie> filterSeries(int totalSeasons, double rating) {
        return serieRepository.SeriesBySeasonAndRating(totalSeasons, rating);
    }
}
