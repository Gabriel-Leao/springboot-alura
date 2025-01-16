package br.com.alura.screenMatch.model;

import jakarta.persistence.*;

import java.time.LocalDate;
@Entity
@Table(name = "episodes")
public class Episode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column()
    private int season;
    @Column(nullable = false)
    private String title;
    @Column()
    private LocalDate releaseDate;
    @Column()
    private int episodeNumber;
    @Column()
    private Double rating;
    @ManyToOne
    @JoinColumn(name = "serie_id")
    private Serie serie;

    public Episode() {}

    public Episode(int season, EpisodeData episodeData) {
        this.season = season;
        this.title = episodeData.title();
        this.episodeNumber = episodeData.episodeNumber();

        try {
            this.rating = episodeData.rating().equals("N/A") ? null : Double.parseDouble(episodeData.rating());
        } catch (Exception e) {
            this.rating = null;
        }

        try {
            this.releaseDate = LocalDate.parse(episodeData.releaseDate());
        } catch (Exception e) {
            this.releaseDate = null;
        }
    }

    public void setSerie(Serie serie) {
        this.serie = serie;
    }

    public Double getRating() {
        return rating;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public int getSeason() {
        return season;
    }

    @Override
    public String toString() {
        return "Temporada=" + season +
                ", Título='" + title + '\'' +
                ", Lançamento=" + releaseDate +
                ", Número do episódio=" + episodeNumber +
                ", Nota=" + rating;
    }
}
