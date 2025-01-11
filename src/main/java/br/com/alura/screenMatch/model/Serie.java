package br.com.alura.screenMatch.model;

import br.com.alura.screenMatch.service.translation.QueryMyMemory;

import java.util.OptionalDouble;

public class Serie {
    private String title;
    private String synopsis;
    private String releaseDate;
    private Double rating;
    private int totalSeasons;
    private Category genre;
    private String actors;
    private String poster;

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
