package br.com.alura.screenmatch.model;

import java.time.LocalDate;

public class Episode {
    private int season;
    private String title;
    private LocalDate releaseDate;
    private int episodeNumber;
    private Double rating;

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

    public Double getRating() {
        return rating;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public int getSeason() {
        return season;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return "season=" + season +
                ", title='" + title + '\'' +
                ", releaseDate=" + releaseDate +
                ", episodeNumber=" + episodeNumber +
                ", rating=" + rating;
    }
}
