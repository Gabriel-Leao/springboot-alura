package br.com.alura.screenMatch.model;

import br.com.alura.screenMatch.service.translation.QueryMyMemory;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;

@Entity
@Table(name = "series")
public class Serie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String title;
    @Column()
    private String synopsis;
    @Column()
    private String releaseDate;
    @Column()
    private Double rating;
    @Column()
    private int totalSeasons;
    @Column()
    @Enumerated(EnumType.STRING)
    private Category genre;
    @Column()
    private String actors;
    @Column()
    private String poster;
    @Transient
    private List<Episode> episodes = new ArrayList<>();

    public Serie(SerieData data) {
        this.title = data.title();
        this.synopsis = QueryMyMemory.getTranslation(data.synopsis()).trim();
        this.releaseDate = data.releaseDate();
        this.rating = OptionalDouble.of(data.rating()).orElse(0.0);
        this.totalSeasons = data.totalSeasons();
        this.genre = Category.fromString(data.genre().split(",")[0].trim());
        this.actors = data.actors();
        this.poster = data.poster();
    }

    public Serie() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public int getTotalSeasons() {
        return totalSeasons;
    }

    public void setTotalSeasons(int totalSeasons) {
        this.totalSeasons = totalSeasons;
    }

    public String getGenre() {
        return genre.toString().substring(0, 1).toUpperCase() + genre.toString().substring(1).toLowerCase();
    }

    public void setGenre(Category genre) {
        this.genre = genre;
    }

    public String getActors() {
        return actors;
    }

    public void setActors(String actors) {
        this.actors = actors;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public List<Episode> getEpisodes() {
        return episodes;
    }

    public void setEpisodes(List<Episode> episodes) {
        this.episodes = episodes;
    }

    @Override
    public String toString() {
        return "Título='" + title + '\'' +
                ", Sinopse='" + synopsis + '\'' +
                ", Gênero=" + getGenre()  +
                ", Lançamento='" + releaseDate + '\'' +
                ", Nota=" + rating +
                ", Temporadas=" + totalSeasons +
                ", Atores='" + actors + '\'' +
                ", Poster='" + poster + '\'';
    }
}
