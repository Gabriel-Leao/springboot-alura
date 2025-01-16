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
    @OneToMany(mappedBy = "serie", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<Episode> episodes = new ArrayList<>();

    public Serie() {}

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

    public String getTitle() {
        return title;
    }

    public int getTotalSeasons() {
        return totalSeasons;
    }

    public String getGenre() {
        return genre.toString().substring(0, 1).toUpperCase() + genre.toString().substring(1).toLowerCase();
    }

    public void setEpisodes(List<Episode> episodes) {
        episodes.forEach(episode -> episode.setSerie(this));
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
                ", Poster='" + poster + '\'' +
                ", Episódios=" + episodes;
    }
}
